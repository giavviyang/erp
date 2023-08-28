<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="报损日期" class="daterange">
        <el-date-picker
          v-model="damageDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss" @change="handleQuery">
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item>-->
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
<!--      </el-form-item>-->
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-s-order"
        size="mini"
        @click="handleDetails"
        v-hasPermi="['system:damage:info']">查看明细
      </el-button>
      <el-dropdown>
        <el-button type="primary" size="mini"   icon="el-icon-plus" v-hasPermi="['system:damage:add']">
          新增报损<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="handleOrder"   icon="el-icon-plus" >订单报损</el-dropdown-item>
          <el-dropdown-item @click.native="handleFlow"   icon="el-icon-plus">流程卡报损</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"  v-hasPermi="['system:damage:edit']">编辑报损
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete" v-hasPermi="['system:damage:del']">删除报损
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-notebook-2"
        size="mini"
        @click="handlePatch" v-hasPermi="['system:damage:patch']">生成补片单
      </el-button>
      <el-button
        type="primary"
        size="mini"
        @click="handleExport" v-hasPermi="['system:damage:export']"><i class="iconfont icon-daochuwenjian"></i>导出
      </el-button>
    </div>
    <slot-table class="damageTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total">
      <el-table highlight-current-row
        :data="damageList"
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
          v-for="(item,index) in damageColumns"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
          <el-table-column :prop="item.prop" show-overflow-tooltip min-width="100" :min-width="item.width">
            <template #header scoped-slot="scope">
              <!--可根据类型来显示为搜索框、下拉框等-->
              <el-input
                v-model="queryParams[item.prop]"
                v-if="item.type == 'ipt'"
                size="mini"
                placeholder="请输入"
                clearable  @keyup.enter.native="handleQuery"/>
              <!--              <el-select v-if="item.type=='select'" v-model="queryParams[item.prop]" placeholder='请选择' clearable-->
              <!--                         size="mini">-->
              <!--                <el-option-->
              <!--                  v-for="item in dict.type.sys_pack"-->
              <!--                  :key="item.value"-->
              <!--                  :label="item.label"-->
              <!--                  :value="item.value">-->
              <!--                </el-option>-->
              <!--              </el-select>-->
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </slot-table>
    <!--  查看明细弹窗  -->
    <el-dialog title='查看明细'
               :visible.sync="detailDialog" width="85%" class="dialog-style detailDialog"
               :close-on-click-modal="false" :close-on-press-escape="false">
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
            show-overflow-tooltip></el-table-column>
        </el-table>
      </slot-table>
    </el-dialog>
    <!--  订单报损  -->
    <el-dialog title="订单报损"
               :visible.sync="oderDamageDialog" width="80%" class="dialog-style oderDamageDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <div class="damageInfo">
        <p class="title">报损信息</p>
        <div class="damageInfoContainer">
          <el-form :model="damageInfo" size="mini" :inline="true" ref="ruleForm" :rules="rules" class="ipt2">
            <el-form-item label="报损单号">
              <el-input v-model="damageInfo.damageNo" disabled clearable/>
            </el-form-item>
            <el-form-item label="报损人">
              <el-input v-model="damageInfo.damagePerson" disabled clearable/>
            </el-form-item>
            <el-form-item label="报损日期" prop="preparationDate">
              <el-date-picker
                v-model="damageInfo.preparationDate"
                :picker-options="preparationDateOptions"
                type="date"
                placeholder="选择日期时间"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="btn">
        <el-button
          type="primary"
          icon="el-icon-folder-add"
          size="mini"
          @click="handleExportOrder">选择订单
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-folder-remove"
          size="mini"
          @click="handleRemoveOrder">移除
        </el-button>
      </div>
      <slot-table class="orderTable">
        <el-table highlight-current-row
          :data="orderList"
          border
          stripe
          height="100%"
          style="width: 100%"
          ref="orderTable"
          @selection-change="handleOrderRemove"
          @row-click="RowClickOrderRemove"
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
            v-for="(item,index) in flowDataColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width"
            show-overflow-tooltip></el-table-column>
          <el-table-column
            label="报损数量" width="200">
            <template slot-scope="scope" style="width: 150px">
              <!--              item.num - item.shelfNum-->
              <el-input-number size="mini" v-model="scope.row.currentDamageNum" :min="0"
                               :max="Number(scope.row.itemNum) - Number(scope.row.completeNum) - Number(scope.row.damageNum)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="责任工序" width="120">
            <template slot-scope="scope">
              {{scope.row.responsibleProcess}}
            </template>
          </el-table-column>
          <el-table-column label="责任班组" width="120">
            <template slot-scope="scope">
              <el-select @change="teamChange($event,scope.$index)" v-model="scope.row.responsibleTeam"
                         placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in teamList"
                  :key="orderItem.id"
                  :label="orderItem.teamName"
                  :value="orderItem.teamName">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="责任人" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.responsiblePerson" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in scope.row.peopleList"
                  :key="orderItem.userId"
                  :label="orderItem.nickName"
                  :value="orderItem.nickName">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损原因" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.damageReason" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="item in dict.type.damage_reason"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损备注" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remarks" clearable
                        size="mini">
              </el-input>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveOrder('ruleForm')">保存</el-button>
        <el-button size="mini" @click="oderDamageDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  流程卡报损  -->
    <el-dialog title="流程卡报损"
               :visible.sync="flowDamageDialog" width="80%" class="dialog-style oderDamageDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <div class="damageInfo">
        <p class="title">报损信息</p>
        <div class="damageInfoContainer">
          <el-form :model="damageInfo" size="mini" :inline="true" ref="ruleForm" :rules="rules" class="ipt2">
            <el-form-item label="报损单号">
              <el-input v-model="damageInfo.damageNo" disabled clearable/>
            </el-form-item>
            <el-form-item label="报损人">
              <el-input v-model="damageInfo.damagePerson" disabled clearable/>
            </el-form-item>
            <el-form-item label="报损日期" prop="preparationDate">
              <el-date-picker
                v-model="damageInfo.preparationDate"
                :picker-options="preparationDateOptions"
                type="date"
                placeholder="选择报损日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="btn">
        <el-button
          type="primary"
          icon="el-icon-folder-add"
          size="mini"
          @click="handleExportFlow">选择流程卡
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-folder-remove"
          size="mini"
          @click="handleRemoveFlow">移除
        </el-button>
      </div>
      <slot-table class="orderTable">
        <el-table highlight-current-row
          :data="orderList"
          border
          stripe
          height="100%"
          style="width: 100%"
          ref="flowTable"
          @selection-change="handleOrderRemove"
          @row-click="RowClickOrderRemove2"
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
            v-for="(item,index) in flowDataColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width"
            show-overflow-tooltip></el-table-column>
          <el-table-column
            label="报损数量" width="200">
            <template slot-scope="scope" style="width: 150px">
              <!--              item.num - item.shelfNum-->
              <el-input-number size="mini" v-model="scope.row.currentDamageNum" :min="0"
                               :max="Number(scope.row.itemNum) - Number(scope.row.completeNum) - Number(scope.row.damageNum)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="责任工序" width="120">
            <template slot-scope="scope">
              {{scope.row.responsibleProcess}}
            </template>
          </el-table-column>
          <el-table-column label="责任班组" width="120">
            <template slot-scope="scope">
              <el-select @change="teamChange($event,scope.$index)" v-model="scope.row.responsibleTeam"
                         placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in teamList"
                  :key="orderItem.id"
                  :label="orderItem.teamName"
                  :value="orderItem.teamName">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="责任人" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.responsiblePerson" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in scope.row.peopleList"
                  :key="orderItem.userId"
                  :label="orderItem.nickName"
                  :value="orderItem.nickName">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损原因" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.damageReason" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="item in dict.type.damage_reason"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损备注" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remarks" clearable
                        size="mini">
              </el-input>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveOrder('ruleForm')">保存</el-button>
        <el-button size="mini" @click="flowDamageDialog = false">取消</el-button>
      </span>
    </el-dialog>
    <!--  选择订单  -->
    <order-dialog :exportOrderDialog="exportOrderDialog"
                  :productList="productList"
                  :pageSize="orderPageSize"
                  :pageNum="orderPageNum"
                  :total="orderTotal"
                  @handlePage="handleOrderPage"
                  @handleOrder="handleDialogOrder"
                  @addOrderList="addOrderList"
                  @backOrderList="backOrderList"
                  :loading="orderLoading"
                  ref="orderDialog"></order-dialog>
    <!--  选择流程卡 -->
    <flow-dialog :exportFlowDialog="exportFlowDialog"
                 :productList="productList"
                 :pageSize="flowPageSize"
                 :pageNum="flowPageNum"
                 :total="flowTotal"
                 @handlePage="handleFlowPage"
                 @handleFlow="handleDialogFlow"
                 @addFlowList="addFlowList"
                 @backFlowList="backFlowList"
                 :loading="flowCardLoading"
                 ref="flowDialog"></flow-dialog>
    <!--  编辑报损  -->
    <el-dialog title="编辑报损"
               :visible.sync="editDamageDialog" width="80%" class="dialog-style oderDamageDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <div class="damageInfo">
        <p class="title">报损信息</p>
        <div class="damageInfoContainer">
          <el-form :model="damageInfo" size="mini" :inline="true" ref="ruleForm" :rules="rules" class="ipt2">
            <el-form-item label="报损单号">
              <el-input v-model="damageInfo.damageNo" disabled clearable/>
            </el-form-item>
            <el-form-item label="报损人">
              <el-input v-model="damageInfo.damagePerson" disabled clearable/>
            </el-form-item>
            <el-form-item label="报损日期" prop="preparationDate">
              <el-date-picker
                disabled
                v-model="damageInfo.damageDate"
                :picker-options="preparationDateOptions"
                type="date"
                placeholder="选择报损日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <slot-table class="orderTable" style="margin-top: 20px;">
        <el-table highlight-current-row
          :data="damageInfo.damageFlowCardList"
          border
          stripe
          height="100%"
          style="width: 100%"
          ref="orderTable"
          @selection-change="handleOrderRemove"
          @row-click="RowClickOrderRemove"
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
            v-for="(item,index) in editFlowDataColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width"
            show-overflow-tooltip></el-table-column>
          <el-table-column
            label="报损数量" width="200">
            <template slot-scope="scope" style="width: 150px">
              <!--              item.num - item.shelfNum-->
              <el-input-number size="mini" v-model="scope.row.damageNum" :min="0"
                               :max="Number(scope.row.maxNum)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="责任工序" width="120">
            <template slot-scope="scope">
              {{scope.row.responsibleProcess}}
            </template>
          </el-table-column>
          <el-table-column label="责任班组" width="120">
            <template slot-scope="scope">
              <el-select @change="teamChange($event,scope.$index)" v-model="scope.row.responsibleTeam"
                         placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in teamList"
                  :key="orderItem.id"
                  :label="orderItem.teamName"
                  :value="orderItem.teamName">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="责任人" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.responsiblePerson" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in scope.row.peopleList"
                  :key="orderItem.userId"
                  :label="orderItem.nickName"
                  :value="orderItem.nickName">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损原因" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.damageReason" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="item in dict.type.damage_reason"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损备注" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remarks" clearable
                        size="mini">
              </el-input>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveEdit('ruleForm')">保存</el-button>
         <el-button size="mini" @click="editDamageDialog = false">取消</el-button>
      </span>
    </el-dialog>
    <!--  生成补片单  -->
    <el-dialog title="生成补片单"
               :visible.sync="patchDialog" width="90%" class="dialog-style oderDamageDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <!--      <div >-->
      <p class="title" style="text-align: left;margin-bottom: 0;overflow: auto">
        补片说明 <span style="color: red;display: inline-block;margin-left: 10px;">补片时，需要订单编号、客户名称、产品名称、单片名称、厚度、工艺相同才可以进行补片</span>
      </p>
      <slot-table  :isPage="false" style="height: calc(100% - 30px)">
        <el-table highlight-current-row
          :data="damagePatchList"
          stripe
          border
          style="width: 100%"
          height="100%"
          ref="multipleTable"
          @selection-change="selectPatcch"
          @row-click="handleRowClick"
          slot="table">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            :index="1"
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in patchDetailCloumns"
            :key="index"
            :label="item.label"
            :prop="item.prop"
            :min-width="item.width"
            show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            label="报损数量" width="180">
            <template slot-scope="scope" style="width: 150px">
              <el-input-number size="mini" v-model="scope.row.currentPatchNum" :min="0"
                               :max="Number(scope.row.damageNum)"></el-input-number>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="savePatch">保存</el-button>
         <el-button size="mini" @click="patchDialog=false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import SlotTable from "@/components/public/table/SlotTable";
