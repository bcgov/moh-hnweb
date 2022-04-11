<template>
  <div>
    <ul class="leftNav">
      <slot></slot>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'AppNav',
}
</script>
<style scoped>
.leftNav {
  border: 1px solid #e1e1e1;
  padding-bottom: 10px;
  padding-right: 10px;
  padding-left: 10px;
  width: 100%;
}

:slotted(li) {
  list-style: none;
  background-position: 0 4px;
  background-repeat: no-repeat;
  line-height: 20px;
  margin-top: 10px;
  padding-left: 5px;
  padding-right: 5px;
}

:slotted(li.current) {
  font-weight: bold;
  text-decoration: underline;
}

:slotted(a) {
  text-decoration: none;
  display: block;
  padding: 2px 0 0 4px;
}

:slotted(a:hover) {
  text-decoration: underline;
}
</style>
