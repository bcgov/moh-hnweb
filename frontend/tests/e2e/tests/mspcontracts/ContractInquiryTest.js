import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ContractInquiryPage from '../../pages/mspcontracts/ContractInquiryPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUCCESS_MESSAGE = 'TRANSACTION SUCCESSFUL'
const WARNING_MESSAGE = 'RPBS0059 MORE THAN 20 PERSONS. PLEASE CONTACT MSP'
const NO_COVEREAGE_ERROR_MESSAGE = 'RPBS0067 NO COVERAGE FOUND FOR THE PHN ENTERED. PLEASE CONTACT MSP'
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

test('Check properly filled form passes validation and validate results', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ContractInquiryPage.groupNumberInput, '6337109')
    .typeText(ContractInquiryPage.phnInput, '9331926919')
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // And a table with three results
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
    .eql('9331926919')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(1).textContent)
    .eql('PROTOCTIST ORDERXD, JACK-LYALLXH MO-CHARAXI')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(2).textContent)
    .eql('20020705')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(3).textContent)
    .eql('F')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(4).textContent)
    .eql('Employee')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(5).textContent)
    .eql('N')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(6).textContent)
    .eql('20220301')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(7).textContent)
    .eql('')
    .expect(ContractInquiryPage.resultsRow1.child('td').nth(8).textContent)
    .eql(' ')
    // Validate the last row
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(0).textContent)
    .eql('9348175493')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(1).textContent)
    .eql('PROTOCTIST ORDERXD, SYCAMOREXC SIMONIDESXD')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(2).textContent)
    .eql('20021223')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(3).textContent)
    .eql('F')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(4).textContent)
    .eql('Dependent')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(5).textContent)
    .eql('Y')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(6).textContent)
    .eql('20220101')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(7).textContent)
    .eql('20220228')
    .expect(ContractInquiryPage.resultsRow3.child('td').nth(8).textContent)
    .eql('E')
})

test('Check properly filled form passes validation and validate results when more than 20', async (t) => {
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
  await t
    // Given the page is filled out correctly
    .typeText(ContractInquiryPage.groupNumberInput, '6337109')
    .typeText(ContractInquiryPage.phnInput, '9873672255')
    // When I click the submit button
    .click(ContractInquiryPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(NO_COVEREAGE_ERROR_MESSAGE)
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
