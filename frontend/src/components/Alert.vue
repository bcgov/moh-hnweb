<script setup>
  import closeUrl from '../assets/images/alert-close.png'
  import errorUrl from '../assets/images/alert-error.png'
  import infoUrl from '../assets/images/alert-info.png'
  import successUrl from '../assets/images/alert-success.png'
  import warningUrl from '../assets/images/alert-warning.png'
</script>

<template>
  <div :class="`alert-container alert-${type}`" v-if="active">
    <div class="alert-icon">
      <img :src="image"/>
    </div>
    <div class="alert-message">{{message}}</div>
    <div class="alert-close">
      <a @click="close()">
        <img :src="closeUrl"/>
      </a>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
export default {
  name: 'Alert',
  computed: {
    ...mapState('alert', {
      message: state => state.message,
      type: state => state.type
    }),
    active: {
      get() {
        return this.$store.state.alert.active;
      },
      set() {
        this.$store.commit('alert/dismissAlert')
      }
    },
    image() {
      switch (this.type) {
        case 'error':
          return errorUrl
        case 'info':
          return infoUrl
        case 'success':
          return successUrl
        case 'warning':
          return warningUrl
      }
    }
  },
  methods: {
    close() {
      this.$store.commit('alert/dismissAlert')
      return false
    }
  }
}

</script>

<style scoped>
  .alert-container {
    align-items: center;
    border-radius: 4px;
    box-shadow: 0px 0px 0px 0px rgb(0 0 0 / 20%), 0px 0px 0px 0px rgb(0 0 0 / 14%), 0px 0px 0px 0px rgb(0 0 0 / 12%);
    color: #FFFFFF;
    display: flex;
    margin-bottom: 10px;
    padding: 16px;
    width: 100%;
  }

  .alert-container a {
    color: #FFFFFF;
  }

  .alert-icon {
    display: flex;
    flex: 0;
    height: 24px;
    justify-content: flex-start;
    width: 150px;
  }
  .alert-message {
    align-items: left;
    flex: 1;
    justify-content: flex-start;
    padding-left: 10px;
    vertical-align: middle;
  }

  .alert-close {
    display: flex;
    flex: 0;
    justify-content: flex-end;
    width: 150px;
  }

  .alert-close a {
    cursor: pointer
  }
  .alert-error {
    background-color: #ff5252 !important;
    border-color: #ff5252 !important;
  }

  .alert-info {
    background-color: #2196f3 !important;
    border-color: #2196f3 !important;
  }

  .alert-success {
    background-color: #4caf50 !important;
    border-color: #4caf50 !important;
  }

  .alert-warning {
    background-color: #fb8c00 !important;
    border-color: #fb8c00 !important;
  }

  </style>