<template>
  <div class="app-container">
    <main-flexible-tree :data="deptOptions"
                        :defaultProps="TreeDefaultProps"
                        @handleNodeClick="handleNodeClick">
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
          <el-form-item label="用户账号" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="请输入用户账号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="用户名称" prop="nickName">
            <el-input
              v-model="queryParams.nickName"
              placeholder="请输入用户名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            v-hasPermi="['system:user:add']"
            @click="handleAdd"
          >新增用户
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="handleUpdate"
            v-hasPermi="['system:user:edit']"
          >编辑用户
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            @click="handleDelete"
            v-hasPermi="['system:user:remove']"
          >删除用户
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-refresh"
            size="mini"
            @click="handleResetPwd"
            v-hasPermi="['system:user:resetPwd']"
          >重置密码
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-setting"
            size="mini"
            @click="changeDept"
            v-hasPermi="['system:user:export']"
          >部门调整
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-link"
            size="mini"
            @click="changeRole"
            v-hasPermi="['system:user:import']"
          >绑定角色
          </el-button>
        </div>
        <slot-table class="rightTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                    :total="total">
          <el-table highlight-current-row
            :data="userList"
            stripe
            border
            height="100%"
            style="width: 100%;"
            ref="multipleTable"
            @selection-change="handleSelectionChange"
            @row-click="handleRowClick"
            slot="table">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column
              type="index"
              label="序号"
              width="50">
              <template slot-scope="scope">
                <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-for="item in userListColumn"
              :key="item.userName"
              :prop="item.prop"
              :label="item.label"
              :width="item.width"
              :formatter="item.formatter" show-overflow-tooltip>
            </el-table-column>
          </el-table>
        </slot-table>

      </div>
    </main-flexible-tree>
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form ref="form" :model="form" :rules="rules" :inline="true" class="ipt">
        <el-form-item label="登录账号" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入账号名称" maxlength="30"/>
        </el-form-item>
        <el-form-item label="真实姓名" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入真实姓名" maxlength="30"/>
        </el-form-item>
        <el-form-item label="用户性别">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="岗位">
          <el-select v-model="form.postIds" placeholder="请选择职位">
            <el-option
              v-for="item in postOptions"
              :key="item.postId"
              :label="item.postName"
              :value="item.postId"
              :disabled="item.status == 1"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId" style="margin-right: 10px;">
              <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="请选择所属部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属班组" prop="teamId" style="margin-right: 10px;">
              <el-select v-model="form.teamId" clearable placeholder="请选择班组">
                <el-option
                  v-for="item in teamList"
                  :key="item.id"
                  :label="item.teamName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11"/>
        </el-form-item>
        <el-form-item label="系数" prop="coefficient">
          <el-input v-model="form.coefficient" placeholder="请输入系数" maxlength="11"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50"/>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" maxlength="50"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="'0'">启用</el-radio>
            <el-radio :label="'1'">停用</el-radio>
          </el-radio-group>
<!--          <el-radio-group v-model="form.status">-->
<!--            <el-radio-->
<!--              v-for="dict in dict.type.sys_normal_disable"-->
<!--              :key="dict.value"-->
<!--              :label="dict.value"-->
<!--            >{{ dict.label }}-->
<!--            </el-radio>-->
<!--          </el-radio-group>-->
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" size="mini" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="mini">保存</el-button>
        <el-button @click="cancel" size="mini">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 部门调整 -->
    <el-dialog title="部门调整" :visible.sync="deptOpen" width="400px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form ref="form" :model="form" :rules="rules" :inline="true" class="ipt onlyOneChild">
          <el-form-item label="所属部门" prop="deptId">
            <el-cascader
              v-model="form.deptId"
              :options="deptOptions"
              :props="{value:'id',label:'label',checkStrictly: true, emitPath: false}"
              :show-all-levels="false"
              ref="computeSelect"
              clearable>
              <template slot-scope="{ node, data }">
                <div @click="cascaderClick(node,data)">
                  <span>{{ data.label }}</span>
                  <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
                </div>
              </template>
            </el-cascader>
          </el-form-item>
        <el-form-item label="所属班组" prop="teamId">
          <el-select v-model="form.teamId" clearable placeholder="请选择班组">
            <el-option
              v-for="item in teamList"
              :key="item.id"
              :label="item.teamName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="mini">保存</el-button>
        <el-button @click="deptOpen = false" size="mini">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 绑定角色 -->
    <el-dialog title="绑定角色" :visible.sync="roleOpen" width="800px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <transferTable
        v-if="roleOpen"
        v-model="roleIds"
        :dataList="roleOptions"
        :columns="roleColumns"
        keyName="roleId"
        @change="change"
      ></transferTable>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="roleChange" size="mini">保存</el-button>
        <el-button @click="roleOpen = false" size="mini">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus} from "@/api/system/user";
import {getToken} from "@/utils/auth";
import {getAllTree, treeselect} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";

