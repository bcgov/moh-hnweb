<template>
  <tr>
    <td>Home Address</td>
    <td>{{ result.homeAddressLine1 }}</td>
    <td>{{ setHomeAddressLine2 }}</td>
    <td>{{ parseHomeAddress }}</td>
    <td>{{ result.homeAddressPostalCode }}</td>
    <td>{{ result.telephone }}</td>
  </tr>
  <tr>
    <td>Mailing Address</td>
    <td>{{ result.mailingAddressLine1 }}</td>
    <td>{{ setMailingAddressLine2 }}</td>
    <td>{{ parseMailingAddress }}</td>
    <td>{{ result.mailingAddressPostalCode }}</td>
    <td>{{ result.telephone }}</td>
  </tr>
</template>
<script>
export default {
  props: {
    result: Object,
  },
  computed: {
    setHomeAddressLine2() {
      return !this.result.homeAddressLine3 && this.result.homeAddressLine2 ? '' : this.result.homeAddressLine2
    },
    setMailingAddressLine2() {
      return !this.result.mailingAddressLine3 && this.result.mailingAddressLine3 ? '' : this.result.mailingAddressLine3
    },
    parseHomeAddress() {
      let city_province = ''
      if (this.result.homeAddressLine3) {
        city_province = this.result.homeAddressLine3
      } else if (this.result.homeAddressLine2) {
        city_province = this.result.homeAddressLine2
      }
      return city_province
    },
    parseMailingAddress() {
      let city_province = ''
      if (this.result.mailingAddressLine3) {
        city_province = this.result.mailingAddressLine3
      } else if (this.result.mailingAddressLine2) {
        city_province = this.result.mailingAddressLine2
        this.result.mailingAddressLine2 = ''
      }
      return city_province
    },
  },
}
</script>
