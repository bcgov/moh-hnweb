<template>
  <label class="text_label"><slot></slot></label>
</template>

<script>
  export default {
    name: 'AppLabel',
  }
</script>

<style scoped>
  .text_label {
    display: flex;
  }
</style>