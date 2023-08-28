<template>
  <div class="app-container">
    <main-flexible-tree-right-click :data="treeData"
                                    :defaultProps="TreeDefaultProps"
                                    @handleNodeClick="handleNodeClick"
                                    @rightClick="rightClick">
      <VueContextMenu
        :target="contextMenuTarget"
        :show="contextMenuVisible"
        class="right-menus"
        @update:show="(show) => contextMenuVisible = show" slot="RightClick">
        <a @click="addType" v-hasPermi="['accessories:definition:addType']">新建类型</a>
        <a @click="editType" v-hasPermi="['accessories:definition:editType']">编辑类型</a>
        <a @click="delType" v-hasPermi="['accessories:definition:delType']">删除类型</a>
      </VueContextMenu>
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
          <el-form-item label="辅料名称" prop="accessoriesName">
            <el-input
              v-model="queryParams.accessoriesName"
              placeholder="请输入辅料名称"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item label="辅料编号" prop="accessoriesNo">
            <el-input
              v-model="queryParams.accessoriesNo"
              placeholder="请输入辅料编号"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item label="型号规格" prop="accessoriesSpecifications">
            <el-input
              v-model="queryParams.accessoriesSpecifications"
              placeholder="请输入型号规格"
              @keyup.enter.native="handleQuery"
              clearable/>
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
            v-hasPermi="['accessories:definition:add']"
            @click="handleAdd"
          >新增辅料
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            v-hasPermi="['accessories:definition:edit']"
            @click="handleUpdate"
          >编辑辅料
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            v-hasPermi="['accessories:definition:del']"
            @click="handleDelete"
          >删除辅料
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-hasPermi="['accessories:definition:export']"
            @click="handleExport"
          ><i class="iconfont icon-daochuwenjian"></i>导出辅料
          </el-button>
        </div>
        <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                    :pageNum="queryParams.pageNum" :total="total">
          <el-table highlight-current-row
            :data="tableDate"
            stripe
            border
            height="100%"
            ref="templateTableRef"
            @selection-change="handleSelectionChange"
            @row-click="handleRowClick" slot="table">
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
              show-overflow-tooltip
              v-for="item in tableColumn"
              :key="item.id"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width">
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </main-flexible-tree-right-click>
    <el-dialog :title="typeDialogType=='add'?'新增辅料类型':typeDialogType=='edit'?'编辑辅料类型':''"
               :visible.sync="typeDialog" width="500px" class="dialog-style typeDialog" :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <el-form :model="typeForm" :rules="typeFormRules" ref="typeFormRef" label-width="100px">
        <el-form-item label="上级类型" prop="tidName">
          <!--          <el-input style="width: 350px" v-model="typeForm.tidName" disabled></el-input>-->
          <span v-if="typeDialogType == 'add'">{{
              currentRightNodeInfo ? currentRightNodeInfo.accessoriesTypeName : '无'
            }}</span>
          <span v-else>{{ typeForm.tidName }}</span>
        </el-form-item>
        <el-form-item label="类型名称" prop="accessoriesTypeName">
          <el-input style="width: 350px" v-model="typeForm.accessoriesTypeName" placeholder="请输入类型名称"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="typeSubmit('typeFormRef')">保存</el-button>
        <el-button size="mini" @click="typeDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="dialogType=='add'?'新增辅料':dialogType=='edit'?'编辑辅料':''" :visible.sync="dialog"
               width="50%" class="dialog-style dialogType" :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <el-form :model="accessoriesForm" ref="accessoriesFormRef" class="ipt2">
        <el-form-item label="辅料名称" prop="accessoriesName">
          <el-input v-model="accessoriesForm.accessoriesName" placeholder="请输入辅料名称" size="mini" clearable></el-input>
        </el-form-item>
        <el-form-item label="辅料编号" prop="accessoriesNo">
          <el-input v-model="accessoriesForm.accessoriesNo" placeholder="请输入辅料编号" size="mini" clearable></el-input>
        </el-form-item>
        <el-form-item label="辅料类型" prop="typeId">
          <el-cascader
            placeholder="请选择辅料类型"
            v-model="accessoriesForm.typeId"
            @change="cascaderChange"
            :props="{value:'id',label:'accessoriesTypeName'}"
            :show-all-levels="false"
            :options="treeData" clearable size="mini">
          </el-cascader>
        </el-form-item>
        <el-form-item label="计数单位" prop="accessoriesCompany">
          <el-input v-model="accessoriesForm.accessoriesCompany" placeholder="请输入计数单位" size="mini" clearable></el-input>
        </el-form-item>
        <el-form-item label="厂家名称" prop="accessoriesMill">
          <el-input v-model="accessoriesForm.accessoriesMill" placeholder="请选择厂家名称" size="mini" @focus="handleBlur"
                    ref="accessoriesMillInput" clearable></el-input>
        </el-form-item>
        <el-form-item label="型号规格" prop="accessoriesSpecifications">
          <el-input v-model="accessoriesForm.accessoriesSpecifications" placeholder="请输入型号规格" size="mini" clearable></el-input>
        </el-form-item>
        <el-form-item label="参考采购价（元）" prop="referencePrice">
          <el-input-number type="number" v-model="accessoriesForm.referencePrice" placeholder="请输入参考采购价（元）" size="mini"
                    clearable></el-input-number>
        </el-form-item>
        <el-form-item label="库存预警" prop="alarmInventory">
          <el-input-number type="number" v-model="accessoriesForm.alarmInventory" placeholder="请输入库存预警" size="mini"
                    clearable></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remarks" class="remarks">
          <el-input
            type="textarea"
            v-model="accessoriesForm.remarks" size="mini" clearable  placeholder="请输入内容">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button type="primary" size="mini" @click="saveAccessories('accessoriesFormRef')">保存</el-button>
            <el-button size="mini" @click="dialog = false;accessoriesForm={}">取消</el-button>
          </span>
    </el-dialog>
    <mill-dialog :selectMillDialog="selectMillDialog"
                 @saveSelectMill="saveSelectMill"
                 @cancelSelectMill="cancelSelectMill"
                 ref="millDialog"></mill-dialog>
  </div>
