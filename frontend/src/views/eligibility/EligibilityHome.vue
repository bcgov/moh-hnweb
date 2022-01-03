<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="CheckEligibility" title="Check Eligibility" v-if="hasPermission('R15')" />
    <SubNavTab routeName="PhnInquiry" title="PHN Inquiry" />
    <SubNavTab routeName="PhnLookup" title="PHN Lookup"/>
    <SubNavTab routeName="CoverageStatusCheck" title="MSP Coverage Status Check" v-if="hasPermission('E45')" />
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'EligibilityHome',
  methods: {
    hasPermission(permission) {
      return this.$store.getters['auth/hasPermission'](permission)
    }
  }
}
</script>