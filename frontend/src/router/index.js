import { createRouter, createWebHistory } from 'vue-router'

import Employees from './../views/Employees.vue'
import Help from './../views/Help.vue'
import Home from './../views/Home.vue'
import CheckEligibility from './../views/eligibility/CheckEligibility.vue'
import CoverageStatusCheck from './../views/eligibility/CoverageStatusCheck.vue'
import store from '../store'
import NotFound from '../views/NotFound.vue'
import AddVisaResidentWithPHN from '../views/coverage/enrollment/AddVisaResidentWithPHN.vue'
import AddVisaResidentWithoutPHN from '../views/coverage/enrollment/AddVisaResidentWithoutPHN.vue'
import CoverageEnrollmentHome from '../views/coverage/enrollment/CoverageEnrollmentHome.vue'
import CoverageMaintenanceHome from '../views/coverage/maintenance/CoverageMaintenanceHome.vue'
import EligibilityHome from '../views/eligibility/EligibilityHome.vue'
import PhnInquiry from '../views/eligibility/PhnInquiry.vue'
import PhnLookup from '../views/eligibility/PhnLookup.vue'

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
        path: 'addVisaResidentWithoutPHN',
        name: 'AddVisaResidentWithoutPHN',
        component: AddVisaResidentWithoutPHN,
      },
      {
        path: 'addVisaResidentWithPHN',
        name: 'AddVisaResidentWithPHN',
        component: AddVisaResidentWithPHN,
      },
    ]
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
        }
      },
      {
        path: 'phnInquiry',
        name: 'PhnInquiry',
        component: PhnInquiry,
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
        }
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
]

function hasPermission(permission) {
  return store.getters['auth/hasPermission'](permission)
}

function handleAuth(to, next, permission) {
  const permissions = store.getters['auth/getPermissions']
  if (hasPermission(permission)) {
    next()
  } else {
    store.commit('alert/setErrorAlert', `You are not authorized to access ${to.path}`)
    next({name: 'Home'})
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

export default router
