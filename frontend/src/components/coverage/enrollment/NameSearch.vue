<template>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.surname" id="surname" label="Surname" type="text" v-model.trim="surname" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.firstName" id="firstName" label="First Name" type="text" v-model.trim="firstName" />
          <template #tooltip>If you know the gender the client is registered with, select the Male or Female radio button. Or, leave as "Unknown" </template>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.secondName" id="secondName" label="Second Name (Optional)" type="text" v-model.trim="secondName" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppDateInput :e-model="v$.dateOfBirth" id="dateOfBirth" label="Date of Birth" v-model="dateOfBirth" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col3">
          <AppRadioButtonGroup :e-model="v$.gender" id="gender" label="Gender">
            <template #tooltip>If you know the gender the client is registered with, select the Male or Female radio button. Or, leave as "Unknown".</template>
            <template #options>
              <AppRadioButton name="gender" v-for="option in this.GENDERS" :label="option.text" :value="option.value" v-model="gender" />
            </template>
          </AppRadioButtonGroup>
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :submitting="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import dayjs from 'dayjs'
import { API_DATE_FORMAT, GENDERS } from '../../../util/constants'
import { validateDOB, validateFirstName, validateSecondName, validateSurname, VALIDATE_DOB_MESSAGE, VALIDATE_FIRST_NAME_MESSAGE, VALIDATE_SECOND_NAME_MESSAGE, VALIDATE_SURNAME_MESSAGE } from '../../../util/validators'
import { required, helpers, maxLength } from '@vuelidate/validators'
import { useAlertStore } from '../../../stores/alert'
import AppRadioButton from '../../ui/AppRadioButton.vue'
import AppRadioButtonGroup from '../../ui/AppRadioButtonGroup.vue'

export default {
  name: 'NameSearch',
  components: {
    AppRadioButton,
    AppRadioButtonGroup,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
  },
  created() {
    this.GENDERS = GENDERS
  },
  data() {
    return {
      surname: '',
      firstName: '',
      secondName: '',
      dateOfBirth: null,
      gender: '',
    }
  },
  props: {
    searching: {
      required: true,
      type: Boolean,
    },
  },
  methods: {
    async submitForm() {
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.alertStore.setErrorAlert()
          return
        }
        this.$emit('search-for-candidates', {
          surname: this.surname,
          givenName: this.firstName,
          secondName: this.secondName,
          dateOfBirth: dayjs(this.dateOfBirth).format(API_DATE_FORMAT),
          gender: this.gender,
        })
      } catch (err) {
        this.alertStore.setErrorAlert(err)
      }
    },
    resetForm() {
      this.surname = ''
      this.firstName = ''
      this.secondName = ''
      this.dateOfBirth = null
      this.gender = null
      this.v$.$reset()
      this.alertStore.dismissAlert()
    },
  },
  validations() {
    return {
      surname: {
        required,
        maxLength: maxLength(35),
        validateSurname: helpers.withMessage(VALIDATE_SURNAME_MESSAGE, validateSurname),
      },
      firstName: {
        required,
        maxLength: maxLength(15),
        validateFirstName: helpers.withMessage(VALIDATE_FIRST_NAME_MESSAGE, validateFirstName),
      },
      secondName: {
        maxLength: maxLength(15),
        validateSecondName: helpers.withMessage(VALIDATE_SECOND_NAME_MESSAGE, validateSecondName),
      },
      dateOfBirth: {
        required,
        validateDOB: helpers.withMessage(VALIDATE_DOB_MESSAGE, validateDOB),
      },
      gender: {
        required,
      },
    }
  },
}
</script>
