<template>
  <td>{{ beneficiaryContractPeriod.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ beneficiaryContractPeriod.contractHolder }}</td>
  <td>{{ relationship }}</td>
  <td>{{ beneficiaryContractPeriod.groupNumber }}</td>
  <td>{{ beneficiaryContractPeriod.effectiveDate }}</td>
  <td>{{ beneficiaryContractPeriod.cancelDate }}</td>
  <td>{{ beneficiaryContractPeriod.cancelReason === 'E' ? 'Eligible' : '' }}</td>
</template>
<script>
import { decodeRelationship } from '../../util/utils'

export default {
  props: {
    beneficiaryContractPeriod: Object,
  },
  computed: {
    fullName() {
      let name = ''
      if (this.beneficiaryContractPeriod.lastName) {
        name = name + this.beneficiaryContractPeriod.lastName
      }
      if (this.beneficiaryContractPeriod.firstName) {
        name = name + ', ' + this.beneficiaryContractPeriod.firstName
      }
      return name
    },
    relationship() {
      return decodeRelationship(this.beneficiaryContractPeriod.relationship)
    },
  },
}
</script>
