<template>
  <TheSubNav>
    <SubNavTab routeName="ChangeEffectiveDate" title="Change Effective Date" v-if="hasPermission('ChangeEffectiveDate')" />
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
