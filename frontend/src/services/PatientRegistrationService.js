import { apiRequest, resources } from './BaseService'

export default {
  getPatientRegistration(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.patientRegistration.getPatientRegistration, request))
  },
  getBcscPayeeMapping(bcscUserId) {
    const url = `${resources.bcscPayeeMappings.getBcscPayeeMapping}/${bcscUserId}`;
    return apiRequest().then((axiosInstance) => axiosInstance.get(url))
  },
}
