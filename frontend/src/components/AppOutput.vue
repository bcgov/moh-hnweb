<template>

  <div class="text_label">
    <label>{{label}}</label>
  </div>
  {{value}}
  
</template>

<script>
import useVuelidate from '@vuelidate/core'

  export default {
    name: 'AppOutput',
    props: {
      label: String,
      value: String,
    },
  }
</script>

<style>
  .text_input {
    font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
    font-size: 18px;
  }

  .text_input {
    height: 34px;
    border: 2px solid #606060;
    margin-top: 5px;
    margin-bottom: 15px;
    border-radius: 4px;
    padding: 5px 5px 5px 7px;
  }

  .text_input[type="text"]:focus {
    outline: 4px solid #3B99FC;
    outline-offset: 1px;
  }

  .text_label {
    display: flex;
  }

  .error-input {
    border-color: crimson;
    border-width: 2px;
  }
  
  .error-text {
    color: crimson;
    margin-top: -15px;
  }
</style>