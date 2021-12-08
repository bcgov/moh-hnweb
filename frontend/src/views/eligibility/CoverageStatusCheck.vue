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
          <AppDateInput :e-model="v$.dateOfBirth" id="dateOfBirth" label="Date Of Birth" v-model="dateOfBirth"/>          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.dateOfService" id ="dateOfService" label="Date Of Service" v-model="dateOfService" />          
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <h3>Patient Status Request</h3>
          <p>Select by clicking one or more boxes</p>
        </AppCol>
        <AppCol>
          <AppCheckbox :errorValue="v$.checkSubsidyInsuredService" id="checkSubsidyInsuredService" label="Check for Subsidy Insured Service" v-model="checkSubsidyInsuredService"/>
          <AppCheckbox :errorValue="v$.checkLastEyeExam" id="checkLastEyeExam" label="Check for Last Eye Exam" v-model="checkLastEyeExam"/>
          <AppCheckbox :errorValue="v$.checkPatientRestriction" id="checkPatientRestriction" label="Check for Patient Restriction" v-model="checkPatientRestriction"/>
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
    <hr />
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="result.phn"/>
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Name" :value="fullName"/>
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Birth Date" :value="result.dateOfBirth"/>
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Gender" :value="result.gender"/>
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="Date Of Service" :value="result.dateOfService"/>
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Eligible on Date of Service?" :value="result.eligibleOnDateOfService"/>
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Coverage End Date" :value="result.coverageEndDate"/>
      </AppCol>
      <AppCol class="col3">
        <AppOutput label="Coverage End Reason" :value="coverageEndReasonText"/>
      </AppCol>
    </AppRow>    
    <br/>
    <AppCard v-if="isPatientStatusRequest">
      <AppRow class="row" v-if="checkSubsidyInsuredService">      
        <AppCol class="col12">
          <p>
            <label>Subsidy Insured Service: </label>{{ subsidyInsuredServiceText }}
          </p>
        </AppCol>    
      </AppRow>
      <AppRow class="row" v-if="checkLastEyeExam">  
        <AppCol class="col12">
          <p>
            <label>Date of Last Eye Examination: </label>{{dateOfLastEyeExaminationText}}
          </p>
        </AppCol>
      </AppRow>
      <AppRow class="row" v-if="checkPatientRestriction">      
        <AppCol class="col12">
          <p>
            <label>Patient Restriction: </label>{{patientRestrictioText}}
          </p>
        </AppCol>               
      </AppRow>
    </AppCard>

    <AppCard v-if="result.careCardWarning">
      <p>{{result.careCardWarning}}</p>
    </AppCard>

    <AppCard v-if="result.clientInstructions">
      <p>{{result.clientInstructions}}</p>
    </AppCard>
  </div>
</template>

<script>
import AppButton from '../../components/AppButton.vue'
import AppCard from '../../components/AppCard.vue'
import AppCheckbox from '../../components/AppCheckbox.vue'
import AppCol from '../../components/grid/AppCol.vue'
import AppDateInput from '../../components/AppDateInput.vue'
import AppInput from '../../components/AppInput.vue'
import AppOutput from '../../components/AppOutput.vue'
import AppRow from '../../components/grid/AppRow.vue'
import EligibilityService from '../../services/EligibilityService'
import useVuelidate from '@vuelidate/core'
import { validateDOB, validatePHN, VALIDATE_DOB_MESSAGE, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { API_DATE_FORMAT, COVERAGE_END_REASONS } from '../../util/constants'
import { required, helpers } from '@vuelidate/validators'
import dayjs from 'dayjs'

export default {
  name: 'CoverageStatusCheck',
  components: {AppButton, AppCard, AppCheckbox, AppCol, AppDateInput, AppInput, AppOutput, AppRow},
  setup() {
    return {
      v$: useVuelidate()}
  },
  data() {
    return {
      phn: '',
      dateOfBirth: null,
      dateOfService: new Date(),
      checkSubsidyInsuredService: false,
      checkLastEyeExam: false,
      checkPatientRestriction: false,
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
      }
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
    coverageEndReasonText() {
      const reasonText = COVERAGE_END_REASONS.get(this.result.coverageEndReason)
      return reasonText ? reasonText : this.result.coverageEndReason
    },
    isPatientStatusRequest() {
      return this.result.subsidyInsuredService || this.result.dateOfLastEyeExamination || this.result.patientRestriction
    },
    subsidyInsuredServiceText() {
      return this.result.subsidyInsuredService === 'N' ? 'THIS IS NOT AN INSURED BENEFIT' : this.result.subsidyInsuredService
    },
    dateOfLastEyeExaminationText() {
      return this.result.dateOfLastEyeExamination === '' ? 'MSP HAS NOT PAID FOR AN EYE EXAM FOR THIS PHN IN THE LAST 24 MTHS FROM TODAY\'S DATE' : this.result.dateOfLastEyeExamination
    },
    patientRestrictionText() {
      return this.result.patientRestriction === 'N' ? 'NO RESTRICTION' : this.result.patientRestriction
    }
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
        this.result = (await EligibilityService.checkCoverageStatus({
          phn: this.phn, 
          dateOfBirth: dayjs(this.dateOfBirth).format(API_DATE_FORMAT), 
          dateOfService: dayjs(this.dateOfService).format(API_DATE_FORMAT), 
          checkSubsidyInsuredService: this.checkSubsidyInsuredService,
          checkLastEyeExam: this.checkLastEyeExam,
          checkPatientRestriction: this.checkPatientRestriction})).data
        
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
        this.searching = false;
      }
    },
    showError(error) {
      this.$store.commit('alert/setErrorAlert', error)
      this.result = {}
      this.searching = false
    },
    resetForm() {
      this.phn = ''
      this.dateOfBirth = null
      this.dateOfService = new Date()
      this.checkSubsidyInsuredService = false
      this.checkLastEyeExam = false
      this.checkPatientRestriction = false
      this.result = null
      this.v$.$reset()
      this.$store.commit("alert/dismissAlert");
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
      dateOfBirth: { 
        required,
        validateDOB: helpers.withMessage(
          VALIDATE_DOB_MESSAGE, validateDOB
        )
      },
      dateOfService: { required },
      checkSubsidyInsuredService: {},
      checkLastEyeExam: {},
      checkPatientRestriction: {},
    }
  }
}

</script>