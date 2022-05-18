import { VALIDATE_MINIMUM_DATE_MESSAGE } from '../../../../src/util/validators'
import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import CheckEligibilityPage from '../../pages/eligibility/CheckEligibilityPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const SUCCESS_MESSAGE = 'HJMB001I SUCCESSFULLY COMPLETED'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const DATE_TO_CHECK_REQUIRED_MESSAGE = 'Date to Check is required'

const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/checkEligibility'

fixture(`CheckEligibility Page`).disablePageCaching`Test CheckEligibility`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (phn, date to check)
    .selectText(CheckEligibilityPage.eligibilityDate)
    .pressKey('delete')
    .pressKey('tab')
    // When I click the submit button
    .click(CheckEligibilityPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(CheckEligibilityPage.errorText.nth(0).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(CheckEligibilityPage.errorText.nth(1).textContent)
    .contains(DATE_TO_CHECK_REQUIRED_MESSAGE)
})

test('Check invalid phn format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(CheckEligibilityPage.phnInput, '9000448000')
    // When I click the submit button
    .click(CheckEligibilityPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(CheckEligibilityPage.errorText.nth(0).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check minimum date validation', async (t) => {
  await t
    // Given date is not later than 19000101
    .typeText(CheckEligibilityPage.phnInput, '9395568139')
    .selectText(CheckEligibilityPage.eligibilityDate)
    .pressKey('delete')
    .pressKey('tab')
    .typeText(CheckEligibilityPage.eligibilityDate, '18991231')
    .pressKey('tab')
    // When I click the submit button
    .click(CheckEligibilityPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for minimum date
    .expect(CheckEligibilityPage.errorText.nth(0).textContent)
    .contains(VALIDATE_MINIMUM_DATE_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation for an ineligible PHN', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(CheckEligibilityPage.phnInput, '9395568139')
    .click(CheckEligibilityPage.eligibilityDate)
    .pressKey('tab')
    // When I click the submit button
    .click(CheckEligibilityPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // And the results to be populated
    .expect(CheckEligibilityPage.result.exists)
    .ok()
    .expect(CheckEligibilityPage.resultRow1.exists)
    .ok()
    .expect(CheckEligibilityPage.resultRow1.child('div').nth(0).child('span').textContent)
    .eql('9395568139')
    .expect(CheckEligibilityPage.resultRow1.child('div').nth(1).child('span').textContent)
    .eql('NO')

    .expect(CheckEligibilityPage.resultRow2.exists)
    .ok()
    .expect(CheckEligibilityPage.resultRow2.child('div').nth(0).child('span').textContent)
    .eql('20190731')
    .expect(CheckEligibilityPage.resultRow2.child('div').nth(1).child('span').textContent)
    .eql('OUT OF PROVINCE MOVE')
    .expect(CheckEligibilityPage.resultRow2.child('div').nth(2).child('span').textContent)
    .eql('')

    .expect(CheckEligibilityPage.clientInstructions.textContent)
    .eql("MSP'S RECORDS INDICATE THAT THIS PERSON HAS MOVED PERMANENTLY FROM BC. PLEASE CONFIRM RESIDENCE, OBTAIN AND UPDATE ADDRESS AND TELEPHONE INFORMATION AND ADVISE PERSON TO CONTACT MSP TO RE-ESTABLISH ELIGIBILITY.")
})

test('Check properly filled form passes validation for an eligible PHN', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(CheckEligibilityPage.phnInput, '9347984074')
    .click(CheckEligibilityPage.eligibilityDate)
    .pressKey('tab')
    // When I click the submit button
    .click(CheckEligibilityPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // And the results to be populated
    .expect(CheckEligibilityPage.result.exists)
    .ok()
    .expect(CheckEligibilityPage.resultRow1.exists)
    .ok()
    .expect(CheckEligibilityPage.resultRow1.child('div').nth(0).child('span').textContent)
    .eql('9347984074')
    .expect(CheckEligibilityPage.resultRow1.child('div').nth(1).child('span').textContent)
    .eql('YES')

    .expect(CheckEligibilityPage.resultRow2.exists)
    .ok()
    .expect(CheckEligibilityPage.resultRow2.child('div').nth(0).child('span').textContent)
    .eql('')
    .expect(CheckEligibilityPage.resultRow2.child('div').nth(1).child('span').textContent)
    .eql('')
    .expect(CheckEligibilityPage.resultRow2.child('div').nth(2).child('span').textContent)
    .eql('')
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(CheckEligibilityPage.phnInput, '9000448000')
    // When I click the Clear button
    .click(CheckEligibilityPage.clearButton)
    // I expect the form to be cleared
    .expect(CheckEligibilityPage.phnInput.value)
    .eql('')
})
