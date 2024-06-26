<template>
  <div id="viewAuditReport">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.userId" id="userId" label="User ID" type="text" v-model.trim="userId" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3 checkbox-row">
          <AppLabel class="checkbox-label">Organization</AppLabel>
          <AppInput @input="filterOrganizations($event)" id="organizationFilter" placeholder="Type org name or ID to filter list" type="text" v-model.trim="organizationFilter" />

          <div class="checkbox-wrapper">
            <label class="checkbox" :for="option.id" v-for="option in organizationOptionsFiltered" :key="option.id">
              {{ option.id }} {{ option.name ? ' - ' : '' }} {{ option.name }}
              <input type="checkbox" :id="option.id" :value="option.id" v-model="organizations" />
              <span class="checkmark"></span>
            </label>
          </div>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3 checkbox-row">
          <AppLabel class="checkbox-label">Transaction Types</AppLabel>
          <div class="checkbox-wrapper">
            <label class="checkbox" :for="option" v-for="option in transactionOptions" :key="option">
              {{ option }}
              <input type="checkbox" :id="option" :value="option" v-model="transactionTypes" />
              <span class="checkmark"></span>
            </label>
          </div>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3 checkbox-row">
          <AppLabel class="checkbox-label">SPG</AppLabel>
          <div class="checkbox-wrapper">
            <label class="checkbox" :for="option" v-for="option in spgOptions" :key="option">
              {{ option }}
              <input type="checkbox" :id="option" :value="option" v-model="spgRoles" />
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
          <AppDateInput :e-model="v$.endDate" id="endDate" label="End Date" v-model="endDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="downloadCSV" v-show="searchOk && result.records.length > 0">
    <AppDownloadLink href="#" :downloading="downloading" @click="downloadReport">Export To CSV</AppDownloadLink>
  </div>
  <div id="searchResults" v-show="searchOk && result.records.length > 0">
    <!-- && result?.records.length > 0 -->
    <DataTable
      id="resultsTable"
      :key="dataTableKey"
      :value="result.records"
      :totalRecords="result.totalRecords"
      :lazy="true"
      ref="dt"
      @page="onPage($event)"
      @sort="onSort($event)"
      stripedRows
      :paginator="true"
      :rows="10"
      :first="firstRecordIndex"
      :loading="loading"
      paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
      :rowsPerPageOptions="[5, 10, 20]"
      responsiveLayout="scroll"
      currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
    >
      <Column field="type" header="Type" :sortable="true"></Column>
      <Column field="organization" header="Organization" :sortable="true"></Column>
      <Column field="organizationName" header="Organization Name" :sortable="true"></Column>
      <Column field="spgRole" header="SPG" :sortable="true"></Column>
      <Column field="userId" header="User ID" :sortable="true" class="userId"></Column>
      <Column field="transactionStartTime" header="Transaction Start Time" :sortable="true"></Column>
      <Column field="affectedPartyId" header="Affected Party ID" :sortable="true"></Column>
      <Column field="affectedPartyType" header="Affected Party ID Type" :sortable="true"></Column>
      <Column field="transactionId" header="Transaction ID"></Column>
    </DataTable>
  </div>
</template>

<script>
import AuditService from '../../services/AuditService'
import AppDownloadLink from '../../components/ui/AppDownloadLink.vue'
import { API_DATE_TIME_FORMAT } from '../../util/constants'
import useVuelidate from '@vuelidate/core'
import { required, helpers } from '@vuelidate/validators'
import { DEFAULT_ERROR_MESSAGE } from '../../util/constants.js'
import { validateFutureDate, validateUserIdLength, VALIDATE_USER_ID_MESSAGE } from '../../util/validators'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'
import AppLabel from '../../components/ui/AppLabel.vue'
import dayjs from 'dayjs'
import isSameOrAfter from 'dayjs/plugin/isSameOrAfter'
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

const VALIDATE_END_DATE_MESSAGE = 'End Date must not be in the future'
const VALIDATE_START_DATE_MESSAGE = 'Start Date must not be in the future'

