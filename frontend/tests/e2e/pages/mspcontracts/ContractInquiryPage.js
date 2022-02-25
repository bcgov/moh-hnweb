import { Selector } from 'testcafe'

class ContractInquiry {
  constructor() {
    this.phnInput = Selector('#phn')
    this.groupNumberInput = Selector('#groupNumber')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.personInfo = Selector('div').withAttribute('class', 'col col3')
    this.resultsTable = Selector('#resultsTable')
    this.resultsRow1 = Selector('#resultsTable > tbody > tr')
    this.resultsRow3 = Selector('#resultsTable > tbody > tr').nth(2)
  }
}

export default new ContractInquiry()
