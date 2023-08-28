<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" class="iptAndBtn" ref="queryForm" size="mini" :inline="true" v-show="showSearch">
      <el-form-item label="公告标题" prop="noticeTitle">
        <el-input
          v-model="queryParams.noticeTitle"
          placeholder="请输入公告标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作人员" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入操作人员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="noticeType">
        <el-select v-model="queryParams.noticeType" placeholder="公告类型" clearable @change="handleQuery">
          <el-option
            v-for="dict in dict.type.sys_notice_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!--      <el-form-item>-->
      <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      <!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
      <!--      </el-form-item>-->
    </el-form>

    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['system:notice:add']"
      >新增
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:notice:edit']"
      >修改
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:notice:remove']"
      >删除
      </el-button>
    </div>
    <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                :pageNum="queryParams.pageNum"
                :total="total">
      <el-table highlight-current-row slot="table" v-loading="loading" style="width: 100%" height="100%" border stripe ref="templateTableRef"
                :data="noticeList" @selection-change="handleSelectionChange" @row-click="handleRowClick">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="序号" align="center" type="index" width="100" :index="getIndex"/>
        <el-table-column
          label="公告标题"
          align="center"
          prop="noticeTitle"
          :show-overflow-tooltip="true" show-overflow-tooltip
        />
        <el-table-column label="公告类型" align="center" prop="noticeType" width="150" class-name="noticeType" show-overflow-tooltip>
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_notice_type" :value="scope.row.noticeType"/>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_notice_status" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="创建者" align="center" prop="createBy" width="150" show-overflow-tooltip/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <!--    <el-table-column label="操作" align="center" width="150">-->
        <!--      <template slot-scope="scope">-->
        <!--        <el-button-->
        <!--          size="mini"-->
        <!--          type="text"-->
        <!--          @click="handleUpdate(scope.row)"-->
        <!--          v-hasPermi="['system:notice:edit']"-->
        <!--        >修改</el-button>-->
        <!--        <el-button-->
        <!--          size="mini"-->
        <!--          type="text"-->
        <!--          @click="handleDelete(scope.row)"-->
        <!--          v-hasPermi="['system:notice:remove']"-->
        <!--        >删除</el-button>-->
        <!--      </template>-->
        <!--    </el-table-column>-->
      </el-table>
    </slot-table>

    <!--    <pagination-->
    <!--      v-show="total>0"-->
    <!--      :total="total"-->
    <!--      :page.sync="queryParams.pageNum"-->
    <!--      :limit.sync="queryParams.pageSize"-->
    <!--      @pagination="getList"-->
    <!--    />-->

    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body :close-on-click-modal="false"
               :close-on-press-escape="false"
               :destroy-on-close="true" class="dialog-style">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="公告标题" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="请输入公告标题"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公告类型" prop="noticeType">
              <el-select v-model="form.noticeType" placeholder="请选择公告类型">
                <el-option
                  v-for="dict in dict.type.sys_notice_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_notice_status"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容">
              <editor v-model="form.noticeContent" :min-height="192"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="mini">确 定</el-button>
        <el-button @click="cancel" size="mini">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listNotice, getNotice, delNotice, addNotice, updateNotice} from "@/api/system/notice";
import AppContent from "@/components/AppContent/index";
import SlotTable from "@/components/public/table/SlotTable";

export default {
  name: "Notice",
  dicts: ['sys_notice_status', 'sys_notice_type'],
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
      // 公告表格数据
      noticeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        noticeTitle: [
          {required: true, message: "公告标题不能为空", trigger: "blur"}
        ],
        noticeType: [
          {required: true, message: "公告类型不能为空", trigger: "change"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    // // 表单重置
    // reset() {
    //   this.form = {
    //     noticeId: undefined,
    //     noticeTitle: undefined,
    //     noticeType: undefined,
    //     noticeContent: undefined,
    //     status: "0"
    //   };
    //   this.resetForm("form");
    // },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // /** 重置按钮操作 */
    // resetQuery() {
    //   this.resetForm("queryForm");
    //   this.handleQuery();
    // },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.noticeId)
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
    /*分页器*/
    handleChange(size, num) {
      this.queryParams.pageNum = num;
      this.queryParams.pageSize = size;
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      // this.reset();
      this.open = true;
      this.title = "添加公告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      if (this.single) {
        this.$message.warning("请选择一条数据进行操作")
        return false
      }
      // this.reset();
      const noticeId = row.noticeId || this.ids
      getNotice(noticeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改公告";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.noticeId != undefined) {
            updateNotice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNotice(this.form).then(response => {
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
      const noticeIds = row.noticeId || this.ids
      this.$modal.confirm('是否确认删除公告编号为"' + noticeIds + '"的数据项？').then(function () {
        return delNotice(noticeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
    Message.info({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);

  ::v-deep .noticeType {
    .el-tag {
      background-color: transparent;
      color: #909399;
      border: none;
    }

  }
}
</style>
