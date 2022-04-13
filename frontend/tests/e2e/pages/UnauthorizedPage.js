import { Selector } from 'testcafe'

class HomePage {
  constructor() {
    this.title = Selector('h1')
    this.transferButton = Selector('#transfer')
    this.moreInfoButton = Selector('#moreInfo')
    // User Transfer app
    this.userTransferTitle = Selector('h1')
    // More Info (MSP Direct page)
    this.moreInfoTitle = Selector('h1')
    this.moreInfoText = Selector('div#body > p')
  }
}

export default new HomePage()
