<template>

  <div class="text_label">
    <label>{{label}}</label>
  </div>
  <span>{{value}}</span>
  
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