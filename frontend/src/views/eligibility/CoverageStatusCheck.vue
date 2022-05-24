<template>
  <AppHelp
    ><p>MSP Direct Coverage Status Check provides the same functionality as the Medical Services Plan (MSP) Teleplan system and Claims IVR.</p>
    <p>Use the MSP Coverage Status Check to check if a person is eligible to have their claim for a health service paid by the MSP. The MSP Coverage Status Check returns a "YES" or "NO" answer for the Personal Health Number (PHN) submitted</p>
    <p>As well as checking eligibility, you can use the MSP Coverage Status Check to submit one or more requests (Patient Status answers are only returned if the person is eligible "YES" eligibility response)</p>
    <p>
      Edit error messages appear on the input screen and tell you what fields need correcting. These will be mandatory fields that have been left blank or fields that have been completed with invalid data. Make the necessary correction(s) on the input screen and click Submit to resubmit the business
      service.
    </p></AppHelp
  >
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" tooltip type="text" v-model.trim="phn">
            <template v-slot:tooltip>Enter the individual’s 10 digit PHN. PHN is a mandatory field. If you leave it blank or enter invalid characters, an edit error message box will be displayed on the input screen.</template>
          </AppInput>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.dateOfBirth" id="dateOfBirth" label="Date Of Birth" v-model="dateOfBirth" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.dateOfService" id="dateOfService" label="Date Of Service" v-model="dateOfService">
            <template v-slot:tooltip>The Date of Service defaults on the screen to today's date. The year (CCYY), month (MM) and day (DD) can be overridden. If you override the date with another date, it cannot be greater than today's date or more than 18 months in the past.</template>
          </AppDateInput>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4 flex">
          <div class="flex">
            <h4>Patient Status Request</h4>
            <AppTooltip>
              <p>
                Subsidy Insured Service: the screen will display whether MSP pays for Subsidy Insured Services for this PHN, including the number of services last paid to date, whether the beneficiary must pay, the number of paid services by calendar year. This information will be of interest to
                supplementary benefits providers.
              </p>
              <p>
                Last Eye Exam: the screen will return the date of the last paid eye exam for this PHN within the last 24 months. This information will be of interest to those performing eye exams. Patient Restriction:, if the patient has been restricted to a single general practitioner, the MSP
                Coverage Status Check will advise you to review the MSP Bulletin.
              </p>
            </AppTooltip>
          </div>
          <div class="checkbox-group">
            <AppCheckbox id="checkSubsidyInsuredService" label="Check for Subsidy Insured Service" v-model="checkSubsidyInsuredService" />
            <AppCheckbox id="checkLastEyeExam" label="Check for Last Eye Exam" v-model="checkLastEyeExam" />
          </div>
        </AppCol>
        <AppCol>

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
        <AppOutput label="Name" :value="fullName" />
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Birth Date" :value="result.dateOfBirth" />
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Gender" :value="gender" />
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Date Of Service" :value="result.dateOfService" />
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Eligible on Date of Service?" :value="eligibleOnDateOfService">
          <template v-slot:tooltip
            ><p>If the response is "YES", the person is eligible to have their claim for that date of service paid by MSP.</p>
            <p>
              If the response is "No", the screen will return additional information about why the coverage was terminated and instructions that should be provided to the individual If the individual is subject to alternate billing (RCMP or Armed Forces), this information will also be displayed
            </p></template
          >
        </AppOutput>
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Coverage End Date" :value="result.coverageEndDate" />
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Coverage End Reason" :value="coverageEndReason" />
      </AppCol>
    </AppRow>
    <br />
    <AppCard id="patientStatusRequest" v-if="isPatientStatusRequest">
      <AppRow class="row" v-if="result.subsidyInsuredService">
        <AppCol class="col12">
          <p><label>Subsidy Insured Service: </label>{{ subsidyInsuredService }}</p>
        </AppCol>
      </AppRow>
      <AppRow class="row" v-if="result.dateOfLastEyeExamination">
        <AppCol class="col12">
          <p><label>Date of Last Eye Examination: </label>{{ dateOfLastEyeExamination }}</p>
        </AppCol>
      </AppRow>
      <AppRow class="row" v-if="result.patientRestriction">
        <AppCol class="col12">
          <p><label>Patient Restriction: </label>{{ patientRestriction }}</p>
        </AppCol>
      </AppRow>
    </AppCard>

    <AppCard id="careCardWarning" v-if="result.careCardWarning">
      <p>{{ result.careCardWarning }}</p>
    </AppCard>

    <AppCard id="clientInstructions" v-if="result.clientInstructions">
      <p>{{ result.clientInstructions }}</p>
    </AppCard>
  </div>
</template>

