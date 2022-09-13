<template>
  <AppRow>
    <AppCol class="welcome-nav-col">
      <WelcomeNav />
    </AppCol>
    <AppCol>
      <section id="loginButtons">
        <h1>MSP Direct Login</h1>
        <p>Welcome to the new MSP Direct. MSP Direct is an online business service, authorized by the Ministry of Health, that allows group plan administrators to do a number of adjustments to their group members account.</p>
        <p>Please log-in to the MSP Direct using one of the IDs:</p>
        <AppButton @click="login('phsa')" class="btn-xxl" id="phsaLogin">Health Authority ID</AppButton>
        <AppButton @click="login('idir')" class="btn-xxl" id="idirLogin">IDIR</AppButton>
        <AppButton @click="login('moh_idp')" class="btn-xxl" id="moh_idpLogin" v-if="enableKcLogin">Keycloak</AppButton>
        <AppButton @click="login('bcsc')" class="btn-xxl" id="bcscLogin">BC Services Card</AppButton>
        <AppButton @click="login('bceid_business')" id="bceid_businessLogin" class="btn-xxl">BCeID Business</AppButton>
      </section>
    </AppCol>
  </AppRow>
</template>

<script>
import WelcomeNav from '../../components/welcome/WelcomeNav.vue'
export default {
  name: 'login',
  components: { WelcomeNav },
  computed: {
    enableKcLogin: () => config.ENABLE_KC_LOGIN || import.meta.env.VITE_ENABLE_KC_LOGIN,
  },
  methods: {
    login: function (idpHint) {
      const options = {
        idpHint,
        redirectUri: location.origin + this.$router.resolve({ name: 'Home' }).path,
      }
      this.$keycloak.login(options)
    },
  },
}
</script>
<style scoped>
.welcome-nav-col {
  flex: 0 0 27%;
  max-width: 27%;
}
</style>
