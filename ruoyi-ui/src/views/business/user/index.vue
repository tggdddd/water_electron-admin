<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账号" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择用户类型" clearable>
          <el-option
            v-for="dict in dict.type.user_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入手机号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable>
          <el-option
            v-for="dict in dict.type.sys_user_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="帐号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择帐号状态" clearable>
          <el-option
            v-for="dict in dict.type.user_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:user:add']"
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
          v-hasPermi="['business:user:edit']"
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
          v-hasPermi="['business:user:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="用户ID" align="center" prop="userId" />-->
      <el-table-column label="账号" align="center" prop="userName"/>
      <el-table-column label="昵称" align="center" prop="nickName"/>
      <el-table-column label="头像" align="center" prop="avatar">
        <template #default="scope">
          <image-preview  :width="64" :height="64" :src="scope.avatar">
          </image-preview>
        </template>
      </el-table-column>
      <el-table-column label="性别" align="center" prop="sex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center" prop="email"/>
      <el-table-column label="手机号码" align="center" prop="phonenumber"/>
      <el-table-column label="类型" align="center" prop="userType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_type" :value="scope.row.userType"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="余额" align="center" prop="money">
        <template #default="scope">
          <el-tag type="warning">￥{{scope.row.money}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会员到期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录IP" align="center" prop="loginIp"/>
      <el-table-column label="最近登录时间" align="center" prop="loginDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column fixed="right" label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="getRecordInfo(scope.row)"
            v-hasPermi="['business:user:edit']"
          >充值记录
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="toCharge(scope.row)"
            v-hasPermi="['business:user:edit']"
          >充值
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:user:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:user:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账号" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.user_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="到期时间" prop="expireTime">
          <el-date-picker
            v-model="form.expireTime"
            type="datetime"
            placeholder="请选择会员到期时间"
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号码"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <image-upload :limit="1" v-model="form.avatar"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--  充值记录  -->
    <el-dialog :title="title" :visible.sync="record_open" width="80%" append-to-body>
      <UserRecord v-if="record_open" :userId="record_user_id"/>
    </el-dialog>
    <!--  充值  -->
    <el-dialog :title="title" :visible.sync="charge_open" width="60%" append-to-body>
      <user-charge :userId="record_user_id"/>
    </el-dialog>
  </div>
</template>

<script>
import {listUser, getUser, delUser, addUser, updateUser} from "@/api/business/user";
import UserRecord from "@/views/business/user/userRecord.vue";
import UserCharge from "@/views/business/user/userCharge.vue";
import {parseTime} from "../../../utils/ruoyi";

export default {
  name: "User",
  components: {UserCharge, UserRecord},
  dicts: ['user_status', 'user_type', 'sys_user_sex'],
  data() {
    return {
      charge_open: false,
      record_user_id: 0,
      record_open: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedUserDuration: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户信息表格数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        nickName: null,
        userType: null,
        email: null,
        phonenumber: null,
        sex: null,
        status: null,
        loginIp: null,
        loginDate: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userName: [
          {required: true, message: "用户账号不能为空", trigger: "blur"}
        ],
        nickName: [
          {required: true, message: "用户昵称不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    parseTime,
    /** 查询用户信息列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(response => {
        this.userList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /*用户充值*/
    toCharge(row) {
      this.record_user_id = row.userId
      this.title = "充值";
      this.charge_open = true;
    },
    /*获取用户的充值记录*/
    getRecordInfo(row) {
      this.record_user_id = row.userId
      this.title = "充值记录";
      this.record_open = true;

    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        avatar: "",
        email: "",
        expireTime: "",
        nickName: "",
        password: "",
        phonenumber: "",
        sex: "",
        status: "",
        userName: "",
      };
      this.userDurationList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeLoginDate = [];
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids
      getUser(userId).then(response => {
        this.form = response.data;
        this.form.password = ""
        this.open = true;
        this.title = "修改用户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != null) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
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
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户信息编号为"' + userIds + '"的数据项？').then(function () {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
