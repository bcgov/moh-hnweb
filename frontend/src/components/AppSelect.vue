<script setup>
import AppInputError from './AppInputError.vue'
</script>

<template>
  <label class="bc-gov-dropdown-label">{{ label }}</label>

  <div class="bc-gov-dropdown-wrapper">
    <i class="fas fa-chevron-down"></i>
    <select :class="dropdownClass" :value="modelValue" @change="$emit('update:modelValue', $event.target.value)" v-bind="$attrs">
      <option v-for="option in options" v-bind:key="option.value" v-bind:value="option.value">
        {{ option.text }}
      </option>
    </select>
  </div>

  <AppInputError :e-model="eModel" :label="label" />
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
    dropdownClass() {
      return {
        'bc-gov-dropdown': true,
        'error-input': this.eModel.$error,
      }
    },
  },
}
</script>

<style scoped>
.bc-gov-dropdown-label {
  margin-bottom: 10px;
}

.bc-gov-dropdown {
  font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
  font-size: 18px;
  color: #313132;
  background: white;
  box-shadow: none;
  border: 2px solid #606060;
  min-width: 200px;
  padding: 8px 45px 8px 15px;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

.fa-chevron-down {
  pointer-events: none;
  position: absolute;
  top: calc(1em - 4px);
  right: 1em;
}

.bc-gov-dropdown-wrapper {
  position: relative;
  display: inline;
}

:focus {
  outline: 4px solid #3b99fc;
  outline-offset: 1px;
}
.error-input {
  border-color: #d8292f !important;
}
</style>
