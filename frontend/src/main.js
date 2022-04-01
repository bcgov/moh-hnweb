import '@bcgov/bc-sans/css/BCSans.css'

import { createApp } from 'vue'

import { library } from '@fortawesome/fontawesome-svg-core'
import { faBullhorn, faCheckCircle, faChevronDown, faExclamationCircle, faExclamationTriangle, faInfoCircle, faQuestionCircle, faSpinner, faTimes, faTrashAlt } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import App from './App.vue'
import AppCol from './components/grid/AppCol.vue'
import AppRow from './components/grid/AppRow.vue'
import AppButton from './components/ui/AppButton.vue'
import AppDateInput from './components/ui/AppDateInput.vue'
import AppInput from './components/ui/AppInput.vue'
import AppOutput from './components/ui/AppOutput.vue'
import AppSelect from './components/ui/AppSelect.vue'
import keycloak from './keycloak'
import { createRouter } from './router'
import UserService from './services/UserService'
import store from './store'

keycloak.onReady = async function (authenticated) {
  console.log('onReady')
  // Only initialize the application after keycloak is ready
  // otherwise the router won't have the correct authentication
  // info to work with
  try {
    if (authenticated) {
      const data = (await UserService.getPermissions()).data
      store.dispatch('auth/setPermissions', data)
    }
  } catch (err) {
    store.commit('alert/setErrorAlert', `${err}`)
  } finally {
    initApp()
  }
}

function initApp() {
  const app = createApp(App)

  app.component('AppCol', AppCol)
  app.component('AppRow', AppRow)

  app.component('AppButton', AppButton)
  app.component('AppDateInput', AppDateInput)
  app.component('AppInput', AppInput)
  app.component('AppOutput', AppOutput)
  app.component('AppSelect', AppSelect)

  const router = createRouter(app)
  app.use(router)
  app.use(store)

  app.config.globalProperties.$keycloak = keycloak

  library.add(faBullhorn)
  library.add(faCheckCircle)
  library.add(faChevronDown)
  library.add(faExclamationCircle)
  library.add(faExclamationTriangle)
  library.add(faInfoCircle)
  library.add(faSpinner)
  library.add(faTimes)
  library.add(faQuestionCircle)
  library.add(faTrashAlt)

  app.component('font-awesome-icon', FontAwesomeIcon)

  app.mount('#app')
}
