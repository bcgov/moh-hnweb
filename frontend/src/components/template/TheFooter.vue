<template>
  <footer class="footer">
    <div class="container">
      <div>
        <ul>
          <li v-if="authenticated">
            <router-link :to="{ name: 'Home' }">Home</router-link>
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
      <div class="version">Version: {{ version }}</div>
    </div>
  </footer>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'TheFooter',
  data() {
    return {
      version: import.meta.env.VITE_APP_VERSION,
    }
  },
  computed: {
    authenticated() {
      return this.$keycloak.authenticated
    },
  },
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
  display: flex;
  justify-content: space-between;
  height: 46px;
  padding: 10px 60px;
  max-width: 1320px;
  min-width: 1100px;
  margin: 0 auto;
}

footer .version {
  font-size: 0.813em;
  align-self: center;
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
  font-weight: normal; /* 400 */
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
  outline: 4px solid #3b99fc;
  outline-offset: 1px;
}
</style>
