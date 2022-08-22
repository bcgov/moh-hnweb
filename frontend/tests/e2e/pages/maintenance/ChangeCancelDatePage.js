import { Selector } from 'testcafe'

class ChangeCancelDate {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.existingCancelDateInput = Selector('#dp-input-existingCancelDate')
    this.newCancelDateInput = Selector('#dp-input-newCancelDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new ChangeCancelDate()
