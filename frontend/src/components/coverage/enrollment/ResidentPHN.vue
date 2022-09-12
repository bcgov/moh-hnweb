<template>
  <AppHelp>
    <p>The purpose of this screen is to retrieve a person's name, address, gender, date of birth, and verify this is the correct person you want to enroll in MSP and add to your group. .</p>
    <p>Results displayed include the client's Personal Health Number, name, birthdate, gender, and date of death (if applicable). </p>
  </AppHelp>
  <div>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col3">
          <AppInput :e-model="v$.phn" id="phn" label="PHN" type="text" v-model.trim="phn" />
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
import AppHelp from '../../../components/ui/AppHelp.vue'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'
import { useAlertStore } from '../../../stores/alert'

export default {
  name: 'ResidentPHN',
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
      phn: '',
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
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.alertStore.setErrorAlert()
          return
        }
        this.$emit('update-resident', this.phn)
      } catch (err) {
        this.alertStore.setErrorAlert(err)
      }
    },
    resetForm() {
      this.phn = ''
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
    }
  },
}
</script>
