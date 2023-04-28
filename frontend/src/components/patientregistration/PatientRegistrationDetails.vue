<template>
  <AppCol class="col1"> <AppOutput :value="registration.payeeNumber" /></AppCol>
  <AppCol class="col2">
    <div v-html="regDeRegDate" />
  </AppCol>
  <AppCol class="col2"><AppOutput :value="registration.currentStatus" /></AppCol>
  <AppCol class="col1"><AppOutput :value="registration.administrativeCode" /></AppCol>
  <AppCol class="col6">
    <div v-html="registrationData" />
  </AppCol>
</template>
<script>
export default {
  props: {
    registration: {
      required: true,
      type: Object,
    },
  },
  computed: {
    registrationData() {
      let data = ''
      if (this.registration.registeredPractitionerNumber) {
        data = `Practitioner No: ${this.registration.registeredPractitionerNumber}<br/>`
      }
      if (this.registration.registeredPractitionerNumber) {
        data = `${data}  Practitioner Name: ${this.practitionerName}<br/>`
      }
      if (this.registration.registrationReasonCode) {
        data = `${data} Reg Reason: ${this.registrationReason}<br/>`
      }
      if (this.registration.deregistrationReasonCode) {
        data = `${data} DeReg Reason: ${this.deregistrationReason}<br/>`
      }
      if (this.registration.cancelReasonCode) {
        data = `${data} Cancel Reason: ${this.cancelReason}<br/>`
      }
      return data
    },
    cancelReason() {
      let cancelReason = this.registration.cancelReasonCode
      if (cancelReason !== 'N/A') {
        if (this.registration.cancelReasonDesc) {
          cancelReason = cancelReason + ' - ' + this.registration.cancelReasonDesc
        }
      }
      return cancelReason
    },
    deregistrationReason() {
      let deregistrationReason = this.registration.deregistrationReasonCode
      if (deregistrationReason !== 'N/A') {
        if (this.registration.deregistrationReasonDesc) {
          deregistrationReason = deregistrationReason + ' - ' + this.registration.deregistrationReasonDesc
        }
      }
      return deregistrationReason
    },
    registrationReason() {
      let registrationReason = this.registration.registrationReasonCode
      if (registrationReason !== 'N/A') {
        if (this.registration.registrationReasonDesc) {
          registrationReason = registrationReason + ' - ' + this.registration.registrationReasonDesc
        }
      }
      return registrationReason
    },
    practitionerName() {
      let name = ''
      if (this.registration.registeredPractitionerSurname) {
        name = name + this.registration.registeredPractitionerSurname
      }
      if (this.registration.registeredPractitionerFirstName) {
        name = name + ', ' + this.registration.registeredPractitionerFirstName
      }
      if (this.registration.registeredPractitionerMiddleName) {
        name = name + ' ' + this.registration.registeredPractitionerMiddleName
      }
      return name
    },
    regDeRegDate() {
      return `${this.registration.effectiveDate} <br/> ${this.registration.cancelDate ? this.registration.cancelDate : ''} `
    },
  },
}
</script>
