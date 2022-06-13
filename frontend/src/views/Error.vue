<template>
  <h1>System Error</h1>
  <div>
    <p>An error has occurred with MSP Direct. Please contact application support at <a href="mailto:HLTH.HnetConnection@gov.bc.ca?subject=MSP Direct Error">HLTH.HnetConnection@gov.bc.ca</a></p>
    <p>Additional contact info is available on the <router-link @click="resetAlert()" :to="{ name: 'Help' }">Help</router-link> screen.</p>
  </div>
</template>
<script>
import { useAlertStore } from '../stores/alert'

export default {
  name: 'Error',
  setup() {
    return { alertStore: useAlertStore() }
  },
  methods: {
    resetAlert() {
      this.alertStore.dismissAlert()
    },
  },
}
</script>
