import { defineStore } from 'pinia'

export const useAuditReportStore = defineStore('auditReport', {
  state: () => ({
    organizations: [],
    transactionTypes: [],
    userId: '',
    startDate: '',
    endDate: '',
  }),
})
