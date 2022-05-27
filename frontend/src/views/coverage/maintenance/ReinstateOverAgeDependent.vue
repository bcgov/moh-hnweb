<template>
  <div id="reinstateOverAgeDependent" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppRow>
          <AppCol class="col4">
            <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model="phn" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col4">
            <AppInput :e-model="v$.dependentPhn" id="dependentPhn" label="Dependent's PHN" type="text" v-model.trim="dependentPhn" />
          </AppCol>
        </AppRow>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.dependentDateOfBirth" id="dependentDateOfBirth" label="Dependent's Birth Date" v-model="dependentDateOfBirth" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <YesNoRadioButtonGroup :e-model="v$.isStudent" id="isStudent" label="Is this Dependent attending a Canadian Educational Institution?" v-model="isStudent">
            <template #tooltip>Click either Yes or No </template>
          </YesNoRadioButtonGroup>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppOutput label="If Yes, enter the expected date studies in Canada will be completed" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.studentEndDate" id="studentEndDate" label="Student End Date" v-model="studentEndDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <div id="confirmation" v-if="!inputFormActive">
    <br />
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Add Another Group Member's Dependent</AppButton>
  </div>
</template>

<script>
import YesNoRadioButtonGroup from '../../../components/ui/YesNoRadioButtonGroup.vue'
import useVuelidate from '@vuelidate/core'
import { helpers, required, requiredIf } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, validateGroupNumber, validatePHN } from '../../../util/validators'
import { API_DATE_FORMAT, RELATIONSHIPS } from '../../../util/constants'
import MaintenanceService from '../../../services/MaintenanceService'
import { useAlertStore } from '../../../stores/alert'
import AppCol from '../../../components/grid/AppCol.vue'

export default {
  name: 'AddDependent',
  components: { YesNoRadioButtonGroup, AppCol },
  setup() {
    const currentMonth = {
      month: new Date().getMonth(),
      year: new Date().getFullYear(),
    }

    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
      currentMonth,
    }
  },
  data() {
    return {
      inputFormActive: true,
      submitting: false,
      groupNumber: '',
      phn: '',
      dependentPhn: '',
      dependentDateOfBirth: null,
      relationship: '',
      isStudent: '',
      studentEndDate: null,
      result: {
        phn: '',
        status: '',
        message: '',
      },
    }
  },
  methods: {
    async submitForm() {
      this.submitting = true
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          console.log('isValid ' + isValid)
          this.showError()
          return
        }

        this.result = (
          await MaintenanceService.reinstateOverAgeDependent({
            groupNumber: this.groupNumber,
            phn: this.phn,
            dependentPhn: this.dependentPhn,
            dependentDateOfBirth: dayjs(this.dependentDateOfBirth).format(API_DATE_FORMAT),
            isStudent: this.isStudent,
            studentEndDate: this.studentEndDate ? dayjs(this.studentEndDate).format(API_DATE_FORMAT) : null,
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.inputFormActive = false
          this.alertStore.setSuccessAlert(this.result.message)
        }
      } catch (err) {
        this.alertStore.setErrorAlert(err)
      } finally {
        this.submitting = false
      }
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.result = {}
    },
    resetForm() {
      this.groupNumber = ''
      this.phn = ''
      this.dependentPhn = ''
      this.dependentDateOfBirth = ''
      this.isStudent = ''
      this.studentEndDate = null
      this.result = null
      this.inputFormActive = true
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
  },
  validations() {
    return {
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      dependentPhn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      dependentDateOfBirth: { required },
      isStudent: { required },
      studentEndDate: {
        requiredIfIsStudent: requiredIf(() => {
          return this.isStudent === 'Y'
        }),
      },
    }
  },
}
</script>
