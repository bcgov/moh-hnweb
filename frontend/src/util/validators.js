import { helpers } from '@vuelidate/validators'

export function validatePHN(phn) {
  if (!helpers.req(phn)) {
    return true
  }

  const PHNsigDigits = [2, 4, 8, 5, 10, 9, 7, 3]
  let checksum = 0
  let digit = 0

  if (phn.length != 10 || phn.charAt(0) != '9') {
    return false
  } else {
    for (let i = 1; i < 9; i++) {
        digit = Number(phn.charAt(i))
        checksum += (digit * PHNsigDigits[i - 1]) % 11
    }
    checksum = 11 - checksum % 11
    if (Number(phn.charAt(9)) != checksum) {
      return false
    }
  }
  return true;
}

export const VALIDATE_PHN_MESSAGE = "PHN format is invalid"