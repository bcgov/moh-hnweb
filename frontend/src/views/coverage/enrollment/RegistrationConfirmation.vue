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
  </div>
</template>

<script>
import AppCol from '../../../components/grid/AppCol.vue'
import AppRow from '../../../components/grid/AppRow.vue'
import AppOutput from '../../../components/AppOutput.vue'
import { formatPersonName } from '../../../util/utils'

export default {
  name: 'RegistrationConfirmation',
  props: {
    resident: {
      phn: '',
    },
  },
  components: {
    AppCol,
    AppOutput,
    AppRow,
  },
  computed: {
    fullName() {
      return formatPersonName(this.resident)
    },
  },
}
</script>
