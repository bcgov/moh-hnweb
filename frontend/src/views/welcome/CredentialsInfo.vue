<template>
  <section>
    <p>Please read the information in the table below to understand your profile and the type of ID that you can use to access MSP Direct web application.</p>
    <AppSimpleTable>
      <thead>
        <tr>
          <th>Your Profile</th>
          <th>Supported ID</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Health Authorities and health care providers or organizations who deliver health services are able to search for clients' Personal Health Numbers (PHNs), confirm client eligibility for publicly funded health care, update person demographics, and assign PHNs, where required.</td>
          <td>
            <ul>
              <li>Health Authority ID</li>
              <li>IDIR</li>
            </ul>
          </td>
        </tr>
        <tr>
          <td>IDIR Individuals who work for the B.C. government. Contractors and others, who work for BC Government but who aren't employees.</td>
          <td>
            <ul>
              <li>IDIR</li>
            </ul>
          </td>
        </tr>
        <tr>
          <td>B.C. residents who are enrolled in the Medical Services Plan (MSP) to get provincial health insurance.</td>
          <td>
            <ul>
              <li>BC Services Card (BCSC)</li>
            </ul>
          </td>
        </tr>
        <tr>
          <td>Individual representing a business or organization, registered in BC.</td>
          <td>
            <ul>
              <li>Business BCeID</li>
            </ul>
          </td>
        </tr>
      </tbody>
    </AppSimpleTable>
  </section>
  <section>
    <h2>Get Credentials and Find Support</h2>
    <p>Please contact the issuing agency to request access credentials.</p>
    <h3>Business BCeID</h3>
    <ul>
      <li>BCeID account provides secure access to online provincial government services.</li>
      <li>
        Your organization may already have an existing business account. If not, an administrator in your organization will need to create an account and set up users:
        <a href="https://www.bceid.ca/register/business/getting_started/getting_started.aspx">https://www.bceid.ca/register/business/getting_started/getting_started.aspx</a>
      </li>
      <li>Business BCeID Support: <a href="https://www.bceid.ca/aboutbceid/contact_us.aspx" target="_blank">https://www.bceid.ca/aboutbceid/contact_us.aspx</a></li>
    </ul>
    <br />
    <h3>Health Authority credentials</h3>
    <ul>
      <li>All users within Health Authorities will use their existing Active Directory credentials.</li>
      <li>When accessing the new MSP Direct, users should be granted access immediately.</li>
      <li>Support requests should be sent to the existing internal help desk contacts.</li>
    </ul>
    <br />
    <h3>IDIR</h3>
    <ul>
      <li>Some government users may log in using existing IDIR credentials. For assistance with IDIR credentials, please contact existing internal government help desk contacts.</li>
    </ul>
    <br />
    <h3>BC Services Card</h3>
    <ul>
      <li>Learn how to use the BC Services Card app to access online services: <a href="https://www2.gov.bc.ca/gov/content/governments/government-id/bc-services-card/log-in-with-card">https://www2.gov.bc.ca/gov/content/governments/government-id/bc-services-card/log-in-with-card</a></li>
      <li>Contact the BC Services Card Help Desk for support: <a href="https://www2.gov.bc.ca/gov/content/governments/government-id/bcservicescardapp/help">https://www2.gov.bc.ca/gov/content/governments/government-id/bcservicescardapp/help</a></li>
    </ul>
  </section>
</template>

<script>
import AppSimpleTable from '../../components/ui/AppSimpleTable.vue'
export default {
  name: 'CredentialsInfo',
  components: {
    AppSimpleTable,
  },
}
</script>

<style scoped>
ul {
  list-style: circle;
  margin-left: 20px;
  padding-bottom: 10px;
}
</style>
