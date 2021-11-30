<template>
  <div  v-if="action === this.PAGE_ACTION.PHN_SEARCH">
    <ResidentPHN @update-resident="updateResident" />
  </div>
  <div v-if="action === this.PAGE_ACTION.STUDENT_REGISTRATION">
    <ResidentDetails :resident="this.result" @register-resident="registerResident" />
  </div>
  <div v-if="action === this.PAGE_ACTION.CONFIRMATION">
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="registrationResult.phn"/>
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol>
        <AppOutput label="Name" :value="registrationResult.name"/>
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
      result: {
        phn: '',
        name: '',
        dateOfBirth: '',
      },
      registrationResult: {
        phn: '',
        name: '',
        errorMessage: null,
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
  methods: {    
    async updateResident(phn) {
      console.log("updateResident")
      console.log(`Resident: [PHN: ${phn}]`
      )
      this.result = (await EnrollmentService.getPersonDemographics({
        phn: phn
      }))
      console.log('Result returned')
      console.log(`Result: [PHN: ${this.result.phn}] [Name: ${this.result.name}] [DOB: ${this.result.dateOfBirth}]`)
      this.action = this.PAGE_ACTION.STUDENT_REGISTRATION
    },
    async registerResident(personDetails) {
      console.log("registerResident")
      console.log(`personDetails: [PHN: ${personDetails.phn}] [Group Number: ${personDetails.groupNumber}]`
      )
      this.registrationResult = (await EnrollmentService.registerResident({
        ...personDetails
      }))
      console.log('Registration Result returned')
      console.log(`Registration Result: [PHN: ${this.registrationResult.phn}] [Name: ${this.registrationResult.name}] [DOB: ${this.registrationResult.errorMessage}]`)
      this.action = this.PAGE_ACTION.CONFIRMATION
    }
  },
};
</script>
