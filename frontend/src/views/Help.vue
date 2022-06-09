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
        <h2 id="helpSection3">Contact the Ministry of Health Help Desk</h2>
        <p>The Ministry of Health provides support for MSP Direct Monday to Friday, 8:00AM to 4:30PM, excluding statutory holidays.</p>
        <p>
          For further information or support about MSP Direct business services, please contact:
          <AppLabel>Email</AppLabel><a href="mailto:HLTH.HnetConnection@gov.bc.ca?subject=MSP Direct Help">HLTH.HnetConnection@gov.bc.ca</a>
        </p>
        <p>For information about access and/or authentication, please contact:<AppLabel>Email</AppLabel> <a href="mailto:HLTH.Helpdesk@gov.bc.ca?subject=MSP Direct Help">HLTH.Helpdesk@gov.bc.ca</a></p>
        <p><AppLabel>Local</AppLabel>(250) 952-1234</p>
        <p><AppLabel>Toll Free Canada Wide</AppLabel>1 (888) 764-2323</p>
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
