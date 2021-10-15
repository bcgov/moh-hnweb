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
    state.message = message || 'Please correct errors before submitting'
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