import { apiRequest, resources } from './BaseService'

export default {

  updateNumberAndDept(request) {
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.updateNumberAndDept, request))
  },
  cancelGroupMember(request) {
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.cancelGroupMember, request))
  },
  cancelGroupMemberDependent(request) {
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.cancelGroupMemberDependent, request))
  },

}