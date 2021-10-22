<template>
  <label class="app-label" :for="for">{{title}}</label>
</template>

<script>
  export default {
    name: 'AppButton',
    props: {
      for: '',
      title: ''
    }
  }
</script>

<style>
  .app-label {
    font-size: 16px;
    font-weight: 600;
  }
</style>