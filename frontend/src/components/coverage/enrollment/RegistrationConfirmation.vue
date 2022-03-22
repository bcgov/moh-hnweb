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
    <AppButton v-if="this.$route.query.pageAction === 'REGISTRATION' || this.$route.name === 'AddVisaResidentWithoutPHN'" @click="addAnotherPermitHolderWithoutPHN()" mode="primary" type="button">Add Another Permit Holder without PHN</AppButton>
    <AppButton v-else @click="addAnotherPermitHolderWithPHN()" mode="primary" type="button">Add Another Permit Holder with PHN</AppButton>
  </div>
</template>

<script>
import { formatPersonName } from '../../../util/utils'

export default {
  name: 'RegistrationConfirmation',
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
    addAnotherPermitHolderWithoutPHN() {
      if (this.$route.query.pageAction === 'REGISTRATION') {
        this.$store.commit('alert/dismissAlert')
        this.$router.replace({ query: null })
        this.$router.push('/coverage/enrollment/addStudyPermitHolderWithoutPHN')
      } else this.$router.go()
    },

    addAnotherPermitHolderWithPHN() {
      this.$router.go()
    },
  },
}
</script>
