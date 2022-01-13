import AddVisaResidentWithoutPHNPage from '../pages/enrollement/AddVisaResidentWithoutPHNPage';
import AlertPage from '../pages/AlertPage';
import NameSearchPage from '../pages/enrollement/NameSearchPage';
import { SITE_UNDER_TEST } from '../configuration';
import { regularAccUser } from '../roles/roles';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const SURNAME_REQUIRED_MESSAGE = 'Surname is required'
const FIRSTNAME_REQUIRED_MESSAGE = 'First Name is required'
const DOB_REQUIRED_MESSAGE = 'Date of Birth is required';
const GENDER_REQUIRED_MESSAGE = 'Gender is required';

const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/enrollment/addStudyPermitHolderWithoutPHN'

fixture(`NameSearch Page`)
.disablePageCaching `Test Name Search for AddVisaResident`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST);

test('Check required fields validation', async t => {
    await t
        // Given required fields aren't filled out (surname,first name, date of birth, gender)       
        // When I click the submit button
        .click(NameSearchPage.submitButton)
        // I expect an error message stating the page had errors and individual error messages for each required field
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
        .expect(NameSearchPage.errorText.nth(0).textContent).contains(SURNAME_REQUIRED_MESSAGE)
        .expect(NameSearchPage.errorText.nth(1).textContent).contains(FIRSTNAME_REQUIRED_MESSAGE)
        .expect(NameSearchPage.errorText.nth(2).textContent).contains(DOB_REQUIRED_MESSAGE)
        .expect(NameSearchPage.errorText.nth(3).textContent).contains(GENDER_REQUIRED_MESSAGE)

});

test('Check properly filled form passes validation', async t => {
	await t
        // Given I have a form filled out with data
        .typeText(NameSearchPage.surnameInput, 'Test')
        .typeText(NameSearchPage.firstNameInput, 'Test')
        .typeText(NameSearchPage.dateOfBirthInput, '20211108')
        .click(NameSearchPage.radioButton)
        .wait(1000)
        // When I click the submit button
		.click(NameSearchPage.submitButton)
        // I expect the AddVisaResident page to be loaded
        .expect(AddVisaResidentWithoutPHNPage.groupNumberInput.exists).ok();
});

test('Check Add Visa Resident is loaded when no matching records found', async t => {
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
        // I expect the Visasident page to be loaded
        .expect(AddVisaResidentWithoutPHNPage.groupNumberInput.exists).ok();
});

test('Check Search Result page loads when matching records found', async t => {
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
        .expect(NameSearchPage.addButton.exists).ok()
        .expect(NameSearchPage.numberOfMatchesText.exists).ok()
        .expect(NameSearchPage.addButton.count).eql(5);
});

test('Check Add button navigates t0 Add Visa Resident page for selected record', async t => {
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
        .expect(AddVisaResidentWithoutPHNPage.groupNumberInput.exists).ok()
        //I expect below fields not not be present for selected record
        .expect(NameSearchPage.surnameInput.exists).notOk()
        .expect(NameSearchPage.firstNameInput.exists).notOk()
        .expect(NameSearchPage.radioButton.exists).notOk()
        .expect(NameSearchPage.dateOfBirthInput.exists).notOk();
});

test('Check prepopulated fields for new record', async t => {
	await t
        // Given I have a form filled out with data
        .typeText(NameSearchPage.surnameInput, 'TestSN')
        .typeText(NameSearchPage.firstNameInput, 'TestFN')
        .typeText(NameSearchPage.dateOfBirthInput, '20211108')
        .click(NameSearchPage.radioButton)
        .wait(1000)
        // When I click the submit button
		.click(NameSearchPage.submitButton)
        //I expect the Add Visa Resident page to be loaded
        .expect(AddVisaResidentWithoutPHNPage.firstNameInput.value).eql('TestFN')
        .expect(AddVisaResidentWithoutPHNPage.surnameInput.value).eql('TestSN')
        .expect(AddVisaResidentWithoutPHNPage.radioButton.exists).ok()
        .expect(AddVisaResidentWithoutPHNPage.dateOfBirthInput.value).eql('20211108')
});

test('Check clear button clears the form', async t => {
	await t
         // Given I have a form filled out with data
         .typeText(NameSearchPage.surnameInput, 'Test Surname')
         .typeText(NameSearchPage.firstNameInput, 'Test First Name')
         .typeText(NameSearchPage.dateOfBirthInput, '20211108')
         .typeText(NameSearchPage.secondNameInput, 'Test Second Name')
        // When I click the clear button
		 .click(NameSearchPage.clearButton)
        // I expect the form to be cleared
         .expect(NameSearchPage.surnameInput.value).eql('')
         .expect(NameSearchPage.firstNameInput.value).eql('')
         .expect(NameSearchPage.secondNameInput.value).eql('')
         .expect(NameSearchPage.dateOfBirthInput.value).eql('')
});
