<template>
  <input :class="styleClass" :id="id" :type="type" :value="modelValue" @input='$emit("update:modelValue", $event.target.value)'/>
  
  <div class="error-text" v-for="error in errorValue.$errors">
    {{error.$message}}
  </div>
  
</template>

<script>
import useVuelidate from '@vuelidate/core'

  export default {
    name: 'AppInput',
      setup () {
        return {
          v$: useVuelidate()
        }
      },
    props: {
      id: '',
      type: '',
      modelValue: '',
      errorValue: ''
    },
    computed: {
      styleClass() {
        return 'app-input ' + (this.errorValue.$error && 'error-input')
      }
    }
  }
</script>

<style>
  .app-input {
    border: 1px rgb(133, 133, 133) solid;
    font-size: 16px;
    font-weight: 400;
    line-height: 20px;
    padding: 5px;
    width: 200px;
  }

  .error-input {
    border-color: crimson;
    border-width: 2px;
  }
  
  .error-text {
    color: crimson;
  }
</style>