<template>
  <div class="row">
    <slot></slot>
  </div>
</template>
<script>
  export default {
    name: 'AppRow'
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
  
.row1 {
  justify-content: center;
}

</style>