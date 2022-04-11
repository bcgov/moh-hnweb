import AddVisaResidentWithoutPHNPage from '../../pages/enrollment/AddVisaResidentWithoutPHNPage'
import AlertPage from '../../pages/AlertPage'
import NameSearchPage from '../../pages/enrollment/NameSearchPage'
import { SITE_UNDER_TEST } from '../../configuration'
import { regularAccUser } from '../../roles/roles'

const immigrationCodeOption = AddVisaResidentWithoutPHNPage.immigrationCodeSelect.find('option')
const provinceOption = AddVisaResidentWithoutPHNPage.provinceSelect.find('option')
const priorResidenceCodeOption = AddVisaResidentWithoutPHNPage.priorResidenceCodeInput.find('option')

const ERROR_MESSAGE = 'Please correct errors before submitting'
const SUCCESS_MESSAGE = 'COVERAGE CANCEL DATE MUST BE AFTER COVERAGE EFFECTIVE DATE'
const NO_SEARCH_RESULT = 'No results were returned. Please ensure you have entered the correct information.'

const INVALID_ADDRESS_LINE1_MESSAGE = 'Address Line 1 is invalid'
const INVALID_ADDRESS_LINE2_MESSAGE = 'Address Line 2 is invalid'
const INVALID_ADDRESS_LINE3_MESSAGE = 'Address Line 3 is invalid'
const GROUPNUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const IMMIGRATION_CODE_REQUIRED_MESSAGE = 'Immigration Code is required'
const PERMIT_ISSUE_DATE_REQUIRED_MESSAGE = 'Permit Issue Date is required'
const PERMIT_EXPIRY_DATE_REQUIRED_MESSAGE = 'Permit Expiry Date is required'
const RESIDENCE_DATE_REQUIRED_MESSAGE = 'Residence Date is required'
const COVERAGE_CANCELLATION_DATE_REQUIRED_MESSAGE = 'Coverage Cancellation Date is required'
const HOME_ADDRESS_REQUIRED_MESSAGE = 'Home Address Line 1 is required'
const MAILING_ADDRESS_REQUIRED_MESSAGE = 'Mailing Address Line 1 is required'
const CITY_REQUIRED_MESSAGE = 'City is required'
const PROVINCE_REQUIRED_MESSAGE = 'Province is required'
const PRIOR_RESIDENCE_REQUIRED_MESSAGE = 'Prior Residence Code is required'
const POSTAL_CODE_REQUIRED_MESSAGE = 'Postal Code is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE = 'Group Member Number is invalid'
const INVALID_DEPARTMENT_NUMBER_VALIDATION_MESSAGE = 'Department Number is invalid'
const INVALID_POSTAL_CODE_VALIDATION_MESSAGE = 'Postal Code is invalid'
const PHONE_NUMBER_VALIDATION_MESSAGE = 'Only numbers 0 to 9 are valid. Phone Number must be entered as ten (10) numbers in length with no space or hyphen.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/enrollment/addStudyPermitHolderWithoutPHN'

fixture(`AddVisaResidentWithoutPHNPage Page`).disablePageCaching`Test AddVisaResidentWithoutPHNPage`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser)
  })
  .page(PAGE_TO_TEST)

test('Check required fields validation', async (t) => {
  await t
    .click(NameSearchPage.clearButton)
    .typeText(NameSearchPage.surnameInput, 'Test')
    .typeText(NameSearchPage.firstNameInput, 'Test')
    .typeText(NameSearchPage.dateOfBirthInput, '20001108')
    .click(NameSearchPage.radioButton)
    .click(NameSearchPage.submitButton)
    .wait(5000)
    // Given required fields aren't filled out

    // When I click the submit button
    // .click(AddVisaResidentWithoutPHNPage.clearButton)
    .click(AddVisaResidentWithoutPHNPage.submitButton)
    .wait(1000)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(0).textContent)
    .contains(GROUPNUMBER_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(1).textContent)
    .contains(IMMIGRATION_CODE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(2).textContent)
    .contains(PERMIT_ISSUE_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(3).textContent)
    .contains(PERMIT_EXPIRY_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(4).textContent)
    .contains(RESIDENCE_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(5).textContent)
    .contains(COVERAGE_CANCELLATION_DATE_REQUIRED_MESSAGE)
    // .expect(AddVisaResidentWithoutPHNPage.errorText.nth(7).textContent).contains(GENDER_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(6).textContent)
    .contains(HOME_ADDRESS_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(7).textContent)
    .contains(CITY_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(8).textContent)
    .contains(PROVINCE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(9).textContent)
    .contains(POSTAL_CODE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(10).textContent)
    .contains(PRIOR_RESIDENCE_REQUIRED_MESSAGE)
})

