<template>
  <div class="info-container bc-gov-alertbanner bc-gov-alertbanner-info" v-if="visible">
    <div class="info-message">
      <p>
        <span style="white-space: pre-line">{{ message }}</span>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AppinfoPanel',

  props: {
    message: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: true,
    },
  },
  emits: ['close'],
}
</script>

<style scoped>
.info-container {
  align-items: center;
  display: flex;
}

.info-icon {
  display: flex;
  flex: 0;
  justify-content: flex-start;
  width: 150px;
}
.info-message {
  flex: 1;
  justify-content: flex-start;
  vertical-align: middle;
  /* white-space: pre-wrap; */
}

.info-additional-info {
  font-weight: normal;
}

.info-close {
  display: flex;
  flex: 0;
  justify-content: flex-end;
  width: 150px;
}
.info-close a {
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

.bc-gov-alertbanner-info {
  background-color: #ece4e4;
  border-color: #c7c1c2;
  color: #0c0c0c;
}

.bc-gov-alertbanner-info a {
  color: #c8bebe;
}

.bc-gov-alertbanner-info p {
  color: #0f0e0e;
}
</style>
