import { Selector } from 'testcafe';

class PersonDetails {
    constructor () {
        this.phnInput = Selector('#phn'); 
        this.submitButton = Selector('button[type="submit"]');
        this.cancelButton = Selector('button[type="button"]');        
        this.ErrorText = Selector('div').withAttribute('class','error-text');
        
    }
}

export default new PersonDetails();