import dayjs from 'dayjs'

import { helpers } from '@vuelidate/validators'

/**
 * Validates that the PHN matches the accepted format.
 * This assumes the PHN is optional and an empty value won't
 * cause validation failure.
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
    checksum = 11 - (checksum % 11)
    if (Number(phn.charAt(9)) != checksum) {
      return false
    }
  }
  return true
}

/**
 * Validates that the Postal Code matches the accepted format.
 * This assumes the Postal Code also has a required validation.
 */
export function validatePostalCode(postalCode) {
  if (!helpers.req(postalCode)) {
    return true
  }
  return validatePostalCodeFormat(postalCode)
}

/**
 * Validates that the Postal Code matches the accepted format.
 * Must be of the format ANANAN (where "A" is alpha and "N" is numeric). Must start with one of the "ABCEGHJKLMNPRSTVWXYZ" i.e., be a Canada postal code
 */
export function validateMailingPostalCode(postalCode) {
  if (postalCode === undefined || postalCode === '') {
    return true
  }
  var regex = new RegExp(/^[ABCEGHJKLMNPRSTVWXYZ]\d[ABCEGHJKLMNPRSTVWXYZ]\d[ABCEGHJKLMNPRSTVWXYZ]\d$/i)

  return regex.test(postalCode)
}

/**
 * Must be of the format ANANAN (where "A" is alpha and "N" is numeric). Must start with "V" i.e., be a British Columbia postal code
 */
