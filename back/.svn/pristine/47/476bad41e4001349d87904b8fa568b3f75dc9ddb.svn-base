<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="完工日期" class="daterange">
        <el-date-picker
          v-model="preparationDateRange"
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
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['system:completion:add']">新增完工
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:completion:del']">编辑完工
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:completion:edit']">删除完工
      </el-button>
      <el-button
        type="primary"
        size="mini"
        @click="handleExport" v-hasPermi="['system:completion:export']"><i class="iconfont icon-daochuwenjian"></i>导出
      </el-button>
    </div>
    <slot-table class="completionTable" @handleChange="handleChange" :pageSize="pageSize"
                   :pageNum="pageNum"
                   :total="total">
      <el-table highlight-current-row
        :data="completionList"
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
          v-for="(item,index) in completionColumn"
          :key="index"
          :label="item.label"
          show-overflow-tooltip>
          <el-table-column :prop="item.prop" show-overflow-tooltip min-width="100" :min-width="item.width">
            <template #header scoped-slot="scope">
              <!--可根据类型来显示为搜索框、下拉框等-->
              <el-select v-if="item.type=='select'" @change="selectChange" v-model="queryParams[item.prop]" placeholder="请选择" clearable
                         size="mini">
                <el-option v-if="item.prop == 'completionCraftName'" v-for="item2 in craftOptions" :label="item2.craftName" :value="item2.id" :key="item.id"></el-option>
                <el-option v-if="item.prop == 'completionShift'" v-for="item2 in workShiftOptions" :label="item2.value" :value="item2.value"  :key="item.id"></el-option>
                <el-option v-if="item.prop == 'completionTeam'" v-for="item2 in teamList" :label="item2.teamName" :value="item2.id"  :key="item.id"></el-option>
              </el-select>
              <el-input
                v-if="item.type == 'input'"
                v-model="queryParams[item.prop]"
                size="mini"
                placeholder="请输入"
                @keyup.enter.native="selectChange"
                clearable/>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </slot-table>
  </div>
</template>

<script>
import slotTable from "@/components/public/table/SlotTable";
import {keepThreeNum} from "@/utils/order/order";
import {getList,delCompletion} from "@/api/product/completion"
import {Message} from "element-ui";
import {getAllTeam} from "@/api/system/team"
import {getCraftList} from "@/api/system/craft";

export default {
  name: "index",
  components: {slotTable},
  data() {
    return {
      //查询参数
      queryParams: {
        pageSize: 20,
        pageNum: 1,
        startDate: '',
        endDate: '',
      },
      preparationDateRange: [], //完工日期范围
      completionList: [],   //完工数据
      workShiftOptions: [{
        value: '早班',
        label: '早班'
      }, {
        value: '中班',
        label: '中班'
      }, {
        value: '晚班',
        label: '晚班'
      }
      ],
      craftOptions: [],
      //流程卡表头
      completionColumn: [
        {label: '完工单号', prop: 'completionNo',type: 'input', width: '150'},
        {label: '流程卡号', prop: 'flowCardNo',type: 'input', width: '150'},
        {label: '工序', prop: 'completionCraftName', type: 'select', width: '100'},
        {label: '客户名称', prop: 'customerName', width: '120'},
        {label: '订单编号', prop: 'orderNo', width: '140'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '班次', prop: 'completionShift', type: 'select',width: '100'},
        {label: '产品名称', prop: 'productName', width: '150'},
        {label: '单片名称', prop: 'monolithicName', width: '150'},
        {label: '数量（片）', prop: 'completionNum', width: '120'},
        {label: '班组', prop: 'completionTeam', type: 'select',width: '100'},
        {label: '制单人', prop: 'createdPerson',width: '100'},
        {label: '完工日期', prop: 'completionDate',width: '120'},
        {label: '备注', prop: 'remarks'},
      ],
      pageSize: 20,
      pageNum: 1,
      total: 0,
      selected: [],  //复选框选中
      teamList:[]
    }
  },
  async created() {
    await this.getTeam();
    await this.getCraftList();
    await this.handleQuery();
  },
  mounted() {
  },
  //设置表格表体高度自适应
  updated() {
    this.$nextTick(() => {
      this.$refs.multipleTable.doLayout();
    })
  },
  methods: {
    selectChange(){
      this.handleQuery();
    },
    getCraftList() {
      getCraftList().then(res => {
        console.log(res)
        if (res.code === 200) {
          this.craftOptions = res.data
        }
      })
    },
    getTeam(){
      getAllTeam().then(res=>{
        this.teamList = res.data
      })
    },
    /* 条件查询 */
    handleQuery() {
      if (this.preparationDateRange) {
        this.queryParams.startDate = this.preparationDateRange[0];
        this.queryParams.endDate = this.preparationDateRange[1];
      }else{
        delete this.queryParams.startDate
        delete this.queryParams.endDate
      }
      let {startDate,endDate,...data} = this.queryParams
      getList({...data,beginDate:startDate,endDate:endDate}).then(res=>{
        this.completionList = res.data.map(item=>{
          let customer = new Set();
          let order = new Set();
          let customNo = new Set();
          let monolithicName = new Set();
          let productName = new Set();
          item.completionBusinessList.forEach(val => {
            customer.add(val.flowCard.customerName)
            order.add(val.flowCard.orderNo)
            customNo.add(val.flowCard.customNo)
            monolithicName.add(val.flowCard.monolithicName)
            productName.add(val.flowCard.productName)
          })
          item.customerName = Array.from(customer).toString()
          item.orderNo = Array.from(order).toString()
          item.customNo = Array.from(customNo).toString()
          item.monolithicName = Array.from(monolithicName).toString()
          item.productName = Array.from(productName).toString()
          let team = this.teamList.filter(filt => {
            return filt.id == item.completionTeam
          })
          item.completionTeam = team.length > 0 ?team[0].teamName:""
          return item;
        })
        this.total = res.count
      })
    },
    /* 重置 */
    resetQuery() {
      this.preparationDateRange = [];   //制单日期范围
      this.queryParams = {
        pageSize: 20,
        pageNum: 1,
        startDate: '',
        endDate: '',
      };
      this.handleQuery();
    },
    /* 新增完工 */
    handleAdd() {
      this.$router.push('/production/completion/addCompletion');
      // this.$router.push('/production/addCompletion');
    },
    /* 编辑完工 */
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
        this.$router.push({path:'/production/completion/editCompletion',query:{id:this.selected[0].id}});
      }
    },
    /* 删除完工 */
    handleDelete() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要删除的数据");
      }else if (this.selected.length > 1) {
        Message.warning("请选择一条要删除的数据");
      } else {
        this.$confirm('此操作将删除选中数据，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          let ids = this.selected.map(item => {
            return item.id
          }).join(",");
          delCompletion(ids).then(res => {
            Message.success(res.msg);
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
    /* 导出完工 */
    handleExport() {
      let ids = this.selected.map(item=>{
        return item.id
      })
      this.download('produce/completion/export', {
        ids: ids.toString()
      }, `完工数据.xlsx`)
    },
    /* 翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 完工单复选框 */
    handleSelectionChange(val) {
      this.selected = val;
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.toggleRowSelection(row, column)
    },

    /* 分页器 */
    handleChange(size, num) {
      this.pageSize = size;
      this.pageNum = num;
      this.queryParams.pageNum = this.pageNum;
      this.queryParams.pageSize = this.pageSize;
      this.handleQuery();
    },
  },
}
</script>

<style lang="scss" scoped>
.completionTable {
  height: calc(100% - 100px);
  //overflow: hidden;
}


</style>
