<template>
  <div>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="resident.phn"/>
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Name" :value="resident.name"/>
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Date of Birth" :value="resident.dateOfBirth"/>
      </AppCol>
    </AppRow>
    <form @submit.prevent="registerVisaResident">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col3">
          <AppInput :e-model="v$.immigrationCode" id="immigrationCode" label="Immigration Code" type="text" v-model.trim="immigrationCode" />
        </AppCol>
      </AppRow>
       <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.immigrationCode" id="groupMemberNumber" label="Group Member Number - Optional" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.visaIssueDate" id ="visaIssueDate" label="Visa Issue Date" v-model="visaIssueDate" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number - Optional" type="text" v-model.trim="departmentNumber" />
        </AppCol>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.visaExpiryDate" id ="visaExpiryDate" label="Visa Expiry Date" v-model="visaExpiryDate" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.residenceDate" id ="residenceDate" label="Residence Date" v-model="residenceDate" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id ="coverageEffectiveDate" label="Coverage Effective Date" v-model="coverageEffectiveDate" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.telephone" id="telephone" label="Telephone - Optional" type="text" v-model.trim="telephone" />
        </AppCol>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.coverageCancellationDate" id ="coverageCancellationDate" label="Coverage Cancellation Date" v-model="coverageCancellationDate" />          
        </AppCol>
      </AppRow>

      <AppRow>
          <AppInput :e-model="v$.homeAddressLine1" id ="homeAddressLine1" label="Home Address" v-model="homeAddressLine1" />          
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.city" id="city" label="City" type="text" v-model.trim="city" />
        </AppCol>
        <AppCol class="col3">
          <AppInput :e-model="v$.province" id="province" label="Province" type="text" v-model.trim="province" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.country" id="country" label="Country" type="text" v-model.trim="country" />
        </AppCol>
        <AppCol class="col3">
          <AppInput :e-model="v$.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="postalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
          <AppInput :e-model="v$.mailHomeAddressLine1" id ="mailHomeAddressLine1" label="Mailing Address (if different from home address)" v-model="mailHomeAddressLine1" />          
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.mailCity" id="mailCity" label="City" type="text" v-model.trim="mailCity" />
        </AppCol>
        <AppCol class="col3">
          <AppInput :e-model="v$.mailProvince" id="mailProvince" label="Province" type="text" v-model.trim="mailProvince" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.mailCountry" id="mailCountry" label="Country" type="text" v-model.trim="mailCountry" />
        </AppCol>
        <AppCol class="col3">
          <AppInput :e-model="v$.mailPostalCode" id="mailPostalCode" label="Postal Code" type="text" v-model.trim="mailPostalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.priorResidenceCode" id="priorResidenceCode" label="Prior Residence Code - Optional" type="text" v-model.trim="priorResidenceCode" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.otherProvinceHealthcareNumber" id="otherProvinceHealthcareNumber" label="Other Province Healthcare Number (If Applicable) - Optional" type="text" v-model.trim="otherProvinceHealthcareNumber" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetFormVisaResident" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
</template>
<script>
import AppButton from '../../../components/AppButton.vue'
import AppCol from '../../../components/grid/AppCol.vue'
import AppDateInput from '../../../components/AppDateInput.vue'
import AppInput from '../../../components/AppInput.vue'
import AppRow from '../../../components/grid/AppRow.vue'
import AppOutput from '../../../components/AppOutput.vue'

import Datepicker from 'vue3-date-time-picker';
import 'vue3-date-time-picker/dist/main.css'
import { INPUT_DATE_FORMAT } from '../../../util/constants'

import EnrollmentService from '../../../services/EnrollmentService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'

export default {
    name: 'ResidentDetails',
    props: {
      resident: {
        phn: '',
        name: 'Result Name',
        dateOfBirth: '222222',
      },
    },
    components: {
        AppButton,
        AppCol,
        AppDateInput,
        AppInput,
        AppRow,
        AppOutput
    },
    setup() {
        return {
        v$: useVuelidate()}
    },
    data() {
        return {
          searching: false,
          searchOk: false,
          //Add Visa Resident Fields
          groupNumber: '',
          immigrationCode: '',
          groupMemberNumber: '',
          visaIssueDate: null,
          departmentNumber: '',
          visaExpiryDate: null,
          residenceDate: null,
          coverageEffectiveDate: null,
          telephone: '',
          coverageCancellationDate: null,
          homeAddressLine1: '',
          city: '',
          province: '',
          country: '',
          postalCode: '',
          mailHomeAddressLine1: '',
          mailCity: '',
          mailProvince: '',
          mailCountry: '',
          mailPostalCode: '',
          priorResidenceCode: '',
          otherProvinceHealthcareNumber: '',
        }
    },
    methods: {
      async submitForm() {
        this.searching = true
        try {
          const isValid = await this.v$.$validate()
          if (!isValid) {
            this.$store.commit('alert/setErrorAlert');
            this.searching = false
            return
          }
          this.result = (await EnrollmentService.getPersonDemographics({
            phn: this.phn
          }))
          console.log('Result returned')
          console.log(`Result: [PHN: ${this.result.phn}] [Name: ${this.result.name}] [DOB: ${this.result.dateOfBirth}]`)
          this.searchOk = true
          this.$store.commit('alert/setSuccessAlert', 'Search complete')
        } catch (err) {
          this.$store.commit('alert/setErrorAlert', `${err}`)
        } finally {
          this.searching = false
        }
      },
      resetForm() {
        this.phn = '9890608412'
        this.v$.$reset()
        this.$store.commit("alert/dismissAlert");
        this.searchOk = false
        this.searching = false
      },
      async registerVisaResident() {
        console.log('registerVisaResident')
        this.searching = true
        try {
          const isValid = await this.v$.$validate()
          if (!isValid) {
            this.$store.commit('alert/setErrorAlert');
            this.searching = false
            return
          }
          console.log('Send to service')
          // this.result = (await EnrollmentService.getPersonDemographics({
          //   phn: this.phn
          // }))
          console.log('Result returned')
          console.log(`Result: [PHN: ${this.result.phn}] [Name: ${this.result.name}] [DOB: ${this.result.dateOfBirth}]`)
          this.searchOk = true
          this.$store.commit('alert/setSuccessAlert', 'Search complete')
        } catch (err) {
          this.$store.commit('alert/setErrorAlert', `${err}`)
        } finally {
          this.searching = false
        }
      },
      resetFormVisaResident() {
        this.groupNumber = ''
        this.v$.$reset()
        this.$store.commit("alert/dismissAlert");
        // this.searchOk = false
        // this.searching = false
      }
    },
    validations() {
      return {
        groupNumber: {
          required,
        },
        immigrationCode: {required},
        groupMemberNumber: {},
        visaIssueDate: {required},
        departmentNumber: {},
        visaExpiryDate: {required},
        residenceDate: {required},
        coverageEffectiveDate: {required},
        telephone: {},
        coverageCancellationDate: {required},
        homeAddressLine1: {required},
        city: {required},
        province: {required},
        country: {required},
        postalCode: {required},
        mailHomeAddressLine1: {},
        mailCity: {},
        mailProvince: {},
        mailCountry: {},
        mailPostalCode: {},
        priorResidenceCode: {},
        otherProvinceHealthcareNumber: {},
      }
    }
}
</script>