<script setup>
import AppInputError from './AppInputError.vue'
import Multiselect from 'vue-multiselect'
</script>

<template>
  <div class="bc-gov-dropdown-label">
    <label>{{ label }}</label>
  </div>

  <div id="app">
    <multiselect select-Label="" :options="options" selected-Label="" deselect-Label="" v-model="transactionType" :multiple="true" track-by="library" :custom-label="customLabel" :close-on-select="false" @select="onSelect($event)" @remove="onRemove($event)" v-bind="$attrs">
      <option v-for="option in options" v-bind:key="option.value" v-bind:value="option.value">
        <span class="checkbox-label" slot="option" slot-scope="scope" @click.self="select(scope.option)">
          {{ scope.option.library }}
          <input class="test" type="checkbox" v-model="scope.option.checked" @focus.prevent />
        </span>
      </option>
      <pre>{{ transactionType }}</pre>
    </multiselect>
  </div>

  <AppInputError :e-model="eModel" :label="label" />
</template>

<script>
export default {
  components: { Multiselect },

  name: 'AppMultiSelect',

  props: {
    eModel: {
      required: false,
      type: Object,
    },

    label: String,
    modelValue: String,
    options: Object,
  },
  data() {
    return {
      transactionType: [],
      options: [
        { library: 'Vue.js', checked: false },
        { library: 'Vue-Multiselect', checked: false },
        { library: 'Vuelidate', checked: false },
      ],
    }
  },

  methods: {
    customLabel(option) {
      return `${option.library}`
    },
    onSelect(option) {
      console.log('Added')
      let index = this.options.findIndex((item) => item.library == option.library)
      this.options[index].checked = true
      console.log(option.library + '  Clicked!! ' + option.checked)
    },

    onRemove(option) {
      console.log('Removed')
      let index = this.options.findIndex((item) => item.lib == option.lib)
      this.options[index].checked = false
      console.log(option.lib + '  Removed!! ' + option.checked)
    },
  },
  computed: {
    dropdownClass() {
      return {
        'bc-gov-dropdown': true,
        'error-input': this.eModel?.$error,
      }
    },
  },
}
</script>

<style scoped>
.checkbox-label {
  display: block;
}

.test {
  position: absolute;
  right: 1vw;
}
.bc-gov-dropdown-label {
  display: flex;
}

.bc-gov-dropdown {
  font-family: ‘BCSans’, ‘Noto Sans’, Verdana, Arial, sans-serif;
  font-size: 18px;
  color: #313132;
  background: white;
  box-shadow: none;
  border: 2px solid #606060;
  min-width: 200px;
  padding: 5px 5px 5px 7px;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  /* custom - different from bc gov */
  height: 34px;
  margin-top: 5px;
  margin-bottom: 15px;
  border-radius: 4px;
}

.fa-chevron-down {
  pointer-events: none;
  position: absolute;
  top: calc(1em - 4px);
  right: 1em;
}

.bc-gov-dropdown-wrapper {
  /* custom - different from bc gov */
  /* display: inline; removed */
  position: relative;
  display: flex;
  flex-direction: column;
}

:focus {
  outline: 2px solid #3b99fc;
  outline-offset: 1px;
}
.error-input {
  border-color: #d8292f !important;
}
</style>
