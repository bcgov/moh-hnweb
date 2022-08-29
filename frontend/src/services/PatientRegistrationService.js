import { apiRequest, resources } from './BaseService'

export default {
  getPatientRegistration(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.patientRegistration.getPatientRegistration, request))
  },
}
