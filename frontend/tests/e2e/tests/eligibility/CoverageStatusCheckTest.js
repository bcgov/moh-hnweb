import { ClientFunction, Selector } from 'testcafe'

import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import CoverageStatusCheckPage from '../../pages/eligibility/CoverageStatusCheckPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const PHN_REQUIRED_MESSAGE = 'PHN is required'
const DOB_REQUIRED_MESSAGE = 'Date Of Birth is required'
const INVALID_DOB_ERROR_MESSAGE = 'Date of Birth must not be in the future'
const DOS_REQUIRED_MESSAGE = 'Date Of Service is required'
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid'
const SUCCESS_MESSAGE = 'SUCCESSFULLY COMPLETED'

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/coverageStatusCheck'

const makeVisible = ClientFunction((selector) => {
  const element = document.getElementById(selector)
  element.style.width = '1px'
  element.style.height = '1px'
})

fixture(`Coverage Status Check Page`)
  .disablePageCaching `Test Coverage Status Check`
  .beforeEach(async t => {
    await t
      .useRole(regularAccUser)
  })
  .page(PAGE_TO_TEST)

test('Check required fields validation', async t => {
  await t
    // Given required fields aren't filled out (phn, date of birth, and date of service)
    .selectText(CoverageStatusCheckPage.dateOfServiceInput)
    .pressKey('delete')
    .pressKey('tab')
    // When I click the submit button
    .click(CoverageStatusCheckPage.submitButton)
    // I expect an error message stating the page had errors and individual error messages for each required field
    .expect(CoverageStatusCheckPage.errorText.nth(0).textContent).contains(PHN_REQUIRED_MESSAGE)
    .expect(CoverageStatusCheckPage.errorText.nth(1).textContent).contains(DOB_REQUIRED_MESSAGE)
    .expect(CoverageStatusCheckPage.errorText.nth(2).textContent).contains(DOS_REQUIRED_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
})

test('Check invalid phn format validation', async t => {
  await t
    // Given a PHN entered with an invalid format
    .typeText(CoverageStatusCheckPage.phnInput, '9000444000')
    // When I click the submit button
		.click(CoverageStatusCheckPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the PHN format
    .expect(CoverageStatusCheckPage.errorText.nth(0).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
})

test('Check date of birth in future validation', async t => {
  await t
    // Given a date of birth entered in the future
    .typeText(CoverageStatusCheckPage.phnInput, '9306448169')
    .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20391103')
    .pressKey('tab')
    // When I click the submit button
		.click(CoverageStatusCheckPage.submitButton)
    // I expect an error message stating the page had errors and an individual error message for the DoB format
    .expect(CoverageStatusCheckPage.errorText.nth(0).textContent).contains(INVALID_DOB_ERROR_MESSAGE)
    .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
})

test('Check properly filled form passes validation for an ineligible PHN', async t => {
  const dateOfService = '20211201'
  await t
    // Given the page is filled out correctly
    .typeText(CoverageStatusCheckPage.phnInput, '9395568139')
    .typeText(CoverageStatusCheckPage.dateOfBirthInput, '19710919') 
    // The date input doesn't seem to take a new value without clearing the original value
    .selectText(CoverageStatusCheckPage.dateOfServiceInput)
    .pressKey('delete')
    .typeText(CoverageStatusCheckPage.dateOfServiceInput, dateOfService)
    // .click(CoverageStatusCheckPage.dateOfServiceInput)
    .pressKey('tab')
    // When I click the submit button
    .expect(CoverageStatusCheckPage.dateOfServiceInput.value).notEql('')
		.click(CoverageStatusCheckPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
    // And the results to be populated
    .expect(CoverageStatusCheckPage.result.exists).ok()
    .expect(CoverageStatusCheckPage.resultRow1.exists).ok()
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(0).child('span').textContent).eql('9395568139') 
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(1).child('span').textContent).eql('PORTFOLIOXE, ROISHANNXD ECKAXK')
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(2).child('span').textContent).eql('19710919')
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(3).child('span').textContent).eql('FEMALE')

    .expect(CoverageStatusCheckPage.resultRow2.exists).ok()
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(0).child('span').textContent).eql(dateOfService)
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(1).child('span').textContent).eql('NO')
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(2).child('span').textContent).eql('20190731')
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(3).child('span').textContent).eql('OUT OF PROVINCE MOVE')

    .expect(CoverageStatusCheckPage.clientInstructions.textContent).eql('MSP\'S RECORDS INDICATE THAT THIS PERSON HAS MOVED PERMANENTLY FROM BC. PLEASE CONFIRM RESIDENCE, OBTAIN AND UPDATE ADDRESS AND TELEPHONE INFORMATION AND ADVISE PERSON TO CONTACT MSP TO RE-ESTABLISH ELIGIBILITY.')
})

