import { createLogger, createStore } from 'vuex'

import alert from './modules/alert'
import studyPermitHolder from './modules/studyPermitHolder'

const debug = import.meta.env.DEV

export default createStore({
  modules: {
    alert,
    studyPermitHolder,
  },
  strict: debug,
  plugins: debug ? [createLogger()] : [],
})
