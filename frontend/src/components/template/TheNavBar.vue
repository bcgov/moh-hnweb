<template>
  <nav role="navigation">
    <div class="container">
      <ul>
        <li id="home-link" :class="($route.name == 'Home') ? 'active' : 'inactive'">
            <router-link :to="{ name: 'Home'}">Home</router-link>
        </li>
        <li id="eligibility-link" :class="$route.path.startsWith('/eligibility') ? 'active' : 'inactive'">
            <router-link :to="{ name: 'Eligibility'}">Eligibility</router-link>
        </li>
        <li id="coverage-link" :class="$route.name == 'Coverage' ? 'active' : 'inactive'">
            <router-link :to="{ name: 'Coverage'}">Coverage</router-link>
        </li>
        <li id="employees-link" :class="$route.name == 'Help' ? 'active' : 'inactive'">
            <router-link :to="{ name: 'Help'}">Help</router-link>
        </li>
        <li id="employees-link" :class="$route.name == 'Employees' ? 'active' : 'inactive'">
            <router-link :to="{ name: 'Employees'}">Employees</router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'TheNavBar',
  methods: {
    // resetAlert: function () {
    //   this.$store.commit("alert/dismissAlert");
    // }
  }
}
</script>

<style scoped>
/* Main Navigation */
nav {
    height: 40px;
    background-color: #38598A;
}
nav .container {
    max-width: 1320px;
    min-width: 1100px;
    margin: 0 auto;
    padding: 0 60px;
}
nav .container ul {
  padding: 0;
}
nav .container ul li {
    color: #FFFFFF;
    display: inline-block;
    height: 40px;
    line-height: 40px;
    vertical-align: top;
    font-weight: bold;
}
nav .container ul li.active {
    background-color: #496DA2;
}
nav .container ul li:first-child {
    margin-left: -20px;
}
nav .container ul li:hover {
    cursor: pointer;
    background-color: #496DA2;
}
nav .container ul li a {
    display: block;
    font-size: 1rem;
    line-height: 40px;
    color: #FFFFFF;
    text-decoration: none;
    height: 40px;
    padding: 0 20px;
}
nav .container ul li a:focus {
    background-color: #496DA2;
}
</style>