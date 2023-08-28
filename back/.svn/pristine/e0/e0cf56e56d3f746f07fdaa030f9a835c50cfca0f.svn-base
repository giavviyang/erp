<template>
  <div class="app-container-padding">
    <!--  选择辅料弹窗  -->
    <el-dialog
      title="选择辅料"
      :visible.sync="selectAccessoriesDialog"
      width="80%"
      class="dialog-style selectAccessoriesDialog"
      :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true"
      :before-close="cancelSelectAccessories">
      <main-flexible-tree :data="treeData"
                          :defaultProps="TreeDefaultProps"
                          @handleNodeClick="handleNodeClick">
        <div slot="mainRight" class="app-container">
          <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
            <el-form-item label="辅料名称" prop="accessoriesName">
              <el-input
                v-model="queryParams.accessoriesName"
                placeholder="请输入辅料名称"
                @keyup.enter.native="handleQuery"
                clearable/>
            </el-form-item>
            <el-form-item label="辅料编号" prop="accessoriesNo">
              <el-input
                v-model="queryParams.accessoriesNo"
                placeholder="请输入辅料编号"
                @keyup.enter.native="handleQuery"
                clearable/>
            </el-form-item>
            <el-form-item label="型号规格" prop="accessoriesSpecifications">
              <el-input
                v-model="queryParams.accessoriesSpecifications"
                placeholder="请输入型号规格"
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
                :index="getIndex"
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
        <el-button type="primary" size="mini" @click="saveSelectAccessories">添加</el-button>
        <el-button size="mini" @click="cancelSelectAccessories">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import {queryAccessoriesFilm, queryTree} from "@/api/accessories/accessoriesFilm";
import {groupBy} from "@/utils/groupBy";
import {Message} from "element-ui";
import SlotTable from "@/components/public/table/SlotTable";

export default {
  name: "accessoriesDialog",
  components: {SlotTable, MainFlexibleTree},
  data() {
    return {
      treeData: [], //左侧tree树
      TreeDefaultProps: {
        children: 'children',
        label: 'accessoriesTypeName'
      },
      queryParams: {
        id: "",
        accessoriesName: "",
        accessoriesNo: "",
        accessoriesSpecifications: '',
        typeId: "",
        accessoriesTypeName: "",
        pageNum: 1,
        pageSize: 20,
      },
      total: 1,
      tableDate: [],
      selectedAccessories: [],
      tableColumn: [
        {label: '辅料名称', prop: 'accessoriesName', width: '120'},
        {label: '辅料编号', prop: 'accessoriesNo', width: '120'},
        // {label: '长度（mm）', prop: 'accessoriesLong', width: '100'},
        // {label: '宽度（mm）', prop: 'accessoriesWidth', width: '100'},
        {label: '计数单位', prop: 'accessoriesCompany', width: '80'},
        {label: '型号规格', prop: 'accessoriesSpecifications', width: '100'},
        {label: '厂家名称', prop: 'accessoriesMill', width: '150'},

        {label: '辅料类型', prop: 'typeName', width: '100'},
        // {label: '等级', prop: 'accessoriesGrade', width: '100'},
        {label: '参考价', prop: 'referencePrice', width: '100'},
        {label: '库存预警', prop: 'alarmInventory', width: '100'},
        {label: '录入日期', prop: 'createdAt', width: '180'},
        {label: '备注', prop: 'remarks', width: '100'},
      ],
      currentNodeInfo: {},
    }
  },
  props: {
    selectAccessoriesDialog: {
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
          this.treeData = [{id: 0, accessoriesTypeName: "辅料类型", children: groupBy(res.data, "tid")}];
          if (this.treeData) {
            this.treeData.forEach(item => {
              item.typeId = item.id
            })
          }
        }
      })
    },
    /* 左侧tree树单击节点 */
    handleNodeClick(val, node, component) {
      console.log(val, node, component);
      this.currentNodeInfo = val;
      this.queryParams.typeId = val.id;
      this.handleQuery();
    },
    /* 查询辅料数据 */
    handleQuery() {
      queryAccessoriesFilm(this.queryParams).then(res => {
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
      this.selectedAccessories = val;
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.templateTableRef.toggleRowSelection(row, column)
    },
     /* 翻页后序号连贯 */
     getIndex($index) {
       //  表格序号
       return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + $index + 1;
     },
    /* 保存选择厂家弹窗 */
    saveSelectAccessories() {
      this.$emit('saveSelectAccessories', this.selectedAccessories, this.tableDate);
    },
    cancelSelectAccessories() {
      this.$emit('cancelSelectAccessories');
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
 ::v-deep .selectAccessoriesDialog {
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
