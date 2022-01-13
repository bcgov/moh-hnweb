import { createRouter, createWebHistory } from 'vue-router'

import AddVisaResidentWithPHN from '../views/coverage/enrollment/AddVisaResidentWithPHN.vue'
import AddVisaResidentWithoutPHN from '../views/coverage/enrollment/AddVisaResidentWithoutPHN.vue'
import CheckEligibility from './../views/eligibility/CheckEligibility.vue'
import CoverageEnrollmentHome from '../views/coverage/enrollment/CoverageEnrollmentHome.vue'
import CoverageMaintenanceHome from '../views/coverage/maintenance/CoverageMaintenanceHome.vue'
import CoverageStatusCheck from './../views/eligibility/CoverageStatusCheck.vue'
import EligibilityHome from '../views/eligibility/EligibilityHome.vue'
import Employees from './../views/Employees.vue'
import Help from './../views/Help.vue'
import Home from './../views/Home.vue'
import NotFound from '../views/NotFound.vue'
import PhnInquiry from '../views/eligibility/PhnInquiry.vue'
import PhnLookup from '../views/eligibility/PhnLookup.vue'
import Unauthorized from '../views/Unauthorized.vue'
import keycloak from '../keycloak'
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
        beforeEnter: (to, _, next) => {
          handleAuth(to, next, 'R15')
        },
      },
      {
        path: 'phnInquiry',
        name: 'PhnInquiry',
        component: PhnInquiry,
        beforeEnter: (to, from, next) => {
          handleAuth(to, next, 'R41')
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
        beforeEnter: (to, from, next) => {
          handleAuth(to, next, 'E45')
        },
      },
    ],
  },
  {
    path: '/manageEmployees',
    name: 'ManageEmployees',
    component: Employees,
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

function handleAuth(to, next, permission) {
  const hasPermission = store.getters['auth/hasPermission'](permission)
  if (hasPermission) {
    next()
  } else {
    store.commit('alert/setErrorAlert', `You are not authorized to access ${to.path}`)
    next({ name: 'Home' })
  }
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

router.beforeEach((to, from, next) => {
  const hasAnyPermission = store.getters['auth/hasAnyPermission']
  if (hasAnyPermission || isUnauthorizedComponent(to)) {
    next()
  } else {
    next({ name: 'Unauthorized' })
  }
})

function isUnauthorizedComponent(to) {
  return to.name === 'Unauthorized'
}

export default router
