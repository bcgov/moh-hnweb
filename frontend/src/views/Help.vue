<template>
  <AppRow>
    <AppCol class="col6">
      <h1>General Help with MSP Direct</h1>

      <h2>Using the Navigation Menu</h2>

      The navigation bar towards the top of the screen displays categories of available business services.
      <ul>
        <li>Hovering over a category will display a dropdown of the services available in that category.</li>
        <li>Selecting a service from the dropdown will take you to a page where you can then run a transaction associated with that service.</li>
      </ul>

      <h2>Bulletins</h2>
      Administrators may post bulletins on the Home page to communicate important information regarding the MSP Direct application.
      <ul>
        <li>This page may be updated at any time with a new bulletin. Users are encouraged
          to regularly review the Home page for new bulletins.</li>
        <li>MSP Direct has several different kinds of users; a bulletin may be relevant to all users or specific user communities.</li>
      </ul>

      <h2>Contact the MSP Direct Helpdesk</h2>
      <p>
        The <span class="fix-me">XXXXXX</span> Help Desk provides support for MSP direct Monday to Friday, 8:00AM to 4:30PM, excluding statutory holidays. <br />
        <br />Email: <span class="fix-me">XXXXXX</span>
        <br />Phone: <span class="fix-me">XXXXXX</span>
        <br />Toll free: <span class="fix-me">XXXXXX</span>
      </p>
    </AppCol>
  </AppRow>
</template>

<script>
    export default {
        name: 'help'
    }
</script>
<style scoped>
.fix-me {
  background-color: yellow;
}
ul {
  margin-bottom: 20px;
}
li {
  padding-top: 10px;
  margin-left: 20px;
  list-style-type: disc;
}
</style>