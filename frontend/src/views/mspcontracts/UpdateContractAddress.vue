<template>
  <AppHelp>
    <p>Use this screen to update the Home or Mailing Address and/or the telephone number recorded by MSP for a group member.</p>
    <p>NOTE: this screen does not call any postal validation software.</p>
    <p>The Home Address must be in British Columbia and the Postal Code must start with V. The Mailing Address is optional, it can be filled out if it differs from the Home Address.</p>
    <p>A complete transaction will display the PHN. You may wish to copy the PHN and click on the "Get Contract Address" menu option to verify that the correct address or phone number has been added.</p>
  </AppHelp>
  <div id="updateContractAddress" v-if="updateMode">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.telephone" id="telephone" label="Telephone (Optional)" type="text" v-model.trim="telephone" placeholder="1234567890" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.homeAddress.addressLine1" id="addressLine1" label="Home Address Line 1" type="text" v-model="homeAddress.addressLine1" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress.addressLine1" id="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.homeAddress.addressLine2" id="addressLine2" label="Line 2 (Optional)" type="text" v-model="homeAddress.addressLine2" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress.addressLine2" id="mailingAddress2" label="Line 2 (Optional)" v-model="mailingAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap-100">
        <AppCol class="col5">
          <AppInput :e-model="v$.homeAddress.addressLine3" id="addressLine3" label="Line 3 (Optional)" type="text" v-model="homeAddress.addressLine3" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress.addressLine3" id="mailingAddress3" label="Line 3 (Optional)" v-model="mailingAddress.addressLine3" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-nowrap">
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.city" id="city" label="City" type="text" v-model.trim="homeAddress.city" />
          </AppCol>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.province" id="province" label="Province" type="text" disabled="disabled" v-model.trim="homeAddress.province" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.mailingAddress.city" id="mailingAddressCity" label="City" type="text" v-model.trim="mailingAddress.city" />
          </AppCol>
          <AppCol class="col5">
            <AppInput v-if="otherCountry" :e-model="v$.mailingAddress.province" id="mailingAddressProvince" :label="regionLabel" type="text" v-model.trim="mailingAddress.province" />
            <AppSelect v-else :e-model="v$.mailingAddress.province" id="mailingAddressProvince" :label="regionLabel" v-model.trim="mailingAddress.province" :options="regionOptions" />
          </AppCol>
        </AppRow>
      </AppRow>
      <AppRow class="flex-nowrap">
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="homeAddress.postalCode" />
          </AppCol>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.country" id="country" label="Country" type="text" disabled="disabled" v-model.trim="homeAddress.country" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.mailingAddress.postalCode" id="mailingPostalCode" :label="zipLabel" type="text" v-model.trim="mailingAddress.postalCode" />
          </AppCol>
          <AppCol class="col5">
            <AppSelect id="mailingAddressCountry" label="Country" v-model="mailingAddress.country" :options="countryOptions" />
          </AppCol>
        </AppRow>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm()" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="confirmation" v-if="updateOk">
    <p><label>Transaction completed for:</label></p>
    <ul>
      <li><label>Group Number: </label>{{ groupNumber }}</li>
      <li><label>PHN: </label>{{ result?.phn }}</li>
    </ul>
    <br />
    <AppButton @click="resetForm" mode="primary" type="button">Update Another Contract Address</AppButton>
  </div>