test('Check required fields validation for Mailing Address', async (t) => {
  await t
    .click(NameSearchPage.clearButton)
    .typeText(NameSearchPage.surnameInput, 'Test')
    .typeText(NameSearchPage.firstNameInput, 'Test')
    .typeText(NameSearchPage.dateOfBirthInput, '20001108')
    .click(NameSearchPage.radioButton)
    .click(NameSearchPage.submitButton)
    .wait(5000)
    // Given required fields aren't filled out

    // When I fill Mailing Address Line 2 and click the submit button
    .typeText(AddVisaResidentWithoutPHNPage.mailingAddress2Input, 'TEST ADDRESS LINE 2')
    .click(AddVisaResidentWithoutPHNPage.submitButton)
    .wait(1000)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(0).textContent)
    .contains(GROUPNUMBER_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(1).textContent)
    .contains(IMMIGRATION_CODE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(2).textContent)
    .contains(PERMIT_ISSUE_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(3).textContent)
    .contains(PERMIT_EXPIRY_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(4).textContent)
    .contains(RESIDENCE_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(5).textContent)
    .contains(COVERAGE_CANCELLATION_DATE_REQUIRED_MESSAGE)
    // .expect(AddVisaResidentWithoutPHNPage.errorText.nth(7).textContent).contains(GENDER_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(6).textContent)
    .contains(HOME_ADDRESS_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(7).textContent)
    .contains(CITY_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(8).textContent)
    .contains(PROVINCE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(9).textContent)
    .contains(POSTAL_CODE_REQUIRED_MESSAGE)

    //Although Mailing Address Line 1 is optional, if any other mailing address line is completed, it becomes required
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(10).textContent)
    .contains(MAILING_ADDRESS_REQUIRED_MESSAGE)

    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(11).textContent)
    .contains(PRIOR_RESIDENCE_REQUIRED_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    .typeText(NameSearchPage.surnameInput, 'Test')
    .typeText(NameSearchPage.firstNameInput, 'Test')
    .typeText(NameSearchPage.dateOfBirthInput, '20001108')
    .click(NameSearchPage.radioButton)
    .click(NameSearchPage.submitButton)
    .wait(1000)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(NO_SEARCH_RESULT)
    // Given the page is filled out correctly
    .typeText(AddVisaResidentWithoutPHNPage.groupNumberInput, '6337109')
    .click(AddVisaResidentWithoutPHNPage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Student Authorization'))
    .typeText(AddVisaResidentWithoutPHNPage.departmentNumberInput, '123456')
    .typeText(AddVisaResidentWithoutPHNPage.visaIssueDateInput, '20210101')
    .click(AddVisaResidentWithoutPHNPage.visaExpiryDateInput)
    .typeText(AddVisaResidentWithoutPHNPage.visaExpiryDateInput, '20221231')

    .typeText(AddVisaResidentWithoutPHNPage.residenceDateInput, '20210101')
    .typeText(AddVisaResidentWithoutPHNPage.coverageEffectiveDateInput, '20210101')
    .typeText(AddVisaResidentWithoutPHNPage.coverageCancellationDateInput, '20201231')
    .typeText(AddVisaResidentWithoutPHNPage.telephoneInput, '7802024022')
    .typeText(AddVisaResidentWithoutPHNPage.address1Input, 'Test 111 ST')
    .typeText(AddVisaResidentWithoutPHNPage.cityInput, 'VICTORIA')
    .click(AddVisaResidentWithoutPHNPage.provinceSelect)
    .click(provinceOption.withText('British Columbia'))
    .typeText(AddVisaResidentWithoutPHNPage.postalCodeInput, 'V8V8V8')
    .click(AddVisaResidentWithoutPHNPage.priorResidenceCodeInput)
    .click(priorResidenceCodeOption.withText('British Columbia'))

    // When I click the submit button
    .click(AddVisaResidentWithoutPHNPage.submitButton)
    .wait(5000)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
})

