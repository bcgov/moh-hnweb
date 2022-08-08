<template>
  <TheSubNav>
    <SubNavTab routeName="ViewPatientRegistration" title="View Patient Registration" v-if="hasPermission('ViewPatientRegistration')" />
  </TheSubNav>
  <router-view />
</template>

<script>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
import { useAuthStore } from '../../stores/auth'

export default {
  name: 'ViewPatientRegistrationHome',
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
