import { apiRequest, resources } from './BaseService'

export default {

  checkEligibility(request) {
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.eligibility.checkEligibility, request))
  },

  checkCoverageStatus(request) {
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.eligibility.checkMspCoverageStatus, request))
  }

}