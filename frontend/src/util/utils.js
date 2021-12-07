export function formatPersonName(person) {
  let name = ''
  if (person) {
    if (person.surname) {
      name = name + person.surname
    }
    if (person.givenName) {
      name = name + ', ' + person.givenName
    }
    if (person.secondName) {
      name = name + ' ' + person.secondName
    }
  }
  return name
}