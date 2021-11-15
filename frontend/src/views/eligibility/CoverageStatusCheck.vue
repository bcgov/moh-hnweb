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
    <div v-if="searched">
    <h2>Transaction Successful</h2>
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
        <AppOutput label="Coverage End Reason" :value="result.coverageEndReason"/>
      </AppCol>
    </AppRow>    
    <br/>
    <AppRow class="row">      
      <AppCol class="col12">
        <p>
          <label>Subsidy Insured Service: </label>{{result.subsidyInsuredService}}
        </p>
      </AppCol>    
    </AppRow>
    <AppRow class="row">  
      <AppCol class="col12">
        <p>
          <label>Date of Last Eye Examination: </label>{{result.dateOfLastEyeExamination}}
        </p>
      </AppCol>
    </AppRow>
    <AppRow class="row">      
      <AppCol class="col12">
        <p>
          <label>Patient Restriction: </label>{{result.patientRestriction}}
        </p>
      </AppCol>               
    </AppRow>
    <AppRow class="row">      
      <AppCol class="col12">
        <p>{{result.carecardWarning}}</p>
      </AppCol>               
    </AppRow>         
  </div>
</template>

<script>
import AppButton from '../../components/AppButton.vue'
import AppCheckbox from '../../components/AppCheckbox.vue'
import AppCol from '../../components/grid/AppCol.vue'
import AppDateInput from '../../components/AppDateInput.vue'
import AppInput from '../../components/AppInput.vue'
import AppOutput from '../../components/AppOutput.vue'
import AppRow from '../../components/grid/AppRow.vue'
import EligibilityService from '../../services/EligibilityService'
import useVuelidate from '@vuelidate/core'
import { validateDOB, validatePHN, VALIDATE_DOB_MESSAGE, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { OUTPUT_DATE_FORMAT } from '../../util/constants'
import { required, helpers } from '@vuelidate/validators'
import dayjs from 'dayjs'

export default {
  name: 'CoverageStatusCheck',
  components: {AppButton, AppCheckbox, AppCol, AppDateInput, AppInput, AppOutput, AppRow},
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
      searched: false,
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
        carecardWarning: '',
        errorMessage: '',
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
  },
  methods: {
    async submitForm() {
      this.searching = true
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert');
          this.result = null
          this.searching = false
          return
        }
        this.result = (await EligibilityService.checkCoverageStatus(this.phn, this.dateOfBirth, this.dateOfService, this.checkSubsidyInsuredService, this.checkLastEyeExam, this.checkPatientRestriction)).data
        if (!this.result.errorMessage || this.result.errorMessage === '') {
          this.searched = true
          this.$store.commit('alert/setSuccessAlert', 'Search complete')
        } else {
          this.$store.commit('alert/setErrorAlert', this.result.errorMessage)
        }

      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.searching = false;
      }
    },
    resetForm() {
      this.phn = ''
      this.dateOfBirth = null
      this.dateOfService = new Date(),
      this.checkSubsidyInsuredService = false
      this.checkLastEyeExam = false
      this.checkPatientRestriction = false
      this.result = null,
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