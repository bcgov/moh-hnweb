<script setup>
  // This starter template is using Vue 3 <script setup> SFCs
  // Check out https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup
  import TheFooter from './components/template/TheFooter.vue'
  import TheHeader from './components/template/TheHeader.vue'
  import TheNavBar from './components/template/TheNavBar.vue'
  import KeycloakDevTools from './components/KeycloakDevTools.vue'
</script>

<template>
  <the-header></the-header>
   <the-nav-bar></the-nav-bar>
   <main>
     <section class="content">
       <!--<alert/>-->
       <router-view></router-view>
      </section>
      <KeycloakDevTools v-if="dev"/>
    </main>
  <the-footer></the-footer>
</template>

<script>
  export default {
    name: 'App',
    data: function () {
      return {
        dev: import.meta.env.DEV,
      }
    }
  }
</script>

<style src=./assets/css/main.css></style>
<style src=./assets/css/reset.css></style>
<style src=./assets/css/typography.css></style>
