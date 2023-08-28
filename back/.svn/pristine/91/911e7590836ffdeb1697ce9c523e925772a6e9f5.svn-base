<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="下单日期" class="daterange">
        <el-date-picker
          v-model="preparationDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd" @change="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        size="mini"
        v-hasPermi="['sales:warehouse:enter']"
        @click="handleEntry"
      ><i class="iconfont icon-ruku1"></i>操作入库
      </el-button>
      <el-button
        type="primary"
        size="mini"
        @click="handleOutbound"
        v-hasPermi="['sales:warehouse:out']"><i class="iconfont icon-chuku"></i>操作出库
      </el-button>
      <el-button
        type="primary"
        size="mini"
        v-hasPermi="['sales:warehouse:queryWarehouseData']"
        @click="handleOperation"><i class="iconfont icon-churukujilu"></i>操作记录
      </el-button>
      <el-dropdown @command="toPrint">
        <el-button type="primary" size="mini" icon="el-icon-printer" v-hasPermi="['sales:warehouse:print']">
          打印<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <!-- 跳转打印页面时 传入模板id-->
          <el-dropdown-item command="a8da4c6d0aba714a11242324edb4d981" icon="el-icon-printer">打印</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-dropdown>
        <el-button type="primary" size="mini" v-hasPermi="['sales:warehouse:export']">
          <i class="iconfont icon-daochuwenjian"></i>导出<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="exportOrder(0)"><i class="iconfont icon-daochuwenjian"></i>导出成品</el-dropdown-item>
          <el-dropdown-item @click.native="exportOrder(1)"><i class="iconfont icon-daochuwenjian"></i>导出成品明细</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-button
        type="primary"
        :icon="tableSizeIcon"
        size="mini"
        style="position: absolute;right: 10px"
        @click="changeTableHeight"
      >{{ tableSize }}
      </el-button>
    </div>
    <count-table class="rightTable finishedTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                 :pageNum="queryParams.pageNum"
                 :total="total" :summation="summation">
      <el-table highlight-current-row


        :data="finishedList"
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
          :index="getIndex"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="item in finishedListColumn"
          :key="item.id"
          :label="item.label"
          show-overflow-tooltip>
          <el-table-column :prop="item.prop" show-overflow-tooltip :min-width="item.width">
            <template #header scoped-slot="scope">
              <!--可根据类型来显示为搜索框、下拉框等-->
              <el-input v-if="item.type==='ipt'"
                        v-model="queryParams[item.prop]"
                        size="mini"
                        placeholder="请输入"
                        clearable @keyup.enter.native="handleQuery"/>
