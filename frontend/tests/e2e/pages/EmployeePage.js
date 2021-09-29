import { Selector } from 'testcafe';

class Employee {
    constructor () {
        this.employeeLink = Selector('#employees-link'); 
        this.addEmpR30  = Selector('li').withText('R30 - Add Employee');       
        
    }
}

export default new Employee();