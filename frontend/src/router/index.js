import { createRouter as createVueRouter, createWebHistory } from 'vue-router'

import Error from './../views/Error.vue'
import Help from './../views/Help.vue'
import Home from './../views/Home.vue'
import CheckEligibility from './../views/eligibility/CheckEligibility.vue'
import CoverageStatusCheck from './../views/eligibility/CoverageStatusCheck.vue'
import { useAlertStore } from '../stores/alert'
import { useAuthStore } from '../stores/auth'
import { useStudyPermitHolderStore } from '../stores/studyPermitHolder'
import NotFound from '../views/NotFound.vue'
import Unauthorized from '../views/Unauthorized.vue'
import AddVisaResidentWithPHN from '../views/coverage/enrollment/AddVisaResidentWithPHN.vue'
import AddVisaResidentWithoutPHN from '../views/coverage/enrollment/AddVisaResidentWithoutPHN.vue'
import CoverageEnrollmentHome from '../views/coverage/enrollment/CoverageEnrollmentHome.vue'
import CoverageMaintenanceHome from '../views/coverage/maintenance/CoverageMaintenanceHome.vue'
import EligibilityHome from '../views/eligibility/EligibilityHome.vue'
import PhnInquiry from '../views/eligibility/PhnInquiry.vue'
import PhnLookup from '../views/eligibility/PhnLookup.vue'
import AddDependent from '../views/groupmember/AddDependent.vue'
import AddGroupMember from '../views/groupmember/AddGroupMember.vue'
import CancelDependent from '../views/groupmember/CancelDependent.vue'
import CancelGroupMember from '../views/groupmember/CancelGroupMember.vue'
import GroupMemberHome from '../views/groupmember/GroupMemberHome.vue'
import UpdateNumberAndDept from '../views/groupmember/UpdateNumberAndDept.vue'
import ContractInquiry from '../views/mspcontracts/ContractInquiry.vue'
import GetContractPeriods from '../views/mspcontracts/GetContractPeriods.vue'
import GetGroupMembersContractAddress from '../views/mspcontracts/GetGroupMembersContractAddress.vue'
import MspContractsHome from '../views/mspcontracts/MspContractsHome.vue'
import UpdateContractAddress from '../views/mspcontracts/UpdateContractAddress.vue'
import CredentialsInfo from '../views/welcome/CredentialsInfo.vue'
import Login from '../views/welcome/Login.vue'

