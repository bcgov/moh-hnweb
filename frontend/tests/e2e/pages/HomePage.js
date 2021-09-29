import { Selector } from 'testcafe';

class HomePage {
    constructor () {
        this.homeLink = Selector('#home-link'); 
    }
}

export default new HomePage();