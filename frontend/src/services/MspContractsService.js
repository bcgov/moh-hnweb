import { apiRequest, resources } from './BaseService'

export default {
  getContractPeriods(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.mspContracts.getContractPeriods, request))
  },
  inquireContract(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.mspContracts.inquireContract, request))
  },
}
