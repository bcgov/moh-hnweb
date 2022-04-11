import dayjs from 'dayjs'

import { OUTPUT_DATE_FORMAT } from '../../../../src/util/constants'
import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import AddVisaResidentWithPHNPage from '../../pages/enrollment/AddVisaResidentWithPHNPage'
import PersonDetails from '../../pages/enrollment/PersonDetailsPage'
import { regularAccUser } from '../../roles/roles'

const immigrationCodeOption = AddVisaResidentWithPHNPage.immigrationCodeSelect.find('option')
const provinceOption = AddVisaResidentWithPHNPage.provinceSelect.find('option')
const priorResidenceCodeOption = AddVisaResidentWithPHNPage.priorResidenceCodeInput.find('option')

const ERROR_MESSAGE = 'Please correct errors before submitting'
const SUCCESS_MESSAGE = 'HRPB246ETHIS PHN IS ALREADY ENROLLED IN MSP"'
const GROUPNUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const IMMIGRATION_CODE_REQUIRED_MESSAGE = 'Immigration Code is required'
const PERMIT_ISSUE_DATE_REQUIRED_MESSAGE = 'Permit Issue Date is required'
const PERMIT_EXPIRY_DATE_REQUIRED_MESSAGE = 'Permit Expiry Date is required'
const RESIDENCE_DATE_REQUIRED_MESSAGE = 'Residence Date is required'
const COVERAGE_CANCELLATION_DATE_REQUIRED_MESSAGE = 'Coverage Cancellation Date is required'
const HOME_ADDRESS_REQUIRED_MESSAGE = 'Home Address Line 1 is required'
const CITY_REQUIRED_MESSAGE = 'City is required'
const PROVINCE_REQUIRED_MESSAGE = 'Province is required'
const PRIOR_RESIDENCE_REQUIRED_MESSAGE = 'Prior Residence Code is required'
const POSTAL_CODE_REQUIRED_MESSAGE = 'Postal Code is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE = 'Group Member Number is invalid'
const INVALID_DEPARTMENT_NUMBER_VALIDATION_MESSAGE = 'Department Number is invalid'
const INVALID_POSTAL_CODE_VALIDATION_MESSAGE = 'Postal Code is invalid'
const PHONE_NUMBER_VALIDATION_MESSAGE = 'Only numbers 0 to 9 are valid. Phone Number must be entered as ten (10) numbers in length with no space or hyphen.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/enrollment/addStudyPermitHolderWithPHN'

