<template>
  <AppHelp>
    <p>Use this screen to retrieve the address and telephone number recorded by MSP for a group member.</p>
    <p>If the transaction was successful, the group member’s PHN, name, and contract home address and mailing address will be displayed.</p>
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
    <AppSimpleTable id="resultsTable">
      <thead>
        <tr>
          <th>PHN</th>
          <th>Surname</th>
          <th>First Name</th>
          <th>Second Name</th>
          <th>Third Name</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <!-- Only the first row should be shown as that will be the searched on Group Member -->
          <GroupMemberContractBeneficiary :beneficiary="primaryContractBeneficiary" />
        </tr>
      </tbody>
    </AppSimpleTable>
    <div id="personInfo">
      <BeneficiaryAddresses :result="result"></BeneficiaryAddresses>
    </div>
  </div>
</template>
<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import AppHelp from '../../components/ui/AppHelp.vue'
import GroupMemberContractBeneficiary from '../../components/mspcontracts/GroupMemberContractBeneficiary.vue'
import BeneficiaryAddresses from '../../components/mspcontracts/BeneficiaryAddresses.vue'
import useVuelidate from '@vuelidate/core'
import { validateOptionalPHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import MspContractsService from '../../services/MspContractsService'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'GetGroupMembersContractAddress',
  components: {
    AppHelp,
    AppSimpleTable,
    GroupMemberContractBeneficiary,
    BeneficiaryAddresses,
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
        phn: '',
        groupNumber: '',
        groupMemberNumber: '',
        groupMemberDepartmentNumber: '',
        homeAddressLine1: '',
        homeAddressLine2: '',
        homeAddressLine3: '',
        homeAddressLine4: '',
        homeAddressPostalCode: '',
        mailingAddressLine1: '',
        mailingAddressLine2: '',
        mailingAddressLine3: '',
        mailingAddressLine4: '',
        mailingAddressPostalCode: '',
        telephone: '',
        contractInquiryBeneficiaries: [],
      },
    }
  },
  computed: {
    primaryContractBeneficiary() {
      return this.result.contractInquiryBeneficiaries[0]
    },
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
        // This screen calls the Contract Inquiry endpoint as the results shown here are a subset of those returned for Contract Inquiry.
        this.result = (await MspContractsService.inquireContract({ phn: this.phn, groupNumber: this.groupNumber })).data
        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        this.searchOk = true
        this.alertStore.setAlertWithInfoForSuccess(this.result.message, this.result.status)
        if (this.result.message === 'RPBS0059 MORE THAN 20 PERSONS. PLEASE CONTACT MSP') {
          // in legacy no results are shown the > 20 message is returned so clear result info
          this.result.contractInquiryBeneficiaries = []
        }
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
