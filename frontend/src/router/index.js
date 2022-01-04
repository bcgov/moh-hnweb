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
import ManageEmployeeHome from '../views/manageemployee/ManageEmployeeHome.vue'
import NotFound from '../views/NotFound.vue'
import PhnInquiry from '../views/eligibility/PhnInquiry.vue'
import PhnLookup from '../views/eligibility/PhnLookup.vue'
import UpdateGroupMember from '../views/manageemployee/UpdateGroupMember.vue'
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
    path: '/manageemployee',
    name: 'ManageEmployee',
    component: ManageEmployeeHome,
    redirect: {
      name: 'UpdateGroupMember',
    },
    children: [
      {
        path: 'updateGroupMember',
        name: 'UpdateGroupMember',
        component: UpdateGroupMember,      
      },
     
    ]
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

function handleAuth(to, next, permission) {
  const hasPermission = store.getters['auth/hasPermission'](permission)
  if (hasPermission) {
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
