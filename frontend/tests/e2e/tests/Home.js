import HomePage from '../pages/HomePage.js';
import { regularAccUser } from '../roles/roles';
import { SITE_UNDER_TEST } from '../configuration';

fixture(`Home Page`)
.disablePageCaching `Test Home Page`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
   .page(SITE_UNDER_TEST);

test('Check Home tab is clickable ', async t => {	  
	await t	       
		.click(HomePage.homeLink);
});

test('Check heading exist on Home page! ', async tc => {  
	await tc
		.click(HomePage.homeLink)
		.expect(HomePage.heading.innerText).eql("Welcome to HN Web")
});

