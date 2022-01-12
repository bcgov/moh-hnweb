import { createLogger, createStore } from 'vuex'

import alert from './modules/alert'
import studyPermitHolder from './modules/studyPermitHolder'
import auth from './modules/auth'

const debug = import.meta.env.DEV

export default createStore({
  modules: {
    alert,
    studyPermitHolder,
    auth,
  },
  strict: debug,
  plugins: debug ? [createLogger()] : [],
})
