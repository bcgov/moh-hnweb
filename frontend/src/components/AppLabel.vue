<template>
  <label class="text_label">{{title}}</label>
</template>

<script>
  export default {
    name: 'AppLabel',
    props: {
      for: String,
      title: String
    }
  }
</script>

<style>
  .text_label {
    display: flex;
  }
</style>