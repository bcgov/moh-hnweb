import { createLogger, createStore as createVuexStore } from 'vuex'

import alert from './modules/alert'
import auth from './modules/auth'
import studyPermitHolder from './modules/studyPermitHolder'

const debug = import.meta.env.DEV

export const createStore = (app) => {
  console.log('createStore ')
  return createVuexStore({
    modules: {
      alert,
      studyPermitHolder,
      auth: auth(app),
    },
    strict: debug,
    plugins: debug ? [createLogger()] : [],
  })
}
