<template>
  <AppHelp>
    <p>The purpose of this screen is to search the Health Registry database to determine if a person already has a Personal Health Number (PHN). It is very important that the person is not assigned a new PHN if they already have one.</p>
    <p>
      Enter the Surname, First Name, Date of Birth and Gender of the person you wish to add. The screen will either return a list of people to chose from based on the search criteria, with the option to "Add" on the right. When you select "Add" you will be directed to Add Study Permit Holder with
      PHN to fill out the required information.
    </p>
    <p>If no matches are found based on the search criteria, enter the information about the person you wish to add. This will create a PHN for the person and give the option to add another Study Permit Holder.</p>
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
          candidates: data.candidates.map((c) => {
            return { ...c, gender: resolveGender(c.gender) }
          }),
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
