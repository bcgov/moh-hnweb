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
  <TheHeader :key="$route.fullPath"/>
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
  export default {
    name: 'App',
    data: function () {
      return {
        dev: import.meta.env.MODE === 'development',
      }
    },
  }
</script>

<style src=./assets/css/main.css></style>
<style src=./assets/css/reset.css></style>
<style src=./assets/css/typography.css></style>