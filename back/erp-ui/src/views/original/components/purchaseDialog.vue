<template>
  <div class="app-container-padding">
    <el-dialog
      title="选择采购单"
      :visible.sync="selectPurchaseDialog"
      width="80%"
      class="dialog-style selectOriginalDialog"
      :close-on-click-modal="false"   :close-on-press-escape="false" :destroy-on-close="true" :before-close="cancelSelectPurchase">
      <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
        <el-form-item label="采购日期" class="daterange">
          <el-date-picker
            v-model="purchaseDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            @change="handleQuery">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="采购单号" prop="purchaseNo">
          <el-input
            v-model="queryParams.purchaseNo"
            placeholder="请输入采购单号"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <el-form-item label="供应商" prop="millName">
          <el-input
            v-model="queryParams.millName"
            placeholder="请输入供应商"
            @keyup.enter.native="handleQuery"
            clearable/>
        </el-form-item>
        <!--      <el-form-item>-->
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <!--      </el-form-item>-->
      </el-form>
      <slot-table class="rightTable">
        <el-table highlight-current-row
          :data="tableDate"
          stripe
          border
          height="100%"
          ref="templateTableRef" slot="table">
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
          <el-table-column
            label="操作"
            width="150" class-name="operation">
            <template slot-scope="scope">
              <el-button type="text" size="mini" icon="el-icon-s-order" @click="handleDetails(scope.row)">查看明细</el-button>
              <el-button type="text" size="mini"  @click="saveSelectPurchase(scope.row)">入库</el-button>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
    </el-dialog>
    <!--  查看明细弹窗  -->
    <el-dialog title='查看明细'
               :visible.sync="detailDialog" width="80%" class="dialog-style detailDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
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
  </div>
</template>

<script>

import SlotTable from "@/components/public/table/SlotTable";
import {Message} from "element-ui";
import {queryPurchaseData} from "@/api/original/originalPurchase";

