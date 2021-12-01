<template>
  <div>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="resident.phn"/>
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Name" :value="fullName"/>
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
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number - Optional" type="text" v-model.trim="groupMemberNumber" />
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
          <AppSelect :e-model="v$.province"  id="province" label="Province" v-model="province" :options="provinceOptions"/>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="postalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress1" id ="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress1" />          
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
          <AppSelect :e-model="v$.mailingAddressProvince"  id="mailingAddressProvince" label="Province" v-model="mailingAddressProvince" :options="provinceOptions"/>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressPostalCode" id="mailingAddressPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddressPostalCode" />
        </AppCol>
      </AppRow>

      <AppRow>
        <AppCol class="col4">
          <AppSelect :e-model="v$.priorResidenceCode"  id="priorResidenceCode" label="Prior Residence Code - Optional" v-model="priorResidenceCode" :options="priorResidenceOptions"/>
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
import { validateNumber, validateGroupMemberNumber, validateDepartmentNumber, validateTelephone, 
        VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE, VALIDATE_DEPARTMENT_NUMBER_MESSAGE, VALIDATE_TELEPHONE_MESSAGE } from '../../../util/validators'
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
          postalCode: '',
          mailingAddress1: '',
          mailingAddress2: '',
          mailingAddress3: '',
          mailingAddressCity: '',
          mailingAddressProvince: '',
          mailingAddressPostalCode: '',
          priorResidenceCode: '',
          otherProvinceHealthcareNumber: '',
          // Immigration Code drop down options
          immigrationCodeOptions: [
            { text: '', value: '' },
            { text: 'Student Authorization', value: 'S' },
          ],
          provinceOptions: [
            { text: '', value: '' },
            { text: 'Alberta', value: 'AB' },
            { text: 'British Columbia', value: 'BC' },
            { text: 'Manitoba', value: 'MB' },
            { text: 'New Brunswick', value: 'NB' },
            { text: 'Newfoundland', value: 'NL' },
            { text: 'Nova Scotia', value: 'NS' },
            { text: 'Northwest Territories', value: 'NT' },
            { text: 'Nunavut', value: 'NU' },
            { text: 'Ontario', value: 'ON' },
            { text: 'P.E.I', value: 'PE' },
            { text: 'Quebec', value: 'QC' },
            { text: 'Saskatchewan', value: 'SK' },
            { text: 'Yukon', value: 'YT' },
          ],
          priorResidenceOptions: [
            { text: '', value: '' },
            { text: 'Alberta', value: 'AB' },
            { text: 'Manitoba', value: 'MB' },
            { text: 'New Brunswick', value: 'NB' },
            { text: 'Newfoundland', value: 'NL' },
            { text: 'Nova Scotia', value: 'NS' },
            { text: 'Northwest Territories', value: 'NT' },
            { text: 'Nunavut', value: 'NU' },
            { text: 'Other Country', value: 'OC' },
            { text: 'Ontario', value: 'ON' },
            { text: 'P.E.I', value: 'PE' },
            { text: 'Quebec', value: 'QC' },
            { text: 'Saskatchewan', value: 'SK' },
            { text: 'U.S.A', value: 'US' },
            { text: 'Yukon', value: 'YT' },
            { text: 'British Columbia', value: 'BC' },
          ],
        }
    },
    computed: {
      fullName() {
        let name = ''
        if (this.resident.surname) {
          name = name + this.resident.surname
        }
        if (this.resident.givenName) {
          name = name + ', ' + this.resident.givenName
        }
        if (this.resident.secondName) {
          name = name + ' ' + this.resident.secondName
        }
        return name
      },
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
            givenName: this.resident.givenName,	
            secondName: this.resident.secondName,        
            surname: this.resident.surname,
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
        this.postalCode = '',
        this.mailingAddress1 = '',
        this.mailingAddress2 = '',
        this.mailingAddress3 = '',
        this.mailingAddressCity = '',
        this.mailingAddressProvince = '',
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
          validateNumber: helpers.withMessage(
            VALIDATE_GROUP_NUMBER_MESSAGE, validateNumber
          )
        },
        immigrationCode: {required},
        groupMemberNumber: {
          validateGroupMemberNumber: helpers.withMessage(
            VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE, validateGroupMemberNumber
          )
        },
        visaIssueDate: {required},
        departmentNumber: {
          validateDepartmentNumber: helpers.withMessage(  
            VALIDATE_DEPARTMENT_NUMBER_MESSAGE, validateDepartmentNumber
          )
        },
        visaExpiryDate: {required},
        residenceDate: {required},
        coverageEffectiveDate: {required},
        telephone: {
          validateTelephone: helpers.withMessage(
            VALIDATE_TELEPHONE_MESSAGE, validateTelephone
          )
        },
        coverageCancellationDate: {required},
        address1: {required},
        address2: {},
        address3: {},
        city: {required},
        province: {required},
        postalCode: {required},
        mailingAddress1: {},
        mailingAddress2: {},
        mailingAddress3: {},
        mailingAddressCity: {},
        mailingAddressProvince: {},
        mailingAddressPostalCode: {},
        priorResidenceCode: {},
        otherProvinceHealthcareNumber: {},
      }
    }
}
</script>