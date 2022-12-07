<template>
  <div id="addGroupMember" v-if="addMode">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id="coverageEffectiveDate" label="Coverage Effective Date" monthPicker inputDateFormat="yyyy-MM" placeholder="YYYY-MM" v-model="coverageEffectiveDate">
            <template #tooltip>Date always defaults to first day of the month</template>
          </AppDateInput>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="Group Member's PHN" type="text" v-model="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupMemberNumber" id="groupMemberNumber" label="Group Member Number (Optional)" type="text" v-model.trim="groupMemberNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.departmentNumber" id="departmentNumber" label="Department Number (Optional)" type="text" v-model="departmentNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.telephone" id="telephone" label="Telephone (Optional)" type="text" v-model.trim="telephone" placeholder="1234567890" />
        </AppCol>
      </AppRow>
      <AppRow class="element-gap">
        <AppCol class="col5">
          <AppInput :e-model="v$.homeAddress.addressLine1" id="addressLine1" label="Home Address Line 1" type="text" v-model="homeAddress.addressLine1" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress.addressLine1" id="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow class="element-gap">
        <AppCol class="col5">
          <AppInput :e-model="v$.homeAddress.addressLine2" id="addressLine2" label="Line 2 (Optional)" type="text" v-model="homeAddress.addressLine2" />
        </AppCol>
        <AppCol class="col5">
          <AppInput :e-model="v$.mailingAddress.addressLine2" id="mailingAddress2" label="Line 2 (Optional)" v-model="mailingAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow class="flex-nowrap">
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.city" id="city" label="City" type="text" v-model.trim="homeAddress.city" />
          </AppCol>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.province" id="province" label="Province" type="text" v-model.trim="homeAddress.province" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.mailingAddress.city" id="mailingAddressCity" label="City" type="text" v-model.trim="mailingAddress.city" />
          </AppCol>
          <AppCol class="col5">
            <AppInput :e-model="v$.mailingAddress.province" id="mailingAddressProvince" :label="stateAddressField" type="text" v-model.trim="mailingAddress.province" />
          </AppCol>
        </AppRow>
      </AppRow>
      <AppRow class="flex-nowrap">
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="homeAddress.postalCode" />
          </AppCol>
          <AppCol class="col5">
            <AppInput :e-model="v$.homeAddress.country" id="country" label="Country" type="text" value="Canada" disabled="disabled" v-model.trim="homeAddress.country" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col5">
            <AppInput :e-model="v$.mailingAddress.postalCode" id="mailingPostalCode" :label="zipAddressField" type="text" v-model.trim="mailingAddress.postalCode" />
          </AppCol>
          <AppCol class="col5">
            <AppSelect id="mailingAddressCountry" label="Country" v-model="mailingAddress.country" :options="countryOptions" />
          </AppCol>
        </AppRow>
      </AppRow>
      <div>
        <AppRow>
          <AppCol class="col"><h2>Dependent(s) (Optional)</h2> </AppCol>
          <AppCol class="col4"> </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col3"><b>Relationship</b> </AppCol>
          <AppCol class="col3"><b>PHN</b> </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col3"><b> Spouse </b> </AppCol>
          <AppCol class="col3">
            <AppInput :e-model="v$.spousePhn" id="spousePhn" type="text" v-model.trim="spousePhn" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col4"> </AppCol>
        </AppRow>
      </div>
      <AddListDependent :dependents="dependents" @add-dependent="addDependent" @remove-dependent="removeDependent" />
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm()" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="confirmation" v-if="addOk">
    <p><label>Transaction completed for:</label></p>
    <ul>
      <li><label>Group Number: </label>{{ groupNumber }}</li>
      <li><label>PHN: </label>{{ result?.phn }}</li>
    </ul>
    <br />
    <AppButton @click="resetForm" mode="primary" type="button">Add Another Group Member</AppButton>
  </div>
