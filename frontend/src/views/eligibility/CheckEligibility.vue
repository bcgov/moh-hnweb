<script setup>
  import AppButton from '../../components/AppButton.vue'
  import AppCol from '../../components/grid/AppCol.vue'
  import AppInput from '../../components/AppInput.vue'
  import AppLabel from '../../components/AppLabel.vue'
  import AppRow from '../../components/grid/AppRow.vue'
  import EligibiityService from '../../services/EligibilityService'
  import useVuelidate from '@vuelidate/core'
  const v$ = useVuelidate()
</script>
<template>
  <div>
    <form @submit.prevent="submitForm">
       <AppRow>
            <AppCol class="col2">
              <AppLabel for="phn" title="PHN"/>
            </AppCol>
            <AppCol class="col3">
              <AppInput :errorValue="v$.phn" id="phn" type="text" v-model="phn"/>
            </AppCol>
        </AppRow>
        <AppRow>
            <AppCol class="col2">
              <AppLabel for="eligibilityDate" title="Date to check"/>
            </AppCol>
            <AppCol class="col3">
              <AppInput :errorValue="v$.eligibilityDate" id="eligibilityDate" type="date" v-model="eligibilityDate"/>
            </AppCol>         
       </AppRow>
       <AppButton :disabled="searching" title="Submit" type="submit"/>
       <AppButton @click="resetForm" title="Clear" type="button"/>
    </form>
  </div>
  <br/>
  <div v-if="searched">
    <h2>Transaction Succesful</h2>
    <AppRow>
      <AppCol class="col3">PHN:</AppCol>
      <AppCol>{{result.phn}}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Beneficiary on Date checked?</AppCol>
      <AppCol>{{result.beneficiaryOnDateChecked ? 'Y' : 'N'}}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Coverage End Date:</AppCol>
      <AppCol>{{result.coverageEndDate}}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Reason:</AppCol>
      <AppCol>{{result.reason}}</AppCol>
    </AppRow>
    <AppRow class="row">
      <AppCol class="col3">Exclusion Period Date:</AppCol>
      <AppCol>{{result.exclusionPeriodEndDate}}</AppCol>
    </AppRow>
    <br/>
    <p>Next Business Service:</p>
    <button @click="$router.push('PhnEnquiry')">PHN Enquiry</button>
  </div>
</template>

<script>
  import { required, numeric } from '@vuelidate/validators'
  
    export default {
      name: 'CheckEligibility', 

      data() {
        return {
          phn: '',
          eligibilityDate: '',
          searching: false,
          searched: false,
          result: {
            phn: '',
            beneficaryOnDateChecked: false,
            coverageEndDate: '',
            reason: '',
            exclusionPeriodEndDate: ''
          }
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
            this.result = (await EligibiityService.checkEligibility(this.phn, this.eligibilityDate)).data
            this.searched = true
            this.$store.commit('alert/setInfoAlert', 'Search complete')
          } catch (err) {
            this.$store.commit('alert/setErrorAlert', `${err}`)
          } finally {
            this.searching = false;
          }
        },
        resetForm() {
          this.phn = ''
          this.eligibilityDate = ''
          this.v$.$reset()
          this.$store.commit("alert/dismissAlert");
          this.searched = false
          this.searching = false
        }
      },
      validations () {
          return {
            phn: { required, numeric },
            eligibilityDate: { required }
          }
      }
    }
</script>