<template>
  <td>{{ record.type }}</td>
  <td>{{ record.organization }}</td>
  <td>{{ record.userId }}</td>
  <td>{{ record.transactionStartTime }}</td>
  <td>{{ record.affectedPartyId }}</td>
  <td>{{ record.affectedPartyType }}</td>
  <td>{{ record.transactionId }}</td>
</template>
<script>
export default {
  props: {
    record: {
      type: Object,
      required: true,
    },
  },
}
</script>