function validatePostalCodeFormat(postalCode) {
  var regex = new RegExp(/^[V]\d[ABCEGHJKLMNPRSTVWXYZ]\d[ABCEGHJKLMNPRSTVWXYZ]\d$/i)

  return regex.test(postalCode)
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
 * Validates that the Date is not earlier than '1900-01-01' .
 */
export function validateMinimumDate(dateInput) {
  if (!helpers.req(dateInput)) {
    return true
  }
  if (!dayjs(dateInput).isAfter(dayjs('1900-01-01'))) {
    return false
  }
  return true
}

/**
 * Validates that the EffectiveDate is not earlier than '1900-01-01' .
 */
export function validateMinimumEffectiveDate(dateInput) {
  if (!helpers.req(dateInput)) {
    return true
  }
  dateInput = new Date(this.coverageEffectiveDate.year, this.coverageEffectiveDate.month, 1)
  if (!dayjs(dateInput).isAfter(dayjs('1900-01-01'))) {
    return false
  }
  return true
}

/**
 * Validates that the Cancel Date is not earlier than '1900-01-01' .
 */
export function validateMinimumCancelDate(dateInput) {
  if (!helpers.req(dateInput)) {
    return true
  }
  dateInput = new Date(this.cancelDate.year, this.cancelDate.month, 1)
  if (!dayjs(dateInput).isAfter(dayjs('1900-01-01'))) {
    return false
  }
  return true
}

/**
 * Validate Group Member Number. It can be up to nine (9) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid.
 */
export function validateGroupMemberNumber(groupMemberNumber) {
  return validateSpecialChars(groupMemberNumber, 9)
}

/**
 * Validate Department Number. It can be up to six (6) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid.
 */
export function validateDepartmentNumber(departmentNumber) {
  return validateSpecialChars(departmentNumber, 6)
}

/**
 * Only numbers 0 to 9 are valid. Phone Number must be entered as seven (10) numbers in length with no space or hyphen.
 */
export function validateTelephone(telephone) {
  if (!helpers.req(telephone)) {
    return true
  }
  return validateNumber(telephone, 10)
}

/*
 * Validates that the Contract Number is valid.
 */
export function validateContractNumber(contractNumber) {
  if (!helpers.req(contractNumber)) {
    return true
  }
  return validateNumber(contractNumber, 9)
}

/**
 * Validates that the Group Number is valid.
 */
export function validateGroupNumber(groupNumber) {
  if (!helpers.req(groupNumber)) {
    return true
  }
  if (!validateNumber(groupNumber, 7)) {
    return false
  }
  if (!validateMod10(groupNumber)) {
    return false
  }
  return true
}

/**
 * Validate that input is allowed length and that it contains no invalid characters
 */
export function validateOptionalAddress(address) {
  if (address === undefined || address === '') {
    return true
  }
  return validateSpecialCharactersForAddress(address, 25)
}

/**
 * Validate that input is allowed length and that it contains no invalid characters
 */
export function validateAddress(address) {
  if (!helpers.req(address)) {
    return true
  }
  return validateSpecialCharactersForAddress(address, 25)
}

/**
 * Validate that input is allowed length and that it contains no invalid characters
 */
function validateSpecialCharactersForAddress(input, length) {
  if (input.length > length) {
    return true
  }
  var invalidChars = /[,<>;:{}()*&^%$#@!~\\|\?_]/
  if (invalidChars.test(input)) {
    return false
  }
  return true
}

/**
 * Used to validate that Mailing Address line 1 must be completed if any other Mailing Address(Line 2, Line 3, Line 4, PostalCode) is complete
 */
export function validateMailingAddress() {
  return this.mailingAddress.addressLine2 !== '' || this.mailingAddress.addressLine3 !== '' || this.mailingAddress.addressLine4 !== '' || this.mailingAddress.postalCode !== ''
}

/**
 * Used to validate that Mailing Address line 1 must be completed if any other Mailing Address(Line 2, Line 3, PostalCode) is complete
 */
export function validateMailingAddressForVisaResident() {
  return this.mailingAddress2 !== '' || this.mailingAddress3 !== '' || this.mailingAddressPostalCode !== ''
}

/**
 * Validate that input is allowed length and that it contains no invalid characters
 */
function validateSpecialChars(input, length) {
  if (input.length > length) {
    return false
  }
  var invalidChars = /[\\|^\&]/
  if (invalidChars.test(input)) {
    return false
  }
  return true
}

export function validateSurname(surname) {
  if (!helpers.req(surname)) {
    return true
  }
  return validateAlpha(surname, 35)
}

export function validateFirstName(firstName) {
  if (!helpers.req(firstName)) {
    return true
  }
  return validateAlpha(firstName, 15)
}

export function validateSecondName(secondName) {
  if (secondName === undefined || secondName === '') {
    return true
  }
  return validateAlpha(secondName, 15)
}

/**
 * Validate that input is allowed length and that it contains only alphabets
 */
function validateAlpha(input, length) {
  if (input.length > length) {
    return true
  }
  return !/[^a-zA-Z]/.test(input)
}

/**
 * Validates that the input is an integer number of the specified length.
 */
function validateNumber(input, length) {
  if (input.length != length) {
    return false
  }
  if (isNaN(input)) {
    return false
  }
  const number = +input
  if (!Number.isInteger(number)) {
    return false
  }
  if (number < 0) {
    return false
  }
  return true
}

function validateMod10(input) {
  const numDigits = input.length
  let sum = 0
  let tmpDigit = 0

  if (input.length % 2 === 0) {
    // Even number of digits in String to check
    for (let i = 0; i < numDigits; i += 2) {
      // Odd Numbers
      tmpDigit = Number(input.charAt(i))
      sum += tmpDigit === 9 ? 9 : (tmpDigit * 2) % 9
      // Even Numbers
      sum += Number(input.charAt(i + 1))
    }
  } else {
    //Odd Number of digits in String to check
    for (let i = 1; i < numDigits; i += 2) {
      // Odd Numbers
      sum += Number(input.charAt(i - 1))
      // Even Numbers
      tmpDigit = Number(input.charAt(i))
      sum += tmpDigit === 9 ? 9 : (tmpDigit * 2) % 9
    }
    // Last odd Number
    sum += Number(input.charAt(numDigits - 1))
  }
  return sum % 10 === 0
}
export const VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE = 'Mailing Address Line 1 is required'
export const VALIDATE_ADDRESS_LINE1_MESSAGE = 'Address Line 1 is invalid'
export const VALIDATE_ADDRESS_LINE2_MESSAGE = 'Address Line 2 is invalid'
export const VALIDATE_ADDRESS_LINE3_MESSAGE = 'Address Line 3 is invalid'
export const VALIDATE_ADDRESS_LINE4_MESSAGE = 'Address Line 4 is invalid'
export const VALIDATE_SURNAME_MESSAGE = 'Surname is invalid'
export const VALIDATE_FIRST_NAME_MESSAGE = 'First Name is invalid'
export const VALIDATE_SECOND_NAME_MESSAGE = 'Second Name is invalid'
export const VALIDATE_CITY_MESSAGE = 'City is invalid'
export const VALIDATE_DOB_MESSAGE = 'Date of Birth must not be in the future'
export const VALIDATE_MINIMUM_DATE_MESSAGE = 'Date must be later than 19000101'
export const VALIDATE_PHN_MESSAGE = 'PHN format is invalid'
export const VALIDATE_CONTRACT_NUMBER_MESSAGE = 'MSP Contract Number is invalid'
export const VALIDATE_GROUP_NUMBER_MESSAGE = 'Group Number is invalid'
export const VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE = 'Group Member Number is invalid'
export const VALIDATE_DEPARTMENT_NUMBER_MESSAGE = 'Department Number is invalid'
export const VALIDATE_POSTAL_CODE_MESSAGE = 'Postal Code is invalid'
export const VALIDATE_TELEPHONE_MESSAGE = 'Telephone is invalid. Only numbers 0 to 9 are valid. Phone Number must be entered as ten (10) numbers in length with no space or hyphen.'
