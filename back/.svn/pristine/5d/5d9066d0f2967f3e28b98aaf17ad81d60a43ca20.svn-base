<template>
  <div class="app-container-padding">
    <el-form :model="flowCardParams" ref="flowCardForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="分架日期" class="daterange">
        <el-date-picker
          v-model="flowCardDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd" @change="handleFlowQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFlowQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetFlowQuery">重置</el-button>
<!--        <el-button-->
<!--          type="success"-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAddFlow"-->
<!--          v-hasPermi="['sales:flowCard:saveFlowCard']">新增流程卡-->
<!--        </el-button>-->
      </el-form-item>
    </el-form>
    <div class="btn" style="display: flex;justify-content: space-between;align-items: center;">
      <div>
        <el-button
          type="primary"
          icon="el-icon-s-order"
          size="mini"
          @click="handleDetail"
          v-hasPermi="['sales:flowCard:queryFlowDetailed']">查看明细
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-edit"
          size="mini"
          @click="handleUpdateFlow"
          v-hasPermi="['sales:flowCard:saveUpdate']">编辑流程卡
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-delete"
          size="mini"
          @click="handleDeleteFlow"
          v-hasPermi="['sales:flowCard:delFlowCard']">删除流程卡
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-edit-outline"
          size="mini"
          @click="handleProcessComplete"
          v-hasPermi="['sales:flowCard:complete']">工艺完工
        </el-button>
        <Print typeId="lckgl" @toPrint="toPrint"></Print>
        <el-dropdown>
          <el-button type="primary" size="mini" v-hasPermi="['sales:flowCard:exportFlowDetail']">
            <i class="iconfont icon-daochuwenjian"></i>导出<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="exportFlow(0)"><i class="iconfont icon-daochuwenjian"></i>导出流程卡</el-dropdown-item>
            <el-dropdown-item @click.native="exportFlow(1)"><i class="iconfont icon-daochuwenjian"></i>导出流程卡明细</el-dropdown-item>
            <el-dropdown-item @click.native="exportFlow(2)"><i class="iconfont icon-daochuwenjian"></i>导出迪赛perfectcut模板</el-dropdown-item>
            <el-dropdown-item @click.native="exportFlow(3)"><i class="iconfont icon-daochuwenjian"></i>导出迪赛optima模板</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div style="padding-right: 20px;color:red;">
        表格内展示的数据为已分架流程卡信息
      </div>
    </div>
    <count-table class="rightTable orderTable" @handleChange="handleFlowChange" :pageSize="pageSizeFlow"
                 :pageNum="pageNumFlow"
                 :total="totalFlow" :summation='summation'>
      <el-table highlight-current-row
                :data="flowCardList"
                stripe
                border
                style="width: 100%"
                height="100%"
                ref="multipleFlowCardTable"
                @selection-change="handleFlowCardChange"
                @row-click="handleFlowCardClick"
                slot="table">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          :index="getFlowCardIndex"
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="(item,index) in flowCardListColumn"
          :key="index"
          :label="item.label"
          show-overflow-tooltip>
          <el-table-column :prop="item.prop" show-overflow-tooltip :min-width="item.width">
            <template #header scoped-slot="scope">
              <!--可根据类型来显示为搜索框、下拉框等-->
              <el-input
                v-model="flowCardParams[item.prop]"
                v-if="item.type==='ipt'"
                size="mini"
                placeholder="请输入"
                clearable @keyup.enter.native="handleFlowQuery"/>
              <el-select v-model="flowCardParams[item.prop]" placeholder="请选择" clearable size="mini" ref='statusSelect' @change="handleFlowQuery"
                         v-if="item.type==='status'">
                <el-option label="未生产" value='0'></el-option>
                <el-option label="生产中" value="1"></el-option>
                <el-option label="已完成" value="2"></el-option>
              </el-select>
            </template>
            <template slot-scope="scope">
              <template v-if="item.prop.includes('Status')">
                <span v-if="scope.row[item.prop]==0">未生产</span>
                <span v-if="scope.row[item.prop]==1">生产中</span>
                <span v-if="scope.row[item.prop]==2">已完成</span>
              </template>
              <template v-if="!item.prop.includes('Status')">{{ scope.row[item.prop] }}</template>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </count-table>
    <!--  查看明细弹窗  -->
    <el-dialog title="查看明细" :visible.sync="detailDialog" width="85%" class="dialog-style detailDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
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
            show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </slot-table>
    </el-dialog>
    <!--  编辑流程卡  -->
    <el-dialog title="编辑分架" :visible.sync="manualDialog" width="90%"
               class="dialog-style naturalDialog manualDialog" :close-on-click-modal="false"
               :close-on-press-escape="false" :destroy-on-close="true">
      <div class="title1" style="padding-left: 0; display: flex; justify-content: space-between;">
        未分架信息
        <div>
          <i class="el-icon-star-on" style="color:#e52b47; font-size: 16px;">部分分架</i>
          <i class="el-icon-star-on" style="color:#43f51b; font-size: 16px;">分架完成</i>
        </div>
      </div>
      <slot-table class="manualTable">
        <el-table highlight-current-row
                  :data="noManualData"
                  border
                  stripe
                  height="100%"
                  :row-class-name="tableRowClassName"
                  :span-method="processSpanMethod"
                  @selection-change="noManualDataChange"
                  ref="noManualTable"
                  @row-click="noManualRowClick"
                  style="width: 100%" slot="table">
          <el-table-column
            type="selection"
            width="40">
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in noManualColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width" show-overflow-tooltip></el-table-column>
          <el-table-column label="分架数量（片）" width="180">
            <template slot-scope="scope">
              <el-input-number size="mini" v-model="scope.row.shelfNum" style="width: 150px"></el-input-number>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <div class="manualBtn">
        <div class="title1" style="padding-left: 0;">已分架信息</div>
        <div>
          <el-button
            type="primary"
            icon="el-icon-edit"
            @click="setFlowCard"
            size="mini">纳入并创建流程卡
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            @click="setLatelyFlowCard"
            size="mini">纳入指定流程卡
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            @click="delFlowCard"
            size="mini">删除流程卡
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete-solid"
            @click="delAllFlowCard"
            size="mini">清空流程卡
          </el-button>
        </div>
      </div>
      <slot-table class="manualTable">
        <el-table highlight-current-row
                  ref="haveManualDataTable222"
                  :data="haveManualData"
                  border
                  stripe
                  @selection-change="manualDataChange"
                  @row-click="manualDataClick"

                  height="100%"
                  style="width: 100%" slot="table" class-name="bBGC">
          <el-table-column
            type="selection"
            width="40" class-name="bBGC">
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="50" class-name="bBGC">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in haveManualColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width" :class-name="item.className" show-overflow-tooltip></el-table-column>
          <el-table-column label="调整后分架数量（片）" width="180" class-name="changeNum">
            <template slot-scope="scope">
              <el-input-number size="mini" v-model="scope.row.itemNum"
                               @focus="itemNumClick(scope.row.itemNum)"
                               @change="itemNumChange(scope.row)" style="width: 150px;" :min="0" :precision="0" :controls="false"></el-input-number>
              <!--              <el-input size="mini" type="number" v-model="scope.row.itemNum"-->
              <!--                        @focus="itemNumClick(scope.row.itemNum)"-->
              <!--                        @change="itemNumChange(scope.row)" style="width: 150px;"  class="del-arrow"></el-input>-->
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
         <el-button
           type="primary"
           size="mini" @click="saveManual">保存
          </el-button>
          <el-button
            size="mini" @click="manualDialog=false">取消
          </el-button>
        <!--          <el-button type="primary" size="mini" @click="saveNaturalPointsPlane">保存</el-button>-->
        <!--          <el-button size="mini" @click="naturalPointsPlaneDialog=false">取消</el-button>-->
        </span>
    </el-dialog>

    <!--  打印名称弹窗  -->
    <el-dialog
      title="修改Excel名称"
      :visible.sync="isUpdateName"
      width="30%"
      class="dialog-style checkDialog"
      :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <div>
        <div style="font-size: 14px; padding: 30px 0; display: inline-block; width: 100px">Excel名称：</div>
        <el-input v-model="printName" style="display: inline-block;  width: 200px" clearable size="mini" placeholder="请输入Excel名称"/>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="exportData">确定</el-button>
        <el-button size="mini" @click="isUpdateName=false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import SlotTable from "@/components/public/table/SlotTable";
