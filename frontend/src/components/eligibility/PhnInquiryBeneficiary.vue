<template>
  <td>{{ beneficiary.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ beneficiary.dateOfBirth }}</td>
  <td>{{ beneficiary.gender }}</td>
  <td>{{ beneficiary.eligible }}</td>
  <td>{{ beneficiary.student }}</td>
</template>
<script>
export default {
  props: {
    beneficiary: Object,
  },
  computed: {
    fullName() {
      let name = ''
      if (this.beneficiary.lastName) {
        name = name + this.beneficiary.lastName
      }
      if (this.beneficiary.firstName) {
        name = name + ', ' + this.beneficiary.firstName
      }
      return name
    },
  }
}
</script>