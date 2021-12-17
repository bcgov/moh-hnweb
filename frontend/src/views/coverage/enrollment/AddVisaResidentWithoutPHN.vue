<template>
  <NameSearch v-if="isNameSearch" @search-for-candidates="searchForCandidates" />
  <NameSearchResults v-else-if="isNameSearchResults" :candidates="this.nameSearchResult.candidates" @add-candidate="addCandidate" />
</template>

<script>
import EnrollmentService from '../../../services/EnrollmentService'
import NameSearch from '../../../components/coverage/enrollment/NameSearch.vue'
import NameSearchResults from '../../../components/coverage/enrollment/NameSearchResults.vue'

export default {
  name: 'AddVisaResidentWithoutPHN',
  components: {
    NameSearch,
    NameSearchResults,
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
        status: '',
        message: null,
      },
    }
  },
  created() {
    this.PAGE_ACTION = {
      NAME_SEARCH: 'NAME_SEARCH',
      NAME_SEARCH_RESULTS: 'NAME_SEARCH_RESULTS',
      STUDENT_REGISTRATION: 'STUDENT_REGISTRATION',
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
      return this.pageAction === this.PAGE_ACTION.STUDENT_REGISTRATION
    },
    isConfirmation() {
      return this.pageAction === this.PAGE_ACTION.CONFIRMATION
    },
  },
  methods: {
    async searchForCandidates(searchCriteria) {
      console.log(`perform name search [${searchCriteria.surname}]`)
      try {
        this.nameSearchResult = (await EnrollmentService.performNameSearch(searchCriteria)).data
        console.log(`Results: Status: [${this.nameSearchResult.status}], Message [${this.nameSearchResult.message}], Size [${this.nameSearchResult.candidates.length}]`)

        if (this.nameSearchResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.nameSearchResult?.message)
          return
        }

        if (this.nameSearchResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.nameSearchResult?.message)
        }

        if (this.nameSearchResult.candidates.length == 0) {
          console.log(`Zero results, setting warning: [${this.nameSearchResult.message}]`)
          this.$store.commit('alert/setWarningAlert', 'No results found')
          return
        } else if (this.nameSearchResult.candidates.length === 1) {
          console.log('One result, load registration')
          this.pageAction = this.PAGE_ACTION.STUDENT_REGISTRATION
        } else {
          console.log('Multiple result, load search results')
          this.pageAction = this.PAGE_ACTION.NAME_SEARCH_RESULTS
        }
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
    addCandidate(candidate) {
      console.log(`adding candidate [${candidate.surname}; ${candidate.phn}`)
    },
  },
}
</script>
