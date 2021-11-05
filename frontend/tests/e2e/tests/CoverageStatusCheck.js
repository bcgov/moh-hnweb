import AlertPage from '../pages/AlertPage';
import CheckEligibilityPage from '../pages/eligibility/CoverageStatusCheckPage';
import CoverageStatusCheckPage from '../pages/eligibility/CoverageStatusCheckPage';
import {Role} from 'testcafe';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const SUCCESS_MESSAGE = 'Search complete';
const SITE_UNDER_TEST = 'http://localhost:3000/eligibility/coverageStatusCheck'

//TODO: import from roles/role.js
const regularAccUser = Role(SITE_UNDER_TEST, async t => {
    await t
        .click('#zocial-moh_idp')
        .typeText('#username', 'hnweb1')
        .typeText('#password', process.env.TESTCAFE_PASSWORD)
        .click("#kc-login");
});

fixture(`Coverage Status Check Page`)
.disablePageCaching `Test Coverage Status Check`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
  .page('http://localhost:3000/eligibility/coverageStatusCheck');

test('Error when no phn, dateOfBirth and dateOfService', async t => {
    await t
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

        console.log("Error when no phn dateOfbirth and dateOfService provided");
});

test('Error when no phn selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '2021-10-10')
        .typeText(CoverageStatusCheckPage.dateOfServiceInput, '2021-10-10')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no phn selected");
});

test('Error when no dateOfBirth selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456')
        .typeText(CoverageStatusCheckPage.dateOfServiceInput, '2021-10-10')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no date of birth selected");
});

test('Error when no dateOfService selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456')
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '2021-10-10')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no date of service selected");
});

test('Error when no phn and dateOfService selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '2021-10-10')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no phn and date of service selected");
});

test('Error when no phn and dateofBirth selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.dateOfServiceInput, '2021-10-10')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no phn and date of birth selected");
});



test('Check validation passed info', async t => {

    await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456')
        .typeText(CoverageStatusCheckPage.dateOfBirthInput,'2021-10-10')
        .expect(CoverageStatusCheckPage.dateOfBirthInput.value).eql('2021-10-10')
        .typeText(CoverageStatusCheckPage.dateOfServiceInput,'2021-10-10')
        .expect(CoverageStatusCheckPage.dateOfServiceInput.value).eql('2021-10-10')
		.click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)

		console.log("Check validation passed info")
});

test('Check submitbutton is clickable', async t => {
	await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456')  
		.click(CoverageStatusCheckPage.submitButton)

		console.log("testcafe clicked submit button")
});

test('Check cancelButton is clickable', async t => {
	await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456') 
        .typeText(CoverageStatusCheckPage.dateOfBirthInput,'2021-10-10')
        .typeText(CoverageStatusCheckPage.dateOfServiceInput,'2021-10-10') 
		.click(CoverageStatusCheckPage.cancelButton)
        .expect(CoverageStatusCheckPage.phnInput.value).eql('')
        .expect(CoverageStatusCheckPage.dateOfBirthInput.value).eql('')	
        .expect(CoverageStatusCheckPage.dateOfServiceInput.value).eql('')	

		console.log("testcafe clicked cancel button")
});

