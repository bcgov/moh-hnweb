<template>
  <h2>Group Member Details</h2>
  <AppSimpleTable id="identifierTable">
    <thead>
      <tr>
        <th>Identifier</th>
        <th>Value</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>Group Member Number</td>
        <td>{{ result.groupMemberNumber }}</td>
      </tr>
      <tr>
        <td>Department Number</td>
        <td>{{ result.groupMemberDepartmentNumber }}</td>
      </tr>
    </tbody>
  </AppSimpleTable>
</template>

<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
export default {
  components: {
    AppSimpleTable,
  },
  props: {
    result: Object,
  },
}
</script>
