import apiRequest from './baseClient'
import {resources} from '.'

export default {
  checkEligibility(phn, eligibilityDate) {
    console.log(`checkEligibility: ${phn} ${eligibilityDate}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(`${resources.eligibility.checkEligibility}?phn=${phn}&eligibilityDate=${eligibilityDate}`));
  }
}

