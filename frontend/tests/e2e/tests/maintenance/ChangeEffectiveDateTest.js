import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ChangeEffectiveDatePage from '../../pages/maintenance/ChangeEffectiveDatePage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const EXISTING_EFFDATE_REQUIRED_MESSAGE = 'Existing Coverage Effective Date is required'
const NEW_EFFDATE_REQUIRED_MESSAGE = 'New Coverage Effective Date is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const NEW_DATE_CANNOT_EQUAL_OLD_DATE = 'RPBS0137 NEW EFFECTIVE CANNOT EQUAL OLD EFFECTIVE DATE'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/maintenance/changeEffectiveDate'

fixture(`Contract Inquiry Page`).disablePageCaching`Test Change Effective Date`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(ChangeEffectiveDatePage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ChangeEffectiveDatePage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(ChangeEffectiveDatePage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(ChangeEffectiveDatePage.errorText.nth(2).textContent)
    .contains(EXISTING_EFFDATE_REQUIRED_MESSAGE)
    .expect(ChangeEffectiveDatePage.errorText.nth(3).textContent)
    .contains(NEW_EFFDATE_REQUIRED_MESSAGE)
})

test('Check invalid phn, groupNumber format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ChangeEffectiveDatePage.phnInput, '9000448000')
    .typeText(ChangeEffectiveDatePage.groupNumberInput, '0000001')
    // When I click the submit button
    .click(ChangeEffectiveDatePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ChangeEffectiveDatePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ChangeEffectiveDatePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ChangeEffectiveDatePage.groupNumberInput, '6337109')
    .typeText(ChangeEffectiveDatePage.phnInput, '9331926919')
    .typeText(ChangeEffectiveDatePage.existingEffectiveDateInput, '20191103')
    .pressKey('tab')
    .pressKey('tab')
    .typeText(ChangeEffectiveDatePage.newEffectiveDateInput, '20191103')
    .pressKey('tab')
    .pressKey('tab')
    // When I click the submit button
    .click(ChangeEffectiveDatePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(NEW_DATE_CANNOT_EQUAL_OLD_DATE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ChangeEffectiveDatePage.phnInput, '9000448000')
    .typeText(ChangeEffectiveDatePage.groupNumberInput, '6337109')
    .typeText(ChangeEffectiveDatePage.existingEffectiveDateInput, '20191103')
    .pressKey('tab')
    .typeText(ChangeEffectiveDatePage.newEffectiveDateInput, '20191103')
    .pressKey('tab')
    // When I click the Clear button
    .click(ChangeEffectiveDatePage.clearButton)
    // I expect the form to be cleared
    .expect(ChangeEffectiveDatePage.phnInput.value)
    .eql('')
    .expect(ChangeEffectiveDatePage.groupNumberInput.value)
    .eql('')
    .expect(ChangeEffectiveDatePage.existingEffectiveDateInput.value)
    .eql('')
    .expect(ChangeEffectiveDatePage.newEffectiveDateInput.value)
    .eql('')
})
