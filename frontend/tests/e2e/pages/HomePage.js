import { Selector } from 'testcafe';

class HomePage {
    constructor () {
        this.homeLink = Selector('#home-link'); 
        this.heading  = Selector('h1');  
    }
}

export default new HomePage();