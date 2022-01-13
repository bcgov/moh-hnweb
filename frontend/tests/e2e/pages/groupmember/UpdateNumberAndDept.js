import { Selector } from 'testcafe';

class UpdateNumberAndDept {
    constructor () {
        this.phnInput = Selector('#phn'); 
        this.groupMemberInput = Selector('#groupNumber');
        this.groupMemberNumberInput = Selector('#groupMemberNumber');
        this.departmentNumberInput = Selector('#departmentNumber')
        this.submitButton = Selector('button[type="submit"]');
        this.clearButton = Selector('button[type="button"]');        
        this.errorText = Selector('div').withAttribute('class','error-text');
        
    }
}

export default new UpdateNumberAndDept();