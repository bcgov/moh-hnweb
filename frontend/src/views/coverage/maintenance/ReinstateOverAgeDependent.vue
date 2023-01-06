<template>
  <AppHelp>
    <p>Use this screen to Reinstate an OverAge Dependent. If you do not know the PHN, use the PHN Lookup screen.</p>
    <p>
      Before you begin:<br />
      Use the PHN Inquiry screen to confirm that the dependent is currently eligible for publicly funded health care (Eligible? = Y) and, if they are a dependent between 19 and 24 years of age, check with your group member that their dependent's student status is confirmed.
    </p>
    <p>
      Student status<br />
      A dependent aged 18 and 9 months through to 24 and 9 months may still be covered on your group members account as an overage student if they are attending a school or educational institution. If yes, click the "YES" button. If the dependent is over 18 and 9 months and is not studying within
      Canada, you will NOT be able to add them as a dependent on an account.
    </p>
    <p>
      Studies Completion Date<br />
      Enter the date when the dependent’s studies are expected to finish. This date must be at least three months from today's date and can be up to five years into the future. If the dependent is turning 25 during this period, you will be able to confirm them as a student until the month before
      their 25th birthday.
    </p>
    <p>If the transaction is successful, the PHN is displayed. You may use the PHN with the Get Contract Periods screen to verify the coverage.</p>
  </AppHelp>

  <div id="reinstateOverAgeDependent" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppRow>
          <AppCol class="col4">
            <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model="phn" />
          </AppCol>
        </AppRow>
        <AppRow>
          <AppCol class="col4">
            <AppInput :e-model="v$.dependentPhn" id="dependentPhn" label="Dependent's PHN" type="text" v-model.trim="dependentPhn" />
          </AppCol>
        </AppRow>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.dependentDateOfBirth" id="dependentDateOfBirth" label="Dependent's Birth Date" v-model="dependentDateOfBirth" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppRadioButtonGroup :e-model="v$.isStudent" id="isStudent" label="Is this Dependent attending a Canadian Educational Institution?">
            <template #tooltip> Click either Yes or No </template>
            <template #options>
              <AppRadioButton name="isStudent" v-for="option in this.YES_NO_OPTIONS" :label="option.text" :value="option.value" v-model="isStudent" />
            </template>
          </AppRadioButtonGroup>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col6">
          <AppOutput label="If Yes, enter the expected date studies in Canada will be completed" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.studentEndDate" id="studentEndDate" label="Student End Date" monthPicker inputDateFormat="yyyyMM" placeholder="YYYYMM" v-model="studentEndDate">
            <template #tooltip>Date always defaults to last day of the month</template>
          </AppDateInput>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="submitting" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <div id="confirmation" v-else>
    <br />
    <p>PHN: {{ result?.phn }}</p>
    <AppButton @click="resetForm" mode="primary" type="button">Reinstate Another OverAge Dependent</AppButton>
  </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import AppHelp from '../../../components/ui/AppHelp.vue'
import AppRadioButton from '../../../components/ui/AppRadioButton.vue'
import AppRadioButtonGroup from '../../../components/ui/AppRadioButtonGroup.vue'
import { helpers, required, requiredIf } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, validateGroupNumber, validatePHN } from '../../../util/validators'
import { API_DATE_FORMAT, YES_NO_OPTIONS } from '../../../util/constants'
import MaintenanceService from '../../../services/MaintenanceService'
import { useAlertStore } from '../../../stores/alert'
import AppCol from '../../../components/grid/AppCol.vue'
import { handleServiceError } from '../../../util/utils'

export default {
  name: 'ReinstateOverAgeDependent',
  components: { AppRadioButton, AppRadioButtonGroup, AppCol, AppHelp },
  setup() {
    const currentMonth = {
      month: new Date().getMonth(),
      year: new Date().getFullYear(),
    }

    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
      currentMonth,
    }
  },
  data() {
    return {
      inputFormActive: true,
      submitting: false,
      groupNumber: '',
      phn: '',
      dependentPhn: '',
      dependentDateOfBirth: null,
      relationship: '',
      isStudent: '',
      studentEndDate: null,
      result: {
        phn: '',
        status: '',
        message: '',
      },
    }
  },
  created() {
    // Yes/No radio button options
    this.YES_NO_OPTIONS = YES_NO_OPTIONS
  },
  computed: {
    // Student End Date should be the last day of the month. In JavaScript this can be done by using day 0 of the next month
    studentEndDateAdjusted() {
      return new Date(this.studentEndDate.year, this.studentEndDate.month + 1, 0)
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
          await MaintenanceService.reinstateOverAgeDependent({
            groupNumber: this.groupNumber,
            phn: this.phn,
            dependentPhn: this.dependentPhn,
            dependentDateOfBirth: dayjs(this.dependentDateOfBirth).format(API_DATE_FORMAT),
            isStudent: this.isStudent,
            studentEndDate: this.studentEndDate ? dayjs(this.studentEndDateAdjusted).format(API_DATE_FORMAT) : null,
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
      this.groupNumber = ''
      this.phn = ''
      this.dependentPhn = ''
      this.dependentDateOfBirth = ''
      this.isStudent = ''
      this.studentEndDate = null
      this.result = null
      this.inputFormActive = true
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
  },
  validations() {
    return {
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      dependentPhn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      dependentDateOfBirth: { required },
      isStudent: { required },
      studentEndDate: {
        requiredIfIsStudent: requiredIf(() => {
          return this.isStudent === 'Y'
        }),
      },
    }
  },
}
</script>
