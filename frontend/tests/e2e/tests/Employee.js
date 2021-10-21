import Employee from '../pages/EmployeePage.js';
import {Role} from 'testcafe';

const SITE_UNDER_TEST = 'http://localhost:3000'

//TODO: import { regularAccUser } from '../roles/roles';
const regularAccUser = Role(SITE_UNDER_TEST, async t => {
    await t
        .click('#zocial-moh_idp')
        .typeText('#username', 'hnweb1')
        .typeText('#password', process.env.TESTCAFE_PASSWORD)
        .click("#kc-login");
});
fixture(`Employee Page`)
.disablePageCaching `Test Employee`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
  .page('http://localhost:3000');

test('Check Employee tab is clickable', async t => {
	await t      
		.click(Employee.employeeLink);

		console.log("testcafe clicked Employee tab ")
});

test('Check R30 - Add Employee is present ', async t => {
	await t
		.click(Employee.employeeLink)
		.wait(1000)
		.hover(Employee.addEmpR30);
			  
		console.log("testcafe hover R30 - Add Employee ")
});

test('Check list of elements on Employee page', async t => {

	await t
		.click(Employee.employeeLink)
		.expect(Employee.list.nth(6).innerText).eql('R30 - Add Employee')
		.expect(Employee.list.nth(7).innerText).contains('R31 - Add Employee')
		.expect(Employee.list.nth(8).innerText).eql('R32 - Get Contract Periods')
		.expect(Employee.list.nth(9).innerText).eql('R34 - Update Employee Number and/or Department')
		.expect(Employee.list.nth(10).innerText).eql('R35 - Cancel Employee')
		.expect(Employee.list.nth(11).innerText).contains('R37- Get Employee')
		.expect(Employee.list.nth(12).innerText).contains('R38 - Update Employee')
		.expect(Employee.list.nth(13).innerText).eql('R40 - Contract Inquiry');

		console.log('checked text for Employee list items');
 
});