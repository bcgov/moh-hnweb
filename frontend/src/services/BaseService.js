import axios from 'axios';
import keycloak from '../keycloak';

export const resources = {
    eligibility: {
      eligibility: '/eligibility',
      checkEligibility: '/eligibility/checkEligibility',
      checkMspCoverageStatus: '/eligibility/check-msp-coverage-status',
      phnEnquiry: '/eligibility/phnEnquiry'
    },
    enrollment: {
      enrollment: '/enrollment',
      personDetails: 'enrollment/person-details',
      addVisaResidentWithPHN: '/enrollment/enroll-subscriber',
    },
  }

export function apiRequest() {
    function createAxios() {
        const baseURL = import.meta.env.VITE_SERVICE_URL;
        return axios.create({
            baseURL: baseURL,
            headers: {Authorization: 'Bearer ' + keycloak.token}
        });
    }

    return keycloak.updateToken(0).then(createAxios);

}

export default apiRequest
