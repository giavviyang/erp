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
      <el-form-item label="收款状态">
        <el-select v-model="queryParams.collectionStatus" placeholder="请选择">
          <el-option
            v-for="item in collectionStatus"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="addCollection"
        v-hasPermi="['sales:order:selectOrder']">新增收款
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-reading"
        size="mini"
        @click="toDetail"
        v-hasPermi="['sales:order:selectOrder']">查看收款记录
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-reading"
        size="mini"
        @click="selectOrder"
        v-hasPermi="['sales:order:selectOrder']">查看订单明细
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-reading"
        size="mini"
        @click="toDevliber"
        v-hasPermi="['sales:order:selectOrder']">查看发货明细
      </el-button>
    </div>
    <count-table class="rightTable orderTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total" :summation="summation">
      <el-table highlight-current-row
                :data="orderList"
                border
                style="width: 100%"
                height="100%"
                ref="collectionTable"
                :row-class-name="tableRowClassName"
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
          v-for="item in orderListColumn"
          :key="item.prop"
          :label="item.label"
          show-overflow-tooltip>
          <el-table-column :prop="item.prop" show-overflow-tooltip :min-width="item.width">
            <template #header scoped-slot="scope">
              <!--可根据类型来显示为搜索框、下拉框等-->
              <el-input
                v-if="item.type=='ipt'"
                v-model="queryParams[item.prop]"
                size="mini"
                placeholder="请输入"
                clearable @keyup.enter.native="handleQuery"/>
              <el-select v-if="item.type=='select'" v-model="queryParams[item.prop]" placeholder='请选择' clearable
                         size="mini" ref='statusSelect' @change="handleQuery">
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
    <el-dialog :title="dialog == 'add' ? '新增收款':'编辑收款'" :visible.sync="dialog" width="900px" class="dialog-style" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-descriptions :column="3" border>
        <el-descriptions-item>
          <template slot="label">
            订单编号
          </template>
          {{ selected[0]?.orderNo }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            客户名称
          </template>
          {{selected[0]?.customerName || "无"}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            结算方式
          </template>
          {{ selected[0]?.settlementMethod || "无" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            定金
          </template>
          {{ selected[0]?.orderDeposit || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            订单总金额
          </template>
          {{ selected[0]?.orderAmount || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            已收款金额
          </template>
          <span style="color: red;"> {{ selected[0]?.receivedAmount || 0 }}</span>元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            已优惠金额
          </template>
          {{ selected[0]?.discountAmount || 0 }}元
        </el-descriptions-item>
<!--        <el-descriptions-item>-->
<!--          <template slot="label">-->
<!--            附加费用-->
<!--          </template>-->
<!--          {{ selected[0]?.additionalCost || 0 }}元-->
<!--        </el-descriptions-item>-->
        <el-descriptions-item>
          <template slot="label">
            收款人
          </template>
          <div style="width: 150px">{{ collectionForm.collectionPerson }}</div>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            收款日期
          </template>
          <el-date-picker
            v-model="collectionForm.collectionDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 150px"
            placeholder="请选择收款日期">
          </el-date-picker>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            收款金额
          </template>
          <el-input type="number" style="width: 150px" v-model="collectionForm.collectionAmount" placeholder="请输入收款方式">
            <template slot="append">元</template>
          </el-input>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            优惠金额
          </template>
          <el-input type="number" style="width: 150px" v-model="collectionForm.preferentialAmount" placeholder="请输入收款方式">
            <template slot="append">元</template>
          </el-input>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            收款方式
          </template>
          <el-select v-model="collectionForm.collectionMode" style="width: 150px" placeholder="请选择收款方式">
            <el-option
              v-for="item in dict.type.fkfs"
              :key="JSON.stringify(item)"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            备注
          </template>
          <el-input style="width: 733px" v-model="collectionForm.remarks" placeholder="请输入备注"></el-input>
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="save">保存</el-button>
        <el-button size="mini" @click="dialog = false">取消</el-button>
      </span>
    </el-dialog>
    <el-dialog title="查看明细" :visible.sync="detailDialog" width="900px" class="dialog-style" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-descriptions :column="3" border>
        <el-descriptions-item>
          <template slot="label">
            订单编号
          </template>
          {{ selected[0]?.orderNo }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            客户名称
          </template>
          {{ selected[0]?.customerName || "无" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            结算方式
          </template>
          {{ selected[0]?.settlementMethod || "无"}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            定金
          </template>
          {{ selected[0]?.orderDeposit || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            加急费用
          </template>
          {{ selected[0]?.urgentCost || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            附加费用
          </template>
          {{ selected[0]?.additionalCost || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            订单总金额
          </template>
          {{ selected[0]?.orderAmount || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            已收款金额
          </template>
          <span style="color: red;"> {{ selected[0]?.receivedAmount || 0 }}</span>元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            已优惠金额
          </template>
          <span style="color: red;"> {{ selected[0]?.discountAmount || 0 }}</span>元
        </el-descriptions-item>
      </el-descriptions>
      <slot-table :is-page="false" style="margin-top: 10px;max-height: 500px">
        <el-table highlight-current-row
                  :data="collectionList"
                  stripe
                  border
                  style="width: 100%"
                  height="100%"
                  ref="collectionTable2"
                  slot="table">
          <el-table-column
            type="index"
            label="序号"
            width="55" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            v-for="item in collectionListColumn"
            :prop="item.prop"
            :label="item.label"
            show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button @click="editCollection(scope.row)" type="text" size="small">编辑</el-button>
              <el-button type="text" size="small" @click="delCollection(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
    </el-dialog>
    <el-dialog title="查看发货明细" :visible.sync="deliverDialog" width="1100px" class="dialog-style" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-descriptions :column="3" border>
        <el-descriptions-item>
          <template slot="label">
            订单编号
          </template>
          {{ selected[0]?.orderNo }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            客户名称
          </template>
          {{ selected[0]?.customerName || "无" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            结算方式
          </template>
          {{ selected[0]?.settlementMethod || "无"}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            定金
          </template>
          {{ selected[0]?.orderDeposit || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            加急费用
          </template>
          {{ selected[0]?.urgentCost || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            附加费用
          </template>
          {{ selected[0]?.additionalCost || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            订单总金额
          </template>
          {{ selected[0]?.orderAmount || 0 }}元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            已收款金额
          </template>
          <span style="color: red;"> {{ selected[0]?.receivedAmount || 0 }}</span>元
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            已优惠金额
          </template>
          <span style="color: red;"> {{ selected[0]?.discountAmount || 0 }}</span>元
        </el-descriptions-item>
      </el-descriptions>
      <slot-table :is-page="false" style="margin-top: 10px;height: 300px">
        <el-table highlight-current-row
                  :data="deliverList"
                  stripe
                  border
                  style="width: 100%"
                  height="100%"
                  ref="collectionTable2"
                  :span-method="processSpanMethod"
                  slot="table">
          <el-table-column
            type="index"
            label="序号"
            width="55" show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            v-for="item in deliverListColumn"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"
            show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </slot-table>
    </el-dialog>
  </div>
</template>

<script>
import slotTable from "@/components/public/table/SlotTable";
import {orderList, list, add, edit, del} from "@/api/salse/collection";
import {getCurrentDay} from "@/utils/order/order";
import CountTable from "@/components/public/table/countTable";
import {queryDeliverBus} from "@/api/salse/deliver"
import {keepOneNum} from "@/utils/order/order";

export default {
  dicts: ['fkfs'],
  components: {CountTable, slotTable},
  data() {
    return {
      summation: [
        {label: 'totalAmount', title: '总金额', value: 0, unit: '元'},
        {label: 'orderDeposit', title: '总定金', value: 0, unit: '元'},
        {label: 'receivedPayment', title: '总收款', value: 0, unit: '元'},
        {label: 'discountAmount', title: '总优惠', value: 0, unit: '元'},
        {label: 'outstandingCollection', title: '总应收', value: 0, unit: '元'}
      ],
      // 查询参数
      preparationDateRange: [],  //日期范围
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        collectionStatus: 1,
        contactNumber: '',
        contacts: '',
        customNo: '',
        customerName: '',
        entryName: '',
        id: '',
        isCache: 0,
        orderNo: '',
        orderType: '',
        packagingStatus: '',
        preparationDateEnd: '',
        preparationDateStart: '',
        preparer: '',
        productionStatus: '',
        rackSplittingStatus: '',
        shipmentStatus: '',       // createTime: undefined,
        isDel: 0,
      },
      queryDetail: {
        productName: "",
        highHead: "",
        id: "",
        orderId: "",
        position: "",
        remarks: "",
        requirement: "",
        wideHead: ""
      },
      collectionStatus:[
        {
          label:"全部",
          value:0
        },
        {
          label:"未完成收款",
          value: 1
        },
        {
          label:"已完成收款",
          value:2
        }
      ],
      orderList: [],  //表格数据
      orderListColumn: [
        {label: '订单编号', prop: 'orderNo', type: 'ipt', width: '140'},
        {label: '订单类型', prop: 'orderType', type: 'select', width: '80'},
        {label: '自定义编号', prop: 'customNo', type: 'ipt', width: '120'},
        {label: '客户名称', prop: 'customerName', type: 'ipt', width: '100'},
        {label: '项目名称', prop: 'entryName', type: 'ipt', width: '180'},
        {label: '总金额（元）', prop: 'orderAmount', width: '110'},
        {label: '待收款金额（元）', prop: 'outstandingCollection', width: '120'},
        {label: '定金（元）', prop: 'orderDeposit', width: '110'},
        {label: '已收款金额（元）', prop: 'receivedAmount', width: '120'},
        {label: '优惠总金额（元）', prop: 'discountAmount', width: '120'},
        // {label: '订单备注', prop: 'orderRemarks', width: '180'},
        // {label: '生产备注', prop: 'remarks', width: '180'},
      ],
      pageSize: 20,
      pageNum: 1,
      total: 0,
      collectionForm: {
        orderId: '',
        collectionCustomerName: '',
        collectionPerson: localStorage.getItem("nickName"),
        collectionDate: getCurrentDay(),
        collectionMode: "现金收款",
        collectionAmount: 0,
        preferentialAmount: 0,
        remarks: ""
      },
      collectionRules: [],
      dialog: false,
      selected: [],
      detailDialog: false,
      collectionListColumn: [
        {label: '收款人', prop: 'collectionPerson'},
        {label: '收款金额', prop: 'collectionAmount'},
        {label: '优惠金额', prop: 'preferentialAmount'},
        {label: '收款方式', prop: 'collectionMode'},
        {label: '收款日期', prop: 'collectionDate'},
        {label: '备注', prop: 'remarks'}
      ],
      collectionList: [],
      dialogType: 'add',
      deliverDialog:false,
      deliverListColumn:[
        {label: '发货编号', prop: 'deliverNo', type: 'ipt', width: '120'},
        {label: '发货负责人', prop: 'deliverPeople', type: 'ipt', width: '120'},
        {label: '发货日期', prop: 'deliverDate', type: 'ipt', width: '200'},
        {label: '产品名称', prop: 'productName', type: 'date', width: '200'},
        {label: '宽', prop: 'wideHead', type: 'ipt'},
        {label: '高', prop: 'highHead'},
        {label: '产品数量（片）', prop: 'num', width: '200'},
        {label: '发货数量（片）', prop: 'deliverNum', width: '120'},
        {label: '发货单价（元）', prop: 'deliverPrice', width: '120'},
        {label: '发货金额（元）', prop: 'deliverAmount', width: '120'},
      ],
      deliverList:[],
      /* 合并类 */
      spanArr: [], // 一个空的数组，用于存放每一行记录的合并数
      pos: 0, // spanArr 的索引
    }
  },
  created() {
    this.handleQuery();
  },
  methods: {
    selectOrder() {
      if (this.selected.length !== 1) {
        this.$message({
          message: "请选择一条要查看的数据",
          type: 'warning'
        });
        return;
      }
      this.$router.push('/sales/collection/selectOrder/' + this.selected[0].id);
    },
    toDetail() {
      if (this.selected.length != 1) {
        this.$message.warning("请选择一条订单进行查看");
        return false;
      }
      list({id : this.selected[0].id}).then(res => {
        if (res.code === 200) {
          this.collectionList = res.data;
          this.detailDialog = true;
        }else {
          this.$message.error("查询失败！")
        }
      })
    },
    toDevliber(){
      if (this.selected.length != 1) {
        this.$message.warning("请选择一条订单进行查看");
        return false;
      }
      queryDeliverBus({id: this.selected[0].id}).then(res => {
        this.deliverList = res.data;
        // 设定一个数组spanArr/contentSpanArr来存放要合并的格数，同时还要设定一个变量pos/position来记录
        this.spanArr = [];
        for (let i = 0; i < this.deliverList.length; i++) {
          if (i === 0) {
            this.spanArr.push(1);
            this.pos = 0;
          } else {
            // 判断当前元素与上一个元素是否相同(第1和第2列)
            if (this.deliverList[i].deliverNo === this.deliverList[i - 1].deliverNo) {
              this.spanArr[this.pos] += 1;
              this.spanArr.push(0);
            } else {
              this.spanArr.push(1);
              this.pos = i;
            }
          }
        }
      })
      this.deliverDialog = true;
    },
    addCollection() {
      if (this.selected.length != 1) {
        this.$message.warning("请选择一条订单进行新增收款");
        return false;
      }
      if (this.selected[0].receivedAmount + this.selected[0].discountAmount >= this.selected[0].orderAmount) {
        this.$message.warning("订单已完成收款！");
        return false;
      }
      this.collectionForm = {
        orderId: this.selected[0].id,
        collectionCustomerName:this.selected[0].customerName,
        collectionPerson: localStorage.getItem("nickName"),
        collectionDate: getCurrentDay(),
        collectionMode: "现金收款",
        collectionAmount: 0,
        preferentialAmount: 0,
        remarks: ""
      }
      this.dialogType = 'add'
      this.dialog = true
    },
    editCollection(row) {
      this.collectionForm = row
      this.dialogType = 'edit'
      this.dialog = true
    },
    delCollection(row) {
      this.$confirm('确定删除收款记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(row).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            // this.handleQuery();
            // list({id : this.selected[0].id}).then(res => {
            //   this.collectionList = res.data;
            // })
            this.detailDialog = false;
            this.handleQuery();
          }else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {});


    },
    /* 各种查询条件查询 */
    handleQuery() {
      this.handleOrderList();
    },
    /* 一级、二级表单同时查询 */
    handleOrderList() {
      // console.log('订单')
      if (this.preparationDateRange) {
        this.queryParams.preparationDateStart = this.preparationDateRange[0];
        this.queryParams.preparationDateEnd = this.preparationDateRange[1];
      } else {
        this.queryParams.preparationDateStart = '';
        this.queryParams.preparationDateEnd = '';
      }
      orderList(this.queryParams).then(response => {
        this.summation = [
          {label: 'totalAmount', title: '总金额', value: 0, unit: '元'},
          {label: 'orderDeposit', title: '总定金', value: 0, unit: '元'},
          {label: 'receivedPayment', title: '总收款', value: 0, unit: '元'},
          {label: 'discountAmount', title: '总优惠', value: 0, unit: '元'},
          {label: 'outstandingCollection', title: '总应收', value: 0, unit: '元'}
        ]
        // console.log('response', response)
        if (response.code === 200) {
          this.orderList = response.data.map(item=>{
            item.outstandingCollection = Number(item.orderAmount) - Number(item.orderDeposit) - Number(item.discountAmount) - Number(item.receivedAmount);
            this.summation[0].value += Number(item.orderAmount);
            this.summation[1].value += Number(item.orderDeposit);
            this.summation[2].value += Number(item.receivedAmount);
            this.summation[3].value += Number(item.discountAmount);
            this.summation[4].value += Number(item.outstandingCollection);
            return item
          });
          this.summation.forEach(item => {
            item.value = keepOneNum(item.value);
          })
          // JSON.parse(JSON.stringify(this.sizeCheckColumn.slice(0)));
          this.total = response.count;

        }
      })
    },
    //新增保存
    save() {
      if (this.dialogType === 'add') {
        add(this.collectionForm).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.dialog = false;
            this.handleQuery();
          }else {
            this.$message.error(res.msg)
          }
        })
      }else {
        edit(this.collectionForm).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.dialog = false;
            this.detailDialog = false;
            this.handleQuery();
          }else {
            this.$message.error(res.msg)
          }
        })
      }
    },
    /* 重置 */
    resetQuery() {
      this.preparationDateRange = [];
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        collectionStatus: '',
        contactNumber: '',
        contacts: '',
        customNo: '',
        customerName: '',
        entryName: '',
        id: '',
        isCache: 0,
        orderNo: '',
        orderType: '',
        packagingStatus: '',
        preparationDateEnd: '',
        preparationDateStart: '',
        preparer: '',
        productionStatus: '',
        rackSplittingStatus: '',
        shipmentStatus: '',       // createTime: undefined,
        isDel: 0,
      };
    },
    //改变行颜色
    tableRowClassName({row, rowIndex}) {
      if (row.receivedAmount + row.discountAmount >= row.orderAmount) {
        return 'green-row';
      }
      return '';
    },
    /* 选中数据 */
    handleSelectionChange(val) {
      // 清除 所有勾选项
      if (val.length === 1) {
        this.selected = val;
        this.queryDetail.orderId = val[0].id;
        this.queryDetail.orderNo = this.selected[0].orderNo;
      }
      if (val.length > 1) {
        this.$refs.collectionTable.clearSelection();
        this.$refs.collectionTable.toggleRowSelection(val.at(-1), true);
        this.selected = val.slice(-1);
      }
      if (val.length === 0) {
        this.selected = [];
      }
    },
    /* 行点击事件 */
    handleRowClick(row, column, event) {
      this.$refs.collectionTable.toggleRowSelection(row, column)
      // console.log( row,column,event)
    },
    /* 翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 分页器 */
    handleChange(pageSize, pageNum) {
      // console.log(pageSize, pageNum)
      this.pageSize = pageSize;
      this.pageNum = pageNum;
      this.queryParams.pageSize = this.pageSize;
      this.queryParams.pageNum = this.pageNum;
      this.handleQuery();
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
  }
}
</script>

<style lang="scss" scoped>
::v-deep .green-row {
  background-color: #9ae375 !important;
}
::v-deep .orderTable{
  height: calc(100% - 105px);
}
.rightTable {
  height: calc(100% - 105px);
}
</style>
