import { Selector } from 'testcafe';

class NameSearch {
    constructor () {
        this.surnameInput = Selector('#surname'); 
        this.firstNameInput = Selector('#firstName');
        this.secondNameInput = Selector('#secondName');
        this.dateOfBirthInput  = Selector('#dp-input-dateOfBirth');
        this.radioButton = Selector('#gender');

        this.submitButton = Selector('button[type="submit"]');
        this.addButton = Selector('button[type="button"]').withText('Add');
        this.clearButton = Selector('button[type="button"]')        
        this.errorText = Selector('div').withAttribute('class','error-text');
        this.numberOfMatchesText =  Selector('p').withText('Number of Matches: 5');
        
    }
}

export default new NameSearch();