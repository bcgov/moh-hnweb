<template>
  <div v-if="pageAction === this.PAGE_ACTION.PHN_SEARCH">
    <ResidentPHN @update-resident="updateResident" />
  </div>
  <div v-else-if="pageAction === this.PAGE_ACTION.STUDENT_REGISTRATION">
    <ResidentDetails :resident="this.getPersonDetailsResult?.person" @register-resident="registerResident" />
  </div>
  <div v-else-if="pageAction === this.PAGE_ACTION.CONFIRMATION">
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="this.getPersonDetailsResult?.person.phn"/>
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
import AppCol from "../../../components/grid/AppCol.vue";
import AppRow from "../../../components/grid/AppRow.vue";
import AppOutput from "../../../components/AppOutput.vue";
import EnrollmentService from "../../../services/EnrollmentService";
import { formatPersonName } from "../../../util/utils"
import ResidentPHN from "./ResidentPHN.vue"
import ResidentDetails from "./ResidentDetails.vue";

export default {
  name: "AddVisaResidentWithPHN",
  components: {
    AppCol,
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
      getPersonDetailsResult: {
        person: {
          phn: '',
          givenName: '',	
          secondName: '',        
          surname: '',
          dateOfBirth: '',
          gender: '',
        },
        status: '',
        message: null,
      },
      registrationResult: {
        status: '',
        message: null,
      },
    };
  },
  created() {
    this.PAGE_ACTION = {
      PHN_SEARCH: 'PHN_SEARCH',
      STUDENT_REGISTRATION: 'STUDENT_REGISTRATION',
      CONFIRMATION: 'CONFIRMATION'
    },
    this.pageAction = this.PAGE_ACTION.PHN_SEARCH
  },
  computed: {
    fullName() {
      return formatPersonName(this.getPersonDetailsResult?.person)
    },
  },
  methods: {    
    async updateResident(phn) {
      console.log(`Resident: [PHN: ${phn}]`)
      try {
        this.getPersonDetailsResult = (await EnrollmentService.getPersonDetails({ phn: phn })).data
        if (this.getPersonDetailsResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.getPersonDetailsResult?.message)
          return
        }

        console.log(`Result: [PHN: ${this.getPersonDetailsResult?.person.phn}] [Name: ${this.getPersonDetailsResult?.person.givenName}] [DOB: ${this.getPersonDetailsResult?.person.dateOfBirth}]`)
        if (this.getPersonDetailsResult?.status === 'success') {
          console.log(`Success: ${this.getPersonDetailsResult?.message}`)        
        } else if (this.getPersonDetailsResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.getPersonDetailsResult?.message)  
        }          
        this.pageAction = this.PAGE_ACTION.STUDENT_REGISTRATION
      } catch(err) {
        console.log(`Error: ${err}`)
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
    async registerResident(personDetails) {
      console.log(`[Group Number: ${personDetails.groupNumber}]`)

      try {
        this.registrationResult = (await EnrollmentService.registerResident({
          phn: this.getPersonDetailsResult?.person.phn,
          dateOfBirth: this.getPersonDetailsResult?.person.dateOfBirth,
          givenName: this.getPersonDetailsResult?.person.givenName,
          secondName: this.getPersonDetailsResult?.person.secondName,
          surname: this.getPersonDetailsResult?.person.surname,
          gender: this.getPersonDetailsResult?.person.gender, 
          ...personDetails})).data
        if (this.registrationResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.registrationResult?.message)
          return
        }
        console.log('Registration Result returned')
        if (this.registrationResult?.status === 'success') {
          console.log(`Success: ${this.registrationResult.message}`)        
        } else if (this.registrationResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.registrationResult?.message)  
        }
        this.pageAction = this.PAGE_ACTION.CONFIRMATION
        this.$store.commit('alert/setSuccessAlert', 'Transaction Successful')
      } catch (err) {
        console.log(`Error: ${err}`)
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
  },
}
</script>
