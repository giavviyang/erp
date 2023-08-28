<template>
  <div class="app-container-padding">
    <el-form :model="createFlowParams" ref="createFlowForm" size="mini" :inline="true" class="iptAndBtn"
             style="position: relative">
      <div>
        <el-form-item label="下单日期" class="daterange">
          <el-date-picker
            v-model="createFlowDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd" @change="handleCreateFlow">
          </el-date-picker>
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleCreateFlow">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetCreateFlow">重置</el-button>
      </div>
<!--      <div style="right:15px;position: fixed;">-->
<!--        <el-button type="primary" icon="el-icon-refresh-left" size="mini" @click="addBack" class="back">返回</el-button>-->
<!--      </div>-->
    </el-form>
    <div class="btn" style="display: flex;justify-content: space-between;align-items: center;">
      <div>
      <el-button
        type="primary"
        icon="el-icon-files"
        size="mini"
        v-hasPermi="['sales:addFlow:automatic']"
        @click="handleOpenNatural">自动分架
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit-outline"
        size="mini"
        v-hasPermi="['sales:addFlow:manual']"
        @click="handleOpenManual">手动分架
      </el-button>
      </div>
      <div style="padding-right: 20px;color:red;">
        表格内展示的数据为未分架流程卡的订单信息</div>
    </div>
    <count-table class="rightTable" @handleChange="handleCreateFlowPage" :pageSize="pageSizeCreateFlow"
                 :pageNum="pageNumCreateFlow"
                 :total="totalCreateFlow" :summation='summation'>
      <el-table highlight-current-row
                :data="createFlowList"
                stripe
                border
                style="width: 100%"
                height="100%"
                ref="multipleCreateFlowTable"
                @selection-change="handleCreateFlowChange"
                @row-click="handleCreateFlowClick"
                slot="table">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          :index="getCreateFlowIndex"
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="(item,index) in createFlowListColumn"
          :key="index"
          :label="item.label"
          show-overflow-tooltip>
          <el-table-column :prop="item.prop" show-overflow-tooltip min-width="100" :min-width="item.width">
            <template #header scoped-slot="scope">
              <!--可根据类型来显示为搜索框、下拉框等-->
              <el-input
                v-if="item.type==='ipt'"
                v-model="createFlowParams[item.prop]"
                size="mini"
                placeholder="请输入"
                clearable @keyup.enter.native="handleCreateFlow"/>
              <el-select v-if="item.type==='select'" v-model="createFlowParams[item.prop]" placeholder='请选择'
                         clearable
                         size="mini" ref='statusSelect' @change="handleCreateFlow">
                <el-option
                  v-for="item in dict.type.order_type"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </count-table>
    <!--  自动分架  -->
    <el-dialog title="自动分架" :visible.sync="naturalDialog" width="90%"
               class="dialog-style naturalDialog" :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <slot-table class="naturalPointsTable">
        <el-table highlight-current-row
                  :data="naturalPointsData"
                  :span-method="processSpanMethodOne"
                  stripe
                  border
                  height="100%"
                  slot="table">
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column v-for="(item,index) in naturalPointsDataColumn"
                           :key="index"
                           :prop="item.prop"
                           :label="item.label"
                           :min-width="item.width" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            label="此次分架数量" width="165">
            <template slot-scope="scope">
              <!--              item.num - item.shelfNum-->
              <el-input-number size="mini" v-model="scope.row.noShelfNum"
                               :min="1"
                               :max="Number(scope.row.num)-Number(scope.row.shelfNum)"
                               :precision="0" style="width: 140px"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column
            label="操作" class-name="operation">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="removeRow(scope.row)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <div class="parameterSetting">
        <p class="title1">分组参数设置-同流程卡中</p>
        <el-form :model="naturalPointsForm" ref="naturalPointsForm" size="mini" :rules="rules" :inline="true"
                 class="parameterSettingForm">
          <el-form-item label="玻璃片数不超过：" prop="number">
            <el-input placeholder="请输入" v-model="naturalPointsForm.number" clearable>
              <template slot="append">片</template>
            </el-input>
          </el-form-item>
          <el-form-item label="玻璃面积：" prop="area">
            <el-input placeholder="请输入" v-model="naturalPointsForm.area" clearable>
              <template slot="append">㎡</template>
            </el-input>
          </el-form-item>
          <el-form-item label="玻璃重量：" prop="weight">
            <el-input placeholder="请输入" v-model="naturalPointsForm.weight" clearable>
              <template slot="append">kg</template>
            </el-input>
          </el-form-item>
          <el-form-item label="最大叠厚：" prop="fold">
            <el-input placeholder="请输入" v-model="naturalPointsForm.fold" clearable>
              <template slot="append">mm</template>
            </el-input>
          </el-form-item>
          <el-form-item label="宽差距（最大）：" prop="wideBig">
            <el-input placeholder="请输入" v-model="naturalPointsForm.wideBig" clearable>
              <template slot="append">mm</template>
            </el-input>
          </el-form-item>
          <el-form-item label="宽差距（最小）：" prop="wideSmall">
            <el-input placeholder="请输入" v-model="naturalPointsForm.wideSmall" clearable disabled>
              <template slot="append">mm</template>
            </el-input>
          </el-form-item>
          <el-form-item label="高差距（最大）：" prop="highBig">
            <el-input placeholder="请输入" v-model="naturalPointsForm.highBig" clearable>
              <template slot="append">mm</template>
            </el-input>
          </el-form-item>
          <el-form-item label="高差距（最小）：" prop="highSmall">
            <el-input placeholder="请输入" v-model="naturalPointsForm.highSmall" clearable disabled>
              <template slot="append">mm</template>
            </el-input>
          </el-form-item>
          <el-form-item label="楼层位置：">
            <el-switch v-model="naturalPointsForm.isPosition" active-text="相同" inactive-text="不相同">>
            </el-switch>
          </el-form-item>
          <el-form-item label="排序方式：">
            <el-select v-model="naturalPointsForm.sortord">
              <el-option
                v-for="orderItem in sortordOptions"
                :key="orderItem.value"
                :label="orderItem.label"
                :value="orderItem.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <!--  分架二次弹窗 -->
      <el-dialog title="分架预览" :visible.sync="naturalPointsPlaneDialog" width="80%"
                 class="dialog-style naturalPointsPlaneDialog" append-to-body :close-on-click-modal="false"
                 :close-on-press-escape="false" :destroy-on-close="true">
        <slot-table class=" naturalPointsPlane">
          <el-table highlight-current-row
                    :data="naturalPointsPlaneData"
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
              v-for="(item,index) in naturalPointsPlaneColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="120">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="handleDetail(scope.row)"
                  type="text"
                  size="small">
                  查看明细
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </slot-table>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" size="mini" @click="saveNaturalPointsPlane">确认分架</el-button>
          <el-button size="mini" @click="naturalPointsPlaneDialog=false">取消</el-button>
        </span>
      </el-dialog>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="handlePointsPlane('naturalPointsForm')">分架</el-button>
         <el-button size="mini" @click="naturalDialog=false">返回</el-button>
      </span>
    </el-dialog>
    <!-- 手动分架 -->
    <el-dialog title="手动分架" :visible.sync="manualDialog" width="95%"
               class="dialog-style naturalDialog manualDialog" :close-on-click-modal="false" ref="manualDialog"
               :close-on-press-escape="false" :destroy-on-close="true">
      <div class="title1" style="padding-left: 0; display: flex; justify-content: space-between;">
        未分架信息
        <div>
          半自动分架:
          <el-switch v-model="isOpen"></el-switch>
        </div>
        <div>
          <i class="el-icon-star-on" style="color:#e52b47; font-size: 16px;">部分分架</i>
          <i class="el-icon-star-on" style="color:#43f51b; font-size: 16px;">分架完成</i>
        </div>
      </div>
      <slot-table class="manualTable">
        <el-table highlight-current-row
                  ref="noManualTable"
                  :data="noManualData"
                  :row-class-name="tableRowClassName"
                  border
                  stripe
                  height="100%"
                  :span-method="processSpanMethod"
                  @selection-change="noManualDataChange"
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
          <el-table-column
            v-for="(item,index) in noManualFixedColumns"
            fixed="right"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width" show-overflow-tooltip></el-table-column>
          <el-table-column
            label="本次分架数量（片）" width="170" fixed="right">
            <template slot-scope="scope">
              <!--              item.num - item.shelfNum-->
              <el-input-number size="mini" v-model="scope.row.shelfNum" :min="0"
                               :max="Number(scope.row.noShelfNum)" style="width: 140px"></el-input-number>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <div class="manualBtn">
        <div class="title1" style="padding-left: 0;">
          已分架信息
          <div style="display: inline-block; color: red; padding-left: 100px">
            待分架数量：{{ this.numQuery.one }}片;
            已分架数量：{{ this.numQuery.two }}
          </div>
        </div>
        <div>
          <el-button
            type="primary"
            icon="el-icon-document-add"
            @click="setFlowCard"
            size="mini">纳入并创建流程卡
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-document"
            @click="setLatelyFlowCard"
            size="mini">纳入最近流程卡
          </el-button>
        </div>
      </div>
      <slot-table class="manualTable">
        <el-table highlight-current-row
                  ref="haveManualDataTable"
                  :data="haveManualData"
                  border
                  :span-method="processSpanMethodTwo"
                  @selection-change="manualDataChange"
                  @row-click="manualDataClick"
                  :row-class-name="tableRowClassName2"
                  height="100%"
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
            v-for="(item,index) in haveManualColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width"
            show-overflow-tooltip></el-table-column>
        </el-table>
      </slot-table>
      <div style="padding: 5px 5px 5px 0; float: right">
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
  </div>
