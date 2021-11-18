import AlertPage from '../pages/AlertPage';
import CheckEligibilityPage from '../pages/eligibility/CheckEligibilityPage';
import {Role} from 'testcafe';
import { Selector } from 'testcafe';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const SUCCESS_MESSAGE = 'Search complete';
const PHN_ERROR_MESSAGE = 'PHN is required'
const SITE_UNDER_TEST = 'http://localhost:3000/eligibility/checkEligibility'

//TODO: import from roles/role.js
const regularAccUser = Role(SITE_UNDER_TEST, async t => {
    await t
        .click('#zocial-moh_idp')
        .typeText('#username', 'hnweb1')
        .typeText('#password', process.env.TESTCAFE_PASSWORD)
        .click("#kc-login");
});

fixture(`CheckEligibility Page`)
.disablePageCaching `Test CheckEligibility`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
  .page('http://localhost:3000/eligibility/checkEligibility');

test('Error when no phn and date', async t => {
    await t
        .click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

        console.log("Error when no phn and date");
});

test('Error when no date selected', async t => {
    await t 
        .typeText(CheckEligibilityPage.phnInput, '123456')
        .click(CheckEligibilityPage.eligibilityDate)
        .pressKey('tab')
        .click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

        console.log("Error when no date selected");
});

test('Error when no phn', async t => {
    await t
        .click(CheckEligibilityPage.eligibilityDate)
        .pressKey('tab')       
        .click(CheckEligibilityPage.submitButton)
        .expect(CheckEligibilityPage.ErrorText.textContent).contains(PHN_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

        console.log("Error when no phn");
});

test('Check validation passed info', async t => {

    await t
        .typeText(CheckEligibilityPage.phnInput, '9306448169')
        .click(CheckEligibilityPage.eligibilityDate)
        .pressKey('tab')
        .expect(CheckEligibilityPage.eligibilityDate.value).notEql('')
		.click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)

		console.log("Check validation passed info")
});



test('Check submitbutton is clickable', async t => {
	await t
        .typeText(CheckEligibilityPage.phnInput, '123456')  
		.click(CheckEligibilityPage.submitButton)

		console.log("testcafe clicked submit button")
});

test('Check cancelButton is clickable', async t => {
	await t
        .typeText(CheckEligibilityPage.phnInput, '123456')  
		.click(CheckEligibilityPage.cancelButton)
        .expect(CheckEligibilityPage.phnInput.value).eql('')	

		console.log("testcafe clicked cancel button")
});

