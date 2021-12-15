<template>
  <ResidentPHN v-if="isPhnSearch" @update-resident="updateResident" />
  <ResidentDetails v-else-if="isStudentRegistration" :resident="this.getPersonDetailsResult?.person" @register-resident="registerResident" />
  <RegistrationConfirmation v-else-if="isConfirmation" :resident="this.getPersonDetailsResult?.person" />
</template>

<script>
import EnrollmentService from '../../../services/EnrollmentService'
import { formatPersonName } from '../../../util/utils'
import ResidentPHN from '../../../components/coverage/enrollment/ResidentPHN.vue'
import ResidentDetails from '../../../components/coverage/enrollment/ResidentDetails.vue'
import RegistrationConfirmation from '../../../components/coverage/enrollment/RegistrationConfirmation.vue'

export default {
  name: 'AddVisaResidentWithPHN',
  components: {
    ResidentPHN,
    ResidentDetails,
    RegistrationConfirmation,
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
    }
  },
  created() {
    ;(this.PAGE_ACTION = {
      PHN_SEARCH: 'PHN_SEARCH',
      STUDENT_REGISTRATION: 'STUDENT_REGISTRATION',
      CONFIRMATION: 'CONFIRMATION',
    }),
      (this.pageAction = this.PAGE_ACTION.PHN_SEARCH)
  },
  computed: {
    isPhnSearch() {
      return this.pageAction === this.PAGE_ACTION.PHN_SEARCH
    },
    isStudentRegistration() {
      return this.pageAction === this.PAGE_ACTION.STUDENT_REGISTRATION
    },
    isConfirmation() {
      return this.pageAction === this.PAGE_ACTION.CONFIRMATION
    },
    fullName() {
      return formatPersonName(this.getPersonDetailsResult?.person)
    },
  },
  methods: {
    async updateResident(phn) {
      try {
        const data = (await EnrollmentService.getPersonDetails({ phn: phn })).data
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
    async registerResident(personDetails) {
      try {
        this.registrationResult = (
          await EnrollmentService.registerResident({
            phn: this.getPersonDetailsResult?.person.phn,
            dateOfBirth: this.getPersonDetailsResult?.person.dateOfBirth,
            givenName: this.getPersonDetailsResult?.person.givenName,
            secondName: this.getPersonDetailsResult?.person.secondName,
            surname: this.getPersonDetailsResult?.person.surname,
            gender: this.getPersonDetailsResult?.person.gender,
            ...personDetails,
          })
        ).data

        if (this.registrationResult?.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.registrationResult?.message)
          return
        }

        if (this.registrationResult?.status === 'warning') {
          this.$store.commit('alert/setWarningAlert', this.registrationResult?.message)
        }
        this.pageAction = this.PAGE_ACTION.CONFIRMATION
        this.$store.commit('alert/setSuccessAlert', 'Transaction Successful')
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
  },
}
</script>