</template>

<script>

import {component as VueContextMenu} from '@xunlei/vue-context-menu';
import MainFlexibleTreeRightClick from "@/components/public/MainBody/MainFlexibleTreeRightClick";
import SlotTable from "@/components/public/table/SlotTable";
import {
  queryTree,
  addAccessoriesType,
  updAccessoriesType,
  delAccessoriesType,
  queryAccessoriesFilm, addAccessoriesFilm, updAccessoriesFilm, delAccessoriesFilm,
} from "@/api/accessories/accessoriesFilm";
import {groupBy} from "@/utils/groupBy";
import {Message} from "element-ui";
import MillDialog from "@/views/accessories/components/millDialog";

export default {
  name: "index",
  components: {MillDialog, SlotTable, MainFlexibleTreeRightClick, VueContextMenu},
  data() {
    return {
      treeData: [], //左侧tree树
      TreeDefaultProps: {
        children: 'children',
        label: 'accessoriesTypeName'
      },
      contextMenuTarget: '',
      contextMenuVisible: false,
      currentRightNodeInfo: {},
      topNodeInfo: {},
      typeForm: {
        accessoriesTypeName: "",
        // tid: this.currentRightNodeInfo.id,
      },
      queryParams: {
        alarmInventory: '',
        id: "",
        accessoriesMill: "",
        operationNum: '',
        accessoriesCompany: "",
        // accessoriesGrade: "",
        // accessoriesLong: '',
        accessoriesName: "",
        accessoriesNo: "",
        accessoriesSpecifications: '',
        typeId: "",
        typeName: "",
        // accessoriesWidth: '',
        pageNum: 1,
        pageSize: 20,
        referencePrice: '',
        remarks: "",
        // stock: '',
        totalAmount: '',
        unitPrice: '',
      },
      total: 1,
      tableDate: [],
      selected: [],
      tableColumn: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '100'},
        {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
        {label: '厂家名称', prop: 'accessoriesMill', width: '150'},
        {label: '辅料类型', prop: 'typeName', width: '120'},
        {label: '参考采购单价（元/m²）', prop: 'referencePrice', width: '150'},
        {label: '库存预警', prop: 'alarmInventory', width: '120'},
        {label: '录入日期', prop: 'createdAt', width: '180'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      typeDialogType: 'add',  //类型弹窗
      typeDialog: false,
      dialogType: 'add', //新增、编辑辅料
      dialog: false,
      currentNodeInfo: {},
      accessoriesForm: {
        alarmInventory: '',
        accessoriesMill: "",
        accessoriesCompany: "",
        // accessoriesGrade: "",
        // accessoriesLong: '',
        accessoriesName: "",
        accessoriesNo: "",
        accessoriesSpecifications: '',
        typeId: "0",
        typeName: "",
        // accessoriesWidth: '',
        referencePrice: '',
        remarks: "",
        // stock: '',
      },
      selectMillDialog: false,  //选择厂家弹窗
      typeFormRules: {
        accessoriesTypeName: [
          {required: true, message: '请输入类型名称', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ]
      },
      /*accessoriesFormRules: {
        accessoriesName: [
          {required: true, message: '请输入辅料名称', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        accessoriesNo: [
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        typeId: [
          {required: true, message: '请选择辅料类型', trigger: 'blur'},
        ],
        accessoriesCompany: [
          {required: true, message: '请输入计数单位', trigger: 'blur'},
          {min:1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur"},
          {pattern:/^[\u0391-\uFFE5]+$/, message: '请输入汉字'},
        ],
        accessoriesMill: [
          // {required: true, message: "厂家名称不能为空", trigger: "blur"},
          {max: 20, message: '字符长度最大不得超过20', trigger: 'blur'}
        ],
        referencePrice:[
          {pattern: /^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/g, message: '请输入大于0的整数或小数'}
        ]
      }*/
    }
  },
  created() {
    this.getTreeData();
    this.handleQuery();
  },
  // mounted() {
  //   // // this.keyupSubmit();
  // },
   methods: {

    /* 左侧tree查询 */
    getTreeData() {
      queryTree().then(res => {
        console.log(res)
        if (res.code === 200) {
          // this.treeData=res.data;
          this.treeData = [{id: "0", accessoriesTypeName: "辅料类型", children: groupBy(res.data, "tid")}];
          if (this.treeData) {
            this.treeData.forEach(item => {
              item.typeId = item.id
            })
          }
        }
      })
    },
    /* 左侧tree树单击节点 */
    handleNodeClick(val, node, component) {
      console.log(val, node, component);
      this.currentNodeInfo = val;
      this.queryParams.typeId = val.id;
      this.handleQuery();
    },
    /* 左侧tree右击 */
    rightClick(event, object, value, element) {
      // console.log(event, object, value, element)
      this.currentRightNodeInfo = object;
      if (!Array.isArray(value.parent.data)) {
        this.topNodeInfo = value.parent.data
      }
      this.contextMenuVisible = true;// 让菜单显示
      const ele = document.querySelector('.right-menus');//绑定样式
      ele.style.position = 'fixed';
      ele.style.top = `${event.clientY}px`;
      ele.style.left = `${event.clientX + 10}px`; //根据鼠标点击位置设置菜单出现
    },
    /* 新增类型节点 */
    addType() {
      this.typeDialogType = 'add';
      this.contextMenuVisible = false;
      this.typeDialog = true;
      this.typeForm = {
        accessoriesTypeName: "",
        tid: this.currentRightNodeInfo.id,
        tidName: this.currentRightNodeInfo.accessoriesTypeName
      };
    },
    /* 编辑类型节点 */
    editType() {
      this.contextMenuVisible = false;
      if (this.currentRightNodeInfo.id == 0) {
        Message.warning('无法操作根节点');
      } else {
        this.typeDialogType = 'edit';
        this.typeDialog = true;
        this.typeForm = JSON.parse(JSON.stringify(this.currentRightNodeInfo));
        // console.log(this.topNodeInfo)
        this.typeForm.tidName = this.topNodeInfo.accessoriesTypeName;
      }
    },
    /* 提交类型表单 */
    typeSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.typeDialogType === 'add') {
            addAccessoriesType(this.typeForm).then(res => {
              console.log(res)
              if (res.code === 200) {
                Message.success(res.msg);
                this.getTreeData();
              } else {
                Message.error(res.msg);
                this.getTreeData();
              }
            })
            this.typeDialog = false;

          }
          if (this.typeDialogType === 'edit') {
            updAccessoriesType(this.typeForm).then(res => {
              console.log(res)
              if (res.code === 200) {
                Message.success(res.msg);
                this.getTreeData();
                // this.getTreeData();
              } else {
                Message.error(res.msg);
                this.getTreeData();
              }
              this.typeDialog = false;

            })
          }
        } else {
          return false;
        }
      });
    },
    /* 删除节点 */
    delType() {
      this.contextMenuVisible = false;
      if (this.currentRightNodeInfo.id == 0) {
        Message.warning('无法操作根节点');
      } else {
        this.$confirm('此操作将删除选中数据，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          delAccessoriesType({id: this.currentRightNodeInfo.id}).then(res => {
            if (res.code === 200) {
              Message.success(res.msg);
              this.getTreeData();
            } else {
              Message.error(res.msg);
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    /* 查询辅料数据 */
    handleQuery() {
      // console.log(this.queryParams)
      queryAccessoriesFilm(this.queryParams).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.tableDate = res.data;
          this.total = res.count;
        }
      })
    },
    /* 新增辅料 */
    handleAdd() {
      //判断顶级节点下是否有类型
      if (this.treeData[0].children.length <= 0 ) {
        Message.warning("在辅料类型下请先新增子类型！")
        return;
      }
      this.dialogType = 'add';
      this.dialog = true;
      this.accessoriesForm = {
        alarmInventory: '',
        accessoriesMill: "",
        accessoriesCompany: "",
        accessoriesName: "",
        accessoriesNo: "",
        accessoriesSpecifications: '',
        typeId: "",
        typeName: "",
        referencePrice: '',
        remarks: "",
      };
      let type = [];
      if (this.currentNodeInfo.id && this.currentNodeInfo.id != 0 && this.currentNodeInfo.children) {
        if (this.currentNodeInfo.children) {
          type = this.currentNodeInfo.children
          do {
            if (type[0].children){
              type = type[0].children
            }
            this.accessoriesForm.typeId = type[0].id;
            this.accessoriesForm.typeName=type[0].accessoriesName;
          }while (type[0].children);
        }else {
          this.accessoriesForm.typeId = this.currentNodeInfo.id;
          this.accessoriesForm.typeName=this.currentNodeInfo.accessoriesTypeName;
        }
      }else {
        type = JSON.parse(JSON.stringify(this.treeData))
        do {
          if (type[0].children){
            type = type[0].children
          }
          this.accessoriesForm.typeId = type[0].id;
          this.accessoriesForm.typeName=type[0].accessoriesName;
        }while (type[0].children);
      }
    },
    /* 编辑辅料 */
    handleUpdate() {
      console.log(this.selected)
      if (!this.selected || this.selected === [] || this.selected.length === 0) {
        Message.warning("请选择要修改的数据");
      } else if (this.selected.length !== 1) {
        Message.warning("请选择一条数据进行修改");
      } else {
        this.dialogType = 'edit';
        this.dialog = true;
        this.accessoriesForm = JSON.parse(JSON.stringify(this.selected[0]));
      }
    },
    /* 选择厂家 */
    handleBlur() {
      this.selectMillDialog = true;
    },
    /* 保存选择厂家弹窗 */
    saveSelectMill(val) {
      if (!val || val.length <= 0) {
        Message.warning("请选择一条数据进行添加");
        return false
      }
      if (val.length === 1) {
        this.selectMillDialog = false;
        this.accessoriesForm.accessoriesMill = val[0].millName;
        this.$refs.millDialog.$refs.multipleMillTable.clearSelection();
        this.$refs.accessoriesMillInput.blur();
      }
    },
    /* 取消选择客户弹窗 */
    cancelSelectMill() {
      this.selectMillDialog = false;
      this.$refs.millDialog.$refs.multipleMillTable.clearSelection();
    },
    cascaderChange(val) {
      console.log(val)
    },
    /* 保存新增、编辑弹窗 */
    saveAccessories(formName) {
      // console.log( this.accessoriesForm)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.accessoriesForm.typeId =  Array.isArray(this.accessoriesForm.typeId)?this.accessoriesForm.typeId[this.accessoriesForm.typeId.length-1]:this.accessoriesForm.typeId;
          if (this.dialogType === 'add') {
            addAccessoriesFilm(this.accessoriesForm).then(res => {
              if (res.code === 200) {
                Message.success(res.msg);
                this.handleQuery();
              } else {
                Message.error(res.msg);
              }
            })
            this.dialog = false;
          }
          if (this.dialogType === 'edit') {
            updAccessoriesFilm(this.accessoriesForm).then(res => {
              if (res.code === 200) {
                Message.success(res.msg);
                this.handleQuery();
              } else {
                Message.error(res.msg);
              }
              this.dialog = false;
            })
          }
        } else {
          return false;
        }
      });
    },
    /* 删除辅料 */
    handleDelete() {
      if (!this.selected || this.selected === [] || this.selected.length === 0) {
        this.$message({
          message: '请至少选择一条数据',
          type: 'warning'
        });
      } else {
        this.$confirm('此操作将删除选中数据，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          let ids = []
          this.selected.forEach(item => {
            ids.push(item.id)
          })
          delAccessoriesFilm({ids: ids.toString()}).then(res => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              this.handleQuery();
            } else {
              this.$message({
                type: 'error',
                message: res.msg
              });
            }
          })
          this.selected = [];
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    /* 导出辅料 */
    handleExport(){
      let ids = [];
      if (this.selected.length > 0) {
        this.selected.forEach(item => {
          ids.push(item.id)
        })
      }
      this.download('/accessories/definition/exportAccessoriesFilm', {
        ids: ids.toString()
      }, `辅料数据.xlsx`)
    },
    /*分页器*/
    handleChange(size, num) {
      this.queryParams.pageNum = num;
      this.queryParams.pageSize = size;
      this.handleQuery();
    },
    /*  勾选表格复选框*/
    handleSelectionChange(val) {
      this.selected = val;
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.templateTableRef.toggleRowSelection(row, column)
    },
     /* 翻页后序号连贯 */
     getIndex($index) {
       //  表格序号
       return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + $index + 1;
     },
  },
}
</script>

<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
}

.typeDialog {
  ::v-deep .el-dialog {
    min-height: 5%;
  }
}

.dialogType {
  ::v-deep .ipt2 {
    width: 100%;
    //justify-content: center;
    .el-form-item {
      width: 48%;
      min-width: 300px;

      .el-form-item__label {
        width: 150px;
      }

      .el-form-item__content {
        width: calc(100% - 150px);
      }
    }

    .remarks {
      .el-textarea {
        top: -5px;
      }
    }
  }
}
</style>
