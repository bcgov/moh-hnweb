<template>
  <nav role="navigation">
    <div class="container">
      <ul>
        <li id="welcome-link" :class="menuTabClass($route, '/welcome')" v-if="!authenticated">
          <router-link @click="resetAlert" :to="{ name: 'Login' }">Welcome</router-link>
        </li>
        <li id="home-link" :class="tabClass($route, 'Home')" v-if="authenticated">
          <router-link @click="resetAlert" :to="{ name: 'Home' }">Home</router-link>
        </li>
        <li id="eligibility-link" :class="menuTabClass($route, '/eligibility')" v-if="hasEligibilityPermission()">
          <div class="dropdown">
            <span>Eligibility & PHN</span>
            <div class="dropdown-content">
              <router-link @click="resetAlert" :class="menuClass($route, 'CheckEligibility')" :to="{ name: 'CheckEligibility' }" v-if="hasPermission('CheckEligibility')">Check Eligibility</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'PhnInquiry')" :to="{ name: 'PhnInquiry' }" v-if="hasPermission('PHNInquiry')">PHN Inquiry</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'PhnLookup')" :to="{ name: 'PhnLookup' }" v-if="hasPermission('PHNLookup')">PHN Lookup</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'CoverageStatusCheck')" :to="{ name: 'CoverageStatusCheck' }" v-if="hasPermission('MSPCoverageCheck')">MSP Coverage Status Check</router-link>
            </div>
          </div>
        </li>
        <li id="coverage-maintenance-link" :class="tabClass($route, 'CoverageMaintenance')" v-if="hasMaintenancePermission()">
          <div class="dropdown">
            <span>Coverage Maintenance</span>
            <div class="dropdown-content">
              <router-link @click="changeEffectiveDate" :class="menuClass($route, 'ChangeEffectiveDate')" :to="{ name: 'ChangeEffectiveDate' }" v-if="hasPermission('ChangeEffectiveDate')">Change Effective Date</router-link>
            </div>
          </div>
        </li>
        <li id="coverage-enrollment-link" :class="menuTabClass($route, '/coverage/enrollment')" v-if="hasEnrollmentPermission()">
          <div class="dropdown">
            <span>Coverage Enrollment</span>
            <div class="dropdown-content">
              <router-link @click="resetCoverageEnrollment" :class="menuClass($route, 'AddVisaResidentWithoutPHN')" :to="{ name: 'AddVisaResidentWithoutPHN' }" v-if="hasPermission('AddPermitHolderWOPHN')">Add Permit Holder without PHN</router-link>
              <router-link @click="resetCoverageEnrollment" :class="menuClass($route, 'AddVisaResidentWithPHN')" :to="{ name: 'AddVisaResidentWithPHN' }" v-if="hasPermission('AddPermitHolderWithPHN')">Add Permit Holder with PHN</router-link>
            </div>
          </div>
        </li>
        <li id="group-member-link" :class="menuTabClass($route, '/groupMember')" v-if="hasGroupMemberPermission()">
          <div class="dropdown">
            <span>Manage Group Member</span>
            <div class="dropdown-content">
              <router-link @click="resetAlert" :class="menuClass($route, 'AddGroupMember')" :to="{ name: 'AddGroupMember' }">Add Group Member</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'AddDependent')" :to="{ name: 'AddDependent' }">Add Dependent</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'UpdateNumberAndDept')" :to="{ name: 'UpdateNumberAndDept' }">Update Number and/or Department</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'CancelGroupMember')" :to="{ name: 'CancelGroupMember' }">Cancel Group Member</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'CancelDependent')" :to="{ name: 'CancelDependent' }">Cancel Dependent</router-link>
            </div>
          </div>
        </li>
        <li id="msp-contracts-link" :class="menuTabClass($route, '/mspContracts')" v-if="hasMSPContractsPermission()">
          <div class="dropdown">
            <span>MSP Contracts</span>
            <div class="dropdown-content">
              <router-link @click="resetAlert" :class="menuClass($route, 'GetContractPeriods')" :to="{ name: 'GetContractPeriods' }">Get Contract Periods</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'ContractInquiry')" :to="{ name: 'ContractInquiry' }">Contract Inquiry</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'GetGroupMembersContractAddress')" :to="{ name: 'GetGroupMembersContractAddress' }" v-if="hasPermission('GetContractAddress')">Get Contract Address</router-link>
              <router-link @click="resetAlert" :class="menuClass($route, 'UpdateContractAddress')" :to="{ name: 'UpdateContractAddress' }" v-if="hasPermission('UpdateContractAddress')">Update Contract Address</router-link>
            </div>
          </div>
        </li>
        <li id="help-link" :class="tabClass($route, 'Help')" v-if="authenticated">
          <router-link @click="resetAlert" :to="{ name: 'Help' }">Help</router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import { useAlertStore } from '../../stores/alert'
