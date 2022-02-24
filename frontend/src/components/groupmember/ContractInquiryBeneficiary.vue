<template>
  <td>{{ contractInquiryBeneficiary.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ contractInquiryBeneficiary.dateOfBirth }}</td>
  <td>{{ contractInquiryBeneficiary.gender }}</td>
  <td>{{ contractInquiryBeneficiary.relationship }}</td>
  <td>{{ contractInquiryBeneficiary.student }}</td>
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
      if (this.contractInquiryBeneficiary.thirdName) {
        name = name + this.contractInquiryBeneficiary.thirdName
      }
      if (this.contractInquiryBeneficiary.secondName) {
        name = name + this.contractInquiryBeneficiary.secondName
      }
      if (this.contractInquiryBeneficiary.firstName) {
        name = name + ', ' + this.contractInquiryBeneficiary.firstName
      }
      return name
    },
  },
}
</script>
