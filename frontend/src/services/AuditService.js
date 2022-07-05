import { apiRequest, resources } from './BaseService'

export default {
  getAuditReport(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.auditReport), request)
  },
}
