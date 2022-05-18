<template>
  <Transition name="modal">
    <div v-if="show" class="modal-mask" @keydown.esc="$emit('close')" tabindex="0">
      <div class="modal-wrapper">
        <div class="modal-container" v-draggable="">
          <div class="modal-header">
            <slot name="header"></slot>
          </div>

          <div class="modal-body">
            <slot name="body"></slot>
          </div>

          <div class="modal-footer">
            <slot name="footer">
              <AppButton class="modal-default-button" mode="secondary" @click="$emit('close')">OK</AppButton>
            </slot>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>
<script>
export default {
  props: {
    show: Boolean,
  },
}
</script>

<style>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-container {
  width: 600px;
  margin: 0px auto;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
  cursor: grab;
  max-height: 600px;
  overflow: auto;
}

.modal-header h3 {
  margin-top: 0;
  color: #003366;
}

.modal-body {
  margin: 20px 0;
}

.modal-footer {
  padding-bottom: 40px;
}

.modal-default-button {
  float: right;
}

:slotted(ul) {
  list-style: disc;
  margin-left: 20px;
}

/*
 * The following styles are auto-applied to elements with
 * transition="modal" when their visibility is toggled
 * by Vue.js.
 *
 * You can easily play with the modal transition by editing
 * these styles.
 */

.modal-enter-from {
  opacity: 0;
}

.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
