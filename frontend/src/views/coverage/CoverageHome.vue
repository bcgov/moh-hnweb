<script setup>
  import TheSubNav from '../../components/template/TheSubNav.vue'
</script>
<template>
   <TheSubNav>
     <a class="active-tab" href="#">Users</a>
     <a v-on:click="$emit('searchTabClicked')" class="inactive-tab">Users</a>
     <a v-on:click="$emit('searchTabClicked')" class="inactive-tab">Employees</a>
   </TheSubNav>
   <router-view/>
    <h1>Coverage</h1>
</template>

<script>
  export default {
    name: 'Coverage',
  }
</script>