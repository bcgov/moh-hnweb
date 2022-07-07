<template>
  <div id="viewAuditReport">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.userId" id="userId" label="User ID:" type="text" v-model.trim="userId" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppSelect :e-model="v$.organization" id="organization" label="Organization" v-model="organization" :options="organizationOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppSelect :e-model="v$.transactionType" id="transactionType" label="transaction Type" v-model="transactionType" :options="transactionTypes" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.startDate" id="startDate" label="Start Date" v-model="startDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.endDate" id="dateOfBirth" label="End Date" v-model="endDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />

  <div id="searchResult" v-if="searchOk && result.auditReports.length > 0">
    <AppSimpleTable id="resultsTable">
      <thead>
        <tr>
          <th>Type</th>
          <th>Organization</th>
          <th>User ID</th>
          <th>Transaction Start Time</th>
          <th>Affected Party ID</th>
          <th>Affected Party ID Type</th>
          <th>Transaction ID</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="auditReport in result.auditReports">
          <AuditReportRecords :auditReport="auditReport" />
        </tr>
      </tbody>
    </AppSimpleTable>
  </div>
</template>

<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import AuditReportRecords from '../../components/reports/AuditReportRecords.vue'
import AuditService from '../../services/AuditService'
import MspContractsService from '../../services/MspContractsService'
import useVuelidate from '@vuelidate/core'
import { required } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  components: { AppSimpleTable, AuditReportRecords },
  name: 'auditReporting',
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      userId: '',
      organization: '',
      endDate: '',
      startDate: '',
      organizationOptions: [],
      transactionType: '',
      searchOk: false,
      searchMode: true,
      result: {
        auditReports: [],
        message: '',
        status: '',
      },
      transactionTypes: [
        { text: 'CheckEligibility', value: 'CheckEligibility' },
        { text: 'PHNInquiry', value: 'PHNInquiry' },
        { text: 'PHNLookup', value: 'PHNLookup' },
        { text: 'EnrollSubscriber', value: 'EnrollSubscriber' },
        { text: 'GetPersonDetails', value: 'GetPersonDetails' },
        { text: 'NameSearch', value: 'NameSearch' },
        { text: 'AddGroupMember', value: 'AddGroupMember' },
        { text: 'AddDependent', value: 'AddDependent' },
        { text: 'UpdateNumberAndDept', value: 'UpdateNumberAndDept' },
        { text: 'CancelDependent', value: 'CancelDependent' },
        { text: 'ContractInquiry', value: 'ContractInquiry' },
        { text: 'GetContractAddress', value: 'GetContractAddress' },
        { text: 'UpdateContractAddress', value: 'UpdateContractAddress' },
      ],
    }
  },
  methods: {
    async submitForm() {
      this.result = null
      this.searchOk = false
      this.alertStore.dismissAlert()

      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }

        this.result = (
          await AuditService.getAuditReport({
            organization: this.organization,
            transactionType: this.transactionType,
            userId: this.userId,
            startDate: this.startDate,
            endDate: this.endDate,
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        if (this.result.auditReports.length > 0) {
          this.result.message = 'Transaction completed successfully'
        } else {
          this.result.message = 'No results were returned. Please refine your search criteria, and try again.'
        }

        this.searchOk = true
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
      this.userId = ''
      this.organization = ''
      this.transactionType = ''
      this.startDate = ''
      this.endDate = ''
      this.result = null
      this.searchOk = false
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
  },
  validations() {
    return {
      userId: {
        required,
      },
      startDate: {
        required,
      },
      endDate: {
        required,
      },
    }
  },
  async created() {
    try {
      this.organizationOptions = (await AuditService.getOrganization()).data
    } catch (err) {
      handleServiceError(err, this.alertStore, this.$router)
    }
  },
}
</script>
