<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn" @submit.native.prevent>
      <el-form-item label="附加项名称">
        <el-input
          v-model="queryParams.additionalName"
          placeholder="请输入附加项名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
        v-hasPermi="['system:additional:add']">新增附加项
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:additional:edit']">编辑附加项
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:additional:del']">删除附加项
      </el-button>
    </div>
    <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                :pageNum="queryParams.pageNum" :total="queryParams.count">
      <el-table highlight-current-row
        :data="tableDate"
        stripe
        border
        height="100%"
        ref="additionalTableRef"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick" slot="table">
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
          v-for="item in tableColumn"
          :key="item.prop"
          :prop="item.prop"
          :label="item.label"
          show-overflow-tooltip
          :formatter="item.formatter" >
        </el-table-column>
      </el-table>
    </slot-table>
    <el-dialog :title="dialogType=='add'?'新增附加项':dialogType=='edit'?'编辑附加项':''" :visible.sync="dialog"
               width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form size="mini" :model="additionalForm" :rules="additionalFormRules" ref="additionalFormRef" label-width="100px">
        <el-form-item label="附加项名称" prop="additionalName">
          <el-input style="width: 350px" v-model="additionalForm.additionalName"
                    placeholder="请输入附加项名称"></el-input>
        </el-form-item>
<!--        <el-form-item label="附加项别名" prop="additionalAlias">-->
<!--          <el-input style="width: 350px" v-model="additionalForm.additionalAlias"-->
<!--                    placeholder="请输入附加项别名"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="计算方式" prop="type">
          <el-select ref="computeSelect" @change="selectChange" v-model="selectType" style="width: 200px"
                     placeholder="请选择">
            <el-option
              v-for="item in dict.type.compute_type"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <span style="margin: 0 20px;">*</span>
          单价
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveSubmit()">保存</el-button>
        <el-button size="mini" @click="dialog = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  dicts: ['compute_type']
}
</script>
<script setup name="Additional">
import AppContent from "@/components/AppContent/index"
import SlotTable from "@/components/public/table/SlotTable";
import {listAddition, addAddition, editAddition, delAddition} from "@/api/system/addition"
import {Message, MessageBox} from "element-ui";
import {ref, onMounted, getCurrentInstance} from "vue";
import {delProduct} from "@/api/system/product";

const _this = getCurrentInstance()

onMounted(() => {
  handleQuery();
})
//**************Data***************
let checkType = (rule, value, callback) => {
  if (selectType.value == "") {
    return callback(new Error('请选择计算方式'));
  }
  callback();
}
const queryParams = ref({
  additionalName: "",
  pageSize: 20,
  pageNum: 1,
  count: 0
})
const tableDate = ref([])
const selectList = ref([])
const additionalTableRef = ref([])
const tableColumn = ref([
  {label: '附加项名称', prop: 'additionalName'},
  {label: '附加项别名', prop: 'additionalAlias'},
  {
    label: '计算方式', prop: 'computeType', formatter: (e) => {
      try {
        return e.computeType.indexOf(",") != -1 ? e.computeType.split(",").join("") : e.computeType
      } catch (err) {
        return e.computeType
      }
    }
  },
  {label: '创建时间', prop: 'createdAt'}
])
const additionalForm = ref({
  additionalName: '',
  additionalAlias: '',
  type: '',
  computeType: ''
})
const additionalFormRef = ref(null)
const additionalFormRules = ref({
  additionalName: [{required: true, message: '请输入附加项名称', trigger: 'blur'}],
  additionalAlias: [{required: true, message: '请输入附加项别名', trigger: 'blur'}],
  type: [{required: true, validator: checkType, trigger: 'change'}],
})
const dialogType = ref('add')
const dialog = ref(false)
const computeSelect = ref(null)
const selectType = ref("")
//**************Methods****************
const handleQuery = () => {
  tableDate.value = []
  listAddition(queryParams.value).then(res => {
    tableDate.value = res.data
    queryParams.value.count = res.count
  })
}
const handleChange = () => {
  queryParams.value.pageSize = pageSize
  queryParams.value.pageNum = pageNum
  tableDate.value=[]
  handleQuery()

}
const handleAdd = () => {
  additionalForm.value = {
    additionalName: '',
    additionalAlias: '',
    type: '',
    computeType: ''
  }
  dialogType.value = 'add'
  dialog.value = true
}
const handleUpdate = () => {
  if (selectList.value.length <= 0) {
    Message.warning("请选择要修改的数据");
    return false
  }
  if (selectList.value.length > 1) {
    Message.warning("请选择一条数据进行修改");
    return false
  }
  additionalForm.value = JSON.parse(JSON.stringify(selectList.value[0]))
  selectType.value = _this.proxy.dict.type.compute_type.filter(item => {
    return item.label == additionalForm.value.computeType.split(",")[0]
  })[0].value
  dialogType.value = 'edit'
  dialog.value = true
}
const handleDelete = () => {
  if (selectList.value.length <= 0) {
    Message.warning("请选择要删除的数据");
    return false
  }
  MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let ids = selectList.value.map(item => {
      return item.id
    }).join(",")
    delAddition(ids).then(res => {
      if (res.code == 200) {
        Message.success(res.msg);
        handleQuery();
      }
    })
  }).catch(() => {
    Message.info({
      type: 'info',
      message: '已取消删除'
    });
  });
}
const handleSelectionChange = (e) => {
  selectList.value = e
}
const handleRowClick = (row, column, event) => {
  additionalTableRef.value.toggleRowSelection(row, column)
}
const saveSubmit = () => {
  additionalFormRef.value.validate((valid) => {
    if (valid) {
      if (dialogType.value == 'add') {
        additionalForm.value.computeType = computeSelect.value.options.filter(item => {
          return item.value == selectType.value
        })[0].label + ',*,单价'
        addAddition(additionalForm.value).then(res => {
          Message.success(res.msg)
          handleQuery();
          dialog.value = false
        })
      } else if (dialogType.value == 'edit') {
        additionalForm.value.computeType = computeSelect.value.options.filter(item => {
          return item.value == selectType.value
        })[0].label + ',*,单价'
        editAddition(additionalForm.value.id, additionalForm.value).then(res => {
          Message.success(res.msg)
          handleQuery();
          dialog.value = false
        })
      }
    } else {
      return false;
    }
  })

}
const selectChange = (e) => {
  selectType.value = e
}
</script>

<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);
}

//表单中只有一个item
.onlyOneChild {
  display: flex;
  justify-content: center;
  align-items: center;

  .el-form-item {
    width: 80%;
    justify-content: center;
    margin-right: 0;
  }
}
</style>
