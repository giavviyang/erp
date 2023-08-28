<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn" @submit.native.prevent>
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="请输入角色名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['system:role:add']">新增角色
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:role:edit']">编辑角色
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:role:remove']">删除角色
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-connection"
        size="mini"
        @click="handleMenu"
        v-hasPermi="['system:role:export']">分配功能权限
      </el-button>
    </div>

    <slot-table class="rightTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total">
      <el-table highlight-current-row
        :data="roleList"
        stripe
        border
        style="width: 100%"
        height="100%"
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
          width="55" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-for="item in roleListColumn"
          :key="item.userName"
          :prop="item.prop"
          :label="item.label" show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="400px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" size="mini" :inline="true">
        <el-form-item label="角色名称" prop="roleName">
          <el-input style="width: 200px" v-model="form.roleName"  placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item label="角色标识" prop="roleKey">
          <el-input  style="width: 200px" v-model="form.roleKey" placeholder="请输入角色标识"/>
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDesc">
          <el-input style="width: 200px" v-model="form.roleDesc" placeholder="请输入角色描述"/>
        </el-form-item>
        <el-form-item label="备注">
          <el-input style="width: 200px" v-model="form.remark" type="textarea" size="mini" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="mini">保存</el-button>
        <el-button @click="cancel" size="mini">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="分配功能权限" :visible.sync="menuRoleDialog" width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <div style="width: 100%;height: 300px;overflow: auto">
        <el-tree
          :data="menuList"
          show-checkbox
          ref="menuTree"
          node-key="menuId"
          :props="{children: 'children', label: 'menuName'}">
        </el-tree>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="menuRoleSubmit" size="mini">保存</el-button>
        <el-button @click="menuCancal" size="mini">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listRole, getRole, delRole, addRole, updateRole, dataScope, changeRoleStatus} from "@/api/system/role";
import SlotTable from "@/components/public/table/SlotTable";
import {listMenu,roleMenuTreeselect} from "@/api/system/menu";

export default {
  name: "Role",
  components: {SlotTable},
  dicts: ['sys_normal_disable'],
  data() {
    return {
      menuList: [],
      // 选中数组
      selected: [],
      // 角色表格数据
      roleList: [],
      roleListColumn: [
        {label: '角色名称', prop: 'roleName'},
        {label: '角色描述', prop: 'roleDesc'},
        {label: '备注', prop: 'remark'}
      ],  //表头
      pageSize: 20,
      pageNum: 1,
      total: 0,    // 总条数
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      menuRoleDialog: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        roleName: undefined,
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        roleName: [
          {required: true, message: "角色名称不能为空", trigger: "blur"}
        ],
        roleKey: [
          {required: true, message: "角色标识不能为空", trigger: "blur"}
        ],
        roleDesc: [
          {required: true, message: "角色描述不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getMenuList();
  },
  methods: {
    getMenuList() {
      listMenu().then(response => {
        this.menuList = this.handleTree(response.data, "menuId");
      });
    },
    /** 查询角色列表 */
    getList() {
      listRole(this.addDateRange(this.queryParams)).then(response => {
          this.roleList = response.rows;
          this.total = response.total;
        }
      );
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selected = selection
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roleId != undefined) {
            updateRole(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRole(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    menuCancal() {
      this.$refs.menuTree.setCheckedKeys([]);
      this.menuRoleDialog = false
    },
    // 表单重置
    reset() {
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleDesc: undefined
      };
      this.resetForm("form");
    },
    /** 新增按钮操作 */
    handleAdd() {
      // this.reset();
      this.open = true;
      this.title = "新增角色";
      this.form={};

    },
    /** 修改按钮操作 */
    handleUpdate() {
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
        this.form = JSON.parse(JSON.stringify(this.selected[0]))
        roleMenuTreeselect(this.form.roleId).then(response => {
            this.form.menuIds = response.checkedKeys
            this.title = "编辑角色";
            this.open = true;
        });
      }
    },
    /** 删除按钮操作 */
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
          size: 'mini'
        }).then(() => {
          delRole(this.selected.map(item => {
            return item.roleId
          }).toString()).then(res => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.handleQuery();
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    //分配权限
    handleMenu() {
      if (!this.selected || this.selected === [] || this.selected.length === 0 || this.selected.length !== 1) {
        this.$message({
          message: '请选择一条数据',
          type: 'warning'
        });
      } else {
        this.form = this.selected[0]
        this.menuRoleDialog = true
        console.log(this.form)
        roleMenuTreeselect(this.form.roleId).then(response => {
          this.$nextTick(() => {
              let checkedKeys = response.checkedKeys
              checkedKeys.forEach((v) => {
                this.$nextTick(()=>{
                  this.$refs.menuTree.setChecked(v, true ,false);
                })
              })
          });
        });
      }
    },
    menuRoleSubmit() {
      if (this.$refs.menuTree.getCheckedKeys().length > 0) {
        this.form.menuIds = this.$refs.menuTree.getCheckedKeys().concat(this.$refs.menuTree.getHalfCheckedKeys())
      } else {
        this.form.menuIds = null
      }
      updateRole(this.form).then(res => {
        this.$message.success("分配权限成功");
        this.menuCancal()
      })
    },
    handleChange(size, num) {
      console.log(size, num)
    },
  }
};
</script>
<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
}

::v-deep .ipt .el-form-item:last-of-type {
  margin-right: 0px;
}
</style>

