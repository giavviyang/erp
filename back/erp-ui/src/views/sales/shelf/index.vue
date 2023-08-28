<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="铁架编号">
        <el-input v-model="queryParams.frameNo"  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="铁架名称">
        <el-input v-model="queryParams.frameName" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd">新增铁架
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate">编辑铁架
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete">删除铁架
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleStock">出入库记录
      </el-button>
    </div>
    <slot-table class="frameTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total">
      <el-table highlight-current-row
        :data="frameList"
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
          v-for="(item,index) in frameColumns"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <!--  新增、编辑铁架  -->
    <el-dialog :title="frameDialogType=='add'?'新增铁架':frameDialogType=='edit'?'编辑铁架':''"
               :visible.sync="frameDialog" width="40%" class="dialog-style frameDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
          <el-form :model="frameInfo" size="mini" :inline="true" ref="ruleForm" :rules="rules" class="ipt2">
            <el-form-item label="铁架编号" prop="frameNo">
              <el-input v-model="frameInfo.frameNo" clearable placeholder="请输入铁架编号"/>
            </el-form-item>
            <el-form-item label="铁架名称" prop="frameName">
              <el-input v-model="frameInfo.frameName" clearable placeholder="请输入铁架名称"/>
            </el-form-item>
            <el-form-item label="铁架数量" prop="frameNum">
              <el-input v-model="frameInfo.frameNum" clearable placeholder="请输入铁架数量"/>
            </el-form-item>
            <el-form-item label="备注"  prop="customNo">
              <el-input v-model="frameInfo.remarks" type="textarea" clearable size="mini" placeholder="请输入内容"/>
            </el-form-item>
          </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveFrame('ruleForm')">保存</el-button>
         <el-button size="mini" @click="frameDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--   出入库记录 -->
    <el-dialog title="出入库记录" :visible.sync="stock" width="70%" class="dialog-style stockDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <slot-table class="stockList">
        <el-table highlight-current-row
          :data="stockList"
          border
          stripe
          height="100%"
          style="width: 100%"
          slot="table">
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in stockColumns"
            :key="index"
            :label="item.label"
            :prop="item.prop"
            :min-width="item.width"
            show-overflow-tooltip>
          </el-table-column>
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
  components: { SlotTable},
  data() {
    return {
      //查询参数
      queryParams: {
        pageSize: 20,
        pageNum: 1,
        frameNo: '',
        frameName: '',
      },
      pageSize: 20,
      pageNum: 1,
      total: 0,
      frameList: [{orderNo: '123', returnPerson: '456'}],  //铁架列表
      //铁架表头
      frameColumns: [
        {label: '铁架编号', prop: 'frameNo', width: '150'},
        {label: '铁架名称', prop: 'frameName', width: '200'},
        {label: '铁架规格', prop: 'frameSpecs', width: '200'},
        {label: '铁架数量（片）', prop: 'frameNum', width: '200'},
        {label: '备注', prop: 'remark'},
      ],
      selected: [],
      frameDialogType: 'add',  //新增
      frameDialog: false,  //新增、编辑弹窗
      frameInfo: {
        frameNo: '',
        frameName: '',
        frameNum: '',
        remarks: '',
      },
      stock: false,  //出入库记录弹窗
      stockColumns: [
        {label: '出入库类型', prop: 'orderNo', width: '150'},
        {label: '发货单号', prop: 'itemW', width: '150'},
        {label: '回执单号', prop: 'itemH', width: '150'},
        {label: '出入库数量（片）', prop: 'itemNum'},
        {label: '出入库日期', prop: 'customNo',},
        {label: '负责人', prop: 'customNo', width: '100'},
        {label: '备注', prop: 'customNo', width: '100'},
      ],
      stockList: [
        {frameName: '123',},
      ],
      // 表单校验
      rules: {
        frameNo: [
          {required: true, message: "铁架编号不能为空", trigger: "blur"}
        ],
        frameName: [
          {required: true, message: "铁架原因不能为空", trigger: "blur"}
        ],
        frameNum: [
          {required: true, message: '铁架数量不能为空', trigger: "blur"},
          {pattern: /^[1-9]\d*$/, message: '请输入整数'}
        ],
      },
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
      // if (this.preparationDateRange) {
      //   this.queryParams.preparationDateStart = this.preparationDateRange[0];
      //   this.queryParams.preparationDateEnd = this.preparationDateRange[1];
      // }
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
    /* 新增铁架 */
    handleAdd() {
      this.frameDialogType = 'add';
      this.frameDialog = true;
    },
    /* 编辑铁架 */
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
        this.frameInfo = JSON.parse(JSON.stringify(this.selected[0]));
        this.deliverData = this.detailDialogData;
        this.frameDialog = true;
        this.frameDialogType = 'edit';
      }
    },
    /* 删除铁架 */
    handleDelete() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要删除的数据");
        return false
      }
    },
    /* 出入库记录 */
    handleStock() {
      // if (this.selected.length <= 0) {
      //   Message.warning("请选择要审核的数据");
      //   return false
      // }
      // if (this.selected.length > 1) {
      //   Message.warning("请选择一条数据进行审核");
      //   return false
      // }
      // if (this.selected.length == 1) {
        this.stock=true;
      // }
    },
    /* 翻页后序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 铁架复选框 */
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
    /* 保存铁架 */
    saveFrame(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // console.log(this.frameInfo)
          if(this.frameDialogType==='add'){

          }
          if(this.frameDialogType==='edit'){

          }
        } else {
          return false;
        }
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.frameTable {
  height: calc(100% - 100px);
}

::v-deep .frameDialog{
  .el-dialog{
    min-height: 50px;
    .ipt2{
      width: 100%;
      justify-content: center;
      .el-form-item{
        width: 45%;
        min-width: 265px;
      }
    }
  }
}

.stockDialog {
  ::v-deep .el-dialog {
    height: calc(60vh);

    .el-dialog__body {
      height: calc(100% - 50px);

      .stockList {
        height: 100%;
      }
    }

  }
}

</style>
