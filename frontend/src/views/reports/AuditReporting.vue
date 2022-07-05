<template>
  <div id="viewAuditReport" v-if="searchMode">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.userId" id="userId" label="User ID:" type="text" v-model.trim="userId" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppSelect :e-model="v$.organization" id="organization" label="Organization" v-model="organization" :options="cancelReasons" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppSelect :e-model="v$.transactionType" id="transactionType" label="transaction Type" v-model="transactionType" :options="cancelReasons" />
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
import AuditService from '../../services/AuditService'
import useVuelidate from '@vuelidate/core'
import { required, helpers } from '@vuelidate/validators'
import { ORGANIZATION_CODE, TRANSACTION_TYPES } from '../../util/constants'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'auditReporting',
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  created() {
    // Organization Id drop down options
    this.organizationIdOptions = ORGANIZATION_CODE
    // Transaction Type drop down options
    this.transactionTypeOptions = TRANSACTION_TYPES
  },
  data() {
    return {
      userId: '',
      organization: '',
      transactionId: [],
      updateOk: false,
      searchMode: true,
      submitting: false,
      authors: ['Laravel', 'Laravel 5', 'Vue JS', 'ItSolutionStuff.com', 'HDTuto.com'],
      result: {
        auditReportResponse: '',
        status: '',
        message: '',
      },
      cancelReasons: [
        { text: 'Divorced', value: 'I' },
        { text: 'No longer a child', value: 'P' },
        { text: 'Out of province move', value: 'E' },
      ],
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
          await AuditService.getAuditReport({
            userId: this.userId,
            organization: this.organization,
            transactionType: this.transactionType,
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
      this.userId = ''
      this.organization = ''
      this.transactionType = {}
      this.result = null
      this.inputFormActive = true
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
  },
  validations() {
    return {
      userId: {
        required,
      },
      organization: {
        required,
      },
    }
  },
}
</script>
