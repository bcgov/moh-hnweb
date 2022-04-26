import { Selector } from 'testcafe'

class LoginPage {
  constructor() {
    this.welcomeLink = Selector('#welcome-link')
    this.menu = Selector('ul.leftNav')
    this.menuLinks = Selector('ul.leftNav > li')
    this.loginButtons = Selector('#loginButtons')
    this.healthAuthorityIDButton = Selector('#phsaLogin')
    this.idirButton = Selector('#idirLogin')
    this.keycloakButton = Selector('#moh_idpLogin')

    // Health Authority Login
    this.healthAuthorityHeader = Selector('#header > h4')
    this.healthAuthorityUserName = Selector('#userNameInput')

    // IDIR Login
    this.idirHeader = Selector('div.panel-heading')
    this.idirLogo = Selector('#idirLogo')
    this.idirUsername = Selector('#user')

    // Keycloak Login
    this.keycloakHeader = Selector('header > h1')
    this.keycloakUsername = Selector('#username')
  }
}

export default new LoginPage()
