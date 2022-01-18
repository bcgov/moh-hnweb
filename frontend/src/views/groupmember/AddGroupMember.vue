<template>
    <form @submit.prevent="addGroupMember">
    <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
       <AppRow class="row-center">
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id="coverageEffectiveDate" label="Coverage Effective Date" v-model="coverageEffectiveDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="Group Member's PHN" type="text" v-model="phn"/>
        </AppCol>
      </AppRow>
       <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number" type="text" v-model="departmentNumber"/>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.telephone" id="telephone" label="Telephone (Optional)" type="text" v-model.trim="telephone" placeholder="1234567890" />
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
          <AppSelect :e-model="v$.province" id="province" label="Province" v-model="province" :options="getProvinceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
        </AppCol>
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
          <AppSelect :e-model="v$.mailingAddressProvince" id="mailingAddressProvince" label="Province" v-model="mailingAddressProvince" :options="getProvinceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
        </AppCol>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddressPostalCode" id="mailingAddressPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddressPostalCode" />
        </AppCol>
      </AppRow>
      <div>
       <b>Dependent(s)(Optional)</b>
        <AppRow>
          <AppCol class="col4"><b>Relationship</b>
          </AppCol>
          <AppCol class="col4"><b>PHN</b>
          </AppCol> 
        </AppRow> 
        <AppRow>
          <AppCol class="col4"><b> Spouse </b>
          </AppCol>
          <AppCol class="col4">
            <AppInput :e-model="v$.spouse" id="spouse" type="text" v-model.trim="spouse" />
          </AppCol> 
        </AppRow>
        <AppRow>
          <AppCol class="col4"><b> Dependent </b>
          </AppCol>
        <AppCol class="col4">
           <AppInput :e-model="v$.dependent1" id="dependent1" type="text" v-model.trim="dependent1" />
        </AppCol> 
        </AppRow>      
      </div> 
      <div v-for="(input, index) in dependents" :key="`dependentInput-${index}`">
        <AppRow>
          <AppCol class="col4">
          </AppCol>
          <AppCol class="col4">
            <AppInput :e-model="v$.dependent" type="text" v-model.trim="input.dependent" />
          </AppCol>
          <font-awesome-icon icon="minus" @click="removeDependent(index, dependents)"/><b>Remove</b>
        </AppRow>    
      </div>
      <AppRow>
        <AppCol class="col4">
        </AppCol>
        <AppCol class="col4">
          <font-awesome-icon icon="plus" @click="addDependent(input,dependents)"/><b>Add</b>
        </AppCol>        
      </AppRow>             
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm(input,dependents)" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
</template>
<script>
import AppSelect from '../../components/ui/AppSelect.vue'
import useVuelidate from '@vuelidate/core'
import { validateGroupNumber, validateTelephone, validatePHN, VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, VALIDATE_DEPARTMENT_NUMBER_MESSAGE, VALIDATE_TELEPHONE_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { API_DATE_FORMAT } from '../../util/constants'

export default {
  name: 'AddGroupMember',
  components: {
    AppSelect,
  },
  setup() {
    return {
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      submitting: false,
      //Add Group Member Fields
      phn: '',
      groupNumber: '', 
      groupMemberNumber: '',     
      departmentNumber: '',   
      coverageEffectiveDate: null,
      telephone: '',
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
      spouse: '',
      dependent1: '',
      dependents: [{dependent: ""}],
    }
  },
  computed: {
    // Province drop down options
    getProvinceOptions() {
      return [
        { text: 'Select', value: '' },
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
      ]
    },
  },
  methods: {
    async addGroupMember() {
      this.submitting = true
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert')
          this.submitting = false
          return
        }
        
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.submitting = false
      }
    },
    addDependent(value, fieldType) { 
      fieldType.push({ value: ""});
    },
    removeDependent(index, fieldType) {
      fieldType.splice(index, 1);
    },
    resetForm() {     
      this.groupNumber = ''     
      this.groupMemberNumber = ''      
      this.departmentNumber = ''      
      this.coverageEffectiveDate = null
      this.telephone = ''
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
      this.dependent = ''
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
      this.submitting = false
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
      groupMemberNumber: {},
      departmentNumber: {},      
      coverageEffectiveDate: { required },
      telephone: {
        validateTelephone: helpers.withMessage(VALIDATE_TELEPHONE_MESSAGE, validateTelephone),
      },
      address1: { required },
      address2: {},
      address3: {},
      city: { required },
      province: { required },
      postalCode: { required },
      mailingAddress1: {},
      mailingAddress2: {},
      mailingAddress3: {},
      mailingAddressCity: {},
      mailingAddressProvince: {},
      mailingAddressPostalCode: {},
      spouse: {},
      dependent: {},
      dependent1:{},
      dependent2:{},
    }
  },
}
</script>
