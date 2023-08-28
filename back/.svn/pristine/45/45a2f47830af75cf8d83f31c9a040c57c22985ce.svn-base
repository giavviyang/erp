<template>
  <AppContent>
    <el-tabs v-model="activeName" style="height: 100%" type="border-card" @tab-click="handleClick">
      <el-tab-pane label="编号规则" name="number">
        <NumberRule/>
      </el-tab-pane>
      <el-tab-pane label="二维码规则" name="code" style="height: calc(100% - 70px);">
        <QRCode/>
      </el-tab-pane>
    </el-tabs>
  </AppContent>
</template>

<script setup>
import AppContent from "@/components/AppContent/index";
import NumberRule from "@/views/system/number/number";
import QRCode from "@/views/system/number/qrcode";
import {ref} from "vue"

/** data */
const activeName = ref("number");
/** methods */
const handleClick = (e)=>{
  activeName.value = e.name
}
</script>
<style lang="scss" scoped>
::v-deep .el-tabs__content , .el-tab-pane{
  height: calc(100% - 20px);
  transition: 1s;
}
::v-deep textarea{
  font-family: Arial;
}
//::v-deep .el-input.is-disabled .el-input__inner{
//  color: #333;
//}
::v-deep .tableContainer .table .el-table .el-table__body-wrapper .el-table__row td:last-child .cell{
  flex-direction: row;
}
::v-deep .el-card__body{
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
</style>
