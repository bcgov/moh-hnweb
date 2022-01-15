import Keycloak from 'keycloak-js'

let config = {
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID,
  realm: import.meta.env.VITE_KEYCLOAK_REALM,
  url: import.meta.env.VITE_KEYCLOAK_URL,
}

// Keycloak uses "public\keycloak.json" by default if not otherwise specified.
// https://www.keycloak.org/docs/latest/securing_apps/index.html#_javascript_adapter
let keycloak = new Keycloak(config)

let initOptions = {
  responseMode: 'fragment',
  flow: 'standard',
  onLoad: 'login-required',
  pkceMethod: 'S256',
  checkLoginIframe: false,
}

keycloak.init(initOptions)

export default keycloak
