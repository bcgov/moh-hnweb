import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ReinstateOverAgeDependentPage from '../../pages/maintenance/ReinstateOverAgeDependentPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const DEPENDENT_PHN_REQUIRED_MESSAGE = "Dependent's PHN is required"
const INVALID_DEPENDENT_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const DEPENDENT_BIRTH_DATE_REQUIRED_MESSAGE = "Dependent's Birth Date is required"
const IS_STUDENT_REQUIRED_MESSAGE = 'Is this Dependent attending a Canadian Educational Institution? is required'
const STUDENT_END_DATE_REQUIRED_MESSAGE = 'The value is required'

const RAPID_ERROR_CANNOT_BE_REINSTATED = 'RPBS1054 DEPENDENT CANNOT BE REINSTATED AS A STUDENT THIS TIME. PLS CONTACT MSP.'
const RAPID_ERROR_NOT_ATTENDING_SCHOOL = 'RPBS0108 STUDENT NOT ATTENDING SCHOOL IN CANADA, MUST FORWARD DOCUMENTS TO MSP.'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/maintenance/ReinstateOverAgeDependent'

fixture(`ReinstateOverAgeDependent Page`).disablePageCaching`Test ReinstateOverAgeDependent`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (Group Number, PHN, Dependent's PHN, Dependent's Birth Date, Is Student)
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(2).textContent)
    .contains(DEPENDENT_PHN_REQUIRED_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(3).textContent)
    .contains(DEPENDENT_BIRTH_DATE_REQUIRED_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(4).textContent)
    .contains(IS_STUDENT_REQUIRED_MESSAGE)
})

test('Check student end date required validation', async (t) => {
  await t
    // Given other form fields filled out and Is Student is Yes but Student End Date is not filled in then
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '6243109')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '9397105575')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '9329090895')
    .typeText(ReinstateOverAgeDependentPage.dependentDateOfBirthInput, '19700101')
    .pressKey('tab')
    .click(ReinstateOverAgeDependentPage.isStudentRadioButton)
    .wait(1000)
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect an error message stating the page had errors and error message for student end date required
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(0).textContent)
    .contains(STUDENT_END_DATE_REQUIRED_MESSAGE)
})

test('Check invalid formats validation', async (t) => {
  await t
    // Given I enter Group Number,  PHN, Dependent's PHN with invalid formats
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '1234')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '9000448000')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '9000559000')
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the Group Number, PHN, Dependent's PHN
    .expect(ReinstateOverAgeDependentPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(2).textContent)
    .contains(INVALID_DEPENDENT_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check max length validation', async (t) => {
  await t
    // Given I enter Group Number, Group Member's PHN, Dependent's PHN with more than allowed length
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '63371099')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '93878074840')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '93878074841')
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the Group Number, PHN, Dependent's PHN
    .expect(ReinstateOverAgeDependentPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(2).textContent)
    .contains(INVALID_DEPENDENT_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check alphanumeric validation', async (t) => {
  await t
    // Given I enter Group Number,  PHN, Dependent's PHN with alphanumeric format
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '633710A')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '9bc7807484')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '9387807aa4')
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the Group Number, PHN, Dependent's PHN
    .expect(ReinstateOverAgeDependentPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(2).textContent)
    .contains(INVALID_DEPENDENT_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check special characters validation', async (t) => {
  await t
    // Given I enter Group Number,  PHN, Dependent's PHN with special chars
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '633710@')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '9$%7807484')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '9387807&&4')
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the Group Number, PHN, Dependent's PHN
    .expect(ReinstateOverAgeDependentPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(ReinstateOverAgeDependentPage.errorText.nth(2).textContent)
    .contains(INVALID_DEPENDENT_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '5028022')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '9387807484')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '9319079926')
    .typeText(ReinstateOverAgeDependentPage.dependentDateOfBirthInput, '20130329')
    .pressKey('tab')
    .click(ReinstateOverAgeDependentPage.isStudentRadioButton)
    .wait(1000)
    .typeText(ReinstateOverAgeDependentPage.studentEndDateInput, '202612')
    .pressKey('tab')
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect a response from RAPID
    .expect(AlertPage.alertBannerText.textContent)
    .contains(RAPID_ERROR_CANNOT_BE_REINSTATED)
})

test('Check properly filled form passes validation, non-student', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '6243109')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '9020198746')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '9329090895')
    .typeText(ReinstateOverAgeDependentPage.dependentDateOfBirthInput, '19700101')
    .pressKey('tab')
    .click(ReinstateOverAgeDependentPage.isStudentNoRadioButton)
    .wait(1000)
    // When I click the submit button
    .click(ReinstateOverAgeDependentPage.submitButton)
    // I expect a response from RAPID
    .expect(AlertPage.alertBannerText.textContent)
    .contains(RAPID_ERROR_NOT_ATTENDING_SCHOOL)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ReinstateOverAgeDependentPage.groupNumberInput, '6243109')
    .typeText(ReinstateOverAgeDependentPage.phnInput, '9020198746')
    .typeText(ReinstateOverAgeDependentPage.dependentPhnInput, '9329090895')
    .typeText(ReinstateOverAgeDependentPage.dependentDateOfBirthInput, '19700101')
    .pressKey('tab')
    .click(ReinstateOverAgeDependentPage.isStudentRadioButton)
    .wait(1000)
    .typeText(ReinstateOverAgeDependentPage.studentEndDateInput, '202201')
    .pressKey('tab')
    // When I click the Clear button
    .click(ReinstateOverAgeDependentPage.clearButton)
    // I expect the form to be cleared
    .expect(ReinstateOverAgeDependentPage.groupNumberInput.value)
    .eql('')
    .expect(ReinstateOverAgeDependentPage.phnInput.value)
    .eql('')
    .expect(ReinstateOverAgeDependentPage.dependentPhnInput.value)
    .eql('')
    .expect(ReinstateOverAgeDependentPage.isStudentRadioButton.value)
    .eql(undefined)
    .expect(ReinstateOverAgeDependentPage.studentEndDateInput.value)
    .eql(undefined)
})