import {getCurrentDay} from "@/utils/order/order";
import {Message} from "element-ui";
import OrderDialog from "@/views/production/components/orderDialog";
import FlowDialog from "@/views/production/components/flowDialog";
import {getOrderList, getFlowCardList, getNo, addDamage, getList ,getDetail,editDamage,delDamage} from "@/api/product/damage"
import {getAllCraftList} from "@/api/system/craft"
import {getAllTeam} from "@/api/system/team"
import {listAllUser} from "@/api/system/user";
import {addPatch, getDamageInfo} from "@/api/product/patch";

export default {
  name: "index",
  components: {FlowDialog, OrderDialog, SlotTable},
  dicts: ['damage_reason'],
  data() {
    return {
      orderPageSize:20,
      orderPageNum:1,
      orderTotal:0,
      flowPageSize:20,
      flowPageNum:1,
      flowTotal:0,
      damagePatchList:[],
      selectedPatchList:[],
      patchDetailCloumns:[
      {label: '订单编号', prop: 'business.orderNo', width: '140'},
      {label: '流程卡号', prop: 'business.flowCardNo', width: '140'},
      {label: '客户名称', prop: 'business.customerName', width: '100'},
      {label: '项目名称', prop: 'business.entryName', width: '120'},
      {label: '产品名称', prop: 'business.productName', width: '120'},
      {label: '单片名称', prop: 'business.itemName', width: '120'},
      {label: '规格', prop: 'specs', width: '100'},
      {label: '报损工序', prop: 'responsibleProcess', width: '100'},
      {label: '报损数量（片）', prop: 'damageNum', width: '120'},
      {label: '报损面积（m²）', prop: 'damageArea', width: '120'},
      {label: '责任班组', prop: 'responsibleTeam', width: '100'},
      {label: '责任人', prop: 'responsiblePerson', width: '100'},
      {label: '报损原因', prop: 'damageReason', width: '120'},
      {label: '备注', prop: 'remarks', width: '120'}
      ],
      queryParams: {
        customerName: "",
        deliverPerson: "",
        id: "",
        orderId: "",
        orderNo: "",
        preparationDateStart: '',
        preparationDateEnd: '',
        schedulingMode: "",
        schedulingName: "",
        schedulingNo: "",
        pageNum: 1,
        pageSize: 20,
        productName: "",
        projectName: "",
        responsiblePerson: ""
      },
      damageDateRange: [], //排产日期范围
      damageList: [],  //报损单列表
      //报损表头
      damageColumns: [
        {label: '报损单号', prop: 'damageNo', type: 'ipt', width: '150'},
        {label: '报损数量（片）', prop: 'damageNum', width: '150'},
        {label: '补片数量（片）', prop: 'patchNum', width: '150'},
        {label: '补片状态', prop: 'status', width: '150'},
        {label: '面积（m²）', prop: 'damageArea', width: '150'},
        {label: '报损时间', prop: 'damageDate', width: '180'},
        {label: '报损人', prop: 'createdPerson', width: '100'},
        {label: '备注', prop: 'remarks', width: '150'},
      ],
      pageSize: 20,
      pageNum: 1,
      total: 0,
      selected: [],  //报损单复选框
      detailDialog: false,  //明细弹窗
      //明细信息
      detailsInfo: [
        {title: '报损单号', value: ''},
        {title: '报损数量（片）', value: ''},
        {title: '报损面积（m²）', value: ''},
      ],
      detailDialogData: [],  //明细数据
      //订单明细表头
      detailDialogColumns: [
        {label: '流程卡号', prop: 'business.orderNo', width: '150'},
        {label: '客户名称', prop: 'business.customerName', width: '150'},
        {label: '项目名称', prop: 'business.entryName', width: '200'},
        {label: '产品名称', prop: 'business.productName', width: '200'},
        {label: '单片名称', prop: 'business.itemName', width: '200'},
        {label: '宽（mm）', prop: 'business.itemW', width: '110'},
        {label: '高（mm）', prop: 'business.itemH', width: '110'},
        {label: '报损数量（片）', prop: 'damageNum', width: '120'},
        {label: '责任工序', prop: 'responsibleProcess', width: '100'},
        {label: '责任班组', prop: 'responsibleTeam', width: '100'},
        {label: '责任人', prop: 'responsiblePerson', width: '100'},
        {label: '报损原因', prop: 'damageReason', width: '100'},
        {label: '报损备注', prop: 'remarks', width: '100'},
      ],
      oderDamageDialog: false,  //订单报损弹窗
      flowDamageDialog: false,  //流程卡报损
      damageInfo: {
        damageNo: '',
        damagePerson: '',
        damageDate: getCurrentDay(),
      },
      // 排产时间不能晚于当前时间
      preparationDateOptions: {
        disabledDate: time => {
          return time.getTime() < Date.now()
        },
      },
      orderList: [],  //订单报损表
      selectedOrder: [], //订单报损复选框
      orderDataColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '项目名称', prop: 'entryName', width: '200'},
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '宽（mm）', prop: 'itemW', width: '110'},
        {label: '高（mm）', prop: 'itemH', width: '110'},
        {label: '数量（片）', prop: 'num', width: '100'},
        {label: '加工要求', prop: 'requirement', width: '100'},
        {label: '已报数量（片）', prop: 'damageNum', width: '150'},
      ],
      flowDataColumns: [
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '项目名称', prop: 'entryName', width: '200'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '单片名称', prop: 'itemName', width: '150'},
        {label: '宽（mm）', prop: 'itemW', width: '110'},
        {label: '高（mm）', prop: 'itemH', width: '110'},
        {label: '数量（片）', prop: 'itemNum', width: '100'},
        {label: '已报损数量（片）', prop: 'damageNum', width: '120'},
        {label: '已完工数量（片）', prop: 'completeNum', width: '120'},
        {label: '加工要求', prop: 'requirement', width: '100'}
      ],
      editFlowDataColumns: [
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '订单编号', prop: 'business.orderNo', width: '120'},
        {label: '客户名称', prop: 'business.customerName', width: '200'},
        {label: '项目名称', prop: 'business.entryName', width: '200'},
        {label: '产品名称', prop: 'business.productName', width: '150'},
        {label: '单片名称', prop: 'business.itemName', width: '150'},
        {label: '宽（mm）', prop: 'business.itemW', width: '110'},
        {label: '高（mm）', prop: 'business.itemH', width: '110'},
        {label: '数量（片）', prop: 'business.itemNum', width: '100'},
        {label: '加工要求', prop: 'business.requirement', width: '100'}
      ],
      orderTypeOptions: [{
        value: 0,
        label: '普通订单'
      }, {
        value: 'rushOrder',
        label: '加急订单'
      }, {
        value: 'sampleOrder',
        label: '样品订单'
      }, {
        value: 'materialOrder',
        label: '来料订单'
      }, {
        value: 'outsourcingOrders',
        label: '外协订单'
      }],
      exportOrderDialog: false,  //导入订单产品信息弹窗
      exportFlowDialog: false,    //导入流程卡产品信息弹窗
      editDamageDialog: false,  //编辑报损
      patchDialog: false,  //生成补片单
      // 表单校验
      rules: {
      },
      productList: [],
      craftList: [],
      teamList: [],
      orderLoading:false,
      flowCardLoading:false
    }
  },
  created() {
    this.handleQuery();
  },
  async mounted() {
    // // this.keyupSubmit();
    let {data} = await getAllCraftList()
    this.craftList = data
    let {data: teamList} = await getAllTeam();
    this.teamList = teamList
  },
  methods: {
    selectPatcch(val){
      this.selectedPatchList = val;
    },
    handleOrderPage(params){
      let {size,num}  = params
      this.orderPageSize = size
      this.orderPageNum = num
      this.handleDialogOrder()
    },
    handleFlowPage(params){
      let {size,num}  = params
      this.flowPageSize = size
      this.flowPageNum = num
      this.handleDialogFlow()
    },
    async teamChange(event, index) {
      let teamList = this.teamList.filter(item => {
        return item.teamName == event
      })[0].id
      let {rows} = await listAllUser({teamId: teamList});
      this.$set(this.orderList[index], "peopleList", rows);
    },
    async handleDialogFlow(){
      if (this.$refs.flowDialog) {
        let params = this.$refs.flowDialog.exportFlowParams
        this.flowCardLoading = true
        getFlowCardList({...params,pageSize:this.flowPageSize,pageNum:this.flowPageNum}).then(res => {
          this.flowCardLoading = false
          this.productList = res.data.map(item => {
            item.specs = `${item.itemW}*${item.itemH}`
            return item
          })
          this.flowTotal = res.count
        })
      }
    },
    handleDialogOrder() {
      if (this.$refs.orderDialog) {
        let params = this.$refs.orderDialog.exportOrderParams
        this.orderLoading = true
        getOrderList({...params,pageSize:this.orderPageSize,pageNum:this.orderPageNum}).then(res => {
          this.orderLoading = false
          this.productList = res.data.map(item => {
            item.specs = `${item.itemW}*${item.itemH}`
            return item
          })
          this.orderTotal = res.count
        })
      }
    },
    // //键盘按下enter搜索事件
    // keyupSubmit() {
    //   document.onkeydown = e => {
    //     const _key = window.event.keyCode
    //     if (_key === 13) {
    //       this.handleQuery();
    //     }
    //   }
    // },
    /* 各种查询条件查询 */
    handleQuery() {
      if (this.damageDateRange) {
        this.queryParams.preparationDateStart = this.damageDateRange[0];
        this.queryParams.preparationDateEnd = this.damageDateRange[1];
      } else {
        this.queryParams.preparationDateStart = '';
        this.queryParams.preparationDateEnd = '';
      }
      this.queryParams.beginDate = this.queryParams.preparationDateStart
      this.queryParams.endDate = this.queryParams.preparationDateEnd
      getList(this.queryParams).then(res => {
        this.damageList = res.data.map(item=>{
          if(item.patchNum > 0){
            item.status = '补片中'
          }else if(item.patchNum == item.damageNum){
            item.status = '已补片'
          }else{
            item.status = '未补片'
          }
          return item
        })
        this.total = res.count
      })
    },
    /* 报损单重置 */
    resetQuery() {
      this.damageDateRange = [];
      this.queryParams = {
        customerName: "",
        deliverPerson: "",
        id: "",
        orderId: "",
        orderNo: "",
        preparationDateStart: '',
        preparationDateEnd: '',
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
        this.detailDialog = true;
      }
      let {id} = this.selected[0]
      getDetail(id).then(res=>{
        this.detailsInfo = [
          {title: '报损单号', value: res.data.damageNo},
          {title: '报损数量（片）', value: res.data.damageNum},
          {title: '报损面积（m²）', value: res.data.damageArea},
        ]
        this.detailDialogData = res.data.damageFlowCardList
      })
    },
    /* 订单报损 */
    async handleOrder() {
      let {data} = await getNo();
      this.damageInfo.damageNo = data.damageNo
      this.damageInfo.damagePerson = data.damageUser
      this.orderList = []
      this.productList = []
      this.oderDamageDialog = true;
    },
    /* 流程卡报损 */
    async handleFlow() {
      let {data} = await getNo();
      this.damageInfo.damageNo = data.damageNo
      this.damageInfo.damagePerson = data.damageUser
      this.orderList = []
      this.productList = []
      this.flowDamageDialog = true;
    },
    /* 编辑报损 */
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
        if(this.selected[0].status != '未补片'){
          Message.warning(`当前报损单状态为[${this.selected[0].status}],无法编辑`)
          return false;
        }
        this.editDamageDialog = true;
      }
      let {id} = this.selected[0]
      getDetail(id).then(res=>{
        this.damageInfo = res.data
        this.damageInfo.damageFlowCardList = this.damageInfo.damageFlowCardList.map(item=>{
          item.maxNum = item.damageNum
          return item
        })
      })
    },
    /* 保存编辑报损 */
    saveEdit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          editDamage(this.damageInfo.id,this.damageInfo).then(res=>{
            this.$message.success(res.msg)
            this.editDamageDialog = false
          })
        } else {
          return false;
        }
      })
    },
    /* 删除报损 */
    handleDelete() {
      if (this.selected.length != 1) {
        Message.warning("请选择一条要删除的数据");
        return;
      }
      if(this.selected[0].status != '未补片'){
        Message.warning(`当前报损单状态为[${this.selected[0].status}],无法删除`)
        return false;
      }
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let { status,id}  = this.selected[0]
        if(status == "未补片"){
          delDamage(id).then(res=>{
            this.$message.success(res.msg);
            this.handleQuery();
          })
        }else{
          this.$message.warning(`当前补片状态为${status},无法删除`)
        }
      }).catch(() => {
    Message.info({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /* 生成补片单 */
    handlePatch() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要修改的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行修改");
        return false
      }
      if (this.selected.length == 1) {
        let ids = this.selected.map(item=>{
          return item.id;
        }).toString()
        getDamageInfo({ids}).then(res=>{
          this.damagePatchList = res.data.map(item=>{
            item.specs = `${item.business.itemW}*${item.business.itemH}`
            item.currentPatchNum = item.damageNum
            return item
          })
          this.patchDialog = true
        })
      }
    },
    /* 保存补片 */
    savePatch() {
      if(this.selectedPatchList <= 0){
        this.$message.warning("请选择要补片的产品");
        return false;
      }
      let submitData = this.selectedPatchList.filter(item=>{return item.currentPatchNum > 0}).map(item=>{
        return {
          damageId:item.damageId,
          damageBusinessId:item.id,
          flowCardNo:item.business.flowCardNo,
          productId:item.business.productId,
          semiProductId:item.business.semiProductId,
          patchNum:item.currentPatchNum,
          patchArea:Number(item.currentPatchNum)*(Number(item.business.totalArea) / Number(item.business.itemNum))
        }
      })
      addPatch(submitData).then(res=>{
        this.patchDialog = false
        const h = this.$createElement;
        this.$msgbox({
          title: '消息',
          message: h('p', null, [
            h('span', null, '成功完成补片并创建补片流程卡 编号[ '),
            h('span', { style: 'color: #409eff' }, res.msg),
            h('span', null, '] ')
          ]),
          type: 'success',
          showCancelButton: false,
          confirmButtonText: '确定'
        }).then(() => {
          this.handleQuery();
        }).catch(()=>{
          this.handleQuery();
        })
      })
    },
    /* 导出 */
    handleExport() {
      let ids = this.selected.map(item=>{
        return item.id
      })
      this.download('produce/damage/export', {
        ids: ids.toString()
      }, `报损数据.xlsx`)
    },
    /* 报损单分页器 */
    handleChange(size, num) {
      this.pageSize = size;
      this.pageNum = num;
      this.queryParams.pageNum = this.pageNum;
      this.queryParams.pageSize = this.pageSize;
      this.handleQuery();
    },
    /* 排产单翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 排产复选框 */
    handleSelectionChange(val) {
      this.selected = val;
    },
    /* 行点击事件 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },
    /* 选择订单 */
    handleExportOrder() {
      this.productList = []
      this.exportOrderDialog = true
      this.handleDialogOrder()
    },
    /* 移除订单 */
    handleRemoveOrder(){
      this.selectedOrder.forEach(item => {
        let index = this.orderList.findIndex(item2 => item2.id == item.id)
        console.log(index)
        if(index != -1){
          this.orderList.splice(index,1)
        }
      })

    },
    handleEditRemoveOrder(index){
      this.damageInfo.damageFlowCardList.splice(index,1);
    },
    /* 添加订单 */
    addOrderList(val) {
      console.log(val)
      if(val <= 0){
        Message.warning("请选择要报损的订单")
      }
      let checkNum = 0
      val.forEach(item => {
        let filter = this.orderList.filter(f => {
          return f.flowCardNo == item.flowCardNo && f.orderNo == item.orderNo && f.itemName == item.itemName
        })
        item.currentDamageNum = item.itemNum - item.damageNum - item.completeNum
        item.responsibleProcess = item.currentCraft
        if (filter.length <= 0) {
          checkNum++;
          this.orderList.push(item);
        }
      })
      if(checkNum <= 0){
        Message.warning("订单中无可报损产品")
      }else{
        Message.success("添加成功");
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
    /* 选择产品信息复选框移除 */
    handleOrderRemove(val) {
      this.selectedOrder = val;
    },
    /* 产品信息移除行点击事件 */
    RowClickOrderRemove(row, column, event) {
      console.log()
      this.$refs.orderTable.toggleRowSelection(row, column)
    },
    RowClickOrderRemove2(row, column, event){
      console.log()
      this.$refs.flowTable.toggleRowSelection(row, column)
    },
    /* 保存订单报损 */
    saveOrder(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.orderList.length <= 0) {
            Message.warning("请导入订单信息进行报损");
          } else {
            console.log(this.damageInfo)

            let damageFlowCard = this.orderList.filter(item => {
              return item.currentDamageNum && item.currentDamageNum != 0
            }).map(item => {
              return {
                flowCardNo: item.flowCardNo,
                productId: item.productId,
                semiProductId: item.semiProductId,
                damageNum: item.currentDamageNum,
                damageArea: item.totalArea,
                responsibleProcess: item.responsibleProcess,
                responsibleTeam: item.responsibleTeam,
                responsiblePerson: item.responsiblePerson,
                damageReason: item.damageReason,
                remarks: item.remarks,
              }
            })
            this.damageInfo.damageNum = 0
            this.damageInfo.damageArea = 0
            damageFlowCard.forEach(item => {
              this.damageInfo.damageNum += item.damageNum
              this.damageInfo.damageArea += item.damageArea
            })
            this.damageInfo.damageDate = this.damageInfo.preparationDate
            addDamage({...this.damageInfo, damageFlowCardList: damageFlowCard}).then(res => {
              this.$message.success(res.msg)
              this.flowDamageDialog = false
              this.oderDamageDialog = false
              this.handleQuery();
            })
          }
        } else {
          return false;
        }
      })
    },
    /* 选择流程卡 */
    handleExportFlow() {
      this.productList = []
      this.exportFlowDialog = true;
      this.handleDialogFlow()
    },
    /* 移除流程卡 */
    handleRemoveFlow() {
      this.selectedOrder.forEach(item => {
        let index = this.orderList.findIndex(item2 => item2.id == item.id)
        console.log(index)
        if(index != -1){
          this.orderList.splice(index,1)
        }
      })
    },
    /* 添加订单 */
    addFlowList(val) {
      console.log(val)
      let checkNum = 0
      val.forEach(item => {
        let filter = this.orderList.filter(f => {
          return f.flowCardNo == item.flowCardNo && f.orderNo == item.orderNo && f.itemName == item.itemName
        })
        item.currentDamageNum = item.itemNum - item.damageNum - item.completeNum
        item.responsibleProcess = item.currentCraft
        if (filter.length <= 0) {
          checkNum++;
          this.orderList.push(item);
        }
      })
      if(checkNum <= 0){
        Message.warning("此流程卡已添加")
      }else{
        Message.success("添加成功");
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
.damageTable {
  height: calc(100% - 100px);
}

::v-deep .detailDialog {
  .el-dialog {
    height: calc(60vh);
    //height: 700px;

    padding: 0;

    .detailsInfo {
      height: 30px;
      //justify-content: flex-start;
      //margin-bottom: 0;

      p {
        min-width: 200px;
      }
    }

    .detailsTable {
      height: calc(100% - 50px);
    }

  }
}

::v-deep .oderDamageDialog {
  .el-dialog {
    //height: 700px;
    height: calc(70vh);

    .el-dialog__body {
      height: calc(100% - 90px);

      .damageInfo {
        height: 95px;
        border-radius: 15px;
        box-shadow: 0 1px 5px 0 rgba(0, 0, 0, 0.3);

        .title {
          height: 30px;
          text-align: left;
          padding-left: 40px;
          margin-bottom: 0 !important;
        }

        .damageInfoContainer {
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

      .orderTable {
        max-height: calc(80% - 50px);
        min-height: 200px;

        .table {
          height: 100%;
          //::v-deep .el-textarea__inner{
          //   height: 28px;
          // }
        }

        .page {
          display: none;
        }
      }
    }

  }

}
</style>