<script>
import AppCard from '../../components/ui/AppCard.vue'
import AppCheckbox from '../../components/ui/AppCheckbox.vue'
import AppHelp from '../../components/ui/AppHelp.vue'
import AppTooltip from '../../components/ui/AppTooltip.vue'
import EligibilityService from '../../services/EligibilityService'
import useVuelidate from '@vuelidate/core'
import { validateDOB, validateMinimumDate, validatePHN, VALIDATE_DOB_MESSAGE, VALIDATE_MINIMUM_DATE_MESSAGE, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { API_DATE_FORMAT, COVERAGE_END_REASONS } from '../../util/constants'
import { required, helpers } from '@vuelidate/validators'
import dayjs from 'dayjs'

import { useAlertStore } from '../../stores/alert.js'

export default {
  name: 'CoverageStatusCheck',
  components: { AppCard, AppCheckbox, AppHelp, AppTooltip },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      phn: '',
      dateOfBirth: null,
      dateOfService: new Date(),
      checkSubsidyInsuredService: false,
      checkLastEyeExam: false,
      searching: false,
      searchOk: false,
      result: {
        phn: '',
        surname: '',
        givenName: '',
        secondName: '',
        dateOfBirth: '',
        gender: '',
        dateOfService: '',
        eligibleOnDateOfService: false,
        coverageEndDate: '',
        coverageEndReason: '',
        subsidyInsuredService: '',
        dateOfLastEyeExamination: '',
        patientRestriction: '',
        careCardWarning: '',
        clientInstructions: '',
        status: '',
        message: '',
      },
    }
  },
  computed: {
    fullName() {
      let name = ''
      if (this.result.surname) {
        name = name + this.result.surname
      }
      if (this.result.givenName) {
        name = name + ', ' + this.result.givenName
      }
      if (this.result.secondName) {
        name = name + ' ' + this.result.secondName
      }
      return name
    },
    isPatientStatusRequest() {
      return this.result.subsidyInsuredService || this.result.dateOfLastEyeExamination || this.result.patientRestriction
    },
    coverageEndReason() {
      const reasonText = COVERAGE_END_REASONS.get(this.result.coverageEndReason)
      return reasonText ? reasonText : this.result.coverageEndReason
    },
    eligibleOnDateOfService() {
      return this.result.eligibleOnDateOfService === 'Y' ? 'YES' : 'NO'
    },
    gender() {
      switch (this.result.gender) {
        case 'M':
          return 'MALE'
        case 'F':
          return 'FEMALE'
        case 'U':
          return 'UNKNOWN'
        default:
          return this.result.gender
      }
    },
    subsidyInsuredService() {
      switch (this.result.subsidyInsuredService) {
        case 'Y':
          return 'THIS IS AN INSURED BENEFIT'
        case 'N':
          return 'THIS IS NOT AN INSURED BENEFIT'
        default:
          return `SERVICES PD TO DATE - ${this.result.subsidyInsuredService}`
      }
    },
    dateOfLastEyeExamination() {
      switch (this.result.dateOfLastEyeExamination) {
        case 'N':
          return "MSP HAS NOT PAID FOR AN EYE EXAM FOR THIS PHN IN THE LAST 24 MTHS FROM TODAY'S DATE"
        case 'ERROR':
          return 'EYE SYSTEM UNAVAILABLE'
        default:
          return this.result.dateOfLastEyeExamination
      }
    },
    patientRestriction() {
      switch (this.result.patientRestriction) {
        case 'Y':
          return 'SEE MSP BULLETIN'
        case 'N':
          return 'NO RESTRICTION'
        case 'ERROR':
          return 'UNAVAILABLE - CONTACT MSP'
        default:
          return this.result.patientRestriction
      }
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
          await EligibilityService.checkCoverageStatus({
            phn: this.phn,
            dateOfBirth: dayjs(this.dateOfBirth).format(API_DATE_FORMAT),
            dateOfService: dayjs(this.dateOfService).format(API_DATE_FORMAT),
            checkSubsidyInsuredService: this.checkSubsidyInsuredService,
            checkLastEyeExam: this.checkLastEyeExam,
            checkPatientRestriction: false,
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
      this.dateOfBirth = null
      this.dateOfService = new Date()
      this.checkSubsidyInsuredService = false
      this.checkLastEyeExam = false
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
      dateOfBirth: {
        required,
        validateDOB: helpers.withMessage(VALIDATE_DOB_MESSAGE, validateDOB),
        validateMinimumDate: helpers.withMessage(VALIDATE_MINIMUM_DATE_MESSAGE, validateMinimumDate),
      },
      dateOfService: {
        required,
        validateMinimumDate: helpers.withMessage(VALIDATE_MINIMUM_DATE_MESSAGE, validateMinimumDate),
      },
    }
  },
}
</script>
<style>
.checkbox-group {
  margin-bottom: 10px;
}
</style>
