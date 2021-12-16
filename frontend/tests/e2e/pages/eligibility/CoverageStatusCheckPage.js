import { Selector } from 'testcafe'

class CoverageStatusCheck {
  constructor() {
    this.phnInput = Selector('#phn')
    this.dateOfBirthInput = Selector('#dp-input-dateOfBirth')
    this.dateOfServiceInput = Selector('#dp-input-dateOfService')
    this.subsidyInsuredServiceCheckBox = Selector('#checkSubsidyInsuredService', { timeout: 1000 })
    this.dateOfLastEyeExaminationCheckBox = Selector('#checkLastEyeExam', { timeout: 1000 })
    this.patientRestrictionCheckBox = Selector('#checkPatientRestriction', { timeout: 1000 })
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')

    this.result = Selector('#result')
    this.resultRow1 = Selector('#result > div:nth-child(2)')
    this.resultRow2 = Selector('#result > div:nth-child(3)')

    this.careCardWarning = Selector('#cardCardWarning')
    this.clientInstructions = Selector('#clientInstructions')
  }
}

export default new CoverageStatusCheck()
