<template>
  <TheSubNav>
    <SubNavTab routeName="ChangeEffectiveDate" title="Change Effective Date" v-if="hasPermission('ChangeEffectiveDate')" />
    <SubNavTab routeName="ChangeCancelDate" title="Change Cancel Date" v-if="hasPermission('ChangeCancelDate')" />
    <SubNavTab routeName="ExtendCancelDate" title="Extend Cancel Date" v-if="hasPermission('ExtendCancelDate')" />
    <SubNavTab routeName="ReinstateOverAgeDependent" title="Reinstate OverAge Dependent" v-if="hasPermission('ReinstateOverAgeDependent')" />
    <SubNavTab routeName="RenewCancelledGroupCoverage" title="Renew Cancelled Group Coverage" v-if="hasPermission('RenewCancelledCoverage')" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../../components/template/TheSubNav.vue'
import SubNavTab from '../../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../../stores/auth'

export default {
  name: 'Coverage Maintenance',
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
