<template>
  <TheSubNav>
    <SubNavTab routeName="CheckEligibility" title="Check Eligibility" v-if="hasPermission('CheckEligibility')" />
    <SubNavTab routeName="PhnInquiry" title="PHN Inquiry" v-if="hasPermission('PHNInquiry')" />
    <SubNavTab routeName="PhnLookup" title="PHN Lookup" v-if="hasPermission('PHNLookup')" />
    <SubNavTab routeName="CoverageStatusCheck" title="MSP Coverage Status Check" v-if="hasPermission('MSPCoverageCheck')" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../stores/auth'

export default {
  components: { SubNavTab, TheSubNav },
  name: 'EligibilityHome',
  setup() {
    const authStore = useAuthStore()

    return { authStore }
  },
  methods: {
    hasPermission(permission) {
      return this.authStore.hasPermission(permission)
    },
  },
}
</script>
