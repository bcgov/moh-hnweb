<template>
  <div>
    <p>
      Number of Matches: <b>{{ numberOfMatches }}</b>
    </p>
    <div v-if="hasCandidates">
      <NameSearchCandidate v-for="candidate in candidates" :candidate="candidate" :key="candidate.phn" />
    </div>
    <hr />
    <p>If none of the candidates returned match the client for whom you would like to add a study permit, please click "Create New PHN"</p>
    <AppRow>
      <AppButton @click="registerWithoutPHN" mode="secondary" type="button">Create New PHN</AppButton>
      <AppButton @click="refineSearch" mode="secondary" type="button">Refine Search</AppButton>
    </AppRow>
  </div>
</template>
<script>
import NameSearchCandidate from './NameSearchCandidate.vue'

export default {
  name: 'NameSearchResults',
  props: {
    candidates: Array,
  },
  emits: ['set-page-action'],
  components: {
    NameSearchCandidate,
  },
  computed: {
    numberOfMatches() {
      return this.candidates ? this.candidates.length : 0
    },
    hasCandidates() {
      return this.candidates && this.candidates.length > 0
    },
  },
  methods: {
    registerWithoutPHN() {
      this.$emit('set-page-action', 'REGISTRATION')
    },
    refineSearch() {
      this.$emit('set-page-action', 'NAME_SEARCH')
    },
  },
}
</script>
