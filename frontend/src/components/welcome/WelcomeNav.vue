<template>
  <AppNav>
    <li :class="linkClass($route, 'Login')"><router-link :to="{ name: 'Login' }">MSP Direct Login</router-link></li>
    <li><AppLink href="#">What is new MSP Direct?</AppLink></li>
    <li :class="linkClass($route, 'CredentialsInfo')"><router-link :to="{ name: 'CredentialsInfo' }">What are new log-in Credentials?</router-link></li>
    <li>
      <AppLink href="https://www2.gov.bc.ca/gov/content/health/practitioner-professional-resources/system-access/msp-direct" popout>Transactions in MSP Direct?</AppLink>
    </li>
    <li>
      <AppLink href="https://www.bceid.ca/directories/bluepages/details.aspx?serviceId=7611&eServiceType=all" popout>Register for Business BCeID</AppLink>
    </li>
    <li>
      <AppLink href="https://www2.gov.bc.ca/gov/content?id=034E906146794F84A00535A3F9354913" popout>Set up the BC Services Card app</AppLink>
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
