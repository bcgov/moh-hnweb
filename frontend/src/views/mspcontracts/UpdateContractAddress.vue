<template>
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
        <AppCol class="col6">
          <AppInput :e-model="v$.homeAddress.addressLine1" id="addressLine1" label="Home Address Line 1" type="text" v-model="homeAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput id="addressLine2" label="Line 2 (Optional)" type="text" v-model="homeAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput id="addressLine3" label="Line 3 (Optional)" type="text" v-model="homeAddress.addressLine3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput id="addressLine4" label="Line 4 (Optional)" type="text" v-model="homeAddress.addressLine4" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.homeAddress.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="homeAddress.postalCode" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput id="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput id="mailingAddress2" label="Line 2 (Optional)" v-model="mailingAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput id="mailingAddress3" label="Line 3 (Optional)" v-model="mailingAddress.addressLine3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput id="mailingAddress4" label="Line 4 (Optional)" v-model="mailingAddress.addressLine4" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput id="mailingPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddress.postalCode" />
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
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Update Another Contract Address</AppButton>
  </div>
</template>
<script>
import useVuelidate from '@vuelidate/core'
import { required, helpers } from '@vuelidate/validators'
import { validateGroupNumber, validatePHN, validatePostalCode, VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, VALIDATE_POSTAL_CODE_MESSAGE } from '../../util/validators'
import MspContractsService from '../../services/MspContractsService'

export default {
  name: 'UpdateContractAddress',

  setup() {
    return {
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      submitting: false,
      updateOk: false,
      updateMode: true,
      //Update Contract Address Fields
      phn: '',
      groupNumber: '',
      homeAddress: {
        addressLine1: '',
        addressLine2: '',
        addressLine3: '',
        addressLine4: '',
        postalCode: '',
      },
      mailingAddress: {
        addressLine1: '',
        addressLine2: '',
        addressLine3: '',
        addressLine4: '',
        postalCode: '',
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
      this.$store.commit('alert/dismissAlert')
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert')
          this.submitting = false
          return
        }
        this.result = (
          await MspContractsService.updateContractAddress({
            phn: this.phn,
            groupNumber: this.groupNumber,
            homeAddress: {
              addressLine1: this.homeAddress.addressLine1,
              addressLine2: this.homeAddress.addressLine2,
              addressLine3: this.homeAddress.addressLine3,
              addressLine4: this.homeAddress.addressLine4,
              postalCode: this.homeAddress.postalCode,
            },
            mailingAddress: {
              addressLine1: this.mailingAddress.addressLine1,
              addressLine2: this.mailingAddress.addressLine2,
              addressLine3: this.mailingAddress.addressLine3,
              addressLine4: this.mailingAddress.addressLine4,
              postalCode: this.mailingAddress.postalCode,
            },
          })
        ).data

        if (this.result.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.updateMode = false
          this.updateOk = true
          this.$store.commit('alert/setSuccessAlert', this.result.message)
          return
        }
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.submitting = false
      }
    },
    resetForm() {
      this.groupNumber = ''
      this.phn = ''
      this.homeAddress = {}
      this.mailingAddress = {}
      this.result = null
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
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
      homeAddress: {
        addressLine1: { required },
        postalCode: {
          required,
          validatePostalCode: helpers.withMessage(VALIDATE_POSTAL_CODE_MESSAGE, validatePostalCode),
        },
      },
    }
  },
}
</script>
