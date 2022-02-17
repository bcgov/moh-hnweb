<template>
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
  <div v-if="searchOk && result.beneficiaries.length > 0">
    <hr />
    <p>More than....</p>
    <AppSimpleTable id="resultsTable">
      <thead>
        <tr>
          <th>Contract Holder</th>
          <th>Relationship</th>
          <th>Group Number#</th>
          <th>Effective</th>
          <th>Cancel</th>
          <th>Reason</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="beneficiary in result.beneficiaries">
          <BeneficiaryContractPeriod :beneficiary="beneficiary" />
        </tr>
      </tbody>
    </AppSimpleTable>
  </div>
</template>

<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import MspContractsService from '../../services/MspContractsService'
import BeneficiaryContractPeriod from '../../components/mspcontracts/BeneficiaryContractPeriod.vue'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { helpers, required } from '@vuelidate/validators'

export default {
  name: 'GetContractPeriods',
  components: {
    AppSimpleTable,
    BeneficiaryContractPeriod,
  },
  setup() {
    return {
      v$: useVuelidate(),
    }
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
      this.$store.commit('alert/dismissAlert')
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }

        this.result = (await MspContractsService.getContractPeriods({ phn: this.phn })).data
        if (this.result.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.result.message)
          return
        }

        this.searchOk = true
        if (this.result.status === 'success') {
          this.$store.commit('alert/setSuccessAlert', this.result.message || 'Transaction successful')
        } else if (this.result.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.result.message)
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
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
    }
  },
}
</script>
