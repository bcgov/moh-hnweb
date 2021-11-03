<script setup>

import AppButton from '../../components/AppButton.vue'
import AppCol from '../../components/grid/AppCol.vue'
import AppDateInput from '../../components/AppDateInput.vue'
import AppInput from '../../components/AppInput.vue'
import AppRow from '../../components/grid/AppRow.vue'

import EligibilityService from '../../services/EligibilityService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'

const v$ = useVuelidate()
</script>
<template>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" label="PHN" type="text" v-model="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput v-model="eligibilityDate" label="Date to Check" :e-model="v$.eligibilityDate"/>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div v-if="searched">
    <h2>Transaction Successful</h2>
    <AppRow>
      <AppCol class="col3">PHN:</AppCol>
      <AppCol>{{ result.phn }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Beneficiary on Date checked?</AppCol>
      <AppCol>{{ result.beneficiaryOnDateChecked ? 'Y' : 'N' }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Coverage End Date:</AppCol>
      <AppCol>{{ result.coverageEndDate }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Reason:</AppCol>
      <AppCol>{{ result.reason }}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Exclusion Period Date:</AppCol>
      <AppCol>{{ result.exclusionPeriodEndDate }}</AppCol>
    </AppRow>
    <br />
    <p>Next Business Service:</p>
    <AppButton @click="$router.push('PhnEnquiry')" mode="secondary" type="button">PHN Enquiry</AppButton>
  </div>
</template>

<script>
export default {
  name: 'CheckEligibility',

  data() {
    return {
      phn: '',
      eligibilityDate: new Date(),
      otherDate: new Date(),
      date: null,
      searching: false,
      searched: false,
      result: {
        phn: '',
        beneficiaryOnDateChecked: false,
        coverageEndDate: '',
        reason: '',
        exclusionPeriodEndDate: ''
      }
    }
  },
  methods: {
    async submitForm() {
      console.log(`eligibilityDate ${this.eligibilityDate}`)
      this.searching = true
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert');
          this.searching = false
          return
        }
        this.result = (await EligibilityService.checkEligibility(this.phn, this.eligibilityDate)).data
        this.searched = true
        this.$store.commit('alert/setSuccessAlert', 'Search complete')
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.searching = false;
      }
    },
    resetForm() {
      this.phn = ''
      this.eligibilityDate = new Date()
      this.v$.$reset()
      this.$store.commit("alert/dismissAlert");
      this.searched = false
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
      otherDate: {required}
    }
  }
}
</script>
