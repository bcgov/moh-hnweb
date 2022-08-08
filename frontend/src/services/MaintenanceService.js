import { apiRequest, resources } from './BaseService'

export default {
  changeEffectiveDate(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.changeEffectiveDate, request))
  },
  reinstateOverAgeDependent(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.reinstateOverAgeDependent, request))
  },
}