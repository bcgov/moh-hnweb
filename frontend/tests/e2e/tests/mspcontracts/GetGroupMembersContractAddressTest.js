import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import GetGroupMembersContractAddressPage from '../../pages/mspcontracts/GetGroupMembersContractAddressPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUCCESS_MESSAGE = 'TRANSACTION SUCCESSFUL'
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
    .typeText(GetGroupMembersContractAddressPage.groupNumberInput, '6337109')
    .typeText(GetGroupMembersContractAddressPage.phnInput, '9331926919')
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
    // Validate the first row
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(0).textContent)
    .eql('9331926919')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(1).textContent)
    .eql('PROTOCTIST ORDERXD')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(2).textContent)
    .eql('JACK-LYALLXH')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(3).textContent)
    .eql('MO-CHARAXI')
    .expect(GetGroupMembersContractAddressPage.resultsRow1.child('td').nth(4).textContent)
    .eql('')
    // And demographic info
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(2).textContent)
    .contains('Home Address')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(3).textContent)
    .contains('Mailing Address')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(4).textContent)
    .contains('Line 1123')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(5).textContent)
    .contains('Line 1123')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(6).textContent)
    .contains('Line 2')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(7).textContent)
    .contains('Line 2')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(8).textContent)
    .contains('Line 3')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(9).textContent)
    .contains('Line 3')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(10).textContent)
    .contains('Line 4')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(11).textContent)
    .contains('Line 4')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(12).textContent)
    .contains('Postal Code')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(13).textContent)
    .contains('Postal CodeV8V8V8')
    .expect(GetGroupMembersContractAddressPage.personInfo.nth(14).textContent)
    .contains('Telephone780 2824033')
})

test('Check properly filled form passes validation and no results shown when more than 20 found', async (t) => {
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

test('Check properly filled form passes validation and displays no result if coverage not exists', async (t) => {
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
