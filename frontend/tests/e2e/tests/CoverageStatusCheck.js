import { ClientFunction, Selector } from 'testcafe';

import AlertPage from '../pages/AlertPage';
import CoverageStatusCheckPage from '../pages/eligibility/CoverageStatusCheckPage';
import { regularAccUser } from '../roles/roles';
import { SITE_UNDER_TEST } from '../configuration';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const PHN_ERROR_MESSAGE = 'PHN is required';
const DOB_ERROR_MESSAGE = 'Date Of Birth is required';
const INVALID_DOB_ERROR_MESSAGE = 'Date of Birth must not be in the future';
const DOS_ERROR_MESSAGE = 'Date Of Service is required';
const INVALID_PHN_ERROR_MESSAGE = 'PHN format is invalid';
const SUCCESS_MESSAGE = 'Search complete';
const TRANS_SUCCESSFUL = 'Transaction Successful';

const PAGE_TO_TEST = SITE_UNDER_TEST + '/eligibility/coverageStatusCheck'

const makeVisible = ClientFunction((selector) => {
        const element = document.getElementById(selector);
        element.style.width = '1px';
        element.style.height = '1px';
    });

fixture(`Coverage Status Check Page`)
.disablePageCaching `Test Coverage Status Check`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
   .page(PAGE_TO_TEST);

test('Error when no phn, dateOfBirth and dateOfService', async t => {
    await t
        .selectText(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('delete')
        .pressKey('tab')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(CoverageStatusCheckPage.ErrorText.nth(0).textContent).contains(PHN_ERROR_MESSAGE)
        .expect(CoverageStatusCheckPage.ErrorText.nth(1).textContent).contains(DOB_ERROR_MESSAGE)
        .expect(CoverageStatusCheckPage.ErrorText.nth(2).textContent).contains(DOS_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Error when no phn selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20211108')
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')    
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Error when no dateOfBirth selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '9306448169')
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab') 
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Error when no dateOfService selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '9306448169')
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20211108') 
        .selectText(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('delete')
        .pressKey('tab')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Error when no phn and dateOfService selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20211108')
        .selectText(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('delete')
        .pressKey('tab') 
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Error when no phn and dateofBirth selected', async t => {
    await t
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')   
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Check invalid phn format error message', async t => {

    await t
        .typeText(CoverageStatusCheckPage.phnInput, '9876543211')
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20211108') 
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')   
        .expect(CoverageStatusCheckPage.dateOfServiceInput.value).notEql('')
		.click(CoverageStatusCheckPage.submitButton)
        .expect(CoverageStatusCheckPage.ErrorText.nth(0).textContent).contains(INVALID_PHN_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});

test('Check future date of birth error message', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '9306448169')
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20291108') 
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')   
        .expect(CoverageStatusCheckPage.dateOfServiceInput.value).notEql('')
		.click(CoverageStatusCheckPage.submitButton)
        .expect(CoverageStatusCheckPage.ErrorText.nth(0).textContent).contains(INVALID_DOB_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
});


test('Check validation passed info', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '9306448169')
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20211108') 
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')   
        .expect(CoverageStatusCheckPage.dateOfServiceInput.value).notEql('')
		.click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(SUCCESS_MESSAGE)
        .expect(Selector('h2').textContent).contains(TRANS_SUCCESSFUL)
});

test('Check Subsidy Insured Service CheckBox is checked/unchecked ', async t => { 
    await makeVisible('checkSubsidyInsuredService');  
  	await t
		.click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox)  //Check box
        .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).ok()  //Confirm whether the option is selected
        .click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox)
        .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).notOk();
});

test('Check Date Of Last Eye Examination CheckBox is checked/unchecked', async t => { 
    await makeVisible('checkLastEyeExam');  
	await t
		.click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox)  //Check box
        .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).ok()  //Confirm whether the option is selected
        .click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox)
        .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).notOk();
});

test('Check Patient Restriction CheckBox is checked/unchecked', async t => { 
    await makeVisible('checkPatientRestriction');   
	await t
		.click(CoverageStatusCheckPage.patientRestrictionCheckBox)  //Check box
        .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).ok()  //Confirm whether the option is selected
        .click(CoverageStatusCheckPage.patientRestrictionCheckBox)
        .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).notOk();
});

test('Check submit button is clickable', async t => {
	await t
        .typeText(CoverageStatusCheckPage.phnInput, '9306448169')  
		.click(CoverageStatusCheckPage.submitButton)
});

test('Check cancelButton is clickable', async t => {
    await makeVisible('checkSubsidyInsuredService');
    await makeVisible('checkLastEyeExam');
    await makeVisible('checkPatientRestriction');
	await t
        .typeText(CoverageStatusCheckPage.phnInput, '9306448169') 
        .typeText(CoverageStatusCheckPage.dateOfBirthInput, '20211108') 
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')
        .click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox) 
        .click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox) 
        .click(CoverageStatusCheckPage.patientRestrictionCheckBox)  
		.click(CoverageStatusCheckPage.cancelButton)
        .expect(CoverageStatusCheckPage.phnInput.value).eql('')
        .expect(CoverageStatusCheckPage.dateOfBirthInput.value).eql('')	
        .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).notOk()
        .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).notOk()
        .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).notOk();
});

