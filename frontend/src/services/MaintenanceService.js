import { apiRequest, resources } from './BaseService'

export default {
  changeEffectiveDate(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.changeEffectiveDate, request))
  },
  changeCancelDate(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.changeCancelDate, request))
  },
  reinstateCancelledGroupCoverage(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.reistateCancelledGroupCoverage, request))
  },
  reinstateOverAgeDependent(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.reinstateOverAgeDependent, request))
  },
  renewCancelledGroupCoverage(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.renewCancelledGroupCoverage, request))
  },
  extendCancelDate(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.extendCancelDate, request))
  },
}
