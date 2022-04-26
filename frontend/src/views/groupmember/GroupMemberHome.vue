<template>
  <TheSubNav>
    <SubNavTab routeName="AddGroupMember" title="Add Group Member" v-if="hasPermission('AddGroupMember')" />
    <SubNavTab routeName="AddDependent" title="Add Dependent" v-if="hasPermission('AddDependent')" />
    <SubNavTab routeName="UpdateNumberAndDept" title="Update Number and/or Department" v-if="hasPermission('UpdateNumberAndDept')" />
    <SubNavTab routeName="CancelGroupMember" title="Cancel Group Member" v-if="hasPermission('CancelGroupMember')" />
    <SubNavTab routeName="CancelDependent" title="Cancel Dependent" v-if="hasPermission('CancelDependent')" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../stores/auth'

export default {
  name: 'GroupMemberHome',
  components: { SubNavTab, TheSubNav },
  setup() {
    return { authStore: useAuthStore() }
  },
  methods: {
    hasPermission(permission) {
      return this.authStore.hasPermission(permission)
    },
  },
}
</script>
