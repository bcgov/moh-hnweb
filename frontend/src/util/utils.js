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

export function decodeRelationship(relationshipCode) {
  console.log(`Decode ${relationshipCode}`)
  switch (relationshipCode) {
    case 'C':
      return 'Employee'
    case 'D':
      return 'Dependent'
    case 'S':
      return 'Spouse'
    default:
      return relationshipCode
  }
}
