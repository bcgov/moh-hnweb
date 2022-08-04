import { apiRequest, resources } from './BaseService'

export default {
  reinstateOverAgeDependent(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.maintenance.reinstateOverAgeDependent, request))
  },
}
