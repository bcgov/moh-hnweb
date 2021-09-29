import HomePage from '../pages/HomePage.js';

fixture(`Home Page`)
  .page('http://localhost:3000');

test('click home ', async t => {	  
		await t
			.setNativeDialogHandler(() => true)        
			.click(HomePage.homeLink);			  
			console.log("testcafe clicked hometab ")
	});

