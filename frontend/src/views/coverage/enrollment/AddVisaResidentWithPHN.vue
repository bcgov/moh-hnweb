<template>
  <div v-if="searchOk != true && registrationOk != true">
    <ResidentPHN @update-resident="updateResident" />
  </div>
  <div v-else-if="searchOk">
    <ResidentDetails :resident="this.result" @register-resident="registerResident" />
  </div>
  <div v-else-if="registrationOk">
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="registrationResult.phn"/>
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
      searchOk: false,
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
      this.searchOk = true
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
      this.searchOk = false
      this.registrationOk = true
    }
  },
};
</script>
