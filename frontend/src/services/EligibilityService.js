import {apiRequest, resources} from './BaseService'

export default {
  checkEligibility(phn, eligibilityDate) {
    console.log(`checkEligibility: PHN: ${phn} Date: ${eligibilityDate}`)
    return apiRequest().then(axiosInstance => axiosInstance.get(`${resources.eligibility.checkEligibility}?phn=${phn}&eligibilityDate=${eligibilityDate}`));
  }
}

