export default (app) => {
  return {
    namespaced: true,
    state: {
      permissions: [],
      // In most cases, when this becomes populated, we end up doing a redirect flow,
      // so when we return to the app, it is fresh again and undefined
      redirectUri: undefined,
    },
    getters: {
      authenticated: () => app.config.globalProperties.$keycloak.authenticated,
      createLoginUrl: () => (options) => app.config.globalProperties.$keycloak.createLoginUrl(options),
      keycloakSubject: () => app.config.globalProperties.$keycloak.subject,
      token: () => app.config.globalProperties.$keycloak.token,
      redirectUri: (state) => state.redirectUri,

      getPermissions(state) {
        return state.permissions
      },
      hasPermission: (state) => (permission) => {
        return state.permissions.includes(permission)
      },
      hasAnyPermission: (state) => {
        return state.permissions.length > 0
      },
    },
    mutations: {
      setPermissions(state, permissions) {
        state.permissions = permissions
      },
      setRedirectUri(state, redirectUri) {
        state.redirectUri = redirectUri
      },
    },
    actions: {
      setPermissions(context, permissions) {
        context.commit('setPermissions', permissions)
      },
      login({ commit, getters }, idpHint = undefined) {
        // Use existing redirect uri if available
        if (!getters.redirectUri) {
          commit('setRedirectUri', location.toString())
        }

        const options = {
          redirectUri: getters.redirectUri,
        }

        // Determine idpHint based on input
        if (idpHint && typeof idpHint === 'string') {
          options.idpHint = idpHint
        }

        // Redirect to Keycloak
        window.location.replace(getters.createLoginUrl(options))
      },
      logout({ getters }) {
        window.location.replace(
          getters.createLogoutUrl({
            redirectUri: `${location.origin}/${Vue.prototype.$config.basePath}`,
          }),
        )
      },
    },
  }
}
