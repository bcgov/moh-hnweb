<template>
  <div>
    <AppRow>
      <AppCol class="col1">
        <span>{{ candidate.score }}</span>
      </AppCol>
      <AppCol class="col6">
        <span>{{ formatDetailsLine1 }}</span>
        <span>{{ formatDetailsLine2 }}</span>
        <span>{{ formatDetailsLine3 }}</span>
      </AppCol>
      <AppCol class="col1">
        <AppButton :disabled="submitting" mode="primary" @click="selectCandidate" type="button">Add</AppButton>
      </AppCol>
    </AppRow>
  </div>
</template>
<script>
export default {
  name: 'NameSearchCandidate',
  props: {
    candidate: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      submitting: false,
    }
  },
  computed: {
    formatDetailsLine1() {
      let details = ''
      details = this.fullName + ' (' + this.resolveNameStatus + ')'
      return details
    },
    resolveNameStatus() {
      switch (this.candidate.nameTypeCode) {
        case 'L':
          return 'CURRENT RECORD'
        case 'C':
          return 'PREVIOUS RECORD'
        default:
          return ''
      }
    },
    formatDetailsLine2() {
      let details = ''
      if (this.candidate.gender) {
        details = details + this.candidate.gender
      }
      if (this.candidate.dateOfBirth) {
        details = details + ' ' + this.candidate.dateOfBirth
      }
      if (this.candidate.phn) {
        details = details + ' ' + this.candidate.phn
      }
      if (this.candidate.assigningAuthority) {
        details = details + ' ' + this.candidate.assigningAuthority
      }
      if (this.candidate.identifierTypeCode) {
        details = details + ' ' + this.candidate.identifierTypeCode
      }
      return details
    },
    formatDetailsLine3() {
      let address = ''
      if (this.candidate.address1) {
        address = address + this.candidate.address1
      }
      if (this.candidate.address2) {
        address = address + ' ' + this.candidate.address2
      }
      if (this.candidate.address3) {
        address = address + ' ' + this.candidate.address3
      }
      if (this.candidate.city) {
        address = address + ' ' + this.candidate.city
      }
      if (this.candidate.province) {
        address = address + ' ' + this.candidate.province
      }
      if (this.candidate.postalCode) {
        address = address + ' ' + this.candidate.postalCode
      }
      return address
    },
    fullName() {
      let name = ''
      if (this.candidate.surname) {
        name = name + this.candidate.surname
      }
      if (this.candidate.givenName) {
        name = name + ', ' + this.candidate.givenName
      }
      if (this.candidate.secondName) {
        name = name + ' ' + this.candidate.secondName
      }
      return name
    },
  },
  methods: {
    selectCandidate() {
      this.submitting = true
      this.$store.commit('studyPermitHolder/setResident', this.candidate)
      this.$router.push({ name: 'AddVisaResidentWithPHN', query: { pageAction: 'REGISTRATION' } })
    },
  },
}
</script>
