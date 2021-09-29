import { Selector } from 'testcafe';

class Employee {
    constructor () {
        this.employeeLink = Selector('#employees-link'); 
    }
}

export default new Employee();