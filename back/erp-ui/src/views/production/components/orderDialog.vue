<template>
  <el-dialog title="订单导入" :visible.sync="exportOrderDialog" width="75%" class="dialog-style exportProductList"
             :close-on-click-modal="false" :close-on-press-escape="false" append-to-body  :before-close="backOrderList">
    <el-form :model="exportOrderParams" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="订单编号">
        <el-input @keyup.enter.native="handleOrder" v-model="exportOrderParams.orderNo" clearable placeholder="请输入订单编号"/>
      </el-form-item>
      <el-form-item label="自定义编号">
        <el-input @keyup.enter.native="handleOrder" v-model="exportOrderParams.customNo" clearable placeholder="请输入自定义编号"/>
      </el-form-item>
      <el-form-item label="项目名称">
        <el-input @keyup.enter.native="handleOrder" v-model="exportOrderParams.entryName" clearable placeholder="请输入项目名称"/>
      </el-form-item>
      <el-form-item label="客户名称">
        <el-input @keyup.enter.native="handleOrder" v-model="exportOrderParams.customerName" clearable placeholder="请输入客户名称"/>
      </el-form-item>
      <el-form-item style="width: 30px;min-width: 30px;margin-left: 20px;">
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleOrder" :disabled="loading">{{loading?'搜索中':'搜索'}}</el-button>-->
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleOrder">搜索</el-button>
      </el-form-item>
    </el-form>
    <slot-table class="productList" @handleChange="handleChange" :pageSize="exportOrderParams.pageSize" :pageNum="exportOrderParams.pageNum"
                :total="total">
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
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="(item,index) in orderDataColumns"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="addOrderList">添加</el-button>
         <el-button size="mini" @click="backOrderList">返回</el-button>
      </span>
  </el-dialog>
</template>

<script>
import {Message} from "element-ui";
import SlotTable from "@/components/public/table/SlotTable";

export default {
  name: "orderDialog",
  components: {SlotTable},
  props:{
    exportOrderDialog:{
      type:Boolean,
      default:false,
    },
    productList:{
      type:Array,
      default: []
    },
    loading:Boolean,
    // pageSize:{
    //   type:Number,
    //   default:20
    // },
    // pageNum:{
    //   type:Number,
    //   default:1
    // },
    total:{
      type:Number,
      default:0
    }
    // exportOrderParams:{
    //   type:Object,
    //   default:{
    //     orderNo: '',
    //       customNo: '',
    //       customerName: '',
    //       entryName: '',
    //   }
    // }
  },
  data(){
    return{
      //导入产品信息查询条件
      exportOrderParams: {
        orderNo: '',
        customNo: '',
        customerName: '',
        entryName: '',
        pageNum: 1,
        pageSize: 20
      },
      productSelect:[],
      customerNameList: [],
      orderDataColumns: [
        // {label: '流程卡编号', prop: 'flowCardNo', width: '150'},
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '项目名称', prop: 'entryName', width: '200'},
        {label: '自定义编号', prop: 'customNo', width: '150'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '位置', prop: 'position', width: '150'},
        {label: '宽(mm)', prop: 'wideHead', width: '150'},
        {label: '高(mm)', prop: 'highHead', width: '150'},
        {label: '产品总数(片)', prop: 'num', width: '150'},
        {label: '完工数量(片)', prop: 'completionNum', width: '150'},
        // {label: '半产品名称', prop: 'itemName', width: '150'},
        {label: '已入库数量(片)', prop: 'warehousingQuantity', width: '120'},
      ],
      loading:false
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
    handleOrder(val) {
      this.$emit('handleOrder',this.exportOrderParams, val);
    },
    handleChange(size, num){
      this.$emit('handlePage',{size, num});
      this.exportOrderParams.pageNum = num;
      this.exportOrderParams.pageSize = size;
      this.handleOrder()
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
    addOrderList() {
      this.$emit('addOrderList',this.productSelect, this.productList);
    },
    /* 取消导入产品信息 */
    backOrderList(val) {
      this.$emit('backOrderList',this.productSelect, val);
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
