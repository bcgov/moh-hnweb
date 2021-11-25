<template>
  <div v-if="searchOk != true">
    <ResidentPHN @update-resident="updateResident" />
  </div>
  <div v-else-if="searchOk">
    <ResidentDetails :resident="this.resident" />
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
      resident: {
        phn: "",
        name: "",
        dateOfBirth: "",
      },
    };
  },
  methods: {
    updateResident(result, searchOk) {
      console.log("Resident")
      console.log(
        `Resident: [PHN: ${result.phn}] [Name: ${result.name}] [DOB: ${result.dateOfBirth}]`
      )
      this.resident = result
      this.searchOk = searchOk
    },
  },
};
</script>