test('Check invalid field validation', async (t) => {
  await t
    .typeText(NameSearchPage.surnameInput, 'Test')
    .typeText(NameSearchPage.firstNameInput, 'Test')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    .click(NameSearchPage.submitButton)
    .wait(1000)
    // Given a Group Number entered with an invalid format
    .typeText(AddVisaResidentWithoutPHNPage.groupNumberInput, '9000444000')
    .typeText(AddVisaResidentWithoutPHNPage.groupMemberNumberInput, '9000444000')
    .typeText(AddVisaResidentWithoutPHNPage.departmentNumberInput, '9000444^^')
    .typeText(AddVisaResidentWithoutPHNPage.telephoneInput, '7807777')
    .typeText(AddVisaResidentWithoutPHNPage.address1Input, 'Test 111 ST is tooooooooooooooooooooooooooooo long')
    .typeText(AddVisaResidentWithoutPHNPage.address2Input, 'Test 111 ST @@@@@')
    .typeText(AddVisaResidentWithoutPHNPage.address3Input, '!@#$%^*(9')
    .typeText(AddVisaResidentWithoutPHNPage.cityInput, 'VICTORIA')
    .click(AddVisaResidentWithoutPHNPage.provinceSelect)
    .click(provinceOption.withText('British Columbia'))
    .typeText(AddVisaResidentWithoutPHNPage.coverageCancellationDateInput, '20221231')
    .typeText(AddVisaResidentWithoutPHNPage.postalCodeInput, 't6t6t6')
    .typeText(AddVisaResidentWithoutPHNPage.mailingAddress1Input, 'Test 111 ST is tooooooooooooooooooooooooooooo long')
    .typeText(AddVisaResidentWithoutPHNPage.mailingAddress2Input, 'Test 111 ST @@@@@')
    .typeText(AddVisaResidentWithoutPHNPage.mailingAddress3Input, '!@#$%^*(9')
    .typeText(AddVisaResidentWithoutPHNPage.mailingPostalCodeInput, 'tttttt')
    // When I click the submit button
    .click(AddVisaResidentWithoutPHNPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(2).textContent)
    .contains(INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(4).textContent)
    .contains(INVALID_DEPARTMENT_NUMBER_VALIDATION_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(7).textContent)
    .contains(PHONE_NUMBER_VALIDATION_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(8).textContent)
    .contains(INVALID_ADDRESS_LINE1_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(9).textContent)
    .contains(INVALID_ADDRESS_LINE2_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(10).textContent)
    .contains(INVALID_ADDRESS_LINE3_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(11).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(12).textContent)
    .contains(INVALID_ADDRESS_LINE1_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(13).textContent)
    .contains(INVALID_ADDRESS_LINE2_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(14).textContent)
    .contains(INVALID_ADDRESS_LINE3_MESSAGE)
    .expect(AddVisaResidentWithoutPHNPage.errorText.nth(15).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check clear button clears the form', async (t) => {
  await t
    .typeText(NameSearchPage.surnameInput, 'Test')
    .typeText(NameSearchPage.firstNameInput, 'Test')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    .click(NameSearchPage.submitButton)
    // Given the page is filled out correctly
    .typeText(AddVisaResidentWithoutPHNPage.groupNumberInput, '6337109')
    .click(AddVisaResidentWithoutPHNPage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Student Authorization'))
    .typeText(AddVisaResidentWithoutPHNPage.dateOfBirthInput, '20211108')
    .typeText(AddVisaResidentWithoutPHNPage.departmentNumberInput, '6337109')
    .typeText(AddVisaResidentWithoutPHNPage.visaIssueDateInput, '20210101')
    .typeText(AddVisaResidentWithoutPHNPage.visaExpiryDateInput, '20221231')
    .typeText(AddVisaResidentWithoutPHNPage.residenceDateInput, '20191108')
    .typeText(AddVisaResidentWithoutPHNPage.coverageCancellationDateInput, '20211231')
    .typeText(AddVisaResidentWithoutPHNPage.telephoneInput, '7802024022')
    .typeText(AddVisaResidentWithoutPHNPage.address1Input, 'Test 111 ST')
    .typeText(AddVisaResidentWithoutPHNPage.cityInput, 'VICTORIA')
    .click(AddVisaResidentWithoutPHNPage.provinceSelect)
    .click(provinceOption.withText('British Columbia'))
    .typeText(AddVisaResidentWithoutPHNPage.postalCodeInput, 'V8V8V8')
    .click(AddVisaResidentWithoutPHNPage.priorResidenceCodeInput)
    .click(priorResidenceCodeOption.withText('British Columbia'))

    // When I click the clear button
    .click(AddVisaResidentWithoutPHNPage.clearButton)
    // I expect the form to be cleared
    .expect(AddVisaResidentWithoutPHNPage.groupNumberInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.immigrationCodeSelect.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.visaIssueDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.visaExpiryDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.surnameInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.firstNameInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.radioButton.focused)
    .notOk()
    .expect(AddVisaResidentWithoutPHNPage.residenceDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.coverageEffectiveDateInput.value)
    .notEql('')
    .expect(AddVisaResidentWithoutPHNPage.coverageCancellationDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.cityInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.provinceSelect.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.postalCodeInput.value)
    .eql('')
    .expect(AddVisaResidentWithoutPHNPage.telephoneInput.value)
    .eql('')
})
