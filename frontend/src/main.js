import '@bcgov/bc-sans/css/BCSans.css'

import { createApp } from 'vue'

import { library } from '@fortawesome/fontawesome-svg-core'
import { faCheckCircle, faExclamationCircle, faExclamationTriangle, faInfoCircle, faTimes } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import App from './App.vue'
import keycloak from './keycloak'
import router from './router'
import store from './store'

keycloak.onAuthSuccess = function () {
  
  const app = createApp(App)
  app.use(router)
  app.use(store)
  app.config.globalProperties.$keycloak = keycloak

  library.add(faCheckCircle)
  library.add(faExclamationCircle)
  library.add(faExclamationTriangle)
  library.add(faInfoCircle)
  library.add(faTimes)

  app.component('font-awesome-icon', FontAwesomeIcon)

  app.mount('#app')
}