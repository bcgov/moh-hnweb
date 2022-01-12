<template>
  <div id = 'updateGroupMember' v-if= "showForm">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn"  id="phn" label="Group Member's PHN" type="text" v-model="phn"/>
        </AppCol>
      </AppRow>
       <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number"   type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.departmentNumber" vi  id="departmentNumber" label="Department Number"   type="text" v-model="departmentNumber"/>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id = "confirmation" v-if="updateOk">
    <p>PHN: {{ result?.phn }}</p>  
  </div>
</template>

<script>
import GroupMemberService from '../../services/GroupMemberService'
import useVuelidate from '@vuelidate/core'
import { validateGroupNumber, validatePHN, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { required,requiredUnless, helpers } from '@vuelidate/validators'

export default {
  name: 'UpdateGroupMember',
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
      showForm : true,
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
      this.updateOk = false
      this.showForm = true
      this.$store.commit("alert/dismissAlert")
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        } 
        this.result = (await GroupMemberService.updateGroupMember({
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
          this.showForm = false
          this.updateOk = true
          this.$store.commit('alert/setSuccessAlert', this.result.message)
          return
        }
      
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
    showError(error) {
      this.$store.commit('alert/setErrorAlert', error)
      this.result = {}
    },
    resetForm() {
      this.phn = ''
      this.groupMemberNumber = ''
      this.groupNumber = ''
      this.departmentNumber = ''
      this.result = null
      this.updateOk = false
      this.showForm = true
      this.v$.$reset()
      this.$store.commit("alert/dismissAlert") 
    }
  },
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validatePHN
        )
      },
      groupNumber: { required,
      validateGroupNumber: helpers.withMessage(
        VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber
      )
      },
     groupMemberNumber: {
      requiredIf: requiredUnless(function() {
        return (
          this.groupMemberNumber !== '' || this.departmentNumber !== ''
        )
      })
    },
    departmentNumber: {
      requiredIf: requiredUnless(function() {
        return (
          this.departmentNumber !== '' || this.groupMemberNumber !== ''
        )
      })
    },
    }
  }
}
</script>
