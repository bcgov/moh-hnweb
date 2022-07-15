import { apiRequest, resources } from './BaseService'

export default {
  getRegistrationHistory(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.patientRegistration.getRegistrationHistory, request))
  },
}
