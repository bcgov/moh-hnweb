<template>
  <div class="nav-tabs" style="width: 100%">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'TheSubNav'
}
</script>

<style scoped>
.nav-tabs {
    border-bottom: 4px solid #003366;
    margin-bottom: 20px;
    font-size: 15px;
}
</style>