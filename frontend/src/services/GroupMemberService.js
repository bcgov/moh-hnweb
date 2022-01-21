import { apiRequest, resources } from './BaseService'

export default {

  updateNumberAndDept(request) {  
   return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.updateNumberAndDept, request)) 
  },
  cancelGroupMember(request) {
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.cancelGroupMember, request))
  },
  addGroupMember(request) {  
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.addGroupMemeber, request )) 
   },

}