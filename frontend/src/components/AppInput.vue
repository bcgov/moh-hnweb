<template>

  <div class="text_label">
    <label>{{label}}</label>
  </div>
  <input :class="inputClass" :type="type" :value="modelValue" @input='$emit("update:modelValue", $event.target.value)'/>
  
  <div class="error-text" v-for="error in errorValue.$errors">
    {{error.$message.replace('Value', label)}}
  </div>
  
</template>

<script>
import useVuelidate from '@vuelidate/core'

  export default {
    name: 'AppInput',
    props: {
      errorValue: Object,
      label: String,
      modelValue: String,
      type: {
        type:  String,
        default: 'text'
      }
    },
    computed: {
      inputClass() {
        return {
          'text_input': true,
          'error-input' : this.errorValue.$error,
        }
      }
    }
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