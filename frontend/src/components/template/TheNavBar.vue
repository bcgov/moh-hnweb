<template>
  <nav role="navigation">
    <div class="container">
      <ul>
        <li id="home-link" :class="($route.name == 'Home') ? 'active' : 'inactive'">
          <router-link @click="resetAlert" :to="{ name: 'Home'}">Home</router-link>
        </li>
        <li id="eligibility-link" :class="$route.path.startsWith('/eligibility') ? 'active' : 'inactive'">
          <div class="dropdown" :key="eligibilityDropdownKey" v-on:click="this.refreshEligibility()">
            <router-link @click="resetAlert" :to="{ name: 'Eligibility'}">Eligibility & PHN</router-link>
            <div class="dropdown-content">
              <router-link @click="resetAlert" :class="($route.name == 'CheckEligibility') ? 'active' : 'inactive'" :to="{ name: 'CheckEligibility'}">Check Eligibility</router-link>
              <router-link @click="resetAlert" :class="($route.name == 'PhnEnquiry') ? 'active' : 'inactive'" :to="{ name: 'PhnEnquiry'}">PHN Enquiry</router-link>
              <router-link @click="resetAlert" :class="($route.name == 'CoverageStatusCheck') ? 'active' : 'inactive'" :to="{ name: 'CoverageStatusCheck'}">MSP Coverage Status Check</router-link>
            </div>
          </div>
        </li>
        <li id="coverage-maintenance-link" :class="$route.name == 'CoverageMaintenance' ? 'active' : 'inactive'">
          <router-link @click="resetAlert" :to="{ name: 'CoverageMaintenance'}">Coverage Maintenance</router-link>
        </li>
        <li id="coverage-enrollment-link" :class="$route.name == 'CoverageEnrollment' ? 'active' : 'inactive'">
          <router-link @click="resetAlert" :to="{ name: 'CoverageEnrollment'}">Coverage Enrollment</router-link>
        </li>
        <li id="manage-employees-link" :class="$route.name == 'ManageEmployees' ? 'active' : 'inactive'">
          <router-link @click="resetAlert" :to="{ name: 'ManageEmployees'}">Manage Employees</router-link>
        </li>
        <li id="help-link" :class="$route.name == 'Help' ? 'active' : 'inactive'">
            <router-link @click="resetAlert" :to="{ name: 'Help'}">Help</router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'TheNavBar',
  data: function() {
    return {
      eligibilityDropdownKey: 0
    }
  },

  methods: {
    resetAlert: function () {
      this.$store.commit('alert/dismissAlert');
    },
    refreshEligibility() {
      this.eligibilityDropdownKey += 1
    }
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

/* The container <div> - needed to position the dropdown content */
.dropdown {
  position: relative;
  display: inline-block;
}

/* Dropdown Content (Hidden by Default) */
.dropdown-content {
  background-clip:  padding-box;
  display: none;
  position: absolute;
  background-color: #496DA2;
  min-width: 200px;
  box-shadow: 0px 6px 6px 0px rgba(0,0,0,0.4);
  z-index: 1;
  width: fit-content;
  white-space: nowrap;
}

/* Links inside the dropdown */
nav .container ul li .dropdown-content a {
  font-size: 14px;
  font-weight: normal;
}


nav .container ul li .dropdown-content a.active {
  font-weight: bold;
}

/* Change color of dropdown links on hover */
.dropdown-content a:hover {
  color: #ffffff;
  background-color: #6583b0;
}

/* Show the dropdown menu on hover */
.dropdown:hover .dropdown-content {
  display: table ;
}

.dropdown-content a:target {
  display: none;
}

</style>