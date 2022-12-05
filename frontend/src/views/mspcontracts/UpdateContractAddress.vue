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
      <AppRow class="flex-gap">
        <AppCol class="col5">
          <AppInput :e-model="v$.homeAddress.addressLine1" id="addressLine1" label="Home Address Line 1" type="text" v-model="homeAddress.addressLine1" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress.addressLine1" id="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap">
        <AppCol class="col5">
          <AppInput :e-model="v$.homeAddress.addressLine2" id="addressLine2" label="Line 2 (Optional)" type="text" v-model="homeAddress.addressLine2" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress.addressLine2" id="mailingAddress2" label="Line 2 (Optional)" v-model="mailingAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap">
        <AppCol class="col5">
          <AppRow>
            <AppCol>
              <AppInput :e-model="v$.homeAddress.city" id="city" label="City" type="text" v-model.trim="homeAddress.city" />
            </AppCol>
            <AppCol>
              <AppInput :e-model="v$.homeAddress.province" id="province" label="Province" type="text" v-model.trim="homeAddress.province" />
            </AppCol>
          </AppRow>
        </AppCol>
        <AppCol class="col5">
          <AppRow>
            <AppCol>
              <AppInput :e-model="v$.mailingAddress.city" id="mailingCity" label="City" type="text" v-model.trim="mailingAddress.city" />
            </AppCol>
            <AppCol>
              <AppInput :e-model="v$.mailingAddress.province" id="mailingProvince" label="Province" type="text" v-model.trim="mailingAddress.province" />
            </AppCol>
          </AppRow>
        </AppCol>
      </AppRow>
      <AppRow class="flex-gap">
        <AppCol class="col5">
          <AppRow>
            <AppCol>
              <AppInput :e-model="v$.homeAddress.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="homeAddress.postalCode" />
            </AppCol>
            <AppCol>
              <AppSelect id="country" label="Country" v-model="homeAddress.country" :options="countryOptions" />
            </AppCol>
          </AppRow>
        </AppCol>
        <AppCol class="col5"
          ><AppRow>
            <AppCol>
              <AppInput :e-model="v$.mailingAddress.postalCode" id="mailingPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddress.postalCode" />
            </AppCol>
            <AppCol>
              <AppSelect id="mailingCountry" label="Country" v-model="mailingAddress.country" :options="countryOptions" />
            </AppCol>
          </AppRow>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm()" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="confirmation" v-if="updateOk">
    <h2 class="result-text">Transaction Completed for:</h2>
    <div>
      <div><span class="result-text"> Group Number: </span>{{ groupNumber }}</div>
      <div><span class="result-text">PHN: </span>{{ result?.phn }}</div>
    </div>
    <AppButton @click="resetForm" mode="primary" type="button">Update Another Contract Address</AppButton>
  </div>
</template>
<script>
import AppHelp from '../../components/ui/AppHelp.vue'
import useVuelidate from '@vuelidate/core'
import { required, requiredIf, helpers, maxLength } from '@vuelidate/validators'
import { COUNTRIES } from '../../util/constants'
import {
  validateGroupNumber,
  validatePHN,
  validatePostalCode,
  validateMailingPostalCode,
  validateAddress,
  validateOptionalAddress,
  validateMailingAddress,
  validateTelephone,
  VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE,
  VALIDATE_ADDRESS_LINE1_MESSAGE,
  VALIDATE_ADDRESS_LINE2_MESSAGE,
  VALIDATE_GROUP_NUMBER_MESSAGE,
  VALIDATE_PHN_MESSAGE,
  VALIDATE_POSTAL_CODE_MESSAGE,
  VALIDATE_TELEPHONE_MESSAGE,
  VALIDATE_CITY_REQUIRED_MESSAGE,
  VALIDATE_CITY_MESSAGE,
  VALIDATE_PROVINCE_REQUIRED_MESSAGE,
  VALIDATE_PROVINCE_MESSAGE,
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
  },
  data() {
    return {
      submitting: false,
      updateOk: false,
      updateMode: true,
      //Update Contract Address Fields
      phn: '',
      groupNumber: '',
      telephone: '',
      homeAddress: {
        addressLine1: '',
        addressLine2: '',
        postalCode: '',
        country: '',
        city: '',
        province: '',
      },
      mailingAddress: {
        addressLine1: '',
        addressLine2: '',
        postalCode: '',
        country: '',
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
              addressLine2: this.homeAddress.addressLine2 === '' ? this.homeAddress.city + ' ' + this.homeAddress.province : this.homeAddress.addressLine2,
              addressLine3: this.homeAddress.addressLine2 === '' ? '' : this.homeAddress.city + ' ' + this.homeAddress.province,
              postalCode: this.homeAddress.postalCode,
            },
            mailingAddress: {
              addressLine1: this.mailingAddress.addressLine1,
              addressLine2: this.mailingAddress.addressLine2 === '' ? this.mailingAddress.city + ' ' + this.mailingAddress.province : this.mailingAddress.addressLine2,
              addressLine3: this.mailingAddress.addressLine2 === '' ? '' : this.mailingAddress.city + ' ' + this.mailingAddress.province,
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
      this.homeAddress.city = ''
      this.homeAddress.province = ''
      this.homeAddress.postalCode = ''
      this.mailingAddress.addressLine1 = ''
      this.mailingAddress.addressLine2 = ''
      this.mailingAddress.city = ''
      this.mailingAddress.province = ''
      this.mailingAddress.postalCode = ''
      this.result = null
      this.v$.$reset()
      this.alertStore.dismissAlert()
      this.submitting = false
      this.updateOk = false
      this.updateMode = true
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
          validateAddress: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateAddress),
        },
        province: {
          required,
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_PROVINCE_MESSAGE, validateAddress),
        },
      },
      mailingAddress: {
        addressLine1: {
          required: helpers.withMessage(VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE, requiredIf(validateMailingAddress)),
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE1_MESSAGE, validateOptionalAddress),
        },
        addressLine2: {
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE2_MESSAGE, validateOptionalAddress),
        },
        postalCode: {
          validateMailingPostalCode: helpers.withMessage(VALIDATE_POSTAL_CODE_MESSAGE, validateMailingPostalCode),
        },
        city: {
          required: helpers.withMessage(VALIDATE_CITY_REQUIRED_MESSAGE, requiredIf(validateMailingAddress)),
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateAddress),
        },
        province: {
          required: helpers.withMessage(VALIDATE_PROVINCE_REQUIRED_MESSAGE, requiredIf(validateMailingAddress)),
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_PROVINCE_MESSAGE, validateAddress),
        },
      },
    }
  },
}
</script>
<style>
.result-text {
  font-weight: bold;
  color: #003366;
}
</style>
