<template>
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
            <template v-slot:tooltip> Date always defaults to last day of the month </template>
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
import { helpers, required } from '@vuelidate/validators'
import { VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, validateGroupNumber, validatePHN } from '../../util/validators'
import GroupMemberService from '../../services/GroupMemberService'
import { useAlertStore } from '../../stores/alert'

export default {
  name: 'CancelDependent',
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
          this.alertStore.setSuccessAlert(this.result.message)
        }
      } catch (err) {
        this.alertStore.setErrorAlert(err)
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
      cancelDate: { required },
      cancelReason: { required },
    }
  },
}
</script>

<style scoped></style>
