import { Selector } from 'testcafe';

class AddVisaResidentWithoutPHN {
    constructor () {
        this.surnameInput = Selector('#surname'); 
        this.firstNameInput = Selector('#givenName');
        this.secondNameInput = Selector('#secondName');
        this.dateOfBirthInput  = Selector('#dp-input-dateOfBirth');
        this.radioButton = Selector('#gender');
        
        this.groupNumberInput = Selector('#groupNumber');
        this.immigrationCodeSelect  = Selector('#immigrationCode');
        this.groupMemberNumberInput = Selector('#groupMemberNumber');
        this.visaIssueDateInput = Selector('#dp-input-permitIssueDate');
        this.departmentNumberInput = Selector('#departmentNumber');
        this.visaExpiryDateInput = Selector('#dp-input-permitExpiryDate');
        this.residenceDateInput = Selector('#dp-input-residenceDate');  
        this.coverageEffectiveDateInput = Selector('#dp-input-coverageEffectiveDate');
        this.telephoneInput = Selector('#telephone');
        this.coverageCancellationDateInput = Selector('#dp-input-coverageCancellationDate');
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