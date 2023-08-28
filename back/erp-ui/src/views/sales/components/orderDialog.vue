<template>
  <el-dialog title="导入订单产品" :visible.sync="exportDialog" width="75%" class="dialog-style exportList"
             :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true" append-to-body>
    <el-form :model="exportParams" size="mini" :inline="true" class="iptAndBtn1">
      <el-form-item label="订单编号">
        <el-input @keyup.enter.native="handleProduct" v-model="exportParams.orderNo" clearable/>
      </el-form-item>
      <el-form-item label="自定义编号">
        <el-input @keyup.enter.native="handleProduct" v-model="exportParams.customNo" clearable/>
      </el-form-item>
      <el-form-item label="客户名称">
        <el-select ref="customerName" @keyup.enter.native="handleProduct" @keydown.enter.native="disableVisible"
                   v-model="exportParams.customerName" filterable placeholder="请选择客户" clearable>
          <el-option
            v-for="(item,index) in customerNameList"
            :key="index"
            :label="item.customerName"
            :value="item.customerName">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="项目名称">
        <el-input @keyup.enter.native="handleProduct" v-model="exportParams.entryName" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleProduct">搜索</el-button>
      </el-form-item>
    </el-form>
    <!--      <slot-table class="tableList" @handleChange="handleChangeDeliver" :pageSize="pageSizeProduct"-->
    <!--                  :pageNum="pageNumProduct"-->
    <!--                  :total="totalProduct">-->
    <slot-table class="tableList"  @handleChange="handleChangeDeliver" :pageSize="exportParams.pageSize" :pageNum="exportParams.pageNum"  :total="total">
      <el-table highlight-current-row
        :data="productList"
        border
        stripe
        height="100%"
        style="width: 100%"
        ref="productListTable"
        @selection-change="handleProductList"
        @row-click="rowClickProductList"
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
        <el-button type="primary" size="mini" @click="addProductList">添加</el-button>
         <el-button size="mini" @click="backProductList">返回</el-button>
      </span>
  </el-dialog>
</template>

<script>

import {Message} from "element-ui";
import SlotTable from "@/components/public/table/SlotTable";
import {queryPackProduct} from "@/api/salse/order/pack";
import {customerList} from "@/api/system/customer";

export default {
  name: "orderDialog",
  components: {SlotTable},
  props:{
    exportDialog:{
      type:Boolean,
      default:false,
    },
    exportParams:{
      type:Object,
      default:{
        orderNo: '',
          customNo: '',
          customerName: '',
          entryName: '',
        pageNum:1,
        pageSize:20,
      }
    }
  },
  data(){
    return{
      customerNameList:[],
      total:0,
      productList:[],
      selectedProductList:[],
      orderDataColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '项目名称', prop: 'entryName', width: '200'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '宽（mm）', prop: 'wideHead', width: '110'},
        {label: '高（mm）', prop: 'highHead', width: '110'},
        {label: '产品数量（片）', prop: 'num', width: '110'},
        {label: '已入库数量（片）', prop: 'warehousingQuantity', width: '120'},
      ],
    }
  },
  computed: {
    disableVisible() {
      this.$refs.customerName.visible = false
    }
  },
  mounted() {
    let param = {
      pagenum: 1,
      pageSize: 10000
    }
    customerList(param).then(res => {
      this.customerNameList = res.data;
    })
    this.handleProduct();
  },
  methods:{
    /* 导入产品信息查询 */
    handleProduct() {
      queryPackProduct(this.exportParams).then(res => {
        this.productList = res.data;
        this.total = res.count;
      })
    },
    /* 导入产品信息分页器 */
    handleChangeDeliver(size, num) {
      // this.pageSizeProduct = size;
      // this.pageNumProduct = num;
      this.exportParams.pageNum = num;
      this.exportParams.pageSize = size;
      this.handleProduct();
    },
    /* 产品信息 翻页后序号连贯 */
    getProductIndex($index) {
      //  表格序号
      return ( this.exportParams.pageNum - 1) * this.exportParams.pageSize + $index + 1;
    },
    /* 选择导入产品信息数据 */
    handleProductList(val) {
      this.selectedProductList = val;
    },
    /* 导入产品信息行点击事件 */
    rowClickProductList(row, column, event) {
      this.$refs.productListTable.toggleRowSelection(row, column)
    },
    /* 添加导入产品信息 */
    addProductList(val) {
      console.log(val)
      this.$emit('addProductList',this.selectedProductList, val);
      // if (this.selectedProductList.length <= 0) {
      //   Message.warning("请选择要添加的数据");
      // } else {
      //   this.entryProduct.push(...this.selectedProductList);
      //   Message.success("导入产品信息成功");
      //   this.$refs.productListTable.clearSelection();
      // }

    },
    /* 取消导入产品信息 */
    backProductList(val) {
      this.$emit('backProductList',this.productSelect, val);
      // this.exportDialog = false;
      // this.$refs.productListTable.clearSelection();
    },
  }
}
</script>
<style lang="scss" scoped>



</style>