test('Check properly filled form passes validation for an eligible PHN', async t => {
  const dateOfService = '20211201'
  await t
    // Given the page is filled out correctly
    .typeText(CoverageStatusCheckPage.phnInput, '9347984074')
    .typeText(CoverageStatusCheckPage.dateOfBirthInput, '19850915') 
    // The date input doesn't seem to take a new value without clearing the original value
    .selectText(CoverageStatusCheckPage.dateOfServiceInput)
    .pressKey('delete')
    .typeText(CoverageStatusCheckPage.dateOfServiceInput, dateOfService)
    .pressKey('tab')
    // When I click the submit button
    .expect(CoverageStatusCheckPage.dateOfServiceInput.value).notEql('')
		.click(CoverageStatusCheckPage.submitButton)
    // I expect a success message
    .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
    // And the results to be populated
    .expect(CoverageStatusCheckPage.result.exists).ok()
    .expect(CoverageStatusCheckPage.resultRow1.exists).ok()
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(0).child('span').textContent).eql('9347984074') 
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(1).child('span').textContent).eql('GENUS ACRIDOTHERESXC, MOHAMMED-ALIMXB ANJUMXI')
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(2).child('span').textContent).eql('19850915')
    .expect(CoverageStatusCheckPage.resultRow1.child('div').nth(3).child('span').textContent).eql('MALE')

    .expect(CoverageStatusCheckPage.resultRow2.exists).ok()
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(0).child('span').textContent).eql(dateOfService)
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(1).child('span').textContent).eql('YES')
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(2).child('span').textContent).eql('')
    .expect(CoverageStatusCheckPage.resultRow2.child('div').nth(3).child('span').textContent).eql('')
})

test('Check Subsidy Insured Service CheckBox check and uncheck work', async t => {
  await makeVisible('checkSubsidyInsuredService')  
  await t
    // When I click a checkbox
    // I expect the status to toggle
		.click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox)  //Check box
    .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).ok()  //Confirm whether the option is selected
    .click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox)
    .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).notOk()
})

test('Check Date Of Last Eye Examination CheckBox check and uncheck work', async t => {
  await makeVisible('checkLastEyeExam')  
	await t
    // When I click a checkbox
    // I expect the status to toggle
		.click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox)  //Check box
    .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).ok()  //Confirm whether the option is selected
    .click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox)
    .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).notOk()
})

test('Check Patient Restriction CheckBox check and uncheck work', async t => {
  await makeVisible('checkPatientRestriction')   
	await t
    // When I click a checkbox
    // I expect the status to toggle
		.click(CoverageStatusCheckPage.patientRestrictionCheckBox)  //Check box
    .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).ok()  //Confirm whether the option is selected
    .click(CoverageStatusCheckPage.patientRestrictionCheckBox)
    .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).notOk()
})

test('Check clear button clears the form', async t => {
  await makeVisible('checkSubsidyInsuredService')
  await makeVisible('checkLastEyeExam')
  await makeVisible('checkPatientRestriction')
	await t
    // Given I have a form filled out with data
    .typeText(CoverageStatusCheckPage.phnInput, '9000444000')
    .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20211108') 
    .click(CoverageStatusCheckPage.dateOfServiceInput)
    .pressKey('tab')
    .click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox) 
    .click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox) 
    .click(CoverageStatusCheckPage.patientRestrictionCheckBox)
    // When I click the Clear button
		.click(CoverageStatusCheckPage.clearButton)
    // I expect the form to be cleared
    .expect(CoverageStatusCheckPage.phnInput.value).eql('')
    .expect(CoverageStatusCheckPage.dateOfBirthInput.value).eql('')	
    .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).notOk()
    .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).notOk()
    .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).notOk()
})

