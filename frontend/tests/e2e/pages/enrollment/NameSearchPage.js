import { Selector } from 'testcafe'

class NameSearch {
  constructor() {
    this.surnameInput = Selector('#surname')
    this.firstNameInput = Selector('#firstName')
    this.secondNameInput = Selector('#secondName')
    this.dateOfBirthInput = Selector('#dp-input-dateOfBirth')
    this.radioButton = Selector('#gender')
    this.radioButtonUnknown = Selector('label').withText('Unknown')

    this.submitButton = Selector('button[type="submit"]')
    this.addButton = Selector('button[type="button"]').withText('Add')
    this.createNewPHNButton = Selector('button[type="button"]').withText('Create New PHN')
    this.clearButton = Selector('button[type="button"]')
    this.errorText = Selector('div').withAttribute('class', 'error-text')
    this.numberOfMatchesText = Selector('p').withText('Number of Matches: 10')
  }
}

export default new NameSearch()
