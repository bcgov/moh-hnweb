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

    this.addressTable = Selector('#addressTable')
    this.addressRow1 = Selector('#addressTable > tbody > tr')
    this.addressRow2 = Selector('#addressTable > tbody > tr').nth(1)

    this.identifierTable = Selector('#identifierTable')
    this.identifierRow1 = Selector('#identifierTable > tbody > tr')
    this.identifierRow2 = Selector('#identifierTable > tbody > tr').nth(1)
  }
}

export default new GetGroupMembersContractAddressPage()
