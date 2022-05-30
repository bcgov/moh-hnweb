<template>
  <div>
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
  <div id="confirmation" v-if="updateOk">
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Change Another Effective Date</AppButton>
  </div>
</template>

<script>
import CoverageMaintenanceService from '../../../services/CoverageMaintenanceService'
import MspContractsService from '../../../services/MspContractsService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT } from '../../../util/constants'
import dayjs from 'dayjs'
import { useAlertStore } from '../../../stores/alert'

export default {
  name: 'ChangeEffectiveDate',

  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      submitting: false,
      updateOk: false,
      updateMode: true,
      phn: '',
      groupNumber: '',
      existingEffectiveDate: new Date(),
      newEffectiveDate: new Date(),
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
      this.updateOk = false
      this.updateMode = true
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (
          await CoverageMaintenanceService.changeEffectiveDate({
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
          this.updateMode = false
          this.updateOk = true
          this.alertStore.setSuccessAlert(this.result.message)
          return
        }

        this.alertStore.setAlert({ message: this.result.message, type: this.result.status })
      } catch (err) {
        this.alertStore.setErrorAlert(err)
        this.submitting = false
      } finally {
        this.submitting = false
      }
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.result = {}
      this.searching = false
    },
    resetForm() {
      this.groupNumber = ''
      this.phn = ''
      this.existingEffectiveDate = new Date()
      this.newEffectiveDate = new Date()
      this.result = null
      this.v$.$reset()
      this.alertStore.dismissAlert()
      this.searchOk = false
      this.searching = false
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
