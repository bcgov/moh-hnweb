import { Selector } from 'testcafe';

class AlertPage {
    constructor () {
        this.alertBannerText = Selector('div').withAttribute('class','alert-message');
    }
}

export default new AlertPage();