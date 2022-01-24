import AddGroupMember from '../../pages/groupmember/AddGroupMember'
import AlertPage from '../../pages/AlertPage'
import { SITE_UNDER_TEST } from '../../configuration'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupmember/AddGroupMember'

fixture(`AddGroupMember Page`)
.disablePageCaching `Test AddGroupMember`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
    .page(PAGE_TO_TEST)

test('Check required fields validation', async t => {
    await t    
        // When I click the submit button
        .click(AddGroupMember.submitButton)
        .wait(1000)
        // I expect an error message stating the page had errors and individual error messages for each required field        
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
        .expect(AddGroupMember.errorText.nth(0).textContent).contains(GROUP_NUMBER_REQUIRED_MESSAGE)
        .expect(AddGroupMember.errorText.nth(1).textContent).contains(PHN_REQUIRED_MESSAGE)
        .expect(AddGroupMember.errorText.nth(2).textContent).contains(HOME_ADDRESS_REQUIRED_MESSAGE)
        .expect(AddGroupMember.errorText.nth(3).textContent).contains(POSTAL_CODE_REQUIRED_MESSAGE)
       
});

test('Check properly filled form passes validation', async t => {
    await t
        // Given the page is filled out correctly
        .typeText(AddGroupMember.groupNumberInput, '6337109')      
        .typeText(AddGroupMember.phnInput, '9882807277')
        .typeText(AddGroupMember.coverageEffectiveDateInput, '20210401')        
        .typeText(AddGroupMember.address1Input, 'Test 111 ST')
        .typeText(AddGroupMember.postalCodeInput, 'V8V8V8')

        // When I click the submit button
		.click(AddGroupMember.submitButton)
        // I expect a success message
        .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
});

test('Check invalid field validation', async t => {
    await t
        // Given a Group Number entered with an invalid format
        .typeText(AddGroupMember.groupNumberInput, '9000444000')
        .typeText(AddGroupMember.phnInput, '9000444000')
        .typeText(AddGroupMember.telephoneInput, '7807777')
        // When I click the submit button
		.click(AddGroupMember.submitButton)
        // I expect an error message stating the page had errors and an individual error message for the PHN format
        .expect(AddGroupMember.errorText.nth(0).textContent).contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
        .expect(AddGroupMember.errorText.nth(1).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(AddGroupMember.errorText.nth(2).textContent).contains(PHONE_NUMBER_VALIDATION_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

});

test('Check click Add button adds dependent', async t => {
    await t
        // Given a Group Number entered with an invalid format
        .typeText(AddGroupMember.dependentPhn, '9882807277')
        //When I click Add buttton
		.click(AddGroupMember.addButton)
        // I expect entered phn is added in depenedent list
        .expect(AddGroupMember.dependentList.nth(1).innerText).eql('9882807277')
});

test('Check click Remove icon removes sselected dependent', async t => {
    await t
        // Given a Group Number entered with an invalid format
        .typeText(AddGroupMember.dependentPhn, '9882807277')
        //When I click Add buttton
		.click(AddGroupMember.addButton)
        .click(AddGroupMember.removeIcon)
        // I expect dependent phn is removed from depenedent list
        .expect(AddGroupMember.dependentList.length).eql(0);
});


test('Check clear button clears the form', async t => {	  
    await t
        // Given the page is filled out correctly
        .typeText(AddGroupMember.groupNumberInput, '6337109')
        .typeText(AddGroupMember.phnInput, '9882807277')
        .typeText(AddGroupMember.groupMemberNumberInput, '000001')
        .typeText(AddGroupMember.departmentNumberInput, '000002')
        .typeText(AddGroupMember.coverageEffectiveDateInput, '20210301')
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
        .expect(AddGroupMember.groupNumberInput.value).eql('')
        .expect(AddGroupMember.immigrationCodeSelect.value).eql('')
        .expect(AddGroupMember.visaIssueDateInput.value).eql('')
        .expect(AddGroupMember.visaExpiryDateInput.value).eql('')
        .expect(AddGroupMember.residenceDateInput.value).eql('')
        .expect(AddGroupMember.coverageEffectiveDateInput.value).eql('')
        .expect(AddGroupMember.coverageCancellationDateInput.value).eql('')
        .expect(AddGroupMember.cityInput.value).eql('')
        .expect(AddGroupMember.provinceSelect.value).eql('')
        .expect(AddGroupMember.postalCodeInput.value).eql('')
        .expect(AddGroupMember.telephoneInput.value).eql('')
});

