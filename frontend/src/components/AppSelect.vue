<script setup>
import AppInputError from './AppInputError.vue'
</script>

<template>

  <div class="text_label">
    <label>{{label}}</label>
  </div>

  <select :class="inputClass" @change="$emit('update:modelValue', $event.target.value)" v-bind="$attrs">
    <option v-for="option in options" v-bind:key="option.value" v-bind:value="option.value">
      {{ option.text }}
    </option>
  </select>

  <AppInputError :e-model="eModel" :label="label"/>
  
</template>

<script>
export default {
  name: 'AppSelect',
  props: {
    eModel: Object,
    label: String,
    modelValue: String,
    options: Object,
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
  border-color: #D8292F !important;
}

</style>