<template>
  <div>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="resident?.phn" />
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Name" :value="fullName" />
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Date of Birth" :value="resident?.dateOfBirth" />
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
        <AppOutput label="Gender" :value="resident?.gender" />
      </AppCol>
    </AppRow>
    <form @submit.prevent="registerVisaResident">
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" size="200" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppSelect :e-model="v$.immigrationCode" id="immigrationCode" label="Immigration Code" v-model="immigrationCode" :options="immigrationCodeOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number (Optional)" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.visaIssueDate" id="visaIssueDate" label="Permit Issue Date" v-model="visaIssueDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number (Optional)" type="text" v-model.trim="departmentNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.visaExpiryDate" id="visaExpiryDate" label="Permit Expiry Date" v-model="visaExpiryDate" />
        </AppCol>
      </AppRow>
      <AppRow class="row-center">
        <AppCol class="col4">
          <AppDateInput :e-model="v$.residenceDate" id="residenceDate" label="Residence Date" v-model="residenceDate" />
        </AppCol>
      </AppRow>
      <AppRow class="row-center">
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id="coverageEffectiveDate" label="Coverage Effective Date" v-model="coverageEffectiveDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.telephone" id="telephone" label="Telephone (Optional)" type="text" v-model.trim="telephone" placeholder="1234567890" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageCancellationDate" id="coverageCancellationDate" label="Coverage Cancellation Date" v-model="coverageCancellationDate" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.address1" id="address1" label="Home Address Line 1" type="text" v-model="address1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.address2" id="address2" label="Line 2 (Optional)" type="text" v-model="address2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.address3" id="address3" label="Line 3 (Optional)" type="text" v-model="address3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.city" id="city" label="City" type="text" v-model.trim="city" />
        </AppCol>
        <AppCol class="col4">
          <AppSelect :e-model="v$.province" id="province" label="Province" v-model="province" :options="provinceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="postalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress1" id="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress2" id="mailingAddress2" label="Line 2 (Optional)" v-model="mailingAddress2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress3" id="mailingAddress3" label="Line 3 (Optional)" v-model="mailingAddress3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressCity" id="mailingAddressCity" label="City" type="text" v-model.trim="mailingAddressCity" />
        </AppCol>
        <AppCol class="col4">
          <AppSelect :e-model="v$.mailingAddressProvince" id="mailingAddressProvince" label="Province" v-model="mailingAddressProvince" :options="provinceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressPostalCode" id="mailingAddressPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddressPostalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col4">
          <AppSelect :e-model="v$.priorResidenceCode" id="priorResidenceCode" label="Prior Residence Code" v-model="priorResidenceCode" :options="priorResidenceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.otherProvinceHealthcareNumber" id="otherProvinceHealthcareNumber" label="Other Province Healthcare Number (If Applicable) (Optional)" type="text" v-model.trim="otherProvinceHealthcareNumber" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit" :disabled="deceased">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
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
  validateMailingAddressForVisaResident,
  VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE,
  VALIDATE_ADDRESS_LINE1_MESSAGE,
  VALIDATE_ADDRESS_LINE2_MESSAGE,
  VALIDATE_ADDRESS_LINE3_MESSAGE,
  VALIDATE_CITY_MESSAGE,
  VALIDATE_GROUP_NUMBER_MESSAGE,
  VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE,
  VALIDATE_DEPARTMENT_NUMBER_MESSAGE,
  VALIDATE_TELEPHONE_MESSAGE,
  VALIDATE_POSTAL_CODE_MESSAGE,
} from '../../../util/validators'
import { required, requiredIf, helpers, maxLength } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { API_DATE_FORMAT, IMMIGRATION_CODES, PROVINCES, PRIOR_RESIDENCES } from '../../../util/constants'
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
      mailingAddress1: '',
      mailingAddress2: '',
      mailingAddress3: '',
      mailingAddressCity: '',
      mailingAddressProvince: '',
      mailingAddressPostalCode: '',
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

    //populate data on component load

    this.address1 = this.resident?.address1
    this.address2 = this.resident?.address2
    this.address3 = this.resident.address3
    this.city = this.resident?.city
    this.province = this.resident.province
    this.postalCode = this.resident.postalCode

    this.mailingAddress1 = this.resident?.mailingAddress1
    this.mailingAddress2 = this.resident?.mailingAddress2
    this.mailingAddress3 = this.resident.mailingAddress3
    this.mailingAddressCity = this.resident?.mailingAddressCity
    this.mailingAddressProvince = this.resident.mailingAddressProvince
    this.mailingAddressPostalCode = this.resident.mailingAddressPostalCode
  },
  computed: {
    resident() {
      return this.studyPermitHolderStore.resident
    },
    fullName() {
      return formatPersonName(this.resident)
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
      this.mailingAddress1 = ''
      this.mailingAddress2 = ''
      this.mailingAddress3 = ''
      this.mailingAddressCity = ''
      this.mailingAddressProvince = ''
      this.mailingAddressPostalCode = ''
      this.priorResidenceCode = ''
      this.otherProvinceHealthcareNumber = ''
      this.v$.$reset()
      this.alertStore.dismissAlert()
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
        validateAddress: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateAddress),
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
        maxLength: maxLength(25),
        validateOptionalAddress: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateOptionalAddress),
      },
      mailingAddressProvince: {},
      mailingAddressPostalCode: {
        validateMailingPostalCode: helpers.withMessage(VALIDATE_POSTAL_CODE_MESSAGE, validateMailingPostalCode),
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
