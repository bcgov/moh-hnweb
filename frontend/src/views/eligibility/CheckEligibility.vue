<template>
  <AppHelp>
    <p>Use Check Eligibility to determine if a person is a beneficiary of the Medical Services Plan (MSP) on a particular date of service. Check Eligibility returns a "Yes" or "No" for the Personal Health Number (PHN) submitted.</p>
    <ul>
      <li>
        A "Yes" response means that, when checked today, the person is eligible on the "Date to Check" date. The person could subsequently become ineligible for service on that date. If a fee-for-service claim is involved, you may wish to use the MSP Teleplan system or Claims IVR to verify
        eligibility.
      </li>
      <li>If the response is "No", the screen will return additional information about why the coverage was terminated and instructions that should be provided to the individual. If the individual is subject to alternate billing (RCMP or Armed Forces), this information will also be displayed.</li>
    </ul>
  </AppHelp>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model.trim="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.eligibilityDate" id="eligibilityDate" label="Date to Check" v-model="eligibilityDate" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="result" v-if="searchOk">
    <hr />
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="result.phn" />
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Beneficiary on Date checked?" :value="beneficiaryOnDateChecked" />
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Coverage End Date" :value="result.coverageEndDate" />
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Coverage End Reason" :value="coverageEndReason" />
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Exclusion Period End Date" :value="result.exclusionPeriodEndDate" />
      </AppCol>
    </AppRow>

    <AppCard id="clientInstructions" v-if="result.clientInstructions">
      <p>{{ result.clientInstructions }}</p>
    </AppCard>
  </div>
</template>

<script>
import AppCard from '../../components/ui/AppCard.vue'
import AppHelp from '../../components/ui/AppHelp.vue'
import EligibilityService from '../../services/EligibilityService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { API_DATE_FORMAT, COVERAGE_END_REASONS } from '../../util/constants'
import dayjs from 'dayjs'
import { useAlertStore } from '../../stores/alert'

export default {
  name: 'CheckEligibility',
  components: {
    AppCard,
    AppHelp,
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
        message: '',
      },
      showModal: false,
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
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (
          await EligibilityService.checkEligibility({
            phn: this.phn,
            eligibilityDate: dayjs(this.eligibilityDate).format(API_DATE_FORMAT),
          })
        ).data

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
      this.phn = ''
      this.eligibilityDate = new Date()
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
      eligibilityDate: { required },
    }
  },
}
</script>
