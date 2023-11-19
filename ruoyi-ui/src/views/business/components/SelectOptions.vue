<script>
import {getOptions} from "@/api/business/common";

export default {
  emits: ['input'],
  props: {
    value: {
      require: true
    },
    placeholder: {
      default: "请选择"
    },
    table: {
      require: true
    },
    valueField: {
      require: true
    },
    labelField: {
      require: true
    },
    where: {
      default: ""
    }
  },
  data() {
    return {
      list: [],
      selectValue: "",
    }
  },
  mounted() {
    getOptions(this.table, this.valueField, this.labelField, this.where)
      .then(res => {
        this.list = res.data
      })
  },
  methods: {
    charge() {

    }
  },
  computed: {
    labelValue: {
      get() {
        return this.value
      },
      set(value) {
        this.$emit('input', value)
      }
    }
  }
}
</script>

<template>
  <div>
    <el-select v-model="labelValue" :placeholder="placeholder" clearable>
      <el-option
        v-for="dict in list"
        :key="dict.value"
        :label="dict.label"
        :value="dict.value"
      />
    </el-select>
  </div>
</template>

<style scoped lang="scss">

</style>