const createRoutes = (app) => [
  {
    path: '/',
    name: 'Landing',
    redirect: {
      name: 'Login',
    },
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/welcome/credentialsInfo',
    name: 'CredentialsInfo',
    component: CredentialsInfo,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/welcome/login',
    name: 'Login',
    component: Login,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/coverage/maintenance',
    name: 'CoverageMaintenance',
    component: CoverageMaintenanceHome,
  },
  {
    path: '/coverage/enrollment',
    name: 'CoverageEnrollment',
    component: CoverageEnrollmentHome,
    redirect: {
      name: 'AddVisaResidentWithoutPHN',
    },
    children: [
      {
        path: 'addStudyPermitHolderWithoutPHN',
        name: 'AddVisaResidentWithoutPHN',
        component: AddVisaResidentWithoutPHN,
        meta: {
          permission: 'AddPermitHolderWOPHN',
          requiresAuth: true,
        },
      },
      {
        path: 'addStudyPermitHolderWithPHN',
        name: 'AddVisaResidentWithPHN',
        component: AddVisaResidentWithPHN,
        meta: {
          permission: 'AddPermitHolderWithPHN',
          requiresAuth: true,
        },
        beforeEnter: (to, _, next) => {
          checkPageAction(to, next)
        },
      },
    ],
  },
  {
    path: '/eligibility',
    name: 'Eligibility',
    component: EligibilityHome,
    redirect: {
      name: 'CheckEligibility',
    },
    children: [
      {
        path: 'checkEligibility',
        name: 'CheckEligibility',
        component: CheckEligibility,
        meta: {
          permission: 'CheckEligibility',
          requiresAuth: true,
        },
      },
      {
        path: 'phnInquiry',
        name: 'PhnInquiry',
        component: PhnInquiry,
        meta: {
          permission: 'PHNInquiry',
          requiresAuth: true,
        },
      },
      {
        path: 'phnLookup',
        name: 'PhnLookup',
        component: PhnLookup,
        meta: {
          permission: 'PHNLookup',
          requiresAuth: true,
        },
      },
      {
        path: 'coverageStatusCheck',
        name: 'CoverageStatusCheck',
        component: CoverageStatusCheck,
        meta: {
          permission: 'MSPCoverageCheck',
          requiresAuth: true,
        },
      },
    ],
  },
  {
    path: '/groupMember',
    name: 'GroupMember',
    component: GroupMemberHome,
    redirect: {
      name: 'AddGroupMember',
    },
    children: [
      {
        path: 'addGroupMember',
        name: 'AddGroupMember',
        component: AddGroupMember,
        meta: {
          permission: 'AddGroupMember',
          requiresAuth: true,
        },
      },
      {
        path: 'addDependent',
        name: 'AddDependent',
        component: AddDependent,
        meta: {
          permission: 'AddDependent',
          requiresAuth: true,
        },
      },
      {
        path: 'updateNumberAndDept',
        name: 'UpdateNumberAndDept',
        component: UpdateNumberAndDept,
        meta: {
          permission: 'UpdateNumberAndDept',
          requiresAuth: true,
        },
      },
      {
        path: 'cancelGroupMember',
        name: 'CancelGroupMember',
        component: CancelGroupMember,
        meta: {
          permission: 'CancelGroupMember',
          requiresAuth: true,
        },
      },
      {
        path: 'cancelDependent',
        name: 'CancelDependent',
        component: CancelDependent,
        meta: {
          permission: 'CancelDependent',
          requiresAuth: true,
        },
      },
    ],
  },
  {
    path: '/mspContracts',
    name: 'MspContracts',
    component: MspContractsHome,
    redirect: {
      name: 'GetContractPeriods',
    },
    children: [
      {
        path: 'getContractPeriods',
        name: 'GetContractPeriods',
        component: GetContractPeriods,
        meta: {
          permission: 'GetContractPeriods',
          requiresAuth: true,
        },
      },
      {
        path: 'contractInquiry',
        name: 'ContractInquiry',
        component: ContractInquiry,
        meta: {
          permission: 'ContractInquiry',
          requiresAuth: true,
        },
      },
      {
        path: 'getGroupMembersContractAddress',
        name: 'GetGroupMembersContractAddress',
        component: GetGroupMembersContractAddress,
        meta: {
          permission: 'GetContractAddress',
          requiresAuth: true,
        },
      },
      {
        path: 'updateContractAddress',
        name: 'UpdateContractAddress',
        component: UpdateContractAddress,
        meta: {
          permission: 'UpdateContractAddress',
          requiresAuth: true,
        },
      },
    ],
  },
  {
    path: '/error',
    name: 'Error',
    component: Error,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/help',
    name: 'Help',
    component: Help,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/:notFound(.*)',
    name: 'NotFound',
    component: NotFound,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: '/unauthorized',
    name: 'Unauthorized',
    component: Unauthorized,
    meta: {
      requiresAuth: true,
    },
  },
]

function checkPageAction(to, next) {
  const pageAction = to.query.pageAction

  if (pageAction !== 'REGISTRATION') {
    const studyPermitHolderStore = useStudyPermitHolderStore()
    studyPermitHolderStore.$reset()
  }
  next()
}

export const createRouter = (app) => {
  const router = createVueRouter({
    history: createWebHistory(),
    routes: createRoutes(app),
    scrollBehavior(to, from, savedPosition) {
      if (savedPosition) {
        return savedPosition
      }
      return { left: 0, top: 0 }
    },
  })
  router.beforeEach(async (to, _, next) => {
    const alertStore = useAlertStore()
    const authStore = useAuthStore()

    const authenticated = app.config.globalProperties.$keycloak.authenticated

    // Check if the API is available
    if (!authStore.apiAvailable && to.name !== 'Error' && to.name !== 'Help') {
      alertStore.setErrorAlert('MSP Direct API is unavailable.')
      next({ name: 'Error' })
      return
    }

    // Authenticated users should never see the Login screen
    // Send them to Home instead
    if (authenticated && to.name === 'Login') {
      next({ name: 'Home' })
      return
    }

    // Always navigate to pages that don't require auth
    if (!to.meta.requiresAuth) {
      next()
      return
    }

    // Secured pages shouldn't be available to unauthenticated users
    if (to.meta.requiresAuth && !authenticated) {
      alertStore.setErrorAlert(`You are not authorized to access ${to.path}. Please login first.`)
      next({ name: 'Login' })
      return
    }

    // Validate that the user has permissions
    const hasAnyPermission = authStore.hasAnyPermission
    if (!hasAnyPermission && to.name !== 'Unauthorized' && to.name !== 'Help') {
      next({ name: 'Unauthorized' })
      return
    }

    // Validate routes secured by permission
    const permission = to.meta.permission
    if (permission) {
      const hasPermission = authStore.hasPermission(permission)
      if (hasPermission) {
        next()
      } else {
        alertStore.setErrorAlert(`You are not authorized to access ${to.path}`)
        next({ name: 'Home' })
      }
    } else {
      next()
    }
  })
  return router
}
