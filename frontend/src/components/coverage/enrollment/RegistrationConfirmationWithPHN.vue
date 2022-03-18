<template>
  <div>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="resident?.phn" />
      </AppCol>
    </AppRow>
    <AppRow>
      <AppCol>
        <AppOutput label="Name" :value="fullName" />
      </AppCol>
    </AppRow>
    <AppButton mode="primary" @click="goToAddStudyPermitHolderWithPHN()" type="button">Add Another Permit Holder With PHN</AppButton>
  </div>
</template>

<script>
import { formatPersonName } from '../../../util/utils'

export default {
  name: 'RegistrationConfirmationWithPHN',
  props: {
    resident: {
      required: true,
      type: Object,
    },
  },
  computed: {
    fullName() {
      return formatPersonName(this.resident)
    },
  },

  methods: {
    goToAddStudyPermitHolderWithPHN() {
      this.$router.go()
    },
  },
}
</script>
