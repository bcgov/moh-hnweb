import { apiRequest, resources } from './BaseService'

export default {
  getPatientRegistration(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.patientRegistration.getPatientRegistration, request))
  },
  getUserPayeeMapping(userId) {
    const url = `${resources.userPayeeMapping.getUserPayeeMapping}/${userId}`
    return apiRequest().then((axiosInstance) => axiosInstance.get(url))
  },
}
