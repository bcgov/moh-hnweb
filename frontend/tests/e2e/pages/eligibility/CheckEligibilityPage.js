import { Selector } from 'testcafe'

class CheckEligibility {
  constructor() {
    this.phnInput = Selector('#phn')
    this.eligibilityDate = Selector('div#eligibilityDate > div > div > div > input')
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')

    this.result = Selector('#result')
    this.resultRow1 = Selector('#result > div:nth-child(2)')
    this.resultRow2 = Selector('#result > div:nth-child(3)')

    this.clientInstructions = Selector('#clientInstructions')
  }
}

export default new CheckEligibility()
