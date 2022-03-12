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
    <br />
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
    </div>
  </div>
</template>
<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import GroupMemberContractBeneficiary from '../../components/mspcontracts/GroupMemberContractBeneficiary.vue'
import useVuelidate from '@vuelidate/core'
import { validateOptionalPHN, validateGroupNumber, VALIDATE_PHN_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import MspContractsService from '../../services/MspContractsService'

export default {
  name: 'GetGroupMembersContractAddress',
  components: {
    AppSimpleTable,
    GroupMemberContractBeneficiary,
  },
  setup() {
    return {
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
      this.$store.commit('alert/dismissAlert')
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        /*
          This screen calls the Contract Inquiry endpoint as the results shown here are a subset of those returned for Contract Inquiry. This does not
          break security as currently all roles with permissions for R37(Get Group Member's Contract Address) also have permission for R40(Contract Inquiry).
        */
        this.result = (await MspContractsService.inquireContract({ phn: this.phn, groupNumber: this.groupNumber })).data
        if (this.result.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.result.message)
          return
        }

        this.searchOk = true
        if (this.result.status === 'success') {
          this.$store.commit('alert/setSuccessAlert', this.result.message || 'Transaction successful')
        } else if (this.result.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.result.message)
          if (this.result.message === 'RPBS0059 MORE THAN 20 PERSONS. PLEASE CONTACT MSP') {
            // in legacy no results are shown the > 20 message is returned so clear result info
            this.result.contractInquiryBeneficiaries = []
          }
        }
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.searching = false
      }
    },
    showError(error) {
      this.$store.commit('alert/setErrorAlert', error)
      this.result = {}
      this.searching = false
    },
    resetForm() {
      this.phn = ''
      this.groupNumber = ''
      this.result = null
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
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
