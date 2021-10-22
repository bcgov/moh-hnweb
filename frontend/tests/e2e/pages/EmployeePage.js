import { Selector } from 'testcafe';

class Employee {
    constructor () {
        this.employeeLink = Selector('#manageEmployees-link'); 
        this.addEmpR30  = Selector('li').withText('R30 - Add Employee');
        this.list = Selector('li');       
        
    }
}

export default new Employee();