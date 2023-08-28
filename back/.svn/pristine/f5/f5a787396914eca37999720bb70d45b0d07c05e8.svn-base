<template>
  <el-dialog
    title="选择铁架"
    :visible.sync="selectShelfDialog"
    width="70%"
    class="dialog-style selectShelfDialog"
    :close-on-click-modal="false"
     :close-on-press-escape="false" :destroy-on-close="true"
    :before-close="cancelSelectShelf">
    <el-form :model="shelfParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="铁架编号">
        <el-input v-model="shelfParams.frameNo" @keyup.enter.native="getShelfList" clearable/>
      </el-form-item>
      <el-form-item label="铁架名称">
        <el-input v-model="shelfParams.frameName" @keyup.enter.native="getShelfList" clearable/>
      </el-form-item>
      <!--          <el-form-item>-->
      <el-button type="primary" icon="el-icon-search" size="mini" @click="getShelfList">搜索</el-button>
      <!--          </el-form-item>-->
    </el-form>
    <slot-table class="rightTable"
                @handleChange="handleShelfChange"
                :pageSize="shelfParams.pageSize"
                :pageNum="shelfParams.pageNum"
                :total="shelfTotal">
      <el-table highlight-current-row
        :data="shelfList"
        stripe
        border
        height="100%"
        style="width: 100%"
        ref="multipleShelfTable"
        @row-click="handleRowClickShelf"
        @selection-change="handleSelectionShelf"
        :index="getShelfIndex"
        slot="table">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="item in shelfListColumn"
          :key="item.prop"
          :prop="item.prop"
          :label="item.label"
          :min-width="item.width" show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveSelectShelf">添加</el-button>
        <el-button size="mini" @click="cancelSelectShelf">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>

import SlotTable from "@/components/public/table/SlotTable";
import {queryShelfData} from "@/api/salse/order/shelfManage";

export default {
  name: "shelfDialog",
  components: {SlotTable},
  data() {
    return {
      // 铁架查询参数
      shelfParams: {
        frameNo: '',
        frameName: '',
        pageNum: 1,
        pageSize: 20,
      },
      shelfTotal: 0,
      shelfList: [],         // 表格数据
      shelfListColumn: [
        {label: '铁架名称', prop: 'frameName', width: '150'},
        {label: '铁架编号', prop: 'frameNo', width: '150'},
        {label: '铁架规格', prop: 'frameSpecs', width: '150'},
        {label: '铁架数量（个）', prop: 'frameNum', width: '150'},
        {label: '创建时间', prop: 'createdAt', width: '180'},
        {label: '创建人', prop: 'createdPerson', width: '150'},
        {label: '备注', prop: 'remark'},
      ],  //表头
    }
  },
  props: {
    selectShelfDialog: {
      type: Boolean,
      default: false,
    },
    haveShelfData: {
      type: Array,
      default: () => []
    }
  },
  mounted() {
      this.getShelfList();
  },
  // computed: {},
   methods: {

    /* 铁架查询 */
    getShelfList() {
      queryShelfData(this.shelfParams).then(res => {
        if (res.code === 200) {
          this.shelfList = res.data;
          this.shelfTotal = res.count;
        }
      })
    },
    /* 翻页后，序号连贯 */
    getShelfIndex($index) {
      //  表格序号
      return (this.shelfParams.pageNum - 1) * this.shelfParams.pageSize + $index + 1;
    },
    handleSelectionShelf(val) {
      this.selectedShelf = val;
    },
    /* 铁架列表点击行 */
    handleRowClickShelf(row, column, event) {
      this.$refs.multipleShelfTable.toggleRowSelection(row, column)
    },
    /* 保存选择铁架弹窗 */
    saveSelectShelf(val) {
      this.$emit('saveSelectShelf', this.selectedShelf, val);
    },
    cancelSelectShelf() {
      this.$emit('cancelSelectShelf');
    },
    /* 铁架分页器 */
    handleShelfChange(size, num) {
      this.shelfParams.pageSize = size;
      this.shelfParams.pageNum = num;
      this.getShelfList();
    },
  }
}
</script>
