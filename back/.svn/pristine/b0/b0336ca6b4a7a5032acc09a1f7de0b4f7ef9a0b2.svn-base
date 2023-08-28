<template>
  <div class="app-container">
    <main-flexible-tree :data="treeData"
                        :defaultProps="TreeDefaultProps"
                        @handleNodeClick="handleNodeClick">
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
          <el-form-item label="辅料名称" prop="accessoriesName">
            <el-input
              v-model="queryParams.accessoriesName"
              placeholder="请输入辅料名称"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item label="辅料编号" prop="accessoriesNo">
            <el-input
              v-model="queryParams.accessoriesNo"
              placeholder="请输入辅料编号"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item label="型号规格" prop="accessoriesSpecifications">
            <el-input
              v-model="queryParams.accessoriesSpecifications"
              placeholder="请输入规格型号"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button
            type="primary"
            size="mini"
            @click="handleAccessoriesEntry"
            v-hasPermi="['accessories:operation:enter']"
          ><i class="iconfont icon-ruku1"></i>辅料入库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handlePurchaseEntry"
            v-hasPermi="['accessories:operation:warehousing']"
          ><i class="iconfont icon-ruku"></i>采购单入库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleEntryRecord"
            v-hasPermi="['accessories:operation:enterRecord']"
          ><i class="iconfont icon-churukujilu"></i>入库记录
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleOutbound"
            v-hasPermi="['accessories:operation:out']"
          ><i class="iconfont icon-chuku"></i>新增出库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleOutRecord"
            v-hasPermi="['accessories:operation:outRecord']"
          ><i class="iconfont icon-churukujilu"></i>出库记录
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleCheckNum"
            v-hasPermi="['accessories:operation:Inventory']"
          ><i class="iconfont icon-pandian"></i>新增盘库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleCheckNumRecord"
            v-hasPermi="['accessories:operation:InventoryRecord']"
          ><i class="iconfont icon-navicon-kcpdd" style="font-size: 11px"></i>盘库记录
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleExport"
            v-hasPermi="['accessories:operation:export']"
          ><i class="iconfont icon-daochuwenjian"></i>导出
          </el-button>
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
            @row-click="handleRowClick"  :cell-class-name="cellName" slot="table">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column
              type="index"
              :index="getIndex"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              v-for="item in tableColumn"
              :key="item.id"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width">
            </el-table-column>
          </el-table>
        </count-table>
      </div>
    </main-flexible-tree>
    <!--  辅料入库  -->
    <el-dialog title="辅料入库"
               :visible.sync="accessoriesEntryDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title1">入库信息</p>
        <div class="addInfoContainer">
          <el-form :model="accessoriesParams" size="mini" :inline="true" ref="accessoriesForm" class="ipt2">
            <el-form-item label="入库单号" prop="operationNo">
              <el-input
                v-model="accessoriesParams.operationNo"
                disabled/>
            </el-form-item>
            <el-form-item label="供应商" prop="millId">
              <el-input
                v-model="accessoriesParams.millId"
                placeholder="请输入供应商"
                clearable/>
            </el-form-item>
            <el-form-item label="负责人" prop="millId">
              <el-input
                v-model="accessoriesParams.person"
                placeholder="请输入负责人"
                clearable/>
            </el-form-item>
            <el-form-item label="入库日期" prop="operationDate">
              <el-date-picker
                v-model="accessoriesParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="accessoriesParams.remarks" size="mini" clearable   placeholder="请输入内容">
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
            @click="handleExportProduct">导入辅料
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
            :data="accessoriesList"
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
              v-for="(item,index) in addAccessoriesColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              show-overflow-tooltip></el-table-column>
            <el-table-column label="入库数量" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.operationNum" :min="1" :precision="0"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="入库单价" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.referencePrice" :min="1"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="入库总金额" width="150">
              <template slot-scope="scope">
                {{ Number(scope.row.operationNum) * Number(scope.row.referencePrice) }}
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="120" class-name="operation">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="deleteRow(scope.$index, accessoriesList)"
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
        <el-button type="primary" size="mini" @click="saveAccessoriesEntry('accessoriesForm')">保存</el-button>
        <el-button size="mini" @click="accessoriesEntryDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  选择辅料  -->
    <accessories-dialog :selectAccessoriesDialog="selectAccessoriesDialog"
                        ref="accessoriesDialog"
                        @saveSelectAccessories="saveSelectAccessories"
                        @cancelSelectAccessories="cancelSelectAccessories"
                        class="accessoriesDialog"></accessories-dialog>
    <!--  采购单入库  -->
    <el-dialog title='采购单入库'
               :visible.sync="purchaseEntryDialog" width="85%" class="dialog-style publicAddDialog purchaseEntryDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title1">入库信息</p>
        <div class="addInfoContainer">
          <el-form :model="purchaseParams" ref="purchaseForm" size="mini" :inline="true" class="ipt2">
            <el-form-item label="入库单号">
              <el-input
                v-model="purchaseParams.operationNo"
                disabled/>
            </el-form-item>
            <el-form-item label="供应商" prop="millId">
              <el-input
                v-model="purchaseParams.millId"
                placeholder="请输入供应商"
                clearable/>
            </el-form-item>
            <el-form-item label="负责人">
              <el-input
                v-model="purchaseParams.person"
                placeholder="请输入负责人"
                clearable/>
            </el-form-item>
            <el-form-item label="入库日期" prop="operationDate">
              <el-date-picker
                v-model="purchaseParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="purchaseParams.remarks" size="mini" clearable placeholder="请输入内容">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="btnTable" style="padding-top: 10px;box-sizing: border-box">
