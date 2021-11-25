import AlertPage from '../pages/AlertPage';
import CheckEligibilityPage from '../pages/eligibility/CheckEligibilityPage';
import { regularAccUser } from '../roles/roles';
import { SITE_UNDER_TEST } from '../configuration';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const SUCCESS_MESSAGE = 'Search complete';
const PHN_ERROR_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid';

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/checkEligibility'

fixture(`CheckEligibility Page`)
.disablePageCaching `Test CheckEligibility`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST);

test('Error when no phn and date', async t => {
    await t
        .click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Error when no date selected', async t => {
    await t 
        .typeText(CheckEligibilityPage.phnInput, '9000448000')
        .click(CheckEligibilityPage.eligibilityDate)
        .pressKey('tab')
        .click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Error when no phn', async t => {
    await t
        .click(CheckEligibilityPage.eligibilityDate)
        .pressKey('tab')       
        .click(CheckEligibilityPage.submitButton)
        .expect(CheckEligibilityPage.ErrorText.textContent).contains(PHN_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Check invalid phn format error message', async t => {
    await t
        .typeText(CheckEligibilityPage.phnInput, '9000448000')
		.click(CheckEligibilityPage.submitButton)
        .expect(CheckEligibilityPage.ErrorText.nth(0).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Check validation passed info', async t => {
    await t
        .typeText(CheckEligibilityPage.phnInput, '9306448169')
        .click(CheckEligibilityPage.eligibilityDate)
        .pressKey('tab')
        .expect(CheckEligibilityPage.eligibilityDate.value).notEql('')
		.click(CheckEligibilityPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
});

test('Check submit button is clickable', async t => {
	await t
        .typeText(CheckEligibilityPage.phnInput, '9000448000')
		.click(CheckEligibilityPage.submitButton)
});

test('Check cancel button is clickable', async t => {
	await t
        .typeText(CheckEligibilityPage.phnInput, '9000448000')
		.click(CheckEligibilityPage.cancelButton)
        .expect(CheckEligibilityPage.phnInput.value).eql('')
});