export default {
  components: { AppLabel, Column, DataTable, AppDownloadLink },
  name: 'AuditReporting',
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      userId: '',
      organizations: [],
      startDate: dayjs().subtract(1, 'month').startOf('month').toDate(),
      endDate: dayjs().subtract(1, 'month').endOf('month').toDate(),
      organizationOptions: [],
      organizationOptionsFiltered: [],
      organizationFilter: '',
      spgRoles: [],
      transactionTypes: [],
      searchOk: false,
      searching: false,
      downloading: false,
      result: {
        records: [],
        totalResults: 0,
        message: '',
        status: '',
      },
      auditReportRequest: {},
      dataTableKey: 0,
      firstRecordIndex: 0,
      loading: false,
      lazyParams: {},
      transactionOptions: [
        'AddDependent',
        'AddGroupMember',
        'CancelDependent',
        'CancelGroupMember',
        'ChangeCancelDate',
        'ChangeEffectiveDate',
        'CheckEligibility',
        'ContractInquiry',
        'EnrollSubscriber',
        'ExtendCancelDate',
        'GetContractAddress',
        'GetContractPeriods',
        'GetPatientRegistration',
        'GetPersonDetails',
        'MSPCoverageCheck',
        'NameSearch',
        'PHNInquiry',
        'PHNLookup',
        'ReinstateCancelledCoverage',
        'ReinstateOverAgeDependent',
        'RenewCancelledCoverage',
        'UpdateContractAddress',
        'UpdateNumberAndDept',
      ],
      spgOptions: ['AUDITUSER', 'E45', 'ELIGIBILITY', 'HELPDESK', 'MANAGEMSPPAYEENUMBER', 'PBFUSER', 'PREMIUMADMIN', 'PREMIUMADMINPLUS', 'SOCIALSECTOR', 'TRAININGHEALTHAUTH', 'VISARESIDENT'],
    }
  },
  mounted() {
    this.resetLazyParams()
  },
  methods: {
    async submitForm() {
      this.resetResult()
      this.searching = true
      this.searchOk = false
      this.alertStore.dismissAlert()

      const errors = []
      const isFormValid = await this.v$.$validate()
      if (!isFormValid) {
        errors.push(DEFAULT_ERROR_MESSAGE)
      }
      const isStartDateBeforeEndDate = this.validateDate()
      if (isFormValid && !isStartDateBeforeEndDate) {
        errors.push('Start Date should not be after End Date')
      }

      const isDateWithinRange = this.validateDateRange()

      if (isStartDateBeforeEndDate && !isDateWithinRange) {
        errors.push('End Date should not be more than 3 months from Start Date')
      }

      if (!isFormValid || !isStartDateBeforeEndDate || !isDateWithinRange) {
        this.showError(errors)
        return
      }

      this.loadLazyData()

      this.searchOk = true

      this.auditReportRequest.userId = this.userId
      this.auditReportRequest.organizations = this.organizations
      this.auditReportRequest.transactionTypes = this.transactionTypes
      this.auditReportRequest.startDate = this.startDate
      this.auditReportRequest.endDate = this.endDate
      this.auditReportRequest.spgRoles = this.spgRoles
    },
    async loadLazyData() {
      this.loading = true
      try {
        this.result = (
          await AuditService.getAuditReport({
            organizations: this.organizations,
            spgRoles: this.spgRoles,
            transactionTypes: this.transactionTypes,
            userId: this.userId,
            startDate: this.startDate,
            endDate: this.endDate,
            page: this.lazyParams.first / this.lazyParams.rows,
            rows: this.lazyParams.rows,
            sortField: this.lazyParams.sortField,
            sortDirection: this.lazyParams.sortOrder === 1 ? 'ASC' : 'DESC',
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        // Only display a message on the initial submit
        if (this.searching) {
          if (this.result.records.length > 0) {
            this.alertStore.setSuccessAlert('Transaction completed successfully')
          } else {
            this.alertStore.setErrorAlert('No results were returned. Please refine your search criteria and try again.')
          }
        }
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.loading = false
        this.searching = false
      }
    },
    filterOrganizations() {
      this.organizationOptionsFiltered = this.organizationOptions.filter((oo) => {
        let organizationDisplayName = oo.id + oo.name
        return organizationDisplayName.toLocaleLowerCase().includes(this.organizationFilter.toLocaleLowerCase())
      })
    },
    onPage(event) {
      this.lazyParams = event
      this.loadLazyData()
    },
    onSort(event) {
      this.lazyParams = event
      this.loadLazyData()
    },
    showError(errors) {
      this.alertStore.setErrorAlerts(errors)
      this.searching = false
    },
    async downloadReport() {
      this.downloading = true
      try {
        await AuditService.downloadAuditReport({
          organizations: this.auditReportRequest.organizations,
          spgRoles: this.auditReportRequest.spgRoles,
          transactionTypes: this.auditReportRequest.transactionTypes,
          userId: this.auditReportRequest.userId,
          startDate: this.auditReportRequest.startDate,
          endDate: this.auditReportRequest.endDate,
          sortField: this.lazyParams.sortField,
          sortDirection: this.lazyParams.sortOrder === 1 ? 'ASC' : 'DESC',
        }).then((response) => {
          this.exportToCSV(response.data)
        })
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.downloading = false
      }
    },
    exportToCSV(response) {
      const now = dayjs().format(API_DATE_TIME_FORMAT)
      const filename = 'auditreport_' + now + '.csv'

      const element = document.createElement('a')
      element.setAttribute('href', 'data:text/csv;charset=utf-8,' + response)
      element.setAttribute('download', filename)

      element.style.display = 'none'
      document.body.appendChild(element)

      element.click()
      document.body.removeChild(element)
    },
    resetForm() {
      this.userId = ''
      this.organizations = []
      this.spgRoles = []
      this.transactionTypes = []
      this.startDate = dayjs().subtract(1, 'month').startOf('month').toDate()
      this.endDate = dayjs().subtract(1, 'month').endOf('month').toDate()
      this.resetResult()
      this.searchOk = false
      this.searching = false
      this.v$.$reset()
      this.alertStore.dismissAlert()
      this.firstRecordIndex = 0
      // This is a workaround to ensure that the paginator is reset by forcing the component to reload
      // Technically this can be handled with firstRecordIndex but there appears to be an issue. See https://github.com/primefaces/primevue/issues/2253.
      this.dataTableKey++
      this.resetLazyParams()
      this.auditReportRequest = {}
    },
    resetLazyParams() {
      this.lazyParams = {
        first: 0,
        rows: this.$refs.dt.rows,
        sortField: null,
        sortOrder: null,
      }
    },
    resetResult() {
      this.result = {
        records: [],
        totalResults: 0,
        message: '',
        status: '',
      }
    },
    /**
     * Validates that End Date is after Start Date
     */
    validateDate() {
      dayjs.extend(isSameOrAfter)
      return dayjs(this.endDate).isSameOrAfter(this.startDate)
    },

    /**
     * Validates that End Date is not more than 3 months from Start Date
     */
    validateDateRange() {
      dayjs.extend(isSameOrBefore)
      return dayjs(this.endDate).isSameOrBefore(dayjs(this.startDate).add(3, 'month'))
    },
  },
  validations() {
    return {
      userId: {
        validateUserIdLength: helpers.withMessage(VALIDATE_USER_ID_MESSAGE, validateUserIdLength),
      },
      startDate: {
        required,
        validateFutureDate: helpers.withMessage(VALIDATE_START_DATE_MESSAGE, validateFutureDate),
      },
      endDate: {
        required,
        validateFutureDate: helpers.withMessage(VALIDATE_END_DATE_MESSAGE, validateFutureDate),
      },
    }
  },
  async created() {
    try {
      this.organizationOptions = (await AuditService.getOrganizations()).data
      this.organizationOptionsFiltered = this.organizationOptions
    } catch (err) {
      handleServiceError(err, this.alertStore, this.$router)
    }
  },
}
</script>

<style scoped>
.checkbox-row {
  padding-bottom: 10px;
}
.checkbox-label {
  padding-bottom: 5px;
}
.checkbox-wrapper {
  max-height: 125px;
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

<style>
.userId {
  max-width: 150px;
  word-wrap: break-word;
}
</style>
