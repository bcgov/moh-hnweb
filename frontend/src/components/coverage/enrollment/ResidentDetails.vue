<template>
  <div>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" tabindex="1" :value="resident?.phn" />
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Name" tabindex="2" :value="fullName" />
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Date of Birth" tabindex="3" :value="resident?.dateOfBirth" />
      </AppCol>
    </AppRow>
    <div v-if="deceased">
      <AppRow class="text-deceased">
        <AppCol class="col3">
          <AppOutput label="Date of Death" :value="resident?.dateOfDeath" />
        </AppCol>
      </AppRow>
    </div>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Gender" :value="resident?.gender" tabindex="4" />
      </AppCol>
    </AppRow>
    <form @submit.prevent="registerVisaResident">
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" size="200" label="Group Number" type="text" tabindex="5" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col5">
          <AppSelect :e-model="v$.immigrationCode" id="immigrationCode" label="Immigration Code" tabindex="6" v-model="immigrationCode" :options="immigrationCodeOptions" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number (Optional)" type="text" tabindex="7" v-model.trim="groupMemberNumber" />
        </AppCol>
        <AppCol class="col5">
          <AppDateInput :e-model="v$.visaIssueDate" id="visaIssueDate" label="Permit Issue Date" tabindex="8" v-model="visaIssueDate" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number (Optional)" type="text" tabindex="9" v-model.trim="departmentNumber" />
        </AppCol>
        <AppCol class="col5">
          <AppDateInput :e-model="v$.visaExpiryDate" id="visaExpiryDate" label="Permit Expiry Date" tabindex="10" v-model="visaExpiryDate" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppDateInput :e-model="v$.residenceDate" id="residenceDate" label="Residence Date" tabindex="11" v-model="residenceDate" />
        </AppCol>
        <AppCol class="col5">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id="coverageEffectiveDate" label="Coverage Effective Date" tabindex="12" v-model="coverageEffectiveDate" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.telephone" id="telephone" label="Telephone (Optional)" type="text" v-model.trim="telephone" tabindex="13" placeholder="1234567890" />
        </AppCol>
        <AppCol class="col5">
          <AppDateInput :e-model="v$.coverageCancellationDate" id="coverageCancellationDate" label="Coverage Cancellation Date" tabindex="14" v-model="coverageCancellationDate" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.address1" id="address1" label="Home Address Line 1" type="text" tabindex="15" v-model="address1" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress1" id="mailingAddress1" label="Mailing Address (if different from home address)" tabindex="22" v-model="mailingAddress1" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.address2" id="address2" label="Line 2 (Optional)" type="text" tabindex="16" v-model="address2" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress2" id="mailingAddress2" label="Line 2 (Optional)" tabindex="23" v-model="mailingAddress2" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.address3" id="address3" label="Line 3 (Optional)" type="text" tabindex="17" v-model="address3" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress3" id="mailingAddress3" label="Line 3 (Optional)" tabindex="24" v-model="mailingAddress3" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-nowrap">
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.city" id="city" label="City" type="text" tabindex="18" v-model.trim="city" />
          </AppCol>
          <AppCol class="col5"> <AppSelect :e-model="v$.province" id="province" label="Province" tabindex="19" v-model.trim="province" :options="provinceOptions" /> </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.mailingAddressCity" id="mailingAddressCity" label="City" type="text" tabindex="25" v-model.trim="mailingAddressCity" />
          </AppCol>
          <AppCol class="col5">
            <AppInput v-if="otherCountry" :e-model="v$.mailingAddressProvince" id="mailingAddressProvince" :label="regionLabel" type="text" tabindex="26" v-model.trim="mailingAddressProvince" />
            <AppSelect v-else :e-model="v$.mailingAddressProvince" id="mailingAddressProvince" :label="regionLabel" tabindex="27" v-model.trim="mailingAddressProvince" :options="regionOptions" />
          </AppCol>
        </AppRow>
      </AppRow>
      <AppRow class="flex-nowrap">
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.postalCode" id="postalCode" label="Postal Code" type="text" tabindex="20" v-model.trim="postalCode" />
          </AppCol>
          <AppCol class="col5">
            <AppInput :e-model="v$.country" id="country" label="Country" type="text" tabindex="21" disabled="disabled" v-model="country" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.mailingAddressPostalCode" id="mailingAddressPostalCode" :label="zipLabel" type="text" tabindex="28" v-model.trim="mailingAddressPostalCode" />
          </AppCol>
          <AppCol class="col5">
            <AppSelect id="mailingAddressCountry" label="Country" v-model="mailingAddressCountry" :options="countryOptions" tabindex="29" />
          </AppCol>
        </AppRow>
      </AppRow>
      <AppRow>
        <AppCol class="col5">
          <AppSelect :e-model="v$.priorResidenceCode" id="priorResidenceCode" label="Prior Residence Code" v-model="priorResidenceCode" tabindex="30" :options="priorResidenceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col5">
          <AppInput :e-model="v$.otherProvinceHealthcareNumber" id="otherProvinceHealthcareNumber" label="Other Province Healthcare Number (If Applicable) (Optional)" type="text" tabindex="31" v-model.trim="otherProvinceHealthcareNumber" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit" tabindex="32">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button" tabindex="33">Clear</AppButton>
      </AppRow>
    </form>
  </div>
