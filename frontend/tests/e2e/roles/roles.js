import { Role } from 'testcafe'

import { SITE_UNDER_TEST } from '../configuration.js'

export const regularAccUser = Role(SITE_UNDER_TEST, async (t) => {
  await t.click('#moh_idpLogin').typeText('#username', 'hnweb1').typeText('#password', process.env.HNWEB1_TESTCAFE_PASSWORD).click('#kc-login')
})
