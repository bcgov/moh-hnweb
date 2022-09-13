import { Selector } from 'testcafe'

class AuditReportingPage {
  constructor() {
    this.userIdInput = Selector('#userId')
    this.organizationsInput = Selector('[for="00000010"]')
    this.checkBoxInput1 = Selector('[for="00000010"]')
    this.checkBoxInput2 = Selector('[for="CheckEligibility"]')
    this.checkBoxInput3 = Selector('[for="PHNInquiry"]')
    this.startDateInput = Selector('#dp-input-startDate')
    this.endDateInput = Selector('#dp-input-endDate')
    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.resultsTable = Selector('#resultsTable > div > table')
    this.resultsRow1 = Selector('#resultsTable > div > table > tbody > tr')
  }
}

export default new AuditReportingPage()
