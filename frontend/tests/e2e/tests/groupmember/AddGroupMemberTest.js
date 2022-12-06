import AddGroupMember from '../../pages/groupmember/AddGroupMember'
import AlertPage from '../../pages/AlertPage'
import { SITE_UNDER_TEST } from '../../configuration'
import { regularAccUser } from '../../roles/roles'

const INVALID_ADDRESS_LINE1_MESSAGE = 'Address Line 1 is invalid'
const INVALID_ADDRESS_LINE2_MESSAGE = 'Address Line 2 is invalid'
const INVALID_CITY_MESSAGE = 'City is invalid'
const INVALID_PROVINCE_MESSAGE = 'Province is invalid'
const MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE = 'The maximum length allowed is 25'
const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const EFFECTIVE_DATE_REQUIRED_MESSAGE = 'Coverage Effective Date is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE = 'Group Member Number is invalid'
const INVALID_DEPARTMENT_NUMBER_ERROR_MESSAGE = 'Department Number is invalid'
const HOME_ADDRESS_REQUIRED_MESSAGE = 'Home Address Line 1 is required'
const MAILING_ADDRESS_REQUIRED_MESSAGE = 'Mailing Address Line 1 is required'
const CITY_REQUIRED_MESSAGE = 'City is required'
const PROVINCE_REQUIRED_MESSAGE = 'Province is required'
const POSTAL_CODE_REQUIRED_MESSAGE = 'Postal Code is required'
const INVALID_POSTAL_CODE_VALIDATION_MESSAGE = 'Postal Code is invalid'
//const SUCCESS_MESSAGE = 'RPBS0031 9882807277 PHN IS INELIGIBLE. PLEASE FORWARD SOURCE DOCS TO MSP'
const SUCCESS_MESSAGE = 'RPBS0065 9882807277 COVERAGE ALREADY EXISTS FOR THE PHN/GROUP NUMBER SPECIFIED'
const PHONE_NUMBER_VALIDATION_MESSAGE = 'Only numbers 0 to 9 are valid. Phone Number must be entered as ten (10) numbers in length with no space or hyphen.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/AddGroupMember'

fixture(`AddGroupMember Page`).disablePageCaching`Test AddGroupMember`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // When I click the submit button
    .click(AddGroupMember.submitButton)
    .wait(1000)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(1).textContent)
    .contains(EFFECTIVE_DATE_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(2).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(3).textContent)
    .contains(HOME_ADDRESS_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(4).textContent)
    .contains(CITY_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(5).textContent)
    .contains(PROVINCE_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(6).textContent)
    .contains(POSTAL_CODE_REQUIRED_MESSAGE)
})

