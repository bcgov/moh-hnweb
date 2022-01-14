<script setup>
import TheSubNav from '../../components/template/TheSubNav.vue'
import SubNavTab from '../../components/template/SubNavTab.vue'
</script>

<template>
  <TheSubNav>
    <SubNavTab routeName="UpdateNumberAndDept" title="Update Group Member's Number and/or Department" />    
  </TheSubNav>
  <router-view />
</template>

<script>
export default {
  name: 'GroupMemberHome',
}
</script>


