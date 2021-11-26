import AlertPage from '../pages/AlertPage';
import PhnInquiryPage from '../pages/eligibility/PhnInquiryPage';
import { regularAccUser } from '../roles/roles';
import { SITE_UNDER_TEST } from '../configuration';
import CheckEligibilityPage from "../pages/eligibility/CheckEligibilityPage";

const ERROR_MESSAGE = 'Please correct errors before submitting';
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid';
const PHN_REQUIRED_MESSAGE = 'At least one PHN is required'
const SUCCESS_MESSAGE = 'Search complete';

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/phnInquiry'

fixture(`PhnInquiry Page`)
    .disablePageCaching `Test PhnInquiry`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST);

test('Check required fields validation', async t => {
    await t
        // Given required fields aren't filled out (at least one phn)
        // When I click the submit button
        .click(PhnInquiryPage.submitButton)
        // I expect an error message stating the page had errors and individual error messages for each required field
        .expect(AlertPage.alertBannerText.textContent).contains(PHN_REQUIRED_MESSAGE)
});

test('Check invalid phn format validation', async t => {
    await t
        // Given a PHN field with an an invalid format
        .typeText(PhnInquiryPage.phnInput1, '9000448000')
        .typeText(PhnInquiryPage.phnInput2, '9000448000')
        .typeText(PhnInquiryPage.phnInput3, '9000448000')
        .typeText(PhnInquiryPage.phnInput4, '9000448000')
        .typeText(PhnInquiryPage.phnInput5, '9000448000')
        .typeText(PhnInquiryPage.phnInput6, '9000448000')
        .typeText(PhnInquiryPage.phnInput7, '9000448000')
        .typeText(PhnInquiryPage.phnInput8, '9000448000')
        .typeText(PhnInquiryPage.phnInput9, '9000448000')
        .typeText(PhnInquiryPage.phnInput10, '9000448000')
        // When I click the submit button
        .click(PhnInquiryPage.submitButton)
        // I expect an error message stating the page had errors and an individual error message for the PHN format
        .expect(PhnInquiryPage.ErrorText.nth(0).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(1).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(2).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(3).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(4).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(5).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(6).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(7).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(8).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(PhnInquiryPage.ErrorText.nth(9).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test.skip('Check properly filled form passes validation', async t => {
    await t
        // Given the page is filled out correctly
        .typeText(PhnInquiryPage.phnInput1, '9879875914')
        .typeText(PhnInquiryPage.phnInput2, '9879875914')
        .typeText(PhnInquiryPage.phnInput3, '9879875914')
        .typeText(PhnInquiryPage.phnInput4, '9879875914')
        .typeText(PhnInquiryPage.phnInput5, '9879875914')
        .typeText(PhnInquiryPage.phnInput6, '9879875914')
        .typeText(PhnInquiryPage.phnInput7, '9879875914')
        .typeText(PhnInquiryPage.phnInput8, '9879875914')
        .typeText(PhnInquiryPage.phnInput9, '9879875914')
        .typeText(PhnInquiryPage.phnInput10, '9879875914')
        // When I click the submit button
        .click(PhnInquiryPage.submitButton)
        // I expect a success message
        .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
});

test('Check cancel button clears the form', async t => {
    await t
        // Given I have a form filled out with data
        .typeText(PhnInquiryPage.phnInput1, '9000448000')
        .typeText(PhnInquiryPage.phnInput2, '9000448000')
        .typeText(PhnInquiryPage.phnInput3, '9000448000')
        .typeText(PhnInquiryPage.phnInput4, '9000448000')
        .typeText(PhnInquiryPage.phnInput5, '9000448000')
        .typeText(PhnInquiryPage.phnInput6, '9000448000')
        .typeText(PhnInquiryPage.phnInput7, '9000448000')
        .typeText(PhnInquiryPage.phnInput8, '9000448000')
        .typeText(PhnInquiryPage.phnInput9, '9000448000')
        .typeText(PhnInquiryPage.phnInput10, '9000448000')
        // When I click the cancel button
        .click(PhnInquiryPage.cancelButton)
        // I expect the form to be cleared
        .expect(PhnInquiryPage.phnInput1.value).eql('')
        .expect(PhnInquiryPage.phnInput2.value).eql('')
        .expect(PhnInquiryPage.phnInput3.value).eql('')
        .expect(PhnInquiryPage.phnInput4.value).eql('')
        .expect(PhnInquiryPage.phnInput5.value).eql('')
        .expect(PhnInquiryPage.phnInput6.value).eql('')
        .expect(PhnInquiryPage.phnInput7.value).eql('')
        .expect(PhnInquiryPage.phnInput8.value).eql('')
        .expect(PhnInquiryPage.phnInput9.value).eql('')
        .expect(PhnInquiryPage.phnInput10.value).eql('')
});
