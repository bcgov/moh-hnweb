<template>
  <NameSearch v-if="isNameSearch" @search-for-candidates="searchForCandidates" :searching="searching" />
  <NameSearchResults v-else-if="isNameSearchResults" :candidates="this.nameSearchResult.candidates" />
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

export default {
  name: 'AddVisaResidentWithoutPHN',
  components: {
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

        this.nameSearchResult = (await EnrollmentService.performNameSearch(searchCriteria)).data

        if (this.nameSearchResult?.status === 'error') {
          this.alertStore.setErrorAlert(this.nameSearchResult?.message)
          return
        }

        if (this.nameSearchResult?.status === 'warning') {
          this.alertStore.setWarningAlert(this.nameSearchResult?.message)
        }

        if (!this.nameSearchResult.candidates || this.nameSearchResult.candidates.length == 0) {
          //found no result so need to register and enroll without a PHN

          this.registrationPerson = { ...searchCriteria }
          this.alertStore.setInfoAlert(this.nameSearchResult?.message)
          this.pageAction = this.PAGE_ACTION.REGISTRATION
        } else if (this.nameSearchResult.candidates.length === 1) {
          //found 1 result so can auto select it for use in Register with PHN
          this.studyPermitHolderStore.resident = this.nameSearchResult.candidates[0]
          this.$router.push({ name: 'AddVisaResidentWithPHN', query: { pageAction: 'REGISTRATION' } })
        } else {
          this.pageAction = this.PAGE_ACTION.NAME_SEARCH_RESULTS
        }
      } catch (err) {
        this.alertStore.setErrorAlert(err)
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

        this.alertStore.setAlert({ message: this.registrationResult?.message, type: this.registrationResult?.status })

        this.registrationPerson = { ...personDetails, phn: this.registrationResult.phn }
        this.pageAction = this.PAGE_ACTION.CONFIRMATION
      } catch (err) {
        this.alertStore.setErrorAlert(err)
      } finally {
        this.submitting = false
      }
    },
  },
}
</script>
