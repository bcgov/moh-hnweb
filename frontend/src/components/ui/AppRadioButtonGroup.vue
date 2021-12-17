<template>
  <div>
    <div class="text_label">
      <label>{{ label }}</label>
    </div>
    <div :class="inputClass">
      <AppRadioButton v-for="item in this.group" :id="item.value" :label="item.label" :value="item.value" v-model="modelValue" />
    </div>
    <div class="error-text" v-for="error in eModel.$errors">{{ error.$message.replace('Value', label) }}</div>
  </div>
</template>
<script>
import AppRadioButton from '../AppRadioButton.vue'
export default {
  name: 'AppRadioButtonGroup',
  components: {
    AppRadioButton,
  },
  computed: {
    inputClass() {
      return {
        'error-input': this.eModel.$error,
      }
    },
  },
  props: {
    eModel: Object,
    label: String,
    modelValue: String,
    group: Array,
  },
  watch: {
    modelValue: function (val) {
      //update the model when the radio selection changes.
      if (val) {
        this.$emit('update:modelValue', val)
      }
    },
  },
}
</script>

<style scoped>
.text_label {
  display: flex;
}
.error-input {
  border: 2px solid #606060;
  border-color: #d8292f !important;
}

.error-text {
  color: #d8292f;
}
</style>
