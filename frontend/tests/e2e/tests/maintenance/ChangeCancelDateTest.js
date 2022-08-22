import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ChangeCancelDatePage from '../../pages/maintenance/ChangeCancelDatePage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const EXISTING_CANCELLATION_DATE_REQUIRED_MESSAGE = 'Existing Coverage Cancellation Date is required'
const NEW_CANCELLATION_DATE_REQUIRED_MESSAGE = 'New Coverage Cancellation Date is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const NEW_DATE_CANNOT_EQUAL_OLD_DATE = 'RPBS0137 NEW CANCELLATION CANNOT EQUAL OLD CANCELLATION DATE'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/maintenance/changeCancelDate'

fixture(`Contract Inquiry Page`).disablePageCaching`Test Change Cancel Date`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(ChangeCancelDatePage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ChangeCancelDatePage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(ChangeCancelDatePage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(ChangeCancelDatePage.errorText.nth(2).textContent)
    .contains(EXISTING_CANCELLATION_DATE_REQUIRED_MESSAGE)
    .expect(ChangeCancelDatePage.errorText.nth(3).textContent)
    .contains(NEW_CANCELLATION_DATE_REQUIRED_MESSAGE)
})

test('Check invalid phn, groupNumber format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ChangeCancelDatePage.phnInput, '9000448000')
    .typeText(ChangeCancelDatePage.groupNumberInput, '0000001')
    // When I click the submit button
    .click(ChangeCancelDatePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ChangeCancelDatePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ChangeCancelDatePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ChangeCancelDatePage.groupNumberInput, '6337109')
    .typeText(ChangeCancelDatePage.phnInput, '9331926919')
    .typeText(ChangeCancelDatePage.existingCancelDateInput, '20191103')
    .pressKey('tab')
    .pressKey('tab')
    .typeText(ChangeCancelDatePage.newCancelDateInput, '20191103')
    .pressKey('tab')
    .pressKey('tab')
    // When I click the submit button
    .click(ChangeCancelDatePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(NEW_DATE_CANNOT_EQUAL_OLD_DATE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ChangeCancelDatePage.phnInput, '9000448000')
    .typeText(ChangeCancelDatePage.groupNumberInput, '6337109')
    .typeText(ChangeCancelDatePage.existingCancelDateInput, '20191103')
    .pressKey('tab')
    .typeText(ChangeCancelDatePage.newCancelDateInput, '20191103')
    .pressKey('tab')
    // When I click the Clear button
    .click(ChangeCancelDatePage.clearButton)
    // I expect the form to be cleared
    .expect(ChangeCancelDatePage.phnInput.value)
    .eql('')
    .expect(ChangeCancelDatePage.groupNumberInput.value)
    .eql('')
    .expect(ChangeCancelDatePage.existingCancelDateInput.value)
    .eql('')
    .expect(ChangeCancelDatePage.newCancelDateInput.value)
    .eql('')
})
