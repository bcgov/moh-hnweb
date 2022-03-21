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
    <AppButton @click="goToAddAnotherPermitHolder()" mode="primary" type="button">Add Another Permit Holder</AppButton>
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
    goToAddAnotherPermitHolder() {
      if (this.$route.query.pageAction === 'REGISTRATION') {
        this.$router.replace({ query: null })
        this.$router.push('/coverage/enrollment/addStudyPermitHolderWithoutPHN')
      } else {
        this.$router.go()
      }
    },
  },
}
</script>
