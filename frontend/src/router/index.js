import {createRouter, createWebHistory} from 'vue-router'

import Employees from './../views/Employees.vue'
import Home from './../views/Home.vue'

const routes = [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
        path: '/employees',
        name: 'Employees',
        component: Employees
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router