<template>
  <div>
    <div class="text_label">
      <label>{{ label }}</label>
      <AppTooltip v-if="this.$slots.tooltip">
        <slot name="tooltip"></slot>
      </AppTooltip>
    </div>
    <div :class="groupClass">
      <AppRadioButton v-for="item in this.group" :id="item.value" :label="item.label" :value="item.value" v-model="modelValue" />
    </div>
    <div class="error-text" v-for="error in eModel.$errors">{{ error.$message.replace('Value', label) }}</div>
  </div>
</template>
<script>
import AppRadioButton from './AppRadioButton.vue'
import AppTooltip from './AppTooltip.vue'
export default {
  name: 'AppRadioButtonGroup',
  components: {
    AppRadioButton,
    AppTooltip,
  },
  computed: {
    groupClass() {
      return {
        'error-input': this.eModel.$error,
        'button-group': true,
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
.button-group {
  padding: 5px 0px 0px 5px;
}
.text_label {
  display: flex;
}

.error-input {
  border: 2px solid #606060;
  border-color: #d8292f !important;
  border-radius: 4px;
}

.error-text {
  color: #d8292f;
}
</style>
