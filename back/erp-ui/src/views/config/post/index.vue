<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" class="iptAndBtn">
      <el-form-item label="岗位编码" prop="postCode">
        <el-input
          v-model="queryParams.postCode"
          placeholder="请输入岗位编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="岗位名称" prop="postName">
        <el-input
          v-model="queryParams.postName"
          placeholder="请输入岗位名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="岗位状态" clearable @change="handleQuery">
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
        @click="handleAdd"
        v-hasPermi="['system:post:add']"
      >新增岗位
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:post:edit']"
      >编辑岗位
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:post:remove']"
      >删除岗位
      </el-button>
      <el-button
        type="primary"
        size="mini"
        @click="handleExport"
        v-hasPermi="['system:post:export']"
      ><i class="iconfont icon-daochuwenjian"></i>导出
      </el-button>
    </div>
    <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                :pageNum="queryParams.pageNum"
                :total="total">
      <el-table  highlight-current-row slot="table" style="width: 100%" height="100%" border stripe v-loading="loading" :data="postList"
                ref="templateTableRef" @selection-change="handleSelectionChange" @row-click="handleRowClick">
        <el-table-column type="selection" width="55" align="center" show-overflow-tooltip/>
        <el-table-column
          type="index"
          :index="getIndex"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="岗位编码" align="center" prop="postCode" show-overflow-tooltip/>
        <el-table-column label="岗位名称" align="center" prop="postName" show-overflow-tooltip/>
        <el-table-column label="岗位排序" align="center" prop="postSort" show-overflow-tooltip/>
        <el-table-column label="状态" align="center" prop="status" show-overflow-tooltip class-name="noticeType">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
        <!--        <template slot-scope="scope">-->
        <!--          <el-button-->
        <!--            size="mini"-->
        <!--            type="text"-->
        <!--            icon="el-icon-edit"-->
        <!--            @click="handleUpdate(scope.row)"-->
        <!--            v-hasPermi="['system:post:edit']"-->
        <!--          >修改</el-button>-->
        <!--          <el-button-->
        <!--            size="mini"-->
        <!--            type="text"-->
        <!--            icon="el-icon-delete"-->
        <!--            @click="handleDelete(scope.row)"-->
        <!--            v-hasPermi="['system:post:remove']"-->
        <!--          >删除</el-button>-->
        <!--        </template>-->
        <!--      </el-table-column>-->
      </el-table>
    </slot-table>

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false"
               :close-on-press-escape="false"
               :destroy-on-close="true" class="dialog-style">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" style="width: 80%;margin: 0 auto">
        <el-form-item label="岗位名称" prop="postName">
          <el-input v-model="form.postName" placeholder="请输入岗位名称"/>
        </el-form-item>
        <el-form-item label="岗位编码" prop="postCode">
          <el-input v-model="form.postCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="岗位顺序" prop="postSort">
          <el-input-number v-model="form.postSort" :min="0" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="岗位状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" size="mini" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="mini">确 定</el-button>
        <el-button @click="cancel" size="mini">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPost, getPost, delPost, addPost, updatePost} from "@/api/system/post";
import AppContent from "@/components/AppContent/index";
import SlotTable from "@/components/public/table/SlotTable";

export default {
  name: "Post",
  dicts: ['sys_normal_disable'],
  components: {SlotTable, AppContent},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 岗位表格数据
      postList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        postCode: undefined,
        postName: undefined,
        status: undefined
      },
      // 表单参数
      form: {status: '0'},
      // 表单校验
      rules: {
        postName: [
          {required: true, message: "岗位名称不能为空", trigger: "blur"}
        ],
       /* postCode: [
          {required: true, message: "岗位编码不能为空", trigger: "blur"}
        ],
        postSort: [
          {required: true, message: "岗位顺序不能为空", trigger: "blur"}
        ]*/
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      listPost(this.queryParams).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    // 表单重置
    // reset() {
    //   this.form = {
    //     postId: undefined,
    //     postCode: undefined,
    //     postName: undefined,
    //     postSort: 0,
    //     status: "0",
    //     remark: undefined
    //   };
    //   this.resetForm("form");
    // },
    /** 搜索按钮操作 */
    handleQuery() {
      // this.queryParams.pageNum = 1;
      this.getList();
    },
    /*分页器*/
    handleChange(size, num) {
      this.queryParams.pageNum = num;
      this.queryParams.pageSize = size;
      this.handleQuery();
    },

    // /** 重置按钮操作 */
    // resetQuery() {
    //   this.resetForm("queryForm");
    //   this.handleQuery();
    // },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.postId)
      this.single = selection.length != 1
      this.multiple = !selection.length
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
    /** 新增按钮操作 */
    handleAdd() {
      // this.reset();
      this.open = true;
      this.title = "新增岗位";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      if (this.single) {
        this.$message.warning("请选择一条数据进行操作")
        return false
      }
      // this.reset();
      const postId = row.postId || this.ids
      getPost(postId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑岗位";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId != undefined) {
            updatePost(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPost(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      if (this.multiple) {
        this.$message.warning("请选择一条数据进行操作")
        return false
      }
      const postIds = row.postId || this.ids;
      this.$modal.confirm('是否确认删除岗位编号为"' + postIds + '"的数据项？').then(function () {
        return delPost(postIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
    this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/post/export', {
        ...this.queryParams
      }, `post_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
  ::v-deep .noticeType{
    .el-tag{
      background-color: transparent;
      color: #909399;
      border: none;
    }

  }
}
</style>
