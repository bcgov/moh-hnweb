import { apiRequest, resources } from './BaseService'

export default {
  getContractPeriods(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.mspContracts.getContractPeriods, request))
  },
  getAuditReport(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.mspContracts.getAuditReport, request))
  },
  getOrganization() {
    return apiRequest().then((axiosInstance) => axiosInstance.get(resources.mspContracts.getOrganization))
  },
  inquireContract(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.mspContracts.inquireContract, request))
  },
  updateContractAddress(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.mspContracts.updateContractAddress, request))
  },
}
