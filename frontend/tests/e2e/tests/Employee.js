import Employee from '../pages/EmployeePage.js';
import { regularAccUser } from '../roles/roles';
import { SITE_UNDER_TEST } from '../configuration';

fixture(`Employee Page`)
.disablePageCaching `Test Employee`
    .beforeEach(async t => {
        await t
            .useRole(regularAccUser)
    })
  .page(SITE_UNDER_TEST);

test('Check Employee tab is clickable', async t => {
	await t      
		.click(Employee.employeeLink);
});

test('Check R30 - Add Employee is present ', async t => {
	await t
		.click(Employee.employeeLink)
		.wait(1000)
		.hover(Employee.addEmpR30);
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
});