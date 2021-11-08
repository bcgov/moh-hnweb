import { Selector } from 'testcafe';

class CoverageStatusCheck {
    constructor () {
        this.phnInput = Selector('#phn'); 
        this.dateOfBirthInput  = Selector('#dateOfBirth');
        this.dateOfServiceInput  = Selector('#dateOfService');

        this.submitButton = Selector('button[type="submit"]');
        this.cancelButton = Selector('button[type="button"]');
        
    }
}

export default new CoverageStatusCheck();