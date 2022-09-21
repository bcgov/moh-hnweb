<template>
  <AppHelp>
    <p>Use the Change Effective Date screen to change the coverage effective date of an employee on your MSP group account. Any dependents with the same existing effective date will automatically have their effective dates changed along with the employee.</p>
    <p>NOTE - if you do not know the PHN, the PHN Lookup transaction can be used to find an employee's PHN from their MSP Contract Number.</p>
    <p>You must enter the effective date that is currently on file. Use the Get Contract Periods business service first to be sure you have the correct date.</p>
    <p>The employee's Personal Health Number is displayed. You may wish to use the PHN to verify, with the Get Contract Periods business service, that the effective date has been changed correctly.</p>
  </AppHelp>
  <div id="changeEffectiveDate" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model.trim="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.existingEffectiveDate" id="existingEffectiveDate" label="Existing Coverage Effective Date" v-model="existingEffectiveDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.newEffectiveDate" id="newEffectiveDate" label="New Coverage Effective Date" v-model="newEffectiveDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <div id="confirmation" v-else>
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Change Another Effective Date</AppButton>
  </div>
</template>

<script>
import AppHelp from '../../../components/ui/AppHelp.vue'
import MaintenanceService from '../../../services/MaintenanceService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT } from '../../../util/constants'
import dayjs from 'dayjs'
import { useAlertStore } from '../../../stores/alert'
import { handleServiceError } from '../../../util/utils'

export default {
  name: 'ChangeEffectiveDate',
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
      submitting: false,
      inputFormActive: true,
      phn: '',
      groupNumber: '',
      existingEffectiveDate: null,
      newEffectiveDate: null,
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
          this.showError()
          return
        }
        this.result = (
          await MaintenanceService.changeEffectiveDate({
            groupNumber: this.groupNumber,
            phn: this.phn,
            existingEffectiveDate: dayjs(this.existingEffectiveDate).format(API_DATE_FORMAT),
            newEffectiveDate: dayjs(this.newEffectiveDate).format(API_DATE_FORMAT),
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }
        if (this.result?.status === 'success') {
          this.inputFormActive = false
          this.alertStore.setSuccessAlert(this.result.message)
          return
        }

        this.alertStore.setAlert({ message: this.result.message, type: this.result.status })
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
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
      this.existingEffectiveDate = null
      this.newEffectiveDate = null
      this.result = null
      this.v$.$reset()
      this.inputFormActive = true
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
      existingEffectiveDate: {
        required,
      },
      newEffectiveDate: {
        required,
      },
    }
  },
}
</script>
