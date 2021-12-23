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
    console.log(`resident: [${resident.phn}] [${resident.surname}]  [${resident.address1}]`)
    state.resident = resident
    console.log(`state.resident: [${state.resident.phn}] [${state.resident.surname}]  [${state.resident.address1}]`)
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
