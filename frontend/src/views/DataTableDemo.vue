<template>
  <DataTable
    :value="result.records"
    :totalRecords="result.totalRecords"
    :lazy="true"
    ref="dt"
    @page="onPage($event)"
    stripedRows
    :paginator="true"
    :rows="10"
    paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
    :rowsPerPageOptions="[5, 10, 20]"
    responsiveLayout="scroll"
    currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
  >
    <Column field="type" header="Type"></Column>
    <Column field="organization" header="Organization"></Column>
    <Column field="userId" header="User ID"></Column>
    <Column field="transactionStartTime" header="Transaction Start Time"></Column>
    <Column field="affectedPartyId" header="Affected Party ID"></Column>
    <Column field="affectedPartyType" header="Affected Party ID Type"></Column>
    <Column field="transactionId" header="Transaction ID"></Column>
  </DataTable>
</template>
<script>
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

import AuditService from '../services/AuditService'
import { handleServiceError } from '../util/utils'

import dayjs from 'dayjs'
export default {
  name: 'DataTableDemo',
  components: { DataTable, Column },
  data() {
    return {
      loading: false,
      result: {
        records: [],
        totalResults: 0,
        message: '',
        status: '',
      },
      lazyParams: {},
    }
  },
  async mounted() {
    this.loading = true

    this.lazyParams = {
      first: 0,
      rows: this.$refs.dt.rows,
      sortField: null,
      sortOrder: null,
      filters: this.filters,
    }

    this.loadLazyData()
  },
  methods: {
    async loadLazyData() {
      this.loading = true
      try {
        this.result = (
          await AuditService.getAuditReport({
            organizations: [],
            transactionTypes: [],
            userId: 'wkubo@idir',
            startDate: dayjs().subtract(1, 'month').toDate(),
            endDate: new Date(),
            first: this.lazyParams.first,
            rows: this.lazyParams.rows,
          })
        ).data
      } catch (err) {
        //handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.loading = false
      }
    },
    onPage(event) {
      this.lazyParams = event
      this.loadLazyData()
    },
  },
}
</script>

<style>
.p-datatable {
  font-family: 'BCSans', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-weight: normal;
  border-collapse: collapse;
  border-radius: 5px 5px 0 0;
  margin: 25px 0;
  min-width: 400px;
  overflow: hidden;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}
.p-datatable .p-datatable-thead > tr > th {
  background-color: #38598a;
  color: #ffffff;
}
</style>
