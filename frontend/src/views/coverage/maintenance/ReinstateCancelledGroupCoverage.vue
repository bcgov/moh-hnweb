<template>
  <AppHelp>
    <p>Use this screen to reinstate a cancelled group member on your MSP group account. Any dependents with the same cancellation date will automatically be reinstated along with the group member.</p>
    <p>Before you begin, use the Get Contract Periods screen first to confirm you have the correct cancel date that is currently on file.</p>
    <p>If the transaction is successful, the PHN is displayed. You may use the PHN with the Get Contract Periods screen to verify the coverage has been correctly reinstated.</p>
  </AppHelp>
  <div id="reinstateCancelledGroupCoverage" v-if="inputFormActive">
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
          <AppDateInput :e-model="v$.cancelDateToBeRemoved" id="cancelDateToBeRemoved" label="Cancel Date to be Removed" v-model="cancelDateToBeRemoved" />
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
    <AppButton @click="resetForm" mode="primary" type="button">Reinstate Another Cancelled Coverage</AppButton>
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
  name: 'ReinstateCancelledGroupCoverage',
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
      cancelDateToBeRemoved: null,
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
          await MaintenanceService.reinstateCancelledGroupCoverage({
            groupNumber: this.groupNumber,
            phn: this.phn,
            cancelDateToBeRemoved: dayjs(this.cancelDateToBeRemoved).format(API_DATE_FORMAT),
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
      this.cancelDateToBeRemoved = null
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
      cancelDateToBeRemoved: {
        required,
      },
    }
  },
}
</script>
