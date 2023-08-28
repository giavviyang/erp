<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn"  style="position: relative">
      <div style="white-space: nowrap;margin-right: 90px;">
        <el-form-item label="操作日期" class="daterange">
          <el-date-picker
            v-model="operationDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            @change="handleQuery">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="操作类型" prop="operationType">
          <el-select v-model="queryParams.operationType" placeholder="请选择" clearable size="mini"  ref="operationType" @change="handleQuery">
            <el-option label="入库" value="入库"></el-option>
            <el-option label="出库" value="出库"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="操作编号" prop="operationNo">
          <el-input
            v-model="queryParams.operationNo"
            placeholder="请输入操作编号"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </div>
      <div style="right:15px;position: fixed;">
          <el-button  type="primary" icon="el-icon-refresh-left"  size="mini" @click="addBack" class="back">返回</el-button>
      </div>
    </el-form>
    <count-table class="rightTable finishedTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                 :pageNum="queryParams.pageNum"
                 :total="total" :summation="summation">
      <el-table highlight-current-row
        :data="operation"
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
        </el-table-column>
        <el-table-column
          v-for="item in operationColumn"
          :key="item.id"
          :label="item.label"
          :prop="item.prop" show-overflow-tooltip :min-width="item.width">
        </el-table-column>
      </el-table>
    </count-table>
    <div class="tabBtn">
      <div style="display: flex;justify-content: space-between">
        <p class="title1" style="padding-left: 10px;">库存信息</p>
<!--        <el-button-->
<!--          type="primary"-->
<!--          :icon="detailSizeIcon"-->
<!--          size="mini"-->
<!--          style="position: absolute;right: 10px"-->
<!--          @click="handleMaximize"-->
<!--        >{{ detailSize }}-->
<!--        </el-button>-->
      </div>
      <slot-table :is-page="false">
        <el-table highlight-current-row
          :data="stockDetails"
          stripe
          border
          style="width: 100%"
          height="100%"
          ref="checkDetailsTable"
          slot="table">
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="item in orderDetailsColumn"
            :key="item.id"
            :label="item.label"
            :prop="item.prop"
            show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </slot-table>
    </div>
  </div>
</template>

<script>
import CountTable from "@/components/public/table/countTable";
import SlotTable from "@/components/public/table/SlotTable";
import {queryOrderProduct, queryWarehouseData, queryWarehouseOrder, warehouseDeliverData} from "@/api/product/finished";
import SummaryTable from "@/components/public/table/summaryTable";
import {keepThreeNum} from "@/utils/order/order";

