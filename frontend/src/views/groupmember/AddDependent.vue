<template>
  <AppHelp>
    <p>Use this screen to add, or renew, a spouse or child on a group account.</p>
    <p>This screen has been enhanced to allow you to also add dependents between the age of 19 and 24 who are studying at a Canadian educational institute.</p>
    <p>Before you begin:</p>
    <p>Use the PHN Inquiry screen to confirm that the dependent is currently eligible for publicly funded health care (Eligible? = Y) and, if they are a dependent between 19 and 24 years of age, check with your group member that their dependent's student status is confirmed.</p>
    <p>Student status</p>
    <p>A dependent child who is older than 18 and younger than 25 years of age may still be covered on your group member's account as an overage student if they are attending a school or educational institution located within Canada. If yes, click the "YES" button.</p>
    <p>Studies Completion Date</p>
    <p>
      This is a mandatory field if you have selected the "YES" answer above. Enter the date (the year and month) when the dependent's studies in Canada are expected to finish. This date must be at least three months from today's date and can be up to five years into the future. If the dependent is
      turning 25 during this period, you will be able to confirm them as a student until the month before their 25th birthday.
    </p>
    <p>If the transaction was completed, the PHN is displayed. You may wish to use the PHN with Get Contract Periods to verify that the correct dependent has been added as of the correct date.</p>
  </AppHelp>
  <div id="addGroupMemberDependent" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id="coverageEffectiveDate" label="Coverage Effective Date" monthPicker inputDateFormat="yyyyMM" placeholder="YYYYMM" v-model="coverageEffectiveDate">
            <template #tooltip>Day always defaults to 1st of month</template>
          </AppDateInput>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.phn" id="phn" label="Group Member's PHN" type="text" v-model="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.dependentPhn" id="dependentPhn" label="Dependent's PHN" type="text" v-model.trim="dependentPhn" />
        </AppCol>
        <AppCol class="col4">
          <AppSelect :e-model="v$.relationship" id="relationship" label="Relationship" v-model="relationship" :options="relationshipOptions" />
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
          <AppDateInput :e-model="v$.studentEndDate" id="studentEndDate" label="Student End Date" v-model="studentEndDate" />
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
    <AppButton @click="resetForm" mode="primary" type="button">Add Another Group Member's Dependent</AppButton>
  </div>
</template>

<script>
import AppHelp from '../../components/ui/AppHelp.vue'
import AppRadioButton from '../../components/ui/AppRadioButton.vue'
import AppRadioButtonGroup from '../../components/ui/AppRadioButtonGroup.vue'
import useVuelidate from '@vuelidate/core'
import { helpers, required, requiredIf } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, validateGroupNumber, validatePHN } from '../../util/validators'
import { API_DATE_FORMAT, RELATIONSHIPS, YES_NO_OPTIONS } from '../../util/constants'
import GroupMemberService from '../../services/GroupMemberService'
import { useAlertStore } from '../../stores/alert'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'AddDependent',
  components: { AppRadioButton, AppRadioButtonGroup, AppHelp },
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
      coverageEffectiveDate: this.currentMonth,
      phn: '',
      dependentPhn: '',
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
    // Dependent Relationship drop down options
    this.relationshipOptions = RELATIONSHIPS
    // Yes/No radio button options
    this.YES_NO_OPTIONS = YES_NO_OPTIONS
  },
  computed: {
    // Coverage Effective Date should be the first day of the month. Set entered date to have first day of the month
    adjustToFirstDayOfMonth() {
      return new Date(this.coverageEffectiveDate.year, this.coverageEffectiveDate.month, 1)
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
          await GroupMemberService.addDependent({
            groupNumber: this.groupNumber,
            coverageEffectiveDate: dayjs(this.adjustToFirstDayOfMonth).format(API_DATE_FORMAT),
            phn: this.phn,
            dependentPhn: this.dependentPhn,
            relationship: this.relationship,
            isStudent: this.isStudent,
            studentEndDate: this.studentEndDate ? dayjs(this.studentEndDate).format(API_DATE_FORMAT) : null,
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
      this.groupNumber = ''
      this.coverageEffectiveDate = this.currentMonth
      this.phn = ''
      this.dependentPhn = ''
      this.relationship = ''
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
      coverageEffectiveDate: {
        required,
      },
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      dependentPhn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
      relationship: {
        required,
      },
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
