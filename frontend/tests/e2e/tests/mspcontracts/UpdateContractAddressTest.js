import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import UpdateContractAddress from '../../pages/mspcontracts/UpdateContractAddress'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const INVALID_ADDRESS_LINE1_MESSAGE = 'Address Line 1 is invalid'
const INVALID_ADDRESS_LINE2_MESSAGE = 'Address Line 2 is invalid'
const INVALID_ADDRESS_LINE3_MESSAGE = 'Address Line 3 is invalid'
const INVALID_ADDRESS_LINE4_MESSAGE = 'Address Line 4 is invalid'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const HOME_ADDRESS_REQUIRED_MESSAGE = 'Home Address Line 1 is required'
const MAILING_ADDRESS_REQUIRED_MESSAGE = 'Mailing Address Line 1 is required'
const POSTAL_CODE_REQUIRED_MESSAGE = 'Postal Code is required'
const INVALID_POSTAL_CODE_VALIDATION_MESSAGE = 'Postal Code is invalid'
const SUCCESS_MESSAGE = 'RPBS0103 COVERAGE CANCELLED. NO UPDATE PERMITTED.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/mspcontracts/UpdateContractAddress'

fixture(`UpdateContractAddress Page`).disablePageCaching`Test UpdateContractAddress`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // When I click the submit button
    .click(UpdateContractAddress.submitButton)
    .wait(1000)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(2).textContent)
    .contains(HOME_ADDRESS_REQUIRED_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(3).textContent)
    .contains(POSTAL_CODE_REQUIRED_MESSAGE)
})

test('Check required fields validation for Mailing Address', async (t) => {
  await t
    // When I click the submit button
    .click(UpdateContractAddress.submitButton)
    .wait(1000)

    .typeText(UpdateContractAddress.mailingAddress2Input, 'TEST ADDRESS LINE 2')
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(2).textContent)
    .contains(HOME_ADDRESS_REQUIRED_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(3).textContent)
    .contains(POSTAL_CODE_REQUIRED_MESSAGE)

    //Although Mailing Address Line 1 is optional, if any other mailing address line is completed, it becomes required
    .expect(UpdateContractAddress.errorText.nth(4).textContent)
    .contains(MAILING_ADDRESS_REQUIRED_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(UpdateContractAddress.groupNumberInput, '6337109')
    .click(UpdateContractAddress.phnInput)
    .typeText(UpdateContractAddress.phnInput, '9882807277')
    .typeText(UpdateContractAddress.address1Input, 'Test 111 ST')
    .typeText(UpdateContractAddress.postalCodeInput, 'V8V8V8')

    // When I click the submit button
    .click(UpdateContractAddress.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
})

test('Check invalid field validation', async (t) => {
  await t
    // Given a Group Number entered with an invalid format
    .typeText(UpdateContractAddress.groupNumberInput, '9000444000')
    .click(UpdateContractAddress.phnInput)
    .typeText(UpdateContractAddress.phnInput, '9000444000')
    .typeText(UpdateContractAddress.address1Input, 'Test 111 ST!@#$%')
    .typeText(UpdateContractAddress.address2Input, 'Test 111 ST()_+{}')
    .typeText(UpdateContractAddress.address3Input, '!@#!@#')
    .typeText(UpdateContractAddress.address4Input, '{}{}{}}')
    .typeText(UpdateContractAddress.postalCodeInput, 'T6T6T6')
    .typeText(UpdateContractAddress.mailingAddress1Input, 'Test 111 ST!@#$%')
    .typeText(UpdateContractAddress.mailingAddress2Input, 'Test 111 ST()_+{}')
    .typeText(UpdateContractAddress.mailingAddress3Input, '!@#!@#')
    .typeText(UpdateContractAddress.mailingAddress4Input, '{}{}{}}')
    .typeText(UpdateContractAddress.mailingPostalCodeInput, 'TTTTTT')
    // When I click the submit button
    .click(UpdateContractAddress.submitButton)
    // I expect an error message stating the page had errors and an individual error message for invalid inputs
    .expect(UpdateContractAddress.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(2).textContent)
    .contains(INVALID_ADDRESS_LINE1_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(3).textContent)
    .contains(INVALID_ADDRESS_LINE2_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(4).textContent)
    .contains(INVALID_ADDRESS_LINE3_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(5).textContent)
    .contains(INVALID_ADDRESS_LINE4_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(6).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(7).textContent)
    .contains(INVALID_ADDRESS_LINE1_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(8).textContent)
    .contains(INVALID_ADDRESS_LINE2_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(9).textContent)
    .contains(INVALID_ADDRESS_LINE3_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(10).textContent)
    .contains(INVALID_ADDRESS_LINE4_MESSAGE)
    .expect(UpdateContractAddress.errorText.nth(11).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(UpdateContractAddress.groupNumberInput, '6337109')
    .typeText(UpdateContractAddress.phnInput, '9882807277')

    .typeText(UpdateContractAddress.address1Input, 'Test 111 ST')
    .typeText(UpdateContractAddress.address2Input, 'Test 111 ST')
    .typeText(UpdateContractAddress.address3Input, 'Test 111 ST')
    .typeText(UpdateContractAddress.address4Input, 'VANCOUVER BC')
    .typeText(UpdateContractAddress.postalCodeInput, 'V8V8V8')
    .typeText(UpdateContractAddress.mailingAddress1Input, 'Test 222 ST')
    .typeText(UpdateContractAddress.mailingAddress2Input, 'Test 222 ST')
    .typeText(UpdateContractAddress.mailingAddress3Input, 'Test 222 ST')
    .typeText(UpdateContractAddress.mailingAddress4Input, 'EDMONTON ALBERTA')
    .typeText(UpdateContractAddress.mailingPostalCodeInput, 'T6T6T6')

    // When I click the clear button
    .click(UpdateContractAddress.clearButton)
    // I expect the form to be cleared
    .expect(UpdateContractAddress.groupNumberInput.value)
    .eql('')
    .expect(UpdateContractAddress.phnInput.value)
    .eql('')
    .expect(UpdateContractAddress.address1Input.value)
    .eql('')
    .expect(UpdateContractAddress.address2Input.value)
    .eql('')
    .expect(UpdateContractAddress.address3Input.value)
    .eql('')
    .expect(UpdateContractAddress.address4Input.value)
    .eql('')
    .expect(UpdateContractAddress.postalCodeInput.value)
    .eql('')
    .expect(UpdateContractAddress.mailingAddress1Input.value)
    .eql('')
    .expect(UpdateContractAddress.mailingAddress2Input.value)
    .eql('')
    .expect(UpdateContractAddress.mailingAddress3Input.value)
    .eql('')
    .expect(UpdateContractAddress.mailingAddress4Input.value)
    .eql('')
    .expect(UpdateContractAddress.mailingPostalCodeInput.value)
    .eql('')
})
