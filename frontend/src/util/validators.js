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
  return validateNumber(contractNumber, 6)
}

/**
 * Only numbers 0 to 9 are valid. Phone Number must be entered as seven (10) numbers in length with no space or hyphen.
 */
 export function validateTelephone(value) {
  return validateNumber(contractNumber, 10)
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
 * Validates that the input is an integer number of the specified length.
 */
function validateNumber(input, length) {
  console.log('validateNumber')
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
  console.log('validateMod10')
	const numDigits = input.length
	let sum = 0
	let tmpDigit = 0

	if (input.length % 2 === 0) {
    // Even number of digits in String to check
		for (let i = 0; i < numDigits; i += 2 ) {
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
  return (sum % 10) === 0
}

export const VALIDATE_DOB_MESSAGE = "Date of Birth must not be in the future"
export const VALIDATE_PHN_MESSAGE = "PHN format is invalid"
export const VALIDATE_CONTRACT_NUMBER_MESSAGE = "MSP Contract Number is invalid"
export const VALIDATE_GROUP_NUMBER_MESSAGE = "Group Number is invalid"
export const VALIDATE_GROUP_NUMBER_MESSAGE = "Only digits 0 to 9 are valid. Group Number must be entered as seven (7) digits in length."
export const VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE = "Can be up to nine (9) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid."
export const VALIDATE_DEPARTMENT_NUMBER_MESSAGE = "Can be up to nine (6) characters. Any alpha or numeric characters are allowed, except for |^ \ & which are invalid."
export const VALIDATE_TELEPHONE_MESSAGE = "Only numbers 0 to 9 are valid. Phone Number must be entered as ten (10) numbers in length with no space or hyphen."
