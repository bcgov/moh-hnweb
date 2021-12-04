import { Selector } from 'testcafe';

class PhnInquiryPage {
    constructor () {
        this.phnInput1 = Selector('#phn1');
        this.phnInput2 = Selector('#phn2');
        this.phnInput3 = Selector('#phn3');
        this.phnInput4 = Selector('#phn4');
        this.phnInput5 = Selector('#phn5');
        this.phnInput6 = Selector('#phn6');
        this.phnInput7 = Selector('#phn7');
        this.phnInput8 = Selector('#phn8');
        this.phnInput9 = Selector('#phn9');
        this.phnInput10 = Selector('#phn10');

        this.ErrorText = Selector('div').withAttribute('class','error-text');

        this.submitButton = Selector('button[type="submit"]');
        this.cancelButton = Selector('button[type="button"]');
    }
}

export default new PhnInquiryPage();