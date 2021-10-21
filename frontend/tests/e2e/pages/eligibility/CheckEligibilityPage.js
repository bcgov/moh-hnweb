import { Selector } from 'testcafe';

class CheckEligibility {
    constructor () {
        this.phnInput = Selector('#phn'); 
        this.eligibilityDate  = Selector('#eligibilityDate');

        this.submitButton = Selector('button[type="submit"]');
        this.cancelButton = Selector('button[type="button"]');
        
    }
}

export default new CheckEligibility();