<template>
  <AppHelp>
    <p>Use this screen to add or renew a new group member and their dependents (if applicable) to your MSP group account.</p>
    <p>Before you begin:</p>
    <p>Use the PHN Inquiry screen to confirm that the employee and dependents are currently eligible for publicly funded health care (Eligible? = Y), and to check student status for any dependent children between 19 and 24.</p>
    <p>A group member's Home Address must be in British Columbia.</p>
    <p>NOTE - this screen does not have CITY or PROVINCE fields. Be sure to put them in the last completed line of address i.e., Line 1: 403 1575 Main Street Line 2: Vancouver BC.</p>
    <p>If the transaction was successful, the PHN will be displayed. You may wish to use the PHN with Get Contract Periods to verify that the correct group member has been added as of the correct date.</p>
  </AppHelp>
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
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.homeAddress.addressLine1" id="addressLine1" label="Home Address Line 1" type="text" v-model="homeAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.homeAddress.addressLine2" id="addressLine2" label="Line 2 (Optional)" type="text" v-model="homeAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.homeAddress.addressLine3" id="addressLine3" label="Line 3 (Optional)" type="text" v-model="homeAddress.addressLine3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.homeAddress.addressLine4" id="addressLine4" label="Line 4 (Optional)" type="text" v-model="homeAddress.addressLine4" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.homeAddress.postalCode" id="postalCode" label="Postal Code" type="text" v-model.trim="homeAddress.postalCode" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.mailingAddress.addressLine1" id="mailingAddress1" label="Mailing Address (if different from home address)" v-model="mailingAddress.addressLine1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.mailingAddress.addressLine2" id="mailingAddress2" label="Line 2 (Optional)" v-model="mailingAddress.addressLine2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.mailingAddress.addressLine3" id="mailingAddress3" label="Line 3 (Optional)" v-model="mailingAddress.addressLine3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppInput :e-model="v$.mailingAddress.addressLine4" id="mailingAddress4" label="Line 4 (Optional)" v-model="mailingAddress.addressLine4" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.mailingAddress.postalCode" id="mailingPostalCode" label="Postal Code" type="text" v-model.trim="mailingAddress.postalCode" />
        </AppCol>
      </AppRow>
      <div>
        <AppRow>
          <AppCol class="col4"><h2>Dependent(s) (Optional)</h2> </AppCol>
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
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Add Another Group Member</AppButton>
  </div>
</template>
<script>
import AppHelp from '../../components/ui/AppHelp.vue'
import useVuelidate from '@vuelidate/core'
import { required, requiredIf, helpers, maxLength } from '@vuelidate/validators'
import AddListDependent from '../../components/groupmember/AddListDependent.vue'
import {
  validateGroupNumber,
  validateTelephone,
  validatePHN,
  validatePostalCode,
  validateMailingPostalCode,
  validateAddress,
  validateMailingAddress,
  validateOptionalAddress,
  validateDepartmentNumber,
  validateGroupMemberNumber,
  VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE,
  VALIDATE_ADDRESS_LINE1_MESSAGE,
  VALIDATE_ADDRESS_LINE2_MESSAGE,
  VALIDATE_ADDRESS_LINE3_MESSAGE,
  VALIDATE_ADDRESS_LINE4_MESSAGE,
  VALIDATE_GROUP_NUMBER_MESSAGE,
  VALIDATE_GROUP_MEMBER_NUMBER_MESSAGE,
  VALIDATE_DEPARTMENT_NUMBER_MESSAGE,
  VALIDATE_PHN_MESSAGE,
  VALIDATE_POSTAL_CODE_MESSAGE,
  VALIDATE_TELEPHONE_MESSAGE,
} from '../../util/validators'
import GroupMemberService from '../../services/GroupMemberService'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'AddGroupMember',
  components: {
    AddListDependent,
    AppHelp,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
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
      this.homeAddress.addressLine3 = ''
      this.homeAddress.addressLine4 = ''
      this.homeAddress.postalCode = ''
      this.mailingAddress.addressLine1 = ''
      this.mailingAddress.addressLine2 = ''
      this.mailingAddress.addressLine3 = ''
      this.mailingAddress.addressLine4 = ''
      this.mailingAddress.postalCode = ''
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
        addressLine3: {
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE3_MESSAGE, validateAddress),
        },
        addressLine4: {
          maxLength: maxLength(25),
          validateAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE4_MESSAGE, validateAddress),
        },
        postalCode: {
          required,
          validatePostalCode: helpers.withMessage(VALIDATE_POSTAL_CODE_MESSAGE, validatePostalCode),
        },
      },
      mailingAddress: {
        addressLine1: {
          required: helpers.withMessage(VALIDATE_ADDRESS_LINE1_REQUIRED_MESSAGE, requiredIf(validateMailingAddress)),
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE1_MESSAGE, validateOptionalAddress),
        },
        addressLine2: {
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE2_MESSAGE, validateOptionalAddress),
        },
        addressLine3: {
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE3_MESSAGE, validateOptionalAddress),
        },
        addressLine4: {
          maxLength: maxLength(25),
          validateOptionalAddress: helpers.withMessage(VALIDATE_ADDRESS_LINE4_MESSAGE, validateOptionalAddress),
        },
        postalCode: {
          validateMailingPostalCode: helpers.withMessage(VALIDATE_POSTAL_CODE_MESSAGE, validateMailingPostalCode),
        },
      },
      spousePhn: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
    }
  },
}
</script>
