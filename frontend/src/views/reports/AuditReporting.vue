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
          <AppLabel>Transaction Types</AppLabel>
          <div class="checkbox-wrapper">
            <label class="checkbox" :for="option.value" v-for="option in transactionOptions" :key="option.value">
              {{ option.value }}
              <input type="checkbox" :id="option.value" :value="option.value" v-model="transactionTypes" />
              <span class="checkmark"></span>
            </label>
          </div>
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
import useVuelidate from '@vuelidate/core'
import { required } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'
import AppLabel from '../../components/ui/AppLabel.vue'

export default {
  components: { AppLabel, AppSimpleTable, AuditReportRecords },
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
      endDate: null,
      startDate: null,
      organizationOptions: [],
      transactionTypes: [],
      searchOk: false,
      searchMode: true,
      submitting: false,
      result: {
        auditReports: [],
        message: '',
        status: '',
      },
      transactionOptions: [
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

<style scoped>
.checkbox-wrapper {
  height: 125px;
  width: 400px;
  overflow: auto;
  padding-left: 10px;
  padding-top: 10px;
}
.checkbox {
  display: block;
  position: relative;
  padding-left: 25px;
  margin-bottom: 12px;
  cursor: pointer;
  font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
  font-size: 16px;
  font-weight: 500;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

/* Hide the browser's default checkbox */
.checkbox input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}

/* Create a custom checkbox */
.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 16px;
  width: 16px;
  outline: 2px solid #606060;
}

/* When the checkbox is checked, add a blue background */
.checkbox input:checked ~ .checkmark {
  background-color: #606060;
}

/* Create the checkmark/indicator (hidden when not checked) */
.checkmark:after {
  content: '\2713';
  color: white;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: none;
}

/* Show the checkmark when checked */
.checkbox input:checked ~ .checkmark:after {
  display: block;
}
</style>
