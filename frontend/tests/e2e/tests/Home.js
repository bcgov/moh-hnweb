import HomePage from '../pages/HomePage.js';
import {Role} from 'testcafe';

const SITE_UNDER_TEST = 'http://localhost:3000'

//TODO:import regularAccUser  from '../roles/roles';
const regularAccUser = Role(SITE_UNDER_TEST, async t => {
    await t
        .click('#zocial-moh_idp')
        .typeText('#username', 'hnweb1')
        .typeText('#password', process.env.TESTCAFE_PASSWORD)
        .click("#kc-login");
});
fixture(`Home Page`)
.disablePageCaching `Test Home Page`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
  .page('http://localhost:3000');

test('Check Home tab is clickable ', async t => {	  
	await t	       
		.click(HomePage.homeLink);
					  
		console.log("testcafe clicked hometab ")
});

test('Check heading exist on Home page! ', async tc => {  
	await tc
		.click(HomePage.homeLink)
		.expect(HomePage.heading.innerText).eql("Welcome to HN Web")
});

test('Check Keycloak dev tool checkbox is checked/unchecked ', async tc => {  
	await tc
		.click(HomePage.homeLink)
		.click(HomePage.checkbox)  //Check box
        .expect(HomePage.checkbox.checked).ok()  //Confirm whether the option is selected
        .click(HomePage.checkbox)
        .expect(HomePage.checkbox.checked).notOk();
});

