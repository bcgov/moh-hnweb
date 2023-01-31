import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ContractInquiryPage from '../../pages/mspcontracts/ContractInquiryPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUCCESS_MESSAGE = 'RPBS9014 TRANSACTION COMPLETED'
const WARNING_MESSAGE = 'RPBS0059 MORE THAN 20 PERSONS. PLEASE CONTACT MSP'
const NO_COVERAGE_ERROR_MESSAGE = 'RPBS0067 NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/mspContracts/contractInquiry'

fixture(`Contract Inquiry Page`).disablePageCaching`Test Contract Inquiry`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ContractInquiryPage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(ContractInquiryPage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
})

test('Check invalid phn, groupNumber format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ContractInquiryPage.phnInput, '9000448000')
    .typeText(ContractInquiryPage.groupNumberInput, '0000001')
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ContractInquiryPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ContractInquiryPage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check properly filled form passes validation and validate Group Member Details and Demographics', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ContractInquiryPage.groupNumberInput, '6099733')
    .typeText(ContractInquiryPage.phnInput, '9873102617')
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // A Group Member Details table with two results
    .expect(ContractInquiryPage.identifierTable.exists)
    .ok()
    .expect(ContractInquiryPage.identifierTable.child('thead').exists)
    .ok()
    .expect(ContractInquiryPage.identifierTable.child('tbody').child('tr').count)
    .eql(2)
    .expect(ContractInquiryPage.identifierRow1.exists)
    .ok()
    .expect(ContractInquiryPage.identifierRow1.child('td').exists)
    .ok()
    // A Demographics table with three results
    .expect(ContractInquiryPage.resultsTable.exists)
    .ok()
    .expect(ContractInquiryPage.resultsTable.child('thead').exists)
    .ok()
    .expect(ContractInquiryPage.resultsTable.child('tbody').child('tr').count)
    .eql(3)
    .expect(ContractInquiryPage.resultsRow1.exists)
    .ok()
    .expect(ContractInquiryPage.resultsRow1.child('td').exists)
    .ok()
    // Validate the first row
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(0).textContent)
    .eql('9873102617')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(1).textContent)
    .eql('MORRISON, MORGAN')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(2).textContent)
    .eql('19730219')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(3).textContent)
    .eql('M')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(4).textContent)
    .eql('Employee')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(5).textContent)
    .eql('N')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(6).textContent)
    .eql('20221201')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(7).textContent)
    .eql('')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(8).textContent)
    .eql(' ')
    // Validate the last row
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(0).textContent)
    .eql('9873102584')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(1).textContent)
    .eql('MORRISON, KENNETH')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(2).textContent)
    .eql('20160617')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(3).textContent)
    .eql('M')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(4).textContent)
    .eql('Dependent')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(5).textContent)
    .eql('N')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(6).textContent)
    .eql('20221201')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(7).textContent)
    .eql('')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(8).textContent)
    .eql(' ')
})

test('heck properly filled form passes validation and validate Addresses', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ContractInquiryPage.groupNumberInput, '6180442')
    .typeText(ContractInquiryPage.phnInput, '9872968646')
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // An address table with two results with all fields populated
    .expect(ContractInquiryPage.addressTable.exists)
    .ok()
    .expect(ContractInquiryPage.addressTable.child('thead').exists)
    .ok()
    .expect(ContractInquiryPage.addressTable.child('tbody').child('tr').count)
    .eql(2)
    .expect(ContractInquiryPage.addressRow1.exists)
    .ok()
    .expect(ContractInquiryPage.addressRow1.child('td').nth(0).textContent)
    .eql('Home Address')
    .expect(ContractInquiryPage.addressRow1.child('td').nth(1).textContent)
    .eql('HOME ADDRESS LINE 1')
    .expect(ContractInquiryPage.addressRow1.child('td').nth(2).textContent)
    .eql('HOME ADDRESS LINE 2')
    .expect(ContractInquiryPage.addressRow1.child('td').nth(3).textContent)
    .eql('HOME ADDRESS LINE 3')
    .expect(ContractInquiryPage.addressRow1.child('td').nth(4).textContent)
    .eql('VANCOUVER BC')
    .expect(ContractInquiryPage.addressRow1.child('td').nth(5).textContent)
    .eql('V8V8V8')
    .expect(ContractInquiryPage.addressRow2.exists)
    .ok()
    .expect(ContractInquiryPage.addressRow2.child('td').nth(0).textContent)
    .eql('Mailing Address')
    .expect(ContractInquiryPage.addressRow2.child('td').nth(1).textContent)
    .eql('MAIL ADDRESS LINE 1')
    .expect(ContractInquiryPage.addressRow2.child('td').nth(2).textContent)
    .eql('MAIL ADDRESS LINE 2')
    .expect(ContractInquiryPage.addressRow2.child('td').nth(3).textContent)
    .eql('MAIL ADDRESS LINE 3')
    .expect(ContractInquiryPage.addressRow2.child('td').nth(4).textContent)
    .eql('CALGARY AB')
    .expect(ContractInquiryPage.addressRow2.child('td').nth(5).textContent)
    .eql('T2G5E6')
})

