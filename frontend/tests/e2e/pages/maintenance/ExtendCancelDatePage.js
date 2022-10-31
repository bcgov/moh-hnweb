import { Selector } from 'testcafe'

class ExtendCancelDate {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.existingCancelDateInput = Selector('#dp-input-existingCancellationDate')
    this.newCancelDateInput = Selector('#dp-input-newCancellationDate')
    this.immigrationCodeSelect = Selector('#immigrationCode')
    this.permitIssueDate = Selector('#dp-input-permitIssueDate')
    this.permitExpiryDate = Selector('#dp-input-permitExpiryDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]').withText('Clear')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new ExtendCancelDate()
