<template>
  <router-link :class="navTabClass($route.name)" :to="{ name: this.routeName}">{{title}}</router-link>
</template>


<script>
  export default {
    name: "SubNavTab",
    props: {
      routeName: String,
      title: String
    },
    methods: {
      navTabClass(currentRoute) {
        return (currentRoute === this.routeName) ? 'active-tab' : 'inactive-tab';
      }
    }
  }
</script>

<style scoped>

.nav-tabs a {
  color: #ffffff; 
  display: inline-block;
  padding: 0 20px;
  height: 36px;
  font-size: 16px;
  font-weight: bold;
  line-height: 40px;
}
.active-tab,
.active-tab:hover {
    background: #003366;
    margin-right:10px; 
    text-decoration: none;
    border-radius: 8px 8px 0 0;
}
.inactive-tab {
    background: #999999; 
    margin-right:10px;
    text-decoration: none;
    border-radius: 8px 8px 0 0;
}
.inactive-tab:hover {
    background: #666666;
    text-decoration: none;
}
</style>