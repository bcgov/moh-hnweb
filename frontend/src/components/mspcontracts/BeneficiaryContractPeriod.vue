<template>
  <td>{{ contractPeriod.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ contractPeriod.contractHolder }}</td>
  <td>{{ relationship }}</td>
  <td>{{ contractPeriod.groupNumber }}</td>
  <td>{{ contractPeriod.effectiveDate }}</td>
  <td>{{ contractPeriod.cancelDate }}</td>
  <td>{{ cancelReason }}</td>
</template>
<script>
import { decodeRelationship } from '../../util/utils'

export default {
  props: {
    contractPeriod: Object,
  },
  computed: {
    fullName() {
      let name = ''
      if (this.contractPeriod.lastName) {
        name = name + this.contractPeriod.lastName
      }
      if (this.contractPeriod.firstName) {
        name = name + ', ' + this.contractPeriod.firstName
      }
      return name
    },
    relationship() {
      return decodeRelationship(this.contractPeriod.relationship)
    },
    cancelReason() {
      switch (this.contractPeriod.cancelReason) {
        case 'E':
          return 'Eligible'
        case 'I':
          return 'Ineligible'
        case 'D':
          return 'Deceased'
        default:
          return ''
      }
    },
  },
}
</script>