import {saveFile} from "@/utils/saveFile"
import SummaryTable from "@/components/public/table/summaryTable";
import {
  queryList,
  queryFlowDetailed,
  updateQuery,
  generateFlowNumber,
  saveUpdate,
  delFlowCard,
  checkFlowCard,
  exportMachineFlow
} from "@/api/salse/flowCard/flowCard";
import {keepThreeNum, sum} from "@/utils/order/order";
import CountTable from "@/components/public/table/countTable";
import {delOrder} from "@/api/salse/order/order";
import {Message} from "element-ui";
import Print from "@/components/Print/index"

export default {
  name: "flowCard",
  components: {CountTable, SummaryTable, SlotTable, Print},
  data() {
    return {
      myData: '',
      //查询参数
      flowCardParams: {
        customNo: "",
        customerName: "",
        flowCardNo: "",
        id: "",
        monolithicName: "",
        orderNo: "",
        pageNum: 1,
        pageSize: 20,
        productName: "",
        productionStatus: null,
        splitDateEnd: "",
        splitDateStart: "",
        splitPerson: ""
      },
      flowCardDateRange: [],  //分架日期范围
      pageSizeFlow: 20,
      pageNumFlow: 1,
      totalFlow: 0,
      //总合计
      summation: [
        {label: 'splitNum', title: '分架数量', value: 0, unit: '片'},
        {label: 'schedulingNum', title: '排产数量', value: 0, unit: '片'},
        {label: 'damageNum', title: '损坏数量', value: 0, unit: '片'},
        {label: 'patchNum', title: '补片数量', value: 0, unit: '片'},
        {label: 'totalArea', title: '总面积', value: 0, unit: 'm²'},
        {label: 'totalWeight', title: '总重量', value: 0, unit: 't'},
      ],
      flowCardList: [],    //流程卡表单
      //流程卡表头
      flowCardListColumn: [
        {label: '流程卡号', prop: 'flowCardNo', width: '150', type: 'ipt'},
        {label: '订单编号', prop: 'orderNo', width: '150', type: 'ipt'},
        {label: '自定义编号', prop: 'customNo', width: '120', type: 'ipt'},
        {label: '生产状态', prop: 'productionStatus', width: '100', type: 'status'},
        {label: '客户名称', prop: 'customerName', width: '120', type: 'ipt'},
        {label: '产品名称', prop: 'productName', width: '180', type: 'ipt'},
        {label: '单片名称', prop: 'monolithicName', width: '180', type: 'ipt'},
        {label: '分架日期', prop: 'splitDate', width: '180'},
        {label: '分架数量（片）', prop: 'splitNum', width: '120'},
        {label: '损坏数量（片）', prop: 'damageNum', width: '120'},
        {label: '补片数量（片）', prop: 'patchNum', width: '120'},
        {label: '补片流程卡号', prop: 'patchFlowCardNo', width: '150'},
        {label: '面积（m²）', prop: 'totalArea', width: '100'},
        {label: '重量（t）', prop: 'totalWeight', width: '100'},
        {label: '已完成工艺', prop: 'completeProcess', width: '300'},
        {label: '流程卡工艺', prop: 'processContent', width: '300'},
        {label: '分架人', prop: 'splitPerson', type: 'ipt'},
        // {label: '排产状态', prop: 'schedulingNum'},
        {label: '排产日期', prop: 'schedulingDate', width: '180'},
        {label: '排产数量（片）', prop: 'schedulingNum', width: '120'},
        {label: '发货单号', prop: 'deliveryNo', width: '150'},
        {label: '发货人', prop: 'deliveryPeople'},
      ],
      selectFlowCard: [],  //选择流程卡
      detailDialog: false,   //查看明细弹窗
      //明细信息
      detailsInfo: [
        {title: '流程卡号', label: 'flowCardNo', value: ''},
        {title: '分架人', label: 'splitPerson', value: ''},
        {title: '分架日期', label: 'splitDate', value: ''},
        {title: '分架数量（片）', label: 'splitNum', value: ''},
        {title: '分架面积（m²）', label: 'totalArea', value: ''},
        {title: '分架重量（t）', label: 'totalWeight', value: ''},
        {title: '已完成工艺', label: 'completeProcess', value: ''},
      ],
      detailDialogData: [],  //流程卡明细数据
      //流程卡表头
      detailDialogColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '120'},
        {label: '项目名称', prop: 'entryName', width: '180'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '楼层位置', prop: 'position', width: '100'},
        {label: '成品面', prop: 'itemSurface', width: '120'},
        {label: '单片名称', prop: 'itemName', width: '150'},
        {label: '高（mm）', prop: 'itemH', width: '100'},
        {label: '宽（mm）', prop: 'itemW', width: '100'},
        {label: '数量（片）', prop: 'itemNum', width: '100'},
        {label: '面积（m²）', prop: 'totalArea', width: '100'},
        {label: '重量（t）', prop: 'totalWeight', width: '100'},
        {label: '加工要求', prop: 'requirement', width: '120'},
      ],
      updateDialogData: [],  //编辑分架表单
      updateDialogColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '项目名称', prop: 'entryName', width: '200'},
        {label: '产品名称', prop: 'productName', width: '200'},
        {label: '楼层位置', prop: 'position', width: '100'},
        {label: '成品面', prop: 'itemSurface', width: '200'},
        {label: '单片名称', prop: 'itemName', width: '200'},
        {label: '分架数量（片）', prop: 'itemNum', width: '300'},
        {label: '高（mm）', prop: 'itemH', width: '110'},
        {label: '宽（mm）', prop: 'itemW', width: '110'},
        {label: '面积（m²）', prop: 'totalArea', width: '100'},
        {label: '重量（t）', prop: 'totalWeight', width: '100'},
        {label: '加工要求', prop: 'requirement', width: '150'},
      ],
      completedProcessDialog: false,  //工艺完工弹窗
      /*                        编辑流程卡                        */
      manualDialog: false,
      /*              未分架信息             */
      //未分架表格数据
      noManualData: [],
      //未分架表格展示内容
      noManualColumns: [
        {label: '订单编号', prop: 'orderNo', width: '80'},
        {label: '产品名称', prop: 'productName', width: '100'},
        {label: '位置', prop: 'position', width: '80'},
        {label: '成品面', prop: 'itemSurface', width: '100'},
        {label: '单片名称', prop: 'itemName', width: '100'},
        {label: '单片类型', prop: 'itemType', width: '100'},
        {label: '工艺流程', prop: 'processContent', width: '250'},
        {label: '厚度（mm）', prop: 'itemThick', width: '100'},
        {label: '宽（mm）', prop: 'itemWide', width: '100'},
        {label: '高（mm）', prop: 'itemHigh', width: '100'},
        // {label: '单片面积（m²）', prop: 'itemArea', width: '120'},
        // {label: '单片重量（t）', prop: 'itemWeight', width: '100'},
        {label: '产品总数（片）', prop: 'num', width: '120'},
        {label: '剩余数量（片）', prop: 'noShelfNum', width: '120'},
      ],
      //已分架数据
      haveManualData: [],
      //已分架表格展示内容
      haveManualColumns: [
        {label: '流程卡号', prop: 'flowCardNo', width: '100', className: 'bBGC'},
        {label: '订单编号', prop: 'orderNo', width: '100', className: 'bBGC'},
        {label: '产品名称', prop: 'productName', width: '100', className: 'bBGC'},
        {label: '位置', prop: 'position', width: '100', className: 'bBGC'},
        {label: '成品面', prop: 'itemSurface', width: '100', className: 'bBGC'},
        {label: '单片名称', prop: 'itemName', width: '100', className: 'bBGC'},
        {label: '宽（mm）', prop: 'itemW', width: '100', className: 'bBGC'},
        {label: '高（mm）', prop: 'itemH', width: '100', className: 'bBGC'},
        {label: '厚度（mm）', prop: 'itemThick', width: '100', className: 'bBGC'},
        {label: '分架数量（片）', prop: 'itemNum', width: '120'},
        {label: '面积（m²）', prop: 'totalArea', width: '100'},
        // {label: '重量（t）', prop: 'totalWeight', width: '100'},
        {label: '加工要求', prop: 'requirement', width: '100', className: 'bBGC'},
        {label: '现有分架数量（片）', prop: 'oldNum', width: '130'},
      ],
      //手动分架 - 未分架选择信息
      noManualChangeData: [],
      //手动分架 - 已分架选择信息
      manualChangeData: [],
      /* 合并类 */
      spanArr: [], // 一个空的数组，用于存放每一行记录的合并数
      pos: 0, // spanArr 的索引
      //旧分架数据
      oldItemNum: 0,
      /*修改Excel名称*/
      isUpdateName: false,
      updateType: 0,
      //Excel名称
      printName: ""
    }
  },
  created() {
    this.handleFlowQuery();
  },
  mounted() {
    var multipleFlowCardTable = this.$refs.multipleFlowCardTable.$refs.bodyWrapper;
    multipleFlowCardTable.addEventListener('scroll', () => {
      // 滚动距离
      const scrollLeft = multipleFlowCardTable.scrollLeft
      // this.$refs.multipleFlowCardTable.$refs.bodyWrapper.scrollLeft = scrollLeft
      this.$refs.multipleFlowCardTable.$refs.headerWrapper.scrollLeft = scrollLeft
    })
  },
  //设置表格表体高度自适应
  // updated() {
  //   this.$nextTick(() => {
  //     var multipleFlowCardTable = this.$refs.multipleFlowCardTable.$refs.footerWrapper;
  //     multipleFlowCardTable.style.display = 'block';
  //     this.$refs.multipleFlowCardTable.doLayout();
  //   })
  // },
  methods: {
    toPrint(printId) {
      if (this.selectFlowCard.length != 1) {
        Message.warning("请选择要打印的流程卡")
        return false;
      }
      /**
       * data为模板所需要参数
       * 例如需要根据id查询模板数据
       */

      let data = {flowCardNo: this.selectFlowCard[0].flowCardNo}
      const {href} = this.$router.resolve({
        path: '/print',
        query: {printId, ...data}
      })
      window.open(href, '_blank')
    },
    /* 搜索流程卡 */
    handleFlowQuery() {
      if (this.flowCardDateRange) {
        this.flowCardParams.splitDateStart = this.flowCardDateRange[0];
        this.flowCardParams.splitDateEnd = this.flowCardDateRange[1];
      } else {
        this.flowCardParams.splitDateStart = '';
        this.flowCardParams.splitDateEnd = '';
      }
      // this.flowCardParams.productionStatus=Number(this.flowCardParams.productionStatus);
      queryList(this.flowCardParams).then(res => {
        if (res.code === 200) {
          this.flowCardList = res.data;
          this.totalFlow = res.count;
          if (this.flowCardList) {
            this.summation = [
              {label: 'splitNum', title: '分架数量', value: 0, unit: '片'},
              {label: 'schedulingNum', title: '排产数量', value: 0, unit: '片'},
              {label: 'damageNum', title: '损坏数量', value: 0, unit: '片'},
              {label: 'patchNum', title: '补片数量', value: 0, unit: '片'},
              {label: 'totalArea', title: '总面积', value: 0, unit: 'm²'},
              {label: 'totalWeight', title: '总重量', value: 0, unit: 't'},
            ];
            this.flowCardList.forEach(item => {
              this.summation.forEach(sumItem => {
                if (item.hasOwnProperty(sumItem.label)) {
                  sumItem.value += item[sumItem.label]
                }
                sumItem.value = keepThreeNum(sumItem.value);
              })
              if (!item.damageNum) {
                item.damageNum = 0;
              }
              if (!item.patchNum) {
                item.patchNum = 0;
              }
              if (!item.schedulingNum) {
                item.schedulingNum = 0;
              }
            })
          }
        }
      })
    },
    /* 重置搜索 */
    resetFlowQuery() {
      this.flowCardDateRange = [];
      this.flowCardParams = {
        customNo: "",
        customerName: "",
        flowCardNo: "",
        id: "",
        monolithicName: "",
        orderNo: "",
        pageNum: 1,
        pageSize: 20,
        productName: "",
        productionStatus: null,
        splitDateEnd: "",
        splitDateStart: "",
        splitPerson: ""
      };
      this.handleFlowQuery();
    },
    /* 选择表单 */
    handleFlowCardChange(val) {
      this.selectFlowCard = val
    },
    /* 流程卡行点击事件 */
    handleFlowCardClick(row, column, event) {
      this.$refs.multipleFlowCardTable.toggleRowSelection(row, column)
    },
    /* 翻页后序号连贯 */
    getFlowCardIndex($index) {
      //  表格序号
      return (this.pageNumFlow - 1) * this.pageSizeFlow + $index + 1;
    },
    /* 查看明细 */
    handleDetail() {
      if (this.selectFlowCard.length != 1) {
        this.$message.warning("请选择一条流程卡信息！")
        return;
      }
      this.detailDialog = true;
      this.detailsInfo.forEach(sumItem => {
        if (this.selectFlowCard[0].hasOwnProperty(sumItem.label)) {
          sumItem.value = this.selectFlowCard[0][sumItem.label]
        }
      })
      queryFlowDetailed({flowId: this.selectFlowCard[0].id}).then(res => {
        this.detailDialogData = res.data;
      })
    },
    /* 新增流程卡 */
    handleAddFlow() {
      this.$router.push('/sales/flowCard/addFlowCard');
    },
    /* 编辑流程卡 */
    handleUpdateFlow() {
      if (this.selectFlowCard.length != 1) {
        this.$message.warning("请选择一条数据！")
        return;
      }
      checkFlowCard({id: this.selectFlowCard[0].id}).then(resport => {
        if (resport.code === 200) {
          updateQuery({flowId: this.selectFlowCard[0].id}).then(res => {
            if (res.code === 200) {
              this.noManualData = res.data.product
              this.haveManualData = res.data.shelf
              // 设定一个数组spanArr/contentSpanArr来存放要合并的格数，同时还要设定一个变量pos/position来记录
              this.spanArr = [];
              for (let i = 0; i < this.noManualData.length; i++) {
                if (i === 0) {
                  this.spanArr.push(1);
                  this.pos = 0;
                } else {
                  // 判断当前元素与上一个元素是否相同(第1和第2列)
                  if (this.noManualData[i].productId === this.noManualData[i - 1].productId) {
                    this.spanArr[this.pos] += 1;
                    this.spanArr.push(0);
                  } else {
                    this.spanArr.push(1);
                    this.pos = i;
                  }
                }
              }
              this.manualDialog = true;
            } else {
              this.$message.error(res.msg)
            }
          })
        } else {
          this.$message.warning(resport.msg)
        }
      })
    },
    /* 删除流程卡 */
    handleDeleteFlow() {
      if (this.selectFlowCard.length != 1) {
        this.$message.warning("请选择一条数据！")
        return;
      }
      this.$confirm('此操作将删除选中数据，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        checkFlowCard({id: this.selectFlowCard[0].id}).then(resport => {
          if (resport.code === 200) {
            delFlowCard({id: this.selectFlowCard[0].id}).then(res => {
              if (res.code === 200) {
                this.$message.success(res.msg)
                this.selectFlowCard = [];
                this.handleFlowQuery()
              } else {
                this.$message.warning(resport.msg)
                this.handleFlowQuery()
              }
            })
          } else {
            this.$message.warning(resport.msg)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

    },
    /* 工艺完工 */
    handleProcessComplete() {
      if (this.selectFlowCard.length != 1) {
        this.$message.warning("请选择一条数据！")
        return;
      }
      // if (this.selectFlowCard[0].productionStatus === 0) {
      //   this.$message.warning("当前流程卡未开始生产，无法进行完工管理！")
      //   return;
      // }
      this.$router.push({
        path: '/production/completion/addCompletion', query: {
          flowCardNo: this.selectFlowCard[0].flowCardNo
        }
      });
    },
    /* 打印 */
    handlePrint() {

    },
    /* 分页器 */
    handleFlowChange(size, num) {
      this.pageSizeFlow = size;
      this.pageNumFlow = num;
      this.flowCardParams.pageSize = this.pageSizeFlow;
      this.flowCardParams.pageNum = this.pageNumFlow;
      this.handleFlowQuery();
    },
    //导出
    exportFlow(param) {
      let ids = [];
      if (this.selectFlowCard.length > 0) {
        this.selectFlowCard.forEach(item => {
          ids.push(item.id)
        })
      }
      if (param === 0) {//导出流程卡
        this.download('/sales/flowCard/exportFlow', {
          ids: ids.toString()
        }, `流程卡.xlsx`)
      } else if (param === 1) {//导出流程卡明细
        this.download('/sales/flowCard/exportFlowDetailed', {
          ids: ids.toString()
        }, `流程卡明细.xlsx`)
      } else if (param === 2) {
        if (ids.length <= 0) {
          this.$message.warning("请至少选择一条流程卡的数据。")
          return;
        }
        this.isUpdateName = true;
        this.updateType = 1;
        this.printName = this.selectFlowCard[0].customerName + "(" + this.selectFlowCard[0].orderNo + ")";
      }else if (param === 3) {
        if (ids.length <= 0) {
          this.$message.warning("请至少选择一条流程卡的数据。")
          return;
        }
        this.isUpdateName = true;
        this.updateType = 2;
        this.printName = this.selectFlowCard[0].customerName + "(" + this.selectFlowCard[0].orderNo + ")";
      }
    },
    //导出机器相关excel
    exportData() {
      let ids = [];
      if (this.selectFlowCard.length > 0) {
        this.selectFlowCard.forEach(item => {
          ids.push(item.id)
        })
      }
      if (this.updateType === 1) {
        this.download('/sales/flowCard/exportMachineFlow', {
          id: ids.toString()
        }, this.printName + `.xlsx`)
      }else if (this.updateType === 2) {
        this.download('/sales/flowCard/exportOptima', {
          id: ids.toString()
        }, this.printName + `.xlsx`)
      }
      this.updateType = 0;
      this.isUpdateName = false;
    },
    //改变行颜色
    tableRowClassName({row, rowIndex}) {
      if (row.noShelfNum === 0) {
        return 'warning-row';
      } else if (row.noShelfNum !== 0 && row.noShelfNum !== row.num) {
        return 'success-row';
      }
      return '';
    },
    /*                     编辑流程卡方法                       */
    /* 合并行  查看工艺 */
    processSpanMethod({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1 || columnIndex === 2 || columnIndex === 3 || columnIndex === 4) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    },
    //未分架信息选择事件
    noManualDataChange(val) {
      this.noManualChangeData = val;
    },
    noManualRowClick(row, column, event) {
      if(column.label == "分架数量（片）"){
        return false;
      }
      this.$refs.noManualTable.toggleRowSelection(row, column)
    },
    //已分架信息选择事件
    manualDataChange(val) {
      this.manualChangeData = val;
    },
    manualDataClick(row, column, event) {
      if (column.label == '调整后分架数量（片）'){
        return false;
      }
      this.$refs.haveManualDataTable222.toggleRowSelection(row, column)
    },
    cellName({row, column, rowIndex, columnIndex}) {
      // console.log(column)
      if (column.label !== '调整后分架数量（片）' && column.label !== '现有分架数量（片）') {
        return 'bBGC'
      }
    },
    /* 生成uuid */
    generateUUID() {
      var d = new Date().getTime();
      if (window.performance && typeof window.performance.now === "function") {
        d += performance.now(); //use high-precision timer if available
      }
      var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
      });
      return uuid;
    },
    /*纳入并创建流程卡*/
    setFlowCard() {
      if (this.noManualChangeData.length === 0) {
        this.$message.warning("请至少选择一条数据！")
        return;
      }
      //先校验选择框中数据的   厚度、单片类型、工艺流程是否一致
      const newArr = this.noManualChangeData.map(item => item.itemThick)
      const newSet = new Set(newArr)
      if (newSet.size > 1) {
        this.$message.warning("请选择相同厚度的未分架信息")
        return;
      }
      const newArr1 = this.noManualChangeData.map(item => item.itemType)
      const newSet1 = new Set(newArr1)
      if (newSet1.size > 1) {
        this.$message.warning("请选择相同类型的未分架信息")
        return;
      }
      const newArr2 = this.noManualChangeData.map(item => item.processId)
      const newSet2 = new Set(newArr2)
      if (newSet2.size > 1) {
        this.$message.warning("请选择相同工艺流程的未分架信息")
        return;
      }
      const newArr3 = this.noManualChangeData.map(item => item.orderNo)
      const newSet3 = new Set(newArr3)
      if (newSet3.size > 1) {
        this.$message.warning("请选择相同订单的未分架信息")
        return;
      }
      let oldFlowNumber = null;
      if (this.haveManualData.length > 0) {
        oldFlowNumber = this.haveManualData[this.haveManualData.length - 1].flowCardNo
      }
      for (let i = 0; i < this.noManualChangeData.length; i++) {
        if (this.noManualChangeData[i].shelfNum > this.noManualChangeData[i].noShelfNum) {
          this.$message.warning("选中数据中，分架数量不得大于剩余数量！")
          return;
        }
      }
      //生成流程卡编号
      generateFlowNumber({oldFlowNumber: oldFlowNumber}).then(res => {
        this.noManualChangeData.forEach(item => {
          let area = item.shelfNum * item.itemArea;
          let weight = item.shelfNum * item.itemWeight;
          let uuid = this.generateUUID()
          //流程卡明细对象
          let flowDetailed = {
            uuid: uuid,
            id: item.id,
            productId: item.productId,
            semiProductId: item.semiProductId,
            productProcessId: item.id,
            productName: item.productName,
            position: item.position,
            itemSurface: item.itemSurface,
            processContent: item.processContent,
            flowCardNo: res.msg,
            orderNo: item.orderNo,
            customNo: item.customNo,
            entryName: item.entryName,
            customerName: item.customerName,
            itemName: item.itemName,
            itemType: item.itemType,
            processId: item.processId,
            itemThick: item.itemThick,
            itemW: item.itemWide,
            itemH: item.itemHigh,
            itemNum: item.shelfNum,
            totalArea: area,
            totalWeight: weight,
            requirement: item.requirement,
            oldNum: 0
          }
          this.haveManualData.push(flowDetailed)
          if (item.shelfNum <= item.noShelfNum) {
            item.noShelfNum = item.noShelfNum - item.shelfNum
          }
        })
        this.$refs.noManualTable.clearSelection();
      })
    },
    //纳入指定流程卡
    setLatelyFlowCard() {
      if (this.noManualChangeData.length === 0) {
        this.$message.warning("请至少选择一条数据！")
        return;
      }
      if (this.manualChangeData.length !== 1) {
        this.$message.warning("请选择一条已分架信息！")
        return;
      }
      //先校验选择框中数据的   厚度、单片类型、工艺流程是否一致
      const newArr = this.noManualChangeData.map(item => item.itemThick)
      const newSet = new Set(newArr)
      if (newSet.size > 1) {
        this.$message.warning("请选择相同厚度的未分架信息")
        return;
      }
      const newArr1 = this.noManualChangeData.map(item => item.itemType)
      const newSet1 = new Set(newArr1)
      if (newSet1.size > 1) {
        this.$message.warning("请选择相同类型的未分架信息")
        return;
      }
      const newArr2 = this.noManualChangeData.map(item => item.processId)
      const newSet2 = new Set(newArr2)
      if (newSet2.size > 1) {
        this.$message.warning("请选择相同工艺流程的未分架信息")
        return;
      }
      const newArr3 = this.noManualChangeData.map(item => item.orderNo)
      const newSet3 = new Set(newArr3)
      if (newSet3.size > 1) {
        this.$message.warning("请选择相同订单的未分架信息")
        return;
      }
      //已分架信息选择内容
      let two = this.manualChangeData[0]
      if (newArr3[0] != two.orderNo) {
        this.$message.warning("请选择与流程卡: " + two.flowCardNo + "，订单编号相同的未分架信息")
        return;
      }
      if (newArr[0] != two.itemThick) {
        this.$message.warning("请选择与流程卡: " + two.flowCardNo + "，厚度相同的未分架信息")
        return;
      }
      if (newArr1[0] != two.itemType) {
        this.$message.warning("请选择与流程卡: " + two.flowCardNo + "，单片类型相同的未分架信息")
        return;
      }
      if (newArr2[0] != two.processId) {
        this.$message.warning("请选择与流程卡: " + two.flowCardNo + "，工艺流程相同的未分架信息")
        return;
      }
      for (let i = 0; i < this.noManualChangeData.length; i++) {
        if (this.noManualChangeData[i].shelfNum > this.noManualChangeData[i].noShelfNum) {
          this.$message.warning("选中数据中，分架数量不得大于剩余数量！")
          return;
        }
      }
      this.noManualChangeData.forEach(item => {
        let area = item.shelfNum * item.itemArea;
        let weight = item.shelfNum * item.itemWeight;
        let uuid = this.generateUUID()
        //流程卡明细对象
        let flowDetailed = {
          uuid: uuid,
          id: item.id,
          productId: item.productId,
          semiProductId: item.semiProductId,
          productName: item.productName,
          position: item.position,
          itemSurface: item.itemSurface,
          processContent: item.processContent,
          flowCardNo: two.flowCardNo,
          orderNo: item.orderNo,
          customNo: item.customNo,
          entryName: item.entryName,
          customerName: item.customerName,
          itemName: item.itemName,
          itemType: item.itemType,
          processId: item.processId,
          itemThick: item.itemThick,
          itemW: item.itemWide,
          itemH: item.itemHigh,
          itemNum: item.shelfNum,
          totalArea: area,
          totalWeight: weight,
          requirement: item.requirement,
          oldNum: 0
        }
        this.haveManualData.push(flowDetailed)
        if (item.shelfNum <= item.noShelfNum) {
          item.noShelfNum = item.noShelfNum - item.shelfNum
        }/*else {
            let index = this.noManualData.indexOf(item)
            this.noManualData.splice(index,1)
          }*/
      })
      this.$refs.noManualTable.clearSelection();

    },
    /* 删除流程卡 */
    delFlowCard() {
      if (this.manualChangeData.length <= 0) {
        this.$message.warning("请选择至少一条数据！")
        return;
      }
      //遍历选中的分架信息
      this.manualChangeData.forEach(item => {
        //修改未分架信息中对应的剩余数量
        this.noManualData.forEach(one => {
          if (item.productId === one.productId && item.itemSurface === one.itemSurface) {
            one.noShelfNum = Number(one.noShelfNum) + Number(item.itemNum);
          }
        })
        let index = this.haveManualData.indexOf(item)
        this.haveManualData.splice(index, 1)
      })
    },
    //清空流程卡
    delAllFlowCard() {
      this.$confirm('是否清空所有流程卡?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        size: 'mini'
      }).then(() => {
        this.haveManualData.forEach(item => {
          this.noManualData.forEach(one => {
            if (item.productId === one.productId && item.itemSurface === one.itemSurface) {
              one.noShelfNum = Number(one.noShelfNum) + Number(item.itemNum);
            }
          })
        })
        this.haveManualData = []
        this.$message.success("清空成功！")
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消清空'
        });
      });
    },
    /* 保存手动分架 */
    saveManual() {
      const set = new Set()
      //查找A，B片数量不同的产品
      this.noManualData.forEach(one => {
        this.noManualData.forEach(two => {
          if (one.id !== two.id && one.productId === two.productId && one.noShelfNum !== two.noShelfNum) {
            if (one.noShelfNum !== two.noShelfNum) {
              set.add(one.productId)
            }
          }
        })
      })
      let msg = "";
      set.forEach(s => {
        for (let i = 0; i < this.noManualData.length; i++) {
          if (this.noManualData[i].productId === s) {
            if (msg === "") {
              msg = (i + 1) + "”的“" + this.noManualData[i].productName;
            } else {
              msg = msg + "”、“" + (i + 1) + "”的“" + this.noManualData[i].productName;
            }
            break;
          }
        }
      })
      let params = {
        noManualData: this.noManualData,
        haveManualData: this.haveManualData,
        isNew: 2
      }
      if (msg !== "") {
        // this.$message.warning("序号为“" + msg + "”中每个单片的剩余数量不同，请修改！")
        this.$confirm("序号为“" + msg + "”中每个单片的剩余数量不同，是否要生成新的流程卡，还是直接修改其所在流程卡信息(以最小值为最终分架数量)?", '提示', {
          distinguishCancelAndClose: true,
          confirmButtonText: '生成新流程卡',
          cancelButtonText: '修改旧流程卡',
          type: 'warning',
          size: 'mini'
        }).then(() => {
          params.isNew = 0
          saveUpdate(params).then(res => {
            if (res.code === 200) {
              this.$message.success(res.msg);
              this.handleFlowQuery();
            } else {
              this.$message.error(res.msg);
              this.handleFlowQuery();
            }
          })
        }).catch(action => {
          if (action == "cancel") {
            params.isNew = 1
            saveUpdate(params).then(res => {
              if (res.code === 200) {
                this.$message.success(res.msg);
                this.handleFlowQuery();
              } else {
                this.$message.error(res.msg);
                this.handleFlowQuery();
              }
            })
            // this.handleFlowQuery();
          }
        });
      } else {
        saveUpdate(params).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg);
            this.handleFlowQuery();
          } else {
            this.$message.error(res.msg);
            this.handleFlowQuery();
          }
        })

      }
      this.manualDialog = false;
      // this.handleFlowQuery();
    },
    //已分架信息修改事件
    itemNumChange(row) {
      //判断修改前的数量  是否小于   修改后的数量
      this.$nextTick(() => {
        if (!row.itemNum) {
          row.itemNum = 0
        }
        if (Number(this.oldItemNum) < Number(row.itemNum)) {
          //新数量与旧数量的差
          let x = row.itemNum - this.oldItemNum
          console.log("x", row.itemNum, this.oldItemNum, x)
          //遍历未分架信息
          for (let i = 0; i < this.noManualData.length; i++) {
            let item = this.noManualData[i]
            if (item.productId === row.productId && item.itemSurface === row.itemSurface) {
              if (Number(item.noShelfNum) < Number(x)) {
                this.$message.warning("序号为“" + (i + 1) + "”的产品剩余数量不足，请重新填写！")
                console.log('row.itemNum', row.itemNum, 'this.oldItemNum', this.oldItemNum)
                row.itemNum = this.oldItemNum;
                // return;
                this.$set(row, 'itemNum', this.oldItemNum)
                console.log('row.itemNum', row.itemNum, 'this.oldItemNum', this.oldItemNum)
              } else {
                item.noShelfNum = item.noShelfNum - (row.itemNum - this.oldItemNum);
                item.shelfNum = item.noShelfNum;
                //修改已分架信息中的面积、重量
                row.totalArea = keepThreeNum(row.itemNum * item.itemArea);
                row.totalWeight = keepThreeNum(row.itemNum * item.itemWeight)
              }
            }
          }
        } else {
          //修改未分架信息中的剩余数量  和  可分架数量
          this.noManualData.forEach(one => {
            if (one.productId === row.productId && one.itemSurface === row.itemSurface) {
              one.noShelfNum = this.oldItemNum - row.itemNum + one.noShelfNum;
              one.shelfNum = one.noShelfNum;
              row.totalArea = keepThreeNum(row.itemNum * one.itemArea);
              row.totalWeight = keepThreeNum(row.itemNum * one.itemWeight);
            }
          })
        }
      })


    },
    //已分架信息中  分架数量获取焦点事件
    itemNumClick(itemNum) {
      this.oldItemNum = itemNum;
      // alert(this.oldItemNum)
      console.log("旧数据=", this.oldItemNum)
    },
  }
}
</script>

