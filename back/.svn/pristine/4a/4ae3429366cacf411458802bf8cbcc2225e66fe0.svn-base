<template>
  <div style="width: 100%;height: calc(100% - 50px);">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="规则名称">
        <el-input
          v-model="queryParams.qrName"
          placeholder="请输入规则名称"
          clearable
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['system:qrcode:add']">新增规则
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:qrcode:edit']">编辑规则
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:qrcode:del']">删除规则
      </el-button>
    </div>
    <slot-table style="height: 100%;" class="rightTable orderTable" @handleChange="handleChange"
                :pageSize="queryParams.pageSize" :pageNum="queryParams.pageNum"
                :total="queryParams.total">
      <el-table highlight-current-row
                v-loading="tableLoading"
                :data="qrcodeList"
                stripe
                border
                style="width: 100%"
                height="100%"
                ref="qrcodeTableRef"
                @selection-change="handleSelectionChange"
                @row-click="handleRowClick"
                slot="table">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-for="item in qrcodeColumn"
          :key="item.label"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          :formatter="item.formatter"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <el-dialog :title="qrcodeDialogType == 'add'?'新增二维码规则':'编辑二维码规则'" :visible.sync="qrcodeDialog"
               width="600px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form :inline="true" size="mini" :model="qrcodeForm" :rules="qrcodeFormRules" ref="qrcodeFormRef"
               label-width="70px">
        <el-form-item label="名称" prop="qrName">
          <el-input style="width: 200px" v-model="qrcodeForm.qrName" placeholder="请输入二维码规则名称"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="qrcodeForm.status">
            <el-radio :label="0">未启用</el-radio>
            <el-radio :label="1">已启用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input style="width: 460px;color:#333;" v-model="qrcodeForm.remarks" type="textarea" size="mini"
                    placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-card style="width: 100%">
          <div slot="header" class="clearfix">
            <span style="color: red;">选择二维码信息</span>
          </div>
          <el-checkbox-group v-model="qrContent" @change="checkboxChange">
            <el-checkbox style="margin-bottom: 5px;" v-for="dict in dict.type.sys_qrcode" :label="dict.label"
                         :key="dict.label">{{ dict.label }}
            </el-checkbox>
          </el-checkbox-group>
        </el-card>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="qrcodeSubmit()">保存</el-button>
        <el-button size="mini" @click="qrcodeDialog = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  dicts: ['sys_qrcode']
}
</script>
<script setup>
import SlotTable from "@/components/public/table/SlotTable";
import {getQRCodeLise, addQRCode, editQRCode, delQRCode} from "@/api/system/qrcode";
import {ref, onMounted, getCurrentInstance} from "vue";
import {Message, MessageBox} from "element-ui";
import {delCraft} from "@/api/system/craft";

onMounted(() => {
  getList()
})

const _this = getCurrentInstance().proxy

/** data */
const qrcodeColumn = ref([
  {label: '二维码规则名称', prop: 'qrName',width:'120'},
  {label: '二维码规则内容', prop: 'qrContent',width:'300'},
  {
    label: '启用状态', prop: 'status',width:'100', formatter: (row, column, cellValue) => {
      return cellValue == 0 ? '未启用' : '已启用'
    }
  },
  {label: '创建人', prop: 'createdPerson',width:'100'},
  {label: '创建时间', prop: 'createdAt',width:'150'}
]);
const queryParams = ref({
  total: 0,
  pageNum: 1,
  pageSize: 20,
  qrName: ""
})
const tableLoading = ref(false)
const qrcodeList = ref([])
const codeSelect = ref([])
const qrcodeTableRef = ref(null)
const qrcodeDialogType = ref("")
const qrcodeDialog = ref(false)
const qrcodeForm = ref({
  qrName: "",
  qrContent: "",
  status: 0,
  remarks: ""
})
const qrcodeFormRules = ref({
  qrName: [{required: true, message: '请输入二维码规则名称', trigger: 'blur'}],
  qrContent: [{required: true, message: '请选择二维码规则内容', trigger: 'change'}]
})
const qrcodeFormRef = ref(null)
const qrContent = ref([])
/** methods */
const handleChange = (size, num) => {
  queryParams.value.pageNum = num
  queryParams.value.pageSize = size
  qrcodeList.value = []
  getList()
}
const handleSelectionChange = (e) => {
  codeSelect.value = e
}
const handleRowClick = (row, column, event) => {
  qrcodeTableRef.value.toggleRowSelection(row, column)
}
const getList = () => {
  getQRCodeLise(queryParams.value).then(res => {
    qrcodeList.value = res.data
    queryParams.value.total = res.count
  })
}
const qrcodeSubmit = () => {
  qrcodeFormRef.value.validate((valid) => {
    if (valid) {
      if (qrContent.value == "" || qrContent.value.length <= 0) {
        Message.error("请选择二维码规则内容")
        return false
      }
      if (!qrcodeForm.value.id) {
        qrcodeForm.value.qrContent = qrContent.value.toString()
        addQRCode(qrcodeForm.value).then(res => {
          Message.success(res.msg)
          qrcodeDialog.value = false
          handleChange(20, 1)
        })
        return false
      }
      qrcodeForm.value.qrContent = qrContent.value.toString()
      editQRCode(qrcodeForm.value.id, qrcodeForm.value).then(res => {
        Message.success(res.msg)
        qrcodeDialog.value = false
        handleChange(20, 1)
      })
    }
    return false
  })
}
const checkboxChange = () => {
  qrcodeForm.value.qrContent = qrContent.value
}
const statusFormatter = (cellValue) => {
  if (cellValue == 0) {
    return "未启用"
  } else {
    return "已启用"
  }
}
const handleAdd = () => {
  qrcodeForm.value = {
    qrName: "",
    qrContent: "",
    status: 0,
    remarks: ""
  }
  qrcodeDialogType.value = 'add'
  qrContent.value = []
  qrcodeDialog.value = true
}
const handleUpdate = () => {
  if (codeSelect.value.length <= 0 || codeSelect.value.length > 1) {
    Message.warning("请选择一条数据")
    return false
  }
  qrcodeForm.value = JSON.parse(JSON.stringify(codeSelect.value[0]))
  if (qrcodeForm.value.qrContent != "") {
    qrContent.value = qrcodeForm.value.qrContent.split(",")
  } else {
    qrContent.value = []
  }
  qrcodeDialogType.value = 'edit'
  qrcodeDialog.value = true
}
const handleDelete = () => {
  if (codeSelect.value.length <= 0) {
    Message.warning("请选择要删除的数据");
    return false
  }
  MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let ids = codeSelect.value.map(item => {
      return item.id
    }).join(",")
    delQRCode(ids).then(res => {
      if (res.code == 200) {
        Message.success(res.msg);
        handleChange(20, 1);
      }
    })
  }).catch(() => {
    Message.info({
      type: 'info',
      message: '已取消删除'
    });
  });
}
</script>
