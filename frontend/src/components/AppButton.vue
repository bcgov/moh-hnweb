<template>
  <button class="app-button" :disabled="disabled" :type="type">{{title}}</button>
</template>

<script>
  export default {
    name: 'AppButton',
    props: {
      disabled: false,
      title: '',
      type: ''
    }
  }
</script>
<style scoped>
  .app-button {
    background-color: #38598a;
    border: 0px;
    box-shadow: 0px 3px 1px -2px rgb(0 0 0 / 20%), 0px 2px 2px 0px rgb(0 0 0 / 14%), 0px 1px 5px 0px rgb(0 0 0 / 12%);;     
    color: #ffffff;
    cursor: pointer;
    font-weight: 600;
    font-size: 14px;
    border-radius: 7px;
    margin: 2px;
    min-width: 75px;
    min-height: 35px;
    transition: 0.3s;
  }

  .app-button:disabled {
    background-color: #999999;
  }

  .app-button:disabled:hover {
    opacity: 1.0;
  }

  .app-button:hover {
    opacity: 0.9;
  }
</style>