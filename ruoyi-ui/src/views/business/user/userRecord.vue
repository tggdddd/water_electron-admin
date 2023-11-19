<script>
import {getUserChargeRecord} from "@/api/business/user";
import {parseTime} from "../../../utils/ruoyi";

export default {
  props: ["userId"],
  data() {
    return {
      total: 0,
      loading: false,
      open: false,
      data: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null
      },
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    parseTime,
    getList(params) {
      this.loading = true;
      getUserChargeRecord(this.userId, this.queryParams).then(response => {
        this.data = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    }
  }
}
</script>

<template>
  <div>
    <el-table v-loading="loading" :data="data">
      <el-table-column label="充值订单" align="center" prop="chargeCode"/>
      <el-table-column label="充值方式" align="center" prop="chargeTypeText">
      </el-table-column>
      <el-table-column label="会员类型" align="center" prop="memberTypeText">
      </el-table-column>
      <el-table-column label="充值时间" align="center" prop="chargeTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.chargeTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作员" align="center" prop="createByText"/>
<!--      <el-table-column label="操作时间" align="center" prop="createTime">-->
<!--        <template #default="scope">-->
<!--          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<style scoped lang="scss">

</style>
