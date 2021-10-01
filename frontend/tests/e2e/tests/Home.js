import HomePage from '../pages/HomePage.js';

fixture(`Home Page`)
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

