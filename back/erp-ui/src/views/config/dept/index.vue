<template>
  <div class="app-container">
    <MainFlexibleTreeRightClick :data="treeData"
                                :defaultProps="TreeDefaultProps"
                                @handleNodeClick="handleNodeClick">
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
          <el-form-item label="部门名称" prop="deptName">
            <el-input
              v-model="queryParams.deptName"
              placeholder="请输入部门名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="部门状态" clearable>
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
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
            v-hasPermi="['system:dept:add']"
            @click="handleAdd"
          >新增部门
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="handleUpdate"
            v-hasPermi="['system:dept:edit']"
          >编辑部门
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            @click="handleDelete"
            v-hasPermi="['system:dept:remove']"
          >删除部门
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-setting"
            size="mini"
            @click="handleChangeParent"
            v-hasPermi="['system:dept:query']"
          >部门调整
          </el-button>
        </div>
        <slot-table class="rightTable" @handleChange="handleChange" :page-num="queryParams.pageNum" :page-size="queryParams.pageSize" :total="queryParams.count">
          <el-table highlight-current-row
            :data="deptList"
            stripe
            border
            height="100%"
            ref="multipleTable"
            @selection-change="handleSelectionChange"  @row-click="handleRowClick" slot="table">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column
              type="index"
              :index="getIndex"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              v-for="item in deptListColumn"
              :key="item.deptName"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              :formatter="item.formatter" show-overflow-tooltip>
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </MainFlexibleTreeRightClick>
    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="35%" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true" class="dialog-style">
      <el-form ref="form" :model="form" :rules="rules" size="mini" :inline="true" class="ipt">
        <el-form-item label="上级部门">
          <el-cascader
            ref="cascader"
            v-model="form.parentId"
            :options="treeData"
            :props="{value:'deptId',label:'deptName',checkStrictly: true, emitPath: false}"
            :show-all-levels="false"
            clearable>
            <template slot-scope="{ node, data }">
              <div @click="cascaderClick(data)">
                <span>{{ data.deptName }}</span>
                <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
              </div>
            </template>
          </el-cascader>
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称"/>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input v-model.number="form.orderNum" placeholder="请输入排序"/>
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20"/>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50"/>
        </el-form-item>
        <el-form-item label="部门状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="mini">保存</el-button>
        <el-button @click="cancel" size="mini">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 调整部门 -->
    <el-dialog title="部门调整" :visible.sync="openChangeParent" width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form ref="form" :model="parentForm" :rules="parentRules" class="ipt onlyOneChild" :inline="true">
        <el-form-item label="归属部门">
          <el-cascader
            ref="cascader"
            v-model="parentForm.parentId"
            :options="treeData"
            :props="{value:'deptId',label:'deptName',checkStrictly: true, emitPath: false}"
            :show-all-levels="false"
            clearable>
            <template slot-scope="{ node, data }">
              <div @click="cascaderClick(data)">
                <span>{{ data.deptName }}</span>
                <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
              </div>
            </template>
          </el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitParentForm" size="mini">保存</el-button>
        <el-button @click="openChangeParent = false" size="mini">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listDept, getAllTree, delDept, addDept, updateDept, listDeptExcludeChild, treeselect} from "@/api/system/dept";
import { groupBy } from "@/utils/groupBy";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import {component as VueContextMenu} from '@xunlei/vue-context-menu';
import SlotTable from "@/components/public/table/SlotTable";
import MainFlexibleTreeRightClick from "@/components/public/MainBody/MainFlexibleTreeRightClick";

