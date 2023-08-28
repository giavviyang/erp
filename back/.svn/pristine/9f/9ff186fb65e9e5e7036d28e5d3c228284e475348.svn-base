<template>
  <div class="app-container-padding">
    <!--  选择原片弹窗  -->
    <el-dialog
      title="选择原片"
      :visible.sync="selectOriginalDialog"
      width="80%"
      class="dialog-style selectOriginalDialog"
      :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true"
      :before-close="cancelSelectOriginal">
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
            <!--            <el-form-item>-->
            <el-button type="primary" size="mini" icon="el-icon-search" @click="handleQuery">搜索</el-button>
            <!--            </el-form-item>-->
          </el-form>
          <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                      :pageNum="queryParams.pageNum" :total="total">
            <el-table highlight-current-row
              :data="tableDate"
              stripe
              border
              height="100%"
              ref="templateTableRef"
              @selection-change="handleSelectionChange"
              @row-click="handleRowClick" slot="table">
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
                v-for="item in tableColumn"
                :key="item.id"
                :prop="item.prop"
                :label="item.label"
                :min-width="item.width">
              </el-table-column>
            </el-table>
          </slot-table>
        </div>
      </main-flexible-tree>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveSelectOriginal">添加</el-button>
        <el-button size="mini" @click="cancelSelectOriginal">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import {queryOriginalFilm, queryTree} from "@/api/original/originalFilm";
import {groupBy} from "@/utils/groupBy";
import {Message} from "element-ui";
import SlotTable from "@/components/public/table/SlotTable";

export default {
  name: "originalDialog",
  components: {SlotTable, MainFlexibleTree},
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
      total: 1,
      tableDate: [],
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
        {label: '参考采购单价（元/m²）', prop: 'referencePrice', width: '150'},
        {label: '库存预警（片）', prop: 'earlyWarning', width: '120'},
        {label: '录入日期', prop: 'createdAt', width: '180'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      currentNodeInfo: {},
    }
  },
  props: {
    selectOriginalDialog: {
      type: Boolean,
      default: false,
    },
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
    /* 保存选择厂家弹窗 */
    saveSelectOriginal() {
      this.$emit('saveSelectOriginal', this.selectedOriginal,this.tableDate);
    },
    cancelSelectOriginal() {
      this.$emit('cancelSelectOriginal');
    },
  }
}
</script>

<style lang="scss" scoped>
::v-deep .containerTree {
  .el-input__suffix {
    .el-icon-circle-close:before {

      top: 20px;
      right: 5px;
      position: absolute;
    }
  }
}
 ::v-deep .selectOriginalDialog {
  height: 100%;

  .el-dialog {
    height: calc(65vh);

    .el-dialog__body {
      height: calc(100% - 90px);
      padding: 0;
      ::v-deep .iptAndBtn {
        overflow: auto;

        .el-form-item {
          min-width: 200px;

          .el-form-item__content {
            width: 120px;
          }
        }
      }

      .rightTable {
        height: calc(100% - 65px);
        margin-top: 10px;

      }
    }
  }
}

</style>
