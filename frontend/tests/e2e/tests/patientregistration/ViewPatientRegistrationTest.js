import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import ViewPatientRegistrationPage from '../../pages/patientregistration/ViewPatientRegistrationPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUCCESS_MESSAGE = 'Transaction completed successfully'
const WARNING_MESSAGE = 'Patient could not be found in the EMPI or in the PBF'
const POTENTIAL_DUPLICATE_EMPI = 'BCHCIM.GD.0.0021  The person returned is the subject of an potential duplicate'
const PATIENT_NOT_EXISTS_PBF = 'No registration information is found in the system for given PHN'
const DIFFERENT_MSP_PAYEE_WITHIN_GROUP = 'Patient is registered with a different MSP Payee number within the reporting group'
const DIFFERENT_MSP_PAYEE_OUTSIDE_GROUP = 'Patient is registered with a different MSP Payee number outside of reporting group'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/patientRegistration'

fixture(`Patient Registration Page`).disablePageCaching`Test Patient Registration`
  .beforeEach(async (t) => {
    await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
  })
  .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
  await t
    // Given required fields aren't filled out (PHN)
    // When I click the submit button
    .click(ViewPatientRegistrationPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ViewPatientRegistrationPage.errorText.nth(0).textContent)
    .contains(PHN_REQUIRED_MESSAGE)
})

test('Check invalid phn format validation', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ViewPatientRegistrationPage.phnInput, '9000448000')
    // When I click the submit button
    .click(ViewPatientRegistrationPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(ERROR_MESSAGE)
    .expect(ViewPatientRegistrationPage.errorText.nth(0).textContent)
    .contains(INVALID_PHN_ERROR_MESSAGE)
})

test('Check Patient Registration Warning Message when no EMPI, PBF records found', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ViewPatientRegistrationPage.phnInput, '9331926919')
    // When I click the submit button
    .click(ViewPatientRegistrationPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(WARNING_MESSAGE)
    .expect(ViewPatientRegistrationPage.patientDemoDetail.exists)
    .notOk()
    .expect(ViewPatientRegistrationPage.registrationResult.exists)
    .notOk()
    .expect(ViewPatientRegistrationPage.registrationData.exists)
    .notOk()
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.exists)
    .notOk()
})

test.skip('Check Patient Registration Success Message when EMPI exists but no PBF records found', async (t) => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(ViewPatientRegistrationPage.phnInput, '9878259011')
    // When I click the submit button
    .click(ViewPatientRegistrationPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    .expect(ViewPatientRegistrationPage.patientDemoDetail.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.registrationResult.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.registrationData.exists)
    .notOk()
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.textContent)
    .contains(PATIENT_NOT_EXISTS_PBF)
})

test('Check properly filled form passes validation and validate results', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ViewPatientRegistrationPage.phnInput, '9879869673')
    // When I click the submit button
    .click(ViewPatientRegistrationPage.submitButton)
    .wait(5000)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    // Both EMPI and PBF record found
    .expect(ViewPatientRegistrationPage.patientDemoDetail.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.registrationResult.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.registrationData.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.resultRow1.nth(0).textContent)
    .contains('9879869673')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(1).textContent)
    .contains('PURPLE')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(2).textContent)
    .contains('19400605')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(3).textContent)
    .contains('M')
    //Registration details
    .expect(ViewPatientRegistrationPage.resultRow1.nth(8).textContent)
    .contains('A0053')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(9).textContent)
    .contains('20200101  20200101')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(10).textContent)
    .contains('De-Registered')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(11).textContent)
    .contains('Practitioner No: X2731 Reg Reason: SL DeReg Reason: N/A Cancel Reason: N/A')
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.textContent)
    .contains(POTENTIAL_DUPLICATE_EMPI)
})

test('Check Patient registered with a different msp payee within group', async (t) => {
  await t
    // Given the page is filled out correctly
    .typeText(ViewPatientRegistrationPage.phnInput, '9879869673')
    .selectText(ViewPatientRegistrationPage.payeeInput)
    .pressKey('delete')
    .typeText(ViewPatientRegistrationPage.payeeInput, 'A0248')
    // When I click the submit button
    .click(ViewPatientRegistrationPage.submitButton)
    .wait(5000)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    .expect(ViewPatientRegistrationPage.patientDemoDetail.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.registrationResult.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.registrationData.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.resultRow1.nth(0).textContent)
    .contains('9879869673')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(1).textContent)
    .contains('PURPLE')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(2).textContent)
    .contains('19400605')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(3).textContent)
    .contains('M')
    //Registration details
    .expect(ViewPatientRegistrationPage.resultRow1.nth(8).textContent)
    .contains('A0053')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(9).textContent)
    .contains('20200101  20200101')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(10).textContent)
    .contains('De-Registered')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(11).textContent)
    .contains('Practitioner No: X2731 Reg Reason: SL DeReg Reason: N/A Cancel Reason: N/A')
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.textContent)
    .contains(DIFFERENT_MSP_PAYEE_WITHIN_GROUP + '\n' + POTENTIAL_DUPLICATE_EMPI)
})

test('Check Patient registered with a different msp payee outside group', async (t) => {
  await t
    .click(ViewPatientRegistrationPage.clearButton)
    // Given the page is filled out correctly
    .typeText(ViewPatientRegistrationPage.phnInput, '9879869673')
    .typeText(ViewPatientRegistrationPage.payeeInput, 'A0055')
    // When I click the submit button
    .click(ViewPatientRegistrationPage.submitButton)
    .wait(5000)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent)
    .contains(SUCCESS_MESSAGE)
    .expect(ViewPatientRegistrationPage.patientDemoDetail.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.registrationResult.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.exists)
    .ok()
    .expect(ViewPatientRegistrationPage.resultRow1.nth(0).textContent)
    .contains('9879869673')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(1).textContent)
    .contains('PURPLE')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(2).textContent)
    .contains('19400605')
    .expect(ViewPatientRegistrationPage.resultRow1.nth(3).textContent)
    .contains('M')
    .expect(ViewPatientRegistrationPage.additionalInfoMessage.textContent)
    .contains(DIFFERENT_MSP_PAYEE_OUTSIDE_GROUP + '\n' + POTENTIAL_DUPLICATE_EMPI)
})

test('Check clear button clears the form', async (t) => {
  await t
    // Given I have a form filled out with data
    .typeText(ViewPatientRegistrationPage.phnInput, '9000448000')

    // When I click the Clear button
    .click(ViewPatientRegistrationPage.clearButton)
    // I expect the form to be cleared
    .expect(ViewPatientRegistrationPage.phnInput.value)
    .eql('')
    .expect(ViewPatientRegistrationPage.payeeInput.value)
    .notEql('')
})
