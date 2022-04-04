<template>
  <AppRow>
    <AppCol class="col3">
      <section id="menu">
        <ul>
          <li><a href="#">What is new MSP Direct?</a></li>
          <li><router-link :to="{ name: 'CredentialsInfo' }">What are new log-in Credentials</router-link></li>
          <li><a href="https://www2.gov.bc.ca/gov/content/health/practitioner-professional-resources/system-access/msp-direct" target="_blank">Transactions in MSP Direct?</a></li>
          <li><a href="https://www2.gov.bc.ca/gov/content/governments/services-for-government/information-management-technology/identity-and-authentication-services" target="_blank">Get BC Services Card or BCeID</a></li>
        </ul>
      </section>
    </AppCol>
    <AppCol>
      <section id="loginButtons">
        <p>Welcome to the new-and-improved MSP Direct website. MSP Direct is an online business service, authorized by the Ministry of Health, that allows group plan administrators to do a number of adjustments to their group members account.</p>
        <p>Please log-in to the MSP Direct using one of the IDs:</p>
        <AppButton @click="login('phsa')" class="btn-xxl" id="phsaLogin">Health Authority ID</AppButton>
        <AppButton @click="login('idir')" class="btn-xxl" id="idirLogin">IDIR</AppButton>
        <AppButton @click="login('moh_idp')" class="btn-xxl" id="moh_idpLogin" v-if="!production">Keycloak</AppButton>
        <AppButton @click="login('bcsc')" class="btn-xxl" id="bcscLogin" v-if="false">BC Services Card</AppButton>
        <AppButton @click="login('bceid_business')" id="bceid_businessLogin" class="btn-xxl" v-if="false">BCeID Business</AppButton>
      </section>
    </AppCol>
  </AppRow>
</template>

<script>
export default {
  name: 'login',
  computed: {
    production: () => import.meta.env.MODE === 'production',
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
#menu {
  background-color: #ffffff;
  border: 2px solid #38598a;
  border-radius: 4px;
  margin: 5px 0 5px 0;
  padding: 5px;
}

li {
  margin-bottom: 10px;
}
</style>
