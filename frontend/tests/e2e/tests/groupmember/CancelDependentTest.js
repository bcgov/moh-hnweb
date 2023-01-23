import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import CancelDependent from '../../pages/groupmember/CancelDependent'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const COVERAGE_CANCEL_DATE_REQUIRED_MESSAGE = 'Coverage Cancel Date is required'
const DEPENDENT_PHN_REQUIRED_MESSAGE = 'PHN is required'
const CANCEL_REASON_REQUIRED_MESSAGE = 'Cancel Reason is required'
const RAPID_RESPONSE = 'RPBS0097 SUBSCRIBER AND DEPENDENT PHN MUST BE DIFFERENT.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/CancelDependent'

fixture(`CancelDependent Page`).disablePageCaching`Test CancelDependent`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (phn, dependent phn, group member, group member number, department)
    // When I click the submit button
    .click(CancelDependent.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(CancelDependent.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(CancelDependent.errorText.nth(2).textContent)
    .contains(DEPENDENT_PHN_REQUIRED_MESSAGE)
    .expect(CancelDependent.errorText.nth(3).textContent)
    .contains(COVERAGE_CANCEL_DATE_REQUIRED_MESSAGE)
    .expect(CancelDependent.errorText.nth(4).textContent)
    .contains(CANCEL_REASON_REQUIRED_MESSAGE)
})

test('Check invalid formats validation', async (t) => {
  await t
    // Given I enter PHN, Dependent PHN, Group Number, and Cancel dates with invalid formats
    .typeText(CancelDependent.phnInput, '9000448000')
    .typeText(CancelDependent.dependentPhnInput, '9000448000')
    .typeText(CancelDependent.groupNumberInput, '123')
    // When I click the submit button
    .click(CancelDependent.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN and Group and Cancel Date formats
    .expect(CancelDependent.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(2).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check max length validation', async (t) => {
  await t
    // Given I enter Group Number, Group Member's PHN, Dependent's PHN with more than allowed length
    .typeText(CancelDependent.phnInput, '93971055755')
    .typeText(CancelDependent.dependentPhnInput, '93971055755')
    .typeText(CancelDependent.groupNumberInput, '123333333')
    // When I click the submit button
    .click(CancelDependent.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN and Group and Cancel Date formats
    .expect(CancelDependent.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(2).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check alphanumeric validation', async (t) => {
  await t
    // Given I enter PHN, Dependent PHN, Group Number, and Cancel dates with alphanumeric
    .typeText(CancelDependent.phnInput, '939710557A')
    .typeText(CancelDependent.dependentPhnInput, '9A97105575')
    .typeText(CancelDependent.groupNumberInput, '123AAA')
    // When I click the submit button
    .click(CancelDependent.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN and Group and Cancel Date formats
    .expect(CancelDependent.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(2).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check special character validation', async (t) => {
  await t
    // Given I enter PHN, Dependent PHN, Group Number, and Cancel dates with special chars
    .typeText(CancelDependent.phnInput, '93971055$%')
    .typeText(CancelDependent.dependentPhnInput, '93971055#$')
    .typeText(CancelDependent.groupNumberInput, '123@#')
    // When I click the submit button
    .click(CancelDependent.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN and Group and Cancel Date formats
    .expect(CancelDependent.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(CancelDependent.errorText.nth(2).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(CancelDependent.groupNumberInput, '6243109')
    .typeText(CancelDependent.phnInput, '9397105575')
    .typeText(CancelDependent.dependentPhnInput, '9397105575')
    .click(CancelDependent.cancelDateInput)
    .click(CancelDependent.divSelectedDate)
    .click(CancelDependent.cancelReasonInput)
    .pressKey('down')
    .pressKey('enter')
    // When I click the submit button
    .click(CancelDependent.submitButton)
    // I expect a response from RAPID
    .expect(AlertPage.alertBannerText.textContent)
    .contains(RAPID_RESPONSE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(CancelDependent.phnInput, '9882807277')
    .typeText(CancelDependent.dependentPhnInput, '9882807277')
    .typeText(CancelDependent.groupNumberInput, '6337109')
    .click(CancelDependent.relationshipInput)
    .pressKey('down')
    .pressKey('enter')
    .click(CancelDependent.cancelReasonInput)
    .pressKey('down')
    .pressKey('enter')
    .click(CancelDependent.cancelDateInput)
    .click(CancelDependent.divSelectedDate)
    // When I click the clear button
    .click(CancelDependent.clearButton)
    // I expect the form to be cleared
    .expect(CancelDependent.phnInput.value)
    .eql('')
    .expect(CancelDependent.dependentPhnInput.value)
    .eql('')
    .expect(CancelDependent.groupNumberInput.value)
    .eql('')
    .expect(CancelDependent.cancelReasonInput.value)
    .eql('')
    .expect(CancelDependent.cancelDateInput.value)
    .eql('')
})
