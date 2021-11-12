import dayjs from 'dayjs'

import { API_DATE_FORMAT } from '../util/constants'
import { apiRequest, resources } from './BaseService'

export default {

  checkEligibility(phn, eligibilityDate) {
    const formattedEligibilityDate = dayjs(eligibilityDate).format(API_DATE_FORMAT)
    console.log(`checkEligibility: PHN: ${phn} Date: ${formattedEligibilityDate}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(`${resources.eligibility.checkEligibility}?phn=${phn}&eligibilityDate=${formattedEligibilityDate}`))
  },

  checkCoverageStatus(phn, dateOfBirth, dateOfService, requestSubsidyInsuredService, requestLastEyeExam, requestPatientRestriction) {
    console.log(`checkStatusCoverage: PHN: ${phn}, Date Of Birth: ${dateOfBirth}, Date Of Service: ${dateOfService}, 
      requestSubsidyInsuredService: ${requestSubsidyInsuredService}, requestLastEyeExam: ${requestLastEyeExam}, requestPatientRestriction: ${requestPatientRestriction}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(
      `${resources.eligibility.checkCoverageStatus}?phn=${phn}&dateOfBirth=${dateOfBirth}&dateOfService=${dateOfService}
      &requestSubsidyInsuredService=${requestSubsidyInsuredService}&requestLastEyeExam=${requestLastEyeExam}&requestPatientRestriction=${requestPatientRestriction}`))
  }

}