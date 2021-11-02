import axios from 'axios';

import keycloak from '../keycloak';

export const resources = {
    coverage: {
      coverage: '/coverage',
    },
    eligibility: {
      eligibility: '/eligibility',
      checkEligibility: '/eligibility/checkEligibility',
      checkCoverageStatus: '/eligibility/checkCoverageStatus',
      phnEnquiry: '/eligibility/phnEnquiry'
    }
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
