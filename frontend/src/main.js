import '@bcgov/bc-sans/css/BCSans.css'

import { createApp } from 'vue'

import App from './App.vue'
import keycloak from './keycloak'
import router from './router'

keycloak.onAuthSuccess = function () {
  const app = createApp(App)
  app.use(router)
  app.config.globalProperties.$keycloak = keycloak

  app.mount('#app')
}


