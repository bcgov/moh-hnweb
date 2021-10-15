import '@bcgov/bc-sans/css/BCSans.css'

import { createApp } from 'vue'

import App from './App.vue'
import keycloak from './keycloak'
import router from './router'
import store from './store'

keycloak.onAuthSuccess = function () {
  const app = createApp(App)
  app.use(router)
  app.use(store)
  app.config.globalProperties.$keycloak = keycloak

  app.mount('#app')
}