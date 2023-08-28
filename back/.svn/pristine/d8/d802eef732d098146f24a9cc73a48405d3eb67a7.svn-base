<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="补片日期" class="daterange">
        <el-date-picker
          v-model="patchDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          @change="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="补片单号">
        <el-input
          v-model="queryParams.patchNo"
          placeholder="请输入补片单号"
          @keyup.enter.native="handleQuery"
          clearable/>
      </el-form-item>
<!--      <el-form-item>-->
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery" v-hasPermi="['system:patch:list']">重置</el-button>-->
<!--      </el-form-item>-->
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-s-order"
        size="mini"
        @click="handleDetails" v-hasPermi="['system:patch:getInfo']">查看明细
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleAdd" v-hasPermi="['system:patch:add']">报损补片
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete" v-hasPermi="['system:patch:del']">删除补片
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-printer"
        size="mini"
        @click="handlePrint" v-hasPermi="['system:patch:print']">打印
      </el-button>
    </div>
    <slot-table class="patchTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total">
      <el-table highlight-current-row
        :data="patchList"
        stripe
        border
        style="width: 100%"
        height="100%"
        ref="multipleTable2"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick2"
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
          v-for="(item,index) in patchColumns"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <!--  查看明细弹窗  -->
    <el-dialog title='查看明细'
               :visible.sync="detailDialog" width="90%" class="dialog-style detailDialog"
               :close-on-click-modal="false" :close-on-press-escape="false">
      <div class="detailsInfo">
        <p>补片编号：<span>{{ detailForm.patchNo }}</span></p>
        <p>补片人：<span>{{ detailForm.patchPerson }}</span></p>
        <p>补片数量：<span>{{ detailForm.patchNum }}</span></p>
        <p>补片日期：<span>{{ detailForm.patchDate }}</span></p>
      </div>
      <slot-table class="detailsTable">
        <el-table highlight-current-row
          :data="detailForm.patchBusinessList"
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
    <!--  报损补片  -->
    <el-dialog title='报损补片'
               :visible.sync="addPatchDialog" width="80%" class="dialog-style addPatchDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form :model="damageParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
        <el-form-item label="报损单号">
          <el-input v-model="damageParams.damageNo" clearable  @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleDamage">搜索</el-button>
        </el-form-item>
      </el-form>
      <slot-table class="damageTable" @handleChange="handleChangeDamage" :pageSize="damageParams.pageSize"
                  :pageNum="damageParams.pageNum"
                  :total="damageParams.count">
        <el-table highlight-current-row
          :data="damageList"
          stripe
          border
          style="width: 100%"
          height="100%"
          ref="multipleTable"
          @selection-change="handleSelectionDamage"
          @row-click="handleRowDamage"
          slot="table">
          <el-table-column
            type="selection"
            :selectable="checkSelect"
            width="55">
          </el-table-column>
          <el-table-column
            :index="getIndexDamage"
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in damageColumns"
            :key="index"
            :label="item.label"
            :prop="item.prop"
            :min-width="item.width"
            show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveAdd">保存</el-button>
        <el-button size="mini" @click="addPatchDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  报损补片  -->
    <el-dialog title='生成补片单'
               :visible.sync="patchDialog" width="60%" class="dialog-style addPatchDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form class="iptAndBtn" size="mini">
        <el-form-item label="补片说明" style="width: 100%;">
          <span style="color: red;">补片时,需要订单编号、客户名称、产品名称、单片名称、厚度、工艺相同才可以补片</span>
        </el-form-item>
      </el-form>
      <slot-table class="damageTable" @handleChange="handleChangeDamage" :pageSize="damageParams.pageSize"
                  :pageNum="damageParams.pageNum"
                  :total="damageParams.count" :isPage="false">
        <el-table highlight-current-row
          :data="damagePatchList"
          stripe
          border
          style="width: 100%"
          height="100%"
          ref="multipleTable"
          @selection-change="selectPatcch"
          @row-click="handleRowDamage"
          slot="table">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            :index="getIndexDamage"
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
            label="报损数量" width="200">
            <template slot-scope="scope" style="width: 150px">
              <el-input-number size="mini" v-model="scope.row.currentPatchNum" :min="0"
                               :max="Number(scope.row.damageNum)"></el-input-number>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="savePatch">保存</el-button>
        <el-button size="mini" @click="addPatchDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  编辑补片  -->
    <el-dialog title="编辑补片"
               :visible.sync="editPatchDialog" width="80%" class="dialog-style addPatchDialog"
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <!--      <div >-->
      <p class="title" style="text-align: left;margin-bottom: 0;overflow: auto">
        补片说明 <span style="color: red;display: inline-block;margin-left: 10px;">补片时，需要订单编号、客户名称、产品名称、单片名称、厚度、工艺相同才可以进行补片</span>
      </p>
      <slot-table class="damageTable" style="height: calc(100% - 40px);">
        <el-table highlight-current-row
          :data="damageList"
          border
          stripe
          height="100%"
          style="width: 100%"
          ref="multipleTable"
          @selection-change="handleSelectionDamage"
          @row-click="handleRowDamage"
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
            v-for="(item,index) in damageColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :min-width="item.width"
            show-overflow-tooltip></el-table-column>
          <el-table-column
            label="报损数量" prop="damageNum">
          </el-table-column>
          <el-table-column
            label="补片数量" width="200" >
            <template slot-scope="scope" style="width: 150px">
              <!--              item.num - item.shelfNum-->
              <el-input-number size="mini" v-model="scope.row.patchNum" :min="1"
                               :max="Number(scope.row.patchNum)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="责任工序" width="120" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-select v-model="scope.row.responsibleProcess" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in orderTypeOptions"
                  :key="orderItem.value"
                  :label="orderItem.label"
                  :value="orderItem.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="责任班组" width="120" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-select v-model="scope.row.responsibleTeam" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in orderTypeOptions"
                  :key="orderItem.value"
                  :label="orderItem.label"
                  :value="orderItem.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="责任人" width="120" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-select v-model="scope.row.responsiblePerson" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in orderTypeOptions"
                  :key="orderItem.value"
                  :label="orderItem.label"
                  :value="orderItem.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损原因" width="120" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-select v-model="scope.row.damageReason" placeholder='请选择' clearable
                         size="mini">
                <el-option
                  v-for="orderItem in orderTypeOptions"
                  :key="orderItem.value"
                  :label="orderItem.label"
                  :value="orderItem.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="报损备注" width="200" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-input v-model="scope.row.remarks" clearable
                        size="mini">
              </el-input>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="editPatch">保存</el-button>
         <el-button size="mini" @click="editPatchDialog=false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import SlotTable from "@/components/public/table/SlotTable";
