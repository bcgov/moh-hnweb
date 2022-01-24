<script setup>
import AppInputError from './AppInputError.vue'
</script>

<template>
  <div class="bc-gov-dropdown-label">
    <label>{{ label }}</label>
  </div>

  <div class="bc-gov-dropdown-wrapper">
    <font-awesome-icon icon="chevron-down" />
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
    eModel: {
      required: false,
      type: Object,
    },
    label: String,
    modelValue: String,
    options: Object,
  },
  computed: {
    dropdownClass() {
      return {
        'bc-gov-dropdown': true,
        'error-input': this.eModel?.$error,
      }
    },
  },
}
</script>

<style scoped>
.bc-gov-dropdown-label {
  display: flex;
}

.bc-gov-dropdown {
  font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
  font-size: 18px;
  color: #313132;
  background: white;
  box-shadow: none;
  border: 2px solid #606060;
  min-width: 200px;
  padding: 5px 5px 5px 7px;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  /* custom - different from bc gov */
  height: 34px;
  margin-top: 5px;
  margin-bottom: 15px;
  border-radius: 4px;
}

.fa-chevron-down {
  pointer-events: none;
  position: absolute;
  top: calc(1em - 4px);
  right: 1em;
}

.bc-gov-dropdown-wrapper {
  /* custom - different from bc gov */
  /* display: inline; removed */
  position: relative;
  display: flex;
  flex-direction: column;
}

:focus {
  outline: 2px solid #3b99fc;
  outline-offset: 1px;
}
.error-input {
  border-color: #d8292f !important;
}
</style>