import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import SlotTable from "@/components/public/table/SlotTable";
import SelectTree from "@/components/public/tree/selectTree";
import TransferTable from "@/components/public/table/transferTable";
import {groupBy} from "@/utils/groupBy";
import {getAllTeam} from "@/api/system/team";

export default {
  name: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: {TransferTable, SelectTree, SlotTable, MainFlexibleTree, Treeselect},
  data() {
    return {
      props: {
        // 配置项（必选）
        value: "id",
        label: "name",
        children: "children"
        // disabled:true
      },
      isClearable: true, // 可清空（可选）
      isAccordion: true, // 可收起（可选）
      valueId:'', // 初始ID（可选）
      // 选项列表（必选）
      list: [],
      teamList:[],
      roleColumns:[
        {label: '角色名称', key: 'roleName',visible: true,},
        {label: '角色描述', key: 'roleDesc',visible: true,},
        {label: '备注', key: 'remark',visible: true,}
        ],
      treeData: [],
      TreeDefaultProps: {
        children: 'children',
        label: 'label'
      },
      pageSize: 20,
      pageNum: 1,
      total: 0,
      // 选中数组
      selected: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      // 用户表格数据
      userList: null,
      userListColumn: [
        {label: '用户账号', prop: 'userName',},
        {label: '用户昵称', prop: 'nickName'},
        {label: '部门', prop: 'dept.deptName'},
        {label: '性别', prop: 'sex', formatter: this.sexFormatter},
        {label: '联系电话', prop: 'phonenumber'},
        {label: '邮箱地址', prop: 'email'},
        {label: '联系地址', prop: 'address'},
        {label: '创建时间', prop: 'createTime',width:'180'},

      ],  //表头
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      deptOpen:false,
      roleIds:[],
      roleOpen:false,
      // 用户名称
      nickName: undefined,
      // 默认密码
      initPassword: undefined,

      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        userName: undefined,
        nickName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 表单校验
      rules: {
        userName: [
          {required: true, message: "用户名称不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur'}
        ],
        nickName: [
          {required: true, message: "用户昵称不能为空", trigger: "blur"}
        ],
        password: [
          {required: true, message: "用户密码不能为空", trigger: "blur"},
          {min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur'}
        ],
        // email: [
        //   {
        //     type: "email",
        //     message: "请输入正确的邮箱地址",
        //     trigger: ["blur", "change"]
        //   }
        // ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      transferList: []
    };
  },
  watch:{
    'form.deptId'(newVal,oldVal){
      if(newVal != undefined && oldVal != undefined){
        this.form.teamId = ''
      }
      this.getTeamSelect(newVal)
    },
    "form.userName": function (newVal, oldVal) {
     if (newVal.length != newVal.replace(/[^\w]/g, '').length) {
        //replace就是清除的意思，判断清除特殊字符串后，如果长度和没有清除之前保持一致
        //那就代表有特殊字符串，就执行这个if代码块
        this.$message.error("只能为数字和字母");
        this.form.userName='';
      }
    }
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },

  computed: {
    /* 转树形数据 */
    optionData() {
      let cloneData = JSON.parse(JSON.stringify(this.list)); // 对源数据深度克隆
      return cloneData.filter(father => {
        // 循环所有项，并添加children属性
        let branchArr = cloneData.filter(child => father.id == child.parentId); // 返回每一项的子级数组
        branchArr.length > 0 ? (father.children = branchArr) : ""; //给父级添加一个children属性，并赋值
        return father.parentId == 0; //返回第一层
      });
    }
  },
  methods: {
    cascaderClick(node,nodeData){
      this.form.deptId = nodeData.id
      this.$refs.computeSelect.checkedValue = node.value;
      this.$refs.computeSelect.computePresentText();
    },
    getTeamSelect(deptId){
      getAllTeam({deptId}).then(res=>{
        this.teamList = res.data
      })
    },
    roleChange(){
      let {...formData} = this.form
      formData.roleIds = this.roleIds
      if(Array.isArray(this.form.postIds) && this.form.postIds.length > 0){
        formData.postIds = this.form.postIds
      }else if (this.form.postIds && this.form.postIds != ""){
        formData.postIds = [this.form.postIds]
      }
      updateUser(formData).then(response => {
        this.$modal.msgSuccess("绑定成功");
        this.roleOpen = false;
        this.getList();
      });
    },
    change(val) {
    },
    /** 查询用户列表 */
    getList() {
      listUser(this.addDateRange(this.queryParams)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
        }
      );
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data;
      });
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    /** 获取字典表性别 */
    sexFormatter(row, column, cellValue, index) {
      if (this.dict.type.sys_user_sex) {
        let sexArr = this.dict.type.sys_user_sex;
        let sexLabel = '';
        sexArr.forEach(item => {
          if (cellValue == item.value) {
            sexLabel = item.label
          }
        })
        return sexLabel
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selected = selection;

    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        teamId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        coefficient:undefined,
        address:undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: undefined,
      };
      this.resetForm("form");
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    // 下拉选择树取值
    getValue(value) {
      this.valueId = value;
    },


    /** 新增按钮操作 */
    handleAdd() {
      // this.reset();
      this.teamList = []
      this.form = {status: "0"}
      this.getTreeselect();
      getUser().then(response => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "新增用户";
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.form = {}
      if (!this.selected || this.selected === [] || this.selected.length === 0 ||this.selected.length !== 1) {
        this.$message({
          message: '请选择一条数据',
          type: 'warning'
        });
      } else {
        getUser(this.selected[0].userId).then(response => {
          this.postOptions = response.posts;
          this.roleOptions = response.roles;
          this.form = response.data
          delete this.form.password
          this.form.postIds = Array.isArray(response.postIds) && response.postIds.length > 0?response.postIds[0]:null
          this.form.roleIds = response.roleIds
          this.open = true;
          console.log("form" , JSON.stringify(this.form))
          this.title = "编辑用户";
        })

      }
    },
    /** 删除按钮操作 */
    // handleDelete() {
    //   const userIds = row.userId || this.selected;
    //   this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function () {
    //     return delUser(userIds);
    //   }).then(() => {
    //     this.getList();
    //     this.$modal.msgSuccess("删除成功");
    //   }).catch(() => {
    //   });
    // },
    handleDelete() {
      if (!this.selected || this.selected === [] || this.selected.length === 0) {
        this.$message({
          message: '请至少选择一条数据',
          type: 'warning'
        });
      } else {
        this.$confirm('此操作将永久删除选中数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          size:'mini'
        }).then(() => {
          delUser(this.selected.map(item=>{
            return item.userId
          }).toString()).then(res=>{
            this.getList();
            this.$modal.msgSuccess("删除成功");
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    /** 重置密码按钮操作 */
    handleResetPwd() {
      if (!this.selected || this.selected === [] || this.selected.length === 0) {
        this.$message({
          message: '请至少选择一条数据',
          type: 'warning'
        });
      } else {
        this.$confirm('此操作会将选中用户密码重置为123456, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          getUser(this.selected[0].userId).then(response => {
            let {...formData} = response.data
            delete this.form.password
            this.roleOptions = response.roles;
            this.form.postIds = response.postIds
            this.roleIds = response.roleIds
            if(Array.isArray(this.form.postIds) && this.form.postIds.length > 0){
              formData.postIds = this.form.postIds
            }else if (this.form.postIds && this.form.postIds != ""){
              formData.postIds = [this.form.postIds]
            }
            formData.password = 123456
            formData.roleIds = this.roleIds
            formData.postIds = this.form.postIds
            updateUser(formData).then(res=>{
              this.$message.success("重置成功")
            })
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消重置'
          });
        });
      }
    },
    changeDept(){
      this.form = {}
      if (!this.selected || this.selected === [] || this.selected.length === 0) {
        this.$message({
          message: '请选择一条数据',
          type: 'warning'
        });
      } else if (this.selected.length !== 1) {
        this.$message({
          message: '请选择一条数据',
          type: 'warning'
        });
      } else {
        getUser(this.selected[0].userId).then(response => {
          this.form = response.data
          delete this.form.password
          this.roleOptions = response.roles;
          this.form.postIds = response.postIds
          this.roleIds = response.roleIds
          this.deptOpen = true;
        });
      }
    },
    changeRole(){
      this.form = {}
      if (!this.selected || this.selected === [] || this.selected.length === 0) {
        this.$message({
          message: '请选择一条数据',
          type: 'warning'
        });
      } else if (this.selected.length !== 1) {
        this.$message({
          message: '请选择一条数据',
          type: 'warning'
        });
      } else {
        getUser(this.selected[0].userId).then(response => {
          this.form = response.data
          delete this.form.password
          this.roleOptions = response.roles;
          this.form.postIds = response.postIds
          this.roleIds = response.roleIds
          this.roleOpen = true;
        });
      }
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            let {...formData} = this.form
            if(Array.isArray(this.form.postIds) && this.form.postIds.length > 0){
              formData.postIds = this.form.postIds
            }else if (this.form.postIds && this.form.postIds != ""){
              formData.postIds = [this.form.postIds]
            }
            updateUser(formData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.deptOpen = false;
              this.getList();
            });
          } else {
            let {...formData} = this.form
            if(Array.isArray(this.form.postIds) && this.form.postIds.length > 0){
              formData.postIds = this.form.postIds
            }else if (this.form.postIds && this.form.postIds != ""){
              formData.postIds = [this.form.postIds]
            }

            addUser(formData).then(response => {
              this.$modal.msgSuccess("新增成功，密码默认为：123456");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 分页 */
    handleChange(size, num) {
    },

  }
};
</script>
<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
  //overflow-x: auto;
}
//表单中只有一个item
.onlyOneChild{
  display: flex;
  justify-content: center;
  align-items: center;
  .el-form-item{
    width: 80%;
    justify-content: center;
    margin-right: 0;
  }
}
</style>

