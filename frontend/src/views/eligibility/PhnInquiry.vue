<template>
  <AppHelp
    ><p>
      Use PHN Inquiry to verify an individual's name, birthdate, gender, and to check their eligibility for Medical Services Plan (MSP) coverage. Access to PHN Inquiry is available to authorized users within both EMPLOYER organizations and HEALTH AUTHORITIES. Please look for notes of special
      interest to either EMPLOYERS or HEALTH AUTHORITIES.
    </p>
    <p>HEALTH AUTHORITIES and HEALTH CARE PROVIDERS: If "N" response is received, use MSP Coverage Status Check to check eligibility.</p>
  </AppHelp>
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
        <AppButton :submitting="searching" mode="primary" type="submit">Submit</AppButton>
        <AppButton @click="resetForm" mode="secondary" type="button">Clear</AppButton>
      </AppRow>
    </form>
  </div>
  <br />
  <div v-if="searchOk && result.beneficiaries.length > 0">
    <hr />
    <AppSimpleTable id="resultsTable">
      <thead>
        <tr>
          <th>
            <div>PHN</div>
            <AppTooltip mode="header">
              <p>
                Each PHN is processed individually.<br />
                Unsuccesfully processed PHNs appear with an error message, at the top of the response screen.<br />
                Successfully processed PHNs are displayed in the order they were entered. Each has two lines, given name, birthdate, gender, ELIGIBILITY status, and STUDENT status if applicable.
              </p>
            </AppTooltip>
          </th>
          <th>Name</th>
          <th>Date of Birth</th>
          <th>Gender</th>
          <th>
            <div>Eligible</div>
            <AppTooltip mode="header">
              <p>"Eligible = Y" means that when checked today, the person is eligible for MSP.</p>
              <ul>
                <li>EMPLOYERS - if Y and this is a new employee or dependent, you can add them to your group account using the Add Group Member or Add Dependent.</li>
                <li>HEALTH AUTHORITIES - although eligible today, a person could subsequently become ineligible for service on today's date. If a fee-for-service claim is involved, you may wish to use the MSP Teleplan system or Claims IVR to verify eligibility.</li>
              </ul>
              <br />
              <p>
                "Eligible = N" means the person was enrolled in MSP in the past, but is NOT eligible for publicly funded health care as of today's date. This person may have moved away from BC, opted out of MSP, joined the Armed Forces or RCMP, or may hold expired temporary immigration documents.
                Any ‘N’ you receive must also be confirmed using the MSP Coverage Status Check business service.
              </p>
              <ul>
                <li>EMPLOYERS - if this person is a new employee or dependent who has moved back to BC, they will have to re-enroll. An MSP application or change form, with copies of foundation documents, must be submitted. Otherwise, contact the Help Desk.</li>
                <li>HEALTH AUTHORITIES - if you need to know WHY a client is not eligible, use the Check Eligibility business service.</li>
              </ul>
            </AppTooltip>
          </th>

          <th>
            <div>Student</div>
            <AppTooltip mode="header">
              <p>Student status, whether "Yes" or "No" does not affect eligibility for publicly funded health care.</p>
              <ul>
                <li>EMPLOYERS - you need to know student status if you are planning to add a dependent aged 19 to 24 to an employee's coverage.</li>
              </ul>
            </AppTooltip>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="beneficiary in result.beneficiaries">
          <PhnInquiryBeneficiary :beneficiary="beneficiary" />
        </tr>
      </tbody>
    </AppSimpleTable>
  </div>
</template>

<script>
import AppHelp from '../../components/ui/AppHelp.vue'
import AppTooltip from '../../components/ui/AppTooltip.vue'
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
import EligibilityService from '../../services/EligibilityService'
import PhnInquiryBeneficiary from '../../components/eligibility/PhnInquiryBeneficiary.vue'
import useVuelidate from '@vuelidate/core'
import { validateOptionalPHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'
import { helpers } from '@vuelidate/validators'
import { useAlertStore } from '../../stores/alert.js'
import { handleServiceError } from '../../util/utils'

export default {
  name: 'PhnInquiry',
  components: {
    AppHelp,
    AppSimpleTable,
    AppTooltip,
    PhnInquiryBeneficiary,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
      v$: useVuelidate(),
    }
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
        status: '',
        message: '',
        beneficiaries: [],
      },
    }
  },
  methods: {
    async submitForm() {
      this.result = null
      this.searching = true
      this.searchOk = false
      this.alertStore.dismissAlert()
      try {
        const isValid = await this.v$.$validate()
        if (!isValid) {
          this.showError()
          return
        }
        const allPhns = [this.phn1, this.phn2, this.phn3, this.phn4, this.phn5, this.phn6, this.phn7, this.phn8, this.phn9, this.phn10]
        const populatedPHNs = allPhns.filter((phn) => phn != '')
        if (populatedPHNs.length === 0) {
          this.showError('At least one PHN is required')
          return
        }

        this.result = (await EligibilityService.inquirePhn({ phns: populatedPHNs })).data
        if (this.result.status === 'error') {
          this.alertStore.setErrorAlert(this.result.message)
          return
        }

        this.searchOk = true
        this.alertStore.setAlert({ message: this.result.message, type: this.result.status })
      } catch (err) {
        handleServiceError(err, this.alertStore, this.$router)
      } finally {
        this.searching = false
      }
    },
    showError(error) {
      this.alertStore.setErrorAlert(error)
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
      this.alertStore.dismissAlert()
      this.searchOk = false
      this.searching = false
    },
  },
  validations() {
    return {
      phn1: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn2: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn3: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn4: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn5: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn6: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn7: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn8: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn9: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
      phn10: {
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validateOptionalPHN),
      },
    }
  },
}
</script>
