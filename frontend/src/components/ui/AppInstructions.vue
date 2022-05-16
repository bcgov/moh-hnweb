<template>
  <div :class="instructionsClass">
    <a href="#" @click="handleOnClick()"><span class="instruction-text">Instructions</span></a>
    <!-- <Transition> -->
    <div class="showHide" v-if="expanded">
      <slot></slot>
    </div>
    <!-- </Transition> -->
  </div>
</template>
<script>
import { Transition } from 'vue'

export default {
  name: 'AppInstructions',
  components: {},
  data() {
    return {
      expanded: false,
    }
  },
  computed: {
    instructionsClass() {
      return {
        instructions: true,
        show: this.expanded,
      }
    },
  },
  methods: {
    handleOnClick() {
      this.expanded = !this.expanded
    },
  },
}
</script>
<style scoped>
a {
  color: #1a5a96;
  outline: none;
  text-decoration: none;
}
a:hover {
  text-decoration: underline;
}
div.instructions {
  background: #ffffff;
  border: 1px solid #535d6e;
  border-radius: 6px;
  -moz-border-radius: 5px;
  -webkit-borderradius: 5px;
  padding: 11px 10px 8px;
  width: 11%;
  text-align: center;
  margin-bottom: 10px;
  float: right;
}
div.instructions.show {
  width: 100%;
  text-align: right;
}
.instruction-text {
  font-size: 1.2em;
  color: #494949;
}

/* .absolute-right {
  position: absolute;
  right: 55px;
} */
.show {
}
.showHide {
  padding-top: 10px;
  text-align: left;
  display: block;
}

.v-enter-active {
  transition: opacity 0.5s ease;
}
.v-leave-active {
}

.v-enter-from {
  opacity: 0;
}
.v-leave-to {
  opacity: 0;
}
</style>
