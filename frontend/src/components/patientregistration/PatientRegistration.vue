<template>
  <AppCol class="col2"> <AppOutput :value="registration.payeeNumber" /></AppCol>
  <AppCol class="col2">
    <span style="white-space: pre-line"><AppOutput :value="regDeRegDate" /></span>
  </AppCol>
  <AppCol class="col2"><AppOutput :value="registration.currentStatus" /></AppCol>
  <AppCol class="col3"><AppOutput :value="registration.administrativeCode" /></AppCol>
  <AppCol class="col2">
    <span style="white-space: pre-line"><AppOutput :value="data" /></span>
  </AppCol>
</template>
<script>
import { required } from '@vuelidate/validators'

export default {
  props: {
    registration: Array,
    type: required,
  },
  computed: {
    data() {
      let data = ''
      if (this.registration.registeredPractitionerNumber) {
        data = data + ' Practioner No: ' + this.registration.registeredPractitionerNumber + '\n'
      }
      if (this.registration.registrationReasonCode) {
        data = data + ' Reg Reason: ' + this.registration.registrationReasonCode + '\n'
      }
      if (this.registration.deregistrationReasonCode) {
        data = data + ' DeReg Reason: ' + this.registration.deregistrationReasonCode + '\n'
      }
      if (this.registration.cancelReasonCode) {
        data = data + ' Cancel Reason: ' + this.registration.cancelReasonCode + '\n'
      }
      return data
    },
    regDeRegDate() {
      return this.registration.effectiveDate + '\n' + this.registration.cancelDate
    },
  },
}
</script>
