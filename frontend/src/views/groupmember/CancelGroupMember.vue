<template>
  <AppHelp>
    <p>
      Use this screen to initiate the cancellation of a group member from your MSP group account. To complete the cancellation, you must submit a Coverage Cancellation form to Health Insurance BC. Any dependents will automatically be cancelled, along with the group member. If you do not know the
      PHN, the PHN Lookup screen can be used.
    </p>
    <p>Coverage Cancel Date</p>
    <p>Most retroactive cancellations are limited to two months on group accounts. Note that MSP coverage can only be cancelled as of the end of the month with this screen. To cancel a group member’s coverage as of the effective date please contact the help desk.</p>
    <p>Cancel Reason</p>
    <p>This is a dropdown list box with a default Cancel Reason of "Employer's Request." The other available Cancel Reason is "Out of Province Move."</p>
    <p>To change a cancel date, use the Change Cancel Date screen.</p>
    <p>
      MSP requires the reason for cancellation, the person’s current address and, if the person has moved outside of BC, the date of the move. To complete a cancellation, you must submit a Coverage Cancellation form (HLTH 217, found at <a>www.gov.bc.ca/mspgroupplanadministratorforms</a>), or submit
      a written request including your group number, employee account number(s) and name(s), and all the information required on the form to Health Insurance BC at:
    </p>
    <p>PO Box 9140</p>
    <p>Stn Prov Govt</p>
    <p>Victoria BC V8W 9E5</p>
    <p>You can use the PHN with the Get Contract Periods screen to verify the cancellation.</p>
  </AppHelp>
  <div id="cancelGroupMember" v-if="inputFormActive">
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
    <AppButton @click="resetForm" mode="primary" type="button">Cancel Another Group Member</AppButton>
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
  name: 'CancelGroupMember',
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
      cancelDate: null,
      cancelReason: '',
      result: {
        phn: '',
        status: '',
        message: '',
      },
      cancelReasons: [
        { text: "Employer's Request", value: 'K' },
        { text: 'Out of province move', value: 'E' },
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
          await GroupMemberService.cancelGroupMember({
            phn: this.phn,
            groupNumber: this.groupNumber,
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
      this.groupNumber = ''
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
