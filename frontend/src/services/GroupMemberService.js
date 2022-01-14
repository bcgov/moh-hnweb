import { apiRequest, resources } from './BaseService'

export default {

  updateNumberAndDept(request) {  
   return apiRequest().then(axiosInstance => axiosInstance.post(resources.groupMember.updateNumberAndDept, request)) 
  },

}