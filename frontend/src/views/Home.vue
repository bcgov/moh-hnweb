<template>
  <section>
    <AppBulletin v-for="bulletin in bulletins" :key="bulletin.id" :content="bulletin.content"> </AppBulletin>
  </section>
  <section>
    <h1>Welcome to the New MSP Direct</h1>
    <p>MSP Direct has been updated to enhance user experience and to meet current Ministry of Health technology standards for web applications.</p>
    <p>MSP Direct has a new look with improved navigation functions. Users will still be able to make the required account adjustments to maintain their group members' accounts.</p>
    <p>To learn more about MSP Direct and available functions please visit: <a href="https://www2.gov.bc.ca/gov/content/health/practitioner-professional-resources/system-access/msp-direct" target="_blank">MSP Direct - Province of British Columbia (gov.bc.ca)</a>.</p>
  </section>
</template>

<script>
import AppBulletin from '../components/ui/AppBulletin.vue'
import BulletinService from '../services/BulletinService.js'
export default {
  name: 'home',
  components: {
    AppBulletin,
  },
  data() {
    return {
      bulletins: [],
    }
  },
  async created() {
    try {
      this.bulletins = (await BulletinService.getBulletins()).data
    } catch (err) {
      this.$store.commit('alert/setErrorAlert', `${err}`)
    }
  },
}
</script>
