<template>

  <div class="text_label">
    <label>{{label}}</label>
  </div>
  {{value}}
  
</template>

<script>
  export default {
    name: 'AppOutput',
    props: {
      label: String,
      value: String,
    },
  }
</script>

<style scoped>

  .text_label {
    display: flex;
  }

</style>