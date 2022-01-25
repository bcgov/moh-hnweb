<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="AddGroupMembersDependent" title="Add Group Member's Dependent" />
    <SubNavTab routeName="UpdateNumberAndDept" title="Update Group Member's Number and/or Department" />
    <SubNavTab routeName="CancelGroupMember" title="Cancel Group Member" />
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'GroupMemberHome',
}
</script>
