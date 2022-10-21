import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import UpdateNumberAndDept from '../../pages/groupmember/UpdateNumberAndDept'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const GROUP_NUMBER_REQUIRED_MESSAGE = 'Group Number is required'
const INVALID_GROUP_NUMBER_ERROR_MESSAGE = 'Group Number is invalid'
const INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE = 'Group Member Number is invalid'
const INVALID_DEPARTMENT_NUMBER_ERROR_MESSAGE = 'Department Number is invalid'
const GROUP_NUMBER_DEPARTMENT_REQUIRED_MESSAGE = 'Group Member Number and/or Department Number is required'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/groupMember/updateNumberAndDept'

fixture(`UpdateNumberAndDept Page`).disablePageCaching`Test UpdateNumberAndDept`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (phn, group member, group member number, department)
    // When I click the submit button
    .click(UpdateNumberAndDept.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent)
    .contains(GROUP_NUMBER_DEPARTMENT_REQUIRED_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(0).textContent)
    .contains(GROUP_NUMBER_REQUIRED_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(1).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
})

test('Check invalid PHN, format GroupMember validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(UpdateNumberAndDept.phnInput, '900044800')
    .typeText(UpdateNumberAndDept.groupMemberInput, '12345')
    .typeText(UpdateNumberAndDept.groupMemberNumberInput, '123')
    .typeText(UpdateNumberAndDept.departmentNumberInput, '123')
    // When I click the submit button
    .click(UpdateNumberAndDept.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN and Group numberformat
    .expect(UpdateNumberAndDept.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)

    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check invalid character validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(UpdateNumberAndDept.phnInput, '90004@@@@')
    .typeText(UpdateNumberAndDept.groupMemberInput, '123%^&*')
    .typeText(UpdateNumberAndDept.groupMemberNumberInput, '123^^^')
    .typeText(UpdateNumberAndDept.departmentNumberInput, '123@#@@@@@@')
    // When I click the submit button
    .click(UpdateNumberAndDept.submitButton)
    // I expect an error message stating the page had errors and an individual error message for all the input fields having invalid characters
    .expect(UpdateNumberAndDept.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(2).textContent)
    .contains(INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(3).textContent)
    .contains(INVALID_DEPARTMENT_NUMBER_ERROR_MESSAGE)

    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check invalid field length validation', async (t) => {
  await t
    // Given input field value exceeds allowed length
    .typeText(UpdateNumberAndDept.phnInput, '90004480000')
    .typeText(UpdateNumberAndDept.groupMemberInput, '60000000003109')
    .typeText(UpdateNumberAndDept.groupMemberNumberInput, '123111111111')
    .typeText(UpdateNumberAndDept.departmentNumberInput, '1231111111')
    // When I click the submit button
    .click(UpdateNumberAndDept.submitButton)
    // I expect an error message stating the page had errors and an individual error message for all the input fields which exceeded length
    .expect(UpdateNumberAndDept.errorText.nth(0).textContent)
    .contains(INVALID_GROUP_NUMBER_ERROR_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(1).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(2).textContent)
    .contains(INVALID_GROUP_MEMBER_NUMBER_ERROR_MESSAGE)
    .expect(UpdateNumberAndDept.errorText.nth(3).textContent)
    .contains(INVALID_DEPARTMENT_NUMBER_ERROR_MESSAGE)

    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(UpdateNumberAndDept.phnInput, '9332912486')
    .typeText(UpdateNumberAndDept.groupMemberInput, '6337109')
    .typeText(UpdateNumberAndDept.groupMemberNumberInput, '123456')
    .typeText(UpdateNumberAndDept.departmentNumberInput, '123456')
    .wait(1000)
    // When I click the submit button
    .click(UpdateNumberAndDept.submitButton)
    .wait(5000)
    // I expect the message from downstream
    .expect(UpdateNumberAndDept.phnOutput.exists)
    .ok()
    .expect(UpdateNumberAndDept.groupMemberOutput.exists)
    .ok()
    .expect(UpdateNumberAndDept.groupMemberNumberOutput.exists)
    .ok()
    .expect(UpdateNumberAndDept.departmentNumberOutput.exists)
    .ok()
    .expect(UpdateNumberAndDept.updateAnotherButton.exists)
    .ok()
})

test('Check validation passes if department is not filled', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(UpdateNumberAndDept.phnInput, '9332912486')
    .typeText(UpdateNumberAndDept.groupMemberInput, '6337109')
    .typeText(UpdateNumberAndDept.groupMemberNumberInput, '123456')
    .wait(5000)
    // When I click the submit button
    .click(UpdateNumberAndDept.submitButton)
    // I expect the message from downstream
    .expect(UpdateNumberAndDept.phnOutput.exists)
    .ok()
    .expect(UpdateNumberAndDept.groupMemberOutput.exists)
    .ok()
    .expect(UpdateNumberAndDept.updateAnotherButton.exists)
    .ok()
})

test('Check validation passes if Group Member number is not filled', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(UpdateNumberAndDept.phnInput, '9332912486')
    .typeText(UpdateNumberAndDept.groupMemberInput, '6337109')
    .typeText(UpdateNumberAndDept.departmentNumberInput, '123456')
    .wait(5000)
    // When I click the submit button
    .click(UpdateNumberAndDept.submitButton)
    // I expect the message from downstream
    .expect(UpdateNumberAndDept.phnOutput.exists)
    .ok()
    .expect(UpdateNumberAndDept.updateAnotherButton.exists)
    .ok()
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(UpdateNumberAndDept.phnInput, '9882807277')
    .typeText(UpdateNumberAndDept.groupMemberInput, '6337109')
    .typeText(UpdateNumberAndDept.groupMemberNumberInput, '123456')
    .typeText(UpdateNumberAndDept.departmentNumberInput, '123456')
    // When I click the cancel button
    .click(UpdateNumberAndDept.clearButton)
    // I expect the form to be cleared
    .expect(UpdateNumberAndDept.phnInput.value)
    .eql('')
    .expect(UpdateNumberAndDept.groupMemberInput.value)
    .eql('')
    .expect(UpdateNumberAndDept.groupMemberNumberInput.value)
    .eql('')
    .expect(UpdateNumberAndDept.departmentNumberInput.value)
    .eql('')
})
