import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import GetContractPeriodsPage from '../../pages/mspcontracts/GetContractPeriodsPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUCCESS_MESSAGE = 'TRANSACTION SUCCESSFUL'
const WARNING_MESSAGE = 'RPBS0086 MORE THAN 20 PERSONS FOUND - NOT ALL DISPLAYED'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/mspContracts/getContractPeriods'

fixture(`GetContractPeriods Page`).disablePageCaching`Test GetContractPeriods`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(GetContractPeriodsPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(GetContractPeriodsPage.errorText.nth(0).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
})

test('Check invalid phn format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(GetContractPeriodsPage.phnInput, '9000448000')
    // When I click the submit button
    .click(GetContractPeriodsPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(GetContractPeriodsPage.errorText.nth(0).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(GetContractPeriodsPage.phnInput, '9873672248')
    // When I click the submit button
    .click(GetContractPeriodsPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // And a table with four results
    .expect(GetContractPeriodsPage.resultsTable.exists)
    .ok()
    .expect(GetContractPeriodsPage.resultsTable.child('thead').exists)
    .ok()
    .expect(GetContractPeriodsPage.resultsTable.child('tbody').child('tr').count)
    .eql(4)
    .expect(GetContractPeriodsPage.resultsRow1.exists)
    .ok()
    .expect(GetContractPeriodsPage.resultsRow1.child('td').exists)
    .ok()
    // Validate the first row
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(0).textContent)
    .eql('9873672248')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(1).textContent)
    .eql('SPBIGDATASNAME, SPBIGDATAFNAME')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(2).textContent)
    .eql('9873672255')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(3).textContent)
    .eql('S')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(4).textContent)
    .eql('0000001')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(5).textContent)
    .eql('20220201')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(6).textContent)
    .eql('')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(7).textContent)
    .eql('')
    // Validate the last row
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(0).textContent)
    .eql('9873672255')
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(1).textContent)
    .eql('BIGDATASNAME, BIGDATAFNAME')
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(2).textContent)
    .eql('9873672248')
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(3).textContent)
    .eql('S')
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(4).textContent)
    .eql('4044574')
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(5).textContent)
    .eql('20220201')
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(6).textContent)
    .eql('20220228')
    .expect(GetContractPeriodsPage.resultsRow4.child('td').nth(7).textContent)
    .eql('Eligible')
})

test('Check properly filled form passes validation and validate results when more than 20', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(GetContractPeriodsPage.phnInput, '9873672255')
    // When I click the submit button
    .click(GetContractPeriodsPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(WARNING_MESSAGE)
    // And a table with four results
    .expect(GetContractPeriodsPage.resultsTable.exists)
    .ok()
    .expect(GetContractPeriodsPage.resultsTable.child('thead').exists)
    .ok()
    .expect(GetContractPeriodsPage.resultsTable.child('tbody').child('tr').count)
    .eql(22)
    .expect(GetContractPeriodsPage.resultsRow1.exists)
    .ok()
    .expect(GetContractPeriodsPage.resultsRow1.child('td').exists)
    .ok()
    // Validate the first row to ensure results display despite warning
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(0).textContent)
    .eql('9873672255')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(1).textContent)
    .eql('BIGDATASNAME, BIGDATAFNAME')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(2).textContent)
    .eql('9873672255')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(3).textContent)
    .eql('C')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(4).textContent)
    .eql('0000001')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(5).textContent)
    .eql('20220201')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(6).textContent)
    .eql('')
    .expect(GetContractPeriodsPage.resultsRow1.child('td').nth(7).textContent)
    .eql('')
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(GetContractPeriodsPage.phnInput, '9000448000')
    // When I click the Clear button
    .click(GetContractPeriodsPage.clearButton)
    // I expect the form to be cleared
    .expect(GetContractPeriodsPage.phnInput.value)
    .eql('')
})
