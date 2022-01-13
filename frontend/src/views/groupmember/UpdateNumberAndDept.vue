<template>
  <div id="updateNumberAndDept" v-if="searchMode">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="Group Member's PHN" type="text" v-model="phn"/>
        </AppCol>
      </AppRow>
       <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number" type="text" v-model="departmentNumber"/>
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
import useVuelidate from '@vuelidate/core'
import { validateGroupNumber, validatePHN, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { DEFAULT_ERROR_MESSAGE } from '../../util/constants.js'

export default {
  name: 'updateNumberAndDept',
  setup() {
    return {
      v$: useVuelidate()}
  },
  data() {
    return {
      groupNumber:'',
      phn: '',
      groupMemberNumber: '',
      departmentNumber: '',
      updateOk: false, 
      searchMode : true,
      submitting: false,
      result: {
        phn: '', 
        status: '',
        message: '',
      }
    }
  },
  methods: {
    async submitForm() {
      this.result = null 
      this.submitting = true
      this.updateOk = false
      this.searchMode = true
      this.$store.commit("alert/dismissAlert")
      try {
        const errors = []
        const isFormValid = await this.v$.$validate()
        if (!isFormValid) {
          errors.push(DEFAULT_ERROR_MESSAGE)
        }

        const isMemberNumberAndDeptValid = this.validateMemberNumberAndDeptNumber();
        if (!isMemberNumberAndDeptValid) {
          errors.push('Group Member Number and/or Department Number is required')
        }

        if (!isFormValid || !isMemberNumberAndDeptValid) {
          this.showError(errors)
          return
        } 
       
        this.result = (await GroupMemberService.updateNumberAndDept({
          phn: this.phn,
          groupNumber: this.groupNumber,
          departmentNumber: this.departmentNumber,
          groupMemberNumber: this.groupMemberNumber,         
        })).data  
        
        if (this.result.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.searchMode = false
          this.updateOk = true
          this.$store.commit('alert/setSuccessAlert', this.result.message)
          return
        }
      
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.submitting = false
      }
    },
    updateAnotherGroupMember() {
      this.$router.go()
    },
    showError(errors) {
      this.$store.commit('alert/setErrorAlerts', errors)
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
      this.$store.commit("alert/dismissAlert") 
    },
    validateMemberNumberAndDeptNumber() {
      return this.groupMemberNumber !== '' || this.departmentNumber !== ''
    },
  },  
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN)
      },
      groupNumber: { 
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber)
      },
      groupMemberNumber: {},
      departmentNumber: {},
   
    }
  }
}
</script>