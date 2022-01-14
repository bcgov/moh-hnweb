<template>
  <div class="tooltip">
    <slot></slot>
    <span class="tooltiptext">{{ tooltipText }}</span>
  </div>
</template>

<script>
export default {
  name: "AppTooltip",
  props: {
    tooltipText: String,
  },
}
</script>

<style scoped>

  .tooltip .tooltiptext {
    visibility: hidden;
    background-color: #000000;
    opacity: 0.80;
    color: #fff;
    text-align: center;
    border-radius: 6px;
    padding: 5px;
    margin-left: 5px;

    /* Position the tooltip */
    position: absolute;
    z-index: 1;
  }

  .tooltip:hover .tooltiptext {
    visibility: visible;
  }
</style>