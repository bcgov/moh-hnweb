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
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
      </form>
  </div>
  <br />
  <div v-if="searchOk">
    <h2>Transaction Successful</h2>
    <AppSimpleTable>
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
import AppButton from '../../components/AppButton.vue'
import AppCol from '../../components/grid/AppCol.vue'
import AppInput from '../../components/AppInput.vue'
import AppRow from '../../components/grid/AppRow.vue'
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import EligibilityService from '../../services/EligibilityService'
import PhnLookupBeneficiary from '../../components/eligibility/PhnLookupBeneficiary.vue'
import useVuelidate from '@vuelidate/core'
import { validateContractNumber, validateGroupNumber, VALIDATE_CONTRACT_NUMBER_MESSAGE, VALIDATE_GROUP_NUMBER_MESSAGE } from '../../util/validators'
import { helpers, required } from '@vuelidate/validators'

export default {
  name: 'PhnInquiry',
  components: {
    AppButton, AppCol, AppInput, AppRow, AppSimpleTable, PhnLookupBeneficiary
  },
  setup() {
    return {
      v$: useVuelidate()}
  },
  data() {
    return {
      groupNumber: '6337109',
      contractNumber: '680342326',
      searching: false,
      searchOk: false,
      result: {
        errorMessage: '',
        warningMessage: '',
        beneficiaries: [],
      },
    }
  },
  methods: {
    async submitForm() {
      this.searching = true
      try {
        const isValid = await this.v$.$validate()      
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (await EligibilityService.lookupPhn({groupNumber: this.groupNumber, contractNumber: this.contractNumber})).data
        console.log(this.result.beneficiaries)

        if (!this.result.errorMessage || this.result.errorMessage === '') {
          this.searchOk = true

          if (this.result.warningMessage) {
            this.$store.commit('alert/setWarningAlert', this.result.warningMessage)  
          } else {
            this.$store.commit('alert/setSuccessAlert', 'Search complete')
          }
        } else {
          this.$store.commit('alert/setWarningAlert', this.result.errorMessage)
          this.result = null
          this.searchOk = false
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
      this.contractNumber = ''
      this.groupNumber = ''
      this.result = null
      this.v$.$reset()
      this.$store.commit("alert/dismissAlert")
      this.searchOk = false
      this.searching = false
    }
  },
  validations() {
    return {
      contractNumber: {
        required,
        validateContractNumber: helpers.withMessage(
          VALIDATE_CONTRACT_NUMBER_MESSAGE, validateContractNumber
        )
      },
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(
          VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber
        )
      },
    }
  }
}
</script>
