<template>
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
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'

export default {
  name: 'ResidentPHN',
  setup() {
    return {
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
          this.$store.commit('alert/setErrorAlert')
          return
        }
        this.$emit('update-resident', this.phn)
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      }
    },
    resetForm() {
      this.phn = ''
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
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
