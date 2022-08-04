import { Selector } from 'testcafe'

class ReinstateOverAgeDependent {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.dependentPhnInput = Selector('#dependentPhn')
    this.dependentDateOfBirthInput = Selector('#dp-input-dependentDateOfBirth')
    this.isStudentRadioButton = Selector('#isStudent')
    this.isStudentNoRadioButton = Selector('#N')
    this.studentEndDateInput = Selector('#studentEndDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
  }
}

export default new ReinstateOverAgeDependent()
