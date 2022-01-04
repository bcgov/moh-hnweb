import { apiRequest, resources } from './BaseService'

export default {

  updateEmployee(request) {
  
   console.log(`Update Empoyee: [PHN: ${request.phn}] [Group Number: ${request.groupNumber}][DEPARTMENT: ${request.departmentNumber}] [Group Member Number: ${request.groupMemberNumber}]`)
   // return apiRequest().then(axiosInstance => axiosInstance.post(resources.enrollment.person-details, request))
   return {
    phn: request.phn,       
    status: 'success',
    message: null
   }
  },

 

}