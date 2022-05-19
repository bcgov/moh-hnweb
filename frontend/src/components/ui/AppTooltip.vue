<template>
  <div class="tooltip">
    <font-awesome-icon class="tooltip-icon" icon="question-circle" />
    <div :class="tooltipTextClass"><slot></slot></div>
  </div>
</template>

<script>
export default {
  name: 'AppTooltip',
  props: {
    mode: {
      type: String,
      default: 'normal',
    },
  },
  computed: {
    tooltipTextClass() {
      return {
        tooltiptext: true,
        'tooltip-normal': this.mode === '' || this.mode === 'normal',
        'tooltip-header': this.mode === 'header',
      }
    },
  },
}
</script>

<style scoped>
.tooltip .tooltiptext {
  visibility: hidden;
  text-align: left;
  padding: 5px;
  margin-left: 5px;
  max-width: 500px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);

  /* Position the tooltip */
  position: absolute;
  z-index: 1;
}
.tooltip-normal {
  background-color: #38598a;
  color: #ffffff;
  opacity: 0.9;
}

:slotted(.tooltip-normal p) {
  color: #ffffff;
}

.tooltip-header {
  background-color: #ffffff;
  color: #000000;
  font-weight: normal;
  opacity: 1;
}

:slotted(.tooltip-header p) {
  color: #000000;
}

.tooltip:hover .tooltiptext {
  visibility: visible;
}

:slotted(li) {
  list-style: disc;
  margin-left: 20px;
}
</style>