</template>

<script>

import SlotTable from "@/components/public/table/SlotTable";
import SummaryTable from "@/components/public/table/summaryTable";
import CommonDialog from "@/components/public/Dialog/commonDialog";
import {
  queryFlowOrder,
  selectProduct,
  automaticShelf,
  saveFlowCard,
  selectSemiProduct,
  generateFlowNumber,
  saveSemiProduct, queryFlowDetailed
} from "@/api/salse/flowCard/flowCard";
import {keepThreeNum, keepOneNum} from "@/utils/order/order";
import CountTable from "@/components/public/table/countTable";

export default {
  dicts: ['order_type'],
  name: "addCreateFlow",
  components: {CountTable, CommonDialog, SummaryTable, SlotTable},
  data() {
    //数值、小数点验证
    var checkArea = (rule, value, callback) => {
      let reg = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g;
      if (!reg.test(value)) {
        callback(new Error('请输入数值'));
      } else {
        callback();
      }
    };
    return {
      detailDialog:false,
      detailDialogData:[],
      //明细信息
      detailsInfo: [
        {title: '流程卡号', label: 'flowCardNo', value: ''},
        {title: '分架人', label: 'splitPerson', value: ''},
        {title: '分架日期', label: 'splitDate', value: ''},
        {title: '分架数量（片）', label: 'splitNum', value: ''},
        {title: '分架面积（m²）', label: 'totalArea', value: ''},
        {title: '分架重量（t）', label: 'totalWeight', value: ''}
      ],
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
      //查询订单参数
      createFlowParams: {
        pageNum: 1,
        pageSize: 20,
        contactNumber: '',
        contacts: '',
        customNo: '',
        customerName: '',
        entryName: '',
        id: '',
        isCache: 0,
        orderNo: '',
        orderType: '',
        preparationDateEnd: '',
        preparationDateStart: '',
        preparer: '',
        isDel: 0,
      },
      createFlowDateRange: [],    //新建流程卡日期范围查询
      createFlowList: [],   //新建流程卡表单
      //选中数据
      selects: [],
      //新建流程卡表头
      createFlowListColumn: [
        {label: '订单编号', prop: 'orderNo', width: '140', type: 'ipt',},
        {label: '订单类型', prop: 'orderType', width: '100', type: 'select'},
        {label: '自定义编号', prop: 'customNo', width: '120', type: 'ipt'},
        {label: '客户名称', prop: 'customerName', width: '120', type: 'ipt'},
        {label: '项目名称', prop: 'entryName', width: '200', type: 'ipt'},
        {label: '联系人', prop: 'contacts', type: 'ipt'},
        {label: '联系电话', prop: 'contactNumber', width: '120', type: 'ipt'},
        {label: '总数量（片）', prop: 'orderNum', width: '100'},
        {label: '总面积（m²）', prop: 'totalArea', width: '100'},
        {label: '总重量（t）', prop: 'totalWeigh', width: '100'},
        {label: '总金额（元）', prop: 'orderAmount', width: '120'},
        {label: '下单时间', prop: 'preparationDate', width: '180'},
        {label: '下单人', prop: 'preparer', type: 'ipt'},
        // {label: '审核人', prop: 'orderReview.reviewedBy'},
        // {label: '审核时间', prop: 'orderReview.auditTime', width: '180'},
        // {label: '创建时间', prop: 'createdAt', width: '180'},
      ],
      orderTypeOptions: [{
        value: '普通订单',
        label: '普通订单'
      }, {
        value: '加急订单',
        label: '加急订单'
      }, {
        value: '样品订单',
        label: '样品订单'
      }, {
        value: '来料订单',
        label: '来料订单'
      }, {
        value: '外协订单',
        label: '外协订单'
      }],
      pageSizeCreateFlow: 20, //新建流程卡分页
      pageNumCreateFlow: 1,
      totalCreateFlow: 0,
      //总合计
      summation: [
        {label: 'orderNum', title: '总数量', value: 0, unit: '片'},
        {label: 'totalArea', title: '总面积', value: 0, unit: 'm²'},
        {label: 'totalWeigh', title: '总重量', value: 0, unit: 't'},
        {label: 'orderAmount', title: '总金额', value: 0, unit: '元'},
      ],
      naturalDialog: false,   //自然分架弹窗
      /*自然分架表格*/
      naturalPointsData: [],
      //自然分架中   分架产品表头
      naturalPointsDataColumn: [
        {label: '订单编号', prop: 'orderNo', width: '140'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '120'},
        {label: '项目名称', prop: 'entryName', width: '150'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '楼层位置', prop: 'position', width: '100'},
        {label: '宽（mm）', prop: 'wideHead', width: '100'},
        {label: '高（mm）', prop: 'highHead', width: '100'},
        {label: '数量（片）', prop: 'num', width: '100'},
        {label: '已分架数量（片）', prop: 'shelfNum', width: '120'},
        {label: '加工要求', prop: 'requirement', width: '100'},
      ],
      //自然分架表单
      naturalPointsForm: {
        number: '',
        area: '',
        weight: '',
        fold: '',
        wideBig: '',
        wideSmall: '',
        highBig: '',
        highSmall: '',
        isPosition: null,
        sortord: 'area',
      },
      sortordOptions: [
        {
          value: 'area',
          label: '按面积（m²）'
        },
        {
          value: 'num',
          label: '按数量（片）'
        },
        {
          value: 'weight',
          label: '按重量（t）'
        },
      ],
      //分架产品和参数
      naturalPointsPlaneParameter: {
        //分架产品
        orderProducts: [],
        //分架参数
        parameter: {}
      },
      naturalPointsPlaneDialog: false,   //自然分架下分架弹窗
      naturalPointsPlaneData: [],   //自然分架下分架表格
      naturalPointsPlaneColumns: [
        {label: '流程卡号', prop: 'flowCardNo', width: '140'},
        {label: '产品名称', prop: 'productName', width: '200'},
        {label: '单片名称+品类', prop: 'monolithicName', width: '200'},
        {label: '厚度（mm）', prop: 'monolithicThick', width: '100'},
        {label: '数量（片）', prop: 'splitNum', width: '100'},
        {label: '面积（m²）', prop: 'totalArea', width: '100'},
        {label: '重量（kg）', prop: 'totalWeight', width: '100'},
      ],
      manualDialog: false,   //手动分架弹窗
      isOpen: false,         //是否开启半自动分架
      noManualData: [],   //未手动分架表格
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
      ],
      noManualFixedColumns: [
        {label: '产品总数（片）', prop: 'num', width: '120'},
        {label: '可分架数量（片）', prop: 'noShelfNum', width: '130'},
      ],
      numQuery: {
        one: 0,
        two: 0
      },
      haveManualData: [],   //未手动分架表格
      haveManualColumns: [
        {label: '流程卡号', prop: 'flowCardNo', width: '140'},
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '位置', prop: 'position', width: '120'},
        {label: '成品面', prop: 'itemSurface', width: '100'},
        {label: '单片名称', prop: 'itemName', width: '100'},
        {label: '单片类型', prop: 'itemType', width: '100'},
        {label: '宽（mm）', prop: 'itemW', width: '100'},
        {label: '高（mm）', prop: 'itemH', width: '100'},
        {label: '厚度（mm）', prop: 'itemThick', width: '100'},
        {label: '分架数量（片）', prop: 'itemNum', width: '120'},
        {label: '面积（m²）', prop: 'totalArea', width: '100'},
        // {label: '重量（t）', prop: 'totalWeight', width: '100'},
        {label: '加工要求', prop: 'requirement', width: '120'},
      ],
      //手动分架 - 未分架选择信息
      noManualChangeData: [],
      //手动分架 - 已分架选择信息
      manualChangeData: [],
      /* 合并类 */
      spanArr: [], // 一个空的数组，用于存放每一行记录的合并数
      pos: 0, // spanArr 的索引
      /* 自动分架 合并类 */
      spanArrOne: [], // 一个空的数组，用于存放每一行记录的合并数
      posOne: 0, // spanArrOne 的索引
      /* 手动分架 已分架信息  合并类 */
      spanArrTwo: [], // 一个空的数组，用于存放每一行记录的合并数
      posTwo: 0, // spanArrOne 的索引
      // 表单校验
      rules: {
        number: [
          // {type: 'number', message: '请输入整数', trigger: 'blur' }
          {pattern: /^[1-9]\d*$/, message: '请输入整数'}
        ],
        area: [
          // {validator: checkArea, change: 'blur'}
          {pattern: /^[+]?(0|([1-9]\d*))(\.\d+)?$/g, message: '请输入整数或小数'}
        ],
        weight: [
          {pattern: /^[+]?(0|([1-9]\d*))(\.\d+)?$/g, message: '请输入整数或小数'}
        ],
        fold: [
          // {type: 'number', message: '请输入整数', trigger: 'blur' }
          {pattern: /^[1-9]\d*$/, message: '请输入整数'}
        ],
        wideBig: [
          // {type: 'number', message: '请输入整数', trigger: 'blur' }
          {pattern: /^[1-9]\d*$/, message: '请输入整数'}
        ],
        wideSmall: [
          // {type: 'number', message: '请输入整数', trigger: 'blur' }
          {pattern: /^[1-9]\d*$/, message: '请输入整数'}
        ],
        highBig: [
          // {type: 'number', message: '请输入整数', trigger: 'blur' }
          {pattern: /^[1-9]\d*$/, message: '请输入整数'}
        ],
        highSmall: [
          // {type: 'number', message: '请输入整数', trigger: 'blur' }
          {pattern: /^[1-9]\d*$/, message: '请输入整数'}
        ],
      },
    }
  },
  created() {
    this.handleCreateFlow()
  },
  mounted() {
    // this.keyupSubmit();
    //合计行滚动条
    var multipleCreateFlowTable = this.$refs.multipleCreateFlowTable.$refs.bodyWrapper;
    multipleCreateFlowTable.addEventListener('scroll', () => {
      // 滚动距离
      const scrollLeft = multipleCreateFlowTable.scrollLeft
      // this.$refs.multipleCreateFlowTable.$refs.bodyWrapper.scrollLeft = scrollLeft
      this.$refs.multipleCreateFlowTable.$refs.headerWrapper.scrollLeft = scrollLeft
    })
  },
  //设置表格表体高度自适应
  updated() {
    this.$nextTick(() => {
      // this.$refs.multipleCreateFlowTable.$refs.footerWrapper.style.display = 'block';
      this.$refs.multipleCreateFlowTable.doLayout();
    })
  },
  methods: {
    handleDetail(row) {
      let {list,...data} = row
      this.detailDialogData = list
      this.detailsInfo.forEach(sumItem => {
        if (data.hasOwnProperty(sumItem.label)) {
          sumItem.value = data[sumItem.label]
        }
      })
      this.detailDialog = true;
    },
    //键盘按下enter搜索事件
    keyupSubmit() {
      document.onkeydown = e => {
        const _key = window.event.keyCode
        if (_key === 13) {
          this.handleCreateFlow();
        }
      }
    },
    /* 新建流程卡查询 */
    handleCreateFlow() {
      if (this.createFlowDateRange) {
        this.createFlowParams.preparationDateStart = this.createFlowDateRange[0];
        this.createFlowParams.preparationDateEnd = this.createFlowDateRange[1];
      } else {
        this.createFlowParams.preparationDateStart = '';
        this.createFlowParams.preparationDateEnd = '';
      }
      queryFlowOrder(this.createFlowParams).then(res => {
        if (res.code === 200) {
          // console.log('res', res)
          this.totalCreateFlow = res.count;
          this.createFlowList = res.data;
          if (this.createFlowList) {
            this.summation = [
              {label: 'orderNum', title: '总数量', value: 0, unit: '片'},
              {label: 'totalArea', title: '总面积', value: 0, unit: 'm²'},
              {label: 'totalWeigh', title: '总重量', value: 0, unit: 't'},
              {label: 'orderAmount', title: '总金额', value: 0, unit: '元'},
            ];
            this.createFlowList.forEach(item => {
              this.summation.forEach(sumItem => {
                if (item.hasOwnProperty(sumItem.label)) {
                  sumItem.value += item[sumItem.label]
                }
                sumItem.value = keepThreeNum(sumItem.value);
                if (sumItem.label === "orderAmount"){
                  sumItem.value = keepOneNum(sumItem.value);
                }
              })
            })
          }
        }
      })
    },
    /* 重置流程卡查询 */
    resetCreateFlow() {
      this.createFlowDateRange = [];
      this.createFlowParams = {
        pageNum: 1,
        pageSize: 20,
        contactNumber: '',
        contacts: '',
        customNo: '',
        customerName: '',
        entryName: '',
        id: '',
        isCache: 0,
        orderNo: '',
        orderType: '',
        preparationDateEnd: '',
        preparationDateStart: '',
        preparer: '',
        isDel: 0,
      };
      this.handleCreateFlow();
    },
    /* 返回主页面 */
    addBack() {
      this.$router.go(-1);
    },
    /*新增流程卡翻页序号连贯*/
    getCreateFlowIndex($index) {
      //  表格序号
      return (this.pageNumCreateFlow - 1) * this.pageSizeCreateFlow + $index + 1;
    },
    // 是否显示下拉框
    isShowSelectOptions(isShowSelectOptions) {
      if (!isShowSelectOptions) {
        this.$refs.statusSelect.forEach(item => {
          item.blur();
          this.handleCreateFlow();
        })
      }
    },
    /* 新增流程卡翻页 */
    handleCreateFlowPage(size, num) {
      this.pageNumCreateFlow = num;
      this.pageSizeCreateFlow = size;
      this.createFlowParams.pageNum = this.pageNumCreateFlow;
      this.createFlowParams.pageSize = this.pageSizeCreateFlow;
      this.handleCreateFlow();
    },
    /* 新增流程卡选择框 */
    handleCreateFlowChange(val) {
      this.selects = val
    },
    /* 流程卡行点击事件 */
    handleCreateFlowClick(row, column, event) {
      this.$refs.multipleCreateFlowTable.toggleRowSelection(row, column)
    },
    /* 打开自然分架弹窗 */
    handleOpenNatural() {
      if (this.selects.length <= 0) {
        this.$message.warning("请选择要分架的订单！")
        return;
      }
      this.naturalDialog = true
      selectProduct(this.selects).then(res => {
        this.naturalPointsData = res.data;
        // 设定一个数组spanArr/contentSpanArr来存放要合并的格数，同时还要设定一个变量pos/position来记录
        this.spanArrOne = [];
        for (let i = 0; i < this.naturalPointsData.length; i++) {
          if (i === 0) {
            this.spanArrOne.push(1);
            this.posOne = 0;
          } else {
            // 判断当前元素与上一个元素是否相同(第1和第2列)
            if (this.naturalPointsData[i].orderNo === this.naturalPointsData[i - 1].orderNo) {
              this.spanArrOne[this.posOne] += 1;
              this.spanArrOne.push(0);
            } else {
              this.spanArrOne.push(1);
              this.posOne = i;
            }
          }
        }
      })
    },
    /* 打开手动分架弹窗 */
    handleOpenManual() {
      if (this.selects.length <= 0) {
        this.$message.warning("请选择要分架的订单！")
        return;
      }
      this.numQuery = {
        one: 0,
        two: 0
      }
      this.manualDialog = true;
      this.$nextTick(() => {
        let noManualTable = this.$refs.noManualTable.$refs.bodyWrapper;
        noManualTable.addEventListener('scroll', () => {
          // 滚动距离
          const scrollLeft = noManualTable.scrollLeft
          this.$refs.noManualTable.$refs.headerWrapper.scrollLeft = scrollLeft
        })
      })
      selectSemiProduct(this.selects).then(res => {
        this.noManualData = res.data;
        this.haveManualData = [];
        // 设定一个数组spanArr/contentSpanArr来存放要合并的格数，同时还要设定一个变量pos/position来记录
        this.spanArr = [];
        for (let i = 0; i < this.noManualData.length; i++) {
          this.numQuery.one += this.noManualData[i].noShelfNum
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
      })
    },
    /* 移除自然分架列 */
    removeRow(row) {
      let index = this.naturalPointsData.indexOf(row);
      this.naturalPointsData.splice(index, 1)
    },
    /* 打开自然分架下分架弹窗 */
    handlePointsPlane(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //校验分架数量
          let one = "";
          for (let i = 0; i < this.naturalPointsData.length; i++) {
            let item = this.naturalPointsData[i]
            if (item.noShelfNum > (item.num - item.shelfNum)) {
              let index = this.naturalPointsData.indexOf(item) + 1;
              if (one === "") {
                one = index + ""
              } else {
                one = one + "," + index
              }
            }
          }
          if (one !== "") {
            this.$message.warning("序号为“" + one + "”的产品此次分架数量与已分架数量和大于产品数量！")
            return;
          }
          this.naturalPointsPlaneParameter.orderProducts = this.naturalPointsData;
          this.naturalPointsPlaneParameter.parameter = this.naturalPointsForm;
          // console.log(this.naturalPointsData)
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          automaticShelf(this.naturalPointsPlaneParameter).then(res => {
            loading.close()
            if (res.code === 200) {
              res.data.forEach(item => {
                item.monolithicName = item.list[0].itemName + "+" + item.list[0].itemType;
              })
              this.naturalPointsPlaneData = res.data;
              this.naturalPointsPlaneDialog = true;
            } else {
              this.$message.error(res.msg)
            }
          })
        } else {
          return false;
        }
      })
    },
    /* 保存分架预览 */
    saveNaturalPointsPlane() {
      this.saveFlowCard(this.naturalPointsPlaneData);
      this.naturalPointsPlaneDialog = false;
      this.naturalDialog = false;
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
            // this.$message.warning("序号为“" + (i + 1) + "”的“" + this.noManualData[i].productName + "”中每个单片的剩余数量不同，请修改！")
            break;
          }
        }
      })
      if (msg !== "") {
        this.$message.warning("序号为“" + msg + "”中每个单片的剩余数量不同，请修改！")
        return;
      }
      saveSemiProduct(this.haveManualData).then(res => {
        if (res.code === 200) {
          this.$message.success(res.msg)
        } else {
          this.$message.error(res.msg)
        }
      })
      this.manualDialog = false
      this.handleCreateFlow();
    },
    /* 保存分架后的流程卡信息 */
    saveFlowCard(data) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      saveFlowCard(data).then(res => {
        loading.close()
        if (res.code === 200) {
          this.$message.success('分架成功');
          selectProduct(this.selects).then(res => {
            this.naturalPointsData = res.data;
            this.handleCreateFlow();
          })
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    noManualDataChange(val) {
      this.noManualChangeData = val;
    },
    noManualRowClick(row, column, event) {
      if(column.label == "本次分架数量（片）"){
        return false;
      }
      this.$refs.noManualTable.toggleRowSelection(row, column)
    },
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
    /* 自动分架  合并行 */
    processSpanMethodOne({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1 || columnIndex === 2 || columnIndex === 3 || columnIndex === 4 || columnIndex === 5) {
        const _row = this.spanArrOne[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
    },
    /* 手动分家  已分架信息  合并行 */
    processSpanMethodTwo({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1 || columnIndex === 2 || columnIndex === 3 || columnIndex === 4) {
        const _row = this.spanArrTwo[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        };
      }
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
    tableRowClassName2({row, rowIndex}){
      let lists = new Set(this.haveManualData.map(item=>{
        return item.flowCardNo
      }))
      let index = Array.from(lists).indexOf(row.flowCardNo);
      if(index % 2 == 0){
        return "green-row"
      }
      return ""
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
    async setFlowCard() {
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
      let oldFlowNumber = null;
      if (this.haveManualData.length > 0) {
        oldFlowNumber = this.haveManualData[this.haveManualData.length - 1].flowCardNo
      }
      //选中要分架产品id集合
      const productIds = new Set;
      const monolithic = new Set;
      for (let i = 0; i < this.noManualChangeData.length; i++) {
        if (this.noManualChangeData[i].shelfNum > this.noManualChangeData[i].noShelfNum && this.noManualChangeData[i].noShelfNum != 0) {
          this.$message.warning("选中数据中，分架数量不得大于剩余数量！")
          return;
        }
        productIds.add(this.noManualChangeData[i].productId)
        monolithic.add(this.noManualChangeData[i].itemSurface)
      }
      //判断选中数据中有无同一个产品的
      if (!this.isOpen || productIds.size !== this.noManualChangeData.length || monolithic.size > 1 || !monolithic.has("A片")) {
        // console.log("无法半自动")
        //生成流程卡编号
        await generateFlowNumber({oldFlowNumber: oldFlowNumber}).then(res => {
          this.noManualChangeData.forEach(item => {
            this.numQuery.one -= item.shelfNum;
            this.numQuery.two += item.shelfNum;
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
              requirement: item.requirement
            }
            this.haveManualData.push(flowDetailed)
            //修改可分架数量
            item.noShelfNum = item.noShelfNum - item.shelfNum
            item.shelfNum = item.noShelfNum
            // 设定一个数组spanArr/contentSpanArr来存放要合并的格数，同时还要设定一个变量pos/position来记录
            this.spanArrTwo = [];
            for (let i = 0; i < this.haveManualData.length; i++) {
              if (i === 0) {
                this.spanArrTwo.push(1);
                this.posTwo = 0;
              } else {
                // 判断当前元素与上一个元素是否相同(第1和第2列)
                if (this.haveManualData[i].flowCardNo === this.haveManualData[i - 1].flowCardNo) {
                  this.spanArrTwo[this.posTwo] += 1;
                  this.spanArrTwo.push(0);
                } else {
                  this.spanArrTwo.push(1);
                  this.posTwo = i;
                }
              }
            }
          })
          // this.noManualChangeData = []
          this.$refs.noManualTable.clearSelection();
        })
      } else {
        // console.log("可以半自动")
        let flag = true;
        //存放校验成品面
        const itemType = new Set;
        this.noManualData.forEach(item => {
          if (productIds.has(item.productId) && item.itemSurface !== "A片") {
            itemType.add(item.itemSurface);
          }
        })
        //判断选中产品数量     校验每个产品中半产品
        // if (productIds.size > 1) {
          //遍历成品面
          for (let itemTypeElement of itemType) {
            const thick = new Set;
            const type = new Set;
            const pos = new Set;
            const shelfNum = new Set;
            const noShelfNum = new Set;
            let x = 0;
            //遍历选择产品
            this.noManualChangeData.forEach(c => {
              //遍历所有产品
              this.noManualData.forEach(n => {
                if (n.productId === c.productId && n.itemSurface === itemTypeElement) {
                  thick.add(n.itemThick)
                  type.add(n.itemType)
                  pos.add(n.processId)
                  console.log("123" , c.shelfNum)
                  console.log("456" , n.noShelfNum)
                  if(c.shelfNum > n.noShelfNum) {
                    this.$message.warning("分架数量不足！")
                    return
                  }
                  // noShelfNum.add(n.noShelfNum)
                  // shelfNum.add(c.shelfNum)
                  x++;
                }
              })
            })
            if (thick.size > 1) {
              flag = false
              this.$message.warning("其余半产品厚度不同，无法进行半自动分架！")
            }
            if (type.size > 1) {
              flag = false
              this.$message.warning("其余半产品类型不同，无法进行半自动分架！")
            }
            if (pos.size > 1) {
              flag = false
              this.$message.warning("其余半产品工艺流程不同，无法进行半自动分架！")
            }
            // if (Math.max(...shelfNum) > Math.min(...noShelfNum)) {
            //   flag = false
            //   this.$message.warning("其余半产品数量不同，无法进行半自动分架！")
            // }
            if (this.noManualChangeData.length !== x) {
              flag = false;
              this.$message.warning("缺少对应半产品，无法进行半自动分架！")
            }
            if (!flag) {
              break;
            }
          }
          // console.log(flag)
        // }
        if (flag) {
          itemType.add("A片");
          //添加已分架数据
          generateFlowNumber({oldFlowNumber: oldFlowNumber}).then(res => {
            for (let itemTypeElement of itemType) {
              //获取流程卡号
              oldFlowNumber = res.msg;
              this.noManualChangeData.forEach(t => {
                this.noManualData.forEach(item => {
                  if (item.productId === t.productId && item.itemSurface === itemTypeElement) {
                    this.numQuery.one -= t.shelfNum;
                    this.numQuery.two += t.shelfNum;
                    let area = t.shelfNum * item.itemArea;
                    let weight = t.shelfNum * item.itemWeight;
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
                      flowCardNo: res.msg + "_" + itemTypeElement.substring(0,1),
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
                      itemNum: t.shelfNum,
                      totalArea: area,
                      totalWeight: weight,
                      requirement: item.requirement
                    }
                    this.haveManualData.push(flowDetailed)
                    item.noShelfNum = item.noShelfNum - t.shelfNum;
                    item.shelfNum = item.noShelfNum
                    // 设定一个数组spanArr/contentSpanArr来存放要合并的格数，同时还要设定一个变量pos/position来记录
                    this.spanArrTwo = [];
                    for (let i = 0; i < this.haveManualData.length; i++) {
                      if (i === 0) {
                        this.spanArrTwo.push(1);
                        this.posTwo = 0;
                      } else {
                        // 判断当前元素与上一个元素是否相同(第1和第2列)
                        if (this.haveManualData[i].flowCardNo === this.haveManualData[i - 1].flowCardNo) {
                          this.spanArrTwo[this.posTwo] += 1;
                          this.spanArrTwo.push(0);
                        } else {
                          this.spanArrTwo.push(1);
                          this.posTwo = i;
                        }
                      }
                    }
                  }
                })
              })
            }
            this.$refs.noManualTable.clearSelection();
          })
        }
      }
    },
    /* 纳入最近流程卡 */
    setLatelyFlowCard() {
      if (this.noManualChangeData.length === 0) {
        this.$message.warning("请至少选择一条数据！")
        return;
      }
      if (this.haveManualData.length === 0) {
        this.$message.warning("无已分架信息！")
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
      /*const newArr3 = this.noManualChangeData.map(item => item.orderNo)
      const newSet3 = new Set(newArr3)
      if (newSet3.size > 1) {
        this.$message.warning("请选择相同订单的未分架信息")
        return;
      }*/

      let two = this.haveManualData[this.haveManualData.length - 1]
      /* if (newArr3[0] != two.orderNo) {
         this.$message.warning("请选择与流程卡: " + two.flowCardNo + "，订单编号相同的未分架信息")
         return;
       }*/
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
        this.numQuery.one -= item.shelfNum;
        this.numQuery.two += item.shelfNum;
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
          requirement: item.requirement
        }
        this.haveManualData.push(flowDetailed)
        item.noShelfNum = item.noShelfNum - item.shelfNum
        item.shelfNum = item.noShelfNum
        // 设定一个数组spanArr/contentSpanArr来存放要合并的格数，同时还要设定一个变量pos/position来记录
        this.spanArrTwo = [];
        for (let i = 0; i < this.haveManualData.length; i++) {
          if (i === 0) {
            this.spanArrTwo.push(1);
            this.posTwo = 0;
          } else {
            // 判断当前元素与上一个元素是否相同(第1和第2列)
            if (this.haveManualData[i].flowCardNo === this.haveManualData[i - 1].flowCardNo) {
              this.spanArrTwo[this.posTwo] += 1;
              this.spanArrTwo.push(0);
            } else {
              this.spanArrTwo.push(1);
              this.posTwo = i;
            }
          }
        }
      })
      this.$refs.noManualTable.clearSelection();
    },
    manualDataChange(val) {
      this.manualChangeData = val;
    },
    manualDataClick(row, column, event) {
      this.$refs.haveManualDataTable.toggleRowSelection(row, column)
    },
    /* 删除流程卡 */
    delFlowCard() {
      if (this.manualChangeData.length <= 0) {
        this.$message.warning("请选择至少一条数据！")
        return;
      }
      this.manualChangeData.forEach(item => {
        this.numQuery.one += item.itemNum;
        this.numQuery.two -= item.itemNum;
        this.noManualData.forEach(one => {
          if (item.id === one.id) {
            one.noShelfNum = Number(one.noShelfNum) + Number(item.itemNum);
            one.shelfNum = one.noShelfNum
          }
        })
        for (let i = this.haveManualData.length - 1; i >= 0; i--) {
          if (item.uuid === this.haveManualData[i].uuid) {
            this.haveManualData.splice(i, 1)
          }
        }
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
          this.numQuery.one += item.itemNum;
          this.numQuery.two = 0;
          this.noManualData.forEach(one => {
            if (item.id === one.id) {
              one.noShelfNum = Number(one.noShelfNum) + Number(item.itemNum);
              one.shelfNum = one.noShelfNum;
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
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .green-row {
  background-color: #9ae375 !important;
}
::v-deep .success-row {
  color: #e52b47 !important;
}

::v-deep .warning-row {
  color: #43f51b !important;
}

.rightTable {
  height: calc(100% - 105px);
  overflow: hidden;

  //::v-deep .el-table {
  //  .el-table__body-wrapper {
  //    height: calc(100% - 115px) !important;
  //  }
  //}
}

::v-deep .naturalDialog {
  .el-dialog {
    height: 80%;

    .el-dialog__body {
      height: calc(100% - 90px);

      .naturalPointsTable {
        height: calc(100% - 210px);
        //background-color: orange;
        min-height: 100px;

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

.parameterSetting {
  position: relative;
  height: 200px;
  border-radius: 15px;
  box-shadow: 0 1px 5px 0 rgba(0, 0, 0, 0.3);
  overflow: auto;
  margin-top: 10px;

  .title {
    padding-left: 20px;
    height: 30px;
    line-height: 30px;
    font-weight: 600;
    text-align: left;
    border: none;
    margin-bottom: 0;

  }

  ::v-deep .parameterSettingForm {
    padding: 10px 20px;
    height: calc(100% - 30px);
    overflow: auto;
    background: rgba(100, 200, 188, 0.05);
    border: 1px solid rgba(100, 200, 188, 0.5);
    border-bottom-left-radius: 15px;
    border-bottom-right-radius: 15px;
    display: flex;
    flex-wrap: wrap;

    .el-form-item {
      width: 380px;

      .el-form-item__label {
        width: 180px;
      }

      .el-form-item__content {
        width: 200px;
      }
    }
  }
}

::v-deep .naturalPointsPlane {
  .table {
    height: 100%;
  }

  .page {
    display: none;
  }
}

.naturalPointsPlaneDialog {
  ::v-deep .el-dialog {
    height: 60%;

    .el-dialog__body {
      height: calc(100% - 90px);

      .naturalPointsPlane {
        height: 100%;

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

//::v-deep .noShelfNum {
//  .cell {
//    .el-input-number {
//      width: 150px;
//    }
//  }
//}
::v-deep .el-table__fixed-right{
  z-index: 1000;
}
</style>
