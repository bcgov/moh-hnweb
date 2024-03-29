<template>
  <AppHelp>
    <p>Use this screen to view a group member's MSP coverage under your Group. This screen returns the demographic and coverage information of every person on the MSP Contract, and the group member's Address, Phone Number, and any Group Member or Department Numbers.</p>
    <p>If you do not know the PHN, the PHN Lookup can be used to find a the group member's PHN from their MSP Contract Number.</p>
    <p>If the transaction was successful, the current Contract Address(s) will be displayed - Home and then Mailing. The Phone Number, Group Member Number, and Department Numbers, if present, are displayed under titles.</p>
    <p>If more than 20 persons, active and cancelled, are on a contract, information for the first 20 persons will be displayed along with a message telling you that not all the information could be returned.</p>
    <p>Cancel Reasons, if applicable, are either "ELIGIBLE" (means coverage is cancelled under your Group but the person is still an MSP beneficiary), "INELIGIBLE" (means the person is no longer an MSP beneficiary), or "DECEASED".</p>
  </AppHelp>
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
    <h2>Addresses</h2>
    <BeneficiaryAddresses :result="result" />
    <BeneficiaryMemberDetails :result="result" />
    <h2>Demographics</h2>
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
    <br />
  </div>
</template>
<script>
import AppHelp from '../../components/ui/AppHelp.vue'
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import BeneficiaryAddresses from '../../components/mspcontracts/BeneficiaryAddresses.vue'
import BeneficiaryMemberDetails from '../../components/mspcontracts/BeneficiaryMemberDetails.vue'
import ContractInquiryBeneficiary from '../../components/mspcontracts/ContractInquiryBeneficiary.vue'
import useVuelidate from '@vuelidate/core'
import { validateOptionalPHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import MspContractsService from '../../services/MspContractsService'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'ContractInquiry',
  components: {
    AppHelp,
    AppSimpleTable,
    ContractInquiryBeneficiary,
    BeneficiaryAddresses,
    BeneficiaryMemberDetails,
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
        homeAddressPostalCode: '',
        mailingAddressLine1: '',
        mailingAddressLine2: '',
        mailingAddressLine3: '',
        mailingAddressPostalCode: '',
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
        this.alertStore.setAlertWithInfoForSuccess(this.result.message, this.result.status)
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
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
