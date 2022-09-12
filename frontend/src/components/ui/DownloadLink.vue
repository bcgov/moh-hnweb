<template>
  <a :href="href" :class="linkClass" :downloading="downloading">
    <slot></slot>
    <span v-if="downloading">
      <font-awesome-icon icon="spinner" class="fa-spin" />
    </span>
  </a>
</template>

<script>
export default {
  name: 'DownloadLink',
  props: {
    href: {
      type: String,
      required: true,
    },
    downloading: {
      type: Boolean,
      default: false,
    },
  },

  computed: {
    linkClass() {
      return {
        isDisabled: this.downloading,
        download: !this.downloading,
      }
    },
  },
}
</script>
<style scoped>
.download {
  float: right;
}
.isDisabled {
  float: right;
  pointer-events: none;
}
</style>