import { useAuthStore } from '../../stores/auth'
import { useStudyPermitHolderStore } from '../../stores/studyPermitHolder'

export default {
  name: 'TheNavBar',
  setup() {
    return { alertStore: useAlertStore(), authStore: useAuthStore(), studyPermitHolderStore: useStudyPermitHolderStore() }
  },
  computed: {
    authenticated() {
      return this.$keycloak.authenticated
    },
  },
  methods: {
    resetCoverageEnrollment() {
      this.alertStore.dismissAlert()
      this.studyPermitHolderStore.$reset()
    },
    resetAlert() {
      this.alertStore.dismissAlert()
    },
    menuClass(route, routeName) {
      return this.tabClass(route, routeName)
    },
    tabClass(route, routeName) {
      return route.name === routeName ? 'active' : 'inactive'
    },
    menuTabClass(route, routePath) {
      return route.path.startsWith(routePath) ? 'active' : 'inactive'
    },
    hasPermission(permission) {
      return this.authStore.hasPermission(permission)
    },
    hasEligibilityPermission() {
      return this.hasPermission('MSPCoverageCheck') || this.hasPermission('CheckEligibility') || this.hasPermission('PHNInquiry') || this.hasPermission('PHNLookup')
    },
    hasEnrollmentPermission() {
      return this.hasPermission('AddPermitHolderWOPHN') || this.hasPermission('AddPermitHolderWithPHN')
    },
    hasGroupMemberPermission() {
      return this.hasPermission('AddGroupMember') || this.hasPermission('AddDependent') || this.hasPermission('UpdateNumberAndDept') || this.hasPermission('CancelGroupMember') || this.hasPermission('CancelDependent')
    },
    hasMaintenancePermission() {
      return this.hasPermission('ChangeEffectiveDate')
    },
    hasMSPContractsPermission() {
      return this.hasPermission('GetContractPeriods') || this.hasPermission('ContractInquiry') || this.hasPermission('UpdateContractAddress')
    },
  },
}
</script>

<style scoped>
/* Main Navigation */
nav {
  height: 40px;
  background-color: #38598a;
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
  color: #ffffff;
  display: inline-block;
  height: 40px;
  line-height: 40px;
  vertical-align: top;
  font-weight: bold;
}
nav .container ul li.active {
  background-color: #496da2;
}
nav .container ul li:first-child {
  margin-left: -20px;
}
nav .container ul li:hover {
  cursor: pointer;
  background-color: #496da2;
}
nav .container ul li a {
  display: block;
  font-size: 1rem;
  line-height: 40px;
  color: #ffffff;
  text-decoration: none;
  height: 40px;
  padding: 0 20px;
}

nav .container ul li span {
  display: block;
  font-size: 1rem;
  line-height: 40px;
  color: #ffffff;
  text-decoration: none;
  height: 40px;
  padding: 0 20px;
  cursor: auto;
}
nav .container ul li a:focus {
  background-color: #496da2;
}

/* The container <div> - needed to position the dropdown content */
.dropdown {
  position: relative;
  display: inline-block;
}

/* Dropdown Content (Hidden by Default) */
.dropdown-content {
  background-clip: padding-box;
  display: none;
  position: absolute;
  background-color: #496da2;
  min-width: 200px;
  box-shadow: 0px 6px 6px 0px rgba(0, 0, 0, 0.4);
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
  display: table;
}

.dropdown-content a:target {
  display: none;
}
</style>
