import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import PhnLookupPage from '../../pages/eligibility/PhnLookupPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const CONTRACT_NUMBER_INVALID_MESSAGE = 'MSP Contract Number is invalid'
const CONTRACT_NUMBER_REQUIRED_MESSAGE = 'MSP Contract Number is required'
const GROUP_NUMBER_INVALID_MESSAGE = 'Group Number is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const SUCCESS_MESSAGE = 'TRANSACTION SUCCESSFUL'
const WARNING_MESSAGE = 'RPBS0070 MORE THAN 50 PERSONS. PLEASE CONTACT MSP.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/phnLookup'

fixture(`PhnInquiry Page`).disablePageCaching`Test PhnLookup`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser)
  })
  .page(PAGE_TO_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (Group Number and Contract Number)
    // When I click the submit button
    .click(PhnLookupPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(PhnLookupPage.errorText.nth(0).textContent).eql(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(PhnLookupPage.errorText.nth(1).textContent).eql(CONTRACT_NUMBER_REQUIRED_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent).eql(ERROR_MESSAGE)
})

test('Check invalid Group Number and Contract Number format validation', async (t) => {
  await t
    // Given a invalid Group Number (doesn't match checksum) and Contract Number (too short)
    .typeText(PhnLookupPage.groupNumberInput, '6337108')
    .typeText(PhnLookupPage.contractNumberInput, '68034232')
    // When I click the submit button
    .click(PhnLookupPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(PhnLookupPage.errorText.nth(0).textContent).eql(GROUP_NUMBER_INVALID_MESSAGE)
    .expect(PhnLookupPage.errorText.nth(1).textContent).eql(CONTRACT_NUMBER_INVALID_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent).eql(ERROR_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(PhnLookupPage.groupNumberInput, '6337109')
    .typeText(PhnLookupPage.contractNumberInput, '680342326')
    // When I click the submit button
    .click(PhnLookupPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
    // And a table with one result
    .expect(PhnLookupPage.resultsTable.exists).ok()
    .expect(PhnLookupPage.resultsTable.child('thead').exists).ok()
    .expect(PhnLookupPage.resultsTable.child('tbody').child('tr').count).eql(1)
    .expect(PhnLookupPage.resultsRow1.exists).ok() 
    .expect(PhnLookupPage.resultsRow1.child('td').exists).ok()
    // Validate the first row
    .expect(PhnLookupPage.resultsRow1.child('td').nth(0).textContent).eql('9873895927') 
    .expect(PhnLookupPage.resultsRow1.child('td').nth(1).textContent).eql('CHECKSETSNAME, CHECSETFNAME CHESERSNDNAME') 
    .expect(PhnLookupPage.resultsRow1.child('td').nth(2).textContent).eql('19700101') 
    .expect(PhnLookupPage.resultsRow1.child('td').nth(3).textContent).eql('M') 
})

test('Check properly filled form passes validation and returns a warning message', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(PhnLookupPage.groupNumberInput, '6243109')
    .typeText(PhnLookupPage.contractNumberInput, '611035841')
    // When I click the submit button
    .click(PhnLookupPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent).contains(WARNING_MESSAGE)
    // And a table with 50 results
    .expect(PhnLookupPage.resultsTable.exists).ok()
    .expect(PhnLookupPage.resultsTable.child('thead').exists).ok()
    .expect(PhnLookupPage.resultsTable.child('tbody').child('tr').count).eql(50)
    .expect(PhnLookupPage.resultsRow1.exists).ok() 
    // Validate the first row
    .expect(PhnLookupPage.resultsRow1.child('td').exists).ok()
    .expect(PhnLookupPage.resultsRow1.child('td').nth(0).textContent).eql('9332912486') 
    .expect(PhnLookupPage.resultsRow1.child('td').nth(1).textContent).eql('PROTOCTIST ORDERXD, RYAN-CARTERXM BRANDYNXD') 
    .expect(PhnLookupPage.resultsRow1.child('td').nth(2).textContent).eql('19560825') 
    .expect(PhnLookupPage.resultsRow1.child('td').nth(3).textContent).eql('M') 
})

test('Check clear button clears the form', async t => {
  await t
    // Given I have a form filled out with data
    .typeText(PhnLookupPage.groupNumberInput, '6337109')
    .typeText(PhnLookupPage.contractNumberInput, '68034236')
    // When I click the cancel button
    .click(PhnLookupPage.clearButton)
    // I expect the form to be cleared
    .expect(PhnLookupPage.groupNumberInput.value).eql('')
    .expect(PhnLookupPage.contractNumberInput.value).eql('')
})
