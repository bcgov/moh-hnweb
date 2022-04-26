import { defineStore } from 'pinia'

export const useStudyPermitHolderStore = defineStore('studyPermitHolder', {
  state: () => ({
    resident: {},
  }),
})
