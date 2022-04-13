<template>
  <AppNav>
    <li :class="linkClass($route, 'Login')"><router-link :to="{ name: 'Login' }">MSP Direct Login</router-link></li>
    <li><AppLink href="#">What is new MSP Direct?</AppLink></li>
    <li :class="linkClass($route, 'CredentialsInfo')"><router-link :to="{ name: 'CredentialsInfo' }">What are new log-in Credentials?</router-link></li>
    <li>
      <AppLink href="https://www2.gov.bc.ca/gov/content/health/practitioner-professional-resources/system-access/msp-direct" popout>Transactions in MSP Direct?</AppLink>
    </li>
    <li>
      <AppLink href="https://www2.gov.bc.ca/gov/content/governments/services-for-government/information-management-technology/identity-and-authentication-services" popout>Get BC Services Card or BCeID</AppLink>
    </li>
  </AppNav>
</template>
<script>
import AppLink from '../ui/AppLink.vue'
import AppNav from '../ui/AppNav.vue'
export default {
  name: 'WelcomeNav',
  components: { AppLink, AppNav },
  methods: {
    linkClass(route, routeName) {
      return route.name === routeName ? 'current' : ''
    },
  },
}
</script>
