import { ClientFunction, Selector } from 'testcafe'

import { SITE_UNDER_TEST } from '../../configuration'
import AlertPage from '../../pages/AlertPage'
import AuditReportingPage from '../../pages/reports/AuditReportingPage'
import { regularAccUser } from '../../roles/roles'

const ERROR_MESSAGE = 'Please correct errors before submitting'
const USER_REQUIRED_MESSAGE = 'User ID: is required'
const START_DATE_REQUIRED_MESSAGE = 'Start Date is required'
const END_DATE_REQUIRED_MESSAGE = 'End Date is required'
const SUCCESS_MESSAGE = 'Transaction completed successfully'
const NO_RESULT_MESSAGE = 'No results were returned. Please refine your search criteria, and try again.'
const PAGE_TO_TEST = SITE_UNDER_TEST + '/reports/auditReporting'


fixture(`Audit Reporting Page`).disablePageCaching`Test Audit Reporting`
    .beforeEach(async (t) => {
        await t.useRole(regularAccUser).navigateTo(PAGE_TO_TEST)
    })
    .page(SITE_UNDER_TEST)

test('Check required fields validation', async (t) => {
    await t
        // Given required fields aren't filled out (User Id, Start Date, End Date)
        // When I click the submit button
        .click(AuditReportingPage.submitButton)
        // I expect an error message stating the page had errors and individual error messages for each required field
        .expect(AlertPage.alertBannerText.textContent)
        .contains(ERROR_MESSAGE)
        .expect(AuditReportingPage.errorText.nth(0).textContent)
        .contains(USER_REQUIRED_MESSAGE)
        .expect(AuditReportingPage.errorText.nth(1).textContent)
        .contains(START_DATE_REQUIRED_MESSAGE)
        .expect(AuditReportingPage.errorText.nth(2).textContent)
        .contains(END_DATE_REQUIRED_MESSAGE)
})

test('Check properly filled form passes validation and validate results', async (t) => {
    await t
        // Given the page is filled out correctly
        .typeText(AuditReportingPage.userIdInput, 'hnweb1')
        .click(AuditReportingPage.checkBoxInput1) //Check box
        .click(AuditReportingPage.checkBoxInput2) //Check box
        .click(AuditReportingPage.checkBoxInput3) //Check box
        .typeText(AuditReportingPage.startDateInput, '20220701')
        .pressKey('tab')
        .typeText(AuditReportingPage.endDateInput, '20220708')
        .pressKey('tab')
        // When I click the submit button
        .click(AuditReportingPage.submitButton)
        // I expect a success message
        .expect(AlertPage.alertBannerText.textContent)
        .contains(SUCCESS_MESSAGE)

        // And a table with two results
        .expect(AuditReportingPage.resultsTable.exists)
        .ok()
        .expect(AuditReportingPage.resultsTable.child('thead').exists)
        .ok()
        .expect(AuditReportingPage.resultsTable.child('tbody').child('tr').count)
        .eql(2)
        .expect(AuditReportingPage.resultsRow1.exists)
        .ok()
        .expect(AuditReportingPage.resultsRow1.child('td').exists)
        .ok()

        // Validate the first row
        .expect(AuditReportingPage.resultsRow1.child('td').nth(0).textContent)
        .eql('CheckEligibility')
        .expect(AuditReportingPage.resultsRow1.child('td').nth(1).textContent)
        .eql('00000010')
        .expect(AuditReportingPage.resultsRow1.child('td').nth(2).textContent)
        .eql('hnweb1')
        .expect(AuditReportingPage.resultsRow1.child('td').nth(3).textContent)
        .eql('2022-07-06T23:22:29.591')
        .expect(AuditReportingPage.resultsRow1.child('td').nth(4).textContent)
        .eql('9331926919')
        .expect(AuditReportingPage.resultsRow1.child('td').nth(5).textContent)
        .eql('PHN')
        .expect(AuditReportingPage.resultsRow1.child('td').nth(6).textContent)
        .eql('51364023-ee0d-420a-a1d0-d3a5a24dae86')
})

test('Check properly filled form passes validation when no record found', async (t) => {
    await t
        // Given the page is filled out correctly
        .typeText(AuditReportingPage.userIdInput, 'hnweb')
        .click(AuditReportingPage.checkBoxInput1) //Check box
        .click(AuditReportingPage.checkBoxInput2) //Check box
        .click(AuditReportingPage.checkBoxInput3) //Check box
        .typeText(AuditReportingPage.startDateInput, '20220701')
        .pressKey('tab')
        .typeText(AuditReportingPage.endDateInput, '20220708')
        .pressKey('tab')
        // When I click the submit button
        .click(AuditReportingPage.submitButton)
        // I expect a success message
        .expect(AlertPage.alertBannerText.textContent)
        .contains(NO_RESULT_MESSAGE)
})

test('Check clear button clears the form', async (t) => {   
    await t
        // Given I have a form filled out with data
        .typeText(AuditReportingPage.userIdInput, 'hnweb1')
        .click(AuditReportingPage.checkBoxInput1) //Check box
        .click(AuditReportingPage.checkBoxInput2) //Check box
        .click(AuditReportingPage.checkBoxInput3) //Check box
        .typeText(AuditReportingPage.startDateInput, '20220701')
        .pressKey('tab')
        .typeText(AuditReportingPage.endDateInput, '20220708')
        .pressKey('tab')
        // When I click the Clear button
        .click(AuditReportingPage.clearButton)
        // I expect the form to be cleared
        .expect(AuditReportingPage.userIdInput.value)
        .eql('')
        .expect(AuditReportingPage.startDateInput.value)
        .eql('')
        .expect(AuditReportingPage.endDateInput.value)
        .eql('')
        .expect(AuditReportingPage.checkBoxInput1.checked)
        .notOk()
        .expect(AuditReportingPage.checkBoxInput2.checked)
        .notOk()
        .expect(AuditReportingPage.checkBoxInput3.checked)
        .notOk()
})
