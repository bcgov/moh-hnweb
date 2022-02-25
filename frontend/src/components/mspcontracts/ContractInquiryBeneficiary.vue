<template>
  <td>{{ contractInquiryBeneficiary.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ contractInquiryBeneficiary.birthDate }}</td>
  <td>{{ contractInquiryBeneficiary.gender }}</td>
  <td>{{ relationship }}</td>
  <td>{{ contractInquiryBeneficiary.studentStatus }}</td>
  <td>{{ contractInquiryBeneficiary.effectiveDate }}</td>
  <td>{{ contractInquiryBeneficiary.cancelDate }}</td>
  <td>{{ contractInquiryBeneficiary.cancelReason }}</td>
</template>
<script>
export default {
  props: {
    contractInquiryBeneficiary: Object,
  },
  computed: {
    fullName() {
      let name = ''
      if (this.contractInquiryBeneficiary.familyName) {
        name = name + this.contractInquiryBeneficiary.familyName
      }
      if (this.contractInquiryBeneficiary.firstName) {
        name = name + ', ' + this.contractInquiryBeneficiary.firstName
      }
      if (this.contractInquiryBeneficiary.secondName) {
        name = name + ' ' + this.contractInquiryBeneficiary.secondName
      }
      if (this.contractInquiryBeneficiary.thirdName) {
        name = name + ' ' + this.contractInquiryBeneficiary.thirdName
      }
      return name
    },
    relationship() {
      switch (this.contractInquiryBeneficiary.relationshipCode) {
        case 'C':
          return 'Employee'
        case 'D':
          return 'Dependent'
        case 'S':
          return 'Spouse'
        default:
          return this.contractInquiryBeneficiary.relationshipCode
      }
    },
  },
}
</script>
