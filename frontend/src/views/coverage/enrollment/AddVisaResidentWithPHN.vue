<template>
  <div v-if="pageAction === this.PAGE_ACTION.PHN_SEARCH">
    <ResidentPHN @update-resident="updateResident" />
  </div>
  <div v-else-if="pageAction === this.PAGE_ACTION.STUDENT_REGISTRATION">
    <ResidentDetails :resident="this.personResult?.person" @register-resident="registerResident" />
  </div>
  <div v-else-if="pageAction === this.PAGE_ACTION.CONFIRMATION">
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="this.personResult?.person.phn"/>
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
      return formatPersonName(this.personResult?.person)
    },
  },
  methods: {    
    async updateResident(phn) {
      console.log(`Resident: [PHN: ${phn}]`)
      try {
        this.personResult = (await EnrollmentService.getPersonDemographics({ phn: phn })).data
        if (this.personResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.personResult?.message)
          return
        }

        console.log(`Result: [PHN: ${this.personResult?.person.phn}] [Name: ${this.personResult?.person.givenName}] [DOB: ${this.personResult?.person.dateOfBirth}]`)
        if (this.personResult?.status === 'success') {
          console.log(`Success: ${this.personResult?.message}`)        
        } else if (this.personResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.personResult?.message)  
        }          
        this.pageAction = this.PAGE_ACTION.STUDENT_REGISTRATION
      } catch(err) {
        console.log(`Error: ${err}`)
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
    async registerResident(personDetails) {
      console.log(`personDetails: [PHN: ${personDetails.phn}] [Surname: ${personDetails.surname}] [Group Number: ${personDetails.groupNumber}]`)

      try {
        this.registrationResult = (await EnrollmentService.registerResident({...personDetails})).data
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
      } catch (err) {
        console.log(`Error: ${err}`)
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
  },
}
</script>
