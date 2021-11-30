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
        <AppCol class="col4">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" size="200" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppSelect :e-model="v$.immigrationCode"  id="immigrationCode" label="Immigration Code" v-model="immigrationCode" :options="immigrationCodeOptions"/>
        </AppCol>
      </AppRow>
       <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.immigrationCode" id="groupMemberNumber" label="Group Member Number - Optional" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.visaIssueDate" id ="visaIssueDate" label="Visa Issue Date" v-model="visaIssueDate" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number - Optional" type="text" v-model.trim="departmentNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.visaExpiryDate" id ="visaExpiryDate" label="Visa Expiry Date" v-model="visaExpiryDate" />          
        </AppCol>
      </AppRow>
      <AppRow class="row1">
        <AppCol class="col4">
          <AppDateInput :e-model="v$.residenceDate" id ="residenceDate" label="Residence Date" v-model="residenceDate" />          
        </AppCol>
      </AppRow>
      <AppRow class="row1">
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id ="coverageEffectiveDate" label="Coverage Effective Date" v-model="coverageEffectiveDate" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.telephone" id="telephone" label="Telephone - Optional" type="text" v-model.trim="telephone" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageCancellationDate" id ="coverageCancellationDate" label="Coverage Cancellation Date" v-model="coverageCancellationDate" />          
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.address1" id ="address1" label="Home Address Line 1" v-model="address1" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.address2" id ="address2" label="Line 2 - Optional" v-model="address2" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.address3" id ="address3" label="Line 3 - Optional" v-model="address3" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.city" id="city" label="City" type="text" v-model.trim="city" />
        </AppCol>
        <AppCol class="col4">
          <AppInput :e-model="v$.province" id="province" label="Province" type="text" v-model.trim="province" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.country" id="country" label="Country - Optional" type="text" v-model.trim="country" />
        </AppCol>
        <AppCol class="col4">
          <AppInput :e-model="v$.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="postalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddres1" id ="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress1" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress2" id ="mailingAddress2" label="Line 2 - Optional" v-model="mailingAddress2" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress3" id ="mailingAddress3" label="Line 3 - Optional" v-model="mailingAddress3" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressCity" id="mailingAddressCity" label="City" type="text" v-model.trim="mailingAddressCity" />
        </AppCol>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressProvince" id="mailingAddressProvince" label="Province" type="text" v-model.trim="mailingAddressProvince" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressCountry" id="mailingAddressCountry" label="Country - Optional" type="text" v-model.trim="mailingAddressCountry" />
        </AppCol>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressPostalCode" id="mailingAddressPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddressPostalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.priorResidenceCode" id="priorResidenceCode" label="Prior Residence Code - Optional" type="text" v-model.trim="priorResidenceCode" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.otherProvinceHealthcareNumber" id="otherProvinceHealthcareNumber" label="Other Province Healthcare Number (If Applicable) - Optional" type="text" v-model.trim="otherProvinceHealthcareNumber" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
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
import AppSelect from '../../../components/AppSelect.vue'

import Datepicker from 'vue3-date-time-picker';
import 'vue3-date-time-picker/dist/main.css'
import { INPUT_DATE_FORMAT } from '../../../util/constants'

import EnrollmentService from '../../../services/EnrollmentService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { API_DATE_FORMAT } from '../../../util/constants'

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
        AppOutput,
        AppRow,
        AppSelect,
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
          address1: '',
          address2: '',
          address3: '',
          city: '',
          province: '',
          country: '',
          postalCode: '',
          mailingAddress1: '',
          mailingAddress2: '',
          mailingAddress3: '',
          mailingAddressCity: '',
          mailingAddressProvince: '',
          mailingAddressCountry: '',
          mailingAddressPostalCode: '',
          priorResidenceCode: '',
          otherProvinceHealthcareNumber: '',
          // Immigration Code drop down options
          immigrationCodeOptions: [
            { text: '', value: '' },
            { text: 'Student Authorization', value: 'Student_Authorization' },
          ],
        }
    },
    methods: {
      async registerVisaResident() {
        console.log('registerVisaResident')
        this.searching = true
        try {
          const isValid = true
          // await this.v$.$validate()
          // if (!isValid) {
          //   this.$store.commit('alert/setErrorAlert');
          //   this.searching = false
          //   return
          // }
          console.log('Emitting')
          this.$emit('register-resident', {
            phn: this.resident.phn,
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
            country: this.country,
            postalCode: this.postalCode,
            mailingAddress1: this.mailingAddress1,
            mailingAddress2: this.mailingAddress2,
            mailingAddress3: this.mailingAddress3,
            mailingAddressCity: this.mailingAddressCity,
            mailingAddressProvince: this.mailingAddressProvince,
            mailingAddressCountry: this.mailingAddressCountry,
            mailingAddressPostalCode: this.mailingAddressPostalCode,
            priorResidenceCode: this.priorResidenceCode,
            otherProvinceHealthcareNumber: this.otherProvinceHealthcareNumber,
          })
          this.$store.commit('alert/setSuccessAlert', 'Transaction Successful')
        } catch (err) {
          this.$store.commit('alert/setErrorAlert', `${err}`)
        } finally {
          this.searching = false
        }
      },
      resetForm() {
        this.groupNumber = '',
        this.immigrationCode = '',
        this.groupMemberNumber = '',
        this.visaIssueDate = null,
        this.departmentNumber = '',
        this.visaExpiryDate = null,
        this.residenceDate = null,
        this.coverageEffectiveDate = null,
        this.telephone = '',
        this.coverageCancellationDate = null,
        this.address1 = '',
        this.address2 = '',
        this.address3 = '',
        this.city = '',
        this.province = '',
        this.country = '',
        this.postalCode = '',
        this.mailingAddress1 = '',
        this.mailingAddress2 = '',
        this.mailingAddress3 = '',
        this.mailingAddressCity = '',
        this.mailingAddressProvince = '',
        this.mailingAddressCountry = '',
        this.mailingAddressPostalCode = '',
        this.priorResidenceCode = '',
        this.otherProvinceHealthcareNumber = '',
        this.v$.$reset()
        this.$store.commit("alert/dismissAlert");
        this.searching = false
        this.registrationOk = false
        this.v$.$reset()
        this.$store.commit("alert/dismissAlert");
        this.searching = false
      },
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
        address1: {required},
        address2: {},
        address3: {},
        city: {required},
        province: {required},
        country: {required},
        postalCode: {required},
        mailingAddress1: {},
        mailingAddress2: {},
        mailingAddress3: {},
        mailingAddressCity: {},
        mailingAddressProvince: {},
        mailingAddressCountry: {},
        mailingAddressPostalCode: {},
        priorResidenceCode: {},
        otherProvinceHealthcareNumber: {},
      }
    }
}
</script>