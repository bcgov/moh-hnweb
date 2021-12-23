<template>
  <div>
    <p>Number of Matches: {{ candidates.length }}</p>
    <div v-for="candidate in candidates">
      <NameSearchCandidate :candidate="candidate" />
    </div>
  </div>
</template>
<script>
import NameSearchCandidate from './NameSearchCandidate.vue'

export default {
  name: 'NameSearchResults',
  props: {
    candidates: Array,
  },
  components: {
    NameSearchCandidate,
  },
}
</script>
