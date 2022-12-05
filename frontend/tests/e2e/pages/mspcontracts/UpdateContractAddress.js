import { Selector } from 'testcafe'

class UpdateContractAddress {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.telephoneInput = Selector('#telephone')

    this.address1Input = Selector('#addressLine1')
    this.address2Input = Selector('#addressLine2')
    this.address3Input = Selector('#city')
    this.address4Input = Selector('#province')
    this.postalCodeInput = Selector('#postalCode')
    this.mailingAddress1Input = Selector('#mailingAddress1')
    this.mailingAddress2Input = Selector('#mailingAddress2')
    this.mailingAddress3Input = Selector('#mailingCity')
    this.mailingAddress4Input = Selector('#mailingProvince')
    this.mailingPostalCodeInput = Selector('#mailingPostalCode')

    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]').withText('Clear')
  }
}

export default new UpdateContractAddress()