<!--              <el-select v-if="item.type=='select'" v-model="queryParams[item.prop]" placeholder='请选择' clearable-->
<!--                         size="mini">-->
<!--              </el-select>-->
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </count-table>
    <div class="tabBtn">
      <div style="display: flex;justify-content: space-between">
        <p class="title1" style="padding-left: 5px;">库存信息</p>
        <el-button
          type="primary"
          :icon="detailSizeIcon"
          size="mini"
          style="position: absolute;right: 10px"
          @click="handleMaximize"
        >{{ detailSize }}
        </el-button>
      </div>
      <summary-table class="rightTable">
        <el-table highlight-current-row
          :data="stockDetails"
          stripe
          border
          style="width: 100%"
          height="100%"
          ref="checkDetailsTable"
          show-summary
          :summary-method="getSummariesOrder"
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
            show-overflow-tooltip :prop="item.prop">
          </el-table-column>
        </el-table>
      </summary-table>
    </div>
    <!--  操作入库  -->
    <el-dialog title='操作入库'
               :visible.sync="productEntryDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <div class="addInfo">
        <p class="title1">入库信息</p>
        <div class="addInfoContainer">
          <el-form :model="productParams" ref="productForm" size="mini" :inline="true" class="ipt2"
                   :rules="productFormRules">
            <el-form-item label="入库单号" prop="operationNo">
              <el-input
                v-model="productParams.operationNo"
                disabled
              />
            </el-form-item>
            <el-form-item label="负责人" prop="operationPeople">
              <el-input
                v-model="productParams.operationPeople"
                placeholder="请输入负责人"
                clearable/>
            </el-form-item>
            <el-form-item label="入库日期" prop="operationDate">
              <el-date-picker
                v-model="productParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="productParams.remarks" size="mini" clearable placeholder="请输入内容">
              </el-input>
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
            @click="handleNoEntry">导入订单
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
            :data="entryProduct"
            border
            stripe
            height="100%"
            style="width: 100%"
            ref="productTable"
            slot="table">
            <el-table-column
              type="index"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              v-for="(item,index) in productListColumn"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              show-overflow-tooltip></el-table-column>
            <el-table-column label="入库数量（片）" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.noShelfNum" :min="1"
                                 :max="Number(scope.row.num) - Number(scope.row.warehousingQuantity)"
                                 :precision="0"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="仓库位置" width="150">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.warehouseLocation"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="货架位置" width="150">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.shelfLocation"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="120" class-name="operation">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="deleteRow(scope.$index, entryProduct)"
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
        <el-button type="primary" size="mini" @click="saveFinishedEntry('productForm')">保存</el-button>
        <el-button size="mini" @click="productEntryDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  操作出库  -->
    <el-dialog title='操作出库'
               :visible.sync="productOutboundDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <div class="addInfo">
        <p class="title1">出库信息</p>
        <div class="addInfoContainer">
          <el-form :model="productParams" ref="productForm" size="mini" :inline="true" class="ipt2"
                   :rules="productFormRules">
            <el-form-item label="出库单号" prop="operationNo">
              <el-input
                v-model="productParams.operationNo"
                disabled
              />
            </el-form-item>
            <el-form-item label="负责人" prop="operationPeople">
              <el-input
                v-model="productParams.operationPeople"
                placeholder="请输入负责人"
                clearable/>
            </el-form-item>
            <el-form-item label="出库日期" prop="operationDate">
              <el-date-picker
                v-model="productParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="productParams.remarks" size="mini" clearable  placeholder="请输入内容">
              </el-input>
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
            @click="handleWarehouseOrder">导入订单
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
            :data="entryProduct"
            border
            stripe
            height="100%"
            style="width: 100%"
            ref="productTable"
            slot="table">
            <el-table-column
              type="index"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              v-for="(item,index) in productListColumn"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              show-overflow-tooltip></el-table-column>
            <el-table-column label="出库数量（片）" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.noShelfNum" :min="1"
                                 :max="Number(scope.row.warehousingQuantity)" :precision="0"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="120" class-name="operation">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="deleteRow(scope.$index, entryProduct)"
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
        <el-button type="primary" size="mini" @click="saveFinishedOutbound('productForm')">保存</el-button>
        <el-button size="mini" @click="productOutboundDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--   导入未入库完成订单产品 -->
    <order-dialog :exportOrderDialog="exportNoEntry"
                  :productList="noEntryProductList"
                  :total="noEntryProducTotal"
                  @handleOrder="queryNoEntryProduct"
                  @addOrderList="addNoEntryList"
                  @backOrderList="backNoEntryList"
                  ref="exportNoEntry"></order-dialog>
    <!--  导入已入库订单  -->
    <el-dialog title="订单导入" :visible.sync="exportWarehouseOrder" width="80%" class="dialog-style exportProductList"
               :close-on-click-modal="false" :close-on-press-escape="false" append-to-body
               :destroy-on-close="true">
      <el-form :model="warehouseOrderParams" size="mini" :inline="true" class="iptAndBtn">
        <el-form-item label="订单编号">
          <el-input @keyup.enter.native="queryWarehouseOrder" v-model="warehouseOrderParams.orderNo" clearable/>
        </el-form-item>
        <el-form-item label="自定义编号">
          <el-input @keyup.enter.native="queryWarehouseOrder" v-model="warehouseOrderParams.customNo" clearable/>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input @keyup.enter.native="queryWarehouseOrder" v-model="warehouseOrderParams.customerName" clearable/>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input @keyup.enter.native="queryWarehouseOrder" v-model="warehouseOrderParams.entryName" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="queryWarehouseOrder">搜索</el-button>
        </el-form-item>
      </el-form>
      <slot-table class="productList" @handleChange="handleChangeWarehouseOrder"
                  :pageSize="warehouseOrderParams.pageSize" :pageNum="warehouseOrderParams.pageNum"
                  :total="warehouseOrderParams.total">
        <el-table highlight-current-row
          :data="warehouseOrderList"
          border
          stripe
          height="100%"
          style="width: 100%"
          ref="productListTable"
          @selection-change="selectionWarehouseOrder"
          @row-click="RowClickWarehouseOrder"
          slot="table">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in finishedListColumnTwo"
            :key="index"
            :label="item.label"
            :prop="item.prop"
            :min-width="item.width"
            show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="addWarehouseOrder">添加</el-button>
         <el-button size="mini" @click="backWarehouseOrder">返回</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import CountTable from "@/components/public/table/countTable";
