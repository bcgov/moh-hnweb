import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import AddVisaResidentWithoutPHNPage from '../../pages/enrollment/AddVisaResidentWithoutPHNPage'
import NameSearchPage from '../../pages/enrollment/NameSearchPage'
import { regularAccUser } from '../../roles/roles'

const MAX_RESULTS_MESSAGE = 'BCHCIM.FC.1.0017  The maximum number of results were returned, and more may be available. Please refine your search criteria and try again.'
const NO_RESULTS_MESSAGE = 'BCHCIM.FC.0.0018  No results were returned. Please refine your search criteria, and try again.'
const ERROR_MESSAGE = 'Please correct errors before submitting'
const SURNAME_REQUIRED_MESSAGE = 'Surname is required'
const FIRSTNAME_REQUIRED_MESSAGE = 'First Name is required'
const DOB_REQUIRED_MESSAGE = 'Date of Birth is required'
const GENDER_REQUIRED_MESSAGE = 'Gender is required'
const INVALID_FIRST_NAME_MESSAGE = 'First Name is invalid'
const INVALID_SECOND_NAME_MESSAGE = 'Second Name is invalid'
const INVALID_SURNMAE_NAME_MESSAGE = 'Surname is invalid'
const MAX_LENGTH_NAME_MESSAGE = 'The maximum length allowed is 15'
const MAX_LENGTH_SURNAME_MESSAGE = 'The maximum length allowed is 35'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/enrollment/addStudyPermitHolderWithoutPHN'

fixture(`NameSearch Page`).disablePageCaching`Test Name Search for Add Study Permit Holder without PHN`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (surname,first name, date of birth, gender)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(NameSearchPage.errorText.nth(0).textContent)
    .contains(SURNAME_REQUIRED_MESSAGE)
    .expect(NameSearchPage.errorText.nth(1).textContent)
    .contains(FIRSTNAME_REQUIRED_MESSAGE)
    .expect(NameSearchPage.errorText.nth(2).textContent)
    .contains(DOB_REQUIRED_MESSAGE)
    .expect(NameSearchPage.errorText.nth(3).textContent)
    .contains(GENDER_REQUIRED_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'Squarepants')
    .typeText(NameSearchPage.firstNameInput, 'Spongebob')
    .typeText(NameSearchPage.dateOfBirthInput, '19800101')
    .click(NameSearchPage.radioButton)
    .wait(1000)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    // I expect the search result page to be loaded
    .expect(NameSearchPage.addButton.exists)
    .ok()
})

test('Check alpha validation for Name', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'Test123')
    .typeText(NameSearchPage.firstNameInput, 'Test123')
    .typeText(NameSearchPage.secondNameInput, 'Test123')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(NameSearchPage.errorText.nth(0).textContent)
    .contains(INVALID_SURNMAE_NAME_MESSAGE)
    .expect(NameSearchPage.errorText.nth(1).textContent)
    .contains(INVALID_FIRST_NAME_MESSAGE)
    .expect(NameSearchPage.errorText.nth(2).textContent)
    .contains(INVALID_SECOND_NAME_MESSAGE)
})

test('Check length validation for Name', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'MySurnameistooooooooooooooooooooooooooooooooooooooooooooLong')
    .typeText(NameSearchPage.firstNameInput, 'MyFirstnameistoooooooooooooooooLong')
    .typeText(NameSearchPage.secondNameInput, 'MySecondnameistoooooooooooooooooLong')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each name field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(NameSearchPage.errorText.nth(0).textContent)
    .contains(MAX_LENGTH_SURNAME_MESSAGE)
    .expect(NameSearchPage.errorText.nth(1).textContent)
    .contains(MAX_LENGTH_NAME_MESSAGE)
    .expect(NameSearchPage.errorText.nth(2).textContent)
    .contains(MAX_LENGTH_NAME_MESSAGE)
})

test('Check Name Seach Result contains warning message when max results reached', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'dumpty')
    .typeText(NameSearchPage.firstNameInput, 'humpty')
    .typeText(NameSearchPage.secondNameInput, 'eggo')
    .typeText(NameSearchPage.dateOfBirthInput, '19700101')
    //When gender code 'Unknown' is selected
    .click(NameSearchPage.radioButtonUnknown)
    .wait(1000)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    // I expect the Name Search Result page with Warning message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(MAX_RESULTS_MESSAGE)
})

test('Check warning message is shown when no matching records found', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'Test')
    .typeText(NameSearchPage.firstNameInput, 'Test')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    .wait(1000)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    .wait(5000)
    // I expect the warning message to display
    .expect(AlertPage.alertBannerText.textContent)
    .contains(NO_RESULTS_MESSAGE)
    // And the Create New PHN BUtton to be available
    .expect(NameSearchPage.createNewPHNButton.exists)
    .ok()
})

test('Check Search Result page loads when matching records found', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'dumpty')
    .typeText(NameSearchPage.firstNameInput, 'humpty')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    .wait(1000)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    .wait(5000)
    // I expect the search result page to be loaded
    .expect(NameSearchPage.addButton.exists)
    .ok()
    .expect(NameSearchPage.numberOfMatchesText.exists)
    .ok()
    .expect(NameSearchPage.addButton.count)
    .eql(10)
})

test('Check Add button navigates to Add Visa Resident page for selected record', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'dumpty')
    .typeText(NameSearchPage.firstNameInput, 'humpty')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    .wait(1000)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    // I expect the search result page to be loaded
    .click(NameSearchPage.addButton)
    //I expect the Add Visa Resident page to be loaded
    .expect(AddVisaResidentWithoutPHNPage.groupNumberInput.exists)
    .ok()
    //I expect below fields not not be present for selected record
    .expect(NameSearchPage.surnameInput.exists)
    .notOk()
    .expect(NameSearchPage.firstNameInput.exists)
    .notOk()
    .expect(NameSearchPage.radioButton.exists)
    .notOk()
    .expect(NameSearchPage.dateOfBirthInput.exists)
    .notOk()
})

test('Check prepopulated fields for new record', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'TestSN')
    .typeText(NameSearchPage.firstNameInput, 'TestFN')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .click(NameSearchPage.radioButton)
    .wait(1000)
    // When I click the submit button
    .click(NameSearchPage.submitButton)
    // And click the Create New PHN button
    .click(NameSearchPage.createNewPHNButton)
    // I expect the Add Visa Study Permit Holder page to be loaded
    .expect(AddVisaResidentWithoutPHNPage.firstNameInput.value)
    .eql('TestFN')
    .expect(AddVisaResidentWithoutPHNPage.surnameInput.value)
    .eql('TestSN')
    .expect(AddVisaResidentWithoutPHNPage.radioButton.exists)
    .ok()
    .expect(AddVisaResidentWithoutPHNPage.dateOfBirthInput.value)
    .eql('20211108')
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(NameSearchPage.surnameInput, 'Test Surname')
    .typeText(NameSearchPage.firstNameInput, 'Test First Name')
    .typeText(NameSearchPage.dateOfBirthInput, '20211108')
    .typeText(NameSearchPage.secondNameInput, 'Test Second Name')
    // When I click the clear button
    .click(NameSearchPage.clearButton)
    // I expect the form to be cleared
    .expect(NameSearchPage.surnameInput.value)
    .eql('')
    .expect(NameSearchPage.firstNameInput.value)
    .eql('')
    .expect(NameSearchPage.secondNameInput.value)
    .eql('')
    .expect(NameSearchPage.dateOfBirthInput.value)
    .eql('')
})
