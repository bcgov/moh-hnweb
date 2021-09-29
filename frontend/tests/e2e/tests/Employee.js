import Employee from '../pages/EmployeePage.js';

fixture(`Employee Page`)
  .page('http://localhost:3000');

test('Click Employee tab ', async t => {
	await t      
		.click(Employee.employeeLink);			  
		console.log("testcafe clicked Employee tab ")
});

test('Checking R30 - Add Employee is present ', async t => {
	await t
		.click(Employee.employeeLink)
		.wait(1000)
		.hover(Employee.addEmpR30);
			  
		console.log("testcafe hover R30 - Add Employee ")
});