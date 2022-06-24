<template>
  <header>
    <div id="header">
      <section class="container">
        <section class="identity">
          <img src="../../assets/images/logo.png" width="154" class="logo" alt="BC Government Logo" />
          <div class="site-container">
            <div class="sitename">{{ title }}</div>
          </div>
        </section>
        <section class="options user-select-off">
          <a id="logoutLink" class="sign-out" v-on:click="logout" v-if="authenticated">Sign Out</a>
        </section>
      </section>
    </div>
  </header>
</template>

<script>
export default {
  name: 'TheHeader',
  data() {
    return {
      title: config.APP_TITLE || import.meta.env.VITE_APP_TITLE,
    }
  },
  computed: {
    authenticated() {
      return this.$keycloak.authenticated
    },
  },
  methods: {
    logout() {
      let logoutUri = ''
      const redirectUri = location.origin + this.$router.resolve({ name: 'Login' }).path
      const siteMinderLogoutUri = config.SITE_MINDER_LOGOUT_URI || import.meta.env.VITE_SITE_MINDER_LOGOUT_URI
      const phsaLogoutUri = config.PHSA_LOGOUT_URI || import.meta.env.VITE_PHSA_LOGOUT_URI
      const idp = this.$keycloak.tokenParsed.identity_provider

      if (confirm('Please confirm you want to sign out. ' + '\nThis will also end all other active Keycloak or SiteMinder sessions you have open.')) {
        switch (idp) {
          case 'idir':
          case 'bceid_business':
            /**
             * Currently Keycloak does not support logging out of SiteMinder IDP's automatically so
             * we set the Keycloak Logout redirect_uri= parameter to be the SiteMinder logout and we
             * set the SiteMinder returl= parameter to be application which chains both logouts for
             * full Single Sign Out. https://github.com/bcgov/ocp-sso/issues/4
             */
            logoutUri = siteMinderLogoutUri + '?retnow=1&returl=' + redirectUri
            break
          case 'moh_idp':
            logoutUri = redirectUri
            break
          case 'phsa':
            logoutUri = phsaLogoutUri
            break
          default:
            logoutUri = redirectUri
        }

        this.$keycloak.logout({
          redirectUri: logoutUri,
        })
      }
    },
  },
}
</script>

<style scoped>
header {
  height: 80px;
  background-color: #003366;
  border-bottom: 2px solid #fcba19;
}
header .container {
  max-width: 1320px;
  min-width: 1100px;
  height: 80px;
  padding: 10px 60px;
  margin: 0 auto;
}
header .container .identity {
  float: left;
  height: 60px;
}
header .container .identity .logo {
  display: inline-block;
  margin: 10px 30px 0 0;
}
header .container .identity .sitename {
  display: inline-block;
  vertical-align: top;
  height: 60px;
  margin: 0;
  font-size: 1.375rem;
  line-height: 59px;
  font-weight: bold;
  color: #ffffff;
}
header .container .options {
  float: right;
  height: 60px;
  padding-top: 10px;
}
header .container .options .sign-out {
  display: inline-block;
  height: 40px;
  padding: 0 20px;
  border: 2px solid #ffffff;
  border-radius: 4px;
  text-decoration: none;
  font-size: 1rem;
  line-height: 2.25rem;
  font-weight: bold;
  color: #ffffff;
  cursor: pointer;
}
header .container .options .sign-out:focus {
  box-shadow: 0 0 3px #ffffff;
}

header .site-container {
  display: inline-block;
  vertical-align: top;
}
</style>
