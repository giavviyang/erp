<template>
  <div class="app-container">
    <main-flexible-tree :data="treeData"
                        :defaultProps="TreeDefaultProps"
                        @handleNodeClick="handleNodeClick">
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
          <el-form-item label="原片名称" prop="originalName">
            <el-input
              v-model="queryParams.originalName"
              placeholder="请输入原片名称"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item label="原片编号" prop="originalNo">
            <el-input
              v-model="queryParams.originalNo"
              placeholder="请输入原片编号"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item label="原片厚度" prop="originalThick">
            <el-input
              v-model="queryParams.originalThick"
              placeholder="请输入原片厚度"
              @keyup.enter.native="handleQuery"
              clearable/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索
            </el-button>
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button
            type="primary"
            size="mini"
            @click="handleOriginalEntry"
            v-hasPermi="['originalFilm:warehouse:enter']"
          ><i class="iconfont icon-ruku1"></i>原片入库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handlePurchaseEntry"
            v-hasPermi="['originalFilm:warehouse:purchaseWarehouse']"
          ><i class="iconfont icon-ruku"></i>采购单入库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleEntryRecord"
            v-hasPermi="['originalFilm:warehouse:enterRecord']"
          ><i class="iconfont icon-churukujilu"></i>入库记录
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleOutbound"
            v-hasPermi="['originalFilm:warehouse:out']"
          ><i class="iconfont icon-chuku"></i>新增出库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleOutRecord"
            v-hasPermi="['originalFilm:warehouse:outRecord']"
          ><i class="iconfont icon-churukujilu"></i>出库记录
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleCheckNum"
            v-hasPermi="['originalFilm:warehouse:inventory\n']"
          ><i class="iconfont icon-pandian"></i>新增盘库
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleCheckNumRecord"
            v-hasPermi="['originalFilm:warehouse:revenRecord']"
          ><i class="iconfont icon-navicon-kcpdd" style="font-size: 11px"></i>盘库记录
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleExport"
            v-hasPermi="['original:warehouse:exportWarehouse']"
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
                    @row-click="handleRowClick"
                    :cell-class-name="cellName" slot="table">
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
    <!--  原片入库  -->
    <el-dialog title='原片入库'
               :visible.sync="originalEntryDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title">入库信息</p>
        <div class="addInfoContainer">
          <el-form :model="originalParams" ref="originalForm" size="mini" :inline="true" class="ipt2">
            <el-form-item label="入库单号" prop="operationNo">
              <el-input
                v-model="originalParams.operationNo"
                placeholder="请输入入库单号"
                disabled
              />
            </el-form-item>
            <el-form-item label="供应商" prop="millId">
              <el-input
                v-model="originalParams.millId"
                placeholder="请输入供应商"
                clearable/>
            </el-form-item>
            <el-form-item label="负责人" prop="person">
              <el-input
                v-model="originalParams.person"
                placeholder="请输入负责人"
                clearable/>
            </el-form-item>
            <el-form-item label="入库日期" prop="operationDate">
              <el-date-picker
                v-model="originalParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="originalParams.remarks" size="mini" clearable placeholder="请输入内容">
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
            @click="handleExportProduct">导入原片
          </el-button>
          <!--          <el-button-->
          <!--            type="primary"-->
          <!--            icon="el-icon-plus"-->
          <!--            size="mini"-->
          <!--            @click="handleRemoveProduct">移除-->
          <!--          </el-button>-->
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
                    :data="originalFilmList"
                    border
                    stripe
                    height="100%"
                    style="width: 100%"
                    ref="productTable"
                    slot="table">
            <!--            <el-table-column-->
            <!--              type="selection"-->
            <!--              width="55">-->
            <!--            </el-table-column>-->
            <el-table-column
              type="index"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              v-for="(item,index) in originalColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              show-overflow-tooltip></el-table-column>
            <el-table-column label="入库数量" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.operationNum" :min="1" :precision="0"></el-input-number>
                <!--                <el-input-number size="mini" v-if="scope.row.purchaseNum" v-model="scope.row.purchaseNum" :min="1" :precision="0"></el-input-number>-->
              </template>
            </el-table-column>
            <el-table-column label="入库单价" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.referencePrice" :min="1"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="总金额" width="150" prop="totalAmount">
              <template slot-scope="scope">
                {{ Number(scope.row.operationNum) * Number(scope.row.referencePrice) }}
                <!--               <span v-if="scope.row.purchaseNum">{{ // Number(scope.row.purchaseNum) * Number(scope.row.referencePrice) }}</span>-->
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="120" class-name="operation">
              <template slot-scope="scope">
                <el-button
                  @click.native.prevent="deleteRow(scope.$index, originalFilmList)"
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
        <el-button type="primary" size="mini" @click="saveOriginalEntry('originalForm')">保存</el-button>
        <el-button size="mini" @click="originalEntryDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  选择原片  -->
    <original-dialog :selectOriginalDialog="selectOriginalDialog"
                     ref="originalDialog"
                     @saveSelectOriginal="saveSelectOriginal"
                     @cancelSelectOriginal="cancelSelectOriginal"
                     class="originalDialog"></original-dialog>
    <!--  采购单入库  -->
    <el-dialog title='采购单入库'
               :visible.sync="purchaseEntryDialog" width="90%" class="dialog-style publicAddDialog purchaseEntryDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title1">入库信息</p>
        <div class="addInfoContainer">
          <el-form :model="purchaseParams" ref="purchaseForm" size="mini" :inline="true" class="ipt2">
            <el-form-item label="入库单号" prop="operationNo">
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
            <el-form-item label="负责人" prop="person">
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
                  <el-table highlight-current-row
                    :data="originalFilmPurchaseList"
                    stripe
                    border
                    height="100%"
                    ref="templateTableRef"
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
                      v-for="(item,index) in purchaseEntryDateColumn"
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
                    :data="originalFilmPurchaseList"
                    stripe
                    border
                    height="100%"
                    ref="templateTableRef"
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
              v-for="(item,index) in purchaseEntryDateColumn"
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
                     @saveSelectPurchase="saveSelectPurchase" @cancelSelectPurchase="cancelSelectPurchase"
                     ref="selectPurchaseDialog"></purchase-dialog>
    <!--  新增出库  -->
    <el-dialog title='新增出库'
               :visible.sync="outboundDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <div class="addInfo">
        <p class="title">出库信息</p>
        <div class="addInfoContainer">
          <el-form :model="originalParams" ref="originalForm" size="mini" :inline="true" class="ipt2">
            <el-form-item label="出库单号" prop="operationNo">
              <el-input
                v-model="originalParams.operationNo"
                placeholder="请输入出库单号"
                disabled
                clearable/>
            </el-form-item>
            <el-form-item label="供应商" prop="millName">
              <el-input
                v-model="originalParams.millId"
                placeholder="请输入供应商"
                clearable/>
            </el-form-item>
            <el-form-item label="出库日期" prop="operationDate">
              <el-date-picker
                v-model="originalParams.operationDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="领用人" prop="operationNo">
              <el-input
                v-model="originalParams.person"
                placeholder="请输入领用人"
                clearable/>
            </el-form-item>
            <el-form-item label="备注" prop="remarks" class="remarks">
              <el-input
                type="textarea"
                v-model="originalParams.remarks" size="mini" clearable placeholder="请输入内容">
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
            @click="handleExportProduct">导入原片
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
                    :data="originalFilmList"
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
              v-for="(item,index) in tableColumn"
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
                  @click.native.prevent="deleteRow(scope.$index, originalFilmList)"
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
        <el-button type="primary" size="mini" @click="saveOutbound('originalForm')">保存</el-button>
        <el-button size="mini" @click="outboundDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  新增盘库  -->
    <el-dialog title='新增盘库'
               :visible.sync="checkNumDialog" width="90%" class="dialog-style publicAddDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
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
            <el-form-item label="盘库日期" prop="entryDate">
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
            @click="handleExportProduct">导入原片
          </el-button>
        </div>
        <slot-table class="addTable">
          <el-table highlight-current-row
                    :data="originalFilmList"
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
              v-for="(item,index) in originalColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width">
            </el-table-column>
            <el-table-column label="盘库数量" width="180">
              <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.operationNum" :max="scope.stock" :precision="0"></el-input-number>
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
                  @click.native.prevent="deleteRow(scope.$index, originalFilmList)"
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
import {queryOriginalFilm, queryTree} from "@/api/original/originalFilm";
import {groupBy} from "@/utils/groupBy";
import CountTable from "@/components/public/table/countTable";
import OriginalDialog from "@/views/original/components/originalDialog";
import {getCurrentDay, isNumber, keepThreeNum} from "@/utils/order/order";
import {HyperFormula} from "hyperformula";
import {Message} from "element-ui";
import PurchaseDialog from "@/views/original/components/purchaseDialog";
import {productionNumber, originalWarehouse, purchaseWarehouse} from "@/api/original/warehouse";
import {queryPurchaseData, viewDetail,} from "@/api/original/originalPurchase";


export default {
  name: "index",
  components: {PurchaseDialog, OriginalDialog, CountTable, SlotTable, MainFlexibleTree},
  data() {
    return {
      treeData: [], //左侧tree树
      TreeDefaultProps: {
        children: 'children',
        label: 'originalTypeName'
      },
      queryParams: {
        id: "",
        originalName: "",
        originalNo: "",
        originalThick: '',
        originalTypeId: "",
        originalTypeName: "",
        pageNum: 1,
        pageSize: 20,
      },
      returnStatusOptions: [
        {
          value: 0,
          label: '审核通过'
        },
        {
          value: 1,
          label: '审核未通过'
        },
        {
          value: 2,
          label: '未审核'
        },
      ],
      total: 1,
      tableDate: [],
      // selectedOriginal:[],
      selectedOriginal: [],
      tableColumn: [
        {label: '原片名称', prop: 'originalName', width: '120'},
        {label: '长度（mm）', prop: 'originalLong', width: '100'},
        {label: '宽度（mm）', prop: 'originalWidth', width: '100'},
        {label: '原片编号', prop: 'originalNo', width: '100'},
        {label: '颜色 / 膜系', prop: 'originalColor', width: '100'},
        {label: '厚度（mm）', prop: 'originalThick', width: '100'},
        {label: '厂家名称', prop: 'millName', width: '150'},
        {label: '原片类型', prop: 'originalTypeName', width: '100'},
        {label: '等级', prop: 'originalGrade', width: '100'},
        // {label: '库存单价（元/m²）', prop: 'unitPrice', width: '130'},
        {label: '库存数量（片）', prop: 'stock', width: '120'},
      ],
      //总合计
      summation: [
        {label: 'stock', title: '库存总数量', value: 0, unit: '片'},
        {label: 'area', title: '库存总面积', value: 0, unit: 'm²'},
        // {label: 'totalAmount', title: '总金额', value: 0, unit: '元'},
      ],
      currentNodeInfo: {},
      originalDialog: false, //新增入库
      editDialog: false, //编辑入库
      originalEntryDialog: false, //原片入库
      purchaseEntryDialog: false, //采购单入库
      outboundDialog: false, //新增出库
      checkNumDialog: false, //新增盘库
      originalParams: {
        operationType: '',
        operationNo: '',
        operationDate: getCurrentDay(),
        millId: '',
        person: '',
        remarks: '',
        originalFilmList: []
      },
      purchaseOutBoundDate: [],
      originalFilmList: [],
      originalColumns: [
        {label: '原片名称', prop: 'originalName', width: '120'},
        {label: '长度（mm）', prop: 'originalLong', width: '100'},
        {label: '宽度（mm）', prop: 'originalWidth', width: '100'},
        {label: '原片编号', prop: 'originalNo', width: '100'},
        {label: '颜色 / 膜系', prop: 'originalColor', width: '100'},
        {label: '厚度（mm）', prop: 'originalThick', width: '100'},
        {label: '厂家名称', prop: 'millName', width: '150'},

        {label: '原片类型', prop: 'originalTypeName', width: '100'},
        {label: '等级', prop: 'originalGrade', width: '100'},
        {label: '库存数量（片）', prop: 'stock', width: '120'},
      ],
      purchaseParams: {
        operationType: '入库',
        operationNo: '',
        purchaseNo: '',
        operationDate: getCurrentDay(),
        millId: '',
        person: '',
        remarks: '',
        originalFilmPurchaseList: []
      },
      checkNumParams: {
        operationType: '盘库',
        operationNo: '',
        person: '',
        operationDate: getCurrentDay(),
        remarks: '',
        originalFilmList: []
      },
      purchaseOperationChangeDate: [],
      queryPurchase: {//查询采购单参数
        purchaseNo: '',
        purchasePerson: '',
        millName: '',
        status: "未入库",
        pageSize: 20,
        pageNum: 1,
        total: 0
      },
      originalFilmPurchaseList: [],   //采购单入库数据
      purchaseEntryDateColumn: [
        {label: '采购单号', prop: 'purchaseNo', width: '120', type: 'ipt'},
        {label: '供应商', prop: 'millName', width: '120', type: 'ipt'},
        {label: '采购负责人', prop: 'purchasePerson', width: '100', type: 'ipt'},
        {label: '采购日期', prop: 'purchaseDate', width: '180'},
        {label: '总数量（片）', prop: 'purchaseNum', width: '120'},
        {label: '总金额（元）', prop: 'purchaseAmount', width: '120'},
        {label: '入库状态', prop: 'status', width: '120'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      // originalFilmPurchaseList: [],
      purchaseOperationDateColumn: [
        {label: '采购单号', prop: 'purchaseNo', width: '120'},
        {label: '供应商', prop: 'millName', width: '120'},
        {label: '采购负责人', prop: 'purchasePerson', width: '100'},
        {label: '采购日期', prop: 'purchaseDate', width: '180'},
        {label: '总数量（片）', prop: 'purchaseNum', width: '120'},
        {label: '总面积（m²）', prop: 'purchaseArea', width: '120'},
        {label: '总金额（元）', prop: 'purchaseAmount', width: '120'},
        {label: '入库状态', prop: 'status', width: '120'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      selectOriginalDialog: false,  //选择原片
      selectPurchaseDialog: false,  //选择采购单
      originalFormRules: {
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
      purchaseFormRules: {
        purchaseNo: [
          {required: true, message: '请输入采购单号', trigger: 'blur'},
          {min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur"},
        ],
        operationNo: [
          {required: true, message: '请输入入库单号', trigger: 'blur'},
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
      checkNumFormRules: {
        operationNo: [
          {required: true, message: '请输入盘库单号', change: 'blur'},
        ],
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
          this.treeData = [{id: 0, originalTypeName: "原片类型", children: groupBy(res.data, "tid")}];
          if (this.treeData) {
            this.treeData.forEach(item => {
              item.originalTypeId = item.id
            })
          }
        }
      })
    },
    /* 左侧tree树单击节点 */
    handleNodeClick(val, node, component) {
      console.log(val, node, component);
      this.currentNodeInfo = val;
      this.queryParams.originalTypeId = val.id;
      this.handleQuery();
    },
    /* 查询原片数据 */
    handleQuery() {
      queryOriginalFilm(this.queryParams).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.tableDate = res.data;
          this.total = res.count;
          if (this.tableDate) {
            this.summation = [
              {label: 'stock', title: '库存总数量', value: 0, unit: '片'},
              {label: 'area', title: '库存总面积', value: 0, unit: 'm²'},
              // {label: 'totalAmount', title: '总金额', value: 0, unit: '元'},
            ];
            this.tableDate.forEach(item => {
              this.summation.forEach(sumItem => {
                if (item.hasOwnProperty('stock')) {
                  if (sumItem.label === 'stock') {
                    sumItem.value += item[sumItem.label]
                  }
                  if (sumItem.label === 'area') {
                    console.log(item.originalWidth, item.originalLong, item.stock)
                    sumItem.value += (item.originalWidth * item.originalLong * item.stock / 1000000)
                  }
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
      this.selectedOriginal = val;
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.templateTableRef.toggleRowSelection(row, column)
    },
    cellName({row, column, rowIndex, columnIndex}) {
      if (row.stock <= row.earlyWarning) {
        return 'earlyWarning'
      }
    },
    /* 翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + $index + 1;
    },
    // 是否显示下拉框
    isShowSelectOptions(isShowSelectOptions) {
      if (!isShowSelectOptions) {
        this.$refs.statusSelect.forEach(item => {
          item.blur();
          this.handleQuery();
        })
      }
    },
    /* 导入原片 */
    handleExportProduct() {
      this.selectOriginalDialog = true
    },
    /* 移除原片 */
    deleteRow(index, rows) {

      rows.splice(index, 1);
    },
    /* 原片入库 */
    handleOriginalEntry() {
      this.originalEntryDialog = true;
      this.originalParams = {
        operationType: '入库',
        operationNo: '',
        operationDate: getCurrentDay(),
        millId: '',
        remarks: ''
      };
      productionNumber({type: 0}).then(res => {
        this.originalParams.operationNo = res.msg;
      })
      this.originalFilmList = [];
    },
    /* 采购单入库 */
    handlePurchaseEntry() {
      this.purchaseEntryDialog = true;
      productionNumber({type: 0}).then(res => {
        this.purchaseParams.operationNo = res.msg;
        queryPurchaseData(this.queryPurchase).then(one => {
          this.originalFilmPurchaseList = one.data;
          this.queryPurchase.total = one.count;
        })
      })
      this.purchaseParams = {
        operationNo: '',
        purchaseNo: '',
        operationDate: getCurrentDay(),
        millName: '',
      };
    },
    /* 查询入库记录 */
    handleEntryRecord() {
      this.$router.push('/original/originalWarehouse/entryRecords');
    },
    /* 原片出库 */
    handleOutbound() {
      this.outboundDialog = true;
      this.originalParams = {
        operationType: '出库',
        operationNo: '',
        operationDate: getCurrentDay(),
        millId: '',
        remarks: ''
      };
      productionNumber({type: 1}).then(res => {
        this.originalParams.operationNo = res.msg;
      })
      this.originalFilmList = []
    },
    /* 出库记录 */
    handleOutRecord() {
      this.$router.push('/original/originalWarehouse/outboundRecords');
    },
    /* 新增盘库 */
    handleCheckNum() {
      this.checkNumParams = {
        operationType: '盘库',
        operationNo: '',
        person: '',
        operationDate: getCurrentDay(),
        remarks: '',
        originalFilmList: []
      }
      this.originalFilmList = []
      this.checkNumDialog = true;
      productionNumber({type: 2}).then(res => {
        this.checkNumParams.operationNo = res.msg;
      })
    },
    /* 盘库记录 */
    handleCheckNumRecord() {
      this.$router.push('/original/originalWarehouse/checkNumRecords');
    },
    /* 导出 */
    handleExport() {
      let ids = [];
      if (this.selectedOriginal.length > 0) {
        this.selectedOriginal.forEach(item => {
          ids.push(item.id)
        })
      }
      this.download('originalFilm/definition/exportOriginalFilm', {
        ids: ids.toString()
      }, `原片数据.xlsx`)
    },
    /* 单击采购单入库 */
    handleBlurEntry() {
      this.selectPurchaseDialog = true;
    },

    /* 保存选择原片数据 */
    saveSelectOriginal(val, tableData) {
      if (!val || val.length <= 0) {
        Message.warning("请选择一条数据进行添加");
        return false
      }
      if (val.length >= 1) {
        for (let i = 0; i < val.length; i++) {
          var index = tableData.indexOf(val[i])
          for (let j = 0; j < this.originalFilmList.length; j++) {
            if (val[i].id == this.originalFilmList[j].id){
              Message.warning("序号为" + (index + 1) + "的数据已经添加,不可重复添加！");
              return;
            }
          }
        }
        if (this.originalEntryDialog) {
          this.originalFilmList.push(...val);
          Message.success("添加成功");
          this.$refs.originalDialog.$refs.templateTableRef.clearSelection();
        }
        if (this.outboundDialog) {
          // purchaseOutBoundDate
          this.originalFilmList.push(...val);
          Message.success("添加成功");
          this.$refs.originalDialog.$refs.templateTableRef.clearSelection();
        }
        if (this.checkNumDialog) {
          this.originalFilmList.push(...val);
          Message.success("添加成功");
          this.$refs.originalDialog.$refs.templateTableRef.clearSelection();
        }
        this.selectOriginalDialog = false;
      }
    },
    /* 取消选择原片数据 */
    cancelSelectOriginal() {
      this.selectOriginalDialog = false;
      this.$refs.originalDialog.$refs.templateTableRef.clearSelection();
    },
    /* 保存入库 */
    saveOriginalEntry(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          this.originalParams.originalFilmList = this.originalFilmList;
          this.originalParams.operationType = "入库"
          originalWarehouse(this.originalParams).then(res => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              this.handleQuery()
              this.originalEntryDialog = false
            } else {
              this.$message({
                type: 'error',
                message: res.msg
              });
            }
          })
          // this.originalEntryDialog = false;
        } else {
          return false;
        }
      });
    },
    /* 选择采购单入库 */
    saveSelectPurchase(val) {
      console.log(val)
      // this.originalFilmPurchaseList=val;
      this.purchaseParams.purchaseNo = val.purchaseNo
      this.selectPurchaseDialog = false;
      viewDetail({id: val.id}).then(res => {
        this.originalFilmPurchaseList = res.data;
      })
    },
    //查询采购单数据
    queryPurchaseData() {
      queryPurchaseData(this.queryPurchase).then(one => {
        queryPurchaseData(this.queryPurchase).then(one => {
          this.originalFilmPurchaseList = one.data;
          this.queryPurchase.total = one.count;
        })
      })
    },
    cancelSelectPurchase() {
      // this.$emit('cancelSelectPurchase');
      this.selectPurchaseDialog = false;
    },
    purchaseOperationChange(val) {
      this.purchaseOperationChangeDate = val
    },
    /* 单击表格行 */
    purchaseOperationRowClick(row, column, event) {
      this.$refs.templateTableRef.toggleRowSelection(row, column)
    },
    /* 保存采购单入库 */
    savePurchaseEntry(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.originalFilmPurchaseList.length <= 0) {
            Message.warning("请添加采购单信息");
            return
          }
          this.purchaseParams.originalFilmPurchaseList = this.originalFilmPurchaseList
          purchaseWarehouse(this.purchaseParams).then(res => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              this.handleQuery()
            } else {
              this.$message({
                type: 'error',
                message: res.msg
              });
            }
          })
          this.purchaseEntryDialog = false;
        } else {
          return false;
        }
      })
    },
    /* 保存出库 */
    saveOutbound(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.originalParams.operationType = "出库"
          this.originalParams.originalFilmList = this.originalFilmList
          originalWarehouse(this.originalParams).then(res => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              this.handleQuery()
              this.originalEntryDialog = false
            } else {
              this.$message({
                type: 'error',
                message: res.msg
              });
            }
          })
          this.outboundDialog = false;
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
          this.checkNumParams.originalFilmList = this.originalFilmList
          originalWarehouse(this.checkNumParams).then(res => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              this.handleQuery()
            } else {
              this.$message({
                type: 'error',
                message: res.msg
              });
            }
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

  ::v-deep .earlyWarning {
    .cell {
      color: red;
    }
  }
}

</style>
