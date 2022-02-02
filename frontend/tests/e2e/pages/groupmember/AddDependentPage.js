import { Selector } from 'testcafe'

class AddDependent {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.coverageEffectiveDateInput = Selector('#dp-input-coverageEffectiveDate')
    this.phnInput = Selector('#phn')
    this.dependentPhnInput = Selector('#dependentPhn')
    this.relationshipSelect = Selector('#relationship')
    this.isStudentRadioButton = Selector('#isStudent')
    this.isStudentNoRadioButton = Selector('#N')
    this.studentEndDateInput = Selector('#studentEndDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new AddDependent()
