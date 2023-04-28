<template>
  <h1>Patient Registration</h1>
  <AppHelp>
    <p>This shows a patient registration history using a patient's PHN and MSP Payee information.</p>
    <p>The MSP Payee number is automatically assigned as part of the permissions the user is given during account creation.</p>
    <p>It returns patient demographic and the registration history, including registration and de-registration dates, and additional information.</p>
  </AppHelp>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model.trim="phn">
            <template #tooltip>Enter the individual’s 10 digit PHN. PHN is a mandatory field. If you leave it blank or enter invalid characters, an edit error message box will be displayed on the input screen.</template>
          </AppInput>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.payee" id="payee" label="MSP Payee" type="text" v-model.trim="payee" disabled="true" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="searching" mode="primary" type="submit" :disabled="payeeInactive">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div id="result" v-if="searchOk">
    <hr />
    <div id="patientDetail" v-if="result.personDetail">
      <AppRow>
        <AppCol class="col2">
          <AppOutput label="PHN" :value="result.personDetail.phn" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Patient" :value="fullName" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Birth Date" :value="result.personDetail.dateOfBirth" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Death Date" :value="result.personDetail.dateOfDeath">
            <template #tooltip>Will display death date in ccyymmdd format, or N/A if person is living.</template>
          </AppOutput>
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="Gender" :value="gender" />
        </AppCol>
      </AppRow>
    </div>

    <AppCard id="clientInstructions" v-if="result.clientInstructions">
      <p>{{ result.clientInstructions }}</p>
    </AppCard>
    <br />
    <div>
      <AppRow>
        <AppCol class="col1">
          <AppOutput label="Payee No" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Reg/DeReg Date">
            <template #tooltip>ccyymmdd</template>
          </AppOutput>
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Current Status" />
        </AppCol>
        <AppCol class="col1">
          <AppOutput label="Admin Code" />
        </AppCol>
        <AppCol class="col6">
          <AppOutput label="Registration Data" />
        </AppCol>
      </AppRow>
    </div>
    <div id="registrationData" v-if="result.patientRegistrationHistory.length > 0">
      <AppRow v-for="registration in result.patientRegistrationHistory" class="detailsRow">
        <PatientRegistrationDetails :registration="registration" />
      </AppRow>
    </div>
  </div>
  <br />
  <Transition>
    <AppInfoPanel :visible="displayMessage" :message="additionalInfoMessage" @close="handleClose()" />
  </Transition>
</template>

<script>
import AppCard from '../../components/ui/AppCard.vue'
import AppHelp from '../../components/ui/AppHelp.vue'
import AppInfoPanel from '../../components/ui/AppInfoPanel.vue'
import PatientRegistrationDetails from '../../components/patientregistration/PatientRegistrationDetails.vue'
import PatientRegistrationService from '../../services/PatientRegistrationService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'ViewPatientRegistration',
  components: {
    AppCard,
    AppHelp,
    PatientRegistrationDetails,
    AppInfoPanel,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      phn: '',
      payee: '',
      searching: false,
      searchOk: false,
      displayMessage: false,
      additionalInfoMessage: '',
      result: {
        additionalInfoMessage: '',
        patientRegistrationHistory: [],
      },
      payeeInactive: false,
    }
  },
  async created() {
    try {
      this.alertStore.dismissAlert()
      const userId = this.$keycloak.tokenParsed?.sub
      const userPayeeMapping = (await PatientRegistrationService.getUserPayeeMapping(userId)).data
      /* If a Payee mapping was found the status will be checked. If there is no active status for a Payee then 
      an error message should be displayed and the field disabled */
      this.payee = userPayeeMapping.payeeNumber
      if (!userPayeeMapping.payeeIsActive) {
        this.payeeInactive = true
        this.alertStore.setErrorAlert(`Payee ${userPayeeMapping.payeeNumber} is not an active PBF clinic.  Please email ${config.PBF_SUPPORT_CONTACT_NO || import.meta.env.VITE_PBF_SUPPORT_CONTACT_NO} if this is incorrect`)
      }
    } catch (err) {
      //Check for Not Found error and add a user friendly error message
      if (typeof err === 'object') {
        const errMessage = String(err)
        if (errMessage.includes('404')) {
          err = 'Error: No MSP Payee Number found for this user'
        }
      }
      handleServiceError(err, this.alertStore, this.$router)
    }
  },

  computed: {
    fullName() {
      let name = ''
      if (this.result.personDetail.surname) {
        name = name + this.result.personDetail.surname
      }
      if (this.result.personDetail.givenName) {
        name = name + ', ' + this.result.personDetail.givenName
      }
      return name
    },

    gender() {
      switch (this.result.personDetail.gender) {
        case 'M':
          return 'Male'
        case 'F':
          return 'Female'
        case 'UN':
          return 'Undifferentiated'
        case 'UNK':
          return 'Unknown'
        default:
          return ''
      }
    },
  },
  methods: {
    async submitForm() {
      this.result = null
      this.searching = true
      this.searchOk = false
      this.displayMessage = false
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        this.result = (
          await PatientRegistrationService.getPatientRegistration({
            phn: this.phn,
            payee: this.payee,
          })
        ).data
        if (this.result.status != 'warning') {
          this.searchOk = true
        }
        this.alertStore.setAlertWithInfoForSuccess(this.result.message)

        if (this.result.additionalInfoMessage != '' && this.result.status != 'warning') {
          this.displayMessage = true
          this.additionalInfoMessage = this.result.additionalInfoMessage
        }

        this.alertStore.setAlertWithInfoForSuccess(this.result.message, this.result.status)
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.searching = false
      }
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.result = {}
      this.searching = false
    },
    resetForm() {
      this.phn = ''
      this.result = null
      this.v$.$reset()
      this.alertStore.dismissAlert()
      this.searchOk = false
      this.searching = false
      this.displayMessage = false
    },
  },
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      payee: {
        required,
      },
    }
  },
}
</script>
<style scoped>
.detailsRow {
  border-top: 2px solid #999999;
  padding-top: 5px;
}
#patientDetail {
  padding-bottom: 20px;
}
</style>
