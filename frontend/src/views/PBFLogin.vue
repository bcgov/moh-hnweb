<template>
  <AppRow>
    <AppCol>
      <section id="loginButtons">
        <h1>PBF Direct Login</h1>
        <p>This application supports Population Based Funding clinics with patient and provider registrations. Clinics can also validate and update patient addresses via HCIMWeb, which is available in the menu bar once connected.</p>
        <AppButton @click="login('bcsc')" class="btn-xxl" id="bcscLogin">BC Services Card</AppButton>
        <AppButton @click="login('idir')" class="btn-xxl" id="idirLogin">IDIR</AppButton>
        <AppButton @click="login('bceid_business')" id="bceid_businessLogin" class="btn-xxl">BCeID Business</AppButton>
        <AppButton @click="login('moh_idp')" class="btn-xxl" id="moh_idpLogin" v-if="enableKcLogin">Keycloak</AppButton>
      </section>
    </AppCol>
  </AppRow>
</template>

<script>
export default {
  name: 'PBFLogin',
  computed: {
    enableKcLogin: () => config.ENABLE_KC_LOGIN || import.meta.env.VITE_ENABLE_KC_LOGIN,
  },
  methods: {
    login: function (idpHint) {
      const options = {
        idpHint,
        redirectUri: location.origin + this.$router.resolve({ name: 'PatientRegistration' }).path,
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
