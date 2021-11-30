import { Selector } from 'testcafe'

class PhnLookupPage {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.contractNumberInput = Selector('#contractNumber')

    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')

    this.resultsTable = Selector('#resultsTable')
    this.resultsRow1 = Selector('#resultsTable > tbody > tr')
  }
}

export default new PhnLookupPage()