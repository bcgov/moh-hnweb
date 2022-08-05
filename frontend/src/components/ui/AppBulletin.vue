<template>
  <div class="app-bulletin">
    <AppRow>
      <AppCol class="col1 bulletin-icon"><font-awesome-icon icon="bullhorn" size="2x" /></AppCol>
      <AppCol class="col9">
        <h3>Announcement</h3>
        <p v-html="content"></p>
      </AppCol>
    </AppRow>
  </div>
</template>

<script>
export default {
  name: 'AppButton',
  props: {
    content: {
      type: String,
      required: true,
    },
  },
}
</script>
<style>
.app-bulletin {
  background-color: #38598a;
  border-radius: 4px;
  color: #ffffff;
  margin: 5px 0 10px 0;
  padding: 5px;
}

.app-bulletin a {
  color: #ffffff;
}

.app-bulletin h3 {
  color: #ffffff;
}

.app-bulletin p {
  color: #ffffff;
  margin-bottom: 0px;
}

.app-bulletin p p {
  margin-bottom: 10px;
}

.app-bulletin p ul {
  list-style-type: disc;
  margin: 0px 0px 10px 40px
}

.bulletin-icon {
  padding: 5px;
}
</style>
