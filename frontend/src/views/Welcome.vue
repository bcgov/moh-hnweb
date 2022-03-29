<template>
  <AppRow>
    <AppCol class="col3">
      <div class="links">
        <ul>
          <li><a href="#">What is new MSP Direct?</a></li>
          <li><a href="#">What are new log-in Credentials</a></li>
          <li><a href="#" target="_blank">Transactions in MSP Direct?</a></li>
          <li><a href="#" target="_blank">Get BC Services Card or BCeID</a></li>
        </ul>
      </div>
    </AppCol>
    <AppCol>
      <section>
        <p>Welcome to the new-and-improved MSP Direct website. MSP Direct is an online business service, authorized by the Ministry of Health, that allows group plan administrators to do a number of adjustments to their group members account.</p>
        <p>Please log-in to the MSP Direct using one of the IDs:</p>
        <AppButton class="btn-xxl"> <a href="https://common-logon-dev.hlth.gov.bc.ca/auth/realms/moh_applications/broker/phsa/login?client_id=MSPDIRECT-WEB">Health Authority ID</a></AppButton>
        <AppButton class="btn-xxl">IDIR</AppButton>
      </section>
    </AppCol>
  </AppRow>
</template>

<script>
export default {
  name: 'welcome',
}
</script>

<style scoped>
.links {
  background-color: #ffffff;
  border: 2px solid #38598a;
  border-radius: 4px;
  margin: 5px 0 5px 0;
  padding: 5px;
}

li {
  margin-bottom: 10px;
}
</style>
