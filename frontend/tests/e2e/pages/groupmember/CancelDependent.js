import { Selector } from 'testcafe'

class CancelDependent {
  constructor() {
    this.phnInput = Selector('#phn')
    this.dependentPhnInput = Selector('#dependentPhn')
    this.groupNumberInput = Selector('#groupNumber')
    this.relationshipInput = Selector('#relationship')
    this.cancelDateInput = Selector('#dp-input-cancelDate')
    this.cancelReasonInput = Selector('#cancelReason')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.divSelectedDate = Selector('div').withAttribute('class', 'dp__overlay_cell_active dp__overlay_cell_pad')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new CancelDependent()
