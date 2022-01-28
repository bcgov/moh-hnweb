import axios from 'axios'
import keycloak from '../keycloak'

export const resources = {
  enrollment: {
    nameSearch: 'enrollment/name-search',
    getPersonDetails: 'enrollment/get-person-details',
    enrollSubscriber: '/enrollment/enroll-subscriber',
  },
  eligibility: {
    checkEligibility: '/eligibility/check-eligibility',
    checkMspCoverageStatus: '/eligibility/check-msp-coverage-status',
    inquirePhn: '/eligibility/inquire-phn',
    lookupPhn: '/eligibility/lookup-phn',
  },
  groupMember: {
    addGroupMember: '/group-member/add-group-member',
    updateNumberAndDept: '/group-member/update-number-and-dept',
    cancelGroupMember: '/group-member/cancel-group-member',
  },
  user: {
    permissions: '/user/permissions'
  },
}

export function apiRequest() {
  function createAxios() {
    const baseURL = import.meta.env.VITE_SERVICE_URL
    return axios.create({
      baseURL: baseURL,
      headers: { Authorization: 'Bearer ' + keycloak.token },
    })
  }
  return keycloak.updateToken(0).then(createAxios)
}

export default apiRequest
