import { SITE_UNDER_TEST } from '../configuration'
import HomePage from '../pages/HomePage.js'
import { regularAccUser } from '../roles/roles'

fixture(`Home Page`).disablePageCaching`Test Home Page`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser)
  })
  .page(SITE_UNDER_TEST)

test('Check Home tab is clickable ', async (t) => {
  await t.click(HomePage.homeLink)
})

test('Check Bulletins show ', async (t) => {
  await t
    .expect(HomePage.bulletins.exists)
    .ok()
    // This requires specific data in the DB
    // insert into mspdirect.bulletin (start_date, end_date, content) values ('2022-01-01', '2042-12-31', 'This bulletin is for TestCafe')
    .expect(HomePage.bulletin.child('div').child('div').child('p').textContent)
    .eql('This bulletin is for TestCafe')
})

test('Check heading exists on Home page! ', async (tc) => {
  await tc.click(HomePage.homeLink).expect(HomePage.heading.innerText).eql('Welcome to the New MSP Direct')
})
