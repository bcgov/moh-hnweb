<template>
  <AppHelp>
    <p>Use this screen to find the Personal Health Numbers (PHNs) for a group member and their dependents. Once found, the PHN is used to submit other web business services, such as Cancel Group Member.</p>
    <p>Fields</p>
    <p>Group member: The group member must have coverage, currently or in the past, under this group number. If a group member or dependent has not yet been added to your group, please collect their PHNs from an MSP application or change form completed by the group member.</p>
    <p>MSP Contract Number: This is found on the MSP monthly Summary of MSP Group Account Enrolment (listed as “Account Number”) . If you have a Contract Number that appears to have only 8 digits, you must enter it with a leading zero i.e., as 012345678.</p>
    <p>If the transaction was successful, the Personal Health Numbers of your group member and all dependents covered on that contract, currently and in the past, will be returned, along with their demographic details.</p>
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
          <AppInput :e-model="v$.contractNumber" id="contractNumber" label="MSP Contract Number" type="text" v-model.trim="contractNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div v-if="searchOk">
    <hr />
    <AppSimpleTable id="resultsTable">
      <thead>
        <tr>
          <th>PHN</th>
          <th>Name</th>
          <th>Date of Birth</th>
          <th>Gender</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="beneficiary in result.beneficiaries">
          <PhnLookupBeneficiary :beneficiary="beneficiary" />
        </tr>
      </tbody>
    </AppSimpleTable>
  </div>
</template>

<script>
import AppHelp from '../../components/ui/AppHelp.vue'
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import EligibilityService from '../../services/EligibilityService'
import PhnLookupBeneficiary from '../../components/eligibility/PhnLookupBeneficiary.vue'
import useVuelidate from '@vuelidate/core'
import { validateContractNumber, validateGroupNumber, VALIDATE_CONTRACT_NUMBER_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { helpers, required } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert.js'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'PhnInquiry',
  components: {
    AppHelp,
    AppSimpleTable,
    PhnLookupBeneficiary,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      groupNumber: '',
      contractNumber: '',
      searching: false,
      searchOk: false,
      result: {
        status: '',
        message: '',
        beneficiaries: [],
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
        this.result = (await EligibilityService.lookupPhn({ groupNumber: this.groupNumber, contractNumber: this.contractNumber })).data
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
      this.contractNumber = ''
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
      contractNumber: {
        required,
        validateContractNumber: helpers.withMessage(VALIDATE_CONTRACT_NUMBER_MESSAGE, validateContractNumber),
      },
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
    }
  },
}
</script>
