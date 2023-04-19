<template>
  <AppSimpleTable id="addressTable">
    <thead>
      <tr>
        <th>Use</th>
        <th>Line 1</th>
        <th>Line 2</th>
        <th>Line 3</th>
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
        addressLine2: this.result.homeAddressLine2,
        addressLine3: this.result.homeAddressLine3,
        addressLine4: this.result.homeAddressLine4,
        postalCode: this.result.homeAddressPostalCode,
        telephone: this.result.telephone,
      }
    },
    mailingAddress() {
      return {
        title: 'Mailing Address',
        addressLine1: this.result.mailingAddressLine1,
        addressLine2: this.result.mailingAddressLine2,
        addressLine3: this.result.mailingAddressLine3,
        addressLine4: this.result.mailingAddressLine4,
        postalCode: this.result.mailingAddressPostalCode,
        telephone: this.result.telephone,
      }
    },
  },
}
</script>
