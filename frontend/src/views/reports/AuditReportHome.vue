<template>
  <TheSubNav>
    <SubNavTab routeName="AuditReporting" title="Audit Reporting" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../stores/auth'

export default {
  name: 'AuditReportHome',
  components: { SubNavTab, TheSubNav },
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
