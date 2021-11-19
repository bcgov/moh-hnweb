<script setup>
import TheSubNav from '../../../components/template/TheSubNav.vue'
import SubNavTab from '../../../components/template/SubNavTab.vue'
</script>

<template>
    <TheSubNav>
      <SubNavTab routeName="AddVisaResidentWithoutPHN" title="Add Visa Resident without PHN" />
      <SubNavTab routeName="AddVisaResidentWithPHN" title="Add Visa Resident with PHN" />
   </TheSubNav>
   <router-view/>
</template>

<script>
  export default {
    name: 'CoverageEnrollmentHome',
  }
</script>