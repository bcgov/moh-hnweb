import HomePage from '../pages/HomePage.js';

fixture(`Home Page`)
  .page('http://localhost:3000');

test('Checking Home tab is clickable ', async t => {	  
	await t	       
		.click(HomePage.homeLink);			  
		console.log("testcafe clicked hometab ")
});

test('Check heading exist on Home page! ', async tc => {  
	await tc
		.click(HomePage.homeLink)
		.expect(HomePage.heading.innerText).eql("Welcome to HN Web")
});

