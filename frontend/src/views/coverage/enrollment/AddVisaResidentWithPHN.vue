<template>
  <AppHelp>
   <div v-if="isPhnSearch">
   	<p>The purpose of this screen is to retrieve a person's name, address, gender, and date of birth by entering their Personal Health Number (PHN). Once you submit the PHN you can verify this is the correct person you want add to your group account and enroll in MSP.</p>
   </div>
   <div v-else-if="isStudentRegistration">
     <p>Review the information that has auto populated to verify this is the correct person you would like to add to your group account. Once you have verified, you can enter the requested information to complete MSP enrollment for the study permit holder. If required information is not provided, the system will alert you when you click "Submit"</p>
   
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
        if (data.phn) {
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
              mailingAddress3: data.mailingAddress3,
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
        } else {
          // Empty PHN in the response indicates that no person was found for the provided PHN
          this.alertStore.setWarningAlert(data.message)
        }
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
