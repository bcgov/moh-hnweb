import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ReinstateCancelledCoveragePage from '../../pages/maintenance/ReinstateCancelledCoveragePage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const CANCELLATION_DATE_TO_BE_REMOVED_REQUIRED_MESSAGE = 'Cancel Date to be Removed is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const CRITERIA_NOT_MET = 'RPBS1024 REINSTATEMENT CRITERIA NOT MET.  PLEASE CONTACT MSP'
const SUBSCRIBER_NOT_COVERED = 'RPBS9109 SUBSCRIBER NOT COVERED UNDER THIS GROUP'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/maintenance/reinstateCancelledGroupCoverage'

fixture(`Reinstate Cancelled Group Coverage`).disablePageCaching`Test Reinstate Cancelled Coverage`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(ReinstateCancelledCoveragePage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(2).textContent)
    .contains(CANCELLATION_DATE_TO_BE_REMOVED_REQUIRED_MESSAGE)
})

test('Check invalid phn, groupNumber format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ReinstateCancelledCoveragePage.phnInput, '9000448000')
    .typeText(ReinstateCancelledCoveragePage.groupNumberInput, '0000001')
    // When I click the submit button
    .click(ReinstateCancelledCoveragePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check max length validation', async (t) => {
  await t
    // Given I enter Group Number, Group Member's PHN, Dependent's PHN with more than allowed length
    .typeText(ReinstateCancelledCoveragePage.phnInput, '93319269199')
    .typeText(ReinstateCancelledCoveragePage.groupNumberInput, '63371099')
    // When I click the submit button
    .click(ReinstateCancelledCoveragePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check alphanumeric validation', async (t) => {
  await t
    // Given a PHN entered with a alphanumeric format
    .typeText(ReinstateCancelledCoveragePage.phnInput, 'A331926919')
    .typeText(ReinstateCancelledCoveragePage.groupNumberInput, 'Abc7109')
    // When I click the submit button
    .click(ReinstateCancelledCoveragePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check special char validation', async (t) => {
  await t
    // Given a PHN entered with special char
    .typeText(ReinstateCancelledCoveragePage.phnInput, '%331926919')
    .typeText(ReinstateCancelledCoveragePage.groupNumberInput, '%^&7109')
    // When I click the submit button
    .click(ReinstateCancelledCoveragePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateCancelledCoveragePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results, subscriber not covered', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ReinstateCancelledCoveragePage.groupNumberInput, '6337109')
    .typeText(ReinstateCancelledCoveragePage.phnInput, '9331926919')
    .typeText(ReinstateCancelledCoveragePage.cancelDateInput, '20220731')
    .pressKey('tab')
    .pressKey('tab')
    // When I click the submit button
    .click(ReinstateCancelledCoveragePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUBSCRIBER_NOT_COVERED)
})

test('Check properly filled form passes validation and validate results, criteria not met', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ReinstateCancelledCoveragePage.groupNumberInput, '4841904')
    .typeText(ReinstateCancelledCoveragePage.phnInput, '9873251693')
    .typeText(ReinstateCancelledCoveragePage.cancelDateInput, '20220731')
    .pressKey('tab')
    .pressKey('tab')
    // When I click the submit button
    .click(ReinstateCancelledCoveragePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(CRITERIA_NOT_MET)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ReinstateCancelledCoveragePage.groupNumberInput, '4841904')
    .typeText(ReinstateCancelledCoveragePage.phnInput, '9873251693')
    .typeText(ReinstateCancelledCoveragePage.cancelDateInput, '20220831')
    .pressKey('tab')
    // When I click the Clear button
    .click(ReinstateCancelledCoveragePage.clearButton)
    // I expect the form to be cleared
    .expect(ReinstateCancelledCoveragePage.phnInput.value)
    .eql('')
    .expect(ReinstateCancelledCoveragePage.groupNumberInput.value)
    .eql('')
    .expect(ReinstateCancelledCoveragePage.cancelDateInput.value)
    .eql('')
})
