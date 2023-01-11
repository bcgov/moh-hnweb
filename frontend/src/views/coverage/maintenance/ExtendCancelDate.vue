<template>
  <AppHelp>
    <p>
      Use this screen to extend the coverage cancellation date of an international student on your MSP group account based on their renewed study permit. Any dependents with the same existing cancellation date will automatically have their cancel dates changed along with the study permit holder.
    </p>
    <p>If you do not know the PHN, the PHN Lookup screen can be used to find a group member’s PHN.</p>
    <p>If the transaction is successful, the group members PHN is displayed. You may use the PHN with the Get Contract Periods screen to verify the cancellation date has been correctly changed.</p>
  </AppHelp>
  <div id="extendCancelDate" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="Group Member's PHN" type="text" v-model.trim="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.permitIssueDate" id="permitIssueDate" label="Permit Issue Date" v-model="permitIssueDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.permitExpiryDate" id="permitExpiryDate" label="Permit Expiry Date" v-model="permitExpiryDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppSelect :e-model="v$.immigrationCode" id="immigrationCode" label="Immigration Code" v-model="immigrationCode" :options="immigrationCodeOptions" />
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
    <AppButton @click="resetForm" mode="primary" type="button">Extend Another Cancellation Date</AppButton>
  </div>
</template>

<script>
import MaintenanceService from '../../../services/MaintenanceService'
import AppHelp from '../../../components/ui/AppHelp.vue'
import useVuelidate from '@vuelidate/core'
import { validatePHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT, IMMIGRATION_CODES } from '../../../util/constants'
import dayjs from 'dayjs'
import { useAlertStore } from '../../../stores/alert'
import { handleServiceError } from '../../../util/utils'

export default {
  name: 'ExtendCancelDate',
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
      immigrationCode: '',
      immigrationCodeOptions: {},
      permitIssueDate: null,
      permitExpiryDate: null,
      result: {
        phn: '',
        status: '',
        message: '',
      },
    }
  },

  created() {
    // Immigration Code drop down options
    this.immigrationCodeOptions = IMMIGRATION_CODES
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
          await MaintenanceService.extendCancelDate({
            groupNumber: this.groupNumber,
            phn: this.phn,
            existingCancellationDate: dayjs(this.existingCancellationDate).format(API_DATE_FORMAT),
            newCancellationDate: dayjs(this.newCancellationDate).format(API_DATE_FORMAT),
            immigrationCode: this.immigrationCode,
            permitIssueDate: dayjs(this.permitIssueDate).format(API_DATE_FORMAT),
            permitExpiryDate: dayjs(this.permitExpiryDate).format(API_DATE_FORMAT),
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
      this.immigrationCode = ''
      this.permitIssueDate = null
      this.permitExpiryDate = null
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
      immigrationCode: {
        required,
      },
      permitIssueDate: {
        required,
      },
      permitExpiryDate: {
        required,
      },
    }
  },
}
</script>
