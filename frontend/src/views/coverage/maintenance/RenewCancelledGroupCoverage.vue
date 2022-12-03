<template>
  <AppHelp>
    <p>Use this screen to renew a cancelled group member on your MSP group account. Dependents that were cancelled on the same date as the group member will automatically be renewed along with the group member.</p>
    <p>
      NOTE: this allows you to renew without having to enter the dependents' PHNs and the address. Please use the Contract Inquiry screen after you have renewed the coverage to make sure that the address on file is current. If it is not, you will need to submit an Update Group Members Contract
      Address.
    </p>
    <p>If you do not know the PHN, the PHN Lookup can be used to find the group member's PHN from their MSP Contract Number.</p>
    <p>If the transaction was successful, the group members PHN is displayed. This PHN may be used in the Contract Inquiry screen to verify that the coverage has been correctly renewed, and to check if the address is current.</p>
  </AppHelp>
  <div id="renewCancelledGroupCoverage" v-if="inputFormActive">
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
          <AppDateInput :e-model="v$.newCoverageEffectiveDate" id="newCoverageEffectiveDate" label="New Coverage Effective Date" v-model="newCoverageEffectiveDate" />
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
    <AppButton @click="resetForm" mode="primary" type="button">Renew Another Coverage Effective Date</AppButton>
  </div>
</template>

<script>
import MaintenanceService from '../../../services/MaintenanceService'
import useVuelidate from '@vuelidate/core'
import AppHelp from '../../../components/ui/AppHelp.vue'
import { validatePHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT } from '../../../util/constants'
import dayjs from 'dayjs'
import { useAlertStore } from '../../../stores/alert'
import { handleServiceError } from '../../../util/utils'

export default {
  name: 'RenewCancelledGroupCoverage',
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
      newCoverageEffectiveDate: dayjs().startOf('month').toDate(),
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
          await MaintenanceService.renewCancelledGroupCoverage({
            groupNumber: this.groupNumber,
            phn: this.phn,
            newCoverageEffectiveDate: dayjs(this.newCoverageEffectiveDate).format(API_DATE_FORMAT),
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
      this.newCoverageEffectiveDate = dayjs().startOf('month').toDate()
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
      newCoverageEffectiveDate: {
        required,
      },
    }
  },
}
</script>
