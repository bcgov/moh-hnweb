<template>
  <button :class="buttonClass" :disabled="submitting" :type="type">
    <slot></slot>
      <span v-if="submitting">
        <font-awesome-icon icon="spinner" class="fa-spin" />
      </span>
  </button>

</template>

<script>
  export default {
    name: 'AppButton',
    props: {
      submitting: {
        type: Boolean,
        default: false
      },
      mode: {
        type: String,
        default: 'primary'
      },
      type: {
        type: String,
        default: 'submit'
      }    
    },
    computed: {
      buttonClass() {
        return {
          'BC-Gov-PrimaryButton' : this.mode === 'primary' || this.mode === '',
          'BC-Gov-SecondaryButton' : this.mode === 'secondary',
        }
      }
    }
  }
</script>
<style scoped>
  .BC-Gov-PrimaryButton {
    background-color: #003366;
    border: none;
    border-radius: 4px;
    color: white;
    padding: 12px 32px;
    text-align: center;
    text-decoration: none;
    display: block;
    font-size: 16px; /* Modified from Design System template */
    font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
    font-weight: 700;
    letter-spacing: 1px;
    cursor: pointer;
    margin: 4px; /* Added to Design System template */
    height: 45px; /* Added to Design System template */
  }

  .BC-Gov-PrimaryButton:hover {
    text-decoration: underline;
    opacity: 0.80;
  }

  :focus {
    outline: 4px solid #3B99FC;
    outline-offset: 1px;
  }

  .BC-Gov-PrimaryButton:active {
    opacity: 1;
  }

  .BC-Gov-PrimaryButton:disabled {
    background-color: #003366;
    opacity: 0.3;
    text-decoration: none;
    cursor: not-allowed;
  }

  .BC-Gov-SecondaryButton {
    background: none;
    border-radius: 4px;
    border: 2px solid #003366;
    padding: 10px 30px;
    text-align: center;
    text-decoration: none;
    display: block;
    font-size: 16px; /* Modified from Design System template */
    Font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
    font-weight: 700;
    letter-spacing: 1px;
    cursor: pointer;
    color: #003366;
    margin: 4px; /* Added to Design System template */
    height: 45px; /* Added to Design System template */
  }
  .BC-Gov-SecondaryButton:hover {
    opacity: 0.80;
    text-decoration: underline;
    background-color: #003366;
    color: #FFFFFF;
  }
  :focus {
    outline-offset: 1px;
    outline: 4px solid #3B99FC;
  }
  .BC-Gov-SecondaryButton:active {
    opacity: 1;
  }

  .BC-Gov-SecondaryButton:disabled {
    background-color: white;
    opacity: 0.3;
    color: #003366;
    text-decoration: none;
    cursor: not-allowed;
  }

  .btn-sm {
    width: 100px;
  }

  .btn-md {
    width: 150px;
  }

  .btn-lg {
    width: 200px;
  }

  .btn-xl {
    width: 250px;
  }
</style>