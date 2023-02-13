<template>
  <AppCol class="col1"> <AppOutput :value="registration.payeeNumber" /></AppCol>
  <AppCol class="col2">
    <div v-html="regDeRegDate" />
  </AppCol>
  <AppCol class="col2"><AppOutput :value="registration.currentStatus" /></AppCol>
  <AppCol class="col1"><AppOutput :value="registration.administrativeCode" /></AppCol>
  <AppCol class="col3">
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
        data = `${data} Reg Reason: ${this.registration.registrationReasonCode}<br/>`
      }
      if (this.registration.deregistrationReasonCode) {
        data = `${data} DeReg Reason: ${this.registration.deregistrationReasonCode}<br/>`
      }
      if (this.registration.cancelReasonCode) {
        data = `${data} Cancel Reason: ${this.registration.cancelReasonCode}<br/>`
      }
      return data
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
      return `${this.registration.effectiveDate} <br/> ${this.registration.cancelDate} `
    },
  },
}
</script>
