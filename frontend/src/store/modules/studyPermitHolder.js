const state = {
  resident: {
    phn: '',
    givenName: '',
    secondName: '',
    surname: '',
    dateOfBirth: '',
    gender: '',
  },
}

const mutations = {
  setResident(state, resident) {
    state.resident = resident
  },
  resetResident(state) {
    resident = {
      phn: '',
      givenName: '',
      secondName: '',
      surname: '',
      dateOfBirth: '',
      gender: '',
    }
  },
}

export default {
  namespaced: true,
  state,
  mutations,
}
