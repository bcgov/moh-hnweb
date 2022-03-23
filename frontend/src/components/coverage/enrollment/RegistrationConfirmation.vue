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
    <AppButton @click="addAnotherPermitHolder()" mode="primary" type="button">{{ addAnotherButtonText }}</AppButton>
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
    addAnotherButtonText() {
      return this.$route.query.pageAction === 'REGISTRATION' || this.$route.name === 'AddVisaResidentWithoutPHN' ? 'Add Another Permit Holder without PHN' : 'Add Another Permit Holder with PHN'
    },
  },

  methods: {
    addAnotherPermitHolder() {
      if (this.$route.query.pageAction === 'REGISTRATION') {
        this.$store.commit('alert/dismissAlert')
        this.$router.replace({ query: null })
        this.$router.push('/coverage/enrollment/addStudyPermitHolderWithoutPHN')
      } else {
        this.$router.go()
      }
    },
  },
}
</script>
