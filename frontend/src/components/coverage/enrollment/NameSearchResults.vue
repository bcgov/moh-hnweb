<template>
  <div>
    <p>
      Number of Matches: <b>{{ numberOfMatches }}</b>
    </p>
    <div v-if="hasCandidates">
      <NameSearchCandidate v-for="candidate in candidates" :candidate="candidate" :key="candidate.phn" />
    </div>
    <hr />
    <p>If the individual for whom you are searching is not listed, you can select "Create New PHN" or "Refine Search"</p>
    <AppRow>
      <AppButton @click="registerWithoutPHN" mode="secondary" type="button">Create New PHN</AppButton>
      <AppButton @click="refineSearch" mode="secondary" type="button">Refine Search</AppButton>
    </AppRow>
  </div>
</template>
<script>
import NameSearchCandidate from './NameSearchCandidate.vue'
import { useAlertStore } from '../../../stores/alert'

export default {
  name: 'NameSearchResults',
  props: {
    candidates: Array,
  },
  emits: ['set-page-action'],
  components: {
    NameSearchCandidate,
  },
  setup() {
    return {
      alertStore: useAlertStore(),
    }
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
      this.alertStore.dismissAlert()
      this.$emit('set-page-action', 'REGISTRATION')
    },
    refineSearch() {
      this.alertStore.dismissAlert()
      this.$emit('set-page-action', 'NAME_SEARCH')
    },
  },
}
</script>
