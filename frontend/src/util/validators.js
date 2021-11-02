import moment from 'moment'

import { helpers } from '@vuelidate/validators'

/**
 * Validates that the PHN matches the accepted format.
 */
export function validatePHN(phn) {
  if (!helpers.req(phn)) {
    return true
  }

  const phnSigDigits = [2, 4, 8, 5, 10, 9, 7, 3]
  let checksum = 0
  let digit = 0

  if (phn.length != 10 || phn.charAt(0) != '9') {
    return false
  } else {
    for (let i = 1; i < 9; i++) {
        digit = Number(phn.charAt(i))
        checksum += (digit * phnSigDigits[i - 1]) % 11
    }
    checksum = 11 - checksum % 11
    if (Number(phn.charAt(9)) != checksum) {
      return false
    }
  }
  return true
}

/**
 * Validates that the Date of Birth is not in the future.
 */
export function validateDOB(dateOfBirth) {
  if (!helpers.req(dateOfBirth)) {
    return true
  }
  console.log(dateOfBirth)
  console.log(moment())
  if (moment(dateOfBirth).isAfter(moment().startOf('day'))) {
    return false
  }
  return true
}

export const VALIDATE_DOB_MESSAGE = "Date of Birth must not be in the future"
export const VALIDATE_PHN_MESSAGE = "PHN format is invalid"