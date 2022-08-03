import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    permissions: [],
    apiAvailable: null,
    pbfUser: null,
    mspUser: null,
  }),
  getters: {
    hasPermission: (state) => {
      return (permission) => state.permissions.includes(permission)
    },
    hasAnyPermission: (state) => state.permissions.length > 0,
  },
})
