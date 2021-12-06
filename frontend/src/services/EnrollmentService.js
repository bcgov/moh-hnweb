import {apiRequest, resources} from './BaseService'

export default {

  getPersonDemographics(request) {
    console.log(`getPersonDemographics: PHN: ${request.phn}`)
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.enrollment.personDetails, request))
    // return {
    //   data: {
    //     person: {
    //       phn: request.phn,
    //       givenName: 'Test',	
    //       secondName: 'The',        
    //       surname: 'Data',
    //       dateOfBirth: '19780212',
    //     },
    //     status: 'success',
    //     message: null
    //   }
    // }
  },

  registerResident(request) {
    console.log(`Service registerResident: [PHN: ${request.phn}] [Group Number: ${request.groupNumber}] [Prior Residence: ${request.priorResidenceCode}]`)
    // return apiRequest().then(axiosInstance => axiosInstance.post(resources.enrollment.addVisaResidentWithPHN, request))
    return {
      data: {
        status: 'success',
        message: ''
      }
    }
  }
}