test('Check required Mailing Address field validation', async (t) => {
  await t
    // When I click the submit button
    .click(AddGroupMember.submitButton)
    .wait(1000)
    .typeText(AddGroupMember.mailingAddress2Input, 'TEST ADDRESS LINE 2')
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(1).textContent)
    .contains(EFFECTIVE_DATE_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(2).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(3).textContent)
    .contains(HOME_ADDRESS_REQUIRED_MESSAGE)
    //Although Mailing Address Line 1 is optional, if any other mailing address line is completed, it becomes required
    .expect(AddGroupMember.errorText.nth(4).textContent)
    .contains(MAILING_ADDRESS_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(5).textContent)
    .contains(CITY_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(6).textContent)
    .contains(PROVINCE_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(7).textContent)
    .contains(CITY_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(8).textContent)
    .contains(PROVINCE_REQUIRED_MESSAGE)
    .expect(AddGroupMember.errorText.nth(9).textContent)
    .contains(POSTAL_CODE_REQUIRED_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(AddGroupMember.groupNumberInput, '6337109')
    .click(AddGroupMember.coverageEffectiveDateInput)
    .click(AddGroupMember.divSelectedDate)
    .click(AddGroupMember.phnInput)
    .typeText(AddGroupMember.phnInput, '9882807277')
    .typeText(AddGroupMember.address1Input, 'Test 111 ST')
    .typeText(AddGroupMember.homeAddressCityInput, 'Victoria')
    .typeText(AddGroupMember.homeAddressProvinceInput, 'British Columbia')
    .typeText(AddGroupMember.postalCodeInput, 'V8V8V8')

    // When I click the submit button
    .click(AddGroupMember.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
})

test('Check PHN, Group Number format validation', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(AddGroupMember.groupNumberInput, '6007109')
    .click(AddGroupMember.coverageEffectiveDateInput)
    .click(AddGroupMember.divSelectedDate)
    .click(AddGroupMember.phnInput)
    .typeText(AddGroupMember.phnInput, '9002807277')
    .typeText(AddGroupMember.address1Input, 'Test 111 ST')
    .typeText(AddGroupMember.homeAddressCityInput, 'Victoria')
    .typeText(AddGroupMember.homeAddressProvinceInput, 'British Columbia')
    .typeText(AddGroupMember.postalCodeInput, 'V8V8V8')

    // When I click the submit button
    .click(AddGroupMember.submitButton)
    // I expect an error message stating the page had errors and an individual error message for invalid input format
    .expect(AddGroupMember.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check input field length validation', async (t) => {
  await t
    // Given a Group Number entered with an invalid format
    .typeText(AddGroupMember.groupNumberInput, '9000444000000')
    .click(AddGroupMember.coverageEffectiveDateInput)
    .click(AddGroupMember.divSelectedDate)
    .click(AddGroupMember.phnInput)
    .typeText(AddGroupMember.phnInput, '9000444000000000000000')
    .typeText(AddGroupMember.groupMemberNumberInput, '11111111111111111')
    .typeText(AddGroupMember.departmentNumberInput, '1111111111')
    .typeText(AddGroupMember.telephoneInput, '78077777777777')
    .typeText(AddGroupMember.address1Input, 'Address Line 1 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.address2Input, 'Address Line 2 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.homeAddressCityInput, 'Address Line 3 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.homeAddressProvinceInput, 'Address Line 4 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.postalCodeInput, 'V8V 8V8')
    .typeText(AddGroupMember.mailingAddress1Input, 'Mailing Address Line 1 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.mailingAddress2Input, 'Mailing Address Line 2 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.mailingAddressCityInput, 'Mailing Address Line 3 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.mailingAddressProvinceInput, 'Mailing Address Line 4 is toooooooooooooooooooooooooooooooo long')
    .typeText(AddGroupMember.mailingPostalCodeInput, 'V8V 8V8')
    // When I click the submit button
    .click(AddGroupMember.submitButton)
    // I expect an error message stating the page had errors and an individual error message for invalid inputs
    .expect(AddGroupMember.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(2).textContent)
    .contains(INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(3).textContent)
    .contains(INVALID_DEPARTMENT_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(4).textContent)
    .contains(PHONE_NUMBER_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(5).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(6).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(7).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(8).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(9).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(10).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(11).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(12).textContent)
    .contains(MAX_LENGTH_ADDRESS_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(13).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(14).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check invalid character validation', async (t) => {
  await t
    // Given a Group Number entered with an invalid format
    .typeText(AddGroupMember.groupNumberInput, '900044400@')
    .click(AddGroupMember.coverageEffectiveDateInput)
    .click(AddGroupMember.divSelectedDate)
    .click(AddGroupMember.phnInput)
    .typeText(AddGroupMember.phnInput, '9000444@@@')
    .typeText(AddGroupMember.groupMemberNumberInput, '!@^^^')
    .typeText(AddGroupMember.departmentNumberInput, '!@^^34')
    .typeText(AddGroupMember.telephoneInput, '7807777@@')
    .typeText(AddGroupMember.address1Input, 'Test 111 ST!@#$%')
    .typeText(AddGroupMember.address2Input, 'Test 111 ST()_+{}')
    .typeText(AddGroupMember.homeAddressCityInput, '!@#!@#')
    .typeText(AddGroupMember.homeAddressProvinceInput, '{}{}{}}')
    .typeText(AddGroupMember.postalCodeInput, '@@@@@@@')
    .typeText(AddGroupMember.mailingAddress1Input, 'Test 111 ST!@#$%')
    .typeText(AddGroupMember.mailingAddress2Input, 'Test 111 ST()_+{}')
    .typeText(AddGroupMember.mailingAddressCityInput, '!@#!@#')
    .typeText(AddGroupMember.mailingAddressProvinceInput, '{}{}{}}')
    .typeText(AddGroupMember.mailingPostalCodeInput, '$%^&*(')
    // When I click the submit button
    .click(AddGroupMember.submitButton)
    // I expect an error message stating the page had errors and an individual error message for invalid inputs
    .expect(AddGroupMember.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(2).textContent)
    .contains(INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(3).textContent)
    .contains(INVALID_DEPARTMENT_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(4).textContent)
    .contains(PHONE_NUMBER_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(5).textContent)
    .contains(INVALID_ADDRESS_LINE1_MESSAGE)
    .expect(AddGroupMember.errorText.nth(6).textContent)
    .contains(INVALID_ADDRESS_LINE1_MESSAGE)
    .expect(AddGroupMember.errorText.nth(7).textContent)
    .contains(INVALID_ADDRESS_LINE2_MESSAGE)
    .expect(AddGroupMember.errorText.nth(8).textContent)
    .contains(INVALID_ADDRESS_LINE2_MESSAGE)
    .expect(AddGroupMember.errorText.nth(9).textContent)
    .contains(INVALID_CITY_MESSAGE)
    .expect(AddGroupMember.errorText.nth(10).textContent)
    .contains(INVALID_PROVINCE_MESSAGE)
    .expect(AddGroupMember.errorText.nth(11).textContent)
    .contains(INVALID_CITY_MESSAGE)
    .expect(AddGroupMember.errorText.nth(12).textContent)
    .contains(INVALID_PROVINCE_MESSAGE)
    .expect(AddGroupMember.errorText.nth(13).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(14).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check click Add button adds dependent', async (t) => {
  await t
    // Given a Group Number entered with an invalid format
    .typeText(AddGroupMember.dependentPhn, '9882807277')
    //When I click Add buttton
    .click(AddGroupMember.addButton)
    // I expect entered phn is added in depenedent list
    .expect(AddGroupMember.dependentText.innerText)
    .eql('9882807277 ')
})

test('Check invalid dependent PHN format', async (t) => {
  await t
    // Given a Group Number entered with an invalid format
    .typeText(AddGroupMember.dependentPhn, '988280727')
    //When I click Add buttton
    .click(AddGroupMember.addButton)
    // I expect an error message stating the page had errors and an individual error message for invalid PHN
    .expect(AddGroupMember.errorText.nth(0).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check click Remove icon removes selected dependent', async (t) => {
  await t
    // Given a Group Number entered with an valid format
    .typeText(AddGroupMember.dependentPhn, '9882807277')
    //When I click Add buttton
    .click(AddGroupMember.addButton)
    .click(AddGroupMember.removeIcon)
    // I expect dependent phn is removed from depenedent list
    .expect(AddGroupMember.dependentList.length)
    .eql(0)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(AddGroupMember.groupNumberInput, '6337109')
    .click(AddGroupMember.coverageEffectiveDateInput)
    .click(AddGroupMember.divSelectedDate)
    .typeText(AddGroupMember.phnInput, '9882807277')
    .typeText(AddGroupMember.groupMemberNumberInput, '000001')
    .typeText(AddGroupMember.departmentNumberInput, '000002')
    .typeText(AddGroupMember.telephoneInput, '7802024022')
    .typeText(AddGroupMember.address1Input, 'Test 111 ST')
    .typeText(AddGroupMember.address2Input, 'Test 111 ST')
    .typeText(AddGroupMember.homeAddressCityInput, 'VANCOUVER')
    .typeText(AddGroupMember.homeAddressProvinceInput, 'British Columbia')
    .typeText(AddGroupMember.postalCodeInput, 'V8V8V8')
    .typeText(AddGroupMember.mailingAddress1Input, 'Test 222 ST')
    .typeText(AddGroupMember.mailingAddress2Input, 'Test 222 ST')
    .typeText(AddGroupMember.mailingAddressCityInput, 'EDMONTON')
    .typeText(AddGroupMember.mailingAddressProvinceInput, 'ALBERTA')
    .typeText(AddGroupMember.mailingPostalCodeInput, 'T6T6T6')
    // When I click the clear button
    .click(AddGroupMember.clearButton)
    // I expect the form to be cleared
    .expect(AddGroupMember.groupNumberInput.value)
    .eql('')
    .expect(AddGroupMember.phnInput.value)
    .eql('')
    .expect(AddGroupMember.groupMemberNumberInput.value)
    .eql('')
    .expect(AddGroupMember.departmentNumberInput.value)
    .eql('')
    .expect(AddGroupMember.address1Input.value)
    .eql('')
    .expect(AddGroupMember.address2Input.value)
    .eql('')
    .expect(AddGroupMember.homeAddressCityInput.value)
    .eql('')
    .expect(AddGroupMember.homeAddressProvinceInput.value)
    .eql('')
    .expect(AddGroupMember.postalCodeInput.value)
    .eql('')
    .expect(AddGroupMember.telephoneInput.value)
    .eql('')
    .expect(AddGroupMember.mailingAddress1Input.value)
    .eql('')
    .expect(AddGroupMember.mailingAddress2Input.value)
    .eql('')
    .expect(AddGroupMember.mailingAddressCityInput.value)
    .eql('')
    .expect(AddGroupMember.mailingAddressProvinceInput.value)
    .eql('')
    .expect(AddGroupMember.mailingPostalCodeInput.value)
    .eql('')
    .expect(AddGroupMember.coverageEffectiveDateInput.value)
    .eql(undefined)

    .expect(AddGroupMember.dependentPhn.value)
    .eql('')
    .expect(AddGroupMember.dependentList.length)
    .eql(0)
})
