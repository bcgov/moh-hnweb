import AlertPage from '../pages/AlertPage';
import CheckEligibilityPage from '../pages/eligibility/CheckEligibilityPage';
import { regularAccUser } from '../roles/roles';
import { SITE_UNDER_TEST } from '../configuration';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const SUCCESS_MESSAGE = 'Search complete';
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const DATE_TO_CHECK_REQUIRED_MESSAGE = 'Date to Check is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid';

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/checkEligibility'

fixture(`CheckEligibility Page`)
.disablePageCaching `Test CheckEligibility`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST);

test('Check required fields validation', async t => {
    await t
        // Given required fields aren't filled out (phn, date to check)
        .selectText(CheckEligibilityPage.eligibilityDate)
        .pressKey('delete')
        .pressKey('tab')
        // When I click the submit button
        .click(CheckEligibilityPage.submitButton)
        // I expect an error message stating the page had errors and individual error messages for each required field
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
        .expect(CheckEligibilityPage.ErrorText.nth(0).textContent).contains(PHN_REQUIRED_MESSAGE)
        .expect(CheckEligibilityPage.ErrorText.nth(1).textContent).contains(DATE_TO_CHECK_REQUIRED_MESSAGE)
});

test('Check invalid phn format validation', async t => {
    await t
        // Given a PHN entered with an invalid format
        .typeText(CheckEligibilityPage.phnInput, '9000448000')
        // When I click the submit button
		.click(CheckEligibilityPage.submitButton)
        // I expect an error message stating the page had errors and an individual error message for the PHN format
        .expect(CheckEligibilityPage.ErrorText.nth(0).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Check properly filled form passes validation', async t => {
    await t
        // Given the page is filled out correctly
        .typeText(CheckEligibilityPage.phnInput, '9306448169')
        .click(CheckEligibilityPage.eligibilityDate)
        .pressKey('tab')
        // When I click the submit button
		.click(CheckEligibilityPage.submitButton)
        // I expect a success message
        .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
});

test('Check clear button clears the form', async t => {
	await t
        // Given I have a form filled out with data
        .typeText(CheckEligibilityPage.phnInput, '9000448000')
        // When I click the cancel button
		.click(CheckEligibilityPage.cancelButton)
        // I expect the form to be cleared
        .expect(CheckEligibilityPage.phnInput.value).eql('')
});

