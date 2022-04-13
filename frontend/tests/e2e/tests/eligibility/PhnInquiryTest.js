import AlertPage from '../../pages/AlertPage'
import PhnInquiryPage from '../../pages/eligibility/PhnInquiryPage'
import { SITE_UNDER_TEST } from '../../configuration'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const PHN_REQUIRED_MESSAGE = 'At least one PHN is required'
const SUCCESS_MESSAGE = 'RPBS9014 TRANSACTION SUCCESSFUL'
const WARNING_MESSAGE = 'RPBS9145 PHN NOT FOUND (9879875914)'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/phnInquiry'

fixture(`PhnInquiry Page`).disablePageCaching`Test PhnInquiry`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (at least one phn)
    // When I click the submit button
    .click(PhnInquiryPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(PHN_REQUIRED_MESSAGE)
})

test('Check invalid phn format validation', async (t) => {
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
    .expect(PhnInquiryPage.errorText.nth(0).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(2).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(3).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(4).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(5).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(6).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(7).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(8).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(PhnInquiryPage.errorText.nth(9).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(PhnInquiryPage.phnInput1, '9873895902')
    .typeText(PhnInquiryPage.phnInput2, '9879431603')
    // When I click the submit button
    .click(PhnInquiryPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // And a table with two results
    .expect(PhnInquiryPage.resultsTable.exists)
    .ok()
    .expect(PhnInquiryPage.resultsTable.child('thead').exists)
    .ok()
    .expect(PhnInquiryPage.resultsTable.child('tbody').child('tr').count)
    .eql(2)
    .expect(PhnInquiryPage.resultsRow1.exists)
    .ok()
    .expect(PhnInquiryPage.resultsRow1.child('td').exists)
    .ok()
    // Validate the first row
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(0).textContent)
    .eql('9873895902')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(1).textContent)
    .eql('GFMSRHNISNAME, GERHNIMODFNAME')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(2).textContent)
    .eql('19700909')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(3).textContent)
    .eql('M')
})

test('Check properly filled form passes validation and validate results (which contain a warning)', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(PhnInquiryPage.phnInput1, '9879875914')
    .typeText(PhnInquiryPage.phnInput2, '9879431603')
    // When I click the submit button
    .click(PhnInquiryPage.submitButton)
    // I expect a warning message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(WARNING_MESSAGE)
    // And a table with one results
    .expect(PhnInquiryPage.resultsTable.exists)
    .ok()
    .expect(PhnInquiryPage.resultsTable.child('thead').exists)
    .ok()
    .expect(PhnInquiryPage.resultsTable.child('tbody').child('tr').count)
    .eql(1)
    .expect(PhnInquiryPage.resultsRow1.exists)
    .ok()
    .expect(PhnInquiryPage.resultsRow1.child('td').exists)
    .ok()
    // Validate the first row
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(0).textContent)
    .eql('9879431603')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(1).textContent)
    .eql('SMITH, JENNIFER       JENNY')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(2).textContent)
    .eql('20000101')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(3).textContent)
    .eql('F')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(4).textContent)
    .eql('Y')
    .expect(PhnInquiryPage.resultsRow1.child('td').nth(5).textContent)
    .eql('N')
})

test('Check clear button clears the form', async (t) => {
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
    // When I click the Clear button
    .click(PhnInquiryPage.clearButton)
    // I expect the form to be cleared
    .expect(PhnInquiryPage.phnInput1.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput2.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput3.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput4.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput5.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput6.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput7.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput8.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput9.value)
    .eql('')
    .expect(PhnInquiryPage.phnInput10.value)
    .eql('')
})
