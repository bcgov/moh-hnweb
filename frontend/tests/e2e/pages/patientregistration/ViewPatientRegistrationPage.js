import { Selector } from 'testcafe'

class ViewPatientRegistrationPage {
  constructor() {
    this.phnInput = Selector('#phn')
    this.payeeInput = Selector('#payee')
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')

    this.patientDemoDetail = Selector('#patientDetail')
    this.registrationData = Selector('#registrationData')

    this.resultRow1 = Selector('div').withAttribute('class', 'row').nth(3)
    this.resultRow2 = Selector('div').withAttribute('class', 'row detailsRow').nth(0)

    this.clientInstructions = Selector('#clientInstructions')
    this.additionalInfoMessage = Selector('div').withAttribute('class', 'info-message')
  }
}

export default new ViewPatientRegistrationPage()