<!--        <slot-table class="addTable">
          <el-table
            highlight-current-row
            :data="purchaseOperationDate"
            stripe
            border
            height="100%"
            ref="purchaseOperationRef"
            @selection-change="purchaseOperationChange"
            @row-click="purchaseOperationRowClick"
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
              show-overflow-tooltip
              v-for="(item,index) in purchaseOperationDateColumn"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width">
            </el-table-column>
          </el-table>
        </slot-table>-->
        <slot-table class="returnTable" :pageSize="queryPurchase.pageSize" :pageNum="queryPurchase.pageNum"
                     :total="queryPurchase.total">
          <el-table highlight-current-row
                    :data="purchaseOperationDate"
                    stripe
                    border
                    height="100%"
                    ref="purchaseOperationRef"
                    @selection-change="purchaseOperationChange"
                    @row-click="purchaseOperationRowClick"
                    slot="table">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column
              :index="getIndex"
              type="index"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              v-for="(item,index) in purchaseOperationDateColumn"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              :min-width="item.width"
              show-overflow-tooltip>
              <el-table-column :prop="item.prop" show-overflow-tooltip min-width="100" :min-width="item.width">
                <template #header scoped-slot="scope">
                  <!--可根据类型来显示为搜索框、下拉框等-->
                  <el-input
                    v-model="queryPurchase[item.prop]"
                    size="mini"
                    placeholder="请输入"
                    v-if="item.type==='ipt'"
                    @keyup.enter.native="queryPurchaseData"
                    clearable/>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="savePurchaseEntry('purchaseForm')">保存</el-button>
        <el-button size="mini" @click="purchaseEntryDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  选择采购单  -->
    <purchase-dialog :selectPurchaseDialog="selectPurchaseDialog"
                     @saveSelectPurchase="saveSelectPurchase"
                     ref="selectPurchaseDialog"></purchase-dialog>
    <!--  新增出库  -->
    <el-dialog title='新增出库'
               :visible.sync="outboundDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title">出库信息</p>
        <div class="addInfoContainer">
          <el-form :model="accessoriesParams" ref="accessoriesForm" size="mini" :inline="true" class="ipt2">
            <el-form-item label="出库单号" prop="operationNo">
              <el-input
                v-model="accessoriesParams.operationNo"
                disabled/>
            </el-form-item>
            <el-form-item label="供应商" prop="millName">
              <el-input
                v-model="accessoriesParams.millId"
                placeholder="请输入供应商"
                clearable/>
            </el-form-item>
            <el-form-item label="出库日期" prop="operationDate">
              <el-date-picker
                v-model="accessoriesParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="领用人" prop="operationNo">
              <el-input
                v-model="accessoriesParams.person"
                placeholder="请输入领用人"
                clearable/>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="accessoriesParams.remarks" size="mini" clearable placeholder="请输入内容">
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
            @click="handleExportProduct">导入辅料
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
            :data="purchaseOutBoundDate"
            stripe
            border
            height="100%"
            ref="templateTableRef"
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
              show-overflow-tooltip
              v-for="(item,index) in addAccessoriesColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width">
            </el-table-column>
            <el-table-column label="出库数量" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.operationNum" :min="1" :precision="0"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="出库单价" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.referencePrice" :min="1"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="出库总金额" width="150">
              <template slot-scope="scope">
                {{ Number(scope.row.operationNum) * Number(scope.row.referencePrice) }}
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="120" class-name="operation">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="deleteRow(scope.$index, purchaseOutBoundDate)"
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
        <el-button type="primary" size="mini" @click="saveOutbound('accessoriesForm')">保存</el-button>
        <el-button size="mini" @click="outboundDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  新增盘库  -->
    <el-dialog title='新增盘库'
               :visible.sync="checkNumDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title1">盘库信息</p>
        <div class="addInfoContainer">
          <el-form :model="checkNumParams" ref="checkNumForm" size="mini" :inline="true" class="ipt2">
            <el-form-item label="盘库单号" prop="operationNo">
              <el-input
                v-model="checkNumParams.operationNo"
                disabled/>
            </el-form-item>
            <el-form-item label="盘库人" prop="person">
              <el-input
                v-model="checkNumParams.person"
                placeholder="请输入盘库人"
                clearable/>
            </el-form-item>
            <el-form-item label="盘库日期" prop="operationDate">
              <el-date-picker
                v-model="checkNumParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="checkNumParams.remarks" size="mini" clearable placeholder="请输入内容">
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
            @click="handleExportProduct">导入辅料
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
            :data="checkNumDate"
            stripe
            border
            height="100%"
            ref="templateTableRef"
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
              show-overflow-tooltip
              v-for="(item,index) in addAccessoriesColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width">
            </el-table-column>
            <el-table-column label="盘库数量" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.operationNum" :min="1" :precision="0"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="盘库总金额" width="150">
              <template slot-scope="scope">
                {{ Number(scope.row.operationNum) * Number(scope.row.referencePrice) }}
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="120" class-name="operation">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="deleteRow(scope.$index, checkNumDate)"
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
        <el-button type="primary" size="mini" @click="saveCheckNum('checkNumForm')">保存</el-button>
        <el-button size="mini" @click="checkNumDialog=false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import SlotTable from "@/components/public/table/SlotTable";
