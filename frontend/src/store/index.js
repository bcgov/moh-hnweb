import { createLogger, createStore } from 'vuex'

import alert from './modules/alert'
import auth from './modules/auth'

const debug = import.meta.env.DEV

export default createStore({
  modules: {
    alert,
    auth
  },
  strict: debug,
  plugins: debug ? [createLogger()] : []
})