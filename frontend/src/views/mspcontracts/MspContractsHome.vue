<template>
  <TheSubNav>
    <SubNavTab routeName="GetContractPeriods" title="Get Contract Periods" v-if="hasPermission('GetContractPeriods')" />
    <SubNavTab routeName="ContractInquiry" title="Contract Inquiry" v-if="hasPermission('ContractInquiry')" />
    <SubNavTab routeName="GetGroupMembersContractAddress" title="Get Contract Address" v-if="hasPermission('GetContractAddress')" />
    <SubNavTab routeName="UpdateContractAddress" title="Update Contract Address" v-if="hasPermission('UpdateContractAddress')" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../stores/auth'

export default {
  name: 'MspContractsHome',
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