import SlotTable from "@/components/public/table/SlotTable";
import SummaryTable from "@/components/public/table/summaryTable";
import {getCurrentDay, keepThreeNum} from "@/utils/order/order";
import {Message} from "element-ui";
import {customerList} from "@/api/system/customer";
import OrderDialog from "@/views/production/components/orderDialog";
import {
  queryWarehouseOrder,
  queryOrderProduct,
  queryProduct,
  productionNumber,
  productWarehouse, warehouseDeliverData
} from "@/api/product/finished";


export default {
  name: "index",
  components: {OrderDialog, SummaryTable, SlotTable, CountTable},
  data() {
    return {
      preparationDateRange: [],  //下单日期范围
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        customNo: '',
        customerName: '',
        entryName: '',
        productName: '',
        preparationDateEnd: '',
        preparationDateStart: '',
      },
      total: 0,
      selected: [],
      //总合计
      summation: [
        {label: 'orderNum', title: '订单数量', value: 0, unit: '片'},
        {label: 'warehouseQuantity', title: '入库数量', value: 0, unit: '片'},
      ],
      tableSize: '最大化',
      tableSizeIcon: 'el-icon-zoom-in',
      tableFlag: false,
      detailSize: '最大化',
      detailSizeIcon: 'el-icon-zoom-in',
      detailsFlag: false,
      finishedList: [],  //表格数据
      finishedListColumn: [
        {label: '订单编号', prop: 'orderNo', type: 'ipt', width: '120'},
        {label: '自定义编号', prop: 'customNo', type: 'ipt', width: '120'},
        {label: '客户名称', prop: 'customerName', type: 'ipt', width: '180'},
        {label: '项目名称', prop: 'entryName', type: 'ipt', width: '180'},
        {label: '订单类型', prop: 'orderType', type: 'select', width: '80'},
        {label: '下单时间', prop: 'preparationDate', width: '180'},
        {label: '制单人', prop: 'preparer', width: '120'},
        {label: '订单数量（片）', prop: 'orderNum', width: '110'},
        {label: '入库数量（片）', prop: 'warehouseQuantity', width: '110'},
        {label: '备注', prop: 'remarks'},
      ],
      finishedListColumnTwo: [
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
      stockDetails: [],
      orderDetailsColumn: [
        {label: '产品名称', prop: 'productName', width: '300'},
        {label: '位置', prop: 'position', width: '100'},
        {label: '宽（mm）', prop: 'wideHead', width: '110'},
        {label: '高（mm）', prop: 'highHead', width: '110'},
        {label: '订单数量（片）', prop: 'num', width: '100'},
        {label: '库存数量（片）', prop: 'warehousingQuantity', width: '150'},
        {label: '仓库位置', prop: 'warehouseLocation', width: '100'},
        {label: '货架位置', prop: 'shelfLocation', width: '120'},
      ],
      productEntryDialog: false, //原片入库
      productOutboundDialog: false,
      operationDialog: false,
      productParams: {
        operationType: '',
        operationNo: '',
        operationDate: getCurrentDay(),
        operationPeople: '',
        remarks: '',
        orderProducts: []
      },
      exportNoEntry: false,  //查询未入库完成订单产品
      noEntryProductList: [], //导入未入库完成订单产品信息数据
      noEntryProducTotal:0,
      // customerNameList:[],
      //导入产品信息查询条件
      exportParams: {
        id: '',
        orderNo: '',
        customNo: '',
        customName: '',
        customerName: '',
        entryName: '',
        pageNum: 1,
        pageSize: 20,
      },
      pageSizeOperation: 20,
      pageNumOperation: 1,
      totalOperation: 0,
      entryProduct: [],
      exportWarehouseOrder: false,  //查询已入库订单
      warehouseOrderParams: {
        orderNo: '',
        customNo: '',
        customerName: '',
        entryName: '',
        warehousingQuantity: 1,
        pageSize:20,
        pageNum:1,
        total:0
      },
      selectedWarehouse: [],
      warehouseOrderList: [], //已入库订单
      productListColumn: [
        {label: '产品名称', prop: 'productName', width: '200'},
        {label: '位置', prop: 'position', width: '100'},
        {label: '宽（mm）', prop: 'wideHead', width: '110'},
        {label: '高（mm）', prop: 'highHead', width: '110'},
        {label: '订单数量（片）', prop: 'num', width: '120'},
        {label: '完工数量（片）', prop: 'completionNum', width: '150'},
        {label: '已入库数量（片）', prop: 'warehousingQuantity', width: '120'},
      ],
      selectedProductList: [],
      operationList: [],
      operationListColumn: [
        {label: '操作类型', prop: 'orderNo', width: '120'},
        {label: '操作编号', prop: 'customNo', width: '120'},
        {label: '操作负责人', prop: 'customNo', width: '120'},
        {label: '操作日期', prop: 'customNo', width: '120'},
        {label: '操作数量', prop: 'customNo', width: '120'},
      ],
      productFormRules: {
        operationNo: [
          // {required: true, message: '请输入供应商', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        millId: [
          {required: true, message: '请输入供应商', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        operationDate: [
          {required: true, message: '入库日期不能为空', trigger: 'blur'}
        ]
      },
    }
  },
  created() {
    this.handleQuery();
  },
  mounted() {
    // // this.keyupSubmit();
    //合计行滚动条
    var multipleTable = this.$refs.multipleTable.$refs.bodyWrapper;
    multipleTable.addEventListener('scroll', () => {
      // 滚动距离
      const scrollLeft = multipleTable.scrollLeft
      this.$refs.multipleTable.$refs.headerWrapper.scrollLeft = scrollLeft
    })
    var checkDetailsTable = this.$refs.checkDetailsTable.$refs.footerWrapper;
    checkDetailsTable.addEventListener('scroll', () => {
      // 滚动距离
      const scrollLeft1 = checkDetailsTable.scrollLeft
      this.$refs.checkDetailsTable.$refs.bodyWrapper.scrollLeft = scrollLeft1
      this.$refs.checkDetailsTable.$refs.headerWrapper.scrollLeft = scrollLeft1
    })
  },
  computed: {
    disableVisible() {
      this.$refs.customerName.visible = false
    }
  },
  methods: {
    //键盘按下enter搜索事件
    keyupSubmit() {
      document.onkeydown = e => {
        const _key = window.event.keyCode
        if (_key === 13) {
          this.handleQuery();
          // this.getOrderDetails();
        }
      }
    },
    handleQuery() {
      if (this.preparationDateRange) {
        this.queryParams.preparationDateStart = this.preparationDateRange[0];
        this.queryParams.preparationDateEnd = this.preparationDateRange[1];
      } else {
        this.queryParams.preparationDateStart = '';
        this.queryParams.preparationDateEnd = '';
      }
      queryWarehouseOrder(this.queryParams).then(res => {
        this.finishedList = res.data;
        this.total=res.count;
        if(this.finishedList){
          this.summation[0].value = 0
          this.summation[1].value = 0
          this.finishedList.forEach(item=>{
            this.summation.forEach(sumItem => {
              if (item.hasOwnProperty(sumItem.label)) {
                sumItem.value += item[sumItem.label]
              }
              sumItem.value = keepThreeNum(sumItem.value);
            })
          })
          queryOrderProduct({id: res.data[0].id}).then(res => {
            this.stockDetails = res.data;
          })
        }
      })

    },
    /* 重置 */
    resetQuery() {
      this.preparationDateRange = [];
      this.receiptDateRange = [];
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        customNo: '',
        customerName: '',
        entryName: '',
        id: '',
        finishedNo: '',
        finishedType: '',
        preparationDateEnd: '',
        preparationDateStart: '',
        productionStatus: '',
      };
      // this.handleQuery();
      // this.getOrderDetails();
      let detailsTable = document.querySelector('.tabBtn');
      let finishedTable = document.querySelector('.finishedTable');
      detailsTable.style.display = 'block';
      detailsTable.style.height = 'calc(50% - 150px)';
      finishedTable.style.height = '50%';
      this.tableSize = '最大化';
      this.tableSizeIcon = 'el-icon-zoom-in';
      this.tableFlag = false;
      this.detailSize = '最大化';
      this.detailSizeIcon = 'el-icon-zoom-in';
      this.detailsFlag = false;
    },
    /* 操作入库 */
    handleEntry() {
      this.productEntryDialog = true;
      this.productParams = {
        operationType: '入库',
        operationNo: '',
        operationDate: getCurrentDay(),
        operationPeople: '',
        remarks: '',
        orderProducts: []
      };
      this.entryProduct = [];
      productionNumber({type: 0}).then(res => {
        this.productParams.operationNo = res.msg;
      })
    },
    /* 保存入库 */
    saveFinishedEntry() {
      this.productParams.orderProducts = this.entryProduct;
      productWarehouse(this.productParams).then(res => {
        if (res.code === 200) {
          this.$message.success(res.msg)
          this.handleQuery()
        } else {
          this.$message.error(res.msg)
          this.handleQuery()
        }
      })
      this.productEntryDialog = false;
    },
    /* 操作出库 */
    handleOutbound() {
      this.productOutboundDialog = true;
      this.productParams = {
        operationType: '出库',
        operationNo: '',
        operationDate: getCurrentDay(),
        operationPeople: '',
        remarks: '',
        orderProducts: []
      };
      this.entryProduct = [];
      productionNumber({type: 1}).then(res => {
        this.productParams.operationNo = res.msg;
      })
    },
    /* 操作记录 */
    handleOperation() {
      // this.operationDialog = true;
      this.$router.push({path : '/production/finished/operationRecord'});
    },
    /* 保存出库 */
    saveFinishedOutbound() {
      this.productParams.orderProducts = this.entryProduct;
      productWarehouse(this.productParams).then(res => {
        if (res.code === 200) {
          this.$message.success(res.msg)
        } else {
          this.$message.error(res.msg)
        }
      })
      this.handleQuery()
      this.productOutboundDialog = false;
    },
    /* 移除产品 */
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },

    handleChangeOperation(size, num) {
      this.pageSizeOperation = size;
      this.pageNumOperation = num;
      this.handleProduct();
    },
    /* 导入未入库产品 */
    handleNoEntry() {
      this.exportNoEntry = true;
      this.queryNoEntryProduct();
    },
    /*查询未入库完成订单产品*/
    queryNoEntryProduct() {
      if (this.$refs.exportNoEntry) {
        let params = this.$refs.exportNoEntry.exportOrderParams
        params.warehousingQuantity = 0;
        queryProduct(params).then(res => {
          this.noEntryProductList = res.data;
          this.noEntryProducTotal = res.count;
        })
      }
    },
    /* 添加导入未入库产品信息 */
    addNoEntryList(val) {
      if (val.length <= 0) {
        Message.warning("请选择要添加的数据");
      } else {
        for (let i = 0; i < val.length; i++) {
          var index = this.entryProduct.indexOf(val[i])
          if (index >= 0) {
            Message.warning("序号为" + (index + 1) + "的数据已经添加,不可重复添加！");
            return;
          }
        }
        this.entryProduct.push(...val);
        Message.success("导入产品信息成功");
        this.$refs.exportNoEntry.$refs.productListTable.clearSelection();

      }
    },
    /* 取消导入未入库产品信息 */
    backNoEntryList() {
      this.exportNoEntry = false;
      this.$refs.exportNoEntry.$refs.productListTable.clearSelection();
    },
    /* 导入已入库产品 */
    handleWarehouseOrder() {
      this.exportWarehouseOrder = true;
      this.queryWarehouseOrder();
    },
    /* 查询已入库产品  */
    queryWarehouseOrder() {
      queryProduct(this.warehouseOrderParams).then(res => {
        this.warehouseOrderList = res.data;
        this.warehouseOrderParams.total = res.count;
      })
      // }
    },
    /* 已入库分页 */
    handleChangeWarehouseOrder(size, num) {
      this.warehouseOrderParams.pageSize = size;
      this.warehouseOrderParams.pageNum = num;
      this.queryWarehouseOrder();
    },
    /* 选中已入库数据 */
    selectionWarehouseOrder(val) {
      this.selectedWarehouse = val;
    },
    RowClickWarehouseOrder(row, column, event) {
      this.$refs.productListTable.toggleRowSelection(row, column)
    },
    /* 添加 */
    addWarehouseOrder() {
      if (this.selectedWarehouse.length <= 0) {
        Message.warning("请选择要添加的数据");
      } else {
        for (let i = 0; i < this.selectedWarehouse.length; i++) {
          var index = this.entryProduct.indexOf(this.selectedWarehouse[i])
          if (index >= 0) {
            Message.warning("序号为" + (index + 1) + "的数据已经添加,不可重复添加！");
            return;
          }
        }
        this.entryProduct.push(...this.selectedWarehouse);
        Message.success("导入产品信息成功");
        this.$refs.productListTable.clearSelection();
      }
    },
    /*取消*/
    backWarehouseOrder() {
      this.exportWarehouseOrder = false;
      this.$refs.productListTable.clearSelection();
    },
    /* 打印 */
    toPrint(printId) {
      /**
       * data为模板所需要参数
       * 例如需要根据id查询模板数据
       */
      let data = {id: this.selected[0].id}
      const {href} = this.$router.resolve({
        path: '/print',
        query: {printId, ...data}
      })
      window.open(href, '_blank')
    },
    //导出条目数据
    exportOrder(param) {
      let ids = [];
      if (this.selected.length > 0) {
        this.selected.forEach(item => {
          ids.push(item.id)
        })
      }
      if (param === 0) {//导出订单
        this.download('sales/order/exportOrder', {
          ids: ids.toString()
        }, `订单库存数据.xlsx`)
      } else if (param === 1) {//导出产品
        this.download('sales/orderProduct/exportProduct', {
          ids: ids.toString()
        }, `订单产品库存数据.xlsx`)
      } else {//全部导出

      }
    },
    /* 分页器 */
    handleChange(pageSize, pageNum) {
      // console.log(pageSize, pageNum)
      // this.pageSize = pageSize;
      // this.pageNum = pageNum;
      this.queryParams.pageSize = pageSize;
      this.queryParams.pageNum = pageNum;
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
      return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + $index + 1;
    },
    /* 行点击事件 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
      queryOrderProduct({id: row.id}).then(res => {
        this.stockDetails = res.data;
      })
    },
    /* 调整订单表格最大化 */
    changeTableHeight() {
      let finishedTable = document.querySelector('.finishedTable');
      let detailsTable = document.querySelector('.tabBtn');
      if (this.tableFlag) {
        this.tableSizeIcon = 'el-icon-zoom-in';
        this.tableSize = '最大化';
        finishedTable.style.height = '50%';
        detailsTable.style.display = 'block';
        detailsTable.style.height = 'calc(50% - 150px)';
        this.detailSize = '还原';
        detailsTable.style.height = 'calc(65% - 150px)';
        finishedTable.style.height = '35%';
        this.detailSizeIcon = 'el-icon-zoom-out';
        this.detailsFlag = true;
      }
      if (!this.tableFlag) {
        this.tableSizeIcon = 'el-icon-zoom-out';
        this.tableSize = '还原';
        finishedTable.style.height = 'calc(100% - 100px)';
        detailsTable.style.display = 'none';
      }
      this.tableFlag = !this.tableFlag;
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
  }
}
</script>

<style lang="scss" scoped>
::v-deep .finishedTable {
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
  margin-top: 5px;
  width: 100%;
  height: calc(50% - 150px);
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

.operationList {
  height: 100%;

  ::v-deep .el-dialog {
    height: calc(75vh);

    .el-dialog__body {
      height: calc(100% - 50px);

      .el-tabs {
        height: 100%;

        .el-tabs__content {
          height: calc(100% - 50px);
          padding-bottom: 0;

          .el-tab-pane {
            height: 100%;
          }
        }
      }
    }
  }
}

//.exportProductList {
//  ::v-deep .el-dialog {
//    height: calc(60vh);
//
//    .el-dialog__body {
//      height: calc(100% - 90px);
//      padding-bottom: 0;
//
//      .productList {
//        height: calc(100% - 100px);
//        margin: 10px 0;
//      }
//
//      .iptAndBtn {
//        height: 80px;
//        flex-wrap: wrap;
//        overflow: auto;
//
//        .el-form-item {
//          min-width: 300px;
//
//          .el-form-item__label {
//            width: 100px;
//          }
//        }
//      }
//    }
//  }
//}
</style>