fixture(`AddVisaResidentWithPHN Page`).disablePageCaching`Test AddVisaResidentWithPHN`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    .typeText(PersonDetails.phnInput, '9882807277')
    .click(PersonDetails.submitButton)
    .wait(5000)
    // Given required fields aren't filled out

    // When I click the submit button
    .click(AddVisaResidentWithPHNPage.submitButton)
    .wait(1000)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(0).textContent)
    .contains(GROUPNUMBER_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(1).textContent)
    .contains(IMMIGRATION_CODE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(2).textContent)
    .contains(PERMIT_ISSUE_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(3).textContent)
    .contains(PERMIT_EXPIRY_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(4).textContent)
    .contains(RESIDENCE_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(5).textContent)
    .contains(COVERAGE_CANCELLATION_DATE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(6).textContent)
    .contains(HOME_ADDRESS_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(7).textContent)
    .contains(CITY_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(8).textContent)
    .contains(PROVINCE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(9).textContent)
    .contains(POSTAL_CODE_REQUIRED_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(10).textContent)
    .contains(PRIOR_RESIDENCE_REQUIRED_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    .typeText(PersonDetails.phnInput, '9882807277')
    .click(PersonDetails.submitButton)
    .wait(5000)
    // Given the page is filled out correctly
    .typeText(AddVisaResidentWithPHNPage.groupNumberInput, '6337109')
    .click(AddVisaResidentWithPHNPage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Employment Authorization'))
    .typeText(AddVisaResidentWithPHNPage.departmentNumberInput, '123456')
    .typeText(AddVisaResidentWithPHNPage.visaIssueDateInput, '20210101')
    .typeText(AddVisaResidentWithPHNPage.visaExpiryDateInput, '20221231')
    .typeText(AddVisaResidentWithPHNPage.residenceDateInput, '20191108')
    .typeText(AddVisaResidentWithPHNPage.coverageEffectiveDateInput, '20210401')
    .typeText(AddVisaResidentWithPHNPage.coverageCancellationDateInput, '20221231')
    .typeText(AddVisaResidentWithPHNPage.telephoneInput, '7802024022')
    .typeText(AddVisaResidentWithPHNPage.address1Input, 'Test 111 ST')
    .typeText(AddVisaResidentWithPHNPage.cityInput, 'VICTORIA')
    .click(AddVisaResidentWithPHNPage.provinceSelect)
    .click(provinceOption.withText('British Columbia'))
    .typeText(AddVisaResidentWithPHNPage.postalCodeInput, 'V8V8V8')
    .click(AddVisaResidentWithPHNPage.priorResidenceCodeInput)
    .click(priorResidenceCodeOption.withText('Alberta'))

    // When I click the submit button
    .click(AddVisaResidentWithPHNPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
})

test('Check invalid field validation', async (t) => {
  await t
    .typeText(PersonDetails.phnInput, '9882807277')
    .click(PersonDetails.submitButton)
    .wait(1000)
    // Given a Group Number entered with an invalid format
    .typeText(AddVisaResidentWithPHNPage.groupNumberInput, '9000444000')
    .typeText(AddVisaResidentWithPHNPage.groupMemberNumberInput, '9000444000')
    .typeText(AddVisaResidentWithPHNPage.departmentNumberInput, '9000444^^')
    .typeText(AddVisaResidentWithPHNPage.telephoneInput, '7807777')
    .typeText(AddVisaResidentWithPHNPage.address1Input, 'Test 111 ST')
    .typeText(AddVisaResidentWithPHNPage.cityInput, 'VICTORIA')
    .click(AddVisaResidentWithPHNPage.provinceSelect)
    .click(provinceOption.withText('British Columbia'))
    .typeText(AddVisaResidentWithPHNPage.postalCodeInput, 'T8T8T8')
    .typeText(AddVisaResidentWithPHNPage.mailingPostalCodeInput, 'tttttt')
    // When I click the submit button
    .click(AddVisaResidentWithPHNPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AddVisaResidentWithPHNPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(2).textContent)
    .contains(INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(4).textContent)
    .contains(INVALID_DEPARTMENT_NUMBER_VALIDATION_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(7).textContent)
    .contains(PHONE_NUMBER_VALIDATION_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(9).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AddVisaResidentWithPHNPage.errorText.nth(10).textContent)
    .contains(INVALID_POSTAL_CODE_VALIDATION_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check clear button clears the form', async (t) => {
  await t
    .typeText(PersonDetails.phnInput, '9882807277')
    .click(PersonDetails.submitButton)
    // Given the page is filled out correctly
    .typeText(AddVisaResidentWithPHNPage.groupNumberInput, '6337109')
    .click(AddVisaResidentWithPHNPage.immigrationCodeSelect)
    .click(immigrationCodeOption.withText('Student Authorization'))
    .typeText(AddVisaResidentWithPHNPage.departmentNumberInput, '6337109')
    .typeText(AddVisaResidentWithPHNPage.visaIssueDateInput, '20210101')
    .typeText(AddVisaResidentWithPHNPage.visaExpiryDateInput, '20221231')
    .typeText(AddVisaResidentWithPHNPage.residenceDateInput, '20191108')
    .typeText(AddVisaResidentWithPHNPage.coverageEffectiveDateInput, '20210301')
    .typeText(AddVisaResidentWithPHNPage.coverageCancellationDateInput, '20211231')
    .typeText(AddVisaResidentWithPHNPage.telephoneInput, '7802024022')
    .typeText(AddVisaResidentWithPHNPage.address1Input, 'Test 111 ST')
    .typeText(AddVisaResidentWithPHNPage.cityInput, 'VICTORIA')
    .click(AddVisaResidentWithPHNPage.provinceSelect)
    .click(provinceOption.withText('British Columbia'))
    .typeText(AddVisaResidentWithPHNPage.postalCodeInput, 'V8V8V8')
    .click(AddVisaResidentWithPHNPage.priorResidenceCodeInput)
    .click(priorResidenceCodeOption.withText('British Columbia'))

    // When I click the clear button
    .click(AddVisaResidentWithPHNPage.clearButton)
    // I expect the form to be cleared
    .expect(AddVisaResidentWithPHNPage.groupNumberInput.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.immigrationCodeSelect.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.visaIssueDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.visaExpiryDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.residenceDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.coverageEffectiveDateInput.value)
    .eql(dayjs().startOf('month').format(OUTPUT_DATE_FORMAT))
    .expect(AddVisaResidentWithPHNPage.coverageCancellationDateInput.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.cityInput.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.provinceSelect.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.postalCodeInput.value)
    .eql('')
    .expect(AddVisaResidentWithPHNPage.telephoneInput.value)
    .eql('')
})
