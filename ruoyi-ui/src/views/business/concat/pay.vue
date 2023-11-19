<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:charge:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:charge:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:charge:remove']"
        >删除
        </el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['business:charge:export']"-->
<!--        >导出-->
<!--        </el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chargeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="类型" align="center" prop="dictType">
        <template #default="slot">
          {{slot.row.dictType === "zfb_pay"?"支付宝":"微信"}}
        </template>
      </el-table-column>
      <el-table-column label="值" align="center" prop="dictValue">
        <template #default="slot">
          <image-preview :width="64" :height="64" :src="slot.row.dictValue"/>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="dictSort"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:charge:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:charge:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="平台" prop="type">
          <el-select v-model="form.type">
            <el-option value="zfb_pay" label="支付宝"></el-option>
            <el-option value="wx_pay" label="微信"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="值" prop="value">
          <image-upload  :limit="1" v-model="form.value"/>
        </el-form-item>
        <el-form-item label="排序" prop="weight">
          <el-input type="number" v-model="form.weight" placeholder="请输入排序"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listpay, delpay, addpay, updatepay, getpay} from "@/api/business/pay";

export default {
  name: "pay",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 充值套餐表格数据
      chargeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        name: null,
        price: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        type: [
          {required: true, message: "类型不能为空", trigger: "blur"}
        ],
        value: [
          {required: true, message: "值不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询充值套餐列表 */
    getList() {
      this.loading = true;
      listpay().then(response => {
        this.chargeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        type: null,
        value: null,
        weight: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictCode)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加客服";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.dictCode || this.ids
      getpay(id).then(response => {
        this.form.type = response.data.dictType
        this.form.value = response.data.dictValue
        this.form.weight = response.data.dictSort
        this.form.id = response.data.dictCode
        this.open = true;
        this.title = "修改客服";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatepay(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addpay(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.dictCode || this.ids;
      this.$modal.confirm('是否确认删除？').then(function () {
        return delpay(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    }
  }
};
</script>

