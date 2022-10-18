import { Selector } from 'testcafe'

class RenewCancelledCoverage {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.newEffectiveDateInput = Selector('#dp-input-newCoverageEffectiveDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]').withText('Clear')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new RenewCancelledCoverage()
