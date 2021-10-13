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
.nav-tabs,
.nav-tabs a {
  color: #ffffff; 
}

.nav-tabs a {
  display: inline-block;
  padding: 0 20px;
  height: 36px;
  font-size: 16px;
  font-weight: bold;
  line-height: 40px;
}

</style>