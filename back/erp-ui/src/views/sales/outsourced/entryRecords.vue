<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn" style="position: relative">
      <div style="white-space: nowrap;margin-right: 90px;">
        <el-form-item label="入库日期" class="daterange">
          <el-date-picker
            v-model="entryDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            @change="handleQuery">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="入库单号" prop="warehousedNo">
          <el-input
            v-model="queryParams.warehousedNo"
            placeholder="请输入入库单号"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <el-form-item label="外协单号" prop="purchaseNo">
          <el-input
            v-model="queryParams.outsourcingNo"
            placeholder="请输入外协单号"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <el-form-item label="入库负责人" prop="warehousedPerson">
          <el-input
            v-model="queryParams.warehousedPerson"
            placeholder="请输入入库负责人"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </div>
      <div style="right:15px;position: fixed">
          <el-button type="primary"  icon="el-icon-refresh-left"  size="mini" @click="entryBack">返回</el-button>
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
        @click="handleDelete">删除入库
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-printer"
        size="mini"
        @click="handlePrint">打印
      </el-button>
      <el-dropdown>
        <el-button type="primary" size="mini">
          导出<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="handleExport(0)">导出入库单</el-dropdown-item>
          <el-dropdown-item @click.native="handleExport(1)">导出入库单明细</el-dropdown-item>
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
import {queryData, detailedView, delData} from "@/api/salse/warehousing"
import {keepThreeNum} from "@/utils/order/order";

export default {
  name: "entryRecords",
  components: {SlotTable, CountTable},
  data(){
    return{
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        warehousedNo:'',
        warehousedPerson:'',
        outsourcingNo:'',
        warehousedDateStart:'',
        warehousedDateEnd:'',
      },
      total:0,
      entryDateRange: [],
      selected: [],
      tableDate: [
      ],
      tableColumn: [
        {label: '入库单号', prop: 'warehousedNo', width: '120'},
        {label: '外协单号', prop: 'outsourcingNo', width: '120'},
        {label: '外协单位', prop: 'outsourcingUnit', width: '120'},
        {label: '外协负责人', prop: 'outsourcingLeader', width: '120'},
        {label: '外协日期', prop: 'outgoingDate', width: '120'},
        {label: '入库负责人', prop: 'warehousedPerson', width: '100'},
        {label: '入库数量（片）', prop: 'warehousedNum', width: '120'},
        {label: '入库日期', prop: 'warehousedDate', width: '120'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      //总合计
      summation: [
        {label: 'warehousedNum', title: '入库数量', value: 0, unit: '片'},
      ],
      detailDialog: false,
      detailsInfo: [
        {title: '入库单号', value: ''},
        {title: '外协单号', value: ''},
        {title: '外协单位', value: ''},
        {title: '入库时间', value: ''},
        {title: '入库人', value: ''},
        {title: '入库数量（片）', value: ''},
        {title: '备注', value: ''},
      ],  //明细
      detailDialogData: [],
      detailDialogColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '120'},
        {label: '项目名称', prop: 'entryName', width: '120'},
        {label: '产品名称', prop: 'productName', width: '120'},
        {label: '位置', prop: 'position', width: '120'},
        {label: '宽（mm）', prop: 'wideHead', width: '100'},
        {label: '高（mm）', prop: 'highHead', width: '100'},
        {label: '外协数量（片）', prop: 'warehousingNum', width: '100'},
      ],
    }
  },
  created() {
    this.handleQuery();
  },
  methods:{
    /* 查询入库单 */
    handleQuery() {
      if (this.entryDateRange) {
        this.queryParams.warehousedDateStart = this.entryDateRange[0];
        this.queryParams.warehousedDateEnd = this.entryDateRange[1];
      }
      queryData(this.queryParams).then(res => {
        this.tableDate = res.data;
        this.total=res.count;
        if (this.tableDate) {
          this.summation=[
            {label: 'warehousedNum', title: '入库数量', value: 0, unit: '片'},
          ];
          this.tableDate.forEach(item => {
            this.summation.forEach(sumItem => {
              if (item.hasOwnProperty(sumItem.label)) {
                sumItem.value += item[sumItem.label]
              }
              sumItem.value = keepThreeNum(sumItem.value);
            })
          })
        }
      })
    },
    /* 返回 */
    entryBack(){
      this.$router.go(-1);
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
        let ids = []
        this.selected.forEach(res => {
          ids.push(res.id)
        })
        this.detailDialog = true;
        this.detailsInfo[0].value = this.selected[0].warehousedNo
        this.detailsInfo[1].value = this.selected[0].outsourcingNo
        this.detailsInfo[2].value = this.selected[0].outsourcingUnit
        this.detailsInfo[3].value = this.selected[0].warehousedDate
        this.detailsInfo[4].value = this.selected[0].warehousedPerson
        this.detailsInfo[5].value = this.selected[0].warehousedNum
        this.detailsInfo[6].value = this.selected[0].remarks
        detailedView({ids: ids.toString()}).then(res => {
          this.detailDialogData = res.data;
        })
      }
    },
    /*删除记录*/
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
          delData({ids: ids.toString()}).then(res => {
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
        this.download('sales/warehousing/exportData', {
          ids: ids.toString()
        }, `外协入库记录单.xlsx`)
      } else if (param === 1) {//导出产品
        this.download('sales/warehousing/exportDataDetailed', {
          ids: ids.toString()
        }, `外协入库记录单明细.xlsx`)
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
  }
}
</script>

<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
}
</style>
