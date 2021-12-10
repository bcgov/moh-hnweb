import { Selector } from 'testcafe';

class PersonDetails {
    constructor () {
        this.phnInput = Selector('#phn'); 
        this.submitButton = Selector('button[type="submit"]');
        this.clearButton = Selector('button[type="button"]');        
        this.errorText = Selector('div').withAttribute('class','error-text');
        
    }
}

export default new PersonDetails();