</template>
<script>
import useVuelidate from '@vuelidate/core'
import { required, requiredIf, helpers, maxLength } from '@vuelidate/validators'
import AddListDependent from '../../components/groupmember/AddListDependent.vue'
import { COUNTRIES } from '../../util/constants'
import {
  validateGroupNumber,
  validateTelephone,
  validatePHN,
  validatePostalCode,
  validateMailingPostalCode,
  validateAddress,
  validateMailingAddressForMSPContracts,
  validateOptionalAddress,
  validateDepartmentNumber,
  validateGroupMemberNumber,
  VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE,
  VALIDATE_ADDRESS_LINE1_MESSAGE,
  VALIDATE_ADDRESS_LINE2_MESSAGE,
  VALIDATE_GROUP_NUMBER_MESSAGE,
  VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE,
  VALIDATE_DEPARTMENT_NUMBER_MESSAGE,
  VALIDATE_PHN_MESSAGE,
  VALIDATE_POSTAL_CODE_MESSAGE,
  VALIDATE_TELEPHONE_MESSAGE,
  VALIDATE_CITY_REQUIRED_MESSAGE,
  VALIDATE_CITY_MESSAGE,
  VALIDATE_PROVINCE_REQUIRED_MESSAGE,
  VALIDATE_PROVINCE_MESSAGE,
} from '../../util/validators'
import GroupMemberService from '../../services/GroupMemberService'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'AddGroupMember',
  components: {
    AddListDependent,
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
      addOk: false,
      addMode: true,
      //Add Group Member Fields
      phn: '',
      groupNumber: '',
      groupMemberNumber: '',
      departmentNumber: '',
      coverageEffectiveDate: null,
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
      spousePhn: '',
      dependents: [],
      result: {
        phn: '',
        status: '',
        message: '',
      },
    }
  },
  computed: {
    // Coverage Effective Date Date should be the first day of the month.
    effectiveDateAdjusted() {
      return new Date(this.coverageEffectiveDate.year, this.coverageEffectiveDate.month, 1)
    },
    stateAddressField() {
      if (this.mailingAddress.country === 'US') {
        return 'State'
      }
      return 'Province'
    },
    zipAddressField() {
      if (this.mailingAddress.country === 'US') {
        return 'ZIP Code'
      }
      return 'Postal Code'
    },
  },
  methods: {
    async submitForm() {
      this.submitting = true
      this.addOk = false
      this.addMode = true
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (
          await GroupMemberService.addGroupMember({
            phn: this.phn,
            groupNumber: this.groupNumber,
            phn: this.phn,
            groupNumber: this.groupNumber,
            groupMemberNumber: this.groupMemberNumber,
            departmentNumber: this.departmentNumber,
            effectiveDate: this.effectiveDateAdjusted,
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
            spousePhn: this.spousePhn,
            dependentPhn1: this.dependents[0],
            dependentPhn2: this.dependents[1],
            dependentPhn3: this.dependents[2],
            dependentPhn4: this.dependents[3],
            dependentPhn5: this.dependents[4],
            dependentPhn6: this.dependents[5],
            dependentPhn7: this.dependents[6],
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.addMode = false
          this.addOk = true
          this.alertStore.setInfoAlert(this.result.message)
          return
        }
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.submitting = false
      }
    },
    addDependent(dependentPHN) {
      this.dependents.push(dependentPHN)
    },
    removeDependent(index) {
      this.dependents.splice(index, 1)
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.submitting = false
    },
    resetForm() {
      this.groupNumber = ''
      this.phn = ''
      this.groupMemberNumber = ''
      this.departmentNumber = ''
      this.coverageEffectiveDate = null
      this.telephone = ''
      this.homeAddress.addressLine1 = ''
      this.homeAddress.addressLine2 = ''
      this.homeAddress.city = ''
      this.homeAddress.province = ''
      this.homeAddress.postalCode = ''
      this.homeAddress.country = ''
      this.mailingAddress.addressLine1 = ''
      this.mailingAddress.addressLine2 = ''
      this.mailingAddress.city = ''
      this.mailingAddress.province = ''
      this.mailingAddress.postalCode = ''
      this.mailingAddress.country = ''
      this.spousePhn = ''
      this.dependents = []
      this.result = null
      this.v$.$reset()
      this.alertStore.dismissAlert()
      this.submitting = false
      this.addOk = false
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
      groupMemberNumber: {
        validateGroupMemberNumber: helpers.withMessage(VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE, validateGroupMemberNumber),
      },
      departmentNumber: {
        validateDepartmentNumber: helpers.withMessage(VALIDATE_DEPARTMENT_NUMBER_MESSAGE, validateDepartmentNumber),
      },
      coverageEffectiveDate: {
        required,
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
          required: helpers.withMessage(VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE, requiredIf(validateMailingAddressForMSPContracts)),
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
          required: helpers.withMessage(VALIDATE_CITY_REQUIRED_MESSAGE, requiredIf(validateMailingAddressForMSPContracts)),
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_CITY_MESSAGE, validateAddress),
        },
        province: {
          required: helpers.withMessage(VALIDATE_PROVINCE_REQUIRED_MESSAGE, requiredIf(validateMailingAddressForMSPContracts)),
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_PROVINCE_MESSAGE, validateAddress),
        },
      },
      spousePhn: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
    }
  },
}
</script>
<style>
.flex-nowrap {
  flex-wrap: nowrap !important;
}
.element-gap {
  gap: 100px;
}
</style>
