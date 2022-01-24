import { Selector } from 'testcafe';

class AddGroupMember {
    constructor () {
        
        this.groupNumberInput = Selector('#groupNumber');
        this.immigrationCodeSelect  = Selector('#immigrationCode');
        this.groupMemberNumberInput = Selector('#groupMemberNumber');   
        this.departmentNumberInput = Selector('#departmentNumber');       
        this.coverageEffectiveDateInput = Selector('#dp-input-coverageEffectiveDate');
        this.telephoneInput = Selector('#telephone');
        this.address1Input = Selector('#address1');
        this.address2Input = Selector('#address2');
        this.address3Input = Selector('#address3');
        this.address4Input = Selector('#address4');
        this.postalCodeInput = Selector('#postalCode');
        this.mailingAddress1Input = Selector('#mailingAddress1');
        this.mailingAddress2Input = Selector('#mailingAddress2');
        this.mailingAddress3Input = Selector('#mailingAddress3');
        this.mailingAddress4Input = Selector('#mailingAddress4');
        this.mailingPostalCodeInput = Selector('#mailingPostalCode');
        this.spouse = Selector('#spousePhn');
        this.dependentPhn = Selector('#dependentPhn');
        this.dependentList = Selector('li');

        this.errorText = Selector('div').withAttribute('class','error-text');

        this.submitButton = Selector('button[type="submit"]');
        this.clearButton = Selector('button[type="button"]').withText('Clear');
        this.addButton = Selector('button[type="button"]').withText('Add');
        this.removeIcon = Selector('font-awesome-icon');
        
    }
}

export default new AddGroupMember();