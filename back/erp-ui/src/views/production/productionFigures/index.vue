<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="产品名称">
        <el-input
          placeholder="请输入产品名称"
          v-model="queryParams.product_name"
          clearable  @keyup.enter.native="handleQuery">
        </el-input>
      </el-form-item>
      <el-form-item label="完工日期" class="daterange">
        <el-date-picker
          v-model="queryParams.date"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd" @change="handleQuery">
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item>-->
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="primary" size="mini" @click="handleExport" v-hasPermi="['system:production_count:export']"><i class="iconfont icon-daochuwenjian"></i>导出</el-button>
        <el-button type="primary" icon="el-icon-s-order" size="mini" @click="handleDetails" v-hasPermi="['system:production_count:info']">查看明细</el-button>
<!--      </el-form-item>-->
    </el-form>
    <slot-table class="figuresTable">
      <el-table highlight-current-row
        :data="figuresList"
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
          v-for="(item,index) in figuresColumns"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <!--  查看明细弹窗  -->
    <el-dialog title='查看明细'
               :visible.sync="detailDialog" width="80%" class="dialog-style detailDialog"
               :close-on-click-modal="false" :close-on-press-escape="false">
<!--      <div class="detailsInfo">-->
<!--        <p v-for="(item,index) in detailsInfo" :key="index">{{ item.title }}：<span>{{ item.value }}</span></p>-->
<!--      </div>-->
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
import SlotTable from "@/components/public/table/SlotTable";
import {Message} from "element-ui";
import {getCountList,getCountInfo} from "@/api/product/productCount";

export default {
  name: "index",
  components: {SlotTable},
  data(){
    return{
      queryParams: {
        product_name: ""
      },
      figuresDateRange: [], //排产日期范围
      figuresList: [],  //统计单列表
      //统计表头
      figuresColumns: [
        {label: '产品名称', prop: 'product_name'},
        {label: '生产数量（片）', prop: 'num'},
        {label: '生产面积（m²）', prop: 'area'}
      ],
      selected: [],  //统计单复选框
      detailDialog: false,  //明细弹窗
      //明细信息
      // detailsInfo: [
      //   {title: '补片流程卡号', value: ''},
      // ],
      detailDialogData: [],  //明细数据
      //补片明细表头
      detailDialogColumns: [
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '订单编号', prop: 'orderNo', width: '150'},
        {label: '自定义编号', prop: 'customNo', width: '100'},
        {label: '客户名称', prop: 'customerName', width: '100'},
        {label: '项目名称', prop: 'entryName', width: '150'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '单片名称', prop: 'monolithicName', width: '150'},
        {label: '工艺流程', prop: 'processContent', width: '200'},
        {label: '数量（片）', prop: 'splitNum', width: '100'},
        {label: '面积（m²）', prop: 'totalArea', width: '100'}
      ],
    }
  },
  created() {
    this.handleQuery();
  },
  mounted() {
   // this.keyupSubmit();
  },
  methods:{
    //键盘按下enter搜索事件
    keyupSubmit() {
      document.onkeydown = e => {
        const _key = window.event.keyCode
        if (_key === 13) {
          this.handleQuery();
        }
      }
    },
    /* 各种查询条件查询 */
    handleQuery() {
      if (this.queryParams.date) {
        this.queryParams.startDate = this.queryParams.date[0]
        this.queryParams.endDate = this.queryParams.date[1]
      }else {
        this.queryParams.startDate=undefined;
        this.queryParams.endDate=undefined;
      }
      let params = JSON.parse(JSON.stringify(this.queryParams))
      delete params.date
      getCountList(params).then(res=>{
        this.figuresList = res.data
      })
    },
    /* 导出 */
    handleExport(){
      let ids = this.selected.map(item=>{
        return item.id
      })
      this.download('produce/production/count/export', {
        ids: ids.toString()
      }, `生产统计.xlsx`)
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
      if (this.selected.length === 1) {
        getCountInfo(this.selected[0].id).then(res=>{
          this.detailDialogData = res.data
          this.detailDialog = true;
        })
      }
    },
    /* 统计单分页器 */
    handleChange(size, num) {
      // this.pageSize = size;
      // this.pageNum = num;
      this.queryParams.pageNum = num;
      this.queryParams.pageSize = size;
      this.handleQuery();
    },
    /* 统计复选框 */
    handleSelectionChange(val) {
      this.selected = val;
    },
    /* 行点击事件 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },
  }
}
</script>

<style lang="scss" scoped>
.figuresTable {
  height: calc(100% - 70px);
  margin-top: 10px;
  ::v-deep .table{
    height: 100%;
  }
 ::v-deep .page{
    display: none;
  }
}
::v-deep .detailDialog {
  .el-dialog {
    height: calc(60vh);
    padding: 0;
    .detailsTable {
      height: 100%;
    }

  }
}

</style>
