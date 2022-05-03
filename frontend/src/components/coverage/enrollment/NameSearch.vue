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
          <GenderRadioButtonGroup :e-model="v$.gender" id="gender" v-model="gender" />
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
import GenderRadioButtonGroup from '../../ui/GenderRadioButtonGroup.vue'
import useVuelidate from '@vuelidate/core'
import dayjs from 'dayjs'
import { API_DATE_FORMAT } from '../../../util/constants'
import { validateDOB, validateFirstName, validateSecondName, validateSurName, VALIDATE_DOB_MESSAGE, VALIDATE_FIRST_NAME_MESSAGE, VALIDATE_SECOND_NAME_MESSAGE, VALIDATE_SURNAME_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { useAlertStore } from '../../../stores/alert'

export default {
  name: 'NameSearch',
  components: {
    GenderRadioButtonGroup,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
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
        validateSurName: helpers.withMessage(VALIDATE_SURNAME_MESSAGE, validateSurName),
      },
      firstName: {
        required,
        validateFirstName: helpers.withMessage(VALIDATE_FIRST_NAME_MESSAGE, validateFirstName),
      },
      secondName: {
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
