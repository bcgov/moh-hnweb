<template>
  <ResidentNameSearch v-if="isNameSearch" @search-for-candidates="searchForCandidates" />
</template>

<script>
import EnrollmentService from '../../../services/EnrollmentService'
import ResidentNameSearch from '../../../components/coverage/enrollment/ResidentNameSearch.vue'
export default {
  name: 'AddVisaResidentWithoutPHN',
  components: {
    ResidentNameSearch,
  },
  data() {
    return {
      pageAction: null,
      getPersonDetailsResult: {
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
      STUDENT_REGISTRATION: 'STUDENT_REGISTRATION',
      CONFIRMATION: 'CONFIRMATION',
    }
    this.pageAction = this.PAGE_ACTION.NAME_SEARCH
  },
  computed: {
    isNameSearch() {
      return this.pageAction === this.PAGE_ACTION.NAME_SEARCH
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
      try {
        const data = (await EnrollmentService.performNameSearch({
          givenName: ,
          secondName: '',
          surname: '',
          dateOfBirth: '',
          gender: '',
        })).data
        this.getPersonDetailsResult = {
          person: {
            phn: data.phn,
            givenName: data.givenName,
            secondName: data.secondName,
            surname: data.surname,
            dateOfBirth: data.dateOfBirth,
            gender: data.gender,
          },
          status: data.status,
          message: data.message,
        }

        if (this.getPersonDetailsResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.getPersonDetailsResult?.message)
          return
        }

        if (this.getPersonDetailsResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.getPersonDetailsResult?.message)
        }
        this.pageAction = this.PAGE_ACTION.STUDENT_REGISTRATION
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
  },
}
</script>
