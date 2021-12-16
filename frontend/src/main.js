import '@bcgov/bc-sans/css/BCSans.css'

import { createApp } from 'vue'

import { library } from '@fortawesome/fontawesome-svg-core'
import { faCheckCircle, faChevronDown, faExclamationCircle, faExclamationTriangle, faInfoCircle, faSpinner, faTimes } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import App from './App.vue'
import AppCol from './components/grid/AppCol.vue'
import AppRow from './components/grid/AppRow.vue'
import AppButton from './components/ui/AppButton.vue'
import AppDateInput from './components/ui/AppDateInput.vue'
import AppInput from './components/ui/AppInput.vue'
import AppOutput from './components/ui/AppOutput.vue'
import keycloak from './keycloak'
import router from './router'
import store from './store'

keycloak.onAuthSuccess = function () {
  
  const app = createApp(App)

  app.component('AppCol', AppCol)
  app.component('AppRow', AppRow)

  app.component('AppButton', AppButton)
  app.component('AppDateInput', AppDateInput)
  app.component('AppInput', AppInput)
  app.component('AppOutput', AppOutput)

  app.use(router)
  app.use(store)
  app.config.globalProperties.$keycloak = keycloak

  library.add(faCheckCircle)
  library.add(faChevronDown)
  library.add(faExclamationCircle)
  library.add(faExclamationTriangle)
  library.add(faInfoCircle)
  library.add(faSpinner)
  library.add(faTimes)

  app.component('font-awesome-icon', FontAwesomeIcon)

  app.mount('#app')
}