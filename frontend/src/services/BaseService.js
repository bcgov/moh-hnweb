import axios from 'axios'

import keycloak from '../keycloak'

export const resources = {
  bulletins: '/bulletins',
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
    addDependent: '/group-member/add-dependent',
    updateNumberAndDept: '/group-member/update-number-and-dept',
    cancelGroupMember: '/group-member/cancel-group-member',
    cancelDependent: '/group-member/cancel-dependent',
  },
  maintenance: {
    changeEffectiveDate: '/maintenance/change-effective-date',
    changeCancelDate: '/maintenance/change-cancel-date',
    reinstateOverAgeDependent: '/maintenance/reinstate-over-age-dependent',
  },
  mspContracts: {
    getContractPeriods: '/msp-contracts/get-contract-periods',
    inquireContract: '/msp-contracts/inquire-contract',
    updateContractAddress: '/msp-contracts/update-contract-address',
  },
  reports: {
    getAuditReport: '/audit/audit-report',
    getOrganizations: '/audit/organizations',
  },
  user: {
    permissions: '/user/permissions',
  },
}

export function apiRequest() {
  function createAxios() {
    const baseURL = config.SERVICE_URL || import.meta.env.VITE_SERVICE_URL
    return axios.create({
      baseURL,
      headers: { Authorization: 'Bearer ' + keycloak.token },
    })
  }
  return keycloak.updateToken(0).then(createAxios)
}

export default apiRequest
