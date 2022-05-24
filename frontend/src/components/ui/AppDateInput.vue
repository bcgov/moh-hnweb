<template>
  <div class="text_label">
    <label>{{ label }}</label>
    <AppTooltip v-if="$slots.tooltip"><slot name="tooltip"></slot></AppTooltip>
  </div>
  <div v-bind="$attrs">
    <Datepicker :autoApply="true" :class="inputClass" :enableTimePicker="false" :format="inputDateFormat" :placeholder="placeholder" :text-input="true" :monthPicker="$attrs.monthPicker" :uid="$attrs.id" v-model="value" />
  </div>
  <AppInputError :e-model="eModel" :label="label" />
</template>

<script>
import AppInputError from './AppInputError.vue'
import AppTooltip from './AppTooltip.vue'

import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import { INPUT_DATE_FORMAT, OUTPUT_DATE_FORMAT } from '../../util/constants.js'

export default {
  name: 'AppDateInput',
  components: { AppTooltip, AppInputError, Datepicker },
  props: {
    eModel: {
      required: false,
      type: Object,
    },
    label: String,
    modelValue: Object,
    inputDateFormat: {
      default: INPUT_DATE_FORMAT,
      type: String,
    },
    placeholder: {
      default: OUTPUT_DATE_FORMAT,
      type: String,
    },
  },
  computed: {
    value: {
      get() {
        return this.modelValue
      },
      set(value) {
        this.$emit('update:modelValue', value)
      },
    },
    inputClass() {
      return {
        text_input: true,
        'error-input': this.eModel?.$error,
      }
    },
  },
}
</script>

<style>
.dp__theme_light {
  --dp-border-color: #606060;
  --dp-text-color: #000000;
  --dp-hover-color: #6583b0;
  /*   --dp-background-color: #ffffff;    
  --dp-hover-color: #f3f3f3;
  --dp-primary-color: #1976d2;
  --dp-primary-text-color: #f8f5f5;
  --dp-secondary-color: #c0c4cc;    
  --dp-border-color-hover: #aaaeb7;
  --dp-disabled-color: #f6f6f6;
  --dp-scroll-bar-background: #f3f3f3;
  --dp-scroll-bar-color: #959595;
  --dp-success-color: #76d275;
  --dp-icon-color: #959595;
  --dp-danger-color: #ff6f60; */
}

input.dp__input {
  border-color: #606060;
  border-width: 2px;
  color: #000000;
  font-family: 'BCSans';
  font-size: 18px;
  height: 34px;
  margin-top: 5px;
  margin-bottom: 15px;
}

.text_label {
  display: flex;
}

.dp__input:focus {
  outline: 2px solid #3b99fc;
  outline-offset: 1px;
}

.error-input input {
  border-color: #d8292f !important;
}

.tooltip-icon {
  margin-left: 5px;
}
</style>
