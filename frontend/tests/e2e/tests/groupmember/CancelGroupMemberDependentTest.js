import AlertPage from '../../pages/AlertPage'
import CancelGroupMemberDependent from '../../pages/groupmember/CancelGroupMemberDependent'
import { SITE_UNDER_TEST } from '../../configuration'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const COVERAGE_CANCEL_DATE_REQUIRED_MESSAGE = 'Coverage Cancel Date is required'
const DEPENDENT_PHN_REQUIRED_MESSAGE = 'PHN is required'
const CANCEL_REASON_REQUIRED_MESSAGE = 'Cancel Reason is required'
const RAPID_RESPONSE = 'RPBS0003 SUBSCRIBER PHN MUST BE ENTERED'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/CancelGroupMemberDependent'

fixture(`CancelGroupMemberDependent Page`).disablePageCaching`Test CancelGroupMemberDependent`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser)
  })
  .page(PAGE_TO_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (phn, dependent phn, group member, group member number, department)
    // When I click the submit button
    .click(CancelGroupMemberDependent.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(CancelGroupMemberDependent.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(CancelGroupMemberDependent.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(CancelGroupMemberDependent.errorText.nth(2).textContent)
    .contains(DEPENDENT_PHN_REQUIRED_MESSAGE)
    .expect(CancelGroupMemberDependent.errorText.nth(3).textContent)
    .contains(COVERAGE_CANCEL_DATE_REQUIRED_MESSAGE)
    .expect(CancelGroupMemberDependent.errorText.nth(4).textContent)
    .contains(CANCEL_REASON_REQUIRED_MESSAGE)
})

test('Check invalid formats validation', async (t) => {
  await t
    // Given I enter PHN, Dependent PHN, Group Number, and Cancel dates with invalid formats
    .typeText(CancelGroupMemberDependent.phnInput, '9000448000')
    .typeText(CancelGroupMemberDependent.dependentPhnInput, '9000448000')
    .typeText(CancelGroupMemberDependent.groupNumberInput, '123')
    // When I click the submit button
    .click(CancelGroupMemberDependent.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN and Group and Cancel Date formats
    .expect(CancelGroupMemberDependent.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(CancelGroupMemberDependent.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(CancelGroupMemberDependent.errorText.nth(2).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(CancelGroupMemberDependent.groupNumberInput, '6243109')
    .typeText(CancelGroupMemberDependent.phnInput, '9397105575')
    .typeText(CancelGroupMemberDependent.dependentPhnInput, '9397105575')
    .click(CancelGroupMemberDependent.cancelDateInput)
    .click(CancelGroupMemberDependent.divSelectedDate)
    .click(CancelGroupMemberDependent.cancelReasonInput)
    .pressKey('down')
    .pressKey('enter')
    // When I click the submit button
    .click(CancelGroupMemberDependent.submitButton)
    .wait(10000)
    // I expect a response from RAPID
    .expect(AlertPage.alertBannerText.textContent)
    .contains(RAPID_RESPONSE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(CancelGroupMemberDependent.phnInput, '9882807277')
    .typeText(CancelGroupMemberDependent.dependentPhnInput, '9882807277')
    .typeText(CancelGroupMemberDependent.groupNumberInput, '6337109')
    .click(CancelGroupMemberDependent.relationshipInput)
    .pressKey('down')
    .pressKey('enter')
    .click(CancelGroupMemberDependent.cancelReasonInput)
    .pressKey('down')
    .pressKey('enter')
    .click(CancelGroupMemberDependent.cancelDateInput)
    .click(CancelGroupMemberDependent.divSelectedDate)
    // When I click the clear button
    .click(CancelGroupMemberDependent.clearButton)
    // I expect the form to be cleared
    .expect(CancelGroupMemberDependent.phnInput.value)
    .eql('')
    .expect(CancelGroupMemberDependent.dependentPhnInput.value)
    .eql('')
    .expect(CancelGroupMemberDependent.groupNumberInput.value)
    .eql('')
    .expect(CancelGroupMemberDependent.cancelReasonInput.value)
    .eql('')
    .expect(CancelGroupMemberDependent.cancelDateInput.value)
    .eql('')
})