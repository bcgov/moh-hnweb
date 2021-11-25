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
        <!-- Group Number

Visa Issue Date
Visa Expiry Date
Residence Date
Coverage Effective Date 
Home Address (Line 1 to 4)
City 
Province 
Postal Code
Prior Residence Code -->
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
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
          groupNumber: "",
          immigrationCode: '',
/*           visaIssueDate,
          visaExpiryDate,
          residenceDate,
          coverageEffectiveDate,
          homeAddressLine1,
          city,
          province,
          postalCode,
          priorResidenceCode,
 */        }
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
        }
      }
    }
}
</script>