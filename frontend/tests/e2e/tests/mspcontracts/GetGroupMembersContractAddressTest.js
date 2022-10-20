import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import GetGroupMembersContractAddressPage from '../../pages/mspcontracts/GetGroupMembersContractAddressPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUCCESS_MESSAGE = 'RPBS9014 TRANSACTION COMPLETED'
const WARNING_MESSAGE = 'RPBS0059 MORE THAN 20 PERSONS. PLEASE CONTACT MSP'
const NO_COVEREAGE_ERROR_MESSAGE = 'RPBS0067 NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/mspContracts/getGroupMembersContractAddress'

fixture(`Get Group Member's Contract Address Page`).disablePageCaching`Test Get Group Member's Contract Address`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(GetGroupMembersContractAddressPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(GetGroupMembersContractAddressPage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(GetGroupMembersContractAddressPage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
})

test('Check invalid phn, groupNumber format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(GetGroupMembersContractAddressPage.phnInput, '9000448000')
    .typeText(GetGroupMembersContractAddressPage.groupNumberInput, '0000001')
    // When I click the submit button
    .click(GetGroupMembersContractAddressPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(GetGroupMembersContractAddressPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(GetGroupMembersContractAddressPage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate results', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(GetGroupMembersContractAddressPage.groupNumberInput, '6099733')
    .typeText(GetGroupMembersContractAddressPage.phnInput, '9873102617')
    // When I click the submit button
    .click(GetGroupMembersContractAddressPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // And a table with only one result, the searched on Group Member, exists.
    .expect(GetGroupMembersContractAddressPage.resultsTable.exists)
    .ok()
    .expect(GetGroupMembersContractAddressPage.resultsTable.child('thead').exists)
    .ok()
    .expect(GetGroupMembersContractAddressPage.resultsTable.child('tbody').child('tr').count)
    .eql(1)
    .expect(GetGroupMembersContractAddressPage.resultsRow1.exists)
    .ok()
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').exists)
    .ok()
    // A address table with two results
    .expect(GetGroupMembersContractAddressPage.addressTable.exists)
    .ok()
    .expect(GetGroupMembersContractAddressPage.addressTable.child('thead').exists)
    .ok()
    .expect(GetGroupMembersContractAddressPage.addressTable.child('tbody').child('tr').count)
    .eql(2)
    .expect(GetGroupMembersContractAddressPage.addressRow1.exists)
    .ok()
    .expect(GetGroupMembersContractAddressPage.addressRow1.child('td').nth(0).textContent)
    .eql('Home Address')
    .expect(GetGroupMembersContractAddressPage.addressRow1.child('td').nth(3).textContent)
    .eql('KELOWNA BC')
    .expect(GetGroupMembersContractAddressPage.addressRow1.child('td').exists)
    .ok()
    .expect(GetGroupMembersContractAddressPage.addressRow2.child('td').nth(0).textContent)
    .eql('Mailing Address')
    .expect(GetGroupMembersContractAddressPage.addressRow2.child('td').nth(3).textContent)
    .eql('VANCOUVER BC')
    // Validate the first row
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(0).textContent)
    .eql('9873102617')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(1).textContent)
    .eql('MORRISON')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(2).textContent)
    .eql('MORGAN')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(3).textContent)
    .eql('')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(4).textContent)
    .eql('')
})

test.skip('Check properly filled form passes validation and no results shown when more than 20 found', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(GetGroupMembersContractAddressPage.groupNumberInput, '6243109')
    .typeText(GetGroupMembersContractAddressPage.phnInput, '9332912486')
    // When I click the submit button
    .click(GetGroupMembersContractAddressPage.submitButton)
    // I expect a warning message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(WARNING_MESSAGE)
    //And the resultTable is reset to 0 records as no data is shown when this message occurs
    .expect(GetGroupMembersContractAddressPage.resultsTable.child('tbody').child('tr').count)
    .eql(0)
})

test.skip('Check properly filled form passes validation and displays no result if coverage not exists', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(GetGroupMembersContractAddressPage.groupNumberInput, '6337109')
    .typeText(GetGroupMembersContractAddressPage.phnInput, '9873672255')
    // When I click the submit button
    .click(GetGroupMembersContractAddressPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(NO_COVEREAGE_ERROR_MESSAGE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(GetGroupMembersContractAddressPage.phnInput, '9000448000')
    .typeText(GetGroupMembersContractAddressPage.groupNumberInput, '6337109')
    // When I click the Clear button
    .click(GetGroupMembersContractAddressPage.clearButton)
    // I expect the form to be cleared
    .expect(GetGroupMembersContractAddressPage.phnInput.value)
    .eql('')
    .expect(GetGroupMembersContractAddressPage.groupNumberInput.value)
    .eql('')
})
