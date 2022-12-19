import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ExtendCancelDatePage from '../../pages/maintenance/ExtendCancelDatePage'
import { regularAccUser } from '../../roles/roles'

const immigrationCodeOption = ExtendCancelDatePage.immigrationCodeSelect.find('option')
const ERROR_MESSAGE = 'Please correct errors before submitting'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const EXISTING_CANCELLATION_DATE_REQUIRED_MESSAGE = 'Existing Cancellation Date is required'
const NEW_CANCELLATION_DATE_REQUIRED_MESSAGE = 'New Cancellation Date is required'
const IMMIGRATION_CODE_REQUIRED_MESSAGE = 'Immigration Code is required'
const PERMIT_ISSUE_DATE_REQUIRED_MESSAGE = 'Permit Issue Date is required'
const PERMIT_EXPIRY_DATE_REQUIRED_MESSAGE = 'Permit Expiry Date is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const EXISTING_CANCEL_DATE_NOT_RECENT = 'RPBS0053 EXISTING CANCEL DATE NOT FOUND AS THE MOST RECENT DATEFOR PHN AND GROUP'
const VISA_EXPIRY_DATE_MUST_BE_AFTER_ISSUE_DATE = 'RPBS0210 VISA EXPIRY DATE MUST BE AFTER VISA ISSUE DATE'
const CANCEL_DATE_TOO_FAR_IN_THE_PAST = 'RPBS0159 NEW COVERAGE CANCEL DATE TOO FAR IN THE PAST'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/maintenance/extendCancelDate'

fixture(`Contract Inquiry Page`).disablePageCaching`Test Change Cancel Date`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(2).textContent)
    .contains(PERMIT_ISSUE_DATE_REQUIRED_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(3).textContent)
    .contains(PERMIT_EXPIRY_DATE_REQUIRED_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(4).textContent)
    .contains(IMMIGRATION_CODE_REQUIRED_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(5).textContent)
    .contains(EXISTING_CANCELLATION_DATE_REQUIRED_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(6).textContent)
    .contains(NEW_CANCELLATION_DATE_REQUIRED_MESSAGE)
})

test('Check invalid phn, groupNumber format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ExtendCancelDatePage.phnInput, '9000448000')
    .typeText(ExtendCancelDatePage.groupNumberInput, '0000001')
    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check max length validation', async (t) => {
  await t
    // Given I enter Group Number, Group Member's PHN, Dependent's PHN with more than allowed length
    .typeText(ExtendCancelDatePage.phnInput, '98732516933')
    .typeText(ExtendCancelDatePage.groupNumberInput, '48419044')
    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check alphanumeric validation', async (t) => {
  await t
    // Given a PHN entered with an alphanueric format
    .typeText(ExtendCancelDatePage.phnInput, '98732516AA')
    .typeText(ExtendCancelDatePage.groupNumberInput, '48abc04')
    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check special char validation', async (t) => {
  await t
    // Given a PHN entered with an special char
    .typeText(ExtendCancelDatePage.phnInput, '98732516#$')
    .typeText(ExtendCancelDatePage.groupNumberInput, '48^&*04')
    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ExtendCancelDatePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results, dates cannot be equal', async (t) => {
  await t
    .typeText(ExtendCancelDatePage.groupNumberInput, '4841904')
    .typeText(ExtendCancelDatePage.phnInput, '9873251693')
    .click(ExtendCancelDatePage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Student Authorization'))
    .typeText(ExtendCancelDatePage.permitIssueDate, '20220701')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.permitExpiryDate, '20230731')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.existingCancelDateInput, '20220731')
    .pressKey('tab')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.newCancelDateInput, '20230731')
    .pressKey('tab')
    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    .wait(5000)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(EXISTING_CANCEL_DATE_NOT_RECENT)
})

test('Check properly filled form passes validation and validate results, invalid visa expiry date', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ExtendCancelDatePage.groupNumberInput, '4841904')
    .typeText(ExtendCancelDatePage.phnInput, '9873251693')
    .click(ExtendCancelDatePage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Student Authorization'))
    .typeText(ExtendCancelDatePage.permitIssueDate, '20230701')
    .pressKey('tab')

    .typeText(ExtendCancelDatePage.permitExpiryDate, '20220731')
    .pressKey('tab')

    .typeText(ExtendCancelDatePage.existingCancelDateInput, '20220630')
    .pressKey('tab')

    .typeText(ExtendCancelDatePage.newCancelDateInput, '20220731')
    .pressKey('tab')

    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(VISA_EXPIRY_DATE_MUST_BE_AFTER_ISSUE_DATE)
})

test('Check properly filled form passes validation and validate results, cancel date too far in the past', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ExtendCancelDatePage.groupNumberInput, '6337109')
    .typeText(ExtendCancelDatePage.phnInput, '9332912486')
    .click(ExtendCancelDatePage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Student Authorization'))
    .typeText(ExtendCancelDatePage.permitIssueDate, '20220701')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.permitExpiryDate, '20230731')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.existingCancelDateInput, '20220630')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.newCancelDateInput, '20220731')
    .pressKey('tab')
    .wait(1000)
    // When I click the submit button
    .click(ExtendCancelDatePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(CANCEL_DATE_TOO_FAR_IN_THE_PAST)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ExtendCancelDatePage.groupNumberInput, '4841904')
    .typeText(ExtendCancelDatePage.phnInput, '9873251693')
    .typeText(ExtendCancelDatePage.existingCancelDateInput, '20220731')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.newCancelDateInput, '20220831')
    .pressKey('tab')
    .click(ExtendCancelDatePage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Student Authorization'))
    .typeText(ExtendCancelDatePage.permitIssueDate, '20220701')
    .pressKey('tab')
    .typeText(ExtendCancelDatePage.permitExpiryDate, '20230731')
    .pressKey('tab')
    // When I click the Clear button
    .click(ExtendCancelDatePage.clearButton)
    // I expect the form to be cleared
    .expect(ExtendCancelDatePage.phnInput.value)
    .eql('')
    .expect(ExtendCancelDatePage.groupNumberInput.value)
    .eql('')
    .expect(ExtendCancelDatePage.existingCancelDateInput.value)
    .eql('')
    .expect(ExtendCancelDatePage.newCancelDateInput.value)
    .eql('')
    .expect(ExtendCancelDatePage.immigrationCodeSelect.value)
    .eql('')
    .expect(ExtendCancelDatePage.permitIssueDate.value)
    .eql('')
    .expect(ExtendCancelDatePage.permitExpiryDate.value)
    .eql('')
})
