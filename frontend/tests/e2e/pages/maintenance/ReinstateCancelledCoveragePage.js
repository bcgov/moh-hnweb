import { Selector } from 'testcafe'

class ReinstateCancelledCoveragePage {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.cancelDateInput = Selector('#dp-input-cancelDateToBeRemoved')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]').withText('Clear')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new ReinstateCancelledCoveragePage()
