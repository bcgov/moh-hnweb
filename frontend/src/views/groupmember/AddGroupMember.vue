<template>
  <div id="updateNumberAndDept" v-if="addMode">
    <form @submit.prevent="submitForm">
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
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number (Optional)" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number (Optional)" type="text" v-model="departmentNumber"/>
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
        <AppRow>
          <AppCol class="col4"><b>Dependent(s) (Optional)</b>
          </AppCol>
          <AppCol class="col4">
          </AppCol> 
        </AppRow>       
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
          <AppCol class="col4">
          </AppCol> 
        </AppRow>      
      </div> 
      <div v-for="(dependent, index) in dependents" :key="`dependent_${index}`" :id="index">
        <AppRow>
          <AppCol class="col4">
            <span  v-show="index==0"><b> Dependent </b></span>
          </AppCol>
          <AppCol class="col4">
            <AppInput :e-model="v$.dependent" type="text" v-model.trim="dependent.dependent"  v-bind:id="'dependent_'+index"/>
          </AppCol>
          <span v-show = "dependents.length > 1 && index != 0">
            <font-awesome-icon icon="minus" @click="removeDependent(index, dependents)"/><b>Remove</b>
          </span>
        </AppRow>    
      </div>      
      <AppRow v-show = "dependents.length < 7">
        <AppCol class="col4">
        </AppCol>
        <AppCol class="col4">
          <span><font-awesome-icon icon="plus" @click="addDependent(dependents)"/><b>Add</b></span>
        </AppCol>        
      </AppRow>             
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm()" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  <br />
  </div>
  <div id="confirmation" v-if="addOk">
    <p>PHN: {{ result?.phn }}</p>  
    <AppButton @click="resetForm" mode="primary" type="button">Add another Group Memeber</AppButton>
  </div>
</template>
<script>
import AppSelect from '../../components/ui/AppSelect.vue'
import DependentAppInput from '../../components/ui/DependentAppInput.vue'
import useVuelidate from '@vuelidate/core'
import { validateGroupNumber, validateTelephone, validatePHN, VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, VALIDATE_TELEPHONE_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import dayjs from 'dayjs'
import GroupMemberService from '../../services/GroupMemberService'

export default {
  name: 'AddGroupMember',
  components: {
    AppSelect,
    DependentAppInput,
  },
  setup() {
    return {
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      submitting: false,
      addOk: false, 
      addMode : true,
      //Add Group Member Fields
      phn: '9873895927',
      groupNumber: '6337109', 
      groupMemberNumber: '',     
      departmentNumber: '',   
      coverageEffectiveDate: dayjs().startOf('month').toDate(),
      telephone: '',
      address1: '101 ave',
      address2: '',
      address3: '',
      city: 'Edmonton',
      province: 'Alberta',
      postalCode: 't6t6t6',
      mailingAddress1: '',
      mailingAddress2: '',
      mailingAddress3: '',
      mailingAddressCity: '',
      mailingAddressProvince: '',
      mailingAddressPostalCode: '',
      spouse: '',
      dependents: [{dependent: ""}],
      result: {
        phn: '', 
        status: '',
        message: '',
      } 
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
    async submitForm() {
      this.submitting = true,
      this.addOk= false, 
      this.addMode= true
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert')
          this.submitting = false
          return
        }
        this.result = (await GroupMemberService.updateNumberAndDept({
          phn: this.phn,
          groupNumber: this.groupNumber,
          phn: this.phn,
          groupNumber: this.groupNumber, 
          groupMemberNumber: this.groupMemberNumber,     
          departmentNumber: this.departmentNumber,   
          coverageEffectiveDate: this.coverageEffectiveDate,
          telephone: this.telephone,
          address1: this.address1,
          address2: this.address2,
          address3: this.address3,
          city: this.city,
          province: this.province,
          postalCode: this.postalCode,
          mailingAddress1: this.mailingAddress1,
          mailingAddress2: this.mailingAddress2,
          mailingAddress3: this.mailingAddress3,
          mailingAddressCity: this.mailingAddress3,
          mailingAddressProvince: this.mailingAddressProvince,
          mailingAddressPostalCode: this.mailingAddressPostalCode,
          spouse: this.spouse,
          dependents: this.dependents,        
        })).data

        if (this.result.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.addMode = false
          this.addOk = true
          this.$store.commit('alert/setSuccessAlert', this.result.message)
          return
        }
        
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.submitting = false
      }
    },
    addDependent(fieldType) {     
      fieldType.push({});
    },
    removeDependent(index, fieldType) {
      fieldType.splice(index, 1);
    },
    resetForm() {     
      this.groupNumber = ''     
      this.groupMemberNumber = ''      
      this.departmentNumber = ''      
      this.coverageEffectiveDate = dayjs().startOf('month').toDate(),
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
      this.spouse = ''
      this.dependents = [{dependent: ""}]
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
      this.submitting = false,
      this.addOk = false, 
      this.addMode = true
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
      spouse: {validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),},
      dependent: {validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),},     
    }
  },
}
</script>