</template>
<script>
import AppHelp from '../../components/ui/AppHelp.vue'
import useVuelidate from '@vuelidate/core'
import { required, requiredIf, helpers, maxLength } from '@vuelidate/validators'
import { COUNTRIES, PROVINCES, STATES } from '../../util/constants'
import {
  validateGroupNumber,
  validatePHN,
  validatePostalCode,
  validateMailingZipCode,
  validateCityOrProvince,
  validateMailingPostalCode,
  validateAddress,
  validateOptionalAddress,
  validateMailingAddressForMSPContracts,
  validateTelephone,
  VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE,
  VALIDATE_ADDRESS_LINE1_MESSAGE,
  VALIDATE_ADDRESS_LINE2_MESSAGE,
  VALIDATE_ADDRESS_LINE3_MESSAGE,
  VALIDATE_GROUP_NUMBER_MESSAGE,
  VALIDATE_PHN_MESSAGE,
  VALIDATE_POSTAL_CODE_MESSAGE,
  VALIDATE_POSTAL_CODE_REQUIRED_MESSAGE,
  VALIDATE_ZIP_CODE_MESSAGE,
  VALIDATE_ZIP_CODE_REQUIRED_MESSAGE,
  VALIDATE_TELEPHONE_MESSAGE,
  VALIDATE_CITY_REQUIRED_MESSAGE,
  VALIDATE_CITY_MESSAGE,
  VALIDATE_PROVINCE_REQUIRED_MESSAGE,
  VALIDATE_PROVINCE_MESSAGE,
  VALIDATE_STATE_MESSAGE,
  VALIDATE_STATE_REQUIRED_MESSAGE,
  VALIDATE_OTHER_ZIP_CODE_REQUIRED_MESSAGE,
  VALIDATE_OTHER_STATE_REQUIRED_MESSAGE,
} from '../../util/validators'
import MspContractsService from '../../services/MspContractsService'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'UpdateContractAddress',
  components: {
    AppHelp,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  created() {
    this.countryOptions = COUNTRIES
    this.provinceOptions = PROVINCES
    this.stateOptions = STATES
  },
  data() {
    return {
      submitting: false,
      updateOk: false,
      updateMode: true,
      otherCountry: false,
      //Update Contract Address Fields
      phn: '',
      groupNumber: '',
      telephone: '',
      homeAddress: {
        addressLine1: '',
        addressLine2: '',
        addressLine3: '',
        postalCode: '',
        country: 'Canada',
        city: '',
        province: 'BC',
      },
      mailingAddress: {
        addressLine1: '',
        addressLine2: '',
        addressLine3: '',
        postalCode: '',
        country: 'Canada',
        city: '',
        province: '',
      },
      result: {
        phn: '',
        status: '',
        message: '',
      },
    }
  },
  computed: {
    regionLabel() {
      if (this.mailingAddress.country === 'United States') {
        return 'State'
      } else if (this.mailingAddress.country === 'Canada') {
        return 'Province'
      }
      return 'Province / Region / State'
    },
    zipLabel() {
      if (this.mailingAddress.country === 'United States') {
        return 'ZIP Code'
      } else if (this.mailingAddress.country === 'Canada') {
        return 'Postal Code'
      }
      return 'Postal / Zip Code'
    },
    regionOptions() {
      if (this.mailingAddress.country === 'United States') {
        return this.stateOptions
      } else if (this.mailingAddress.country === 'Canada') {
        return this.provinceOptions
      }
    },
  },
  watch: {
    'mailingAddress.country'(newValue) {
      if (newValue === 'Other') {
        this.mailingAddress.province = ''
        this.otherCountry = true
      } else {
        this.mailingAddress.province = ''
        this.otherCountry = false
      }
    },
  },
  methods: {
    async submitForm() {
      this.submitting = true
      this.updateOk = false
      this.updateMode = true
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (
          await MspContractsService.updateContractAddress({
            phn: this.phn,
            groupNumber: this.groupNumber,
            phone: this.telephone,
            homeAddress: {
              addressLine1: this.homeAddress.addressLine1,
              addressLine2: this.homeAddress.addressLine2,
              addressLine3: this.homeAddress.addressLine3,
              addressLine4: this.homeAddress.city !== '' ? this.homeAddress.city + ' ' + this.homeAddress.province : '',
              postalCode: this.homeAddress.postalCode,
            },
            mailingAddress: {
              addressLine1: this.mailingAddress.addressLine1,
              addressLine2: this.mailingAddress.addressLine2,
              addressLine3: this.mailingAddress.addressLine3,
              addressLine4: this.mailingAddress.city !== '' ? this.mailingAddress.city + ' ' + this.mailingAddress.province : '',
              postalCode: this.mailingAddress.postalCode,
            },
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.updateMode = false
          this.updateOk = true
          this.alertStore.setInfoAlert(this.result.message)
          return
        }
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.submitting = false
      }
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.searching = false
    },
    resetForm() {
      this.groupNumber = ''
      this.phn = ''
      this.telephone = ''
      this.homeAddress.addressLine1 = ''
      this.homeAddress.addressLine2 = ''
      this.homeAddress.addressLine3 = ''
      this.homeAddress.city = ''
      this.homeAddress.province = 'BC'
      this.homeAddress.country = 'Canada'
      this.homeAddress.postalCode = ''
      this.mailingAddress.addressLine1 = ''
      this.mailingAddress.addressLine2 = ''
      this.mailingAddress.addressLine3 = ''
      this.mailingAddress.city = ''
      this.mailingAddress.province = ''
      this.mailingAddress.postalCode = ''
      this.mailingAddress.country = 'Canada'
      this.result = null
      this.v$.$reset()
      this.alertStore.dismissAlert()
      this.submitting = false
      this.updateOk = false
      this.updateMode = true
    },
    regionFieldRequiredValidationMessage() {
      if (this.mailingAddress.country === 'United States') {
        return VALIDATE_STATE_REQUIRED_MESSAGE
      } else if (this.mailingAddress.country === 'Canada') {
        return VALIDATE_PROVINCE_REQUIRED_MESSAGE
      }
      return VALIDATE_OTHER_STATE_REQUIRED_MESSAGE
    },
    zipFieldRequiredValidationMessage() {
      if (this.mailingAddress.country === 'United States') {
        return VALIDATE_ZIP_CODE_REQUIRED_MESSAGE
      } else if (this.mailingAddress.country === 'Canada') {
        return VALIDATE_POSTAL_CODE_REQUIRED_MESSAGE
      }
      return VALIDATE_OTHER_ZIP_CODE_REQUIRED_MESSAGE
    },
    regionFieldInvalidValidationMessage() {
      if (this.mailingAddress.country === 'United States') {
        return VALIDATE_STATE_MESSAGE
      }
      return VALIDATE_PROVINCE_MESSAGE
    },
    zipFieldInvalidValidationMessage() {
      if (this.mailingAddress.country === 'United States') {
        return VALIDATE_ZIP_CODE_MESSAGE
      }
      return VALIDATE_POSTAL_CODE_MESSAGE
    },
    validateZipOrPostalCode(zipOrPostalCode) {
      if (this.mailingAddress.country === 'United States') {
        return validateMailingZipCode(zipOrPostalCode)
      } else if (this.mailingAddress.country === 'Canada') {
        return validateMailingPostalCode(zipOrPostalCode)
      }
      return true
    },
  },

  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
      telephone: {
        validateTelephone: helpers.withMessage(VALIDATE_TELEPHONE_MESSAGE, validateTelephone),
      },
      homeAddress: {
        addressLine1: {
          required,
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE1_MESSAGE, validateAddress),
        },
        addressLine2: {
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE2_MESSAGE, validateAddress),
        },
        postalCode: {
          required,
          validatePostalCode: helpers.withMessage(VALIDATE_POSTAL_CODE_MESSAGE, validatePostalCode),
        },
        city: {
          required,
          maxLength: maxLength(25),
          validateCityOrProvince: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateCityOrProvince),
        },
      },
      mailingAddress: {
        addressLine1: {
          required: helpers.withMessage(VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE, requiredIf(validateMailingAddressForMSPContracts)),
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE1_MESSAGE, validateOptionalAddress),
        },
        addressLine2: {
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE2_MESSAGE, validateOptionalAddress),
        },
        addressLine3: {
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE3_MESSAGE, validateOptionalAddress),
        },
        postalCode: {
          required: helpers.withMessage(this.zipFieldRequiredValidationMessage, requiredIf(validateMailingAddressForMSPContracts)),
          validateMailingPostalCode: helpers.withMessage(this.zipFieldInvalidValidationMessage, this.validateZipOrPostalCode),
        },
        city: {
          required: helpers.withMessage(VALIDATE_CITY_REQUIRED_MESSAGE, requiredIf(validateMailingAddressForMSPContracts)),
          maxLength: maxLength(25),
          validateCityOrProvince: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateCityOrProvince),
        },
        province: {
          required: helpers.withMessage(this.regionFieldRequiredValidationMessage, requiredIf(validateMailingAddressForMSPContracts)),
          maxLength: maxLength(25),
          validateCityOrProvince: helpers.withMessage(this.regionFieldInvalidValidationMessage, validateCityOrProvince),
        },
      },
    }
  },
}
</script>
