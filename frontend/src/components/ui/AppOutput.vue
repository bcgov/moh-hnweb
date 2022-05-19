<template>
  <div class="text_label">
    <label>{{ label }}</label>
    <AppTooltip v-if="$slots.tooltip">
      <slot name="tooltip"></slot>
    </AppTooltip>
  </div>
  <span>{{ value }}</span>
</template>

<script>
import AppTooltip from './AppTooltip.vue'
export default {
  name: 'AppOutput',
  components: { AppTooltip },
  props: {
    label: String,
    value: String,
  },
}
</script>

<style scoped>
.text_label {
  display: flex;
}
</style>
