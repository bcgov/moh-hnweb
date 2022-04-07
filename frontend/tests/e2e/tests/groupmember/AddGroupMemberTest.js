import AddGroupMember from '../../pages/groupmember/AddGroupMember'
import AlertPage from '../../pages/AlertPage'
import { SITE_UNDER_TEST } from '../../configuration'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const EFFECTIVE_DATE_REQUIRED_MESSAGE = 'Coverage Effective Date is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const HOME_ADDRESS_REQUIRED_MESSAGE = 'Home Address Line 1 is required'
const POSTAL_CODE_REQUIRED_MESSAGE = 'Postal Code is required'
const INVALID_POSTAL_CODE_VALIDATION_MESSAGE = 'Postal Code is invalid'
const SUCCESS_MESSAGE = 'RPBS0031 9882807277 PHN IS INELIGIBLE. PLEASE FORWARD SOURCE DOCS TO MSP'
const PHONE_NUMBER_VALIDATION_MESSAGE = 'Only numbers 0 to 9 are valid. Phone Number must be entered as ten (10) numbers in length with no space or hyphen.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/AddGroupMember'

fixture(`AddGroupMember Page`).disablePageCaching`Test AddGroupMember`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser)
  })
  .page(PAGE_TO_TEST)

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
    .typeText(AddGroupMember.postalCodeInput, 'V8V8V8')

    // When I click the submit button
    .click(AddGroupMember.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
})

test('Check invalid field validation', async (t) => {
  await t
    // Given a Group Number entered with an invalid format
    .typeText(AddGroupMember.groupNumberInput, '9000444000')
    .click(AddGroupMember.coverageEffectiveDateInput)
    .click(AddGroupMember.divSelectedDate)
    .click(AddGroupMember.phnInput)
    .typeText(AddGroupMember.phnInput, '9000444000')
    .typeText(AddGroupMember.telephoneInput, '7807777')
    .typeText(AddGroupMember.address1Input, 'Test 111 ST')
    .typeText(AddGroupMember.postalCodeInput, 'T6T6T6')
    .typeText(AddGroupMember.mailingPostalCodeInput, 'TTTTTT')
    // When I click the submit button
    .click(AddGroupMember.submitButton)
    // I expect an error message stating the page had errors and an individual error message for invalid inputs
    .expect(AddGroupMember.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AddGroupMember.errorText.nth(2).textContent)
    .contains(PHONE_NUMBER_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(3).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AddGroupMember.errorText.nth(4).textContent)
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
    .typeText(AddGroupMember.address3Input, 'Test 111 ST')
    .typeText(AddGroupMember.address4Input, 'VANCOUVER BC')
    .typeText(AddGroupMember.postalCodeInput, 'V8V8V8')
    .typeText(AddGroupMember.mailingAddress1Input, 'Test 222 ST')
    .typeText(AddGroupMember.mailingAddress2Input, 'Test 222 ST')
    .typeText(AddGroupMember.mailingAddress3Input, 'Test 222 ST')
    .typeText(AddGroupMember.mailingAddress4Input, 'EDMONTON ALBERTA')
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
    .expect(AddGroupMember.address3Input.value)
    .eql('')
    .expect(AddGroupMember.address4Input.value)
    .eql('')
    .expect(AddGroupMember.postalCodeInput.value)
    .eql('')
    .expect(AddGroupMember.telephoneInput.value)
    .eql('')
    .expect(AddGroupMember.mailingAddress1Input.value)
    .eql('')
    .expect(AddGroupMember.mailingAddress2Input.value)
    .eql('')
    .expect(AddGroupMember.mailingAddress3Input.value)
    .eql('')
    .expect(AddGroupMember.mailingAddress4Input.value)
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
