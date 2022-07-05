import '@bcgov/bc-sans/css/BCSans.css'

import { createPinia } from 'pinia'
import { createApp } from 'vue'
import Multiselect from 'vue-multiselect'

import { DraggablePlugin } from '@braks/revue-draggable'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faBullhorn, faCheckCircle, faChevronDown, faExclamationCircle, faExclamationTriangle, faExternalLinkAlt, faInfoCircle, faQuestionCircle, faSpinner, faTimes, faTrashAlt } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import AppMultiSelect from './components/ui/AppMultiSelect.vue'
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
import { useAuthStore } from './stores/auth'

keycloak.onReady = async function (authenticated) {
  // Only initialize the application after keycloak is ready
  // otherwise the router won't have the correct authentication
  // info to work with
  let apiAvailable = true
  let permissions = []
  try {
    if (authenticated) {
      const data = (await UserService.getPermissions()).data
      permissions = data
    }
  } catch (err) {
    // Check for network error
    // Other errors (401, 403) are possible
    if (err.message === 'Network Error') {
      apiAvailable = false
    }
  } finally {
    initApp(permissions, apiAvailable)
  }
}

function initApp(permissions, apiAvailable) {
  const app = createApp(App)

  app.component('AppCol', AppCol)
  app.component('AppRow', AppRow)

  app.component('AppButton', AppButton)
  app.component('AppDateInput', AppDateInput)
  app.component('AppInput', AppInput)
  app.component('AppOutput', AppOutput)
  app.component('AppSelect', AppSelect)
  app.component('AppMultiSelect', AppMultiSelect)

  // Set up Pinia with permissions
  app.use(createPinia())
  const auth = useAuthStore()
  auth.permissions = permissions
  auth.apiAvailable = apiAvailable

  const router = createRouter(app)
  app.use(router)

  app.use(DraggablePlugin)

  app.config.globalProperties.$keycloak = keycloak

  library.add(faBullhorn)
  library.add(faCheckCircle)
  library.add(faChevronDown)
  library.add(faExclamationCircle)
  library.add(faExclamationTriangle)
  library.add(faExternalLinkAlt)
  library.add(faInfoCircle)
  library.add(faSpinner)
  library.add(faTimes)
  library.add(faQuestionCircle)
  library.add(faTrashAlt)

  app.component('font-awesome-icon', FontAwesomeIcon)

  app.mount('#app')
}
