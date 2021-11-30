<template>
  <router-link @click="resetAlert" :to="{ name: this.routeName}">{{title}}</router-link>
</template>

<script>
  export default {
    name: "SubNavTab",
    props: {
      routeName: String,
      title: String
    },
    methods: {
      resetAlert() {
       this.$store.commit('alert/dismissAlert');
      }
    }
  }
</script>

<style scoped>

a {
  background: #999999; 
  border-radius: 8px 8px 0 0;
  color: #ffffff; 
  display: inline-block;
  padding: 0 20px;
  height: 36px;
  font-size: 16px;
  font-weight: bold;
  line-height: 40px;
  margin-right: 10px;
  text-decoration: none;
}
a:hover {
    background: #666666;
}
.router-link-exact-active,
.router-link-exact-active:hover {
    background: #003366;
}

</style>