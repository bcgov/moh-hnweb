<script setup>
import AppInputError from './AppInputError.vue'
</script>

<template>
  <label class="checkbox">
    {{ label }}
    <input type="checkbox" :checked="modelValue" @change="$emit('update:modelValue', $event.target.checked)" v-bind="$attrs" />
    <span class="checkmark"></span>
  </label>

  <AppInputError :e-model="eModel" :label="label" />
</template>

<script>
export default {
  name: 'AppCheckbox',
  props: {
    eModel: {
      type: Object,
      required: false,
    },
    label: String,
    modelValue: Boolean,
  },
  computed: {
    inputClass() {
      return {
        text_input: true,
        'error-input': this.eModel?.$error,
      }
    },
  },
}
</script>

<style scoped>
/* Customize the label (the container) */
.checkbox {
  display: block;
  position: relative;
  padding-left: 25px;
  margin-bottom: 12px;
  cursor: pointer;
  font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
  font-size: 16px;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

/* Hide the browser's default checkbox */
.checkbox input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}

/* Create a custom checkbox */
.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 16px;
  width: 16px;
  outline: 2px solid #606060;
}

/* When the checkbox is checked, add a blue background */
.checkbox input:checked ~ .checkmark {
  background-color: #606060;
}

/* Create the checkmark/indicator (hidden when not checked) */
.checkmark:after {
  content: '\2713';
  color: white;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: none;
}

/* Show the checkmark when checked */
.checkbox input:checked ~ .checkmark:after {
  display: block;
}

.error-input {
  border-color: #d8292f !important;
}
</style>
