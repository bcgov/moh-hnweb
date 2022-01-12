import { Selector } from 'testcafe';

class CancelGroupMember {
    constructor () {
        this.phnInput = Selector('#phn')
        this.groupNumberInput = Selector('#groupNumber')
        this.cancelDateInput = Selector('#dp-input-cancelDate')
        this.cancelReasonInput = Selector('#cancelReason')
        this.submitButton = Selector('button[type="submit"]')
        this.clearButton = Selector('button[type="button"]')
        this.errorText = Selector('div').withAttribute('class','error-text');

    }
}

export default new CancelGroupMember();