import { Selector } from 'testcafe';

class HomePage {
    constructor () {
        this.homeLink = Selector('#home-link'); 
        this.heading  = Selector('h1');
        this.checkbox = Selector('#showKeycloakTools')  ;
    }
}

export default new HomePage();