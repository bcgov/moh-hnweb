import { apiRequest, resources } from './BaseService'

export default {

  updateNumberAndDept(request) {  
   return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.updateNumberAndDept, request)) 
  },
  cancelGroupMember(request) {
    console.log(`Update Empoyee: [PHN: ${request.phn}] [Group Number: ${request.groupNumber}][Cancel Date: ${request.coverageCancelDate}] [Cancel Reason: ${request.cancelReason}]`)
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.cancelGroupMember, request))
  },

}