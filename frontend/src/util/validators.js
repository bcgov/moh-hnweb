import dayjs from 'dayjs'

import { helpers } from '@vuelidate/validators'

/**
 * Validates that the PHN matches the accepted format.
 * This assumes the PHN is optional and an empty value won't
 * case validation failure.
 */
export function validateOptionalPHN(phn) {
  if (phn === undefined || phn === '') {
    return true
  }
  return validatePHNFormat(phn)
}

/**
 * Validates that the PHN matches the accepted format.
 * This assumes the PHN also has a required validation.
 */
export function validatePHN(phn) {
  if (!helpers.req(phn)) {
    return true
  }
  return validatePHNFormat(phn)
}

function validatePHNFormat(phn) {
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
  if (dayjs(dateOfBirth).isAfter(dayjs().startOf('day'))) {
    return false
  }
  return true
}

/**
 * Validates that the Contract Number is valid.
 */
 export function validateContractNumber(contractNumber) {
  if (!helpers.req(contractNumber)) {
    return true
  }
  // TODO (weskubo-cgi) Add contract number validator
  return true
}

/**
 * Validates that the Broup Number is valid.
 */
 export function validateGroupNumber(groupNumber) {
  if (!helpers.req(groupNumber)) {
    return true
  }
  // TODO (weskubo-cgi) Add group number validator
  return true
}

export const VALIDATE_DOB_MESSAGE = "Date of Birth must not be in the future"
export const VALIDATE_PHN_MESSAGE = "PHN format is invalid"
export const VALIDATE_CONTRACT_NUMBER_MESSAGE = "MSP Contract Number is invalid"
export const VALIDATE_GROUP_NUMBER_MESSAGE = "Group Number is invalid"