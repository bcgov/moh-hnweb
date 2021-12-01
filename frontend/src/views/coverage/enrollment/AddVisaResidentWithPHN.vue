<template>
  <div v-if="pageAction === this.PAGE_ACTION.PHN_SEARCH">
    <ResidentPHN @update-resident="updateResident" />
  </div>
  <div v-else-if="pageAction === this.PAGE_ACTION.STUDENT_REGISTRATION">
    <ResidentDetails :resident="this.personResult.person" @register-resident="registerResident" />
  </div>
  <div v-else-if="pageAction === this.PAGE_ACTION.CONFIRMATION">
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="this.personResult.person.phn"/>
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol>
        <AppOutput label="Name" :value="fullName"/>
      </AppCol>
    </AppRow>
  </div>
</template>

<script>
import AppButton from "../../../components/AppButton.vue";
import AppCol from "../../../components/grid/AppCol.vue";
import AppDateInput from "../../../components/AppDateInput.vue";
import AppInput from "../../../components/AppInput.vue";
import AppRow from "../../../components/grid/AppRow.vue";
import AppOutput from "../../../components/AppOutput.vue";

import Datepicker from "vue3-date-time-picker";
import "vue3-date-time-picker/dist/main.css";
import { INPUT_DATE_FORMAT } from "../../../util/constants";

import EnrollmentService from "../../../services/EnrollmentService";
import useVuelidate from "@vuelidate/core";
import { validatePHN, VALIDATE_PHN_MESSAGE } from "../../../util/validators";
import { required, helpers } from "@vuelidate/validators";
import { formatPersonName } from "../../../util/utils"
import ResidentPHN from "./ResidentPHN.vue"
import ResidentDetails from "./ResidentDetails.vue";

export default {
  name: "AddVisaResidentWithPHN",
  components: {
    AppButton,
    AppCol,
    AppInput,
    AppRow,
    AppOutput,
    ResidentPHN,
    ResidentDetails,
  },
  setup() {
    return {};
  },
  data() {
    return {
      pageAction: null,
      registrationOk: false,
      personResult: {
        person: {
          phn: '',
          givenName: '',	
          secondName: '',        
          surname: '',
          dateOfBirth: '',
        },
        status: '',
        message: null,
      },
      registrationResult: {
        status: '',
        message: null,
      }
    };
  },
  created() {
    this.PAGE_ACTION = {
      PHN_SEARCH: 'PHN_SEARCH',
      STUDENT_REGISTRATION: 'STUDENT_REGISTRATION',
      CONFIRMATION: 'CONFIRMATION'
    }
    this.pageAction = this.PAGE_ACTION.PHN_SEARCH
  },
  computed: {
    fullName() {
      return formatPersonName(this.personResult.person)
    },
  },
  methods: {    
    async updateResident(phn) {
      console.log("updateResident")
      console.log(`Resident: [PHN: ${phn}]`
      )
      this.personResult = (await EnrollmentService.getPersonDemographics({ phn: phn }).data)
      if (this.personResult.status === 'error') {
        this.$store.commit('alert/setErrorAlert', this.personResult.message)
        return
      }

      console.log('Result returned')
      console.log(`Result: [PHN: ${this.personResult.person.phn}] [Name: ${this.personResult.person.givenName}] [DOB: ${this.personResult.person.dateOfBirth}]`)
      if (this.personResult.status === 'success') {
        console.log(`Success: ${this.personResult.message}`)        
      }             
      this.pageAction = this.PAGE_ACTION.STUDENT_REGISTRATION
    },
    async registerResident(personDetails) {
      console.log("registerResident")
      console.log(`personDetails: [PHN: ${personDetails.phn}] [Surname: ${personDetails.surname}] [Group Number: ${personDetails.groupNumber}]`
      )
      this.registrationResult = (await EnrollmentService.registerResident({...personDetails}).data)
      if (this.registrationResult.status === 'error') {
        this.$store.commit('alert/setErrorAlert', this.registrationResult.message)
        return
      }
      console.log('Registration Result returned')
      if (this.registrationResult.status === 'success') {
        console.log(`Success: ${this.registrationResult.message}`)        
      }             
      this.pageAction = this.PAGE_ACTION.CONFIRMATION
    }
  },
};
</script>