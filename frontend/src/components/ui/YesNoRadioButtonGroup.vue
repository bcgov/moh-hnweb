<template>
  <AppRadioButtonGroup :e-model="eModel" :id="id" :label="label" :group="this.YES_NO_RADIO_BUTTON_GROUP" :tooltip="tooltip" :tooltipText="tooltipText" v-model="modelValue" />
</template>

<script>
import AppRadioButtonGroup from './AppRadioButtonGroup.vue'

export default {
  name: 'YesNoRadioButtonGroup',
  components: {
    AppRadioButtonGroup,
  },
  props: {
    eModel: Object,
    label: { default: 'Choose Yes or No', type: String },
    id: { default: 'yesNo', type: String },
    modelValue: String,
    tooltip: Boolean,
    tooltipText: String,
  },
  created() {
    this.YES_NO_RADIO_BUTTON_GROUP = [
      { label: 'Yes', value: 'Y' },
      { label: 'No', value: 'N' },
    ]
  },
  watch: {
    modelValue: function (val) {
      //update the model
      if (val) {
        this.$emit('update:modelValue', val)
      }
    },
  },
}
</script>
