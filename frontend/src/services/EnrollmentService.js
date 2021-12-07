import {apiRequest, resources} from './BaseService'

export default {

  getPersonDemographics(request) {
    console.log(`getPersonDemographics: PHN: ${request.phn}`)
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.enrollment.personDetails, request))
  },

  registerResident(request) {
    console.log(`Service registerResident: [PHN: ${request.phn}] [Group Number: ${request.groupNumber}] [Prior Residence: ${request.priorResidenceCode}]`)
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.enrollment.addVisaResidentWithPHN, request))
  }
}