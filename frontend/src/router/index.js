import {createRouter, createWebHistory} from 'vue-router'

import Employees from './../views/Employees.vue'
import Help from './../views/Help.vue'
import Home from './../views/Home.vue'
import CheckEligibility from './../views/eligibility/CheckEligibility.vue'
import CoverageStatusCheck from './../views/eligibility/CoverageStatusCheck.vue'
import PhnEnquiry from './../views/eligibility/PhnEnquiry.vue'
import CoverageEnrollmentHome from '../views/coverage/enrollment/CoverageEnrollmentHome.vue'
import CoverageMaintenanceHome from '../views/coverage/maintenance/CoverageMaintenanceHome.vue'
import EligibilityHome from '../views/eligibility/EligibilityHome.vue'

const routes = [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
        path: '/coverage/maintenance',
        name: 'CoverageMaintenance',
        component: CoverageMaintenanceHome
    },
    {
        path: '/coverage/enrollment',
        name: 'CoverageEnrollment',
        component: CoverageEnrollmentHome
    },
    {
        path: '/eligibility',
        name: 'Eligibility',
        component: EligibilityHome,
        redirect: {
            name: 'CheckEligibility'
        },
        children: [
            {
                path: 'checkEligibility',
                name: 'CheckEligibility',
                component: CheckEligibility,
            },
            {
                path: 'phnEnquiry',
                name: 'PhnEnquiry',
                component: PhnEnquiry,
            },
            {
                path: 'coverageStatusCheck',
                name: 'CoverageStatusCheck',
                component: CoverageStatusCheck,
            },
        ]
    },
    {
        path: '/managemployees',
        name: 'ManageEmployees',
        component: Employees
    },
    {
        path: '/help',
        name: 'Help',
        component: Help
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router