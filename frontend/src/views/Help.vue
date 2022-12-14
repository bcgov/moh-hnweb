<template>
  <AppRow>
    <AppCol>
      <h1>General Help with MSP Direct</h1>

      <section aria-labelledby="helpSection1">
        <h2 id="helpSection1">Using the Navigation Menu</h2>
        The navigation bar towards the top of the screen displays categories of available business services.
        <ul>
          <li>Hovering over a category will display a dropdown of the services available in that category.</li>
          <li>Selecting a service from the dropdown will take you to a page where you can then run a transaction associated with that service.</li>
        </ul>
      </section>

      <section aria-labelledby="helpSection2">
        <h2 id="helpSection2">Bulletins</h2>
        Administrators may post bulletins on the Home page to communicate important information regarding the MSP Direct application.
        <ul>
          <li>This page may be updated at any time with a new bulletin. Users are encouraged to regularly review the Home page for new bulletins.</li>
          <li>MSP Direct has several different kinds of users; a bulletin may be relevant to all users or specific user communities.</li>
        </ul>
      </section>

      <section aria-labelledby="helpSection3">
        <h2 id="helpSection3">Support</h2>
        <p>If you have questions about using MSP Direct, contact Health Insurance BC.</p>
        <p>(604) 683-7520 (Lower Mainland)</p>
        <p>1-877-955-5656 (Elsewhere in B.C.)</p>
        <p>
          <a href="mailto:HLTH.HnetConnection@gov.bc.ca?subject=MSP Direct Help">HLTH.HnetConnection@gov.bc.ca</a>
        </p>
      </section>
    </AppCol>
  </AppRow>
</template>

<script>
import AppLabel from '../components/ui/AppLabel.vue'
export default {
  name: 'help',
  components: { AppLabel },
}
</script>
<style scoped>
ul {
  margin-bottom: 20px;
}
li {
  padding-top: 10px;
  margin-left: 20px;
  list-style-type: disc;
}
</style>
