import { Selector } from 'testcafe'

class GetContractPeriods {
  constructor() {
    this.phnInput = Selector('#phn')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.resultsTable = Selector('#resultsTable')
    this.resultsRow1 = Selector('#resultsTable > tbody > tr')
    this.resultsRow4 = Selector('#resultsTable > tbody > tr').nth(3)
  }
}

export default new GetContractPeriods()
