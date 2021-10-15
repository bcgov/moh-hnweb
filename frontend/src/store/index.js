import { createLogger, createStore } from 'vuex'

import alert from './modules/alert'

const debug = import.meta.env.DEV

export default createStore({
  modules: {
    alert
  },
  strict: debug,
  plugins: debug ? [createLogger()] : []
})