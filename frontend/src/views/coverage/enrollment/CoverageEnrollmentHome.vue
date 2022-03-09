<script setup>
import TheSubNav from '../../../components/template/TheSubNav.vue'
import SubNavTab from '../../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="AddVisaResidentWithoutPHN" title="Add Study Permit holder without PHN" v-if="hasPermission('AddPermitHolderWOPHN')" />
    <SubNavTab routeName="AddVisaResidentWithPHN" title="Add Study Permit holder with PHN" v-if="hasPermission('AddPermitHolderWithPHN')" />
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'CoverageEnrollmentHome',
  methods: {
    hasPermission(permission) {
      return this.$store.getters['auth/hasPermission'](permission)
    },
  },
}
</script>
