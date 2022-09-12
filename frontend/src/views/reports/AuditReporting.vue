<template>
  <div id="viewAuditReport">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.userId" id="userId" label="User ID" type="text" v-model.trim="userId" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppLabel>Organization</AppLabel>
          <div class="checkbox-wrapper">
            <label class="checkbox" :for="option" v-for="option in organizationOptions" :key="option">
              {{ option }}
              <input type="checkbox" :id="option" :value="option" v-model="organizations" />
              <span class="checkmark"></span>
            </label>
          </div>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppLabel>Transaction Types</AppLabel>
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

  <div id="searchResults" v-if="searchOk && result.records.length > 0">
    <DownloadLink href="#" :downloading="downloading" @click="downloadReport">Export To CSV</DownloadLink>
    <div>
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
          <tr v-for="record in result.records">
            <AuditReportRecord :record="record" />
          </tr>
        </tbody>
      </AppSimpleTable>
    </div>
  </div>
</template>

<script>
import DownloadLink from '../../components/ui/DownloadLink.vue'
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import AuditReportRecord from '../../components/reports/AuditReportRecord.vue'
import AuditService from '../../services/AuditService'
import useVuelidate from '@vuelidate/core'
import { required } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'
import AppLabel from '../../components/ui/AppLabel.vue'
import dayjs from 'dayjs'

export default {
  components: { AppLabel, AppSimpleTable, AuditReportRecord, DownloadLink },
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
      organizations: [],
      endDate: new Date(),
      startDate: dayjs().subtract(1, 'month').toDate(),
      organizationOptions: [],
      transactionTypes: [],
      searchOk: false,
      searching: false,
      downloading: false,
      result: {
        records: [],
        message: '',
        status: '',
      },
      transactionOptions: ['CheckEligibility', 'PHNInquiry', 'PHNLookup', 'EnrollSubscriber', 'GetPersonDetails', 'NameSearch', 'AddGroupMember', 'AddDependent', 'UpdateNumberAndDept', 'CancelDependent', 'ContractInquiry', 'GetContractAddress', 'UpdateContractAddress'],
    }
  },
  methods: {
    async submitForm() {
      this.result = null
      this.searching = true
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
            organizations: this.organizations,
            transactionTypes: this.transactionTypes,
            userId: this.userId,
            startDate: this.startDate,
            endDate: this.endDate,
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }
        const maxResults = 1000
        if (this.result.records.length >= maxResults) {
          this.alertStore.setWarningAlert(`Maximum results (${maxResults}) returned. Please refine your search criteria and try again.`)
        } else if (this.result.records.length > 0) {
          this.alertStore.setSuccessAlert('Transaction completed successfully')
        } else {
          this.alertStore.setErrorAlert('No results were returned. Please refine your search criteria and try again.')
        }

        this.searchOk = true
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.searching = false
      }
    },
    async downloadReport() {
      this.downloading = true
      try {
        await AuditService.downloadAuditReport({
          organizations: this.organizations,
          transactionTypes: this.transactionTypes,
          userId: this.userId,
          startDate: this.startDate,
          endDate: this.endDate,
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
      const now = dayjs().format('YYYYMMDDhhmmss')
      let filename = 'auditreport_' + now + '.csv'

      let element = document.createElement('a')
      element.setAttribute('href', 'data:text/csv;charset=utf-8,' + response)
      element.setAttribute('download', filename)

      element.style.display = 'none'
      document.body.appendChild(element)

      element.click()
      document.body.removeChild(element)
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.result = {}
      this.searching = false
    },
    resetForm() {
      this.userId = ''
      this.organizations = []
      this.transactionTypes = []
      this.endDate = new Date()
      this.startDate = dayjs().subtract(1, 'month').toDate()
      this.result = null
      this.searchOk = false
      this.searching = false
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
      this.organizationOptions = (await AuditService.getOrganizations()).data
    } catch (err) {
      handleServiceError(err, this.alertStore, this.$router)
    }
  },
}
</script>

<style scoped>
.download {
  float: right;
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

#searchResults {
  overflow: auto;
  height: 800px;
}

#resultsTable {
}
</style>
