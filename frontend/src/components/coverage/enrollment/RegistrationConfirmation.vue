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
    <AppButton @click="addAnotherPermitHolder()" mode="primary" type="button">Add Another Study Permit Holder</AppButton>
  </div>
</template>

<script>
import { formatPersonName } from '../../../util/utils'
import { useAlertStore } from '../../../stores/alert'

export default {
  name: 'RegistrationConfirmation',
  setup() {
    return {
      alertStore: useAlertStore(),
    }
  },
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
    addAnotherPermitHolder() {
      if (this.$route.query.pageAction === 'REGISTRATION') {
        this.alertStore.dismissAlert()
        this.$router.replace({ query: null })
        this.$router.push('/coverage/enrollment/addStudyPermitHolderWithoutPHN')
      } else {
        this.$router.go()
      }
    },
  },
}
</script>
