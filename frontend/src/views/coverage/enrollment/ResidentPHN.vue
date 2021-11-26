<template>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model.trim="phn" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
</template>

<script>
import AppButton from '../../../components/AppButton.vue'
import AppCol from '../../../components/grid/AppCol.vue'
import AppDateInput from '../../../components/AppDateInput.vue'
import AppInput from '../../../components/AppInput.vue'
import AppRow from '../../../components/grid/AppRow.vue'
import AppOutput from '../../../components/AppOutput.vue'

import Datepicker from 'vue3-date-time-picker';
import 'vue3-date-time-picker/dist/main.css'
import { INPUT_DATE_FORMAT } from '../../../util/constants'

import EnrollmentService from '../../../services/EnrollmentService'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'

import ResidentDetails from './ResidentDetails.vue'

export default {
    name: 'ResidentPHN',
    components: {
    AppButton,
    AppCol,
    AppInput,
    AppRow,
    AppOutput,
    ResidentDetails
},
    setup() {
        return {
        v$: useVuelidate()}
    },
    data() {
      return {
        phn: '9890608412',
        searching: false,
      }
    },
    methods: {
      async submitForm() {
        this.searching = true
        try {
          const isValid = await this.v$.$validate()
          if (!isValid) {
            this.$store.commit('alert/setErrorAlert');
            this.searching = false
            return
          }
          this.$emit('update-resident', this.phn)
          this.$store.commit('alert/setSuccessAlert', 'Search complete')
        } catch (err) {
          console.log(`Error ${err}`)
          this.$store.commit('alert/setErrorAlert', `${err}`)
        } finally {
          this.searching = false
        }
      },
      resetForm() {
        this.phn = '9890608412'
        this.v$.$reset()
        this.$store.commit("alert/dismissAlert");
        this.searching = false
      },
    },
    validations() {
      return {
        phn: {
          required,
          validatePHN: helpers.withMessage(
            VALIDATE_PHN_MESSAGE, validatePHN
          )
        },
      }
    }
}
</script>