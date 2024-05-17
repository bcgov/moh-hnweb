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

export function handlePBFServiceError(err, alertStore, isPBFUser, router) {
  if (typeof err === 'boolean') {
    // Keycloak refresh token expiry causes Boolean err
    // Warn and return to login
    alertStore.setWarningAlert('Session expired. Please login.')
    isPBFUser ? router.push({ name: 'PBFLogin' }) : router.push({ name: 'Login' })
  } else {
    alertStore.setErrorAlert(err)
  }
}

export function handleServiceError(err, alertStore, router) {
  if (typeof err === 'boolean') {
    // Keycloak refresh token expiry causes Boolean err
    // Warn and return to login
    alertStore.setWarningAlert('Session expired. Please login.')
    router.push({ name: 'Login' })
  } else {
    alertStore.setErrorAlert(err)
  }
}

/**
 * A method to transform the Gender value from anything other than "M" or "F" to "U" as not all systems can 
 * handle variations such as "*"(masked), "UNK" or "UN".
 * @param gender 
 * @returns 
 */
export function resolveGender(gender) {
  if (gender !== 'F' && gender !== 'M') {
    return 'U'
  }
  return gender
}
