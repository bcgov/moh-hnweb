<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="GetContractPeriods" title="Get Contract Periods" v-if="hasPermission('GetContractPeriods')" />
    <SubNavTab routeName="ContractInquiry" title="Contract Inquiry" v-if="hasPermission('ContractInquiry')" />
    <SubNavTab routeName="GetGroupMembersContractAddress" title="Get Group Member's Contract Address" v-if="hasPermission('GetGroupMembersContractAddress')" />
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'MspContractsHome',
  methods: {
    hasPermission(permission) {
      return this.$store.getters['auth/hasPermission'](permission)
    },
  },
}
</script>
