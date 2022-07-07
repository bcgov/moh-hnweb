<template>
  <td>{{ auditReport.type }}</td>
  <td>{{ auditReport.organization }}</td>
  <td>{{ auditReport.userId }}</td>
  <td>{{ auditReport.transactionStartTime }}</td>
  <td>{{ auditReport.affectedPartyId }}</td>
  <td>{{ auditReport.affectedPartyType }}</td>
  <td>{{ auditReport.transactionId }}</td>
</template>
<script>
export default {
  props: {
    auditReport: Object,
  },
}
</script>
