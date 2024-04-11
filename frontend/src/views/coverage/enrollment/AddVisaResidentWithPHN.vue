<template>
  <AppHelp>
    <p>The purpose of this screen is to retrieve a person's name, address, gender, date of birth, and verify this is the correct person you want to enroll in MSP and add to your group.</p>
    <p>Results displayed include the client's Personal Health Number, name, birthdate, gender, and date of death (if applicable). Enter the additional required information about the person you wish to enroll in MSP and add to your group.</p>
    <p>Once additional information has been added and submitted, the completed transaction will display the PHN and give the option to add another Study Permit Holder.</p>
  </AppHelp>
  <ResidentPHN v-if="isPhnSearch" @update-resident="updateResident" :searching="searching" />
  <ResidentDetails v-else-if="isStudentRegistration" @register-resident="registerResident" :submitting="submitting" />
  <RegistrationConfirmation v-else-if="isConfirmation" :resident="this.getPersonDetailsResult?.person" />
</template>

<script>
import useVuelidate from '@vuelidate/core'
import EnrollmentService from '../../../services/EnrollmentService'
import { formatPersonName, resolveGender } from '../../../util/utils'
import ResidentPHN from '../../../components/coverage/enrollment/ResidentPHN.vue'
import ResidentDetails from '../../../components/coverage/enrollment/ResidentDetails.vue'
import RegistrationConfirmation from '../../../components/coverage/enrollment/RegistrationConfirmation.vue'
import { useAlertStore } from '../../../stores/alert'
import { useStudyPermitHolderStore } from '../../../stores/studyPermitHolder'
import { handleServiceError } from '../../../util/utils'
import AppHelp from '../../../components/ui/AppHelp.vue'

export default {
  name: 'AddVisaResidentWithPHN',
  components: {
    AppHelp,
    ResidentPHN,
    ResidentDetails,
    RegistrationConfirmation,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      studyPermitHolderStore: useStudyPermitHolderStore(),
      v$: useVuelidate(),
    }
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
      searching: false,
      submitting: false,
    }
  },
  created() {
    this.PAGE_ACTION = {
      PHN_SEARCH: 'PHN_SEARCH',
      REGISTRATION: 'REGISTRATION',
      CONFIRMATION: 'CONFIRMATION',
    }
    this.pageAction = this.$route.query.pageAction ? this.$route.query.pageAction : this.PAGE_ACTION.PHN_SEARCH
  },
  computed: {
    isPhnSearch() {
      return this.pageAction === this.PAGE_ACTION.PHN_SEARCH
    },
    isStudentRegistration() {
      return this.pageAction === this.PAGE_ACTION.REGISTRATION
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
      this.searching = true
      this.alertStore.dismissAlert()

      try {
        const data = (await EnrollmentService.getPersonDetails({ phn: phn })).data
        this.getPersonDetailsResult = {
          person: {
            phn: data.phn,
            givenName: data.givenName,
            secondName: data.secondName,
            surname: data.surname,
            dateOfBirth: data.dateOfBirth,
            dateOfDeath: data.dateOfDeath,
            gender: resolveGender(data.gender),
            address1: data.address1,
            address2: data.address2,
            address3: data.address2,
            city: data.city,
            province: data.province,
            postalCode: data.postalCode,
            mailingAddress1: data.mailingAddress1,
            mailingAddress2: data.mailingAddress2,
            mailingAddress3: data.mailingAddress33,
            mailingAddressCity: data.mailingAddressCity,
            mailingAddressProvince: data.mailingAddressProvince,
            mailingAddressPostalCode: data.mailingAddressPostalCode,
          },
          status: data.status,
          message: data.message,
        }
        if (data?.dateOfDeath && data.dateOfDeath != 'N/A') {
          const dateOfDeathMessage = `A Date of Death was found for this client record. If this is incorrect, confirm the correct PHN was entered and contact HCIM at ${config.HCIM_CONTACT_NO || import.meta.env.VITE_HCIM_CONTACT_NO} (8am to 4:30pm, Mon - Fri).`
          this.alertStore.setErrorAlert(dateOfDeathMessage)
        }
        if (this.getPersonDetailsResult?.status === 'error') {
          this.alertStore.setErrorAlert(this.getPersonDetailsResult?.message)
          return
        }
        if (this.getPersonDetailsResult?.status === 'warning') {
          this.alertStore.setWarningAlert(this.getPersonDetailsResult?.message)
        }
        this.studyPermitHolderStore.resident = this.getPersonDetailsResult.person
        this.pageAction = this.PAGE_ACTION.REGISTRATION
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.searching = false
      }
    },
    async registerResident(personDetails) {
      this.submitting = true
      this.alertStore.dismissAlert()
      try {
        this.registrationResult = (await EnrollmentService.registerResident(personDetails)).data

        if (this.registrationResult?.status === 'error') {
          this.alertStore.setErrorAlert(this.registrationResult?.message)
          return
        }

        this.alertStore.setAlertWithInfoForSuccess(this.registrationResult?.message, this.registrationResult?.status)

        this.getPersonDetailsResult.person = { ...personDetails }
        this.pageAction = this.PAGE_ACTION.CONFIRMATION
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.submitting = false
      }
    },
  },
}
</script>
