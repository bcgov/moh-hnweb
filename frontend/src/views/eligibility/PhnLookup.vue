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
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import EligibilityService from '../../services/EligibilityService'
import PhnLookupBeneficiary from '../../components/eligibility/PhnLookupBeneficiary.vue'
import useVuelidate from '@vuelidate/core'
import { validateContractNumber, validateGroupNumber, VALIDATE_CONTRACT_NUMBER_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { helpers, required } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert.js'

export default {
  name: 'PhnInquiry',
  components: {
    AppSimpleTable,
    PhnLookupBeneficiary,
  },
  setup() {
    const alertStore = useAlertStore()
    return {
      alertStore,
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
