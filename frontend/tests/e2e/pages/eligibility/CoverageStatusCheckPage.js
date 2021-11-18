import { Selector } from 'testcafe';

class CoverageStatusCheck {
    constructor () {
        this.phnInput = Selector('#phn'); 
        this.dateOfBirthInput  = Selector('div#dateOfBirth > div > div > div > input');
        this.dateOfServiceInput  =  Selector('div#dateOfService > div > div > div > input');
        this.subsidyInsuredServiceCheckBox = Selector('#checkSubsidyInsuredService', { timeout: 1000 });
        this.dateOfLastEyeExaminationCheckBox = Selector('#checkLastEyeExam', { timeout: 1000 });
        this.patientRestrictionCheckBox = Selector('#checkPatientRestriction', { timeout: 1000 });
        this.ErrorText = Selector('div').withAttribute('class','error-text');

        this.submitButton = Selector('button[type="submit"]');
        this.cancelButton = Selector('button[type="button"]');
        
    }
}

export default new CoverageStatusCheck();