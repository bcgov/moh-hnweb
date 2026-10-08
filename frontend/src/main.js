import '@bcgov/bc-sans/css/BCSans.css'
import 'primevue/resources/themes/saga-blue/theme.css' //theme
import 'primevue/resources/primevue.min.css' //core css
import 'primeicons/primeicons.css' //icons

import { createPinia } from 'pinia'
import PrimeVue from 'primevue/config'
import { createApp } from 'vue'

import { DraggablePlugin } from '@braks/revue-draggable'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faBullhorn, faCheckCircle, faChevronDown, faExclamationCircle, faExclamationTriangle, faExternalLinkAlt, faInfoCircle, faQuestionCircle, faSpinner, faTimes, faTrashAlt } from '@fortawesome/free-solid-svg-icons'
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
import { useAuthStore } from './stores/auth'

const app = createApp(App)

keycloak.onReady = async function (authenticated) {
  // Only initialize the application after keycloak is ready
  // otherwise the router won't have the correct authentication
  // info to work with
  let apiAvailable = true
  let isPBFUser = false
  let permissions = []
  try {
    if (authenticated) {
      const data = (await UserService.getPermissions()).data
      permissions = data
      isPBFUser = checkForPBFUser()
    }
  } catch (err) {
    // Check for network error
    // Other errors (401, 403) are possible
    if (err.message === 'Network Error') {
      apiAvailable = false
    }
  } finally {
    initApp(permissions, apiAvailable, isPBFUser)
  }
}

function checkForPBFUser() {
  var audience = ''

  if (keycloak.tokenParsed.hasOwnProperty('aud')) {
    const aud = keycloak.tokenParsed.aud
    if (typeof aud === 'string') {
      audience = aud.startsWith('MSPDIRECT-SERVICE') ? aud : ''
    } else {
      audience = aud.find((element) => element.startsWith('MSPDIRECT-SERVICE'))
    }
  }

  if (keycloak.tokenParsed.hasOwnProperty('resource_access')) {
    if (keycloak.tokenParsed.resource_access.hasOwnProperty(audience)) {
      const mspDirect = keycloak.tokenParsed.resource_access[audience]
      return mspDirect.roles.length === 1 && mspDirect.roles.includes('PBFUSER')
    }
  }
  return false
}

function initApp(permissions, apiAvailable, isPBFUser) {
  const app = createApp(App)

  app.component('AppCol', AppCol)
  app.component('AppRow', AppRow)

  app.component('AppButton', AppButton)
  app.component('AppDateInput', AppDateInput)
  app.component('AppInput', AppInput)
  app.component('AppOutput', AppOutput)
  app.component('AppSelect', AppSelect)

  // Set up Pinia with permissions
  app.use(createPinia())
  const auth = useAuthStore()
  auth.permissions = permissions
  auth.apiAvailable = apiAvailable
  auth.isPBFUser = isPBFUser

  const router = createRouter(app)
  app.use(router)

  app.use(DraggablePlugin)

  app.use(PrimeVue)

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
