import { Selector } from 'testcafe';

class AddVisaResidentWithoutPHN {
    constructor () {
        this.surnameInput = Selector('#surname'); 
        this.firstNameInput = Selector('#firstName');
        this.secondNameInput = Selector('#secondName');
        this.dateOfBirthInput  = Selector('div#dateOfBirth > div > div > div > input');
        this.radioButton = Selector('#gender');
        
        this.groupNumberInput = Selector('#groupNumber');
        this.immigrationCodeSelect  = Selector('#immigrationCode');
        this.groupMemberNumberInput = Selector('#groupMemberNumber');
        this.visaIssueDateInput = Selector('div#visaIssueDate > div > div > div > input');
        this.departmentNumberInput = Selector('#departmentNumber');
        this.visaExpiryDateInput = Selector('div#visaExpiryDate > div > div > div > input');
        this.residenceDateInput = Selector('div#residenceDate > div > div > div > input'); 
        this.coverageEffectiveDateInput = Selector('div#coverageEffectiveDate > div > div > div > input');
        this.telephoneInput = Selector('#telephone');
        this.coverageCancellationDateInput = Selector('div#coverageCancellationDate > div > div > div > input');
        this.address1Input = Selector('#address1');
        this.address2Input = Selector('#address2');
        this.address3Input = Selector('#address3');
        this.cityInput = Selector('#city');
        this.provinceSelect = Selector('#province');
        this.postalCodeInput = Selector('#postalCode');
        this.mailingAddress1Input = Selector('#mailingAddress1');
        this.mailingAddress2Input = Selector('#mailingAddress2');
        this.mailingAddress3Input = Selector('#mailingAddress3');
        this.mailingCityInput = Selector('#mailingCity');
        this.mailingProvinceInput = Selector('#mailingProvince');
        this.mailingPostalCodeInput = Selector('#mailingPostalCode');
        this.priorResidenceCodeInput = Selector('#priorResidenceCode');
        this.otherProvinceHealthcareNumberInput = Selector('#otherProvinceHealthcareNumber');

        this.errorText = Selector('div').withAttribute('class','error-text');

        this.submitButton = Selector('button[type="submit"]');
        this.clearButton = Selector('button[type="button"]');
        
    }
}

export default new AddVisaResidentWithoutPHN();