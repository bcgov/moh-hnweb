import { Selector } from 'testcafe'

class UpdateContractAddress {
  constructor() {
    this.groupNumberInput = Selector('#groupNumber')
    this.phnInput = Selector('#phn')
    this.telephoneInput = Selector('#telephone')

    this.address1Input = Selector('#addressLine1')
    this.address2Input = Selector('#addressLine2')
    this.homeAddressCityInput = Selector('#city')
    this.homeAddressProvinceInput = Selector('#province')
    this.homeAddressCountryInput = Selector('#country')
    this.postalCodeInput = Selector('#postalCode')
    this.mailingAddress1Input = Selector('#mailingAddress1')
    this.mailingAddress2Input = Selector('#mailingAddress2')
    this.mailingAddressCityInput = Selector('#mailingCity')
    this.mailingAddressProvinceInput = Selector('#mailingProvince')
    this.mailingPostalCodeInput = Selector('#mailingPostalCode')
    this.mailingAddressCountrySelect = Selector('#mailingCountry')
    this.errorText = Selector('div').withAttribute('class', 'error-text')

    this.submitButton = Selector('button[type="submit"]')
    this.clearButton = Selector('button[type="button"]').withText('Clear')
  }
}

export default new UpdateContractAddress()
