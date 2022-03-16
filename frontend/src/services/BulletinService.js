import { apiRequest, resources } from './BaseService'

export default {
  getBulletins() {
    return apiRequest().then((axiosInstance) => axiosInstance.get(resources.bulletins))
  },
}
