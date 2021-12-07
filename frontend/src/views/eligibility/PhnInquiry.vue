<template>
  <div>
    <p>Enter 1 or more PHNs</p>
    <form @submit.prevent="submitForm">
      <AppRow>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn1" id="phn1" label="1" type="text" v-model.trim="phn1" />
        </AppCol>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn6" id="phn6" label="6" type="text" v-model.trim="phn6" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn2" id="phn2" label="2" type="text" v-model.trim="phn2" />
        </AppCol>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn7" id="phn7" label="7" type="text" v-model.trim="phn7" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn3" id="phn3" label="3" type="text" v-model.trim="phn3" />
        </AppCol>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn8" id="phn8" label="8" type="text" v-model.trim="phn8" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn4" id="phn4" label="4" type="text" v-model.trim="phn4" />
        </AppCol>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn9" id="phn9" label="9" type="text" v-model.trim="phn9" />
        </AppCol>
      </AppRow>
      <AppRow>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn5" id="phn5" label="5" type="text" v-model.trim="phn5" />
        </AppCol>
        <AppCol class="col2">
          <AppInput :e-model="v$.phn10" id="phn10" label="10" type="text" v-model.trim="phn10" />
        </AppCol>
      </AppRow>                        
      <AppRow>
        <AppButton :disabled="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
      </form>
  </div>
  <br />
  <div v-if="searchOk">
    <h2>Transaction Successful</h2>
    <AppSimpleTable>
      <thead>
      <tr>
        <th>PHN</th>
        <th>Name</th>
        <th>Date of Birth</th>
        <th>Gender</th>
        <th>Eligible</th>
        <th>Student</th>
      </tr>
      </thead>
      <tbody>
        <tr v-for="match in result.matches">
          <PhnInquiryMatch :match="match" />
        </tr>
      </tbody>
    </AppSimpleTable>

  </div>
</template>

<script>
import AppButton from '../../components/AppButton.vue'
import AppCol from '../../components/grid/AppCol.vue'
import AppInput from '../../components/AppInput.vue'
import AppRow from '../../components/grid/AppRow.vue'
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import EligibilityService from '../../services/EligibilityService'
import PhnInquiryMatch from '../../components/eligibility/PhnInquiryMatch.vue'
import useVuelidate from '@vuelidate/core'
import { validateOptionalPHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { helpers } from '@vuelidate/validators'

export default {
  name: 'PhnInquiry',
  components: {
    AppButton, AppCol, AppInput, AppRow, AppSimpleTable, PhnInquiryMatch
  },
  setup() {
    return {
      v$: useVuelidate()}
  },
  data() {
    return {
      phn1: '',
      phn2: '',
      phn3: '',
      phn4: '',
      phn5: '',
      phn6: '',
      phn7: '',
      phn8: '',
      phn9: '',
      phn10: '',
      searching: false,
      searchOk: false,
      result: {
        errorMessage: '',
        matches: [],
      },
    }
  },
  methods: {
    async submitForm() {
      this.searching = true
      try {
        const isValid = await this.v$.$validate()      
        if (!isValid) {
          this.showError()
          return
        }
        const allPhns = [this.phn1, this.phn2, this.phn3, this.phn4, this.phn5, this.phn6, this.phn7, this.phn8, this.phn9, this.phn10]
        const populatedPHNs = allPhns.filter(phn => phn != '')
        if (populatedPHNs.length === 0) {
          this.showError('At least one PHN is required')
          return
        }
        console.log(populatedPHNs)
        this.result = (await EligibilityService.inquirePhn({phns: populatedPHNs})).data

        if (!this.result.errorMessage || this.result.errorMessage === '') {
          this.searchOk = true
          this.$store.commit('alert/setSuccessAlert', 'Search complete')
        } else {
          this.$store.commit('alert/setWarningAlert', this.result.errorMessage)
          this.result = null
          this.searchOk = false
        }
      } catch (err) {
        this.$store.commit('alert/setErrorAlert', `${err}`)
      } finally {
        this.searching = false
      }
    },
    showError(error) {
      this.$store.commit('alert/setErrorAlert', error)
      this.result = {}
      this.searching = false
    },
    resetForm() {
      this.phn1 = ''
      this.phn2 = ''
      this.phn3 = ''
      this.phn4 = ''
      this.phn5 = ''
      this.phn6 = ''
      this.phn7 = ''
      this.phn8 = ''
      this.phn9 = ''
      this.phn10 = ''
      this.result = null
      this.v$.$reset()
      this.$store.commit("alert/dismissAlert")
      this.searchOk = false
      this.searching = false
    }
  },
  validations() {
    return {
      phn1: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn2: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn3: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn4: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn5: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn6: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn7: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn8: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn9: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
      phn10: {
        validatePHN: helpers.withMessage(
          VALIDATE_PHN_MESSAGE, validateOptionalPHN
        )
      },
    }
  }
}
</script>