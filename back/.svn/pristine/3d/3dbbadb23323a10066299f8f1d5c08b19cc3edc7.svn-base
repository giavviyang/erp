<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="发货日期">
        <el-date-picker
          v-model="preparationDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"  @change="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-s-order"
        size="mini"
        @click="handleDetails"
        v-hasPermi="['system:role:add']">查看明细
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:role:edit']">删除发货单
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleRestore">还原发货单
      </el-button>
    </div>
    <slot-table class="recycleTable" @handleChange="handleChange" :pageSize="pageSize"
                :pageNum="pageNum"
                :total="total">
      <el-table highlight-current-row
        :data="recycleList"
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
          v-for="(item,index) in recycleColumns"
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
                size="mini"
                placeholder="请输入"
                clearable @keyup.enter.native="handleQuery"/>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </slot-table>
    <!--  查看明细弹窗  -->
    <el-dialog title="查看明细" :visible.sync="detailDialog" width="60%" class="dialog-style detailDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <div class="detailsInfo">
        <p v-for="(item,index) in detailsInfo" :key="index">{{ item.title }}：<span>{{ item.value }}</span></p>
      </div>
      <slot-table class="detailsTable">
        <el-table highlight-current-row
          :data="detailDialogData"
          border
          stripe
          class="sizeCheckTable"
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
  </div>
</template>

<script>

