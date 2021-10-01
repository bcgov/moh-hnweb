<template>
  <footer class="primary">
    <div class="container">
      <div class="row">
        <div class="col col3">
          <ul>
            <li>
              <a href="https://www2.gov.bc.ca/gov/content/home/get-help-with-government-services" target="_blank">Contact Us</a>
            </li>
          </ul>
        </div>
        <div class="col col3">
          <ul>
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
          </ul>
        </div>
      </div>
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
  height: 160px;
  background-color: #003366;
}
footer .container {
  max-width: 1320px;
  min-width: 1100px;
  margin: 0 auto;
  padding: 20px 60px 0px 60px;
}

footer .primary {
  background-color: #003366 !important;
  border-color: #003366 !important;
}
footer li {
  margin-bottom: 10px;
}
footer li,
footer li a {
  font-size: 0.8125rem;
  color: #ffffff;
}
footer li a {
  text-decoration: none;
}
footer li a:hover,
footer li a:focus {
  text-decoration: underline;
}
footer ul {
  padding: 0px;
}

.row {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  width: 100%;
}

.col {
  display: flex;
  flex-direction: column;
  flex-basis: 100%;
  flex: 1;
}

.col3 {
  flex: 0 0 25%;
  max-width: 25%;
}

</style>