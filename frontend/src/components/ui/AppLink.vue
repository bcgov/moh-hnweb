<template>
  <a :href="href" :target="target">
    <slot></slot>
  </a>
  <font-awesome-icon class="external-link-alt" icon="external-link-alt" v-if="popout" />
</template>

<script>
export default {
  name: 'AppLink',
  props: {
    href: {
      type: String,
      required: true,
    },
    popout: {
      type: Boolean,
      required: false,
    },
  },
  computed: {
    target() {
      return this.popout ? '_blank' : null
    },
  },
}
</script>

<style scoped>
a {
  color: #1a5a96;
  padding: 2px 5px 0 4px;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.fa-external-link-alt {
  color: #1a5a96;
}
</style>
