<script setup>
  import EligibiityService from '../../services/EligibilityService'
</script>
<template>
  <div>
    <form @submit.prevent="onSubmit">
       <div class="row">
            <div class="col col3">PHN*</div>
            <div class="col col3"><input style="width: 150px" type="text" v-model="phn"/></div>
        </div>
        <div class="row">
            <div class="col col3">Date to check</div>
            <div class="col col3"><input style="width: 150px" type="date" v-model="eligibiityDate"/></div>
       </div>
       <button type="subnmit">Submit</button>
    </form>
  </div>
  <br/>
  <div v-if="searched">
    <h2>Transaction Succesful</h2>
    <div class="row">
      <div class="col col3">PHN:</div>
      <div class="col col3">{{result.phn}}</div>
    </div>
    <div class="row">
      <div class="col col3">Beneficiary on Date checked?</div>
      <div class="col col3">{{result.beneficiaryOnDateChecked ? 'Y' : 'N'}}</div>
    </div>
    <div class="row">
      <div class="col col3">Coverage End Date:</div>
      <div class="col col3">{{result.coverageEndDate}}</div>
    </div>
    <div class="row">
      <div class="col col3">Reason:</div>
      <div class="col col3">{{result.reason}}</div>
    </div>
    <div class="row">
      <div class="col col3">Exclusion Period Date:</div>
      <div class="col col3">{{result.exclusionPeriodEndDate}}</div>
    </div>
      <br/>
    <p>Next Business Service:</p>
    <button @click="$router.push('PhnEnquiry')">PHN Enquiry</button>
  </div>
</template>

<script>
    export default {
      name: 'Employees', 
      data() {
        return {
          phn: '',
          eligibiityDate: '',
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
        onSubmit() {
          if (!this.validate()) {
            return
          }
          console.log('Submitting data')
          this.searched = true
          EligibiityService.checkEligibility(this.phn, this.eligibiityDate)
            .then(response => {
              this.result = response.data
            })
            .catch (error => {
              // TODO (weskubo-cgi) Implement error handling
            })
          
        },
        validate() {
          if (!this.phn) {
            alert('PHN is required')
            return false
          }
          if (!this.eligibiityDate) {
            alert('Date is required')
            return false
          }
          return true
        }
      }
    }
</script>