export default {
  name: "purchaseDialog",
  components: {SlotTable},
  data() {
    return {
      queryParams: {
        purchaseDateEnd: '',
        purchaseDateStart: '',
        purchaseNo: '',
        millName: '',
        pageNum: 1,
        pageSize: 20,
      },
      // total:0,
      purchaseDateRange: [],
      selected: [],
      tableDate: [
        {originalName: '123',purchaseNo:'234'},
        {originalName: '123',purchaseNo:'234'},
        {originalName: '123',purchaseNo:'234'},
      ],
      tableColumn: [
        {label: '采购单号', prop: 'originalName', width: '120'},
        // {label: '自定义编号', prop: 'originalName', width: '180'},
        {label: '采购日期', prop: 'originalName', width: '100'},
        {label: '供应商', prop: 'originalName', width: '120'},
        {label: '总数量（片）', prop: 'originalName', width: '100'},
        {label: '总面积（m²）', prop: 'originalName', width: '100'},
        {label: '总金额（元）', prop: 'originalName', width: '100'},
        {label: '创建人', prop: 'originalName', width: '100'},
        {label: '创建时间', prop: 'originalName', width: '180'},
        // {label: '入库状态', prop: 'originalName', width: '120'},
        {label: '备注', prop: 'originalName', width: '100'},
      ],
      detailDialog: false,
      detailsInfo: [
        {title: '采购单号', value: ''},
        // {title: '自定义编号', value: ''},
        {title: '供应商', value: ''},
        {title: '总数量（片）', value: ''},
        {title: '总面积（m²）', value: ''},
        {title: '总金额（元）', value: ''},
        {title: '入库状态', value: ''},
        {title: '制单人', value: ''},
        {title: '制单时间', value: ''},
        {title: '备注', value: ''},
      ],  //明细
      detailDialogData: [],
      detailDialogColumns: [
        {label: '原片名称', prop: 'originalName', width: '120'},
        {label: '长度（mm）', prop: 'originalLong', width: '100'},
        {label: '宽度（mm）', prop: 'originalWidth', width: '100'},
        {label: '原片编号', prop: 'originalNo', width: '100'},
        {label: '颜色 / 膜系', prop: 'originalColor', width: '100'},
        {label: '厚度（mm）', prop: 'originalThick', width: '100'},
        {label: '厂家名称', prop: 'millName', width: '150'},

        {label: '原片类型', prop: 'originalTypeName', width: '100'},
        {label: '等级', prop: 'originalGrade', width: '100'},
        {label: '数量（片）', prop: 'num', width: '100'},
        {label: '单价（元/m²）', prop: 'price', width: '120'},
        {label: '面积（m²）', prop: 'area', width: '100'},
        {label: '金额（元）', prop: 'money', width: '100'},
      ],
    }
  },
  props: {
    selectPurchaseDialog: {
      type: Boolean,
      default: false,
    },
  },
  created() {
    // if(this.selectPurchaseDialog){
      this.handleQuery();
    // }

  },
   methods: {

    /* 查询采购单 */
    handleQuery() {
      if (this.purchaseDateRange) {
        this.queryParams.purchaseDateStart = this.purchaseDateRange[0];
        this.queryParams.purchaseDateEnd = this.purchaseDateRange[1];
      }
      queryPurchaseData(this.queryParams).then(res => {
        this.tableDate = res.data;
        console.log('123',this.tableDate)
        // this.total=res.count;
      })

    },
    /* 查看明细 */
    handleDetails(row) {
      console.log(row)

      this.detailDialog = true;
      // detailsView({deliverId: this.selected[0].id}).then(res => {
      //   this.detailsInfo[0].value = res.data.deliverNo
      //   this.detailsInfo[1].value = res.data.deliverPerson
      //   this.detailsInfo[2].value = res.data.customerName
      //   this.detailsInfo[3].value = res.data.deliverPhone
      //   this.detailsInfo[4].value = res.data.deliverAddress
      //   this.detailsInfo[5].value = res.data.purchaseDate
      //   this.detailsInfo[6].value = res.data.deliverCost
      //   if (res.data.deliverType == 0) {
      //     this.detailsInfo[7].value = "订单发货"
      //   } else if (res.data.deliverType == 1) {
      //     this.detailsInfo[7].value = "打包发货"
      //   }
      //   this.detailsInfo[8].value = res.data.deliverRemarks
      //   this.detailDialogData = res.data.deliverBusinessList;
      // })
      // }
    },
    // /*  勾选表格复选框*/
    // handleSelectionChange(val) {
    //   this.selected = val;
    // },
    // /* 单击表格行 */
    // handleRowClick(row, column, event) {
    //   this.$refs.templateTableRef.toggleRowSelection(row, column)
    // },
    /* 保存选择采购单弹窗 */
    saveSelectPurchase(row,val) {
      this.$emit('saveSelectPurchase', row, val);
    },
    cancelSelectPurchase() {
      this.$emit('cancelSelectPurchase');
    },
  }
}
</script>

<style lang="scss" scoped>
::v-deep .selectOriginalDialog {
  height: 100%;

  .el-dialog {
    height: calc(60vh);

    .el-dialog__body {
      height: calc(100% - 50px);
      .rightTable {
        height: calc(100% - 65px);
        margin-top: 10px;
        min-height: 150px;

        .table {
          height: 100%;
        }

        .page {
          display: none;
        }

      }
    }
  }
}

::v-deep .iptAndBtn {
  overflow: auto;

  .el-form-item {
    width: 200px;
    min-width: 200px;
    margin-right: 0;

    .el-form-item__content {
      width: 120px;
    }
  }

  .daterange {
    min-width: 320px;
    .el-range-editor {
      width: 240px;

      .el-range-input:last-of-type {
        margin-left: 5px;
      }
    }
  }
}

::v-deep .detailDialog {
  .el-dialog {
    height: calc(55vh);

    .detailsInfo {
      min-height: 90px;
      max-height: 200px;
      justify-content: flex-start;
    }
    .detailsTable{
      min-height: 150px;
    }
  }
}
</style>
