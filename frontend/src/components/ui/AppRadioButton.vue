<template>
  <label class="radio">
    {{ label }}
    <input type="radio" :name="name" :checked="isChecked" :value="value" @change="$emit('update:modelValue', $event.target.value)" v-bind="$attrs" />
    <span class="dot"></span>
  </label>
</template>

<script>
export default {
  name: 'AppRadioButton',
  props: {
    label: String,
    modelValue: String,
    value: { type: String, default: undefined },
    name: String,
  },
  computed: {
    isChecked() {
      return this.modelValue == this.value
    },
  },
}
</script>

<style scoped>
/* Customize the label (the container) */
.radio {
  display: block;
  position: relative;
  padding-left: 30px;
  margin-bottom: 6px;
  cursor: pointer;
  font-size: 18px;
  font-weight: 400;
  font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

/* Hide the browser's default radio button */
.radio input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}

/* Create a custom radio button */
.dot {
  position: absolute;
  top: 1px;
  left: 0;
  height: 18px;
  width: 18px;
  border-radius: 50%;
  border: 2px solid #606060;
}

/* When the radio button is checked, add a blue background */
.radio input:checked ~ .dot {
  background-color: #ffffff;
}

/* Custom checkbox has blue outline when in focus */
.radio input:focus ~ .dot {
  outline: 4px solid #3b99fc;
  outline-offset: 1px;
}

/* Create the indicator (the dot/circle - hidden when not checked) */
.dot:after {
  content: '';
  position: absolute;
  display: none;
  top: 50%;
  left: 50%;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #606060;
  transform: translate(-50%, -50%);
}

/* Show the indicator (dot/circle) when checked */
.radio input:checked ~ .dot:after {
  display: block;
}
</style>
