import { SITE_UNDER_TEST } from '../configuration'
import UnauthorizedPage from '../pages/UnauthorizedPage.js'
import { regularAccUser } from '../roles/roles'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/unauthorized'

fixture(`Unauthorized Page`).disablePageCaching`Test Unauthorized Page`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check Page content ', async (t) => {
  await t.expect(UnauthorizedPage.title.innerText).eql('Service Permissions not assigned')
})

test('Check Transfer button ', async (tc) => {
  await tc.click(UnauthorizedPage.transferButton).expect(UnauthorizedPage.userTransferTitle.innerText).eql('Welcome to the MSP Direct Account Transfer')
})

test('Check More Info button ', async (tc) => {
  await tc.click(UnauthorizedPage.moreInfoButton).expect(UnauthorizedPage.moreInfoTitle.innerText).eql('MSP Direct').expect(UnauthorizedPage.moreInfoText.innerText).contains('MSP Direct is an online business service,')
})
