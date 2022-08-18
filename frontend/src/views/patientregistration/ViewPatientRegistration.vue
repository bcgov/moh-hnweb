<template>
  <AppHelp>
    <p>This shows a patient registration history using a pateint's PHN and MSP Payee information.</p>
    <p>Enter the individuals 10 digit PHN. The PHN is a mandatory field, if you leave it blank or enter invalid characters, an edit error message will be displayed.</p>
    <p>The MSP Payee number is automatically assigned as part of the permissions the user is given during account creation</p>
    <p>It returns patient demographic and registration history, including registration and de-registration dates, and additional information</p>
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
          <AppInput :e-model="v$.payee" id="payee" label="MSP Payee" type="text" v-model.trim="payee" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="searching" mode="primary" type="submit">Submit</AppButton>
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
          <AppOutput label="Patient" :value="result.personDetail.givenName" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="BirthDate" :value="result.personDetail.dateOfBirth" />
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="DeathDate" :value="result.personDetail.dateOfDeath" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Gender" :value="result.personDetail.gender" />
        </AppCol>
      </AppRow>
    </div>

    <AppCard id="clientInstructions" v-if="result.clientInstructions">
      <p>{{ result.clientInstructions }}</p>
    </AppCard>
    <br />
    <div id="registrationResult">
      <AppRow>
        <AppCol class="col2">
          <AppOutput label="Payee" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Reg/DeReg Date" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Current Status" />
        </AppCol>
        <AppCol class="col3">
          <AppOutput label="Administration Code" />
        </AppCol>
        <AppCol class="col2">
          <AppOutput label="Registration Data" />
        </AppCol>
      </AppRow>
    </div>
    <div id="registrationData" v-if="result.patientRegistrationHistory.length > 0">
      <AppRow v-for="registration in result.patientRegistrationHistory">
        <PatientRegistration :registration="registration" />
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
import PatientRegistration from '../../components/patientregistration/PatientRegistration.vue'
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
    PatientRegistration,
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
        clientInstructions: '',
        status: '',
        message: '',
        personDetail: '',
        additionalInfoMessage: '',
        patientRegistrationHistory: [],
      },
      showModal: false,
    }
  },
  created() {
    this.payee = 'A0053'
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
          await PatientRegistrationService.getRegistrationHistory({
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
      this.payee = ''
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
    }
  },
}
</script>
<style></style>
