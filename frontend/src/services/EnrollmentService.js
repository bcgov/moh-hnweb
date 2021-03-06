import { apiRequest, resources } from './BaseService'

export default {
  performNameSearch(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.enrollment.nameSearch, request))
  },

  getPersonDetails(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.enrollment.getPersonDetails, request))
  },

  registerResident(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.enrollment.enrollSubscriber, request))
  },
}
