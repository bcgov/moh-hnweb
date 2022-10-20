<template>
  <tr>
    <td>Group Member Number</td>
    <td>{{ result.groupMemberNumber }}</td>
  </tr>
  <tr>
    <td>Department Number</td>
    <td>{{ result.groupMemberDepartmentNumber }}</td>
  </tr>
</template>
<script>
export default {
  props: {
    result: Object,
  },
}
</script>
