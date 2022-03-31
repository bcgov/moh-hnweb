import { createRouter, createWebHistory } from 'vue-router'

import Help from './../views/Help.vue'
import Home from './../views/Home.vue'
import CheckEligibility from './../views/eligibility/CheckEligibility.vue'
import CoverageStatusCheck from './../views/eligibility/CoverageStatusCheck.vue'
import { createStore } from '../store'
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
import CredentialsInfo from '../views/welcome/CredentialsInfo.vue'
import Login from '../views/welcome/Login.vue'

const routes = [
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
      permission: 'AddPermitHolderWOPHN',
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
    ],
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
    // store.commit('studyPermitHolder/resetResident')
  }
  next()
}

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    }
    return { left: 0, top: 0 }
  },
})

router.beforeEach((to, _, next) => {
  // Always navigate to pages that don't require auth
  if (!to.meta.requiresAuth) {
    console.log('auth not required')
    next()
    return
  } else {
    console.log('auth required')
  }

  // Validate that the user has permissions
  const hasAnyPermission = store.getters['auth/hasAnyPermission']
  if (!hasAnyPermission && to.name !== 'Unauthorized') {
    next({ name: 'Unauthorized' })
    return
  }

  // Validate routes secured by permission
  const permission = to.meta.permission
  if (permission) {
    const hasPermission = store.getters['auth/hasPermission'](permission)
    if (hasPermission) {
      next()
    } else {
      store.commit('alert/setErrorAlert', `You are not authorized to access ${to.path}`)
      next({ name: 'Home' })
    }
  } else {
    next()
  }
})

export default router
