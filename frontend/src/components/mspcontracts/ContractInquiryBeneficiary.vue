<template>
  <td>{{ beneficiary.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ beneficiary.birthDate }}</td>
  <td>{{ beneficiary.gender }}</td>
  <td>{{ relationship }}</td>
  <td>{{ beneficiary.studentStatus }}</td>
  <td>{{ beneficiary.effectiveDate }}</td>
  <td>{{ beneficiary.cancelDate }}</td>
  <td>{{ beneficiary.cancelReason }}</td>
</template>
<script>
import { decodeRelationship } from '../../util/utils'

export default {
  props: {
    beneficiary: Object,
  },
  computed: {
    fullName() {
      let name = ''
      if (this.beneficiary.familyName) {
        name = name + this.beneficiary.familyName
      }
      if (this.beneficiary.firstName) {
        name = name + ', ' + this.beneficiary.firstName
      }
      if (this.beneficiary.secondName) {
        name = name + ' ' + this.beneficiary.secondName
      }
      if (this.beneficiary.thirdName) {
        name = name + ' ' + this.beneficiary.thirdName
      }
      return name
    },
    relationship() {
      return decodeRelationship(this.beneficiary.relationshipCode)
    },
  },
}
</script>
