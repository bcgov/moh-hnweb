import Keycloak from 'keycloak-js'

let kcConfig = {
  clientId: config.KEYCLOAK_CLIENT_ID || import.meta.env.VITE_KEYCLOAK_CLIENT_ID,
  realm: import.meta.env.VITE_KEYCLOAK_REALM,
  url: config.KEYCLOAK_URL || import.meta.env.VITE_KEYCLOAK_URL,
}

// Keycloak uses "public\keycloak.json" by default if not otherwise specified.
// https://www.keycloak.org/docs/latest/securing_apps/index.html#_javascript_adapter
let keycloak = new Keycloak(kcConfig)

let initOptions = {
  responseMode: 'fragment',
  flow: 'standard',
  onLoad: 'check-sso',
  pkceMethod: 'S256',
  checkLoginIframe: false,
}

keycloak.init(initOptions).then(function (authenticated) {
  console.log('authenticated ' + authenticated)
  console.log(keycloak)
})

keycloak.onAuthSuccess = () => {
  console.log('onAuthSuccess')
}

export default keycloak
