const state = {
  permissions: [],
}

const mutations = {
  setPermissions(state, permissions) {
    state.permissions = permissions
    state.permissionsLoaded = true
  },
}

const getters = {
  getPermissions(state) {
    return state.permissions
  },
  hasPermission: (state) => (permission) => {
    return state.permissions.includes(permission)
  },
  hasAnyPermission: (state) => {
    return state.permissions.length > 0
  },
}

const actions = {
  setPermissions(context, permissions) {
    context.commit('setPermissions', permissions)
  },
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
}
