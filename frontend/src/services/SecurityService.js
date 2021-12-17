import { apiRequest, resources } from './BaseService'

export default {
  getPermissions() {
    return apiRequest().then((axiosInstance) => axiosInstance.get(resources.security.permissions))
  },
}
