<template>
  <div class="instructions">
    <a href="#" id="show-modal" @click="showModal = true">Instructions</a>
  </div>
  <Teleport to="body">
    <!-- use the modal component, pass in the prop -->
    <AppModal :show="showModal" @close="showModal = false">
      <template #header>
        <h3>Instructions</h3>
      </template>
      <template #body>
        <slot></slot>
      </template>
      <template #footer></template>
    </AppModal>
  </Teleport>
</template>
<script>
import AppModal from './AppModal.vue'
export default {
  name: 'AppHelp',
  components: { AppModal },
  data() {
    return {
      showModal: false,
    }
  },
}
</script>
<style scoped>
.instructions {
  float: right;
}

:slotted(ul) {
  list-style: disc;
  margin-left: 20px;
}
</style>
