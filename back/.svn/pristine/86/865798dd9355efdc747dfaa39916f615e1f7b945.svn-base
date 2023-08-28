<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="排产日期" class="daterange">
        <el-date-picker
          v-model="schedulingDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          @change="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="排产编号">
        <el-input
          v-model="queryParams.schedulingNo"
          placeholder="请输入排产编号"
          @keyup.enter.native="handleQuery"
          clearable/>
      </el-form-item>
      <el-form-item label="排产人">
        <el-input
          v-model="queryParams.schedulingName"
          placeholder="请输入排产人"
          @keyup.enter.native="handleQuery"
          clearable/>
      </el-form-item>
      <!--      <el-form-item>-->
      <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索
      </el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置
      </el-button>
      <!--      </el-form-item>-->
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-s-order"
        size="mini"
        @click="handleDetails" v-hasPermi="['system:scheduling:info']">查看明细
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd" v-hasPermi="['system:scheduling:add']">新增排产
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate" v-hasPermi="['system:scheduling:edit']">编辑排产
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete" v-hasPermi="['system:scheduling:del']">删除排产
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-printer"
        size="mini"
        @click="handlePrint" v-hasPermi="['system:scheduling:print']">打印
      </el-button>
      <el-dropdown>
        <el-button type="primary" size="mini" v-hasPermi="['system:scheduling:exportDetail']">
          <i class="iconfont icon-daochuwenjian"></i>导出<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="exportFlow(0)"><i class="iconfont icon-daochuwenjian"></i>导出排产信息
          </el-dropdown-item>
          <el-dropdown-item @click.native="exportFlow(1)"><i class="iconfont icon-daochuwenjian"></i>导出排产明细
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
    <slot-table class="schedulingTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total">
      <el-table highlight-current-row
                :data="schedulingList"
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
          :index="getIndex"
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="(item,index) in schedulingColumns"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
          <!--          <el-table-column :prop="item.prop" show-overflow-tooltip min-width="100" :min-width="item.width">
                      <template #header scoped-slot="scope">
                        &lt;!&ndash;可根据类型来显示为搜索框、下拉框等&ndash;&gt;
                        <el-input
                          v-if="item.type == 'ipt'"
                          v-model="queryParams[item.prop]"
                          size="mini"
                          placeholder="请输入"
                          clearable/>
                        &lt;!&ndash;              <el-select v-if="item.type=='select'" v-model="queryParams[item.prop]" placeholder='请选择' clearable&ndash;&gt;
                        &lt;!&ndash;                         size="mini">&ndash;&gt;
                        &lt;!&ndash;                <el-option&ndash;&gt;
                        &lt;!&ndash;                  v-for="item in dict.type.sys_pack"&ndash;&gt;
                        &lt;!&ndash;                  :key="item.value"&ndash;&gt;
                        &lt;!&ndash;                  :label="item.label"&ndash;&gt;
                        &lt;!&ndash;                  :value="item.value">&ndash;&gt;
                        &lt;!&ndash;                </el-option>&ndash;&gt;
                        &lt;!&ndash;              </el-select>&ndash;&gt;
                      </template>
                    </el-table-column>-->
        </el-table-column>
      </el-table>
    </slot-table>
    <!--  查看明细弹窗  -->
    <el-dialog title='查看明细'
               :visible.sync="detailDialog" width="75%" class="dialog-style detailDialog"
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
    <!--  新增排产  -->
    <el-dialog title="新增排产"
               :visible.sync="addSchedulingDialog" width="80%" class="dialog-style addSchedulingDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <div class="schedulingInfo">
        <p class="title">排产信息</p>
        <div class="schedulingInfoContainer">
          <el-form :model="schedulingInfo" size="mini" :inline="true" ref="ruleForm" :rules="rules" class="ipt2">
            <el-form-item label="排产日期" prop="schedulingDate" style="width: 450px;min-width:450px">
              <el-date-picker
                v-model="schedulingInfo.schedulingDate"
                :picker-options="preparationDateOptions"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" class="remarks" style="width: 450px;min-width:450px">
              <el-input v-model="schedulingInfo.remarks" type="textarea" clearable size="mini"
                        placeholder="请输入内容"/>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="btn">
        <el-button
          type="primary"
          icon="el-icon-folder-add"
          size="mini"
          @click="handleExportOrder">订单导入
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-folder-add"
          size="mini"
          @click="handleExportFlow">流程卡导入
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-folder-remove"
          size="mini"
          @click="handleRemoveProduct">移除
        </el-button>
      </div>
      <slot-table class="productTable">
        <el-table highlight-current-row
                  :data="schedulingData"
                  border
                  stripe
                  height="100%"
                  style="width: 100%"
                  ref="productTable"
                  @selection-change="handleOrderRemove"
                  @row-click="RowClickProductRemove"
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
            v-for="(item,index) in addSchedulingDataColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width"
            show-overflow-tooltip></el-table-column>
          <!--          <el-table-column label="排产数量" width="200" class-name="noShelfNum">-->
          <!--            <template slot-scope="scope">-->
          <!--              <el-input-number size="mini" v-model="scope.row.noShelfNum" :min="1"-->
          <!--                               :max="Number(scope.row.warehousingQuantity)-Number(scope.row.packNum)"></el-input-number>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveAddScheduling('ruleForm')">保存</el-button>
         <el-button size="mini" @click="addSchedulingDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  编辑排产  -->
    <el-dialog title="编辑排产"
               :visible.sync="editSchedulingDialog" width="80%" class="dialog-style addSchedulingDialog"
               :close-on-click-modal="false" :close-on-press-escape="false">
      <div class="schedulingInfo">
        <p class="title">排产信息</p>
        <div class="schedulingInfoContainer">
          <el-form :model="schedulingInfo" size="mini" :inline="true" ref="ruleForm" :rules="rules" class="ipt2">
            <el-form-item label="排产日期" prop="schedulingDate" style="width: 450px;min-width:450px">
              <el-date-picker
                v-model="schedulingInfo.schedulingDate"
                :picker-options="preparationDateOptions"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" class="remarks" style="width: 450px;min-width:450px">
              <el-input v-model="schedulingInfo.remarks" type="textarea" size="mini" placeholder="请输入内容"
                        clearable/>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="btn">
        <el-button
          type="primary"
          icon="el-icon-folder-add"
          size="mini"
          @click="handleExportOrder">订单导入
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-folder-add"
          size="mini"
          @click="handleExportFlow">流程卡导入
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-folder-remove"
          size="mini"
          @click="handleUpdRemoveProduct">移除
        </el-button>
      </div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="未生产" name="noProduct">
          <slot-table class="tabTable" :is-page="false">
            <el-table highlight-current-row
                      :data="schedulingData"
                      border
                      stripe
                      height="100%"
                      style="width: 100%"
                      ref="productTable"
                      @selection-change="handleOrderRemove"
                      @row-click="RowClickProductRemove"
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
                v-for="(item,index) in addSchedulingDataColumns"
                :key="index"
                :prop="item.prop"
                :label="item.label"
                :min-width="item.width"
                show-overflow-tooltip></el-table-column>
              <!--          <el-table-column label="排产数量" width="200" class-name="noShelfNum">-->
              <!--            <template slot-scope="scope">-->
              <!--              <el-input-number size="mini" v-model="scope.row.noShelfNum" :min="1"-->
              <!--                               :max="Number(scope.row.warehousingQuantity)-Number(scope.row.packNum)"></el-input-number>-->
              <!--            </template>-->
              <!--          </el-table-column>-->
            </el-table>
          </slot-table>
        </el-tab-pane>
        <el-tab-pane label="已生产" name="haveProduct">
          <slot-table class="tabTable" :is-page="false">
            <el-table highlight-current-row
                      :data="schedulingData"
                      border
                      stripe
                      height="100%"
                      style="width: 100%"
                      ref="productTable"
                      @selection-change="handleOrderRemove"
                      @row-click="RowClickProductRemove"
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
                v-for="(item,index) in addSchedulingDataColumns"
                :key="index"
                :prop="item.prop"
                :label="item.label"
                :min-width="item.width"
                show-overflow-tooltip></el-table-column>
            </el-table>
          </slot-table>
        </el-tab-pane>
      </el-tabs>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveEditScheduling('ruleForm')">保存</el-button>
        <el-button size="mini" @click="editDialogCancal">取消</el-button>
      </span>
    </el-dialog>
    <!--  选择订单  -->
    <order-dialog :exportOrderDialog="exportOrderDialog"
                  :pageSize="orderPageSize"
                  :pageNum="orderPageNum"
                  :total="orderTotal"
                  @handlePage="handleOrderPage"
                  @addOrderList="addOrderList"
                  @handleOrder="handleOrder"
                  @backOrderList="backOrderList"
                  :productList="orderList"
                  ref="orderDialog"></order-dialog>
    <!--  选择流程卡 -->
    <flow-dialog :exportFlowDialog="exportFlowDialog"
                 :productList="flowCardList"
                 :pageSize="flowPageSize"
                 :pageNum="flowPageNum"
                 :total="flowTotal"
                 @handlePage="handleFlowPage"
                 @addFlowList="addFlowList"
                 @handleFlow="handleFlow"
                 @backFlowList="backFlowList"
                 ref="flowDialog"></flow-dialog>
  </div>
