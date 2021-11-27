<template>
  <td>{{ beneficiary.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ beneficiary.dateOfBirth }}</td>
  <td>{{ beneficiary.gender }}</td>
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
      if (this.beneficiary.secondName) {
        name = name + ' ' + this.beneficiary.secondName
      }
      if (this.beneficiary.thirdName) {
        name = name + ' ' + this.beneficiary.thirdName
      }
      return name
    },
  }
}
</script>