import SlotTable from "@/components/public/table/SlotTable";
import {Message} from "element-ui";
export default {
  name: "index",
  components: {SlotTable},
  data() {
    return {
      //查询参数
      queryParams: {
        pageSize: 20,
        pageNum: 1,
        preparationDateEnd: '',
        preparationDateStart: '',
      },
      preparationDateRange: [], //制单日期范围
      recycleList: [
        {orderNo:'123'}
      ],
      //流程卡表头
      recycleColumns: [
        {label: '订单编号', prop: 'orderNo', width: '120'},
        {label: '自定义编号', prop: 'customNo', width: '120'},
        {label: '客户名称', prop: 'customerName',  width: '200'},
        {label: '项目名称', prop: 'entryName',  width: '200'},
        {label: '作废人', prop: 'customerName',  width: '200'},
        {label: '发货日期', prop: 'customerName',  width: '200'},
        {label: '发货方式', prop: 'preparationDate', type: 'date', width: '200'},
        {label: '发货编号', prop: 'contacts',width:'200' },
        {label: '发货负责人', prop: 'contactNumber',  width: '150'},
        {label: '发货地址', prop: 'contactNumber',  width: '150'},
        {label: '发货数量（片）', prop: 'contactNumber',  width: '150'},
        {label: '发货面积（m²）', prop: 'contactNumber',  width: '150'},
        {label: '发货费用', prop: 'contactNumber',  width: '150'},
        {label: '回单状态', prop: 'reviewedBy'},
        {label: '备注', prop: 'reviewedBy'},
      ],
      orderDetailsColumn: [
        {label: '产品名称', prop: 'productName', width: '300'},
        {label: '位置', prop: 'position', width: '100'},
        {label: '宽（mm）', prop: 'wideHead', width: '110'},
        {label: '高（mm）', prop: 'highHead', width: '110'},
        {label: '数量（片）', prop: 'num', width: '100'},
        {label: '总周长（mm）', prop: 'lengthen', width: '150'},
        {label: '计费方式', prop: 'jifei', width: '150'},
        {label: '单价（元/m²）', prop: 'unitPrice', width: '100'},
        {label: '面积（m²）', prop: 'productArea', width: '100'},
        {label: '重量（t）', prop: 'productWeight', width: '100'},
        {label: '特殊工艺', prop: 'teshu'},
        {label: '总金额（元）', prop: 'productAmount', width: '100'},
        {label: '加工要求', prop: 'requirement'},
        {label: '备注', prop: 'remarks'},
      ],
      pageSize: 20,
      pageNum: 1,
      total: 0,
      selected: [],
      detailDialog:false,
      //明细信息
      detailsInfo: [
        {title: '订单编号', value: ''},
        {title: '自定义编号', value: ''},
        {title: '订单类型', value: ''},
        {title: '总数量（片）', value: ''},
        {title: '总面积（m²）', value: ''},
        {title: '总金额（元）', value: ''},
        {title: '项目名称', value: ''},
        {title: '客户名称', value: ''},
        {title: '联系电话', value: ''},
        {title: '制单人', value: ''},
        {title: '制单时间', value: ''},
        {title: '备注', value: ''},
      ],
      detailDialogData: [
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
        {orderNo:'123'},
      ],  //回收站明细数据
      //订单明细表头
      detailDialogColumns: [
        {label: '产品名称', prop: 'orderNo',},
        {label: '宽（mm）', prop: 'itemW', width: '110'},
        {label: '高（mm）', prop: 'itemH', width: '110'},
        {label: '数量（片）', prop: 'itemNum', width: '100'},
        {label: '单价（元/m²）', prop: 'customNo', width: '120'},
        {label: '位置', prop: 'position', },
        {label: '加工要求', prop: 'requirement',},
        {label: '总面积（m²）', prop: 'totalArea', width: '120'},
        {label: '总重量（t）', prop: 'totalWeight', width: '100'},
        {label: '总价（元）', prop: 'entryName', width: '100'},
        {label: '备注', prop: 'remarks', },
      ],
    }
  },
  created() {
    this.handleQuery();
  },
  mounted() {
    // this.keyupSubmit();
  },
   methods: {

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
      if (this.preparationDateRange) {
        this.queryParams.preparationDateStart = this.preparationDateRange[0];
        this.queryParams.preparationDateEnd = this.preparationDateRange[1];
      }
      // queryrecycleList(this.queryParams).then(res => {
      //   console.log(res)
      //   if (res.code === 200) {
      //     this.recycleList = res.data;
      //     this.total = res.count;
      //     if (res.data) {
      //       this.currentId = res.data[0].id;
      //       this.getcompletionFile();
      //     }
      //   }
      // })
    },
    /* 查看订单明细 */
    handleDetails() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要查看的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行查看");
        return false
      }
      if (this.selected.length == 1) {
        this.detailDialog=true;
      }
    },
    /* 删除订单 */
    handleDelete() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要删除的数据");
        return false
      }
      if (this.selected.length == 1) {
        this.$confirm('此操作将永久删除选中数据，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          // delOrder({orderId: this.selected[0].id}).then(res => {
          //   if (res.code === 200) {
          //     this.$message({
          //       type: 'success',
          //       message: res.msg
          //     });
          //     this.handleQuery();
          //   } else {
          //     this.$message({
          //       type: 'error',
          //       message: res.msg
          //     });
          //   }
          // })
          this.selected = [];
          this.$refs.multipleTable.clearSelection();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    /* 还原订单 */
    handleRestore() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要还原的数据");
        return false
      }
      if (this.selected.length == 1) {
        this.$confirm('此操作将还原选中数据,还原后的数据将可以在订单管理中查看，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          // delOrder({orderId: this.selected[0].id}).then(res => {
          //   if (res.code === 200) {
          //     this.$message({
          //       type: 'success',
          //       message: res.msg
          //     });
          //     this.handleQuery();
          //   } else {
          //     this.$message({
          //       type: 'error',
          //       message: res.msg
          //     });
          //   }
          // })

          this.$refs.multipleTable.clearSelection();
          this.selected = [];
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消还原'
          });
        });
      }
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
    /* 行点击事件 */
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
  }
}
</script>

<style lang="scss" scoped>
.recycleTable {
  height: calc(100% - 100px);
  //overflow: hidden;
}
::v-deep .detailDialog{
  .el-dialog{
    .el-dialog__body{
      .detailsInfo {
        height: 120px;
        p {
          min-width: 300px;
        }
      }
    }
  }
}

</style>
