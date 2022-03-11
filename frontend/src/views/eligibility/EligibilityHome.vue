<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

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
export default {
  name: 'EligibilityHome',
  methods: {
    hasPermission(permission) {
      return this.$store.getters['auth/hasPermission'](permission)
    },
  },
}
</script>
