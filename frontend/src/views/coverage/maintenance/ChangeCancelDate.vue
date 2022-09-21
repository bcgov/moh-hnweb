<template>
  <AppHelp>
    <p>Use the Change Cancel Date screen to change the coverage cancel date of an employee on your MSP group account. Any dependents with the same existing cancel date will automatically have their cancel dates changed along with the employee.</p>
    <p>NOTE - if you do not know the PHN, the PHN Lookup transaction can be used to find an employee's PHN from their MSP Contract Number.</p>
    <p>You must enter the cancel date that is currently on file. Use the Get Contract Periods business service first to be sure you have the correct date.</p>
    <p>The employee's Personal Health Number is displayed. You may wish to use the PHN to verify, with the Get Contract Periods business service, that the cancel date has been changed correctly.</p>
  </AppHelp>
  <div id="changeCancelDate" v-if="inputFormActive">
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
          <AppDateInput :e-model="v$.existingCancellationDate" id="existingCancellationDate" label="Existing Cancellation Date" v-model="existingCancellationDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.newCancellationDate" id="newCancellationDate" label="New Cancellation Date" v-model="newCancellationDate" />
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
    <AppButton @click="resetForm" mode="primary" type="button">Change Another Cancellation Date</AppButton>
  </div>
</template>

<script>
import MaintenanceService from '../../../services/MaintenanceService'
import AppHelp from '../../../components/ui/AppHelp.vue'
import useVuelidate from '@vuelidate/core'
import { validatePHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT } from '../../../util/constants'
import dayjs from 'dayjs'
import { useAlertStore } from '../../../stores/alert'
import { handleServiceError } from '../../../util/utils'

export default {
  name: 'ChangeCancelDate',

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
      existingCancellationDate: null,
      newCancellationDate: null,
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
          await MaintenanceService.changeCancelDate({
            groupNumber: this.groupNumber,
            phn: this.phn,
            existingCancellationDate: dayjs(this.existingCancellationDate).format(API_DATE_FORMAT),
            newCancellationDate: dayjs(this.newCancellationDate).format(API_DATE_FORMAT),
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
      this.existingCancellationDate = null
      this.newCancellationDate = null
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
      existingCancellationDate: {
        required,
      },
      newCancellationDate: {
        required,
      },
    }
  },
}
</script>
