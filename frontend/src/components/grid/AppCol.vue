<template>
  <div :class="colClass">
    <slot></slot>
  </div>
</template>
<script>
export default {
  name: 'AppRow',
  props: {
    class: String,
  },
  computed: {
    colClass() {
      return 'col ' + this.class
    },
  },
}
</script>
<style scoped>
.center {
  align-self: center;
}
.col {
  display: flex;
  flex-direction: column;
  flex-basis: 100%;
  flex: 1;
  padding-right: 15px;
}

.col1 {
  flex: 0 0 8.33%;
  max-width: 8.33%;
}

.col2 {
  flex: 0 0 16.67%;
  max-width: 16.67%;
}

.col3 {
  flex: 0 0 25%;
  max-width: 25%;
}

.col4 {
  flex: 0 0 33.33%;
  max-width: 33.33%;
}

.col5 {
  flex: 0 0 41.67%;
  max-width: 41.67%;
}

.col6 {
  flex: 0 0 50%;
  max-width: 50%;
}
.col7 {
  flex: 0 0 66.66%;
  max-width: 66.66%;
}
</style>
