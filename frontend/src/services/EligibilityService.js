import {apiRequest, resources} from './BaseService'

export default {

  checkEligibility(phn, eligibilityDate) {
    console.log(`checkEligibility: PHN: ${phn} Date: ${eligibilityDate}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(`${resources.eligibility.checkEligibility}?phn=${phn}&eligibilityDate=${eligibilityDate}`))
  },

  checkCoverageStatus(phn, dateOfBirth, dateOfService, requestSubsidyInsuredService, requestLastEyeExam, requestPatientRestriction) {
    console.log(`checkStatusCoverage: PHN: ${phn}, Date Of Birth: ${dateOfBirth}, Date Of Service: ${dateOfService}, 
      requestSubsidyInsuredService: ${requestSubsidyInsuredService}, requestLastEyeExam: ${requestLastEyeExam}, requestPatientRestriction: ${requestPatientRestriction}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(
      `${resources.eligibility.checkCoverageStatus}?phn=${phn}&dateOfBirth=${dateOfBirth}&dateOfService=${dateOfService}
      &requestSubsidyInsuredService=${requestSubsidyInsuredService}&requestLastEyeExam=${requestLastEyeExam}&requestPatientRestriction=${requestPatientRestriction}`))
  }

}