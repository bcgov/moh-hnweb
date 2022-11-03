<template>
  <tr>
    <td>{{ address.title }}</td>
    <td>{{ address.addressLine1 }}</td>
    <td>{{ address.addressLine2 }}</td>
    <td>{{ address.cityProvince }}</td>
    <td>{{ address.postalCode }}</td>
    <td>{{ address.telephone }}</td>
  </tr>
</template>
<script>
export default {
  props: {
    address: Object,
  },
}
</script>
