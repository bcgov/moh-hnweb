import { apiRequest, resources } from './BaseService'

export default {
  getPersonDetails(request) {
    console.log(`getPersonDemographics: PHN: ${request.phn}`)
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.enrollment.personDetails, request))
  },

  registerResident(request) {
    console.log(`Service registerResident: [PHN: ${request.phn}] [Gender: ${request.gender}] [Group Number: ${request.groupNumber}] [Prior Residence: ${request.priorResidenceCode}]`)
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.enrollment.enrollSubscriber, request))
  },
}
