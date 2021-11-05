import AlertPage from '../pages/AlertPage';
import CheckEligibilityPage from '../pages/eligibility/CheckEligibilityPage';
import {Role} from 'testcafe';
import { Selector } from 'testcafe';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const SUCCESS_MESSAGE = 'Search complete';
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
        .debug()
        .typeText(Selector('phn'), '123456')
        .debug()
        .click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

        console.log("Error when no date selected");
});

test('Error when no phn', async t => {
    await t
        .debug()
        .typeText(CheckEligibilityPage.eligibilityDate, '10/12/2021')
        .click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

        console.log("Error when no phn");
});

test('Check validation passed info', async t => {

    await t
        .typeText(CheckEligibilityPage.phnInput, '123456')
        .typeText(CheckEligibilityPage.eligibilityDate,'2021-10-10')
        .expect(CheckEligibilityPage.eligibilityDate.value).eql('2021-10-10')
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
        .expect(CheckEligibilityPage.eligibilityDate.value).eql('')	

		console.log("testcafe clicked cancel button")
});

