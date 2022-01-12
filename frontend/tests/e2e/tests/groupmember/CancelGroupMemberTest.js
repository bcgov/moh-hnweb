import AlertPage from '../../pages/AlertPage';
import { SITE_UNDER_TEST } from '../../configuration';
import CancelGroupMember from '../../pages/groupmember/CancelGroupMember';
import { regularAccUser } from '../../roles/roles';
import UpdateGroupMember from "../../pages/groupmember/UpdateGroupMember";
import CheckEligibilityPage from "../../pages/eligibility/CheckEligibilityPage";

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const COVERAGE_CANCEL_DATE_REQUIRED_MESSAGE = 'Coverage Cancel Date is required'
const INVALID_COVERAGE_CANCEL_DATE_ERROR_MESSAGE = 'Coverage Cancel Date is invalid'
const CANCEL_REASON_REQUIRED_MESSAGE = 'Coverage Cancel Reason is required'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/CancelGroupMember'

fixture(`CancelGroupMember Page`)
    .disablePageCaching `Test CancelGroupMember`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST);

test('Check required fields validation', async t => {
    await t
        // Given required fields aren't filled out (phn, group member, group member number, department)
        // When I click the submit button
        .click(CancelGroupMember.submitButton)
        // I expect an error message stating the page had errors and individual error messages for each required field
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
        .expect(CancelGroupMember.errorText.nth(0).textContent).contains(GROUP_NUMBER_REQUIRED_MESSAGE)
        .expect(CancelGroupMember.errorText.nth(1).textContent).contains(PHN_REQUIRED_MESSAGE)
        .expect(CancelGroupMember.errorText.nth(2).textContent).contains(COVERAGE_CANCEL_DATE_REQUIRED_MESSAGE)
        .expect(CancelGroupMember.errorText.nth(3).textContent).contains(CANCEL_REASON_REQUIRED_MESSAGE)
});

test('Check invalid formats validation', async t => {
    await t
        // Given I enter PHN, Group Number, and Cancel dates with invalid formats
        .typeText(CancelGroupMember.phnInput, '9000448000')
        .typeText(CancelGroupMember.groupNumberInput, '123')
        .typeText(CancelGroupMember.cancelDateInput, '123-123') // TODO (tschia) test how to enter invalid date before PR
        // When I click the submit button
        .click(CancelGroupMember.submitButton)
        // I expect an error message stating the page had errors and an individual error message for the PHN and Group and Cancel Date formats
        .expect(CancelGroupMember.errorText.nth(0).textContent).contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
        .expect(CancelGroupMember.errorText.nth(1).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(CancelGroupMember.errorText.nth(2).textContent).contains(INVALID_COVERAGE_CANCEL_DATE_ERROR_MESSAGE)

        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Check properly filled form passes validation', async t => {
    await t
        // TODO (tschia) add this before PR
});



test('Check clear button clears the form', async t => {
    await t
        // Given I have a form filled out with data
        .typeText(CancelGroupMember.phnInput, '9882807277')
        .typeText(CancelGroupMember.groupNumberInput,'6337109')
        .click(CancelGroupMember.cancelDateInput)
        .pressKey('tab')
        // TODO (tschia) add cancel reason before PR
        // When I click the cancel button
        .click(CancelGroupMember.clearButton)
        // I expect the form to be cleared
        .expect(CancelGroupMember.phnInput.value).eql('')
        .expect(CancelGroupMember.groupNumberInput.value).eql('')
        .expect(CancelGroupMember.cancelDateInput.value).eql('')
});