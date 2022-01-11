import AddVisaResidentWithPHNPage from '../pages/enrollement/AddVisaResidentWithPHNPage';
import AlertPage from '../pages/AlertPage';
import PersonDetailsPage from '../pages/enrollement/PersonDetailsPage';
import { SITE_UNDER_TEST } from '../configuration';
import { regularAccUser } from '../roles/roles';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid';


const PAGE_TO_TEST = SITE_UNDER_TEST + '/coverage/enrollment/addStudyPermitHolderWithPHN'

fixture(`PersonDetails Page`)
.disablePageCaching `Test PersonDetails for AddVisaResident`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST);

test('Check required fields validation', async t => {
    await t
        // Given required fields aren't filled out (phn)       
        // When I click the submit button
        .click(PersonDetailsPage.submitButton)
        // I expect an error message stating the page had errors and individual error messages for each required field
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
        .expect(PersonDetailsPage.errorText.nth(0).textContent).contains(PHN_REQUIRED_MESSAGE)

});

test('Check invalid phn format validation', async t => {
    await t
        // Given a PHN entered with an invalid format
        .typeText(PersonDetailsPage.phnInput, '9000448000')
        // When I click the submit button
		.click(PersonDetailsPage.submitButton)
        // I expect an error message stating the page had errors and an individual error message for the PHN format
        .expect(PersonDetailsPage.errorText.nth(0).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});


test('Check properly filled form passes validation', async t => {
	await t
        // Given I have a form filled out with data
        .typeText(PersonDetailsPage.phnInput, '9882807277')
        .wait(1000)
        // When I click the submit button
		.click(PersonDetailsPage.submitButton)
        // I expect the AddVisaResident page to be loaded
        .expect(AddVisaResidentWithPHNPage.groupNumberInput.exists).ok();
});

test('Check clear button clears the form', async t => {
	await t
        // Given I have a form filled out with data
        .typeText(PersonDetailsPage.phnInput, '9000448000')
        // When I click the cancel button
		.click(PersonDetailsPage.clearButton)
        // I expect the form to be cleared
        .expect(PersonDetailsPage.phnInput.value).eql('')
});

