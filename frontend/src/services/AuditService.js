import { apiRequest, resources } from './BaseService'

export default {
  getAuditReport(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.reports.getAuditReport, request))
  },
  getOrganization() {
    return apiRequest().then((axiosInstance) => axiosInstance.get(resources.reports.getOrganizations))
  },
}
