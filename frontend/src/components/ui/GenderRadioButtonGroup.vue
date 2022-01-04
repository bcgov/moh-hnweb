<template>
  <AppRadioButtonGroup :e-model="eModel" id="gender" label="Gender" :group="this.GENDER_RADIO_BUTTON_GROUP" v-model="modelValue" />
</template>

<script>
import AppRadioButtonGroup from './AppRadioButtonGroup.vue'

export default {
  name: 'GenderRadioButtonGroup',
  components: {
    AppRadioButtonGroup,
  },
  props: {
    eModel: Object,
    label: String,
    modelValue: String,
  },
  data() {
    return {
      gender: null,
    }
  },
  created() {
    this.GENDER_RADIO_BUTTON_GROUP = [
      { label: 'Male', value: 'M' },
      { label: 'Female', value: 'F' },
      { label: 'Unknown', value: 'U' },
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
