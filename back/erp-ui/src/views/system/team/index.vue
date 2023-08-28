<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn" @submit.native.prevent>
      <el-form-item label="班组名称">
        <el-input
          v-model="queryParams.teamName"
          placeholder="请输入班组名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--      <el-form-item>-->
      <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      <!--      </el-form-item>-->
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['system:team:add']">新增班组
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:team:edit']">编辑班组
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:team:del']">删除班组
      </el-button>
    </div>
    <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                :pageNum="queryParams.pageNum" :total="queryParams.count">
      <el-table highlight-current-row
        :data="tableDate"
        stripe
        border
        height="100%"
        ref="temTableRef"
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
          :formatter="item.formatter">
        </el-table-column>
      </el-table>
    </slot-table>
    <el-dialog :title="dialogType=='add'?'新增班组':dialogType=='edit'?'编辑班组':''" :visible.sync="dialog"
               width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form size="mini" :model="teamForm" :rules="teamFormRules" ref="teamFormRef" label-width="100px">
        <el-form-item label="新增班组" prop="teamName">
          <el-input style="width: 350px" v-model="teamForm.teamName" placeholder="请输入班组名称"></el-input>
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-cascader
            ref="computeSelect"
            v-model="teamForm.deptId"
            :options="treeData"
            style="width: 350px"
            :props="{value:'deptId',label:'deptName',checkStrictly: true, emitPath: false}"
            :show-all-levels="false"
            clearable>
            <template slot-scope="{ node, data }">
              <div @click="cascaderClick(node,data)">
                <span>{{ data.deptName }}</span>
                <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
              </div>
            </template>
          </el-cascader>
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
import {listTeam, addTeam, updateTeam, delTeam} from "@/api/system/team"
import {getAllTree} from "@/api/system/dept";
import {Message, MessageBox} from "element-ui";
import {ref, onMounted, getCurrentInstance} from "vue";
import {groupBy} from "@/utils/groupBy";

onMounted(() => {
  handleQuery();
  getTreeList();
})
//**************Data***************
const queryParams = ref({
  teamName: "",
  pageSize: 20,
  pageNum: 1,
  count: 0
})
const tableDate = ref([])
const selectList = ref([])
const deptList = ref([])
const temTableRef = ref([])
const tableColumn = ref([
  {label: '班组名称', prop: 'teamName'},
  {label: '所属部门', prop: 'deptName'},
])
const teamForm = ref({
  teamName: '',
  deptId: ''
})
const teamFormRef = ref(null)
const teamFormRules = ref({
  teamName: [{required: true, message: '请输入班组名称', trigger: 'blur'}],
  deptId: [{required: true, message: '请选择所属部门', trigger: 'change'}],
})
const treeData = ref(null)
const getTreeList = () => {
  getAllTree().then(res => {
    treeData.value = groupBy(res.data, "parentId", "deptId")
    deptList.value = res.data;
  })
}
const computeSelect = ref(null)
const dialogType = ref('add')
const dialog = ref(false)
const cascaderClick = (node, nodeData) => {
  teamForm.value.deptId = nodeData.deptId
  teamForm.value.deptName = nodeData.deptName
  computeSelect.value.checkedValue = node.value;
  computeSelect.value.computePresentText();
}
//**************Methods****************
const handleQuery = () => {
  tableDate.value = []
  listTeam(queryParams.value).then(res => {
    tableDate.value = res.data
    queryParams.value.count = res.count
  })
}

const handleChange = (size, num) => {
  queryParams.value.pageSize = size
  queryParams.value.pageNum = num
  tableDate.value = []
  handleQuery();
}
const handleAdd = () => {
  teamForm.value = {
    teamName: '',
    deptId: ''
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
  teamForm.value = JSON.parse(JSON.stringify(selectList.value[0]))
  console.log(selectList.value[0])
  teamForm.value.deptId = Number(teamForm.value.deptId)
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
    delTeam(ids).then(res => {
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
  temTableRef.value.toggleRowSelection(row, column)
}

const saveSubmit = () => {
  teamFormRef.value.validate((valid) => {
    if (valid) {
      if (teamForm.value.deptName || teamForm.value.deptName != '') {
        teamForm.value.deptName = deptList.value.filter(item => {
          return item.deptId == teamForm.value.deptId
        })[0]["deptName"]
      }
      if (dialogType.value == 'add') {
        addTeam(teamForm.value).then(res => {
          Message.success(res.msg)
          dialog.value = false
          handleQuery();
        })
      } else if (dialogType.value == 'edit') {
        let {id, ...data} = {...teamForm.value}
        updateTeam(id, data).then(res => {
          Message.success(res.msg)
          dialog.value = false
          handleQuery();
        })
      }
    } else {
      return false;
    }
  })

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
