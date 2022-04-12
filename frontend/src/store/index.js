import { createLogger, createStore } from 'vuex'

import alert from './modules/alert'
import auth from './modules/auth'
import studyPermitHolder from './modules/studyPermitHolder'

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
