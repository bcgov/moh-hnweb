import dayjs from 'dayjs'
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
  if (dayjs(dateOfBirth).isAfter(dayjs().startOf('day'))) {
    return false
  }
  return true
}

/**
 * Validate a number field to a length of 7
 */
export function validateNumber(value) {
  if (!helpers.req(value)) {
    return true
  }
  if (value.length != 7) {
    return false
  }
  for (let i = 0; i < value.length; i++) {
    if (isNaN(value.charAt(i))) {
      return false
    }
  }
  return true
}

/**
 * Can be up to nine (9) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid.
 */
export function validateGroupMemberNumber(value) {
  if (value.length > 9) {
    return false
  }
  var invalidChars = /[\\|^\&]/;  
  if (invalidChars.test(value)) {
    return false
  }
  return true
} 

/**
 * Can be up to nine (6) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid.
 */
 export function validateDepartmentNumber(value) {
  if (value.length > 6) {
    return false
  }
  for (let i = 0; i < 7; i++) {
    if (isNaN(value.charAt(i))) {
      return false
    }
  }
  return true
}

/**
 * Only numbers 0 to 9 are valid. Phone Number must be entered as seven (10) numbers in length with no space or hyphen.
 */
 export function validateTelephone(value) {
  if (value.length != 10) {
    return false
  }
  for (let i = 0; i < value.length; i++) {
    if (isNaN(value.charAt(i))) {
      return false
    }
  }
  return true
} 

export const VALIDATE_DOB_MESSAGE = "Date of Birth must not be in the future"
export const VALIDATE_PHN_MESSAGE = "PHN format is invalid"
export const VALIDATE_GROUP_NUMBER_MESSAGE = "Only digits 0 to 9 are valid. Group Number must be entered as seven (7) digits in length."
export const VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE = "Can be up to nine (9) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid."
export const VALIDATE_DEPARTMENT_NUMBER_MESSAGE = "Can be up to nine (6) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid."
export const VALIDATE_TELEPHONE_MESSAGE = "Only numbers 0 to 9 are valid. Phone Number must be entered as ten (10) numbers in length with no space or hyphen."