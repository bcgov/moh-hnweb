import { Selector } from 'testcafe';

class CheckEligibility {
    constructor () {
        this.phnInput = Selector('#phn'); 
        this.eligibilityDate  = Selector('div#eligibilityDate > div > div > div > input');

        this.submitButton = Selector('button[type="submit"]');
        this.cancelButton = Selector('button[type="button"]');
        
    }
}

export default new CheckEligibility();