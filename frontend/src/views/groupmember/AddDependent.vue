<template>
  <div id="addDependent" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id="coverageEffectiveDate" label="Coverage Effective Date" v-model="coverageEffectiveDate" />
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
        <AppCol class="col4">
          <p>Is this Dependent attending a Canadian Educational Institution?</p>
          <p>(Click either Yes or No)</p>
        </AppCol>
        <AppCol>
          <AppCheckbox :errorValue="v$.isAttendingCanadianEducationalInstitute" id="isAttendingCanadianEducationalInstitute" label="Yes" v-model="isAttendingCanadianEducationalInstitute" />
          <AppCheckbox :errorValue="v$.isNotAttendingCanadianEducationalInstitute" id="isNotAttendingCanadianEducationalInstitute" label="No" v-model="isNotAttendingCanadianEducationalInstitute" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <p>If Yes, enter the expected date studies in Canada will be completed</p>
        </AppCol>
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
import AppCheckbox from '../../components/ui/AppCheckbox.vue'
import useVuelidate from '@vuelidate/core'
import { helpers, required, requiredIf } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, validateGroupNumber, validatePHN } from '../../util/validators'
import { API_DATE_FORMAT, RELATIONSHIPS } from '../../util/constants'
import GroupMemberService from '../../services/GroupMemberService'

export default {
  name: 'AddDependent',
  components: { AppCheckbox },
  setup() {
    return {
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      inputFormActive: true,
      submitting: false,
      groupNumber: '',
      coverageEffectiveDate: dayjs().startOf('month').toDate(),
      phn: '',
      dependentPhn: '',
      relationship: '',
      isAttendingCanadianEducationalInstitute: false,
      isNotAttendingCanadianEducationalInstitute: false,
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
  },
  computed: {
    // Cancel Date should be the last day of the month. In JavaScript this can be done by using day 0 of the next month
    adjustToLastDayOfMonth() {
      return new Date(this.cancelDate.year, this.cancelDate.month + 1, 0)
    },
  },
  methods: {
    async submitForm() {
      this.submitting = true
      this.$store.commit('alert/dismissAlert')

      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }

        this.result = (
          await GroupMemberService.addDependent({
            groupNumber: this.groupNumber,
            coverageEffectiveDate: dayjs(this.coverageEffectiveDate).format(API_DATE_FORMAT),
            phn: this.phn,
            dependentPhn: this.dependentPhn,
            relationship: this.relationship,
            isAttendingCanadianEducationalInstitute: this.isAttendingCanadianEducationalInstitute,
            isNotAttendingCanadianEducationalInstitute: this.isNotAttendingCanadianEducationalInstitute,
            studentEndDate: dayjs(this.studentEndDate).format(API_DATE_FORMAT),
          })
        ).data

        if (this.result.status === 'error') {
          this.$store.commit('alert/setErrorAlert', this.result.message)
          return
        }

        if (this.result?.status === 'success') {
          this.inputFormActive = false
          this.$store.commit('alert/setSuccessAlert', 'Transaction Successful')
        }
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.submitting = false
      }
    },
    showError(error) {
      this.$store.commit('alert/setErrorAlert', error)
      this.result = {}
    },
    resetForm() {
      this.groupNumber = ''
      this.coverageEffectiveDate = dayjs().startOf('month').toDate()
      this.phn = ''
      this.dependentPhn = ''
      this.relationship = ''
      this.isAttendingCanadianEducationalInstitute = false
      this.isNotAttendingCanadianEducationalInstitute = false
      this.studentEndDate = null
      this.result = null
      this.inputFormActive = true
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
    },
  },
  validations() {
    return {
      groupNumber: {
        required,
        validateGroupNumber: helpers.withMessage(VALIDATE_GROUP_NUMBER_MESSAGE, validateGroupNumber),
      },
      coverageEffectiveDate: { required },
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
      isAttendingCanadianEducationalInstitute: {},
      isNotAttendingCanadianEducationalInstitute: {},
      studentEndDate: {
        requiredIfIsAttending: requiredIf(this.isAttendingCanadianEducationalInstitute),
      },
    }
  },
}
</script>
