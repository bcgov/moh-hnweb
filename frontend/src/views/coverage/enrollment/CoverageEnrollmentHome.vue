<script setup>
  import TheSubNav from '../../../components/template/TheSubNav.vue'
</script>
<template>
   <TheSubNav>
   </TheSubNav>
   <router-view/>
    <h1>Coverage Enrollment</h1>
</template>

<script>
  export default {
    name: 'CoverageEnrollmentHome',
  }
</script>