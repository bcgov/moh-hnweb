<template>
  <h1>Service Permissions not assigned</h1>
  <p>Your log-in credentials are valid but you don't have Permissions to view or run transactions in MSP Direct. Please follow the instructions below to get permissions.</p>
  <p>If you have previously accessed HN Web using a HealthNetBC ID you can transfer the permissions associated with that HN Web account to your new MSP Direct account using the Transfer button below.</p>
  <AppButton class="btn-lg">
    <a :href="userTransferURL">Transfer</a>
  </AppButton>
  <br />
  <p>If you are a new user and want to request access to MSP Direct, please contact your organization's Access Administrator.</p>
  <AppButton class="btn-lg">
    <a href="https://www2.gov.bc.ca/gov/content/health/practitioner-professional-resources/system-access/msp-direct" target="_blank">More Info</a>
  </AppButton>
</template>
<script>
export default {
  name: 'Unauthorized',
  data() {
    return {
      userTransferURL: config.USER_TRANSFER_URL || import.meta.env.VITE_USER_TRANSFER_URL,
    }
  },
}
</script>
