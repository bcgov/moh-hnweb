import { SITE_UNDER_TEST } from '../../configuration'
import LoginPage from '../../pages/welcome/LoginPage.js'

fixture(`Login Page`).disablePageCaching`Test Login`
  // Auth/role is not required for this test
  .page(SITE_UNDER_TEST)

test('Check Welcome menu is clickable ', async (t) => {
  await t.click(LoginPage.welcomeLink)
})

test('Check that the page content is correct', async (t) => {
  await t.expect(LoginPage.menu.exists).ok().expect(LoginPage.menuLinks.count).eql(5).expect(LoginPage.loginButtons.exists).ok()
})

test('Check the Health Authority login is correct', async (t) => {
  await t.click(LoginPage.healthAuthorityIDButton).wait(1000).expect(LoginPage.healthAuthorityHeader.innerText).eql('QA STS HEALTHBC').expect(LoginPage.healthAuthorityUserName.exists).ok()
})

test('Check that the IDIR login is correct', async (t) => {
  await t.click(LoginPage.idirButton).wait(1000).expect(LoginPage.idirHeader.innerText).eql('Log in with ').expect(LoginPage.idirLogo.exists).ok().expect(LoginPage.idirUsername.exists).ok()
})

test('Check that the Keycloak login is correct', async (t) => {
  await t.click(LoginPage.keycloakButton).wait(1000).expect(LoginPage.keycloakHeader.innerText).eql('Keycloak Login').expect(LoginPage.keycloakUsername.exists).ok()
})
