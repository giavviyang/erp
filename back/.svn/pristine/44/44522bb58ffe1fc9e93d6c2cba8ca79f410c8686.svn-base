<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="采购日期" class="daterange">
        <el-date-picker
          v-model="purchaseDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          @change="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="采购单号" prop="purchaseNo">
        <el-input
          v-model="queryParams.purchaseNo"
          placeholder="请输入采购单号"
          @keyup.enter.native="handleQuery"
          clearable/>
      </el-form-item>
      <el-form-item label="供应商" prop="millName">
        <el-input
          v-model="queryParams.millName"
          placeholder="请输入供应商"
          @keyup.enter.native="handleQuery"
          clearable/>
      </el-form-item>
      <el-form-item label="采购负责人" prop="purchasePerson">
        <el-input
          v-model="queryParams.purchasePerson"
          placeholder="请输入采购负责人"
          @keyup.enter.native="handleQuery"
          clearable/>
      </el-form-item>
      <!--      <el-form-item>-->
      <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      <!--      </el-form-item>-->
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-s-order"
        size="mini"
        @click="handleDetails"
        v-hasPermi="['accessories:purchase:detail']"
      >查看明细
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['accessories:purchase:add']"
      >新增采购
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['accessories:purchase:edit']"
      >编辑采购
      </el-button>
      <el-button
        type="primary"
        size="mini"
        icon="el-icon-delete"
        @click="handleDelete"
        v-hasPermi="['accessories:purchase:del']">删除采购
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-printer"
        size="mini"
        v-hasPermi="['accessories:purchase:print']"
        @click="handlePrint">打印
      </el-button>
      <el-dropdown>
        <el-button type="primary" size="mini" v-hasPermi="['accessories:purchase:export']">
          <i class="iconfont icon-daochuwenjian"></i>导出<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="handleExport(0)"><i class="iconfont icon-daochuwenjian"></i>导出采购单</el-dropdown-item>
          <el-dropdown-item @click.native="handleExport(1)"><i class="iconfont icon-daochuwenjian"></i>导出采购单明细</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
    <count-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                 :pageNum="queryParams.pageNum" :total="total" :summation="summation">
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
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          v-for="(item,index) in tableColumn"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :min-width="item.width">
        </el-table-column>
      </el-table>
    </count-table>
    <!--  查看明细弹窗  -->
    <el-dialog title='查看明细'
               :visible.sync="detailDialog" width="80%" class="dialog-style detailDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <div class="detailsInfo">
        <p v-for="(item,index) in detailsInfo" :key="index">{{ item.title }}：<span>{{ item.value }}</span></p>
      </div>
      <slot-table class="detailsTable">
        <el-table highlight-current-row
          :data="detailDialogData"
          border
          stripe
          height="100%"
          style="width: 100%" slot="table">
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in detailDialogColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width"
            show-overflow-tooltip></el-table-column>
        </el-table>
      </slot-table>
    </el-dialog>
    <!--  新增采购  -->
    <el-dialog :title="dialogType==='add'?'新增采购':dialogType==='edit'?'编辑采购':''"
               :visible.sync="addDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title">采购信息</p>
        <div class="addInfoContainer">
          <el-form :model="addParams" size="mini" :inline="true" ref="addForm" class="ipt2">
            <el-form-item label="采购单号" prop="purchaseNo">
              <el-input
                disabled
                v-model="addParams.purchaseNo"
                placeholder="请输入采购单号"
                clearable/>
            </el-form-item>
            <el-form-item label="供应商" prop="millName">
              <el-input
                v-model="addParams.millName"
                placeholder="请输入供应商"
                clearable/>
            </el-form-item>
            <el-form-item label="采购负责人" prop="purchasePerson">
              <el-input
                v-model="addParams.purchasePerson"
                placeholder="请输入采购负责人"
                clearable/>
            </el-form-item>
            <el-form-item label="采购日期" prop="purchaseDate">
              <el-date-picker
                v-model="addParams.purchaseDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                size="mini"
                v-model="addParams.remarks"
                placeholder="请输入内容"
                clearable/>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="btnTable">
        <div class="btn">
          <el-button
            type="primary"
            icon="el-icon-folder-add"
            size="mini"
            @click="handleExportProduct">导入辅料
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
            :data="accessoriesList"
            border
            stripe
            height="100%"
            style="width: 100%"
            ref="productTable"
            slot="table">
            <!--            <el-table-column-->
            <!--              type="selection"-->
            <!--              width="55">-->
            <!--            </el-table-column>-->
            <el-table-column
              type="index"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              v-for="(item,index) in addAccessoriesColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              show-overflow-tooltip></el-table-column>
            <el-table-column label="采购数量" width="200">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.operationNum" :min="1" :precision="0"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="采购单价" width="200">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.referencePrice" :min="1"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="总金额（元）" width="150">
              <template slot-scope="scope">
             {{ keepThreeNum(Number(scope.row.operationNum) * Number(scope.row.referencePrice)) }}
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="120" class-name="operation">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="deleteRow(scope.$index, accessoriesList)"
                  type="text"
                  size="mini">
                  移除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveAdd('addForm')">保存</el-button>
        <el-button size="mini" @click="addDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  选择辅料  -->
    <accessories-dialog :selectAccessoriesDialog="selectAccessoriesDialog"
                        ref="accessoriesDialog" @saveSelectAccessories="saveSelectAccessories"
                        @cancelSelectAccessories="cancelSelectAccessories"
                        class="accessoriesDialog"></accessories-dialog>
  </div>
