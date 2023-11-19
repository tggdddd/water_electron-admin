<script>
import SelectOptions from "@/views/business/components/SelectOptions.vue";
import {userChargeApi} from "@/api/business/user";

export default {
  props: ["userId"],
  emits: ["close"],
  components: {SelectOptions},
  dicts: ['charge_type'],
  data() {
    return {
      formData: {
        chargeType: "",
        code: "",
        memberType: "",
        money: "",
        remark: ""
      },
      switchValue: false
    }
  },
  mounted() {

  },
  methods: {
    charge() {
      const data = {
        chargeType: this.formData.chargeType,
        code: this.formData.code,
        remark: this.formData.remark,
        memberType: this.formData.memberType,
        money: this.formData.money,
        chargeMember: this.switchValue
      }
      userChargeApi(this.userId,data).then(res => {
          this.$modal.msgSuccess("充值成功")
          this.$emit("close")
        }
      ).catch(error => {
      })
    }
  }
}
</script>

<template>
  <el-form :model="formData" ref="queryForm" size="small" label-width="68px">
    <el-form-item label="充值方式" prop="chargeType">
      <el-select v-model="formData.chargeType" placeholder="请选择充值方式" clearable>
        <el-option
          v-for="dict in dict.type.charge_type"
          :key="dict.value"
          :label="dict.label"
          :value="dict.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="充值订单" prop="code">
      <el-input
        v-model="formData.code"
        placeholder="请输入充值订单"
        clearable
      />
    </el-form-item>
    <!--  todo  充值金钱 或  充值会员-->
    <el-form-item label="充值类型">
      <template #default>
        <el-switch
          v-model="switchValue"
          class="ml-2"
          active-color="#13ce66"
          inactive-color="13ce66"
          active-text="会员"
          inactive-text="金额"
        />
        <template v-if="switchValue">
          <select-options
            table="charge"
            label-field="name"
            value-field="id"
            where=""
            v-model="formData.memberType" placeholder="请选择充值方式"/>
        </template>
        <template v-else>
          <el-input
            v-model="formData.money"
            placeholder="请输入充值金额"
            type="number"
            clearable
          />
        </template>
      </template>
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input type="textarea" rows="2" v-model="formData.remark"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="charge">充值</el-button>
    </el-form-item>
  </el-form>

</template>

<style scoped lang="scss">

</style>
