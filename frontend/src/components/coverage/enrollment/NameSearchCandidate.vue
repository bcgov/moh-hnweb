<template>
  <div>
    <AppRow>
      <AppCol class="col1">
        <span>{{ candidate.score }}</span>
      </AppCol>
      <AppCol class="col6">
        <span>{{ formatDetailsLine1 }}</span>
        <span v-html="formatDetailsLine2"></span>
        <span>{{ formatDetailsLine3 }}</span>
      </AppCol>
      <AppCol class="col1">
        <AppButton class="btn-sm" :submitting="submitting" mode="primary" @click="selectCandidate" type="button">Add</AppButton>
      </AppCol>
    </AppRow>
  </div>
</template>
<script>
import { useAlertStore } from '../../../stores/alert'
import { useStudyPermitHolderStore } from '../../../stores/studyPermitHolder'
import { DATE_OF_DEATH_MESSAGE } from '../../../util/constants.js'

export default {
  name: 'NameSearchCandidate',
  setup() {
    return {
      alertStore: useAlertStore(),
      studyPermitHolderStore: useStudyPermitHolderStore(),
    }
  },
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
      return this.fullName + ' (' + this.resolveNameStatus + ')'
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
      if (this.candidate.dateOfDeath && this.candidate.dateOfDeath != 'N/A') {
        details = `${details} <span class = "text-deceased"> ${this.candidate.dateOfDeath}</span>`
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
      } else if (this.candidate.mailingAddress1) {
        address = address + this.candidate.mailingAddress1

        if (this.candidate.mailingAddress2) {
          address = address + ' ' + this.candidate.mailingAddress2
        }
        if (this.candidate.mailingAddress2) {
          address = address + ' ' + this.candidate.mailingAddress3
        }
        if (this.candidate.mailingAddressCity) {
          address = address + ' ' + this.candidate.mailingAddressCity
        }
        if (this.candidate.mailingAddressProvince) {
          address = address + ' ' + this.candidate.mailingAddressProvince
        }
        if (this.candidate.mailingAddressPostalCode) {
          address = address + ' ' + this.candidate.mailingAddressPostalCode
        }
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
      this.studyPermitHolderStore.resident = this.candidate
      this.alertStore.dismissAlert()
      if (this.candidate.dateOfDeath && this.candidate.dateOfDeath != 'N/A') {
        this.alertStore.setErrorAlert(DATE_OF_DEATH_MESSAGE)
      }
      this.$router.push({ name: 'AddVisaResidentWithPHN', query: { pageAction: 'REGISTRATION' } })
      this.submitting = false
    },
  },
}
</script>
<style>
.text-deceased {
  color: red;
  font-weight: bold;
}
</style>
