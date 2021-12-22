<template>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.surname" id="surname" label="Surname" type="text" v-model.trim="surname" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
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
        <AppCol class="col4">
          <AppDateInput :e-model="v$.dateOfBirth" id="dateOfBirth" label="Date of Birth" v-model="dateOfBirth" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col4">
          <AppRadioButtonGroup :e-model="v$.gender" id="gender" label="Gender" :group="this.GENDER_RADIO_BUTTON_GROUP" v-model="gender" />
        </AppCol>
      </AppRow>
    </form>
  </div>
</template>

<script>
import AppRadioButtonGroup from '../../ui/AppRadioButtonGroup.vue'
import useVuelidate from '@vuelidate/core'
import dayjs from 'dayjs'
import { API_DATE_FORMAT } from '../../../util/constants'
import { required, helpers } from '@vuelidate/validators'

export default {
  name: 'ResidentNameSearch',
  components: {
    AppRadioButtonGroup,
  },
  setup() {
    return {
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      surname: 'N',
      firstName: 'BARNEY',
      secondName: '',
      dateOfBirth: null,
      gender: null,
      searching: false,
    }
  },
  created() {
    this.GENDER_RADIO_BUTTON_GROUP = [
      { label: 'Male', value: 'M' },
      { label: 'Female', value: 'F' },
      { label: 'Unknown', value: 'U' },
    ]
  },
  computed: {
    inputClass() {
      return {
        'error-input': this.v$.gender.$error,
      }
    },
  },
  methods: {
    async submitForm() {
      this.searching = true
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.$store.commit('alert/setErrorAlert')
          this.searching = false
          return
        }
        console.log(`Person [${this.firstName}]`)
        this.$emit('search-for-candidates', {
          surname: this.surname,
          givenName: this.firstName,
          secondName: this.secondName,
          dateOfBirth: dayjs(this.dateOfBirth).format(API_DATE_FORMAT),
          gender: this.gender,
        })
      } catch (err) {
        console.log(`Error ${err}`)
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.searching = false
      }
    },
    resetForm() {
      this.surname = ''
      this.firstName = ''
      this.secondName = ''
      this.dateOfBirth = null
      this.gender = null
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
      this.searching = false
    },
  },
  validations() {
    return {
      surname: {
        // required,
      },
      firstName: {
        // required,
      },
      secondName: {},
      dateOfBirth: {
        // required,
      },
      gender: {
        // required,
      },
    }
  },
}
</script>
