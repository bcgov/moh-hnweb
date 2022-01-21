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
          <AppInput :e-model="v$.homeAddress.addressLine1" id="addressLine1" label="Home Address Line 1" type="text" v-model="homeAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.homeAddress.addressLine2" id="addressLine2" label="Line 2 (Optional)" type="text" v-model="homeAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.homeAddress.addressLine3" id="addressLine3" label="Line 3 (Optional)" type="text" v-model="homeAddress.addressLine3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.homeAddress.city" id="city" label="City" type="text" v-model.trim="homeAddress.city" />
        </AppCol>
        <AppCol class="col4">
          <AppSelect :e-model="v$.homeAddress.province" id="province" label="Province" v-model="homeAddress.province" :options="provinceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
        </AppCol>
        <AppCol class="col4">
          <AppInput :e-model="v$.homeAddress.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="homeAddress.postalCode" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress.addressLine1" id="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress.addressLine2" id="mailingAddress2" label="Line 2 (Optional)" v-model="mailingAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col7">
          <AppInput :e-model="v$.mailingAddress.addressLine3" id="mailingAddress3" label="Line 3 (Optional)" v-model="mailingAddress.addressLine3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddress.city" id="mailingAddressCity" label="City" type="text" v-model.trim="mailingAddress.city" />
        </AppCol>
        <AppCol class="col4">
          <AppSelect :e-model="v$.mailingAddress.province" id="mailingAddressProvince" label="Province" v-model="mailingAddress.province" :options="provinceOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
        </AppCol>
        <AppCol class="col4">
          <AppInput :e-model="v$.mailingAddress.postalCode" id="mailingAddressPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddress.postalCode" />
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
            <AppInput :e-model="v$.spousePhn" id="spouse" type="text" v-model.trim="spousePhn" />
          </AppCol> 
        </AppRow>
        <AppRow>
          <AppCol class="col4">
          </AppCol> 
        </AppRow>      
      </div> 
      <div id="addPHN">     
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
                {{dependent}} <span v-show="dependents.length > 0"><font-awesome-icon icon="trash-alt" @click="removeDependent(index)"/></span>
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
import { validateGroupNumber, validateTelephone, validatePHN, VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, VALIDATE_TELEPHONE_MESSAGE } from '../../util/validators'
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
      //Add Group Member Fields
      phn: '',
      groupNumber: '', 
      groupMemberNumber: '',     
      departmentNumber: '',   
      coverageEffectiveDate: dayjs().startOf('month').toDate(),
      telephone: '',
      homeAddress: {
        addressLine1: '',
        addressLine2: '',
        addressLine3: '',
        city: '',
        province: '',
        postalCode: '',
      },
      mailingAddress: {
        addressLine1: '',
        addressLine2: '',
        addressLine3: '',
        city: '',
        province: '',
        postalCode: '',
      },      
      spousePhn: '',
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
      this.addMode= true
      try {
        const isValid = await this.v$.$validate()
        console.log(isValid)
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert')
          this.submitting = false
          return
        }
        this.result = (await GroupMemberService.addGroupMember({
          phn: this.phn,
          groupNumber: this.groupNumber,
          phn: this.phn,
          groupNumber: this.groupNumber, 
          groupMemberNumber: this.groupMemberNumber,     
          departmentNumber: this.departmentNumber,   
          coverageEffectiveDate: this.coverageEffectiveDate,
          telephone: this.telephone,
          homeAddress: {
            addressLine1: this.homeAddress.addressLine1,
            addressLine2: this.homeAddress.addressLine2,
            addressLine3: this.homeAddress.addressLine3,
            city: this.homeAddress.city,
            province: this.homeAddress.province, 
            postalCode: this.homeAddress.postalCode,
          },               
          mailingAddress: {
            addressLine1: this.mailingAddress.addressLine1,
            addressLine2: this.mailingAddress.addressLine2,
            addressLine3: this.mailingAddress.addressLine3, 
            city: this.mailingAddress.city,
            province: this.mailingAddress.province,
            postalCode: this.mailingAddress.postalCode,
          },
          spousePhn: this.spousePhn,
          dependentPhn1: this.dependents[0],
          dependentPhn2: this.dependents[1],
          dependentPhn3: this.dependents[2],
          dependentPhn4: this.dependents[3],
          dependentPhn5: this.dependents[4],
          dependentPhn6: this.dependents[5],
          dependentPhn7: this.dependents[6],
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

  validateDependent(){  
    const rules = {
       dependentPHN: {validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),},
    }   
    const v$ = useVuelidate(rules, dependentPHN)
    console.log(v$.dependentPHN.$validate);
    return {
      v$
    }    
  },
   addDependent(){
     const isValid = this.validateDependent() 
     console.log(isValid)
      if (!isValid || this.dependentPHN === '') {
        return
      }
      this.dependents.push(this.dependentPHN);
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
      this.homeAddress.addressLine1 = ''
      this.homeAddress.addressLine2 = ''
      this.homeAddress.addressLine3 = ''
      this.homeAddress.city = ''
      this.homeAddress.province = ''
      this.homeAddress.postalCode = ''
      this.mailingAddress.addressLine1 = ''
      this.mailingAddress.addressLine2 = ''
      this.mailingAddress.addressLine3 = ''
      this.mailingAddress.city = ''
      this.mailingAddress.province = ''
      this.mailingAddress.postalCode = ''
      this.spousePhn = ''
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
      homeAddress: {
        addressLine1: { required },
        addressLine2: {},
        addressLine3: {},
        city: { required },
        province: { required },
        postalCode: { required },
      },           
      mailingAddress: {
        addressLine1: {},
        addressLine2: {},
        addressLine3: {},
        city: {},
        province: {},
        postalCode: {},
      },        
      spousePhn: {validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),},
      dependentPHN: {validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),}, 
         
    }
  },
}
</script>
