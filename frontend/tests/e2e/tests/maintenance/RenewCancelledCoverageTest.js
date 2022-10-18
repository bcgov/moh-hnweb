import dayjs from 'dayjs'

import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import RenewCancelledCoveragePage from '../../pages/maintenance/RenewCancelledCoveragePage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const NEW_COVERAGE_EFFECTIVE_DATE_REQUIRED_MESSAGE = 'New Coverage Effective Date is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUBSCRIBER_HAS_FUTURE_COVERAGE = 'RPBS0049 SUBSCRIBER HAS FUTURE COVERAGE.  PLS FORWARD DOCS TO MSP'
const INVALID_COVERAGE_EFFECTIVE_DATE = 'RPBS0276 COVERGE EFFECTIVE DAY MUST BE 01'
const SUBSCRIBER_NOT_COVERED_UNDER_THIS_GROUP = 'RPBS9109 SUBSCRIBER NOT COVERED UNDER THIS GROUP'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/maintenance/renewCancelledGroupCoverage'

fixture(`Renew Cancelled Group Coverage`).disablePageCaching`Test Renew Cancelled Group Coverage`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(RenewCancelledCoveragePage.newEffectiveDateInput)
    .selectText(RenewCancelledCoveragePage.newEffectiveDateInput)
    .pressKey('delete')
    .pressKey('tab')
    .click(RenewCancelledCoveragePage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(RenewCancelledCoveragePage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(RenewCancelledCoveragePage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(RenewCancelledCoveragePage.errorText.nth(2).textContent)
    .contains(NEW_COVERAGE_EFFECTIVE_DATE_REQUIRED_MESSAGE)
})

test('Check invalid phn, groupNumber format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(RenewCancelledCoveragePage.phnInput, '9000448000')
    .typeText(RenewCancelledCoveragePage.groupNumberInput, '0000001')
    // When I click the submit button
    .click(RenewCancelledCoveragePage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(RenewCancelledCoveragePage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(RenewCancelledCoveragePage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results, invalid coverage effective date', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(RenewCancelledCoveragePage.groupNumberInput, '4841904')
    .typeText(RenewCancelledCoveragePage.phnInput, '9873251693')
    .selectText(RenewCancelledCoveragePage.newEffectiveDateInput)
    .pressKey('delete')
    .typeText(RenewCancelledCoveragePage.newEffectiveDateInput, '20220731')
    .pressKey('tab')
    .pressKey('tab')
    // When I click the submit button
    .click(RenewCancelledCoveragePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(INVALID_COVERAGE_EFFECTIVE_DATE)
})

test('Check properly filled form passes validation and validate results, subscriber has future coverage', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(RenewCancelledCoveragePage.groupNumberInput, '6099733')
    .typeText(RenewCancelledCoveragePage.phnInput, '9873102617')
    // When I click the submit button
    .click(RenewCancelledCoveragePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUBSCRIBER_HAS_FUTURE_COVERAGE)
})

test('Check properly filled form passes validation and validate results, subscriber not covered under this coverage', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(RenewCancelledCoveragePage.groupNumberInput, '4841904')
    .typeText(RenewCancelledCoveragePage.phnInput, '9873102617')
    // When I click the submit button
    .click(RenewCancelledCoveragePage.submitButton)
    // I expect a Error message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUBSCRIBER_NOT_COVERED_UNDER_THIS_GROUP)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(RenewCancelledCoveragePage.groupNumberInput, '4841904')
    .typeText(RenewCancelledCoveragePage.phnInput, '9873251693')
    .selectText(RenewCancelledCoveragePage.newEffectiveDateInput)
    .pressKey('delete')
    .typeText(RenewCancelledCoveragePage.newEffectiveDateInput, '20220701')
    .pressKey('tab')
    // When I click the Clear button
    .click(RenewCancelledCoveragePage.clearButton)
    // I expect the form to be cleared
    .expect(RenewCancelledCoveragePage.phnInput.value)
    .eql('')
    .expect(RenewCancelledCoveragePage.groupNumberInput.value)
    .eql('')
    .expect(RenewCancelledCoveragePage.newEffectiveDateInput.value)
    .notEql('')
})