</template>
<script>
import AppSelect from '../../ui/AppSelect.vue'
import useVuelidate from '@vuelidate/core'
import {
  validateGroupNumber,
  validateGroupMemberNumber,
  validateDepartmentNumber,
  validateTelephone,
  validateMailingPostalCode,
  validatePostalCode,
  validateAddress,
  validateOptionalAddress,
  validateCityOrProvince,
  validateMailingZipCode,
  validateMailingAddressForVisaResident,
  VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE,
  VALIDATE_ADDRESS_LINE1_MESSAGE,
  VALIDATE_ADDRESS_LINE2_MESSAGE,
  VALIDATE_ADDRESS_LINE3_MESSAGE,
  VALIDATE_CITY_MESSAGE,
  VALIDATE_CITY_REQUIRED_MESSAGE,
  VALIDATE_GROUP_NUMBER_MESSAGE,
  VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE,
  VALIDATE_DEPARTMENT_NUMBER_MESSAGE,
  VALIDATE_TELEPHONE_MESSAGE,
  VALIDATE_POSTAL_CODE_MESSAGE,
  VALIDATE_POSTAL_CODE_REQUIRED_MESSAGE,
  VALIDATE_PROVINCE_REQUIRED_MESSAGE,
  VALIDATE_OTHER_STATE_REQUIRED_MESSAGE,
  VALIDATE_OTHER_ZIP_CODE_REQUIRED_MESSAGE,
  VALIDATE_STATE_REQUIRED_MESSAGE,
  VALIDATE_ZIP_CODE_MESSAGE,
  VALIDATE_ZIP_CODE_REQUIRED_MESSAGE,
} from '../../../util/validators'
import { required, requiredIf, helpers, maxLength } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { API_DATE_FORMAT, IMMIGRATION_CODES, PROVINCES, PRIOR_RESIDENCES, STATES, COUNTRIES } from '../../../util/constants'
import { formatPersonName } from '../../../util/utils'
import { useAlertStore } from '../../../stores/alert'
import { useStudyPermitHolderStore } from '../../../stores/studyPermitHolder'

