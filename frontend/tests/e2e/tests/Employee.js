import Employee from '../pages/EmployeePage.js';

fixture(`Employee Page`)
  .page('http://localhost:3000');

test('Click Employee tab ', async t => {
	await t
		.setNativeDialogHandler(() => true)        
		.click(Employee.employeeLink);			  
		console.log("testcafe clicked Employee tab ")
});