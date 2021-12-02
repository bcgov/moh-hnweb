<template>
  <td>{{ match.phn }}</td>
  <td>{{ fullName }}</td>
  <td>{{ match.dateOfBirth }}</td>
  <td>{{ match.gender }}</td>
  <td>{{ match.eligible }}</td>
  <td>{{ match.student }}</td>
</template>
<script>
export default {
  props: {
    match: Object,
  },
  computed: {
    fullName() {
      let name = ''
      if (this.match.lastName) {
        name = name + this.match.lastName
      }
      if (this.match.firstName) {
        name = name + ', ' + this.match.firstName
      }
      // if (this.result.secondName) {
      //   name = name + ' ' + this.result.secondName
      // }
      return name
    },
  }
}
</script>