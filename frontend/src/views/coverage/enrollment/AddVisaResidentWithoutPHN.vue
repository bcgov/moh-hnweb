<template>
  <NameSearch v-if="isNameSearch" @search-for-candidates="searchForCandidates" />
  <NameSearchResults v-else-if="isNameSearchResults" :candidates="this.nameSearchResult.candidates" />
  <ResidentDetailsWithoutPHN v-else-if="isStudentRegistration" :resident="this.registrationPerson" @register-resident="registerResident" />
  <RegistrationConfirmation v-else-if="isConfirmation" :resident="this.registrationPerson" />
</template>

<script>
import EnrollmentService from '../../../services/EnrollmentService'
import NameSearch from '../../../components/coverage/enrollment/NameSearch.vue'
import NameSearchResults from '../../../components/coverage/enrollment/NameSearchResults.vue'
import ResidentDetailsWithoutPHN from '../../../components/coverage/enrollment/ResidentDetailsWithoutPHN.vue'
import RegistrationConfirmation from '../../../components/coverage/enrollment/RegistrationConfirmation.vue'

export default {
  name: 'AddVisaResidentWithoutPHN',
  components: {
    NameSearch,
    NameSearchResults,
    ResidentDetailsWithoutPHN,
    RegistrationConfirmation,
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
      console.log(`perform name search [${searchCriteria.surname}] [${searchCriteria.givenName}]`)
      try {
        this.nameSearchResult = (await EnrollmentService.performNameSearch(searchCriteria)).data
        console.log(`Results: Status: [${this.nameSearchResult.status}], Message [${this.nameSearchResult.message}], Size [${this.nameSearchResult.candidates?.length}]`)

        if (this.nameSearchResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.nameSearchResult?.message)
          return
        }

        if (this.nameSearchResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.nameSearchResult?.message)
        }

        if (!this.nameSearchResult.candidates || this.nameSearchResult.candidates.length == 0) {
          //found no result so need to register and enroll without a PHN
          console.log(`Zero results, need to register and enroll without a PHN`)

          this.registrationPerson = { ...searchCriteria }
          this.$store.commit('alert/setInfoAlert', 'No results were returned. Please ensure you have entered the correct information')
          this.pageAction = this.PAGE_ACTION.REGISTRATION
        } else if (this.nameSearchResult.candidates.length === 1) {
          //found 1 result so can auto select it for use in Register with PHN
          console.log('One result, load registration')
          this.$store.commit('studyPermitHolder/setResident', this.nameSearchResult.candidates[0])
          this.$router.push({ name: 'AddVisaResidentWithPHN', query: { pageAction: 'REGISTRATION' } })
        } else {
          console.log('Multiple result, load search results')
          this.pageAction = this.PAGE_ACTION.NAME_SEARCH_RESULTS
        }
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
    async registerResident(personDetails) {
      try {
        this.registrationResult = (await EnrollmentService.registerResident({ ...personDetails })).data

        if (this.registrationResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.registrationResult?.message)
          return
        }

        if (this.registrationResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.registrationResult?.message)
        }
        this.registrationPerson = { ...personDetails, phn: this.registrationResult.phn }
        this.pageAction = this.PAGE_ACTION.CONFIRMATION
        this.$store.commit('alert/setSuccessAlert', 'Transaction Successful')
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
  },
}
</script>
