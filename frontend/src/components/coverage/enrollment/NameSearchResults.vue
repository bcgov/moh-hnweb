<template>
  <div>
    <p>Number of Matches: {{ candidates.length }}</p>
    <div v-for="candidate in candidates">
      <NameSearchCandidate :candidate="candidate" @select-candidate="selectCandidate" />
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
  methods: {
    selectCandidate(candidate) {
      console.log(`selected candidate from results [${candidate.surname}; ${candidate.phn}`)
      this.$emit('add-candidate', candidate)
    },
  },
}
</script>
