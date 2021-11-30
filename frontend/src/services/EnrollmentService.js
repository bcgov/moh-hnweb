import {apiRequest, resources} from './BaseService'

export default {

  getPersonDemographics(request) {
    console.log(`getPersonDemographics: PHN: ${request.phn}`)
    // return apiRequest().then(axiosInstance => axiosInstance.post(resources.enrollment.person-details, request))
    return {
      data: {
        person: {
          phn: request.phn,
          givenName: 'Test',	
          secondName: 'The',        
          surname: 'Data',
          dateOfBirth: '19780212',
        },
        status: 'success',
        message: null
      }
    }
  },

  registerResident(request) {
    console.log(`Service registerResident: [PHN: ${request.phn}] [Group Number: ${request.groupNumber}]`)
    // return apiRequest().then(axiosInstance => axiosInstance.post(resources.enrollment.addVisaResidentWithPHN, request))
    return {
      data: {
          person: {
          phn: request.phn,
          name: 'Reg Test Data',
        },
        status: 'success',
        message: null
      }
    }
  }
}