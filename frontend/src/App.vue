<script setup>
// This starter template is using Vue 3 <script setup> SFCs
// Check out https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup
import TheFooter from './components/template/TheFooter.vue'
import TheHeader from './components/template/TheHeader.vue'
import TheNavBar from './components/template/TheNavBar.vue'
</script>

<template>
    <the-header></the-header>
    <the-nav-bar></the-nav-bar>
    <main>
      <section class="content">
        <!--<alert/>-->
        <router-view></router-view>
      </section>
      <!--<v-checkbox v-model="showKeycloakTools" :label="`Keycloak Dev Tools`" v-if="vueCliMode === 'development'"></v-checkbox>-->
      <!--<KeycloakDevTools v-if="showKeycloakTools" />-->
    </main>
    <the-footer></the-footer>
</template>

<style src=./assets/css/main.css></style>
<style src=./assets/css/reset.css></style>
<style src=./assets/css/typography.css></style>