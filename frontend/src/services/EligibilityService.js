import dayjs from 'dayjs'

import {API_DATE_FORMAT} from '../util/constants'
import {apiRequest, resources} from './BaseService'

export default {

  checkEligibility(phn, eligibilityDate) {
    console.log(`checkEligibility: PHN: ${phn} Date: ${eligibilityDate}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(`${resources.eligibility.checkEligibility}?phn=${phn}&eligibilityDate=${eligibilityDate}`))
  },

  checkCoverageStatus(phn, dateOfBirth, dateOfService, checkSubsidyInsuredService, checkLastEyeExam, checkPatientRestriction) {
    const formattedDateOfBirth = dayjs(dateOfBirth).format(API_DATE_FORMAT)
    const formattedDateOfService = dayjs(dateOfService).format(API_DATE_FORMAT)
    console.log(`checkStatusCoverage: PHN: ${phn}, Date Of Birth: ${formattedDateOfBirth}, Date Of Service: ${formattedDateOfService}, 
    checkSubsidyInsuredService: ${checkSubsidyInsuredService}, checkLastEyeExam: ${checkLastEyeExam}, checkPatientRestriction: ${checkPatientRestriction}`)
    
    return apiRequest().then(axiosInstance => axiosInstance.get(
      `${resources.eligibility.checkMspCoverageStatus}?phn=${phn}&dateOfBirth=${formattedDateOfBirth}&dateOfService=${formattedDateOfService}` +
      `&checkSubsidyInsuredService=${checkSubsidyInsuredService}&checkLastEyeExam=${checkLastEyeExam}&checkPatientRestriction=${checkPatientRestriction}`))
  }

}