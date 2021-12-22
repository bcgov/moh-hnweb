const state = {
  permissions: [],
}

const mutations = {
  setPermissions(state, permissions) {
    state.permissions = permissions
  },
}

const getters = {
  getPermissions(state) {
    return state.permissions
  },
  hasPermission: (state) => (permission) => {
    return state.permissions.includes(permission)
  }
}

const actions = {
  setPermissions(context, permissions) {
    context.commit('setPermissions', permissions)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}