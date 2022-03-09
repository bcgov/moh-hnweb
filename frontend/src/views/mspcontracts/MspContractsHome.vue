<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="GetContractPeriods" title="Get Contract Periods" />
    <SubNavTab routeName="ContractInquiry" title="Contract Inquiry" />
    <SubNavTab routeName="GetGroupMembersContractAddress" title="Get Group Member's Contract Address" />
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'MspContractsHome',
}
</script>
