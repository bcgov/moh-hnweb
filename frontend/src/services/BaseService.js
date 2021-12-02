import axios from 'axios';

import keycloak from '../keycloak';

export const resources = {
  eligibility: {
    checkEligibility: '/eligibility/check-eligibility',
    checkMspCoverageStatus: '/eligibility/check-msp-coverage-status',
    inquirePhn: '/eligibility/inquire-phn',
    lookupPhn: '/eligibility/lookup-phn',
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
  return keycloak.updateToken(0).then(createAxios)
}

export default apiRequest
