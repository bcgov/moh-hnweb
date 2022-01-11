const state = {
  resident: {
    phn: '',
    givenName: '',
    secondName: '',
    surname: '',
    dateOfBirth: null,
    gender: '',
    telephone: '',
    address1: '',
    address2: '',
    address3: '',
    city: '',
    province: '',
    postalCode: '',
    mailingAddress1: '',
    mailingAddress2: '',
    mailingAddress3: '',
    mailingAddressCity: '',
    mailingAddressProvince: '',
    mailingAddressPostalCode: '',
    priorResidenceCode: '',
    otherProvinceHealthcareNumber: '',
  },
}

const mutations = {
  setResident(state, resident) {
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

const getters = {
  getResident: (state) => {
    return state.resident
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  getters,
}
