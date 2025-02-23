<template>
  <div class="row">
    <slot></slot>
  </div>
</template>
<script>
export default {
  name: 'AppRow',
}
</script>
<style scoped>
.row {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-bottom: 5px;
  width: 100%;
}

.row-center {
  justify-content: center;
}

.flex-gap-100 {
  gap: 100px;
}

.flex-nowrap {
  flex-wrap: nowrap !important;
}
</style>
