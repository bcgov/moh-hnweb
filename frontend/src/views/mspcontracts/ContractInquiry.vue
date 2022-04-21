<template>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model.trim="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div v-if="searchOk && result.contractInquiryBeneficiaries.length > 0">
    <hr />
    <div id="personInfo">
      <AppRow>
        <AppCol class="col3"><h2>Home Address</h2> </AppCol>
        <AppCol class="col3"><h2>Mailing Address</h2></AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppOutput label="Line 1" :value="result.homeAddressLine1" />
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="Line 1" :value="result.mailingAddressLine1" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppOutput label="Line 2" :value="result.homeAddressLine2" />
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="Line 2" :value="result.mailingAddressLine2" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppOutput label="Line 3" :value="result.homeAddressLine3" />
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="Line 3" :value="result.mailingAddressLine3" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppOutput label="Line 4" :value="result.homeAddressLine4" />
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="Line 4" :value="result.mailingAddressLine4" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppOutput label="Postal Code" :value="result.homeAddressPostalCode" />
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="Postal Code" :value="result.mailingAddressPostalCode" />
        </AppCol>
      </AppRow>
      <br />
      <AppRow>
        <AppCol class="col3">
          <AppOutput label="Telephone" :value="result.telephone" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppOutput label="Group Member Number" :value="result.groupMemberNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col5">
          <AppOutput label="Group Member Department Number" :value="result.groupMemberDepartmentNumber" />
        </AppCol>
      </AppRow>
    </div>
    <br />
    <AppSimpleTable id="resultsTable">
      <thead>
        <tr>
          <th>PHN</th>
          <th>Name</th>
          <th>Birth Date</th>
          <th>Gender</th>
          <th>Relationship</th>
          <th>Student</th>
          <th>Effective Date</th>
          <th>Cancel Date</th>
          <th>Cancel Reason</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="beneficiary in result.contractInquiryBeneficiaries">
          <ContractInquiryBeneficiary :beneficiary="beneficiary" />
        </tr>
      </tbody>
    </AppSimpleTable>
  </div>
</template>
<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import ContractInquiryBeneficiary from '../../components/mspcontracts/ContractInquiryBeneficiary.vue'
import useVuelidate from '@vuelidate/core'
import { validateOptionalPHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import MspContractsService from '../../services/MspContractsService'
import { useAlertStore } from '../../stores/alert'

export default {
  name: 'ContractInquiry',
  components: {
    AppSimpleTable,
    ContractInquiryBeneficiary,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      phn: '',
      groupNumber: '',
      searching: false,
      searchOk: false,
      result: {
        status: '',
        message: '',
        departmentNumber: '',
        groupMemberNumber: '',
        homeAddressLine1: '',
        homeAddressLine2: '',
        homeAddressLine3: '',
        homePostalCode: '',
        MailinghomeLine1: '',
        MailingAddressLine2: '',
        MailingAddressLine3: '',
        MailingPostalCode: '',
        telephone: '',
        contractInquiryBeneficiaries: [],
      },
    }
  },
  methods: {
    async submitForm() {
      this.result = null
      this.searching = true
      this.searchOk = false
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (await MspContractsService.inquireContract({ phn: this.phn, groupNumber: this.groupNumber })).data
        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        this.searchOk = true
        this.alertStore.setAlert({ message: this.result.message, type: this.result.status })
      } catch (err) {
        this.alertStore.setErrorAlert(err)
      } finally {
        this.searching = false
      }
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.result = {}
      this.searching = false
    },
    resetForm() {
      this.phn = ''
      this.groupNumber = ''
      this.result = null
      this.v$.$reset()
      this.alertStore.dismissAlert()
      this.searchOk = false
      this.searching = false
    },
  },
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
    }
  },
}
</script>
