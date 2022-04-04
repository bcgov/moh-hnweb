import { Selector } from 'testcafe'

class HomePage {
  constructor() {
    this.overview = Selector('#overview')
    this.profileTable = Selector('#overview > table')
    this.profileRows = Selector('#overview > table > tbody > tr')
    this.credentials = Selector('#credentials')
    this.credentialsHeaders = Selector('#credentials > h3')
  }
}

export default new HomePage()
