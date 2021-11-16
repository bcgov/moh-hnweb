import dayjs from 'dayjs'

import { API_DATE_FORMAT } from '../util/constants'
import { apiRequest, resources } from './BaseService'

export default {

  checkEligibility(request) {
    console.log(`checkEligibility: PHN: ${request.phn} Date: ${request.eligibilityDate}`)
    return apiRequest().then(axiosInstance => axiosInstance.post(resources.eligibility.checkEligibility, request))
  },

  checkCoverageStatus(phn, dateOfBirth, dateOfService, requestSubsidyInsuredService, requestLastEyeExam, requestPatientRestriction) {
    console.log(`checkStatusCoverage: PHN: ${phn}, Date Of Birth: ${dateOfBirth}, Date Of Service: ${dateOfService}, 
      requestSubsidyInsuredService: ${requestSubsidyInsuredService}, requestLastEyeExam: ${requestLastEyeExam}, requestPatientRestriction: ${requestPatientRestriction}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(
      `${resources.eligibility.checkCoverageStatus}?phn=${phn}&dateOfBirth=${dateOfBirth}&dateOfService=${dateOfService}
      &requestSubsidyInsuredService=${requestSubsidyInsuredService}&requestLastEyeExam=${requestLastEyeExam}&requestPatientRestriction=${requestPatientRestriction}`))
  }

}