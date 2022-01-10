import AlertPage from '../../pages/AlertPage';
import { SITE_UNDER_TEST } from '../../configuration';
import UpdateGroupMember from '../../pages/groupmember/UpdateGroupMember';
import { regularAccUser } from '../../roles/roles';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid';
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid';
const GROUP_NUMBER_DEPARTMENT_REQUIRED_MESSAGE = 'The value is required'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/UpdateGroupMember'

fixture(`UpdateGroupMember Page`)
.disablePageCaching `Test UpdateGroupMember`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST);

test('Check required fields validation', async t => {
    await t
        // Given required fields aren't filled out (phn, group member, group member number, department)       
        // When I click the submit button
        .click(UpdateGroupMember.submitButton)
        // I expect an error message stating the page had errors and individual error messages for each required field
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)  
        .expect(UpdateGroupMember.errorText.nth(0).textContent).contains(GROUP_NUMBER_REQUIRED_MESSAGE)
        .expect(UpdateGroupMember.errorText.nth(1).textContent).contains(PHN_REQUIRED_MESSAGE)
        .expect(UpdateGroupMember.errorText.nth(2).textContent).contains(GROUP_NUMBER_DEPARTMENT_REQUIRED_MESSAGE)
        .expect(UpdateGroupMember.errorText.nth(3).textContent).contains(GROUP_NUMBER_DEPARTMENT_REQUIRED_MESSAGE)

});

test('Check invalid phn format validation', async t => {
    await t
        // Given a PHN entered with an invalid format
        .typeText(UpdateGroupMember.phnInput, '9000448000')
        .typeText(UpdateGroupMember.groupMemberInput, '123')
        // When I click the submit button
		.click(UpdateGroupMember.submitButton)
        // I expect an error message stating the page had errors and an individual error message for the PHN and Group numberformat
        .expect(UpdateGroupMember.errorText.nth(0).textContent).contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
        .expect(UpdateGroupMember.errorText.nth(1).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});


test('Check properly filled form passes validation', async t => {
	await t
        // Given I have a form filled out with data
        .typeText(UpdateGroupMember.phnInput, '9882807277')
        .typeText(UpdateGroupMember.groupMemberInput,'6337109')
        .typeText(UpdateGroupMember.groupMemberNumberInput,'123456')
        .typeText(UpdateGroupMember.departmentNumberInput,'123456')
        .wait(1000)
        // When I click the submit button
		.click(UpdateGroupMember.submitButton)
        // I expect the message from downstream
        .expect(UpdateGroupMember.phnInput.exists).ok();
});


test('Check validation passes if department is not filled', async t => {
    await t
        // Given I have a form filled out with data
        .typeText(UpdateGroupMember.phnInput, '9882807277')
        .typeText(UpdateGroupMember.groupMemberInput,'6337109')
        .typeText(UpdateGroupMember.groupMemberNumberInput,'123456') 
        .wait(1000)
        // When I click the submit button
        .click(UpdateGroupMember.submitButton)
        // I expect the message from downstream
        .expect(UpdateGroupMember.phnInput.exists).ok();

});


test('Check validation passes if Group Member number is not filled', async t => {
    await t
        // Given I have a form filled out with data
        .typeText(UpdateGroupMember.phnInput, '9882807277')
        .typeText(UpdateGroupMember.groupMemberInput,'6337109')
        .typeText(UpdateGroupMember.departmentNumberInput,'123456') 
        .wait(1000)
        // When I click the submit button
        .click(UpdateGroupMember.submitButton)
        // I expect the message from downstream
        .expect(UpdateGroupMember.phnInput.exists).ok();
});

test('Check clear button clears the form', async t => {
	await t
        // Given I have a form filled out with data
        .typeText(UpdateGroupMember.phnInput, '9882807277')
        .typeText(UpdateGroupMember.groupMemberInput,'6337109')
        .typeText(UpdateGroupMember.groupMemberNumberInput,'123456')
        .typeText(UpdateGroupMember.departmentNumberInput,'123456')
        // When I click the cancel button
		.click(UpdateGroupMember.clearButton)
        // I expect the form to be cleared
        .expect(UpdateGroupMember.phnInput.value).eql('')
        .expect(UpdateGroupMember.groupMemberInput.value).eql('')
        .expect(UpdateGroupMember.groupMemberNumberInput.value).eql('')
        .expect(UpdateGroupMember.departmentNumberInput.value).eql('')
});