<style lang="scss" scoped>

::v-deep .success-row {
  color: #e52b47 !important;
}

::v-deep .warning-row {
  color: #43f51b !important;
}

::v-deep .del-arrow input::-webkit-outer-spin-button,
::v-deep .del-arrow input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
}

::v-deep .del-arrow input[type="number"] {
  -moz-appearance: textfield;
}

.rightTable {
  height: calc(100% - 105px);
}

::v-deep .naturalDialog {
  .el-dialog {
    height: 80%;

    .el-dialog__body {
      height: calc(100% - 90px);

      .naturalPointsTable {
        height: calc(100% - 200px);

      }

      .manualTable {
        height: calc(50% - 45px);
        overflow: hidden;

      }

      .title {
        //padding-left: 10px;
        height: 30px;
        line-height: 30px;
        font-weight: 600;
        text-align: left;
        border: none;
      }

      .manualBtn {
        padding: 5px 5px 5px 0;
        width: 100%;
        display: flex;
        justify-content: space-between;
      }

      .table {
        height: 100%;
        border-bottom: 1px solid #dfe6ec;

        .changeNum {
          //background-color: red;
          .el-input-number {
            .el-input__inner {
              //text-align: center;
              padding-left: 10px;
            }
          }
        }
      }

      .page {
        display: none;
      }
    }
  }
}

::v-deep .manualDialog {
  .el-dialog__body {
    height: calc(100% - 50px) !important;
  }
}

::v-deep .bBGC {
  background-color: rgba(204, 204, 204, 0.18);
}

</style>
