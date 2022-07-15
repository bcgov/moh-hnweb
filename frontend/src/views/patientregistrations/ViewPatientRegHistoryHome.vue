<template>
  <TheSubNav>
    <SubNavTab routeName="ViewPatientRegHistory" title="View Registration History" v-if="hasPermission('ViewPatientRegHistory')" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../stores/auth'

export default {
  name: 'ViewPatientRegHistoryHome',
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
