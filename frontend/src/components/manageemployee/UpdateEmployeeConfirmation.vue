<template>
  <div>
    <AppRow>
      <AppCol class="col3">
        <AppOutput label="PHN" :value="resident?.phn" />
      </AppCol>
    </AppRow> 
  </div>
</template>

<script>
export default {
  name: 'UpdateEmployeeConfirmation',
  props: {
    resident: {
      phn: '',
    },
  },  
}
</script>