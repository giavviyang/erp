<template>
  <el-dialog title="流程卡导入" :visible.sync="exportFlowDialog" width="75%" class="dialog-style exportProductList"
             :close-on-click-modal="false" :close-on-press-escape="false" append-to-body  :before-close="backFlowList">
    <el-form :model="exportFlowParams" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="流程卡号">
        <el-input @keyup.enter.native="handleFlow" v-model="exportFlowParams.flowCardNo" clearable placeholder="请输入流程卡号"/>
      </el-form-item>
      <el-form-item label="订单编号">
        <el-input @keyup.enter.native="handleFlow" v-model="exportFlowParams.orderNo" clearable placeholder="请输入订单编号"/>
      </el-form-item>
      <el-form-item label="项目名称">
        <el-input @keyup.enter.native="handleFlow" v-model="exportFlowParams.entryName" clearable placeholder="请输入项目名称"/>
      </el-form-item>
      <el-form-item label="客户名称">
        <el-input @keyup.enter.native="handleFlow" v-model="exportFlowParams.customerName" clearable placeholder="请输入客户名称"/>
      </el-form-item>
      <el-form-item style="width: 30px;min-width: 30px;margin-left: 20px;">
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFlow" :disabled="loading">{{loading?'搜索中':'搜索'}}</el-button>-->
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFlow" >搜索</el-button>
      </el-form-item>
    </el-form>
    <slot-table class="productList" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total" >
      <el-table highlight-current-row
        :data="productList"
        border
        stripe
        height="100%"
        style="width: 100%"
        ref="productListTable"
        @selection-change="handleProductList"
        @row-click="RowClickProductList"
        slot="table">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          type="index"
          :index="getProductIndex"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="(item,index) in flowDataColumns"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="addFlowList">添加</el-button>
         <el-button size="mini" @click="backFlowList">返回</el-button>
      </span>
  </el-dialog>
</template>

<script>
import SlotTable from "@/components/public/table/SlotTable";

export default {
  name: "flowDialog",
  components: {SlotTable},
  props:{
    exportFlowDialog:{
      type:Boolean,
      default:false,
    },
    productList:{
      type:Array,
      default: []
    },
    loading:Boolean,
    pageSize:{
      type:Number,
      default:20
    },
    pageNum:{
      type:Number,
      default:1
    },
    total:{
      type:Number,
      default:0
    }
  },
  data(){
    return{
      //导入流程卡产品信息查询条件
      exportFlowParams: {
        flowCardNo:"",
        orderNo:"",
        entryName:"",
        customerName:""
      },
      pageSizeProduct: 20,
      pageNumProduct: 1,
      totalProduct: 0,
      productSelect:[],
      customerNameList: [],
      flowDataColumns: [
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '项目名称', prop: 'entryName', width: '200'},
        {label: '自定义编号', prop: 'customNo', width: '150'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '半产品名称', prop: 'itemName', width: '150'},
        {label: '规格', prop: 'specs', width: '120'},
        {label: '总面积（m²）', prop: 'totalArea', width: '120'},
        {label: '数量（片）', prop: 'itemNum', width: '150'},
      ],
    }
  },
  mounted() {
  },
  methods:{
    // 是否显示下拉框
    isShowSelectOptions(isShowSelectOptions) {
      if (!isShowSelectOptions) {
        this.$refs.customerName.blur();
      }
    },
    /* 导入订单产品信息查询 */
    handleFlow(val) {
      this.$emit('handleFlow',this.exportFlowParams, val);
      // listOrder(this.exportFlowParams).then(response => {
      //   // console.log(response)
      //   if (response.code === 200) {
      //     this.productList = response.data;
      //     // JSON.parse(JSON.stringify(this.sizeCheckColumn.slice(0)));
      //     this.total = response.count;
      //   }
      // });
    },
    /* 导入产品信息分页器 */
    handleChange(size, num) {
      this.$emit('handlePage',{size, num});
    },
    /* 产品信息 翻页后序号连贯 */
    getProductIndex($index) {
      //  表格序号
      return (this.pageNumProduct - 1) * this.pageSizeProduct + $index + 1;
    },
    /* 选择导入产品信息数据 */
    handleProductList(val) {
      this.productSelect = val;
    },
    /* 导入产品信息行点击事件 */
    RowClickProductList(row, column, event) {
      this.$refs.productListTable.toggleRowSelection(row, column)
    },
    /* 添加导入产品信息 */
    addFlowList(val) {
      this.$emit('addFlowList',this.productSelect, val);
    },
    /* 取消导入产品信息 */
    backFlowList(val) {
      this.$emit('backFlowList',this.productSelect, val);
    },
  }
}
</script>

<!--<style lang="scss" scoped>-->
<!--.exportProductList {-->
<!--  ::v-deep .el-dialog {-->
<!--    height: calc(60vh);-->

<!--    .el-dialog__body {-->
<!--      height: calc(100% - 90px);-->
<!--      padding-bottom: 0;-->

<!--      .productList {-->
<!--        height: calc(100% - 100px);-->
<!--        min-height: 150px;-->
<!--        margin: 10px 0;-->
<!--      }-->

<!--      .iptAndBtn {-->
<!--        height: auto;-->
<!--        min-height: 48px;-->
<!--        max-height: 80px;-->
<!--        flex-wrap: wrap;-->
<!--        //overflow: auto;-->

<!--        .el-form-item {-->
<!--          //min-width: 300px;-->
<!--          margin-bottom: 5px;-->
<!--          //.el-form-item__label {-->
<!--          //  width: 100px;-->
<!--          //}-->
<!--        }-->
<!--      }-->
<!--    }-->
<!--  }-->
<!--}-->


<!--</style>-->
