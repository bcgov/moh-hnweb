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
    this.phnOutput = Selector('#confirmation > p').withText('PHN')
    this.groupMemberOutput = Selector('#confirmation > p').withText('Group Number')
    this.groupMemberNumberOutput = Selector('#confirmation > p').withText('Group Member Number')
    this.departmentNumberOutput = Selector('#confirmation > p').withText('Department Number')
    this.updateAnotherButton = Selector('#confirmation > button[type="button"]')
  }
}

export default new UpdateNumberAndDept()
