<template>
  <div id="addDependent">
    <AppRow>
      <AppCol class="col3">
        <AppLabel>Dependent</AppLabel>
      </AppCol>
      <AppCol class="col3">
        <AppInput :e-model="v$.phn" id="dependentPHN" label="PHN" v-model.trim="phn" />
      </AppCol>
      <AppCol class="col2 center">
        <AppButton @click="addDependent()" class="btn-sm" mode="secondary" type="button" v-if="isAddVisible">Add</AppButton>
      </AppCol>
    </AppRow>

    <AppRow>
      <AppCol class="col3"></AppCol>
      <AppCol class="col3">
        <ul id="dependentList" class="dependent-list">
          <li v-for="(dependent, index) in dependents">{{ dependent }} <font-awesome-icon id="removeIcon" icon="trash-alt" @click="removeDependent(index)" class="trash" /></li>
        </ul>
      </AppCol>
    </AppRow>
  </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import { required, helpers } from '@vuelidate/validators'
import AppLabel from '../ui/AppLabel.vue'
import { validatePHN, VALIDATE_PHN_MESSAGE } from '../../util/validators'

export default {
  name: 'AddDependent',
  components: {
    AppLabel,
  },
  setup() {
    return {
      v$: useVuelidate({ $stopPropagation: true }),
    }
  },
  data() {
    return {
      phn: '',
    }
  },
  props: {
    dependents: {
      type: Array,
      required: true,
    },
  },
  emits: ['addDependent', 'removeDependent'],
  methods: {
    async addDependent() {
      const isValid = await this.v$.$validate()
      if (!isValid) {
        this.$store.commit('alert/setErrorAlert')
        return
      }

      this.$emit('addDependent', this.phn)
      this.phn = ''
      this.v$.$reset()
    },
    removeDependent(index) {
      this.$emit('removeDependent', index)
    },
  },
  computed: {
    isAddVisible() {
      return this.dependents.length < 7
    },
  },
  validations() {
    return {
      phn: {
        required,
        validatePHN: helpers.withMessage(VALIDATE_PHN_MESSAGE, validatePHN),
      },
    }
  },
}
</script>

<style scoped>
.dependent-list > li {
  font-size: 18px;
  list-style: circle;
  margin-left: 20px;
  padding-bottom: 10px;
}

.trash {
  cursor: pointer;
}
</style>