import {Message} from "element-ui";
import {delDamage, getList} from "@/api/product/damage";
import {getDamageInfo,addPatch,getPatchList,getPatchInfo,delPatch} from "@/api/product/patch";

export default {
  name: "index",
  components: {SlotTable},
  data() {
    return {
      queryParams: {
        patchNo:"",
        pageNum: 1,
        pageSize: 20,
      },
      patchDateRange: [], //排产日期范围
      patchList: [
      ],  //补片单列表
      //补片表头
      patchColumns: [
        {label: '补片单号', prop: 'patchNo', type: 'ipt', width: '150'},
        {label: '补片数量（片）', prop: 'patchNum', width: '150'},
        {label: '面积（m²）', prop: 'patchArea', width: '150'},
        {label: '补片时间', prop: 'patchDate',type: 'Date', width: '180'},
        {label: '补片人', prop: 'createdPerson', width: '100'}
      ],
      pageSize: 20,
      pageNum: 1,
      total: 0,
      selected: [],  //补片单复选框
      detailDialog: false,  //明细弹窗
      addPatchDialog: false, //报损补片
      editPatchDialog: false, //编辑补片
      //明细信息
      detailsInfo: [
        {title: '补片流程卡号', value: ''},
      ],
      detailDialogData: [],  //明细数据
      //补片明细表头
      detailDialogColumns: [
        {label: '补片流程卡号', prop: 'patchFlowCardNo', width: '150'},
        {label: '流程卡号', prop: 'flowCardNo', width: '150'},
        {label: '客户名称', prop: 'businessInfo.customerName', width: '100'},
        {label: '项目名称', prop: 'businessInfo.entryName', width: '150'},
        {label: '产品名称', prop: 'businessInfo.productName', width: '120'},
        {label: '单片名称', prop: 'businessInfo.itemName', width: '120'},
        {label: '宽（mm）', prop: 'businessInfo.itemW', width: '100'},
        {label: '高（mm）', prop: 'businessInfo.itemH', width: '100'},
        {label: '报损数量（片）', prop: 'damageFlowCardInfo.damageNum', width: '120'},
        {label: '补片数量（片）', prop: 'patchNum', width: '120'},
        {label: '责任工序', prop: 'damageFlowCardInfo.responsibleProcess', width: '100'},
        {label: '责任班组', prop: 'damageFlowCardInfo.responsibleTeam', width: '100'},
        {label: '责任人', prop: 'damageFlowCardInfo.responsiblePerson', width: '100'},
        {label: '报损原因', prop: 'damageFlowCardInfo.damageReason', width: '100'},
        {label: '报损备注', prop: 'damageFlowCardInfo.remarks', width: '100'},
      ],
      //报损补片
      queryDamage: {
        damageNo: '',
      },
      damageList: [],  //报损单列表
      //报损表头
      damageColumns: [
        {label: '报损单号', prop: 'damageNo', type: 'ipt', width: '150'},
        {label: '报损数量（片）', prop: 'damageNum', type: 'ipt', width: '150'},
        {label: '补片数量（片）', prop: 'patchNum', type: 'ipt', width: '150'},
        {label: '补片状态', prop: 'status', width: '150'},
        {label: '面积（m²）', prop: 'damageArea', type: 'ipt', width: '150'},
        {label: '报损时间', prop: 'damageDate', width: '180'},
        {label: '报损人', prop: 'createdPerson', type: 'ipt', width: '100'},
        {label: '备注', prop: 'remarks', width: '150'},
      ],
      patchDetailCloumns:[
        {label: '订单编号', prop: 'business.orderNo', width: '150'},
        {label: '流程卡号', prop: 'business.flowCardNo', width: '150'},
        {label: '客户名称', prop: 'business.customerName', width: '150'},
        {label: '项目名称', prop: 'business.entryName', width: '150'},
        {label: '产品名称', prop: 'business.productName', width: '150'},
        {label: '单片名称', prop: 'business.itemName', width: '150'},
        {label: '规格', prop: 'specs', width: '150'},
        {label: '报损工序', prop: 'responsibleProcess', width: '150'},
        {label: '报损数量', prop: 'damageNum', width: '150'},
        {label: '报损面积', prop: 'damageArea', width: '150'},
        {label: '责任班组', prop: 'responsibleTeam', width: '150'},
        {label: '责任人', prop: 'responsiblePerson', width: '150'},
        {label: '报损原因', prop: 'damage_reason', width: '150'},
        {label: '备注', prop: 'remarks', width: '150'}
      ],
      pageSizeDamage: 20,
      pageNumDamage: 1,
      totalDamage: 0,
      selectedDamage: [],  //报损补片单复选框
      damageParams:{
        pageSize:20,
        pageNum:1,
        count:0,
        damageNo: ""
      },
      patchDialog:false,
      damagePatchList:[],
      selectedPatchList:[],
      detailForm:{}
    }
  },
  created() {
    this.handleQuery();
  },
  mounted() {
    // // this.keyupSubmit();
  },
  methods: {
    checkSelect(row){
      return row.status != '已补片'
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
      if (this.patchDateRange) {
        this.queryParams.patchDateStart = this.patchDateRange[0];
        this.queryParams.patchDateEnd = this.patchDateRange[1];
      }else {
        this.queryParams.patchDateStart='';
        this.queryParams.patchDateEnd='';
      }
      getPatchList({...this.queryParams}).then(res=>{
        this.patchList = res.data
        this.total = res.count
      })
    },
    savePatch(){
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
        this.addPatchDialog = false
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
    /* 补片单重置 */
    // resetQuery() {
    //   this.patchDateRange=[];
    //   this.queryParams={
    //     customerName: "",
    //       deliverPerson: "",
    //       id: "",
    //       orderId: "",
    //       orderNo: "",
    //       patchDateEnd: "",
    //       patchDateStart: "",
    //       patchMode: "",
    //       patchName: "",
    //       patchNo: "",
    //       pageNum: 1,
    //       pageSize: 20,
    //       productName: "",
    //       projectName: "",
    //       responsiblePerson: ""
    //   };
    //   this.handleQuery();
    // },
    /* 补片单分页器 */
    handleChange(size, num) {
      this.pageSize = size;
      this.pageNum = num;
      this.queryParams.pageNum = this.pageNum;
      this.queryParams.pageSize = this.pageSize;
      this.handleQuery();
    },
    /* 补片单翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 补片复选框 */
    handleSelectionChange(val) {
      this.selected = val;
    },
    /* 行点击事件 */
    handleRowClick(row, column, event) {
      this.$nextTick(()=>{
        this.$refs.multipleTable.toggleRowSelection(row, column)
      })
    },
    handleRowClick2(row, column, event) {
      this.$nextTick(()=>{
        this.$refs.multipleTable2.toggleRowSelection(row, column)
      })
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
        let {id} = this.selected[0]
        getPatchInfo(id).then(res=>{
          this.detailForm = res.data
          this.detailDialog = true;
        })
      }
    },
    /* 报损补片 */
    async handleAdd() {
      await getList(this.damageParams).then(res => {
        this.damageList = res.data.map(item=>{
          if(item.patchNum == item.damageNum){
            item.status = '已补片'
          }else if(item.patchNum > 0){
            item.status = '补片中'
          }else {
            item.status = '未补片'
          }
          return item
        })
        this.damageParams.count = res.count
      })
      this.addPatchDialog = true;
    },
    /* 编辑补片 */
    handleUpdate() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要修改的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行修改");
        return false
      }
      if (this.selected.length === 1) {
        // this.patchInfo = JSON.parse(JSON.stringify(this.selected[0]));
        this.editPatchDialog = true;
      }
    },
    /* 删除补片 */
    handleDelete() {
      if (this.selected.length == 0) {
        Message.warning("请选择一条要删除的数据");
        return;
      }
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delPatch(this.selected[0].id).then(res => {
          this.$message.success(res.msg)
          this.handleQuery();
        })
      }).catch(()=>{
        this.$message.info('已取消删除')
      })
    },
    /* 打印 */
    handlePrint() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要打印的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行打印");
        return false
      }
      if (this.selected.length === 1) {
        let {id} = this.selected[0]
        getPatchInfo(id).then(res=>{
          let data = {
            patchId:res.data.id,
            flowCardNo:res.data.patchBusinessList[0].patchFlowCardNo
          }
          const {href} = this.$router.resolve({
            path: '/print',
            query: {printId:"207b5cdd7a8a4d25aafd9f829c856fbe", ...data}
          })
          window.open(href, '_blank')
        })
      }

    },
    /* 报损补片搜索 */
    handleDamage() {
      getList(this.damageParams).then(res => {
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
        this.damageParams.count = res.count
      })
    },
    /* 报损补片单分页器 */
    handleChangeDamage(size, num) {
      this.damageParams.pageSize = size;
      this.damageParams.pageNum = num;
      this.handleDamage();
    },
    /* 补片单翻页后序号连贯 */
    getIndexDamage($index) {
      //  表格序号
      return (this.pageNumDamage - 1) * this.pageSizeDamage + $index + 1;
    },
    /* 补片复选框 */
    handleSelectionDamage(val) {
      this.selectedDamage = val;
    },
    selectPatcch(val){
      this.selectedPatchList = val;
    },
    /* 行点击事件 */
    handleRowDamage(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },
    /* 保存报损补片 */
    saveAdd() {
      if(this.selectedDamage.length <= 0){
        this.$message.warning("请选择要补片的报损数据")
        return false
      }
      let ids = this.selectedDamage.map(item=>{
        return item.id
      }).toString()
      getDamageInfo({ids}).then(res=>{
        this.damagePatchList = res.data.map(item=>{
          item.specs = `${item.business.itemW}*${item.business.itemH}`
          item.currentPatchNum = item.damageNum
          return item
        })
        this.patchDialog = true
      })
    },
    /* 保存编辑补片 */
    editPatch(){

    },
  }
}
</script>

<style lang="scss" scoped>
.patchTable {
  height: calc(100% - 100px);
}

::v-deep .detailDialog {
  .el-dialog {
    height: calc(60vh);
    padding: 0;

    .detailsInfo {
      height: 30px;
      justify-content: flex-start;

      p {
        min-width: 200px;
      }
    }

    .detailsTable {
      height: calc(100% - 50px);
    }

  }
}

::v-deep .addPatchDialog {
  .el-dialog {
    height: calc(60vh);

    .el-dialog__body {
      height: calc(100% - 90px);

      .damageTable {
        margin-top: 10px;
        height: calc(100% - 60px);
      }
    }
  }
}
</style>
