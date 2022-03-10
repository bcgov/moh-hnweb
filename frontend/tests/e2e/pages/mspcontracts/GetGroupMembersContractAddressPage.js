import { Selector } from 'testcafe'

class GetGroupMembersContractAddressPage {
  constructor() {
    this.phnInput = Selector('#phn')
    this.groupNumberInput = Selector('#groupNumber')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.resultsTable = Selector('#resultsTable')
    this.resultsRow1 = Selector('#resultsTable > tbody > tr')
    this.personInfo = Selector('div').withAttribute('class', 'col col3')
  }
}

export default new GetGroupMembersContractAddressPage()