export default {
  name: "operationRecord",
  components: {SummaryTable, SlotTable, CountTable},
  data() {
    return {
      operationDateRange: [],  //下单日期范围
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        customNo: '',
        customerName: '',
        entryName: '',
        productName: '',
        operationDateEnd: '',
        operationDateStart: '',
      },
      total: 0,
      selected: [],
      //总合计
      summation: [
        {label: 'operationNumber', title: '操作数量', value: 0, unit: '片'},
        {label: 'operationArea', title: '操作面积', value: 0, unit: 'm'},
        {label: 'operationWeight', title: '操作重量', value: 0, unit: 't'},
      ],
      tableSize: '最大化',
      tableSizeIcon: 'el-icon-zoom-in',
      tableFlag: false,
      detailSize: '最大化',
      detailSizeIcon: 'el-icon-zoom-in',
      detailsFlag: false,
      operation: [],  //表格数据
      operationColumn: [
        {label: '操作编号', prop: 'operationNo', width: '150'},
        {label: '操作类型', prop: 'operationType', width: '100'},
        {label: '操作数量（片）', prop: 'operationNumber', width: '120'},
        {label: '操作面积（m²）', prop: 'operationArea', width: '120'},
        {label: '操作重量（t）', prop: 'operationWeight', width: '120'},
        {label: '操作负责人', prop: 'operationPeople', width: '120'},
        {label: '操作日期', prop: 'operationDate', width: '120'},
        {label: '备注', prop: 'remarks'},
      ],
      stockDetails: [],
      orderDetailsColumn: [
        {label: '产品名称', prop: 'productName', width: '300'},
        {label: '位置', prop: 'position', width: '100'},
        {label: '宽（mm）', prop: 'wideHead', width: '110'},
        {label: '高（mm）', prop: 'highHead', width: '110'},
        {label: '订单数量（片）', prop: 'num', width: '100'},
        {label: '操作数量（片）', prop: 'productNumber', width: '150'},
        {label: '操作面积（片）', prop: 'productArea', width: '150'},
        {label: '操作重量（片）', prop: 'productWeight', width: '150'},
        {label: '仓库位置', prop: 'warehouseLocation', width: '100'},
        {label: '货架位置', prop: 'shelfLocation', width: '120'},
      ],
    }
  },
  created() {
    this.handleQuery();

  },
  mounted() {
    //合计行滚动条
    var multipleTable = this.$refs.multipleTable.$refs.bodyWrapper;
    multipleTable.addEventListener('scroll', () => {
      // 滚动距离
      const scrollLeft = multipleTable.scrollLeft
      this.$refs.multipleTable.$refs.headerWrapper.scrollLeft = scrollLeft
    })
    var checkDetailsTable = this.$refs.checkDetailsTable.$refs.bodyWrapper;
    checkDetailsTable.addEventListener('scroll', () => {
      // 滚动距离
      const scrollLeft1 = checkDetailsTable.scrollLeft
      // this.$refs.checkDetailsTable.$refs.bodyWrapper.scrollLeft = scrollLeft1
      this.$refs.checkDetailsTable.$refs.headerWrapper.scrollLeft = scrollLeft1
    })
  },
  methods: {
    // 是否显示下拉框
    isShowSelectOptions(isShowSelectOptions) {
      if (!isShowSelectOptions) {
        this.$refs.operationType.blur()
      }
    },
    handleQuery() {
      if (this.operationDateRange) {
        this.queryParams.operationDateStart = this.operationDateRange[0];
        this.queryParams.operationDateEnd = this.operationDateRange[1];
      } else {
        this.queryParams.operationDateStart = '';
        this.queryParams.operationDateEnd = '';
      }
      queryWarehouseData(this.queryParams).then(res => {
        this.operation = res.data;
        this.total=res.count;
        if(this.operation){
          this.summation[0].value = 0
          this.summation[1].value = 0
          this.summation[2].value = 0
          this.operation.forEach(item=>{
            this.summation.forEach(sumItem => {
              if (item.hasOwnProperty(sumItem.label)) {
                sumItem.value += item[sumItem.label]
              }
              sumItem.value = keepThreeNum(sumItem.value);
            })
          })
          warehouseDeliverData({id: res.data[0].id}).then(res => {
            this.stockDetails = res.data;
          })
        }
      })
    },
    // /* 重置 */
    // resetQuery() {
    //   this.operationDateRange = [];
    //   this.receiptDateRange = [];
    //   this.queryParams = {
    //     pageNum: 1,
    //     pageSize: 20,
    //     customNo: '',
    //     customerName: '',
    //     entryName: '',
    //     id: '',
    //     finishedNo: '',
    //     finishedType: '',
    //     operationDateEnd: '',
    //     operationDateStart: '',
    //     productionStatus: '',
    //   };
    //   // this.handleQuery();
    //   // this.getOrderDetails();
    //   let detailsTable = document.querySelector('.tabBtn');
    //   let finishedTable = document.querySelector('.finishedTable');
    //   detailsTable.style.display = 'block';
    //   detailsTable.style.height = 'calc(50% - 150px)';
    //   finishedTable.style.height = '50%';
    //   this.tableSize = '最大化';
    //   this.tableSizeIcon = 'el-icon-zoom-in';
    //   this.tableFlag = false;
    //   this.detailSize = '最大化';
    //   this.detailSizeIcon = 'el-icon-zoom-in';
    //   this.detailsFlag = false;
    // },
    /* 分页器 */
    handleChange(pageSize, pageNum) {
      console.log(pageSize, pageNum)
      // this.pageSize = pageSize;
      // this.pageNum = pageNum;
      this.queryParams.pageSize = this.pageSize;
      this.queryParams.pageNum = this.pageNum;
      this.handleQuery();
    },
    /* 选中数据 */
    handleSelectionChange(val) {
      // console.log('this.selected',val.slice(-1))
      // 清除 所有勾选项
      if (val.length === 1) {
        this.selected = val;
      }
      if (val.length > 1) {
        this.$refs.multipleTable.clearSelection();
        this.$refs.multipleTable.toggleRowSelection(val.at(-1), true);
        this.selected = val.slice(-1);
      }
      if (val.length ===0) {
        this.selected=[];
      }
    },
    /* 翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 行点击事件 */
    handleRowClick(row, column, event) {
      console.log(row)
      this.$refs.multipleTable.toggleRowSelection(row, column)
      warehouseDeliverData({id: row.id}).then(res => {
        this.stockDetails = res.data;
      })
    },
    /* 最大化弹窗 */
    handleMaximize() {
      let detailsTable = document.querySelector('.tabBtn');
      let finishedTable = document.querySelector('.finishedTable');
      if (this.detailsFlag) {
        this.detailSizeIcon = 'el-icon-zoom-in';
        this.detailSize = '最大化';
        detailsTable.style.height = 'calc(50% - 150px)';
        finishedTable.style.height = '50%';
        // this.handleSearch(this.queryParams);
      }
      if (!this.detailsFlag) {
        this.detailSizeIcon = 'el-icon-zoom-out';
        this.detailSize = '还原';
        detailsTable.style.height = 'calc(65% - 150px)';
        finishedTable.style.height = '35%';
      }
      this.detailsFlag = !this.detailsFlag;
    },
    /* 合计行方法 */
    getSummariesOrder(param) {
      const {columns, data} = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (column.property === 'orderNum' || column.property === 'totalArea' || column.property === 'orderAmount' || column.property === 'totalWeigh'
          || column.property === 'productAmount' || column.property === 'productWeight' || column.property === 'num' || column.property === 'lengthen' || column.property === 'productArea') {
          sums[index] = keepThreeNum(values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0));
          sums[index] += '';
        } else {
          sums[index] = '';
        }
      });
      return sums;
    },
    /* 返回主页面 */
    addBack() {
      this.$router.go(-1);
    },
  },
}
</script>

<style lang="scss" scoped>
::v-deep .finishedTable {
  margin-top: 10px;
  height: 50%;

  .noReceiptDate {
    .cell {
      color: red;
    }
  }

  .partReceiptDate {
    .cell {
      color: #FF9232;
    }
  }

  .el-icon-check, .icon-dkw_wancheng {
    color: #22AC38
  }

  .el-icon-close {
    color: #FF0000
  }

  .icon-weiwancheng {
    color: #0066DC
  }

  i, .iconfont {
    font-weight: 600;
  }
}

.tabBtn {
  //margin-top: 5px;
  width: 100%;
  height: calc(50% - 60px);
  position: relative;

  ::v-deep .rightTable {
    margin-top: 5px;

    .table {
      height: 100%;
    }

    .page {
      display: none;
    }
  }
}
</style>
