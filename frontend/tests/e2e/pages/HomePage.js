import { Selector } from 'testcafe'

class HomePage {
  constructor() {
    this.homeLink = Selector('#home-link')
    this.bulletins = Selector('#bulletins')
    this.bulletin = Selector('#bulletins > div:last-child')
    this.heading = Selector('h1')
    this.checkbox = Selector('#showKeycloakTools')
  }
}

export default new HomePage()
