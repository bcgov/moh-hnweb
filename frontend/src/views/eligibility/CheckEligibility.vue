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
  <div v-if="searchOk">
    <h2>Transaction Successful</h2>
    <AppRow>
      <AppCol class="col3">PHN:</AppCol>
      <AppCol>{{ result.phn }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Beneficiary on Date checked?</AppCol>
      <AppCol>{{ result.beneficiaryOnDateChecked }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Coverage End Date:</AppCol>
      <AppCol>{{ result.coverageEndDate }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Reason:</AppCol>
      <AppCol>{{ result.coverageEndReason }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Exclusion Period Date:</AppCol>
      <AppCol>{{ result.exclusionPeriodEndDate }}</AppCol>
    </AppRow>
  </div>
</template>

<script>
import AppButton from '../../components/AppButton.vue'
import AppCol from '../../components/grid/AppCol.vue'
import AppDateInput from '../../components/AppDateInput.vue'
import AppInput from '../../components/AppInput.vue'
import AppRow from '../../components/grid/AppRow.vue'
import EligibilityService from '../../services/EligibilityService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT, OUTPUT_DATE_FORMAT } from '../../util/constants'
import dayjs from 'dayjs'

export default {
  name: 'CheckEligibility',
  components: {
    AppButton, AppCol, AppDateInput, AppInput, AppRow
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
        errorMessage: '',
      }
    }
  },
  methods: {
    async submitForm() {
      this.searching = true
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert')
          this.result = null
          this.searching = false
          return
        }
        this.result = (await EligibilityService.checkEligibility({
          phn: this.phn,
          eligibilityDate: dayjs(this.eligibilityDate).format(API_DATE_FORMAT)
        })).data
        if (!this.result.errorMessage || this.result.errorMessage === '') {
          this.searchOk = true
          this.$store.commit('alert/setSuccessAlert', 'Search complete')
        } else {
          this.$store.commit('alert/setErrorAlert', this.result.errorMessage)
          this.result = null
          this.searchOk = false
        }
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.searching = false
      }
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
