<template>
  <div class="chartsBox">
    <el-table
      class="tableBox"
      :data="list"
      border
      style="width: 100%;"
      height="100%"
      :header-cell-style="{color:'#a2b5c7'}">
      <el-table-column
        v-for="item in tableCloumn"
        :prop="item.prop"
        :label="item.label"
        :resizable="false"
        :width="item.width"
        show-overflow-tooltip>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "Table",
  props:{
    list:Array
  },
  data() {
    return {
      tableCloumn: [
        {prop: "flow_card_no", label: "流程卡号",width:"100"},
        {prop: "entry_name", label: "项目名称",width:"100"},
        {prop: "custom_no", label: "自编号",width:"100"},
        {prop: "product_name", label: "产品名称",width:"100"},
        {prop: "monolithic_name", label: "单片名称",width:"100"},
        {prop: "total_area", label: "总面积(m²)",width:"100"},
        {prop: "complete_process", label: "完成工序",width:"200"},
      ]
    }
  }
}
</script>
<style lang="scss" scoped>
.chartsBox {
  width: 100%;
  height: 100%;
  background-color: #032c4a !important;

  ::v-deep .el-table--scrollable-x .el-table__body-wrapper {
    overflow-x: auto !important;
  }

  ::v-deep .el-table::before, .el-table--group::after, .el-table--border::after{
    background-color: #032c4a !important;
  }

  ::v-deep .tableBox {
    background-color: #032c4a !important;
    border-color: #0171ba !important;

    th {
      padding: 0 !important;
      height: 20px;
      line-height: 20px;
      background-color: #032c4a !important;
      border-color: #0171ba !important;
      font-size: 12px !important;
    }

    td {
      padding: 0 !important;
      height: 20px;
      line-height: 20px;
      background-color: #032c4a !important;
      border-color: #0171ba !important;
      font-size: 12px !important;
      color: #05cff6 !important;
    }
  }
}
</style>
