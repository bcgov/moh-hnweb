<template>
  <AppSimpleTable id="addressTable">
    <thead>
      <tr>
        <th>Use</th>
        <th>Line 1</th>
        <th>Line 2</th>
        <th>City/Province</th>
        <th>Postal Code</th>
        <th>Telephone</th>
      </tr>
    </thead>
    <tbody>
      <BeneficiaryAddress :address="homeAddress" />
      <BeneficiaryAddress :address="mailingAddress" />
    </tbody>
  </AppSimpleTable>
</template>
<script>
import BeneficiaryAddress from './BeneficiaryAddress.vue'
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
export default {
  components: {
    BeneficiaryAddress,
    AppSimpleTable,
  },
  props: {
    result: Object,
  },
  computed: {
    homeAddress() {
      return {
        title: 'Home Address',
        addressLine1: this.result.homeAddressLine1,
        addressLine2: this.homeAddressLine2,
        cityProvince: this.homeCityProvince,
        postalCode: this.result.homeAddressPostalCode,
        telephone: this.result.telephone,
      }
    },
    mailingAddress() {
      return {
        title: 'Mailing Address',
        addressLine1: this.result.mailingAddressLine1,
        addressLine2: this.mailingAddressLine2,
        cityProvince: this.mailingCityProvince,
        postalCode: this.result.mailingAddressPostalCode,
        telephone: this.result.telephone,
      }
    },
    homeAddressLine2() {
      console.log(this.result.homeAddressLine3 && this.result.homeAddressLine2)
      return this.result.homeAddressLine3 && this.result.homeAddressLine2 ? this.result.homeAddressLine2 : ''
    },
    mailingAddressLine2() {
      return this.result.mailingAddressLine3 && this.result.mailingAddressLine2 ? this.result.mailingAddressLine3 : ''
    },
    homeCityProvince() {
      let cityProvince = ''
      if (this.result.homeAddressLine3) {
        cityProvince = this.result.homeAddressLine3
      } else if (this.result.homeAddressLine2) {
        cityProvince = this.result.homeAddressLine2
      }
      return cityProvince
    },
    mailingCityProvince() {
      let cityProvince = ''
      if (this.result.mailingAddressLine3) {
        cityProvince = this.result.mailingAddressLine3
      } else if (this.result.mailingAddressLine2) {
        cityProvince = this.result.mailingAddressLine2
        this.result.mailingAddressLine2 = ''
      }
      return cityProvince
    },
  },
}
</script>
