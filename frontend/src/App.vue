<script setup>
  // This starter template is using Vue 3 <script setup> SFCs
  // Check out https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup
  import TheFooter from './components/template/TheFooter.vue'
  import TheHeader from './components/template/TheHeader.vue'
  import TheNavBar from './components/template/TheNavBar.vue'
  import KeycloakDevTools from './components/KeycloakDevTools.vue'
  import TheAlert from './components/TheAlert.vue'
</script>

<template>
  <TheHeader/>
  <TheNavBar/>
  <main>
    <section class="content">
      <TheAlert/>
      <router-view></router-view>
    </section>
    <KeycloakDevTools v-if="dev"/>
  </main>
  <TheFooter/>
</template>

<script>
  import SecurityService from './services/SecurityService.js'
  export default {
    name: 'App',
    data: function () {
      return {
        dev: import.meta.env.DEV,
      }
    },
    async created() {
      const permissions = (await SecurityService.getPermissions()).data
      this.$store.dispatch('auth/setPermissions', permissions)
    }
  }
</script>

<style src=./assets/css/main.css></style>
<style src=./assets/css/reset.css></style>
<style src=./assets/css/typography.css></style>