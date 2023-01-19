<template>
  <AppHelp>
    <p>
      Use this screen to retrieve the persons and coverage periods associated with a group member’s MSP contract. This can be used to verify that a particular business service has been correctly submitted, for example whether an individual was canceled from the group account. The PHN Lookup screen
      can be used to find a group member or dependent's PHN.
    </p>
    <p>
      If the transaction was successful, persons will be returned: 
      <ul>
        <li>the spouse(s), active and cancelled ·</li>
        <li>the dependent(s)</li>
        <li>the person whose PHN was entered on the input screen</li>
        <li>the group member.</li>      
      </ul>  
    </p>
    <p>Coverage periods are returned in descending order of effective date, for each person.</p>
    <p>A maximum of 10 persons and 5 coverage periods per person can be returned.</p>
    <p>The Cancel Reason, if applicable, is either "ELIGIBLE" (means coverage is cancelled under that Group but the person is still an MSP beneficiary), "INELIGIBLE" (means the person is no longer an MSP beneficiary), or "DECEASED</p>
  </AppHelp>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col2">
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
  <div id="result" v-if="searchOk && result.beneficiaryContractPeriods.length > 0">
    <hr />
    <AppSimpleTable id="resultsTable">
      <thead>
        <tr>
          <th>PHN</th>
          <th>Name</th>
          <th>Contract Holder</th>
          <th>Relationship</th>
          <th>Group Number</th>
          <th>Effective</th>
          <th>Cancel</th>
          <th>Reason</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="beneficiaryContractPeriod in sortedBeneficiaryContractPeriods">
          <BeneficiaryContractPeriod :contractPeriod="beneficiaryContractPeriod" />
        </tr>
      </tbody>
    </AppSimpleTable>
  </div>
</template>

<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import AppHelp from '../../components/ui/AppHelp.vue'
import MspContractsService from '../../services/MspContractsService'
import BeneficiaryContractPeriod from '../../components/mspcontracts/BeneficiaryContractPeriod.vue'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { helpers, required } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'GetContractPeriods',
  components: {
    AppSimpleTable,
    AppHelp,
    BeneficiaryContractPeriod,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  computed: {
    // Compare on PHN, then Effective Date
    sortedBeneficiaryContractPeriods: function () {
      function compareBeneficiaryContractPeriod(a, b) {
        if (a.phn === b.phn) {
          return a.effectiveDate === b.effectiveDate ? 0 : a.effectiveDate < b.effectiveDate ? -1 : 1
        }
        return a.phn < b.phn ? -1 : 1
      }

      return this.result.beneficiaryContractPeriods.sort(compareBeneficiaryContractPeriod)
    },
  },
  data() {
    return {
      phn: '',
      searching: false,
      searchOk: false,
      result: {
        status: '',
        message: '',
        beneficiaryContractPeriods: [],
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

        this.result = (await MspContractsService.getContractPeriods({ phn: this.phn })).data
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
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
    }
  },
}
</script>
