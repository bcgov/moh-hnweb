<script setup>
import AppInputError from './AppInputError.vue'
</script>

<template>

  <div class="text_label">
    <label>{{label}}</label>
  </div>

  <input :class="inputClass" :value="modelValue" @input='$emit("update:modelValue", $event.target.value)' v-bind="$attrs"/>
  
  <AppInputError :e-model="eModel" :label="label"/>
  
</template>

<script>
export default {
  name: 'AppInput',
  props: {
    eModel: Object,
    label: String,
    modelValue: String,
  },
  computed: {
    inputClass() {
      return {
        'text_input': true,
        'error-input' : this.eModel.$error,
      }
    }
  }
}
</script>

<style scoped>
  .text_input {
    font-family: 'BCSans', 'Noto Sans', Verdana, Arial, sans-serif;
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
    outline: 2px solid #3B99FC;
    outline-offset: 1px;
  }

  .text_label {
    display: flex;
  }

  .error-input {
    border-color: crimson !important;
  }

</style>