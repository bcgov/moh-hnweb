<script setup>
  import AppRow from '../grid/AppRow.vue'
  import AppCol from '../grid/AppCol.vue'
</script>
<template>
  <footer class="footer">
    <div class="container">
    <ul>
      <li>
        <router-link :to="{ name: 'Home'}">Home</router-link>
      </li>
      <li>
        <a href="http://www2.gov.bc.ca/gov/content/home/disclaimer" target="_blank">Disclaimer</a>
      </li>
      <li>
        <a href="http://www2.gov.bc.ca/gov/content/home/privacy" target="_blank">Privacy</a>
      </li>
      <li>
        <a href="http://www2.gov.bc.ca/gov/content/home/accessibility" target="_blank">Accessibility</a>
      </li>
      <li>
        <a href="http://www2.gov.bc.ca/gov/content/home/copyright" target="_blank">Copyright</a>
      </li>
      <li>
        <a href="https://www2.gov.bc.ca/gov/content/home/get-help-with-government-services" target="_blank">Contact Us</a>
      </li>
    </ul>
    </div>
  </footer>
</template>

<script>
  export default {
    name: "TheFooter"
  }
</script>

<style scoped>
footer {
  background-color: #036;
  border-top: 2px solid #fcba19;
  color: #fff;
  font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif; 
}

footer .container {
  height: 46px;
  padding: 10px 60px;
  max-width: 1320px;
  min-width: 1100px;
  margin: 0 auto;
}

footer ul {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  margin: 0;
  color: #fff;
  list-style: none;
  align-items: center;
  height: 100%;
}

footer ul li a {
  font-size: 0.813em;
  font-weight: normal;  /* 400 */
  color: #fff;
  border-right: 1px solid #4b5e7e;
  padding-left: 5px;
  padding-right: 5px;
  text-decoration: none;
}

a:hover {
  color: #fff;
  text-decoration: underline;
}

:focus {
  outline: 4px solid #3B99FC;
  outline-offset: 1px;
}
</style>