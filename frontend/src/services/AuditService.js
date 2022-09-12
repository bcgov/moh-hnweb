import { apiRequest, resources } from './BaseService'

export default {
  getAuditReport(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.reports.getAuditReport, request))
  },
  getOrganizations() {
    return apiRequest().then((axiosInstance) => axiosInstance.get(resources.reports.getOrganizations))
  },
  downloadAuditReport(request) {
    return apiRequest().then((axiosInstance) => axiosInstance.post(resources.reports.downloadAuditReport, request))
  },
}
