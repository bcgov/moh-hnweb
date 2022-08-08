import { Selector } from 'testcafe'

class ChangeEffectiveDate {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.existingEffectiveDateInput = Selector('#dp-input-existingEffectiveDate')
    this.newEffectiveDateInput = Selector('#dp-input-newEffectiveDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new ChangeEffectiveDate()