export default {
  name: "Dept",
  dicts: ['sys_normal_disable'],
  components: {
    MainFlexibleTreeRightClick,
    Treeselect,
    SlotTable,
    MainFlexibleTree,
    'vue-context-menu': VueContextMenu,
  },
  // components: {  },
  data() {
    return {
      contextMenuTarget: "",
      contextMenuVisible: false,
      treeData: [],
      TreeDefaultProps: {
        children: 'children',
        label: 'deptName'
      },
      deptList: [],         // 表格数据
      deptListColumn: [
        {label: '部门名称', prop: 'deptName',width:'150'},
        {label: '负责人', prop: 'leader',width:'150'},
        {label: '联系电话', prop: 'phone',width:'180'},
        {label: '邮箱', prop: 'email',width:'200'},
        {label: '排序', prop: 'orderNum',width:'100'},
        {label: '状态', prop: 'status', formatter: this.statusFormatter,width:'100'},
        {label: '创建时间', prop: 'createTime',width:'150'},
      ],  //表头
      selected: [],  //被选中表格数据
      title: "",     // 弹出层标题
      open: false,    // 是否显示新增、编辑弹窗
      openChangeParent: false,  //是否显示新增、编辑弹窗
      // 查询参数
      queryParams: {
        deptName: undefined,
        status: undefined,
        parentId:undefined,
        count:0,
        pageSize:20,
        pageNum:1
      },
      // 表单参数
      form: {status: "0"},
      parentForm: {},
      // 部门树选项
      deptOptions: undefined,
      // 表单校验
      rules: {
        // parentId: [
        //   {required: true, message: "归属部门不能为空", trigger: "blur"}
        // ],
        deptName: [
          {required: true, message: "部门名称不能为空", trigger: "blur"}
        ],
        orderNum: [
          {required: true, message: "排序不能为空", trigger: "blur"},
          {type: 'number', message: '排序必须为数值', trigger: "blur"},
          {pattern: /^[0-9]*$/, message: '排序必须为正整数', trigger: "blur"},
        ],
        // email: [
        //   {
        //     type: "email",
        //     message: "请输入正确的邮箱地址",
        //     trigger: ["blur", "change"]
        //   }
        // ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      parentRules: {
        parentId: [
          {required: true, message: "归属部门不能为空", trigger: "blur"}
        ],
      },
      options: []
    };
  },
  created() {
    this.getList();
    this.getTreeList();
  },
  methods: {
    getTreeList(){
      getAllTree().then(response => {
        this.treeData = groupBy(response.data,"parentId","deptId")
      });
    },
    handleChange(size,num){
      this.queryParams.pageNum = num
      this.queryParams.pageSize = size
      this.getList();
    },
    /** 点击tree树节点 */
    handleNodeClick(data) {
      this.queryParams.parentId = data.deptId
      this.getList();
    },
    /*tree右键添加数据*/
    rightClick(event, object, value, element) {
      this.contextMenuVisible = true;// 让菜单显示
      const ele = document.querySelector('.right-menus');//绑定样式
      ele.style.position = 'fixed';
      ele.style.top = `${event.clientY}px`;
      ele.style.left = `${event.clientX + 10}px`; //根据鼠标点击位置设置菜单出现
    },
    /** 查询部门列表 */
    getList() {
      listDept(this.queryParams).then(response => {
        this.deptList = response.data;
        this.queryParams.count = response.count
        this.loading = false;
      });
    },
    /** 获取字典表状态 */
    statusFormatter(row, column, cellValue, index) {
      // console.log(row, column, cellValue, index)
      if (this.dict.type.sys_normal_disable) {
        let statusArr = this.dict.type.sys_normal_disable;
        let statusLabel = '';
        statusArr.forEach(item => {
          if (cellValue == item.value) {
            // console.log(item.label)
            statusLabel = item.label
          }
        })
        return statusLabel
      }
    },
    /** 选中表格数据 */
    handleSelectionChange(val) {
      this.selected = val;
      // console.log(this.selected)
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },
    /* 翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + $index + 1;
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 新增按钮操作 */
    handleAdd() {
        // this.reset();
        this.open = true;
        this.title = "新增部门";
    },
    /** 编辑按钮操作 */
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
        this.open = true;
        this.title = "编辑部门";
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
          let ids = this.selected.map(item=>{
            return item.deptId
          }).toString()
          delDept(ids).then(res=>{
            this.$message.success(res.msg)
            this.selected = [];
            this.getList();
            this.getTreeList();
          })

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    cascaderClick(nodeData){
      this.$set(this.parentForm,"parentId",nodeData.value)
      this.$refs.cascader.checkedValue = nodeData.value;
      this.$refs.cascader.computePresentText();
    },
    /** 调整部门按钮操作 */
    handleChangeParent() {
      if (!this.selected || this.selected === [] || this.selected.length === 0 ||this.selected.length > 1) {
        this.$message({
          message: '请选择一条数据',
          type: 'warning'
        });
      } else {
        this.$set(this,"parentForm",JSON.parse(JSON.stringify(this.selected[0])))
        this.openChangeParent = true;
      }
    },
    /** 查询部门级联面板结构 */
    getTreeselect() {

    },
    /** 新增、编辑 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deptId != undefined) {
            updateDept(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.getTreeList();
              this.selected = [];
            });
          } else {
            addDept(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.getTreeList();
              this.selected = [];
            });
          }
        }
        this.form = {status: "0"}
      });
    },
    /** 调整部门 提交按钮 */
    submitParentForm() {
      this.$confirm('此操作将永久将调整部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        size: 'mini'
      }).then(() => {
        updateDept(this.parentForm).then(res=>{
          this.$message.success(res.msg)
          this.selected = [];
          this.openChangeParent = false
          this.getList();
          this.getTreeList();
        })
      }).catch(() => {
    Message.info({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.form = {status: "0"}
      // this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0"
      };
      this.parentForm = {
        parentId: undefined,
      }
    },
  }
};
</script>
<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
}

//表单中只有一个item
.onlyOneChild {
  display: flex;
  justify-content: center;
  align-items: center;

  .el-form-item {
    width: 80%;
    justify-content: center;
    margin-right: 0;
  }
}
</style>
