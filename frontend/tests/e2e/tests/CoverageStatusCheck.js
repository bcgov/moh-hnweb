import AlertPage from '../pages/AlertPage';
import CoverageStatusCheckPage from '../pages/eligibility/CoverageStatusCheckPage';
import {Role} from 'testcafe';

const ERROR_MESSAGE = 'Please correct errors before submitting';
const PHN_ERROR_MESSAGE = 'PHN is required';
const DOB_ERROR_MESSAGE = 'Date Of Birth is required';
const INVALID_DOB_ERROR_MESSAGE = 'Date of Birth must not be in the future';
const SUCCESS_MESSAGE = 'Search complete';
const SITE_UNDER_TEST = 'http://localhost:3000/eligibility/coverageStatusCheck'

const regularAccUser = Role(SITE_UNDER_TEST, async t => {
    await t
        .click('#zocial-moh_idp')
        .typeText('#username', 'hnweb1')
        .typeText('#password', process.env.TESTCAFE_PASSWORD)
        .click("#kc-login");
});

fixture(`Coverage Status Check Page`)
.disablePageCaching `Test Coverage Status Check`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
  .page('http://localhost:3000/eligibility/coverageStatusCheck');

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

        console.log("Error when no phn, dateOfbirth and dateOfService provided");
});

test('Error when no phn selected', async t => {
    await t
        .click(CoverageStatusCheckPage.dateOfBirthInput)
        .pressKey('tab')
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')    
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no phn selected");
});

test('Error when no dateOfBirth selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456')
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab') 
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no date of birth selected");
});

test('Error when no dateOfService selected', async t => {
    await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456')
        .click(CoverageStatusCheckPage.dateOfBirthInput)
        .pressKey('tab')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no date of service selected");
});

test('Error when no phn and dateOfService selected', async t => {
    await t
        .click(CoverageStatusCheckPage.dateOfBirthInput)
        .pressKey('tab')
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no phn and date of service selected");
});

test('Error when no phn and dateofBirth selected', async t => {
    await t
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')   
        .click(CoverageStatusCheckPage.submitButton)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)
       
        console.log("Error when no phn and date of birth selected");
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

		console.log("Error when no phn and date of birth selected")
        console.log(CoverageStatusCheckPage.dateOfServiceInput.value)
});

test('Check future date of birth error message', async t => {
    var today = new Date();
    var date = today.getFullYear()+(today.getMonth()+1)+today.getDate();

    await t
        .typeText(CoverageStatusCheckPage.phnInput, '9306448169')
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab')   
        .expect(CoverageStatusCheckPage.dateOfServiceInput.value).notEql('')
		.click(CoverageStatusCheckPage.submitButton)
        //.expect(CoverageStatusCheckPage.ErrorText.nth(1).textContent).contains(DOB_ERROR_MESSAGE)
        .expect(AlertPage.alertBannerText.textContent).contains(ERROR_MESSAGE)

		console.log("Check future date of birth error message")
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

		console.log("Check validation passed info")
});

test.skip('Check Subsidy Insured Service CheckBox is checked/unchecked ', async tc => {  
	await tc
        .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.visible).ok()
		.click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox)  //Check box
        .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).ok()  //Confirm whether the option is selected
        .click(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox)
        .expect(CoverageStatusCheckPage.subsidyInsuredServiceCheckBox.checked).notOk();
});

test.skip('Check Date Of Last Eye Examination CheckBox is checked/unchecked ', async tc => {  
	await tc
        .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.visible).ok()
		.click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox)  //Check box
        .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).ok()  //Confirm whether the option is selected
        .click(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox)
        .expect(CoverageStatusCheckPage.dateOfLastEyeExaminationCheckBox.checked).notOk();
});

test.skip('Check Patient Restriction CheckBox is checked/unchecked ', async tc => {  
	await tc
		.click(CoverageStatusCheckPage.patientRestrictionCheckBox)  //Check box
        .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).ok()  //Confirm whether the option is selected
        .click(CoverageStatusCheckPage.patientRestrictionCheckBox)
        .expect(CoverageStatusCheckPage.patientRestrictionCheckBox.checked).notOk();
});

test('Check submitbutton is clickable', async t => {
	await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456')  
		.click(CoverageStatusCheckPage.submitButton)

		console.log("testcafe clicked submit button")
});

test('Check cancelButton is clickable', async t => {
	await t
        .typeText(CoverageStatusCheckPage.phnInput, '123456') 
        .click(CoverageStatusCheckPage.dateOfBirthInput)
        .pressKey('tab') 
        .click(CoverageStatusCheckPage.dateOfServiceInput)
        .pressKey('tab') 
		.click(CoverageStatusCheckPage.cancelButton)
        .expect(CoverageStatusCheckPage.phnInput.value).eql('')
        .expect(CoverageStatusCheckPage.dateOfBirthInput.value).eql('')	
       
		console.log("testcafe clicked cancel button")
});

