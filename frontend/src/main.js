import '@bcgov/bc-sans/css/BCSans.css'

import VueKeycloakJs from 'keycloak-js'
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
import router from './router'
import UserService from './services/UserService'
import { createStore } from './store'

// import keycloak from './plugins/keycloak'

keycloak.onAuthSuccess = async function () {
  // Retrieve the User permissions immediately after Keycloak login
  // The permissions are required by the router which may be invoked
  // before App is created
  // Once the permissions data is available, then we create the App
  // If permission retrieval fails, then still create the App and
  // the router will handle it
  try {
    const data = (await UserService.getPermissions()).data
    store.dispatch('auth/setPermissions', data)
  } catch (err) {
    store.commit('alert/setErrorAlert', `${err}`)
  }
}

const app = createApp(App)

app.component('AppCol', AppCol)
app.component('AppRow', AppRow)

app.component('AppButton', AppButton)
app.component('AppDateInput', AppDateInput)
app.component('AppInput', AppInput)
app.component('AppOutput', AppOutput)
app.component('AppSelect', AppSelect)

//loadKeycloak(app, {})

//initKeycloak()

app.use(router)
const store = createStore(app)
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

//loadKeycloak()

app.mount('#app')

// export { app }

// function initKeycloak() {
//   var keycloak = new VueKeycloakJs({
//     url: 'http://keycloak-server$',
//     realm: 'myrealm',
//     clientId: 'myapp',
//   })
//   keycloak
//     .init()
//     .then(function (authenticated) {
//       alert(authenticated ? 'authenticated' : 'not authenticated')
//     })
//     .catch(function () {
//       alert('failed to initialize')
//     })
//   keycloak.login()
// }

function loadKeycloak(app) {
  console.log('loadKeycloak')

  // Load the config
  let kcConfig = {
    clientId: config.KEYCLOAK_CLIENT_ID || import.meta.env.VITE_KEYCLOAK_CLIENT_ID,
    realm: import.meta.env.VITE_KEYCLOAK_REALM,
    url: config.KEYCLOAK_URL || import.meta.env.VITE_KEYCLOAK_URL,
  }

  app.use(VueKeycloakJs, {
    init: { onLoad: 'check-sso' },
    config: {
      clientId: kcConfig.clientId,
      realm: kcConfig.realm,
      url: kcConfig.serverUrl,
    },
    onReady: () => {
      console.log('onerror')
      initializeApp(true, config.basePath)
    },
    onInitError: (error) => {
      console.log('error')
      console.error('Keycloak failed to initialize') // eslint-disable-line no-console
      console.error(error) // eslint-disable-line no-console
    },
  })
}