export default {
  name: 'ResidentDetails',
  components: {
    AppSelect,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      studyPermitHolderStore: useStudyPermitHolderStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      //Add Visa Resident Fields
      groupNumber: '',
      immigrationCode: '',
      groupMemberNumber: '',
      visaIssueDate: null,
      departmentNumber: '',
      visaExpiryDate: null,
      residenceDate: null,
      coverageEffectiveDate: dayjs().startOf('month').toDate(),
      telephone: '',
      coverageCancellationDate: null,
      address1: '',
      address2: '',
      address3: '',
      city: '',
      province: '',
      postalCode: '',
      country: 'Canada',
      mailingAddress1: '',
      mailingAddress2: '',
      mailingAddress3: '',
      mailingAddressCity: '',
      mailingAddressProvince: '',
      mailingAddressPostalCode: '',
      mailingAddressCountry: 'Canada',
      priorResidenceCode: '',
      otherProvinceHealthcareNumber: '',
    }
  },
  created() {
    // Immigration Code drop down options
    this.immigrationCodeOptions = IMMIGRATION_CODES
    // Province drop down options
    this.provinceOptions = PROVINCES
    // Prior Residence drop down options
    this.priorResidenceOptions = PRIOR_RESIDENCES
    // US State drop down options
    this.stateOptions = STATES
    // Country Options for mailing country
    this.countryOptions = COUNTRIES

    //populate data on component load

    this.address1 = this.resident.address1 ?? ''
    this.address2 = this.resident.address2 ?? ''
    this.address3 = this.resident.address3
    this.city = this.resident.city ?? ''
    this.province = this.resident.province ?? ''
    this.postalCode = this.homePostalCode ?? ''

    this.mailingAddress1 = this.resident.mailingAddress1 ?? ''
    this.mailingAddress2 = this.resident.mailingAddress2 ?? ''
    this.mailingAddress3 = this.resident.mailingAddress3 ?? ''
    this.mailingAddressCity = this.resident.mailingAddressCity ?? ''
    this.mailingAddressProvince = this.resident.mailingAddressProvince ?? ''
    this.mailingAddressPostalCode = this.mailingPostalCode ?? ''
  },
  computed: {
    resident() {
      return this.studyPermitHolderStore.resident
    },
    fullName() {
      return formatPersonName(this.resident)
    },
    homePostalCode() {
      if (this.resident?.postalCode != undefined) {
        return this.resident.postalCode.replace(/\s+/, '')
      } else {
        return ''
      }
    },
    mailingPostalCode() {
      if (this.resident?.mailingAddressPostalCode != undefined) {
        return this.resident.mailingAddressPostalCode.replace(/\s+/, '')
      } else {
        return ''
      }
    },
    regionLabel() {
      if (this.mailingAddressCountry === 'United States') {
        return 'State'
      } else if (this.mailingAddressCountry === 'Canada') {
        return 'Province'
      }
      return 'Province / Region / State'
    },
    zipLabel() {
      if (this.mailingAddressCountry === 'United States') {
        return 'ZIP Code'
      } else if (this.mailingAddressCountry === 'Canada') {
        return 'Postal Code'
      }
      return 'Postal / Zip Code'
    },
    regionOptions() {
      if (this.mailingAddressCountry === 'United States') {
        return this.stateOptions
      } else if (this.mailingAddressCountry === 'Canada') {
        return this.provinceOptions
      }
    },
    otherCountry() {
      return this.mailingAddressCountry === 'Other'
    },
  },
  watch: {
    mailingAddressCountry() {
      this.mailingAddressProvince = ''
    },
    deceased() {
      return this.resident?.dateOfDeath && this.resident.dateOfDeath != 'N/A'
    },
  },
  props: {
    submitting: {
      required: true,
      type: Boolean,
    },
  },
  methods: {
    async registerVisaResident() {
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.alertStore.setErrorAlert()
          return
        }
        this.$emit('register-resident', {
          phn: this.resident?.phn,
          dateOfBirth: dayjs(this.resident?.dateOfBirth).format(API_DATE_FORMAT),
          givenName: this.resident?.givenName,
          secondName: this.resident?.secondName,
          surname: this.resident?.surname,
          gender: this.resident?.gender,
          groupNumber: this.groupNumber,
          immigrationCode: this.immigrationCode,
          groupMemberNumber: this.groupMemberNumber,
          visaIssueDate: dayjs(this.visaIssueDate).format(API_DATE_FORMAT),
          departmentNumber: this.departmentNumber,
          visaExpiryDate: dayjs(this.visaExpiryDate).format(API_DATE_FORMAT),
          residenceDate: dayjs(this.residenceDate).format(API_DATE_FORMAT),
          coverageEffectiveDate: dayjs(this.coverageEffectiveDate).format(API_DATE_FORMAT),
          telephone: this.telephone,
          coverageCancellationDate: dayjs(this.coverageCancellationDate).format(API_DATE_FORMAT),
          address1: this.address1,
          address2: this.address2,
          address3: this.address3,
          city: this.city,
          province: this.province,
          postalCode: this.postalCode,
          mailingAddress1: this.mailingAddress1,
          mailingAddress2: this.mailingAddress2,
          mailingAddress3: this.mailingAddress3,
          mailingAddressCity: this.mailingAddressCity,
          mailingAddressProvince: this.mailingAddressProvince,
          mailingAddressPostalCode: this.mailingAddressPostalCode,
          priorResidenceCode: this.priorResidenceCode,
          otherProvinceHealthcareNumber: this.otherProvinceHealthcareNumber,
        })
      } catch (err) {
        this.alertStore.setErrorAlert(err)
      }
    },
    resetForm() {
      this.groupNumber = ''
      this.immigrationCode = ''
      this.groupMemberNumber = ''
      this.visaIssueDate = null
      this.departmentNumber = ''
      this.visaExpiryDate = null
      this.residenceDate = null
      this.coverageEffectiveDate = dayjs().startOf('month').toDate()
      this.telephone = ''
      this.coverageCancellationDate = null
      this.address1 = ''
      this.address2 = ''
      this.address3 = ''
      this.city = ''
      this.province = ''
      this.postalCode = ''
      this.country = 'Canada'
      this.mailingAddress1 = ''
      this.mailingAddress2 = ''
      this.mailingAddress3 = ''
      this.mailingAddressCity = ''
      this.mailingAddressProvince = ''
      this.mailingAddressPostalCode = ''
      this.mailingAddressCountry = 'Canada'
      this.priorResidenceCode = ''
      this.otherProvinceHealthcareNumber = ''
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
    regionFieldRequiredValidationMessage() {
      if (this.mailingAddressCountry === 'United States') {
        return VALIDATE_STATE_REQUIRED_MESSAGE
      } else if (this.mailingAddressCountry === 'Canada') {
        return VALIDATE_PROVINCE_REQUIRED_MESSAGE
      }
      return VALIDATE_OTHER_STATE_REQUIRED_MESSAGE
    },
    zipFieldRequiredValidationMessage() {
      if (this.mailingAddressCountry === 'United States') {
        return VALIDATE_ZIP_CODE_REQUIRED_MESSAGE
      } else if (this.mailingAddressCountry === 'Canada') {
        return VALIDATE_POSTAL_CODE_REQUIRED_MESSAGE
      }
      return VALIDATE_OTHER_ZIP_CODE_REQUIRED_MESSAGE
    },
    regionFieldInvalidValidationMessage() {
      if (this.mailingAddressCountry === 'United States') {
        return VALIDATE_STATE_MESSAGE
      }
      return VALIDATE_PROVINCE_MESSAGE
    },
    zipFieldInvalidValidationMessage() {
      if (this.mailingAddressCountry === 'United States') {
        return VALIDATE_ZIP_CODE_MESSAGE
      }
      return VALIDATE_POSTAL_CODE_MESSAGE
    },
    validateZipOrPostalCode(zipOrPostalCode) {
      if (this.mailingAddressCountry === 'United States') {
        return validateMailingZipCode(zipOrPostalCode)
      } else if (this.mailingAddressCountry === 'Canada') {
        return validateMailingPostalCode(zipOrPostalCode)
      }
      return true
    },
    validateMailingCity(city) {
      if (this.otherCountry) {
        return true
      } else {
        return validateCityOrProvince(city)
      }
    },
  },
  validations() {
    return {
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
      immigrationCode: { required },
      groupMemberNumber: {
        validateGroupMemberNumber: helpers.withMessage(VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE, validateGroupMemberNumber),
      },
      visaIssueDate: {
        required,
      },
      departmentNumber: {
        validateDepartmentNumber: helpers.withMessage(VALIDATE_DEPARTMENT_NUMBER_MESSAGE, validateDepartmentNumber),
      },
      visaExpiryDate: {
        required,
      },
      residenceDate: {
        required,
      },
      coverageEffectiveDate: { required },
      telephone: {
        validateTelephone: helpers.withMessage(VALIDATE_TELEPHONE_MESSAGE, validateTelephone),
      },
      coverageCancellationDate: {
        required,
      },
      address1: {
        required,
        maxLength: maxLength(25),
        validateAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE1_MESSAGE, validateAddress),
      },
      address2: {
        maxLength: maxLength(25),
        validateAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE2_MESSAGE, validateAddress),
      },
      address3: {
        maxLength: maxLength(25),
        validateAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE3_MESSAGE, validateAddress),
      },
      city: {
        required,
        maxLength: maxLength(25),
        validateCityOrProvince: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateCityOrProvince),
      },
      province: { required },
      postalCode: {
        required,
        validatePostalCode: helpers.withMessage(VALIDATE_POSTAL_CODE_MESSAGE, validatePostalCode),
      },
      mailingAddress1: {
        required: helpers.withMessage(VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE, requiredIf(validateMailingAddressForVisaResident)),
        maxLength: maxLength(25),
        validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE1_MESSAGE, validateOptionalAddress),
      },
      mailingAddress2: {
        maxLength: maxLength(25),
        validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE2_MESSAGE, validateOptionalAddress),
      },
      mailingAddress3: {
        maxLength: maxLength(25),
        validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE3_MESSAGE, validateOptionalAddress),
      },
      mailingAddressCity: {
        required: helpers.withMessage(VALIDATE_CITY_REQUIRED_MESSAGE, requiredIf(validateMailingAddressForVisaResident)),
        maxLength: maxLength(25),
        validateMailingCity: helpers.withMessage(VALIDATE_CITY_MESSAGE, this.validateMailingCity),
      },
      mailingAddressProvince: {
        required: helpers.withMessage(this.regionFieldRequiredValidationMessage, requiredIf(validateMailingAddressForVisaResident)),
      },
      mailingAddressPostalCode: {
        required: helpers.withMessage(this.zipFieldRequiredValidationMessage, requiredIf(validateMailingAddressForVisaResident)),
        validateMailingPostalCode: helpers.withMessage(this.zipFieldInvalidValidationMessage, this.validateZipOrPostalCode),
      },
      priorResidenceCode: { required },
      otherProvinceHealthcareNumber: {},
    }
  },
}
</script>
<style scoped>
.text-deceased {
  color: red;
  font-weight: bold;
}
</style>
