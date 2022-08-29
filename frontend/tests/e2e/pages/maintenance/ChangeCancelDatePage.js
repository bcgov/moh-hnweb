import { Selector } from 'testcafe'

class ChangeCancelDate {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.existingCancelDateInput = Selector('#dp-input-existingCancellationDate')
    this.newCancelDateInput = Selector('#dp-input-newCancellationDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]').withText('Clear')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new ChangeCancelDate()
