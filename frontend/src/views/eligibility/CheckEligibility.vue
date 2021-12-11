<template>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model.trim="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.eligibilityDate"  id="eligibilityDate" label="Date to Check" v-model="eligibilityDate"/>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="result" v-if="searchOk">
  <hr />
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="result.phn"/>      
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Beneficiary on Date checked?" :value="beneficiaryOnDateChecked"/>      
      </AppCol>
    </AppRow>      
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Coverage End Date" :value="result.coverageEndDate"/>      
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Coverage End Reason" :value="coverageEndReason"/>      
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Exclusion Period Date" :value="result.exclusionPeriodEndDate"/>      
      </AppCol>
    </AppRow>

    <AppCard id="clientInstructions" v-if="result.clientInstructions">
      <p>{{result.clientInstructions}}</p>
    </AppCard>
  </div>
</template>

<script>
import AppButton from '../../components/AppButton.vue'
import AppCard from '../../components/AppCard.vue'
import AppCol from '../../components/grid/AppCol.vue'
import AppDateInput from '../../components/AppDateInput.vue'
import AppInput from '../../components/AppInput.vue'
import AppOutput from '../../components/AppOutput.vue'
import AppRow from '../../components/grid/AppRow.vue'
import EligibilityService from '../../services/EligibilityService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT, COVERAGE_END_REASONS } from '../../util/constants'
import dayjs from 'dayjs'

export default {
  name: 'CheckEligibility',
  components: {
    AppButton, AppCard, AppCol, AppDateInput, AppInput, AppOutput, AppRow
  },
  setup() {
    return {
      v$: useVuelidate()}
  },
  data() {
    return {
      phn: '',
      eligibilityDate: new Date(),
      searching: false,
      searchOk: false,
      result: {
        phn: '',
        beneficiaryOnDateChecked: '',
        coverageEndDate: '',
        coverageEndReason: '',
        exclusionPeriodEndDate: '',
        clientInstructions: '',
        status: '',
        message: ''
      }
    }
  },
  computed: {
    beneficiaryOnDateChecked() {
      return this.result.beneficiaryOnDateChecked === 'Y' ? 'YES' : 'NO'
    },
    coverageEndReason() {
      const reasonText = COVERAGE_END_REASONS.get(this.result.coverageEndReason)
      return reasonText ? reasonText : this.result.coverageEndReason
    },
  },
  methods: {
    async submitForm() {
      this.result = null
      this.searching = true
      this.searchOk = false
      this.$store.commit("alert/dismissAlert")
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (await EligibilityService.checkEligibility({
          phn: this.phn,
          eligibilityDate: dayjs(this.eligibilityDate).format(API_DATE_FORMAT)
        })).data

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
      this.eligibilityDate = new Date()
      this.result = null
      this.v$.$reset()
      this.$store.commit("alert/dismissAlert")
      this.searchOk = false
      this.searching = false
    }
  },
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validatePHN
        )
      },
      eligibilityDate: { required },
    }
  }
}
</script>