</template>

<script>
import SlotTable from "@/components/public/table/SlotTable";
import {Message} from "element-ui";
import OrderDialog from "@/views/production/components/schedulingOrderDialog";
import FlowDialog from "@/views/production/components/schedulingFlowDialog";
import {
  getOrderList,
  getFlowCardList,
  addScheduling,
  editScheduling,
  delScheduling,
  getSchedulingList,
  getSchedulingListInfo
} from "@/api/product/scheduling";
import {delCompletion} from "@/api/product/completion";

export default {
  dicts: ['sys_pack'],
  name: "index",
  components: {FlowDialog, OrderDialog, SlotTable},
  data() {
    return {
      orderPageSize:20,
      orderPageNum:1,
      orderTotal:0,
      flowPageSize:20,
      flowPageNum:1,
      flowTotal:0,
      //查询参数0
      queryParams: {
        customerName: "",
        deliverPerson: "",
        id: "",
        orderId: "",
        orderNo: "",
        schedulingDateEnd: "",
        schedulingDateStart: "",
        schedulingMode: "",
        schedulingName: "",
        schedulingNo: "",
        pageNum: 1,
        pageSize: 20,
        productName: "",
        projectName: "",
        responsiblePerson: ""
      },
      schedulingDateRange: [], //排产日期范围
      pageSize: 20,
      pageNum: 1,
      total: 0,
      schedulingList: [],  //排产列表
      //排产表头
      schedulingColumns: [
        {label: '排产编号', prop: 'schedulingNo', type: 'ipt', width: '150'},
        {label: '排产数量（片）', prop: 'schedulingNum', width: '120'},
        {label: '排产面积（m²）', prop: 'schedulingArea', width: '120'},
        {label: '开始时间', prop: 'startDate', width: '180'},
        {label: '结束时间', prop: 'endDate', width: '180'},
        {label: '排产日期', prop: 'schedulingDate', width: '180'},
        {label: '排产人', prop: 'schedulingPerson', type: 'ipt', width: '150'},
        {label: '备注', prop: 'remarks', type: 'ipt', width: '150'},
      ],
      selected: [],
      detailDialog: false,  //明细弹窗
      //明细信息
      // detailsInfo: [
      //   {title: '排产编号', value: ''},
      //   {title: '排产名称', value: ''},
      //   {title: '订单编号', value: ''},
      //   {title: '客户名称', value: ''},
      //   {title: '项目名称', value: ''},
      //   {title: '排产方式', value: ''},
      //   {title: '排产负责人', value: ''},
      //   {title: '备注', value: ''},
      // ],
      detailDialogData: [],  //明细数据
      //订单明细表头
      detailDialogColumns: [
        {label: '订单编号', prop: 'orderNo', width: '150'},
        {label: '客户名称', prop: 'customerName', width: '120'},
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '数量（片）', prop: 'splitNum', width: '100'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '单片厚度（mm）', prop: 'monolithicThick', width: '120'},
        {label: '单片名称', prop: 'monolithicName', width: '100'},
        {label: '总面积（m²）', prop: 'totalArea', width: '120'}
      ],
      addSchedulingDialog: false,  //新增弹窗
      editSchedulingDialog: false,  //编辑弹窗
      schedulingInfo: {
        schedulingNo: '',
        schedulingName: '',
        responsiblePerson: '',
        schedulingDate: '',
        schedulingMode: '',
        schedulingCost: '',
        customerName: '',
        remarks: '',
        list: []
      },
      // 排产时间不能晚于当前时间
      preparationDateOptions: {
        disabledDate: time => {
          return time.getTime() < Date.now()
        },
      },
      schedulingData: [],  //排产表格
      orderDataColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '项目名称', prop: 'entryName', width: '200'},
        {label: '自定义编号', prop: 'customNo', width: '150'},
        {label: '数量（片）', prop: 'productName', width: '150'},
        {label: '总面积（m²）', prop: 'wideHead', width: '120'},
        {label: '制单日期', prop: 'highHead', width: '100'},
        {label: '交货日期', prop: 'num', width: '100'},
        {label: '备注', prop: 'warehousingQuantity', width: '100'},
      ],
      addSchedulingDataColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '自定义编号', prop: 'customNo', width: '150'},
        {label: '数量（片）', prop: 'splitNum', width: '100'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '单片厚度（mm）', prop: 'monolithicThick', width: '120'},
        {label: '单片名称', prop: 'monolithicName', width: '100'},
        {label: '总面积（m²）', prop: 'totalArea', width: '120'}
      ],
      selectedProduct: [],  //选择产品信息移除
      exportOrderDialog: false,  //导入订单产品信息弹窗
      exportFlowDialog: false,    //导入流程卡产品信息弹窗
      activeName: 'noProduct',
      // 表单校验
      rules: {
        schedulingDate:[{required: true,message: '请选择排产', trigger: 'change'}],
        schedulingName: [
          {max: 20, message: '字符长度最大不得超过20', trigger: 'blur'}
        ],
        responsiblePerson: [
          {max: 10, message: '字符长度最大不得超过10', trigger: 'blur'}
        ],
        schedulingMode: [
          {max: 20, message: '字符长度最大不得超过20', trigger: 'blur'}
        ],
        customerName: [
          {max: 40, message: '字符长度最大不得超过40', trigger: 'blur'}
        ],
        schedulingCost: [
          {pattern: /^[+]?(0|([1-9]\d*))(\.\d+)?$/g, message: '请输入整数或小数'}
        ],
      },
      orderList: [],
      flowCardList: []
    }
  },
  created() {
    this.handleQuery();
  },
  mounted() {
    // this.keyupSubmit();
  },
  computed: {
    disableVisible() {
      this.$refs.customerName.visible = false
    }
  },
  methods: {
    handleOrderPage(params){
      let { size,num} = params
      this.orderPageNum = num
      this.orderPageSize = size
      this.handleOrder();
    },
    handleFlowPage(params){
      let { size,num} = params
      this.flowPageNum = num
      this.flowPageSize = size
      this.handleFlow();
    },
    editDialogCancal() {
      this.editSchedulingDialog = false
    },
    handleFlow() {
      if (this.$refs.flowDialog) {
        let params = this.$refs.flowDialog.exportFlowParams
        getFlowCardList({...params,pageNum:this.flowPageNum,pageSize:this.flowPageSize}).then(res => {
          this.flowCardList = res.data
          this.flowTotal = res.count
        })
      }
    },
    handleOrder() {
      if (this.$refs.orderDialog) {
        let params = this.$refs.orderDialog.exportOrderParams
        getOrderList({...params,pageSize:this.orderPageSize,pageNum:this.orderPageNum}).then(res => {
          this.orderList = res.data
          this.orderTotal = res.count
        })
      }
    },
    //键盘按下enter搜索事件
    keyupSubmit() {
      document.onkeydown = e => {
        const _key = window.event.keyCode
        if (_key === 13) {
          this.handleQuery();
        }
      }
    },
    /* 订单查询 */
    handleQuery() {
      if (this.schedulingDateRange) {
        this.queryParams.startDate = this.schedulingDateRange[0];
        this.queryParams.endDate = this.schedulingDateRange[1];
      } else {
        this.queryParams.startDate = undefined;
        this.queryParams.endDate = undefined;
      }
      getSchedulingList(this.queryParams).then(res => {
        this.schedulingList = res.data
        this.total = res.count
      })
    },
    /* 重置 */
    resetQuery() {
      this.schedulingDateRange = [];
      this.queryParams = {
        customerName: "",
        deliverPerson: "",
        id: "",
        orderId: "",
        orderNo: "",
        schedulingDateEnd: "",
        schedulingDateStart: "",
        schedulingMode: "",
        schedulingName: "",
        schedulingNo: "",
        pageNum: 1,
        pageSize: 20,
        productName: "",
        projectName: "",
        responsiblePerson: ""
      };
      this.handleQuery();
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
        getSchedulingListInfo(this.selected[0].id).then(res => {
          this.detailDialogData = res.data.flowCardList
          this.detailDialog = true;
        })
      }
    },
    /* 新增排产 */
    handleAdd() {
      this.schedulingInfo = {
        schedulingNo: '',
        schedulingName: '',
        responsiblePerson: '',
        schedulingDate: '',
        schedulingMode: '',
        schedulingCost: '',
        customerName: '',
        remarks: '',
        list: []
      }
      this.schedulingData = []
      this.orderList = []
      this.flowCardList = []
      this.addSchedulingDialog = true;
    },
    /* 编辑排产 */
    handleUpdate() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要修改的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行修改");
        return false
      }
      if (this.selected.length == 1) {
        getSchedulingListInfo(this.selected[0].id).then(res => {
          this.schedulingInfo = res.data
          this.schedulingInfo.schedulingDate = [res.data.startDate, res.data.endDate]
          this.schedulingData = res.data.flowCardList.filter(item => {
            return item.productionStatus == 0
          })
          this.editSchedulingDialog = true;
        })
        // updateDetails({id: this.selected[0].id}).then(res => {
        //   this.schedulingData = res.data;
        // })

      }
    },
    /* 删除排产 */
    handleDelete() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要删除的数据");
        return;
      }else {
        this.$confirm('此操作将删除选中数据，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          let ids = [];
          this.selected.forEach(one => {
            ids.push(one.id)
          })
          delScheduling(ids.toString()).then(res => {
            this.$message.success(res.msg)
            this.handleQuery();
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    /* 导出排产 */
    exportFlow(param) {
      if (param === 0) {//导出排产信息
        let ids = this.selected.map(item => {
          return item.id
        })
        this.download('produce/scheduling/export', {
          ids: ids.toString()
        }, `排产数据.xlsx`)
      } else if (param === 1) {//导出排产明细
        let ids = this.selected.map(item => {
          return item.id
        })
        if (ids.length != 1) {
          this.$message.warning("请选择一条要导出明细的数据")
          return false
        }
        this.download('produce/scheduling/exportDetail', {
          id: ids[0]
        }, `排产明细.xlsx`)
      }
    },
    /* 打印排产 */
    handlePrint() {

    },
    /* 排产单翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 排产复选框 */
    handleSelectionChange(val) {
      this.selected = val
    },
    /* 行点击事件 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },
    /* 排产单分页器 */
    handleChange(size, num) {
      this.pageSize = size;
      this.pageNum = num;
      this.queryParams.pageNum = this.pageNum;
      this.queryParams.pageSize = this.pageSize;
      this.handleQuery();
    },
    /* 移除产品信息 */
    handleRemoveProduct() {
      if (this.schedulingData.length <= 0) {
        Message.warning("请先导入产品信息");
      } else {
        if (this.selectedProduct.length <= 0) {
          Message.warning("请选择需要移除的数据");
        } else {
          this.selectedProduct.forEach(item => {
            var number = this.schedulingData.indexOf(item);
            this.schedulingData.splice(number, 1)
          })
        }
      }
    },
    handleUpdRemoveProduct() {
      if (this.activeName == 'haveProduct') {
        this.$message.warning("已生产数据无法操作")
        return false
      }
      if (this.schedulingData.length <= 0) {
        Message.warning("请先导入产品信息");
      } else {
        if (this.selectedProduct.length <= 0) {
          Message.warning("请选择需要移除的数据");
        } else {
          this.selectedProduct.forEach(item => {
            var number = this.schedulingData.indexOf(item);
            this.schedulingData.splice(number, 1)
            var number2 = this.schedulingInfo.flowCardList.indexOf(item);
            this.schedulingInfo.flowCardList.splice(number2, 1)
          })
        }
      }
    },
    /* tab切换 */
    handleClick(tab, event) {
      if (tab.index == 0) {
        this.schedulingData = this.schedulingInfo.flowCardList.filter(item => {
          return item.productionStatus == 0
        })
      } else {
        this.schedulingData = this.schedulingInfo.flowCardList.filter(item => {
          return item.productionStatus != 0
        })
      }
    },
    /* 选择产品信息复选框移除 */
    handleOrderRemove(val) {
      this.selectedProduct = val;
    },
    /* 产品信息移除行点击事件 */
    RowClickProductRemove(row, column, event) {
      this.$refs.productTable.toggleRowSelection(row, column)
    },
    /* 保存排产 */
    saveAddScheduling(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.schedulingInfo.schedulingDate || this.schedulingInfo.schedulingDate == '' || this.schedulingInfo.schedulingDate.length != 2) {
            Message.warning("请选择排产时间")
            return false
          }
          if (this.schedulingData.length <= 0) {
            Message.warning("请导入产品信息进行排产");
          } else {
            addScheduling({
              startDate: this.schedulingInfo.schedulingDate[0],
              endDate: this.schedulingInfo.schedulingDate[1],
              remarks: this.schedulingInfo.remarks,
              flowCardList: this.schedulingData
            }).then(res => {
              this.$message.success(res.msg)
              this.addSchedulingDialog = false
              this.handleQuery()
            })
          }
        } else {
          return false;
        }
      })
    },
    /* 编辑排产 */
    saveEditScheduling(formName) {
      console.log('1232')
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.schedulingInfo.schedulingDate || this.schedulingInfo.schedulingDate == '' || this.schedulingInfo.schedulingDate.length != 2) {
            Message.warning("请选择排产时间")
            return false
          }
          if (this.schedulingInfo.flowCardList <= 0) {
            Message.warning("请导入产品信息进行排产");
          } else {
            this.schedulingInfo.startDate = this.schedulingInfo.schedulingDate[0]
            this.schedulingInfo.endDate = this.schedulingInfo.schedulingDate[0]
            delete this.schedulingInfo.schedulingDate
            editScheduling({
              ...this.schedulingInfo
            }).then(res => {
              this.$message.success(res.msg)
              this.editSchedulingDialog = false
              this.handleQuery()
            })
          }
        } else {
          return false;
        }
      })
    },
    /* 选择订单 */
    handleExportOrder() {
      this.exportOrderDialog = true
      this.handleOrder()
    },
    /* 移除订单 */
    handleRemoveOrder() {
    },
    /* 添加订单 */
    addOrderList(val) {
      if (val.length <= 0) {
        Message.success("请选择数据进行添加");
      } else {
        let orderNos = val.map(item => {
          return item.orderNo
        }).toString();
        getFlowCardList({orderNos}).then(res => {
          let pushNum = 0;
          res.data.forEach(item => {
            let filter = this.schedulingData.filter(f => {
              return JSON.stringify(f) == JSON.stringify(item)
            })
            if (filter.length <= 0) {
              pushNum++;
              this.schedulingData.push(item);
            }
            if (Array.isArray(this.schedulingInfo.flowCardList)) {
              let filter2 = this.schedulingInfo.flowCardList.filter(f => {
                return JSON.stringify(f) == JSON.stringify(item)
              })
              if (filter2.length <= 0) {
                pushNum++;
                this.schedulingInfo.flowCardList.push(item);
              }
            }
          })
          if (pushNum > 0) {
            Message.success("添加成功");
          } else {
            Message.warning("选择的订单中无可排产产品")
          }
        })
      }
    },
    /* 返回订单导入*/
    backOrderList(val) {
      console.log(val)
      this.exportOrderDialog = false;
      this.$refs.orderDialog.$refs.productListTable.clearSelection();
      this.$refs.orderDialog.exportOrderParams = {
        id: '',
        orderNo: '',
        customNo: '',
        customerName: '',
        entryName: '',
        pageSize: 20,
        pageNum: 1,
      }
    },
    /* 选择流程卡 */
    handleExportFlow() {
      this.exportFlowDialog = true;
      this.handleFlow();
    },
    /* 移除流程卡 */
    handleRemoveFlow() {

    },
    /* 添加流程卡 */
    addFlowList(val) {
      if (val.length <= 0) {
        Message.success("请选择数据进行添加");
      } else {
        let pushNum = 0
        val.forEach(item => {
          let filter = this.schedulingData.filter(f => {
            return JSON.stringify(f) == JSON.stringify(item)
          })
          if (filter.length <= 0) {
            pushNum++;
            this.schedulingData.push(item);
          }
          if (Array.isArray(this.schedulingInfo.flowCardList)) {
            let filter2 = this.schedulingInfo.flowCardList.filter(f => {
              return JSON.stringify(f) == JSON.stringify(item)
            })
            if (filter2.length <= 0) {
              pushNum++;
              this.schedulingInfo.flowCardList.push(item);
            }
          }
        })
        if (pushNum > 0) {
          Message.success("添加成功");
        } else {
          Message.warning("选择的订单中无可排产产品")
        }
      }
    },
    /* 返回流程卡导入*/
    backFlowList(val) {
      console.log(val)
      this.exportFlowDialog = false;
      this.$refs.flowDialog.$refs.productListTable.clearSelection();
      this.$refs.flowDialog.exportFlowParams = {
        id: '',
        orderNo: '',
        customNo: '',
        customerName: '',
        entryName: '',
        pageSize: 20,
        pageNum: 1,
        splitDate: '',
        flowCardNo: ''
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.schedulingTable {
  height: calc(100% - 100px);
}

::v-deep .detailDialog {
  .el-dialog {
    height: calc(60vh);
    padding: 0;

    .detailsInfo {
      justify-content: flex-start;
      margin-bottom: 0;

      p {
        min-width: 310px;
      }
    }

    .detailsTable {
      height: calc(100% - 10px);
    }

  }
}

::v-deep .addSchedulingDialog {
  .el-dialog {
    height: 700px;

    .el-dialog__body {
      height: calc(100% - 90px);

      .schedulingInfo {
        height: 95px;
        border-radius: 15px;
        box-shadow: 0 1px 5px 0 rgba(0, 0, 0, 0.3);

        .title {
          height: 30px;
          padding-left: 40px;
          margin-bottom: 0 !important;
        }

        .schedulingInfoContainer {
          height: calc(100% - 30px);
          overflow: auto;
          background: rgba(100, 200, 188, 0.05);
          border: 1px solid rgba(100, 200, 188, 0.5);
          border-bottom-left-radius: 15px;
          border-bottom-right-radius: 15px;


          ::v-deep .ipt2 {
            justify-content: start;
            flex-wrap: nowrap;
            overflow: auto;
          }
        }
      }

      .el-tabs {
        height: calc(80% - 30px);

        .el-tabs__content {
          height: calc(100% - 40px);

          .el-tab-pane {
            height: 100%;
          }
        }
      }

      .productTable {
        max-height: calc(80% - 50px);
        min-height: 200px;

        .table {
          height: 100%;
          border-bottom: 1px solid #dfe6ec;
        }

        .page {
          display: none;
        }
      }
    }

  }

}

/*.exportProductList {
  ::v-deep .el-dialog {
    height: calc(60vh);

    .el-dialog__body {
      height: calc(100% - 90px);
      padding-bottom: 0;

      .productList {
        height: calc(100% - 100px);
        margin: 10px 0;
      }

      .iptAndBtn {
        height: 80px;
        flex-wrap: wrap;
        overflow: auto;

        .el-form-item {
          min-width: 300px;

          .el-form-item__label {
            width: 100px;
          }
        }
      }
    }
  }
}*/


</style>
