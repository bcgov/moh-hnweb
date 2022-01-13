import { DEFAULT_ERROR_MESSAGE } from '../../util/constants.js'

const state = {
  message: '',
  type: 'success',
  active: false
}

const mutations = {
  setAlert(state, alert) {
    state.message = alert.message
    state.type = alert.type
    state.active = true
  },
  setErrorAlert(state, message) {
    state.message = message || DEFAULT_ERROR_MESSAGE
    state.type = 'error'
    state.active = true
  },
  setErrorAlerts(state, messages) {
    const message = messages.join('\n')
    state.message = message
    state.type = 'error'
    state.active = true
  },
  setInfoAlert(state, message) {
    state.message = message
    state.type = 'info'
    state.active = true
  },
  setSuccessAlert(state, message) {
    state.message = message
    state.type = 'success'
    state.active = true
  },
  setWarningAlert(state, message) {
    state.message = message
    state.type = 'warning'
    state.active = true
  },
  dismissAlert(state) {
    state.active = false
  }
}

export default {
  namespaced: true,
  state,
  mutations
}