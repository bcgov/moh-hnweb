import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import AddDependentPage from '../../pages/groupmember/AddDependentPage'
import { regularAccUser } from '../../roles/roles'

const relationshipOption = AddDependentPage.relationshipSelect.find('option')

const ERROR_MESSAGE = 'Please correct errors before submitting'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const GROUP_MEMBER_PHN_REQUIRED_MESSAGE = "Group Member's PHN is required"
const INVALID_GROUP_MEMBER_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const DEPENDENT_PHN_REQUIRED_MESSAGE = "Dependent's PHN is required"
const INVALID_DEPENDENT_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const RELATIONSHIP_REQUIRED_MESSAGE = 'Relationship is required'
const IS_STUDENT_REQUIRED_MESSAGE = 'Is this Dependent attending a Canadian Educational Institution? is required'
const STUDENT_END_DATE_REQUIRED_MESSAGE = 'The value is required'
const RAPID_RESPONSE = 'RPBS9145 PHN NOT FOUND'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/AddDependent'

fixture(`AddDependent Page`).disablePageCaching`Test AddDependent`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (Group Number, Coverage Effective Date, Group Member's PHN, Dependent's PHN, Relationship, Is Student)
    // When I click the submit button
    .click(AddDependentPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AddDependentPage.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(AddDependentPage.errorText.nth(1).textContent)
    .contains(GROUP_MEMBER_PHN_REQUIRED_MESSAGE)
    .expect(AddDependentPage.errorText.nth(2).textContent)
    .contains(DEPENDENT_PHN_REQUIRED_MESSAGE)
    .expect(AddDependentPage.errorText.nth(3).textContent)
    .contains(RELATIONSHIP_REQUIRED_MESSAGE)
    .expect(AddDependentPage.errorText.nth(4).textContent)
    .contains(IS_STUDENT_REQUIRED_MESSAGE)
})

test('Check student end date required validation', async (t) => {
  await t
    // Given other form fields filled out and Is Student is Yes but Student End Date is not filled in then
    .typeText(AddDependentPage.groupNumberInput, '6243109')
    .typeText(AddDependentPage.coverageEffectiveDateInput, '202112')
    .pressKey('tab')
    .typeText(AddDependentPage.phnInput, '9397105575')
    .typeText(AddDependentPage.dependentPhnInput, '9329090895')
    .click(AddDependentPage.relationshipSelect)
    .click(relationshipOption.withText('Spouse'))
    .click(AddDependentPage.isStudentRadioButton)
    .wait(1000)
    // When I click the submit button
    .click(AddDependentPage.submitButton)
    // I expect an error message stating the page had errors and error message for student end date required
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AddDependentPage.errorText.nth(0).textContent)
    .contains(STUDENT_END_DATE_REQUIRED_MESSAGE)
})

test('Check invalid formats validation', async (t) => {
  await t
    // Given I enter Group Number, Group Member's PHN, Dependent's PHN with invalid formats
    .typeText(AddDependentPage.groupNumberInput, '1234')
    .typeText(AddDependentPage.phnInput, '9000448000')
    .typeText(AddDependentPage.dependentPhnInput, '9000559000')
    // When I click the submit button
    .click(AddDependentPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the Group Number, Group Member's PHN, Dependent's PHN
    .expect(AddDependentPage.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(AddDependentPage.errorText.nth(1).textContent)
    .contains(INVALID_GROUP_MEMBER_PHN_ERROR_MESSAGE)
    .expect(AddDependentPage.errorText.nth(2).textContent)
    .contains(INVALID_DEPENDENT_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(AddDependentPage.groupNumberInput, '6243109')
    .typeText(AddDependentPage.coverageEffectiveDateInput, '202112')
    .pressKey('tab')
    .typeText(AddDependentPage.phnInput, '9397105575')
    .typeText(AddDependentPage.dependentPhnInput, '9329090895')
    .click(AddDependentPage.relationshipSelect)
    .click(relationshipOption.withText('Spouse'))
    .click(AddDependentPage.isStudentRadioButton)
    .wait(1000)
    .typeText(AddDependentPage.studentEndDateInput, '20221231')
    .pressKey('tab')
    // When I click the submit button
    .click(AddDependentPage.submitButton)
    // I expect a response from RAPID
    .expect(AlertPage.alertBannerText.textContent)
    .contains(RAPID_RESPONSE)
})

test('Check properly filled form passes validation, non student', async (t) => {
  await t
    // Given I have a form filled out with data and Is Student is No
    .typeText(AddDependentPage.groupNumberInput, '6243109')
    .typeText(AddDependentPage.coverageEffectiveDateInput, '202112')
    .pressKey('tab')
    .typeText(AddDependentPage.phnInput, '9397105575')
    .typeText(AddDependentPage.dependentPhnInput, '9329090895')
    .click(AddDependentPage.relationshipSelect)
    .click(relationshipOption.withText('Spouse'))
    .click(AddDependentPage.isStudentNoRadioButton)
    .wait(1000)
    // And I don't fill in Student End Date
    // When I click the submit button
    .click(AddDependentPage.submitButton)
    // I expect a response from RAPID
    .expect(AlertPage.alertBannerText.textContent)
    .contains(RAPID_RESPONSE)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(AddDependentPage.groupNumberInput, '6243109')
    .typeText(AddDependentPage.coverageEffectiveDateInput, '202112')
    .pressKey('tab')
    .typeText(AddDependentPage.phnInput, '9397105575')
    .typeText(AddDependentPage.dependentPhnInput, '9329090895')
    .click(AddDependentPage.relationshipSelect)
    .click(relationshipOption.withText('Spouse'))
    .click(AddDependentPage.isStudentRadioButton)
    .wait(1000)
    .typeText(AddDependentPage.studentEndDateInput, '20221231')
    .pressKey('tab')
    // When I click the Clear button
    .click(AddDependentPage.clearButton)
    // I expect the form to be cleared
    .expect(AddDependentPage.groupNumberInput.value)
    .eql('')
    //Note, Coverage Effective Date has a default value so is not checked here
    .expect(AddDependentPage.phnInput.value)
    .eql('')
    .expect(AddDependentPage.dependentPhnInput.value)
    .eql('')
    .expect(AddDependentPage.relationshipSelect.value)
    .eql('')
    .expect(AddDependentPage.isStudentRadioButton.value)
    .eql(undefined)
    .expect(AddDependentPage.studentEndDateInput.value)
    .eql(undefined)
})