</template>

<script>

import SlotTable from "@/components/public/table/SlotTable";
import {Message} from "element-ui";
import CountTable from "@/components/public/table/countTable";
import {HyperFormula} from "hyperformula";
import {keepOneNum, keepThreeNum, getCurrentDay, sum} from "@/utils/order/order";
import {
  productionNumber,
  queryPurchaseData,
  addPurchaseData,
  viewDetail,
  updPurchaseData,
  delPurchaseData,
  exportPurchase,
  exportPurchaseData,
  updPurchaseDataQuery
} from "@/api/accessories/accessoriesPurchase";
import AccessoriesDialog from "@/views/accessories/components/accessoriesDialog";

export default {
  name: "index",
  components: {AccessoriesDialog, CountTable, SlotTable},
  data() {
    return {
      queryParams: {
        purchaseDateEnd: '',
        purchaseDateStart: '',
        purchaseNo: '',
        purchasePerson: '',
        millName: '',

        // accessoriesMill: '',
        pageNum: 1,
        pageSize: 20,
      },
      total: 1,
      purchaseDateRange: [],
      selected: [],
      tableDate: [],
      tableColumn: [
        {label: '采购单号', prop: 'purchaseNo', width: '120'},
        {label: '供应商', prop: 'millName', width: '120'},
        {label: '采购负责人', prop: 'purchasePerson', width: '100'},
        {label: '采购日期', prop: 'purchaseDate', width: '180'},
        {label: '总数量', prop: 'purchaseNum', width: '120'},
        // {label: '总面积（m²）', prop: 'purchaseArea', width: '120'},
        {label: '总金额（元）', prop: 'purchaseAmount', width: '120'},
        {label: '入库状态', prop: 'status', width: '120'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      //总合计
      summation: [
        {label: 'purchaseNum', title: '总数量', value: 0,},
        {label: 'purchaseAmount', title: '总金额', value: 0, unit: '元'},
      ],
      detailDialog: false,
      detailsInfo: [
        {title: '采购单号',label:'purchaseNo',  value: ''},
        {title: '供应商',label:'millName',  value: ''},
        {title: '总数量',label:'purchaseNum',  value: ''},
        {title: '总金额（元）',label:'purchaseAmount',  value: ''},
        {title: '入库状态',label:'status',  value: ''},
        {title: '采购负责人', label:'purchasePerson', value: ''},
        {title: '采购日期', label:'purchaseDate', value: ''},
        {title: '备注', label:'remarks', value: ''},
      ],  //明细
      detailDialogData: [],
      detailDialogColumns: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '120'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
       {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '厂家名称', prop: 'accessoriesMill', width: '150'},
        {label: '辅料类型', prop: 'typeName', width: '120'},
        {label: '数量', prop: 'purchaseNum', width: '100'},
        {label: '采购价', prop: 'purchasePrice', width: '100'},
        {label: '金额（元）', prop: 'purchaseAmount', width: '100'},
      ],
      dialogType: 'add',
      addDialog: false, //新增采购
      editDialog: false, //编辑采购
      accessoriesList: [],
      addAccessoriesColumns: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '120'},
        // {label: '长度（mm）', prop: 'accessoriesLong', width: '100'},
        // {label: '宽度（mm）', prop: 'accessoriesWidth', width: '100'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
       {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '厂家名称', prop: 'accessoriesMill', width: '150'},
        // {label: '辅料编号', prop: 'accessoriesNo', width: '120'},
        {label: '辅料类型', prop: 'typeName', width: '120'},
        // {label: '数量', prop: 'operationNum', width: '100'},
        // {label: '采购价', prop: 'purchaseUnitPrice', width: '100'},
        // {label: '金额（元）', prop: 'purchaseAmount', width: '100'},
      ],
      addParams: {
        purchaseNo: '',
        purchaseDate: getCurrentDay(),
        millName: '',
        remarks: '',
        purchasePerson: '',
        accessoriesList: []
      },
      selectAccessoriesDialog: false,
      addFormRules: {
        purchaseNo: [
          {required: true, message: '请输入采购单号', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        millName: [
          {required: true, message: '请输入供应商', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        purchaseDate: [
          {required: true, message: '采购日期不能为空', trigger: 'blur'}
        ],
        purchasePerson: [
          {required: true, message: '采购负责人不能为空', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
      },
    }
  },
  created() {
    this.handleQuery();
  },
   methods: {

    /* 查询采购单 */
    handleQuery() {
      if (this.purchaseDateRange) {
        this.queryParams.purchaseDateStart = this.purchaseDateRange[0];
        this.queryParams.purchaseDateEnd = this.purchaseDateRange[1];
      }else {
        this.queryParams.purchaseDateStart ='';
        this.queryParams.purchaseDateEnd = '';
      }
      queryPurchaseData(this.queryParams).then(res => {
        // console.log(res)
        if (res.code === 200) {
          this.tableDate = res.data;
          this.total = res.count;
          if(this.tableDate){
            this.summation= [
              {label: 'purchaseNum', title: '总数量', value: 0,},
              {label: 'purchaseAmount', title: '总金额', value: 0, unit: '元'},
            ];
            this.tableDate.forEach(item=>{
              item.purchaseAmount=keepThreeNum(item.purchaseAmount)
              this.summation.forEach(sumItem => {
                if (item.hasOwnProperty(sumItem.label)) {
                  sumItem.value += item[sumItem.label]
                }
                sumItem.value = keepThreeNum(sumItem.value);
                if (sumItem.label === "purchaseAmount") {
                  sumItem.value = keepOneNum(sumItem.value);
                }
              })
            })
          }
        }
      })
    },
    /* 查看明细 */
    handleDetails() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要查看的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行查看");
        return false
      }
      if (this.selected.length == 1) {
        this.detailDialog = true;
        this.detailsInfo.forEach(item => {
          if (this.selected[0].hasOwnProperty(item.label)) {
            item.value = this.selected[0][item.label]
          }
        })
        viewDetail({id: this.selected[0].id}).then(res => {
          this.detailDialogData = res.data;
          this.detailDialogData.forEach(item=>{
            item.purchaseAmount=keepThreeNum(item.purchaseAmount)
          })
        })
      }
    },
    /* 新增采购 */
    handleAdd() {
      this.dialogType = 'add';
      this.addDialog = true;
      this.addParams = {
        purchaseNo: '',
        purchaseDate: getCurrentDay(),
        // accessoriesMill: '',
        millName: '',
        purchasePerson: '',
        remarks: '',
      };
      productionNumber().then(res => {
        this.addParams.purchaseNo = res.msg;
      })
      this.accessoriesList = [];
    },
    /* 编辑采购 */
    handleUpdate() {
      if (!this.selected || this.selected === [] || this.selected.length === 0) {
        Message.warning("请选择要修改的数据");
      } else if (this.selected.length !== 1) {
        Message.warning("请选择一条数据进行修改");
      } else {
        if (this.selected[0].status != "未入库") {
          Message.warning("请选择未入库的采购信息！")
          return;
        }
        this.dialogType = 'edit';
        this.addDialog = true;
        this.accessoriesList = []
        this.addParams = JSON.parse(JSON.stringify(this.selected[0]));
        // this.accessoriesList= JSON.parse(JSON.stringify(this.selected[0].accessoriesList));
        updPurchaseDataQuery({id: this.selected[0].id}).then(res => {
          this.accessoriesList = res.data;
          // this.saveSelectAccessories(res.data, 1);
        })
      }
    },
    /* 导入辅料 */
    handleExportProduct() {
      this.selectAccessoriesDialog = true
    },
    /* 移除辅料 */
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    /*删除采购*/
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
          let ids = [];
          for (let i = 0; i < this.selected.length; i++) {
            if (this.selected[i].status != "未入库") {
              Message.warning("请选择未入库的采购信息！")
              return;
            }
            ids.push(this.selected[i].id)
          }
          delPurchaseData({ids: ids.toString()}).then(res => {
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
    /* 导出采购 */
    handleExport(param) {
      let ids = [];
      if (this.selected.length > 0) {
        this.selected.forEach(item => {
          ids.push(item.id)
        })
      }
      if (param === 0) {//导出订单
        this.download('accessories/purchase/exportPurchase', {
          ids: ids.toString()
        }, `采购单.xlsx`)
      } else if (param === 1) {//导出产品
        this.download('accessories/purchase/exportPurchaseData', {
          ids: ids.toString()
        }, `采购单明细.xlsx`)
      }
    },
    /* 打印 */
    handlePrint() {

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
    /* 保存选择辅料数据 */
    saveSelectAccessories(val, tableData) {
      if (!val || val.length <= 0) {
        Message.warning("请选择一条数据进行添加");
        return false
      }
      if (val.length >= 1) {
        for (let i = 0; i < val.length; i++) {
          var index = tableData.indexOf(val[i])
          for (let j = 0; j < this.accessoriesList.length; j++) {
            if (val[i].id == this.accessoriesList[j].id){
              Message.warning("序号为" + (index + 1) + "的数据已经添加,不可重复添加！");
              return;
            }
          }
        }
        this.accessoriesList.push(...val);
        Message.success("添加成功");
        this.selectAccessoriesDialog = false;
        this.$refs.accessoriesDialog.$refs.templateTableRef.clearSelection();
      }
    },
    /* 取消选择辅料数据 */
    cancelSelectAccessories() {
      this.selectAccessoriesDialog = false;
      this.$refs.accessoriesDialog.$refs.templateTableRef.clearSelection();
    },
    saveAdd(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.accessoriesList.length <= 0) {
            Message.warning("请导入辅料信息进行采购");
          } else {
            this.addParams.accessoriesList = this.accessoriesList;
            if (this.dialogType === 'add') {
              addPurchaseData(this.addParams).then(res => {
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
              this.addDialog = false;
            }
            if (this.dialogType === 'edit') {
              updPurchaseData(this.addParams).then(res => {
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
              this.addDialog = false;
            }
          }
        } else {
          return false;
        }
      });
    },

  }
}
</script>

<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
}

::v-deep .addDialog {
  height: 100%;

  .el-dialog {
    height: calc(70vh);

    .el-dialog__body {
      height: calc(100% - 90px);

      .ipt2 {
        padding: 0;
        height: 50px;
        overflow: auto;

        .el-form-item {
          width: 280px;
        }

        .el-form-item:first-of-type {
          margin-left: -30px;
        }
      }

      .handTable {
        margin-top: 15px;
        height: calc(100% - 65px);
        overflow: hidden;
        border: 1px solid #ccc;
        //border-top: 1px solid #ccc;
        td {
          text-align: center;
        }

        .ht_master {
          border-bottom: 1px solid #ccc;
        }
      }
    }
  }
}

//::v-deep .accessoriesDialog {
//  height: 100%;
//
//  .el-dialog {
//    height: calc(65vh);
//
//    .el-dialog__body {
//      height: calc(100% - 90px);
//      padding: 0;
//    }
//  }
//}

</style>
