import { createRouter, createWebHistory } from 'vue-router'

import AddDependent from '../views/groupmember/AddDependent.vue'
import AddGroupMember from '../views/groupmember/AddGroupMember.vue'
import AddVisaResidentWithPHN from '../views/coverage/enrollment/AddVisaResidentWithPHN.vue'
import AddVisaResidentWithoutPHN from '../views/coverage/enrollment/AddVisaResidentWithoutPHN.vue'
import CancelDependent from '../views/groupmember/CancelDependent.vue'
import CancelGroupMember from '../views/groupmember/CancelGroupMember.vue'
import CheckEligibility from './../views/eligibility/CheckEligibility.vue'
import ContractInquiry from '../views/mspcontracts/ContractInquiry.vue'
import CoverageEnrollmentHome from '../views/coverage/enrollment/CoverageEnrollmentHome.vue'
import CoverageMaintenanceHome from '../views/coverage/maintenance/CoverageMaintenanceHome.vue'
import CoverageStatusCheck from './../views/eligibility/CoverageStatusCheck.vue'
import EligibilityHome from '../views/eligibility/EligibilityHome.vue'
import GetContractPeriods from '../views/mspcontracts/GetContractPeriods.vue'
import GroupMemberHome from '../views/groupmember/GroupMemberHome.vue'
import Help from './../views/Help.vue'
import Home from './../views/Home.vue'
import MspContractsHome from '../views/mspcontracts/MspContractsHome.vue'
import NotFound from '../views/NotFound.vue'
import PhnInquiry from '../views/eligibility/PhnInquiry.vue'
import PhnLookup from '../views/eligibility/PhnLookup.vue'
import Unauthorized from '../views/Unauthorized.vue'
import UpdateNumberAndDept from '../views/groupmember/UpdateNumberAndDept.vue'
import store from '../store'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
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
      },
      {
        path: 'addStudyPermitHolderWithPHN',
        name: 'AddVisaResidentWithPHN',
        component: AddVisaResidentWithPHN,
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
        },
      },
      {
        path: 'phnInquiry',
        name: 'PhnInquiry',
        component: PhnInquiry,
        meta: {
          permission: 'PHNInquiry',
        },
      },
      {
        path: 'phnLookup',
        name: 'PhnLookup',
        component: PhnLookup,
      },
      {
        path: 'coverageStatusCheck',
        name: 'CoverageStatusCheck',
        component: CoverageStatusCheck,
        meta: {
          permission: 'MSPCoverageCheck',
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
      },
      {
        path: 'addDependent',
        name: 'AddDependent',
        component: AddDependent,
      },
      {
        path: 'updateNumberAndDept',
        name: 'UpdateNumberAndDept',
        component: UpdateNumberAndDept,
      },
      {
        path: 'cancelGroupMember',
        name: 'CancelGroupMember',
        component: CancelGroupMember,
      },
      {
        path: 'cancelDependent',
        name: 'CancelDependent',
        component: CancelDependent,
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
      },
      {
        path: 'contractInquiry',
        name: 'ContractInquiry',
        component: ContractInquiry,
      },
    ],
  },
  {
    path: '/help',
    name: 'Help',
    component: Help,
  },
  {
    path: '/:notFound(.*)',
    name: 'NotFound',
    component: NotFound,
  },
  {
    path: '/unauthorized',
    name: 'Unauthorized',
    component: Unauthorized,
  },
]

function checkPageAction(to, next) {
  const pageAction = to.query.pageAction

  if (pageAction !== 'REGISTRATION') {
    store.commit('studyPermitHolder/resetResident')
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