// TODO Need new test data - currently returns "RPBS0067 NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP"
test.skip('Check properly filled form passes validation and validate results when more than 20', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ContractInquiryPage.groupNumberInput, '6243109')
    .typeText(ContractInquiryPage.phnInput, '9332912486')
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect a warning message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(WARNING_MESSAGE)
    //And Person Demographic info
    .expect(ContractInquiryPage.personInfo.nth(2).textContent)
    .contains('Home Address')
    .expect(ContractInquiryPage.personInfo.nth(3).textContent)
    .contains('Mailing Address')
    .expect(ContractInquiryPage.personInfo.nth(4).textContent)
    .contains('Line 15951 WDSOU YF')
    .expect(ContractInquiryPage.personInfo.nth(5).textContent)
    .contains('Line 15961 WDSOU ZF')
    .expect(ContractInquiryPage.personInfo.nth(6).textContent)
    .contains('Line 2ZT5')
    .expect(ContractInquiryPage.personInfo.nth(7).textContent)
    .contains('Line 2ZT6')
    .expect(ContractInquiryPage.personInfo.nth(8).textContent)
    .contains('Line 3ZT5')
    .expect(ContractInquiryPage.personInfo.nth(9).textContent)
    .contains('Line 3CRESTON BC')
    .expect(ContractInquiryPage.personInfo.nth(10).textContent)
    .contains('Line 4ARMSTRONG')
    .expect(ContractInquiryPage.personInfo.nth(11).textContent)
    .contains('Line 4')
    .expect(ContractInquiryPage.personInfo.nth(12).textContent)
    .contains('Postal CodeV4D7D7')
    .expect(ContractInquiryPage.personInfo.nth(13).textContent)
    .contains('Postal CodeV8V8V8')
    .expect(ContractInquiryPage.personInfo.nth(14).textContent)
    .contains('Telephone250 6301086')
    .expect(ContractInquiryPage.personInfo.nth(15).textContent)
    .contains('Group Member Number123456')
    .expect(ContractInquiryPage.departmentInfo.nth(0).textContent)
    .contains('Group Member Department Number123456')
    //And resultTable with 20 records
    .expect(ContractInquiryPage.resultsTable.child('tbody').child('tr').count)
    .eql(20)
})

test('Check properly filled form passes validation and displays no result if coverage not exists', async (t) => {
  // Need new test data
  await t
    // Given the page is filled out correctly
    .typeText(ContractInquiryPage.groupNumberInput, '6243109')
    .typeText(ContractInquiryPage.phnInput, '9332912486')
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(NO_COVERAGE_ERROR_MESSAGE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ContractInquiryPage.phnInput, '9000448000')
    .typeText(ContractInquiryPage.groupNumberInput, '6337109')
    // When I click the Clear button
    .click(ContractInquiryPage.clearButton)
    // I expect the form to be cleared
    .expect(ContractInquiryPage.phnInput.value)
    .eql('')
    .expect(ContractInquiryPage.groupNumberInput.value)
    .eql('')
})
