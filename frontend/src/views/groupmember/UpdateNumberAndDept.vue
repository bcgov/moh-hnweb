<template>
  <AppHelp>
    <p>Use this screen to add, change or delete the Group Member Number or Department Number recorded for an group member.</p>
    <p>The Contract Inquiry shows you the Group Member and Department Numbers on your Group Member's contracts.</p>
    <p>Group Number and Group Member PHN are required fields. At least one of Group Member Number or Department Number must be populated.</p>
    <p>To ADD or CHANGE an Group Member Number, enter the new number of up to nine letters or numbers.</p>
    <p>To DELETE a Group Member Number, enter 6 or more dashes ( "------") in the field.</p>
    <p>To ADD or CHANGE a Department Number, enter the new number of up to six letters or numbers. To DELETE a Department Number, enter 6 dashes ( "------" ) in the field.</p>
  </AppHelp>
  <div id="updateNumberAndDept" v-if="searchMode">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="Group Member's PHN" type="text" v-model="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number" type="text" v-model="departmentNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="confirmation" v-if="updateOk">
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Update Another Group Member</AppButton>
  </div>
</template>

<script>
import GroupMemberService from '../../services/GroupMemberService'
import AppHelp from '../../components/ui/AppHelp.vue'
import useVuelidate from '@vuelidate/core'
import { validateGroupNumber, validateGroupMemberNumber, validateDepartmentNumber, validatePHN, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE, VALIDATE_DEPARTMENT_NUMBER_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { DEFAULT_ERROR_MESSAGE } from '../../util/constants.js'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'updateNumberAndDept',
  components: {
    AppHelp,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      groupNumber: '',
      phn: '',
      groupMemberNumber: '',
      departmentNumber: '',
      updateOk: false,
      searchMode: true,
      submitting: false,
      result: {
        phn: '',
        status: '',
        message: '',
      },
    }
  },
  methods: {
    async submitForm() {
      this.result = null
      this.submitting = true
      this.updateOk = false
      this.searchMode = true
      this.alertStore.dismissAlert()
      try {
        const errors = []
        const isFormValid = await this.v$.$validate()
        if (!isFormValid) {
          errors.push(DEFAULT_ERROR_MESSAGE)
        }

        const isMemberNumberAndDeptValid = this.validateMemberNumberAndDeptNumber()
        if (!isMemberNumberAndDeptValid) {
          errors.push('Group Member Number and/or Department Number is required')
        }

        if (!isFormValid || !isMemberNumberAndDeptValid) {
          this.showError(errors)
          return
        }

        this.result = (
          await GroupMemberService.updateNumberAndDept({
            phn: this.phn,
            groupNumber: this.groupNumber,
            departmentNumber: this.departmentNumber,
            groupMemberNumber: this.groupMemberNumber,
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.searchMode = false
          this.updateOk = true
          this.alertStore.setInfoAlert(this.result.message)
          return
        }
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.submitting = false
      }
    },
    updateAnotherGroupMember() {
      this.$router.go()
    },
    showError(errors) {
      this.alertStore.setErrorAlerts(errors)
      this.result = {}
      this.submitting = false
    },
    resetForm() {
      this.phn = ''
      this.groupMemberNumber = ''
      this.groupNumber = ''
      this.departmentNumber = ''
      this.result = null
      this.updateOk = false
      this.searchMode = true
      this.submitting = false
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
    validateMemberNumberAndDeptNumber() {
      return this.groupMemberNumber !== '' || this.departmentNumber !== ''
    },
  },
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
      groupMemberNumber: {
        validateGroupMemberNumber: helpers.withMessage(VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE, validateGroupMemberNumber),
      },
      departmentNumber: {
        validateDepartmentNumber: helpers.withMessage(VALIDATE_DEPARTMENT_NUMBER_MESSAGE, validateDepartmentNumber),
      },
    }
  },
}
</script>
