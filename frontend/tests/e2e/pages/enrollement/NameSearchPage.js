import { Selector } from 'testcafe';

class NameSearch {
    constructor () {
        this.surnameInput = Selector('#surname'); 
        this.firstNameInput = Selector('#firstName');
        this.secondNameInput = Selector('#secondName');
        this.dateOfBirthInput  = Selector('div#dateOfBirth > div > div > div > input');
        this.radioButton = Selector('#gender');

        this.submitButton = Selector('button[type="submit"]');
        this.addButton = Selector('button[type="add"]');
        //this.clearButton = Selector('button[type="button"]');        
        this.errorText = Selector('div').withAttribute('class','error-text');
        
    }
}

export default new NameSearch();