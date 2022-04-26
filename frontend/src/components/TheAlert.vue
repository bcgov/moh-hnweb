<template>
  <div :class="`alert-container bc-gov-alertbanner bc-gov-alertbanner-${type}`"  role="alert" v-if="active">
    <div class="alert-icon">
      <font-awesome-icon :icon="icon"></font-awesome-icon>
    </div>
    <div class="alert-message">
      <p>{{message}}</p>
    </div>
    <div class="alert-close">
      <a @click="close()">
        <font-awesome-icon icon="times" />
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
    icon() {
      switch (this.type) {
        case 'error':
          return 'exclamation-circle'
        case 'info':
          return 'info-circle'
        case 'success':
          return 'check-circle'
        case 'warning':
          return 'exclamation-triangle'
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
    display: flex;
  }

  .alert-icon {
    display: flex;
    flex: 0;
    justify-content: flex-start;
    width: 150px;
  }
  .alert-message {
    align-items: left;
    flex: 1;
    justify-content: flex-start;
    vertical-align: middle;
    white-space: pre-wrap;
  }

  .alert-close {
    display: flex;
    flex: 0;
    justify-content: flex-end;
    width: 150px;
  }
  .alert-close a {
    cursor: pointer;
  }

  .bc-gov-alertbanner {
    border: 1px solid transparent;
    border-radius: 4px;
    font-weight: 700;
    margin-bottom: 20px;
    padding: 15px;
  }
  .bc-gov-alertbanner p {
    font-size: 18px;
    margin: 0;
    padding-left: 35px;
  }

  .bc-gov-alertbanner-error {
    background-color: #f2dede;
    border-color: #ebccd1;
    color: #a12622;
  }

  .bc-gov-alertbanner-error a {
    color: #a12622;
  }

  .bc-gov-alertbanner-error p {
    color: #843534;
  }

  .bc-gov-alertbanner-warning {
    background-color: #f9f1c6;
    border-color: #faebcc;
    color: #6c4a00;
  }

  .bc-gov-alertbanner-warning p {
    color: #6c4a00;
  }

  .bc-gov-alertbanner-warning a {
    color: #66512c;
  }

.bc-gov-alertbanner-info {
  background-color: #d9eaf7;
}

.bc-gov-alertbanner-info a {
  color: #1a5a96;
}

.bc-gov-alertbanner-info p {
  color: #313132
}


.bc-gov-alertbanner-success {
  background-color: #dff0d8;
  border-color: #d6e9c6;
  color: #2d4821;
}

.bc-gov-alertbanner-success a {
  color: #2b542c;
}

.bc-gov-alertbanner-success p {
  color: #2d4821;
}

  </style>