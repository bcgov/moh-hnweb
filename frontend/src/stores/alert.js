import { defineStore } from 'pinia'

import { DEFAULT_ERROR_MESSAGE } from '../util/constants.js'

export const useAlertStore = defineStore('alert', {
  state: () => ({
    message: '',
    type: 'success',
    active: false,
  }),
  getters: {},
  actions: {
    setAlert(alert) {
      this.message = alert.message
      this.type = alert.type
      this.active = true
    },
    setErrorAlert(message) {
      this.message = message || DEFAULT_ERROR_MESSAGE
      this.type = 'error'
      this.active = true
    },
    setErrorAlerts(messages) {
      const message = messages.join('\n')
      this.message = message
      this.type = 'error'
      this.active = true
    },
    setInfoAlert(message) {
      this.message = message
      this.type = 'info'
      this.active = true
    },
    setSuccessAlert(message) {
      this.message = message
      this.type = 'success'
      this.active = true
    },
    setWarningAlert(message) {
      this.message = message
      this.type = 'warning'
      this.active = true
    },
    dismissAlert(state) {
      this.active = false
    },
  },
})
