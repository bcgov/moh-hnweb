<template>
  <div id="addGroupMemberDependent" v-if="inputFormActive">
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col4">
          <AppInput :e-model="v$.groupNumber" id="groupNumber" label="Group Number" type="text" v-model.trim="groupNumber" />
        </AppCol>
        <AppCol class="col4">
          <AppDateInput :e-model="v$.coverageEffectiveDate" id="coverageEffectiveDate" label="Coverage Effective Date" tooltip tooltipText="Day always defaults to 1st of month" monthPicker inputDateFormat="yyyyMM" placeholder="YYYYMM" v-model="coverageEffectiveDate" />
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
          <YesNoRadioButtonGroup :e-model="v$.isStudent" id="isStudent" label="Is this Dependent attending a Canadian Educational Institution?" tooltip tooltipText="Click either Yes or No" v-model="isStudent" />
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
import YesNoRadioButtonGroup from '../../components/ui/YesNoRadioButtonGroup.vue'
import useVuelidate from '@vuelidate/core'
import { helpers, required, requiredIf } from '@vuelidate/validators'
import dayjs from 'dayjs'
import { VALIDATE_GROUP_NUMBER_MESSAGE, VALIDATE_PHN_MESSAGE, validateGroupNumber, validatePHN } from '../../util/validators'
import { API_DATE_FORMAT, RELATIONSHIPS } from '../../util/constants'
import GroupMemberService from '../../services/GroupMemberService'

export default {
  name: 'AddDependent',
  components: { YesNoRadioButtonGroup },
  setup() {
    const currentMonth = {
      month: new Date().getMonth(),
      year: new Date().getFullYear(),
    }

    return {
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
            coverageEffectiveDate: dayjs(this.adjustToFirstDayOfMonth).format(API_DATE_FORMAT),
            phn: this.phn,
            dependentPhn: this.dependentPhn,
            relationship: this.relationship,
            isStudent: this.isStudent,
            studentEndDate: this.studentEndDate ? dayjs(this.studentEndDate).format(API_DATE_FORMAT) : null,
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
      this.coverageEffectiveDate = this.currentMonth
      this.phn = ''
      this.dependentPhn = ''
      this.relationship = ''
      this.isStudent = ''
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