import {queryAccessoriesFilm, queryTree} from "@/api/accessories/accessoriesFilm";
import {groupBy} from "@/utils/groupBy";
import CountTable from "@/components/public/table/countTable";
import AccessoriesDialog from "@/views/accessories/components/accessoriesDialog";
import {getCurrentDay, isNumber, keepThreeNum} from "@/utils/order/order";
import {HyperFormula} from "hyperformula";
import {Message} from "element-ui";
import PurchaseDialog from "@/views/accessories/components/purchaseDialog";
import {productionNumber, accessoriesWarehouse, purchaseWarehouse} from "@/api/accessories/accessoriesWarehouse";
import {queryPurchaseData,} from "@/api/accessories/accessoriesPurchase";


export default {
  // name: "index",
  components: {PurchaseDialog, AccessoriesDialog, CountTable, SlotTable, MainFlexibleTree},
  data() {
    return {
      treeData: [], //左侧tree树
      TreeDefaultProps: {
        children: 'children',
        label: 'accessoriesTypeName'
      },
      queryParams: {
        id: "",
        accessoriesName: "",
        accessoriesNo: "",
        accessoriesSpecifications: '',
        accessoriesTypeId: "",
        accessoriesTypeName: "",
        pageNum: 1,
        pageSize: 20,
      },
      total: 1,
      tableDate: [],
      // selectedAccessories:[],
      selectedAccessories: [],
      tableColumn: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '100'},
        {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
        {label: '厂家名称', prop: 'accessoriesMill', width: '150'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '120'},
        {label: '辅料类型', prop: 'typeName', width: '120'},
        {label: '库存数量', prop: 'stock', width: '120'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      //总合计
      summation: [
        {label: 'stock', title: '库存总数量', value: 0},
        // {label: 'area', title: '总面积', value: 0, unit: 'm²'},
        // {label: 'totalAmount', title: '总金额', value: 0, unit: '元'},
      ],
      currentNodeInfo: {},
      accessoriesDialog: false, //新增入库
      editDialog: false, //编辑入库
      accessoriesEntryDialog: false, //辅料入库
      purchaseEntryDialog: false, //采购单入库
      outboundDialog: false, //新增出库
      checkNumDialog: false, //新增盘库
      accessoriesList: [],
      addAccessoriesColumns: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '120'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
       {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '厂家名称', prop: 'accessoriesMill', width: '150'},
        {label: '辅料类型', prop: 'typeName', width: '120'},
        {label: '库存数量', prop: 'stock', width: '120'},
      ],
      accessoriesParams: {
        operationType: '',
        operationNo: '',
        operationDate: getCurrentDay(),
        millId: '',
        person: '',
        accessoriesList: []
      },
      purchaseParams: {
        operationType: '',
        operationNo: '',
        purchaseNo: '',
        operationDate: getCurrentDay(),
        millId: '',
        person: '',
        accessoriesPurchaseList: []
      },
      checkNumParams: {
        operationType: '盘库',
        operationNo: '',
        person: '',
        remarks: '',
        operationDate: getCurrentDay(),
        accessoriesList: []
      },
      accessoriesoperationDate: [],   //采购单入库数据
      accessoriesoperationDateColumn: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        // {label: '长度（mm）', prop: 'accessoriesLong', width: '100'},
        // {label: '宽度（mm）', prop: 'accessoriesWidth', width: '100'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
       {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '厂家名称', prop: 'millName', width: '150'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '120'},
        {label: '辅料类型', prop: 'accessoriesTypeName', width: '120'},
        // {label: '等级', prop: 'accessoriesGrade', width: '100'},
        {label: '数量', prop: 'num', width: '100'},
        {label: '单价', prop: 'price', width: '120'},
        // {label: '面积（m²）', prop: 'area', width: '100'},
        {label: '金额（元）', prop: 'money', width: '100'},
      ],
      queryPurchase: {//查询采购单参数
        purchaseNo: '',
        purchasePerson: '',
        millName: '',
        status: "未入库",
        pageSize: 20,
        pageNum: 1,
        total: 0
      },
      purchaseOperationDate: [],
      purchaseOperationChangeDate: [],
      checkNumDate: [],
      purchaseOperationDateColumn: [
        {label: '采购单号', prop: 'purchaseNo', width: '120', type: 'ipt'},
        {label: '供应商', prop: 'millName', width: '120', type: 'ipt'},
        {label: '采购负责人', prop: 'purchasePerson', width: '100', type: 'ipt'},
        {label: '采购日期', prop: 'purchaseDate', width: '180'},
        {label: '总数量', prop: 'purchaseNum', width: '120'},
        {label: '总金额（元）', prop: 'purchaseAmount', width: '120'},
        {label: '入库状态', prop: 'status', width: '120'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      purchaseOutBoundDate: [],
      selectAccessoriesDialog: false,  //选择辅料
      selectPurchaseDialog: false,  //选择采购单
      accessoriesFormRules: {
        operationNo: [
          {required: true, message: '请输入供应商', trigger: 'blur'},
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
      purchaseFormRules: {
        millName: [
          {required: true, message: '请输入供应商', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        operationDate: [
          {required: true, message: '入库日期不能为空', trigger: 'blur'}
        ]
      },
      checkNumFormRules: {
        person: [
          {required: true, message: '请输入盘库人', change: 'blur'},
        ],
        remarks: [
          // {required: true, message: '请输入入库单号', trigger: 'blur'},
          {max: 125, message: '字符长度最大不得超过125', trigger: 'blur'}
        ],
      }
    }

  },
  created() {
    this.getTreeData();
    this.handleQuery();
  },
   methods: {

    /* 左侧tree查询 */
    getTreeData() {
      queryTree().then(res => {
        console.log(res)
        if (res.code === 200) {
          // this.treeData=res.data;
          this.treeData = [{id: 0, accessoriesTypeName: "辅料类型", children: groupBy(res.data, "tid")}];
          if (this.treeData) {
            this.treeData.forEach(item => {
              item.accessoriesTypeId = item.id
            })
          }
        }
      })
    },
    /* 左侧tree树单击节点 */
    handleNodeClick(val, node, component) {
      console.log(val, node, component);
      this.currentNodeInfo = val;
      this.queryParams.accessoriesTypeId = val.id;
      this.handleQuery();
    },
    /* 查询辅料数据 */
    handleQuery() {
      queryAccessoriesFilm(this.queryParams).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.tableDate = res.data;
          this.total = res.count;
          if(this.tableDate){
            this.summation=[
              {label: 'stock', title: '库存总数量', value: 0},
            ];
            this.tableDate.forEach(item=>{
              this.summation.forEach(sumItem => {
                if (item.hasOwnProperty(sumItem.label)) {
                  sumItem.value += item[sumItem.label]
                }
                sumItem.value = keepThreeNum(sumItem.value);
              })
            })
          }
        }
      })
    },
    /*分页器*/
    handleChange(size, num) {
      this.queryParams.pageNum = num;
      this.queryParams.pageSize = size;
      this.handleQuery();
    },
    /*  勾选表格复选框*/
    handleSelectionChange(val) {
      this.selectedAccessories = val;
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.templateTableRef.toggleRowSelection(row, column)
    },
     cellName({row, column, rowIndex, columnIndex}) {
       if (row.stock<=row.alarmInventory) {
         return 'alarmInventory'
       }
     },
     /* 翻页后序号连贯 */
     getIndex($index) {
       //  表格序号
       return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + $index + 1;
     },
     //查询采购单数据
     queryPurchaseData() {
       queryPurchaseData(this.queryPurchase).then(one => {
         this.purchaseOperationDate = one.data;
         if(this.purchaseOperationDate){
           this.purchaseOperationDate.forEach(item=>{
             item.purchaseAmount=keepThreeNum(item.purchaseAmount)
           })
         }
         this.queryPurchase.total = one.count;
       })
     },
    /* 导入辅料 */
    handleExportProduct() {
      this.selectAccessoriesDialog = true
    },
    /* 移除辅料 */
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    /* 辅料入库 */
    handleAccessoriesEntry() {
      this.accessoriesEntryDialog = true;
      this.accessoriesParams = {
        operationType: '入库',
        operationNo: '',
        person: '',
        operationDate: getCurrentDay(),
        millName: '',
      };
      this.accessoriesList = [];
      productionNumber({type: 0}).then(res => {
        this.accessoriesParams.operationNo = res.msg;
      })
    },
    /* 采购单入库 */
    handlePurchaseEntry() {
      this.purchaseEntryDialog = true;
      this.purchaseParams = {};
      this.purchaseParams.operationDate = getCurrentDay();
      this.purchaseOperationDate = [];
      productionNumber({type: 0}).then(res => {
        this.purchaseParams.operationNo = res.msg;
        this.queryPurchaseData()
      })
    },
    /* 查询入库记录 */
    handleEntryRecord() {
      this.$router.push('/accessories/accessoriesWarehouse/entryRecords');
    },
    /* 辅料出库 */
    handleOutbound() {
      this.outboundDialog = true;
      this.accessoriesParams = {
        operationType: '出库',
        operationNo: '',
        operationDate: getCurrentDay(),
        millId: '',
      };
      this.purchaseOutBoundDate = []
      productionNumber({type: 1}).then(res => {
        this.accessoriesParams.operationNo = res.msg;
      })
    },
    /* 出库记录 */
    handleOutRecord() {
      this.$router.push('/accessories/accessoriesWarehouse/outboundRecords');
    },
    /* 新增盘库 */
    handleCheckNum() {
      this.checkNumDialog = true;
      this.checkNumDate = []
      this.checkNumParams = {
        operationType: '盘库',
          operationNo: '',
          person: '',
          remarks: '',
          operationDate: getCurrentDay(),
          accessoriesList: []
      }
      productionNumber({type: 2}).then(res => {
        this.checkNumParams.operationNo = res.msg;
      })
    },
    /* 盘库记录 */
    handleCheckNumRecord() {
      this.$router.push('/accessories/accessoriesWarehouse/checkNumRecords');
    },
    /* 导出 */
    handleExport() {
      let ids = [];
      if (this.selectedAccessories.length > 0) {
        this.selectedAccessories.forEach(item => {
          ids.push(item.id)
        })
      }
      this.download('/accessories/definition/exportAccessoriesFilm', {
        ids: ids.toString()
      }, `辅料数据.xlsx`)
    },
    /* 单击采购单入库 */
    handleBlurEntry() {
      this.selectPurchaseDialog = true;
    },
    /* 保存选择辅料数据 */
    saveSelectAccessories(val, tableData) {
      if (!val || val.length <= 0) {
        Message.warning("请选择一条数据进行添加");
        return false
      }
      if (val.length >= 1) {
        if (this.accessoriesEntryDialog) {
          for (let i = 0; i < val.length; i++) {
            var index = tableData.indexOf(val[i])
            for (let j = 0; j < this.accessoriesList.length; j++) {
              if (val[i].id == this.accessoriesList[j].id){
                Message.warning("序号为" + (index + 1) + "的数据已经添加,不可重复添加！");
                return;
              }
            }
          }
          this.accessoriesList.push(...val);
          Message.success("添加成功");
          this.$refs.accessoriesDialog.$refs.templateTableRef.clearSelection();
        }
        if (this.outboundDialog) {
          // purchaseOutBoundDate
          for (let i = 0; i < val.length; i++) {
            var index = tableData.indexOf(val[i])
            for (let j = 0; j < this.purchaseOutBoundDate.length; j++) {
              if (val[i].id == this.purchaseOutBoundDate[j].id){
                Message.warning("序号为" + (index + 1) + "的数据已经添加,不可重复添加！");
                return;
              }
            }
          }
          this.purchaseOutBoundDate.push(...val);
          Message.success("添加成功");
          this.$refs.accessoriesDialog.$refs.templateTableRef.clearSelection();
        }
        if (this.checkNumDialog) {
          for (let i = 0; i < val.length; i++) {
            var index = tableData.indexOf(val[i])
            for (let j = 0; j < this.checkNumDate.length; j++) {
              if (val[i].id == this.checkNumDate[j].id){
                Message.warning("序号为" + (index + 1) + "的数据已经添加,不可重复添加！");
                return;
              }
            }
          }
          this.checkNumDate.push(...val);
          Message.success("添加成功");
          this.selectAccessoriesDialog = false;
          this.$refs.accessoriesDialog.$refs.templateTableRef.clearSelection();
        }
      }
    },
    /* 取消选择辅料数据 */
    cancelSelectAccessories() {
      this.selectAccessoriesDialog = false;
      this.$refs.accessoriesDialog.$refs.templateTableRef.clearSelection();
    },
    /* 保存入库 */
    saveAccessoriesEntry(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //给订单赋值
          this.accessoriesEntryDialog = false;
          this.accessoriesParams.accessoriesList = this.accessoriesList
          accessoriesWarehouse(this.accessoriesParams).then(res => {
            if (res.code === 200) {
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
            this.handleQuery()
          })
        } else {
          return false;
        }
      });
    },
    /* 选择采购单入库 */
    saveSelectPurchase(val) {
      console.log(val)
      this.purchaseOperationDate.push(val);
      this.purchaseParams.purchaseNo = val.purchaseNo
      this.selectPurchaseDialog = false;
      Message.success("已选择采购单");
      // this.$refs.selectPurchaseDialog.tableDate.splice(val)

    },
    purchaseOperationChange(val) {
      this.purchaseOperationChangeDate = val
    },
     /* 单击表格行 */
     purchaseOperationRowClick(row, column, event) {
       this.$refs.purchaseOperationRef.toggleRowSelection(row, column)
     },
    /* 保存采购单入库 */
    savePurchaseEntry(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.purchaseOperationDate.length <= 0) {
            Message.warning("请添加采购单信息");
          } else {
            this.purchaseEntryDialog = false;
            this.purchaseParams.accessoriesPurchaseList = this.purchaseOperationChangeDate;
            purchaseWarehouse(this.purchaseParams).then(res => {
              if (res.code === 200) {
                this.$message.success(res.msg)
              } else {
                this.$message.error(res.msg)
              }
              this.handleQuery()
            })
          }
        } else {
          return false;
        }
      })
    },
    /* 保存出库 */
    saveOutbound(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.purchaseOutBoundDate.length < 0) {
            this.$message.error("请选择要出库的辅料！")
            return
          }
          this.accessoriesParams.accessoriesList = this.purchaseOutBoundDate
          accessoriesWarehouse(this.accessoriesParams).then(res => {
            if (res.code === 200) {
              this.$message.success(res.msg);
              this.handleQuery();
              this.outboundDialog=false;
            } else {
              this.$message.error(res.msg);
              this.handleQuery();
              this.outboundDialog=false;
            }
          })
        } else {
          return false;
        }
      });
    },
    /* 保存出库 */
    saveCheckNum(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.checkNumDialog = false;
          this.checkNumParams.accessoriesList = this.checkNumDate
          accessoriesWarehouse(this.checkNumParams).then(res => {
            if (res.code === 200) {
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
            this.handleQuery()
          })
        } else {
          return false;
        }
      });
    },
  }
}
</script>

<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
  ::v-deep .alarmInventory{
    .cell{
      color: red;
    }
  }
}

.purchaseEntryDialog {
  .addTable {
    height: 100%;
  }
}


</style>
