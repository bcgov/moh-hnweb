<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="AddGroupMember" title="Add Group Member" />
    <SubNavTab routeName="UpdateNumberAndDept" title="Update Number and/or Department" />
    <SubNavTab routeName="CancelGroupMember" title="Cancel Group Member" />
    <SubNavTab routeName="CancelGroupMemberDependent" title="Cancel Dependent" />
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'GroupMemberHome',
}
</script>
