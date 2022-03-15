<template>
  <td>{{ beneficiary.phn }}</td>
  <td>{{ beneficiary.familyName }}</td>
  <td>{{ beneficiary.firstName }}</td>
  <td>{{ beneficiary.secondName }}</td>
  <td>{{ beneficiary.thirdName }}</td>
</template>
<script>
export default {
  props: {
    beneficiary: Object,
  },
}
</script>
