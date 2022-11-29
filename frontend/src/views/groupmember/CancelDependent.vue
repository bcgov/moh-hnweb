<template>
  <AppHelp>
    <p>Use this screen to initiate the cancellation of a group member’s spouse or child from your MSP group account. To complete the cancellation, you must submit a Group Change Request form to Health Insurance BC. If you do not know the PHN, the PHN Lookup screen can be used.</p>
    <p>The cancellation date must be no more than two months prior to current date. Note that MSP coverage can only be cancelled as of the end of the month with this screen. To cancel coverage as of the effective date please contact the help desk.</p>
    <p>Most retroactive cancellations are limited to two months on group accounts. Group enrolment is cancelled on the last day of the month; however, if a group member leaves before becoming eligible, their group coverage can be cancelled as of the effective date.</p>
    <p>"No longer a child" is used when a child aged 19 to 24 with YES student status either leaves full-time studies or enters a marriage or marriage-like relationship</p>
    <p>Contact the Help Desk if you need to change or correct a dependent's cancellation date.</p>
    <p>
      MSP requires the reason for cancellation, the person’s current address and, if the person has moved outside of BC, the date of the move. To complete a cancellation, you must submit a Group Change Request (HLTH 217, found at
      <a href="http://www.gov.bc.ca/mspgroupplanadministratorforms" target="_blank">www.gov.bc.ca/mspgroupplanadministratorforms</a>), or submit a written request to Health Insurance BC at:<br/>
      <p align="center">
      PO Box 9140 <br />
      Stn Prov Govt <br />
      Victoria BC V8W 9E5</p>
    </p>
    <p>You can use the PHN with the Get Contract Periods screen to verify the cancellation.</p>
  </AppHelp>
  <div id="cancelDependent" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="Group Member's PHN" type="text" v-model="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppSelect id="relationship" label="Relationship (Optional)" v-model="relationship" :options="relationshipOptions" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.dependentPhn" id="dependentPhn" label="Dependent's PHN" type="text" v-model="dependentPhn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.cancelDate" id="cancelDate" label="Coverage Cancel Date" monthPicker inputDateFormat="yyyy-MM" placeholder="YYYY-MM" v-model="cancelDate">
            <template #tooltip> Date always defaults to last day of the month </template>
          </AppDateInput>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppSelect :e-model="v$.cancelReason" id="cancelReason" label="Cancel Reason" v-model="cancelReason" :options="cancelReasons" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <div id="confirmation" v-if="!inputFormActive">
    <br />
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Cancel Another Group Member's Dependent</AppButton>
  </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import AppHelp from '../../components/ui/AppHelp.vue'
import { helpers, required } from '@vuelidate/validators'
import { VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, validateGroupNumber, validatePHN } from '../../util/validators'
import GroupMemberService from '../../services/GroupMemberService'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'CancelDependent',
  components: {
    AppHelp,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      inputFormActive: true,
      submitting: false,
      groupNumber: '',
      phn: '',
      dependentPhn: '',
      relationship: '',
      cancelDate: null,
      cancelReason: '',
      result: {
        phn: '',
        status: '',
        message: '',
      },
      cancelReasons: [
        { text: 'Divorced', value: 'I' },
        { text: 'No longer a child', value: 'P' },
        { text: 'Out of province move', value: 'E' },
      ],
      relationshipOptions: [
        { text: 'Spouse', value: 'SP' },
        { text: 'Dependent', value: 'DP' },
      ],
    }
  },
  computed: {
    // Cancel Date should be the last day of the month. In JavaScript this can be done by using day 0 of the next month
    cancelDateAdjusted() {
      return new Date(this.cancelDate.year, this.cancelDate.month + 1, 0)
    },
  },
  methods: {
    async submitForm() {
      this.submitting = true
      this.alertStore.dismissAlert()

      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }

        this.result = (
          await GroupMemberService.cancelDependent({
            phn: this.phn,
            dependentPhn: this.dependentPhn,
            groupNumber: this.groupNumber,
            relationship: this.relationship,
            coverageCancelDate: this.cancelDateAdjusted,
            cancelReason: this.cancelReason,
          })
        ).data

        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.inputFormActive = false
          this.alertStore.setInfoAlert(this.result.message)
        }
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.submitting = false
      }
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
      this.result = {}
    },
    resetForm() {
      this.phn = ''
      this.dependentPhn = ''
      this.groupNumber = ''
      this.relationship = ''
      this.cancelDate = null
      this.cancelReason = ''
      this.result = null
      this.inputFormActive = true
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
  },
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      dependentPhn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
      cancelDate: {
        required,
      },
      cancelReason: { required },
    }
  },
}
</script>

<style scoped></style>
