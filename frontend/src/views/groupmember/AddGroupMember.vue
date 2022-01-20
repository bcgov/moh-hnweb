<template>
  <div id="addGroupMember" v-if="addMode">
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
          <AppSelect :e-model="v$.province" id="province" label="Province" v-model="province" :options="provinceOptions" />
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
          <AppSelect :e-model="v$.mailingAddressProvince" id="mailingAddressProvince" label="Province" v-model="mailingAddressProvince" :options="provinceOptions" />
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
      <div id="addPHN"> 
        <AppRow v-if = "isValidDependentPHN">
        <AppCol class="col4">
          </AppCol>
          <AppCol class="col4">
            <p class="error-text">Invalid or blank PHN</p>                              
          </AppCol> 
          </AppRow>      
        <AppRow>
          <AppCol class="col4"><b> Dependent </b>
          </AppCol>
          <AppCol class="col4">          
            <AppInput :e-model="v$.dependentPHN" id="dependentPHN" type="dependentPHN" v-model.trim="dependentPHN" />          
          </AppCol>           
            <AppButton @click="addDependent()" mode="secondary" type="button" v-show="dependents.length < 7">Add</AppButton>            
        </AppRow>
        
        <AppRow>
          <AppCol class="col4">
          </AppCol>
          <AppCol class="col4">
            <ul>
              <li v-for="(dependent, index) in dependents" >
                {{ dependent.dependentPHN }} <span v-show = "dependents.length > 0"><font-awesome-icon icon="trash-alt" @click="removeDependent(index)"/></span>
              </li>
            </ul>
          </AppCol>        
        </AppRow>       
    </div>
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
import useVuelidate from '@vuelidate/core'
import { validateGroupNumber, validateTelephone, validatePHN, validatePHNFormat, VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, VALIDATE_TELEPHONE_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { PROVINCES } from '../../util/constants'
import GroupMemberService from '../../services/GroupMemberService'

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
      addOk: false, 
      addMode : true,
      isValidDependentPHN : false,
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
      dependents: [],
		  dependentPHN: '',
      result: {
        phn: '', 
        status: '',
        message: '',
      } 
    }
  },
  created() {
    // Province drop down options
    this.provinceOptions = PROVINCES
  },
  methods: {
    async submitForm() {
      this.submitting = true,
      this.addOk = false, 
      this.isValidDependentPHN = false,
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
    
    addDependent(){     
    	let dependent = {dependentPHN: this.dependentPHN};
      const validate = validatePHNFormat(this.dependentPHN);      
       if(!validate){
        // this.isValidDependentPHN = true;
        //return helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN)
         return       
      }
      this.dependents.push(dependent);
			this.dependentPHN = ''; // clear dependent
    },
    removeDependent(index){
      this.dependents.splice(index, 1)
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
      this.dependents = []
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
      dependentPHN: {}, 
         
    }
  },
}
</script>
