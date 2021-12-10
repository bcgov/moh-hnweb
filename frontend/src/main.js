import '@bcgov/bc-sans/css/BCSans.css'

import { faCheckCircle, faChevronDown, faExclamationCircle, faExclamationTriangle, faInfoCircle, faTimes } from '@fortawesome/free-solid-svg-icons'

import App from './App.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { createApp } from 'vue'
import keycloak from './keycloak'
import { library } from '@fortawesome/fontawesome-svg-core'
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
  library.add(faChevronDown)

  app.component('font-awesome-icon', FontAwesomeIcon)

  app.mount('#app')
}