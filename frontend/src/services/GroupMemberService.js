import { apiRequest, resources } from './BaseService'

export default {
  addGroupMember(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.groupMember.addGroupMember, request))
  },
  addDependent(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.groupMember.addDependent, request))
  },
  updateNumberAndDept(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.groupMember.updateNumberAndDept, request))
  },
  cancelGroupMember(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.groupMember.cancelGroupMember, request))
  },
  cancelDependent(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.groupMember.cancelDependent, request))
  },
}
