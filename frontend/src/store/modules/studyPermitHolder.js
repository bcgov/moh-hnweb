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
    console.log(`resident: [${resident.phn}] [${resident.surname}]`)
    state.resident = resident
  },
  resetResident(state) {
    state.resident = {
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
