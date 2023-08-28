<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn" style="position: relative">
      <div style="white-space: nowrap;margin-right: 90px;">
        <el-form-item label="盘库日期" class="daterange">
          <el-date-picker
            v-model="checkNumDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            @change="handleQuery">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="盘库编号" prop="operationNo">
          <el-input
            v-model="queryParams.operationNo"
            placeholder="请输入盘库编号"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <el-form-item label="盘库人" prop="person">
          <el-input
            v-model="queryParams.person"
            placeholder="请输入盘库人"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </div>
      <div style="right:15px;position: fixed">

        <el-button type="primary" icon="el-icon-refresh-left"  size="mini" @click="checkNumBack">返回</el-button>
      </div>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-s-order"
        size="mini"
        @click="handleDetails"
      >查看明细
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete">删除盘库
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-printer"
        size="mini"
        @click="handlePrint">打印
      </el-button>
      <el-dropdown>
        <el-button type="primary" size="mini">
          <i class="iconfont icon-daochuwenjian"></i> 导出<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="handleExport(0)"><i class="iconfont icon-daochuwenjian"></i>导出盘库单</el-dropdown-item>
          <el-dropdown-item @click.native="handleExport(1)"><i class="iconfont icon-daochuwenjian"></i>导出盘库单明细</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
    <count-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                 :pageNum="queryParams.pageNum" :total="queryParams.count" :summation="summation">
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
  </div>
</template>

<script>

import CountTable from "@/components/public/table/countTable";
import {Message} from "element-ui";
import SlotTable from "@/components/public/table/SlotTable";
import {warehouseRecord, warehouseDetailsView, delInventoryDetails} from "@/api/accessories/accessoriesWarehouse";
import {keepThreeNum, keepOneNum} from "@/utils/order/order";

export default {
  name: "checkNumRecords",
  components: {SlotTable, CountTable},
  data() {
    return {
      queryParams: {
        operationType: '盘库',
        operationDateEnd: '',
        operationDateStart: '',
        operationNo: '',
        pageNum: 1,
        pageSize: 20,
        person: ''
      },
      checkNumDateRange: [],
      selected: [],
      tableDate: [],
      tableColumn: [
        {label: '盘库编号', prop: 'operationNo', width: '120'},
        {label: '盘库数量', prop: 'originalNum', width: '120'},
        {label: '盘库金额（元）', prop: 'totalAmount', width: '120'},
        {label: '盘库人', prop: 'person', width: '100'},
        {label: '盘库日期', prop: 'operationDate', width: '120'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      //总合计
      summation: [
        {label: 'originalNum', title: '盘库总数量', value: 0,},
        {label: 'totalAmount', title: '盘库总金额', value: 0, unit: '元'},
      ],
      detailDialog: false,
      detailsInfo: [
        {title: '盘库编号',label:'operationNo', value: ''},
        {title: '盘库时间', label:'operationDate',value: ''},
        {title: '盘库人', label:'person',value: ''},
        {title: '总数量',label:'originalNum', value: ''},
        {title: '总金额（元）', label:'totalAmount',value: ''},
        {title: '备注', label:'remarks',value: ''},
      ],  //明细
      detailDialogData: [],
      detailDialogColumns: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
        {label: '厂家名称', prop: 'accessoriesMill', width: '150'},
        {label: '辅料类型', prop: 'typeName', width: '120'},
        {label: '盘库数量', prop: 'operationNum', width: '120'},
        {label: '盘库单价（元/m²）', prop: 'operationPrice', width: '150'},
      ],
    }
  },
  created() {
    this.handleQuery();
  },
   methods: {

    /* 查询盘库单 */
    handleQuery() {
      if (this.checkNumDateRange) {
        this.queryParams.operationDateStart = this.checkNumDateRange[0];
        this.queryParams.operationDateEnd = this.checkNumDateRange[1];
      }else {
        this.queryParams.operationDateStart ='';
        this.queryParams.operationDateEnd ='';
      }
      warehouseRecord(this.queryParams).then(res => {
        this.tableDate = res.data;
        this.queryParams.operationType= '盘库';
        this.queryParams.count=res.count;
        if(this.tableDate){
          this.summation= [
            {label: 'originalNum', title: '盘库总数量', value: 0,},
            {label: 'totalAmount', title: '盘库总金额', value: 0, unit: '元'},
          ];
          this.tableDate.forEach(item=>{
            this.summation.forEach(sumItem => {
              if (item.hasOwnProperty(sumItem.label)) {
                sumItem.value += item[sumItem.label]
              }
              sumItem.value = keepThreeNum(sumItem.value);
              if (sumItem.label === "totalAmount") {
                sumItem.value = keepOneNum(sumItem.value);
              }
            })
          })
        }
      })
    },
    /* 返回 */
    checkNumBack() {
      this.$router.go(-1);
    },
    /* 查看明细 */
    handleDetails() {
      if (this.selected.length != 1) {
        Message.warning("请选择一条数据进行查看");
        return false
      }
      this.detailDialog = true;
      if (this.selected.length == 1) {
        this.detailDialog = true;
        this.detailsInfo[0].value = this.selected[0].operationNo
        this.detailsInfo[1].value = this.selected[0].millId
        this.detailsInfo[2].value = this.selected[0].person
        this.detailsInfo[3].value = this.selected[0].originalNum
        this.detailsInfo[4].value = this.selected[0].totalAmount
        this.detailsInfo[5].value = this.selected[0].remarks
        warehouseDetailsView({id: this.selected[0].id}).then(res => {
          this.detailDialogData = res.data;
        })
      }
    },
    /*删除记录*/
    handleDelete() {
      if (this.selected.length !== 1) {
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
          delInventoryDetails({id: this.selected[0].id}).then(res => {
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
    /* 导出记录*/
    handleExport(param) {
      let ids = [];
      if (this.selected.length > 0) {
        this.selected.forEach(item => {
          ids.push(item.id)
        })
      }
      if (param === 0) {//导出订单
        this.download('accessories/operation/exportWarehouse', {
          ids: ids.toString()
        }, `辅料盘库记录单.xlsx`)
      } else if (param === 1) {//导出产品
        this.download('accessories/operation/exportWarehouseDetails', {
          ids: ids.toString()
        }, `辅料盘库记录单明细.xlsx`)
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
  }
}
</script>

<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
}

//::v-deep .iptAndBtn {
//  overflow: auto;
//
//  .el-form-item {
//    width: 250px;
//    min-width: 225px;
//    margin-right: 0;
//
//    .el-form-item__label {
//      width: 80px;
//    }
//
//    .el-form-item__content {
//      width: 150px;
//    }
//  }
//
//  .daterange {
//    min-width: 320px;
//
//    .el-range-editor {
//      width: 240px;
//
//      .el-range-input:last-of-type {
//        margin-left: 5px;
//      }
//    }
//  }
//}

//::v-deep .detailDialog{
//  .detailsInfo{
//    justify-content: flex-start !important;
//  }
//}
</style>
