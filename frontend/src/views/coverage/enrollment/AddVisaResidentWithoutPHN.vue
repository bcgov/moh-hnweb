<template>
  <AppHelp>
   <div v-if="isNameSearch">
   	<p>The purpose of this screen is to search the Health Registry database to determine if the person already has a Personal Health Number (PHN). It is very important that the person is not assigned a new PHN if they already have one.</p>
   
   	<p>Enter the Surname, First Name, Date of Birth and Gender of the person you wish to add. The screen will either return a list of people to choose from based on the search criteria or allow you to add a new person, creating a new PHN for them.</p>
   </div>
   <div v-else-if="isNameSearchResults">
     <p>This screen generates a list of people to choose from to add to your group account, or allows you to add a new person, creating a new PHN for them.</p>
   
     <p>Review the list to determine if the person you wish to add already has a PHN. Clicking the option "Add" on the right will direct you add the person to your group account with their PHN and complete the required information.</p>
   
     <p>If no matches are found based on the search criteria, click "Create New PHN" for the person you wish to add. This will create a PHN for the person and give you the option to add them to your group account as a study permit holder.</p>
   </div>
   <div v-else-if="isStudentRegistration">
     <p>The purpose of this screen is to enroll study permit holders in MSP and add to your group account.</p>
   
     <p>Enter the requested information to complete enrollment for the study permit holder. If required information is not provided, the system will alert you when you click "Submit"</p>
   
     <p>Some key points to note when entering information:</p>
     <ul>
   	   <li>When entering the "Residence Date", ensure this is the date the student arrived in BC. This date may be different from, either before or after, the issue date of the permit.</li>
   	   <li>Ensure you provide the coverage effective date upon completion of the mandatory wait period. The wait period is calculated based on the issue date of the permit or their arrival date in BC, whichever is later. The wait period is the balance of the month they establish residency plus two months.</li>
   	   <li>When entering the "Coverage Cancellation Date", ensure this date is entered as the last day of the month in which the study permit expires. The coverage cancellation date cannot be entered as an earlier date than is stated on the permit, regardless of the student's anticipated study end date.</li>
   	   <li>Enter the student's Home Address (mandatory). This can be the student's residential address or group's address. Enter a mailing address if applicable.</li>
   	   <li>If the group address contains additional mailing information (e.g., C/O, International Program, etc.), please enter this information in line 1 of the mailing address and the physical address of the group in line 2.</li>
     </ul>
     <br />
     <p>When you have entered all required information and submitted the student's enrollment, please mail or fax a copy of the study permit to HIBC.</p>
   </div>
   <div v-else-if="isConfirmation">
     <p>You have completed MSP enrollment, please record the PHN and mail or fax a copy of the study permit to HIBC.</p>
   </div>
  </AppHelp>
  <NameSearch v-if="isNameSearch" @search-for-candidates="searchForCandidates" :searching="searching" />
  <NameSearchResults v-else-if="isNameSearchResults" :candidates="this.nameSearchResult.candidates" @set-page-action="setPageAction" />
  <ResidentDetailsWithoutPHN v-else-if="isStudentRegistration" :resident="this.registrationPerson" @register-resident="registerResident" :submitting="submitting" />
  <RegistrationConfirmation v-else-if="isConfirmation" :resident="this.registrationPerson" />
</template>

<script>
import EnrollmentService from '../../../services/EnrollmentService'
import NameSearch from '../../../components/coverage/enrollment/NameSearch.vue'
import NameSearchResults from '../../../components/coverage/enrollment/NameSearchResults.vue'
import ResidentDetailsWithoutPHN from '../../../components/coverage/enrollment/ResidentDetailsWithoutPHN.vue'
import RegistrationConfirmation from '../../../components/coverage/enrollment/RegistrationConfirmation.vue'
import { useAlertStore } from '../../../stores/alert'
import { useStudyPermitHolderStore } from '../../../stores/studyPermitHolder'
import { handleServiceError, resolveGender } from '../../../util/utils.js'
import AppHelp from '../../../components/ui/AppHelp.vue'

export default {
  name: 'AddVisaResidentWithoutPHN',
  components: {
    AppHelp,
    NameSearch,
    NameSearchResults,
    ResidentDetailsWithoutPHN,
    RegistrationConfirmation,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      studyPermitHolderStore: useStudyPermitHolderStore(),
    }
  },
  data() {
    return {
      pageAction: null,
      nameSearchResult: {
        candidates: [],
        status: '',
        message: null,
      },
      registrationResult: {
        phn: '',
        status: '',
        message: null,
      },
      registrationPerson: {
        phn: '',
        givenName: '',
        secondName: '',
        surname: '',
        dateOfBirth: '',
        gender: '',
      },
      searching: false,
      submitting: false,
    }
  },
  created() {
    this.PAGE_ACTION = {
      NAME_SEARCH: 'NAME_SEARCH',
      NAME_SEARCH_RESULTS: 'NAME_SEARCH_RESULTS',
      REGISTRATION: 'REGISTRATION',
      CONFIRMATION: 'CONFIRMATION',
    }
    this.pageAction = this.PAGE_ACTION.NAME_SEARCH
  },
  computed: {
    isNameSearch() {
      return this.pageAction === this.PAGE_ACTION.NAME_SEARCH
    },
    isNameSearchResults() {
      return this.pageAction === this.PAGE_ACTION.NAME_SEARCH_RESULTS
    },
    isStudentRegistration() {
      return this.pageAction === this.PAGE_ACTION.REGISTRATION
    },
    isConfirmation() {
      return this.pageAction === this.PAGE_ACTION.CONFIRMATION
    },
  },
  methods: {
    async searchForCandidates(searchCriteria) {
      try {
        this.searching = true
        this.alertStore.dismissAlert()

        let data = (await EnrollmentService.performNameSearch(searchCriteria)).data
        this.nameSearchResult = {
          candidates: data.candidates
            ? data.candidates.map((c) => {
                return { ...c, gender: resolveGender(c.gender) }
              })
            : null,
          status: data.status,
          message: data.message,
        }

        if (this.nameSearchResult?.status === 'error') {
          this.alertStore.setErrorAlert(this.nameSearchResult?.message)
          return
        }

        if (this.nameSearchResult?.status === 'warning') {
          this.alertStore.setWarningAlert(this.nameSearchResult?.message)
        }

        this.registrationPerson = { ...searchCriteria } //make the search criteria available for the Registration screen for populating it in the case where the user wants to use it to create a new PHN
        if (!this.nameSearchResult.candidates || this.nameSearchResult.candidates.length == 0) {
          //found no result so add message for user
          this.alertStore.setInfoAlert(this.nameSearchResult?.message)
        }
        this.pageAction = this.PAGE_ACTION.NAME_SEARCH_RESULTS
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.searching = false
      }
    },
    async registerResident(personDetails) {
      try {
        this.submitting = true
        this.alertStore.dismissAlert()
        this.registrationResult = (await EnrollmentService.registerResident(personDetails)).data

        if (this.registrationResult?.status === 'error') {
          this.alertStore.setErrorAlert(this.registrationResult?.message)
          this.submitting = false
          return
        }

        this.alertStore.setAlertWithInfoForSuccess(this.registrationResult?.message, this.registrationResult?.status)

        this.registrationPerson = { ...personDetails, phn: this.registrationResult.phn }
        this.pageAction = this.PAGE_ACTION.CONFIRMATION
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.submitting = false
      }
    },
    setPageAction(pageAction) {
      this.pageAction = pageAction
    },
  },
}
</script>
