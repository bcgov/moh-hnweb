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
import AppButton from '../../AppButton.vue'
import AppCol from '../../grid/AppCol.vue'
import AppRow from '../../grid/AppRow.vue'
import AppInput from '../../AppInput.vue'
import useVuelidate from '@vuelidate/core'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../../util/validators'
import { required, helpers } from '@vuelidate/validators'

export default {
  name: 'ResidentPHN',
  components: {
    AppButton,
    AppCol,
    AppRow,
    AppInput,
  },
  setup() {
    return {
      v$: useVuelidate(),
    }
  },
  data() {
    return {
      phn: '',
      searching: false,
    }
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
        this.$emit('update-resident', this.phn)
      } catch (err) {
        console.log(`Error ${err}`)
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.searching = false
      }
    },
    resetForm() {
      this.phn = ''
      this.v$.$reset()
      this.$store.commit('alert/dismissAlert')
      this.searching = false
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
