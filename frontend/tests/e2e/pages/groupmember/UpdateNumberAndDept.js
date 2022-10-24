import { Selector } from 'testcafe'

class UpdateNumberAndDept {
  constructor() {
    this.phnInput = Selector('#phn')
    this.groupMemberInput = Selector('#groupNumber')
    this.groupMemberNumberInput = Selector('#groupMemberNumber')
    this.departmentNumberInput = Selector('#departmentNumber')
    this.submitButton = Selector('#updateNumberAndDept > form > div > button[type="submit"]')
    this.clearButton = Selector('#updateNumberAndDept > form > div > button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
    this.groupMemberOutput = Selector('#confirmation > p > ul > li').nth(0)
    this.phnOutput = Selector('#confirmation > p > ul > li').nth(1)
    this.groupMemberNumberOutput = Selector('#confirmation > p > ul > li').nth(2)
    this.departmentNumberOutput = Selector('#confirmation > p > ul > li').nth(3)
    this.updateAnotherButton = Selector('#confirmation > button[type="button"]')
  }
}

export default new UpdateNumberAndDept()
