<template>
  <TheSubNav>
    <SubNavTab routeName="AddVisaResidentWithoutPHN" title="Add Study Permit Holder without PHN" v-if="hasPermission('AddPermitHolderWOPHN')" />
    <SubNavTab routeName="AddVisaResidentWithPHN" title="Add Study Permit Holder with PHN" v-if="hasPermission('AddPermitHolderWithPHN')" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../../components/template/TheSubNav.vue'
import SubNavTab from '../../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../../stores/auth'
export default {
  name: 'CoverageEnrollmentHome',
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
