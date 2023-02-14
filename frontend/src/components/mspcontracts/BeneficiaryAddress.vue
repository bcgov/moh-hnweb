<template>
  <tr>
    <td>{{ address.title }}</td>
    <td>{{ address.addressLine1 }}</td>
    <td>{{ addressLine2 }}</td>
    <td>{{ addressLine3 }}</td>
    <td>{{ cityProvince }}</td>
    <td>{{ address.postalCode }}</td>
    <td>{{ address.telephone }}</td>
  </tr>
</template>
<script>
export default {
  props: {
    address: Object,
  },
  computed: {
    addressLine2() {
      return this.address.addressLine3 && this.address.addressLine2 ? this.address.addressLine2 : ''
    },
    addressLine3() {
      return this.address.addressLine4 && this.address.addressLine3 ? this.address.addressLine3 : ''
    },
    cityProvince() {
      let cityProvince = ''
      if (this.address.addressLine4) {
        cityProvince = this.address.addressLine4
      } else if (this.address.addressLine3) {
        cityProvince = this.address.addressLine3
      } else if (this.address.addressLine2) {
        cityProvince = this.address.addressLine2
      }
      return cityProvince
    },
  },
}
</script>
