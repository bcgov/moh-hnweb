<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="CheckEligibility" title="Check Eligibility" />
    <SubNavTab routeName="PhnEnquiry" title="PHN Enquiry" />
    <SubNavTab routeName="PhnLookup" title="PHN Lookup"/>
    <SubNavTab routeName="CoverageStatusCheck" title="MSP Coverage Status Check" />
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'EligibilityHome',
}
</script>