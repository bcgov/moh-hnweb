import { SITE_UNDER_TEST } from '../../configuration'
import CredentialsInfoPage from '../../pages/welcome/CredentialsInfoPage.js'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/welcome/credentialsInfo'

fixture(`Credentials Info`).disablePageCaching`Test Credentials Info`
  // Auth/role is not required for this test
  .page(PAGE_TO_TEST)

test('Check content ', async (t) => {
  await t.expect(CredentialsInfoPage.overview.exists).ok().expect(CredentialsInfoPage.profileTable.exists).ok().expect(CredentialsInfoPage.profileRows.count).eql(4).expect(CredentialsInfoPage.credentials.exists).ok().expect(CredentialsInfoPage.credentialsHeaders.count).eql(4)
})
