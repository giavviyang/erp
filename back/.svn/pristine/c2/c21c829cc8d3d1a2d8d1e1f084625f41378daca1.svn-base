<template>
  <AppContent>
    <el-tabs v-model="activeName" style="height: 100%" type="border-card" @tab-click="handleClick">
      <el-tab-pane label="产品工艺" name="craft">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn"
                 @submit.native.prevent>
          <el-form-item label="工艺名称">
            <el-input
              v-model="queryParams.craftName"
              placeholder="请输入工艺名称"
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
            v-hasPermi="['system:craft:add']">新增工艺
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="handleUpdate"
            v-hasPermi="['system:craft:edit']">编辑工艺
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            @click="handleDelete"
            v-hasPermi="['system:craft:edit']">删除工艺
          </el-button>
        </div>
        <div style="width: 100%; height: calc(100% - 56px)">
          <slot-table style="height: 100%;" class="rightTable orderTable" @handleChange="handleChange"
                      :pageSize="pageSize" :pageNum="pageNum"
                      :total="total">
            <el-table highlight-current-row
                      v-loading="tableLoading"
                      :data="craftResult"
                      stripe
                      border
                      style="width: 100%"
                      height="100%"
                      ref="craftTableRef"
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
                width="50">
                <template slot-scope="scope">
                  <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                v-for="item in craftColumn"
                :key="item.label"
                :label="item.label"
                :prop="item.prop"
                show-overflow-tooltip>
              </el-table-column>
            </el-table>
          </slot-table>
        </div>
        <el-dialog :title="craftDialogType=='add'?'新增工艺':craftDialogType=='edit'?'编辑工艺':''"
                   :visible.sync="craftDialog" width="500px"
                   class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
                   :destroy-on-close="true">
          <el-form size="mini" :model="craftForm" :rules="craftFormRules" ref="craftFormRef" label-width="100px">
            <el-form-item label="工艺名称" prop="craftName">
              <el-input style="width: 350px" v-model="craftForm.craftName" placeholder="请输入工艺名称"></el-input>
            </el-form-item>
            <el-form-item label="工艺描述" prop="craftDescribe">
              <el-input type="textarea" style="width: 350px;" v-model="craftForm.craftDescribe"
                        placeholder="请输入工艺描述"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" size="mini" @click="dialogSubmit('craftSave')">保存</el-button>
            <el-button size="mini" @click="dialogSubmit('craftCancel')">取消</el-button>
          </span>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane label="工艺流程" name="craftFlow">
        <el-form :model="craftFlowQueryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn"
                 @submit.native.prevent>
          <el-form-item label="工艺名称">
            <el-input
              v-model="craftFlowQueryParams.craftFlowTxt"
              placeholder="请输入工艺名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="流程描述">
            <el-input
              v-model="craftFlowQueryParams.craftFlowDescribe"
              placeholder="请输入流程描述"
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
            v-hasPermi="['system:craftflow:add']">新增流程
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="handleUpdate"
            v-hasPermi="['system:craftflow:edit']">编辑流程
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            @click="handleDelete"
            v-hasPermi="['system:craftflow:edit']">删除流程
          </el-button>
        </div>
        <div style="width: 100%; height: calc(100% - 56px)">
          <slot-table style="height: 100%;" class="rightTable orderTable" @handleChange="handleChange"
                      :pageSize="pageSize" :pageNum="pageNum"
                      :total="total">
            <el-table highlight-current-row
                      v-loading="tableLoading"
                      :data="craftFlowResult"
                      stripe
                      border
                      style="width: 100%"
                      height="100%"
                      ref="craftFlowTableRef"
                      @selection-change="handleSelectionChange"
                      @row-click="handleFlowRowClick"
                      slot="table">
              <el-table-column
                type="selection"
                width="55">
              </el-table-column>
              <el-table-column
                type="index"
                label="序号"
                width="50">
                <template slot-scope="scope">
                  <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                v-for="item in craftFlowColumn"
                :key="item.label"
                :label="item.label"
                :prop="item.prop"
                :min-width="item.width"
                show-overflow-tooltip>
              </el-table-column>
            </el-table>
          </slot-table>
        </div>
        <el-dialog :title="craftFlowDialogType=='add'?'新增流程':craftFlowDialogType=='edit'?'编辑流程':''"
                   :visible.sync="craftFlowDialog" width="500px"
                   class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
                   :destroy-on-close="true">
          <el-form size="mini" :model="craftFlowForm" :rules="craftFlowFormRules" ref="craftFlowFormRef"
                   label-width="100px">
            <el-form-item label="流程描述" prop="craftFlowDescribe">
              <el-input style="width: 350px" v-model="craftFlowForm.craftFlowDescribe"
                        placeholder="请输入流程描述"></el-input>
            </el-form-item>
            <el-form-item label="工艺流程" prop="craftFlowTxt">
              <el-input style="width: 350px;color:#333;" v-model="craftFlowForm.craftFlowTxt" placeholder="请选择工艺"
                        disabled></el-input>
            </el-form-item>
          </el-form>
          <el-card>
            <el-checkbox-group class="craftSelectBox" v-model="craftFlowId" @change="checkBoxChange">
              <el-checkbox style=" flex: 0 0 20%; margin-right: 0px; margin: 3px 0" v-for="item in craftList"
                           :label="item" :key="item.id">
                {{ item.craftName }}
              </el-checkbox>
            </el-checkbox-group>
          </el-card>
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" size="mini" @click="dialogSubmit('craftSave')">保存</el-button>
            <el-button size="mini" @click="dialogSubmit('craftCancel')">取消</el-button>
          </span>
        </el-dialog>
      </el-tab-pane>
    </el-tabs>
  </AppContent>
</template>

<script setup>
import AppContent from "@/components/AppContent/index";
import SlotTable from "@/components/public/table/SlotTable";
import {
  getCraftList,
  addCraft,
  editCraft,
  delCraft,
  getCraftFlowList,
  getAllCraftList,
  addCraftFlow, editCraftFlow, delCraftFlow
} from "@/api/system/craft"
import {ref, onMounted} from "vue";
import {Message, MessageBox} from "element-ui";

const craftFlowId = ref([])
const activeName = ref("craft")
const craftList = ref([])
const craftDialog = ref(false)
const craftFlowDialog = ref(false)
const craftFlowDialogType = ref("add")
const craftDialogType = ref("add")
const craftFormRef = ref(null)
const craftFlowFormRef = ref(null)
const craftFlowTableRef = ref(null)
const craftTableRef = ref(null)
const craftForm = ref({craftName: "", craftDescribe: ""})
const craftFlowForm = ref({craftFlowId: "", craftFlowTxt: "", craftFlowDescribe: ""})
const craftFormRules = ref({
  craftName: [
    {required: true, message: '请输入工艺名称', trigger: 'blur'}
  ]
})
const craftFlowFormRules = ref({
  craftFlowTxt: [
    {required: true, message: '请选择工艺', trigger: 'blur'}
  ]
})
const craftSelect = ref([])
const craftFlowSelect = ref([])
const queryParams = ref({craftName: ""})
const craftFlowQueryParams = ref({craftFlowTxt: "", craftFlowDescribe: ""})
const pageSize = ref(20)
const pageNum = ref(1)
const total = ref(0)
const tableLoading = ref(false)
const craftResult = ref([])
const craftFlowResult = ref([])
const craftColumn = ref([
  {label: '工艺名称', prop: 'craftName'},
  {label: '工艺描述', prop: 'craftDescribe'},
  {label: '创建人', prop: 'createdPerson'},
  {label: '创建时间', prop: 'createdAt'}
])
const craftFlowColumn = ref([
  {label: '工艺流程', prop: 'craftFlowTxt', width: '400'},
  {label: '流程描述', prop: 'craftFlowDescribe', width: '200'},
  {label: '创建人', prop: 'createdPerson', width: '100'},
  {label: '创建日期', prop: 'createdAt', width: '100'},
])

onMounted(() => {
  getList()
})

const getList = () => {
  tableLoading.value = true
  if (activeName.value == 'craft') {
    let {craftName} = queryParams.value
    getCraftList({craftName, pageNum: pageNum.value, pageSize: pageSize.value}).then(res => {
      tableLoading.value = false
      if (res.code == 200) {
        craftResult.value = craftResult.value.concat(res.data)
        total.value = res.count
      }
    })
    return false;
  }
  if (activeName.value == 'craftFlow') {
    let {craftFlowTxt, craftFlowDescribe} = craftFlowQueryParams.value
    getCraftFlowList({craftFlowTxt, craftFlowDescribe, pageNum: pageNum.value, pageSize: pageSize.value}).then(res => {
      tableLoading.value = false
      if (res.code == 200) {
        craftFlowResult.value = craftFlowResult.value.concat(res.data)
        total.value = res.count
      }
    })
    return false;
  }

}

const handleClick = (e) => {
  activeName.value = e.name
  craftResult.value = []
  pageNum.value = 1
  craftFlowResult.value = []
  getList()
}
const handleQuery = () => {
  pageNum.value = 1
  pageSize.value = 20
  craftResult.value = []
  craftFlowResult.value = []
  getList()
}
const dialogSubmit = (type) => {
  switch (type) {
    case 'craftSave':
      if (activeName.value == 'craft') {
        if (craftDialogType.value == 'add') {
          craftFormRef.value.validate((valid) => {
            if (valid) {
              let data = {...craftForm.value}
              addCraft(data).then(res => {
                if (res.code == 200) {
                  Message.success(res.msg);
                  handleChange(20, 1);
                  craftDialog.value = false;
                }
              })
            } else {
              return false;
            }
          });
          return false
        }
        if (craftDialogType.value == 'edit') {
          console.log(111)
          craftFormRef.value.validate((valid) => {
            if (valid) {
              let {id, craftName, craftDescribe} = {...craftForm.value}
              editCraft(id, {craftName, craftDescribe}).then(res => {
                if (res.code == 200) {
                  Message.success(res.msg);
                  handleChange(pageSize.value, pageNum.value);
                  craftDialog.value = false;
                }
              })
            } else {
              return false;
            }
            return false
          });
        }
      } else if (activeName.value == 'craftFlow') {
        if (craftFlowDialogType.value == 'add') {
          craftFlowFormRef.value.validate((valid) => {
            if (valid) {
              let data = {...craftFlowForm.value}
              addCraftFlow(data).then(res => {
                if (res.code == 200) {
                  Message.success(res.msg);
                  handleChange(20, 1);
                  craftFlowDialog.value = false;
                }
              })
            } else {
              return false;
            }
          });
          return false
        }
        if (craftFlowDialogType.value == 'edit') {
          craftFlowFormRef.value.validate((valid) => {
            if (valid) {
              let {id, craftFlowId, craftFlowDescribe, craftFlowTxt} = {...craftFlowForm.value}
              editCraftFlow(id, {craftFlowId, craftFlowDescribe, craftFlowTxt}).then(res => {
                if (res.code == 200) {
                  Message.success(res.msg);
                  handleChange(pageSize.value, pageNum.value);
                  craftFlowDialog.value = false;
                }
              })
            } else {
              return false;
            }
            return false
          });
        }
      }
      break;
    case 'craftCancel':
      if (activeName.value == 'craftFlow') {
        craftFlowDialog.value = false
        break;
      }
      craftDialog.value = false;
      break;
  }
}

const handleAdd = async () => {
  if (activeName.value == 'craftFlow') {
    craftFlowDialogType.value = "add"
    craftFlowId.value = []
    craftFlowForm.value = {craftFlowId: "", craftFlowTxt: "", craftFlowDescribe: ""}
    // if(craftList.value.length <= 0){
    let {data} = await getAllCraftList()
    craftList.value = data
    // }
    craftFlowDialog.value = true
    return false
  }
  craftForm.value = {craftName: "", craftDescribe: ""}
  craftDialogType.value = "add"
  craftDialog.value = true;
}
const handleUpdate = async () => {
  if (craftList.value.length <= 0) {
    let {data} = await getAllCraftList()
    craftList.value = data
  }
  if (activeName.value == "craft") {
    if (craftSelect.value.length <= 0) {
      Message.warning("请选择要修改的数据");
      return false
    }
    if (craftSelect.value.length > 1) {
      Message.warning("请选择一条数据进行修改");
      return false
    }
    craftForm.value = JSON.parse(JSON.stringify(craftSelect.value[0]))
    craftDialogType.value = "edit"
    craftDialog.value = true;
  } else {
    if (craftFlowSelect.value.length <= 0) {
      Message.warning("请选择要修改的数据");
      return false
    }
    if (craftFlowSelect.value.length > 1) {
      Message.warning("请选择一条数据进行修改");
      return false
    }
    craftFlowForm.value = JSON.parse(JSON.stringify(craftFlowSelect.value[0]))
    craftFlowId.value = craftFlowForm.value.craftFlowId.split(',').map(item => {
      return craftList.value.filter(item2 => {
        return item == item2.id
      })[0]
    })
    console.log(craftFlowId)
    craftFlowDialogType.value = "edit"
    craftFlowDialog.value = true;
  }

}
const handleDelete = () => {
  if (activeName.value == 'craft') {
    if (craftSelect.value.length <= 0) {
      Message.warning("请选择要删除的数据");
      return false
    }
    MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      let ids = craftSelect.value.map(item => {
        return item.id
      }).join(",")
      delCraft(ids).then(res => {
        if (res.code == 200) {
          Message.success(res.msg);
          handleChange(pageSize.value, pageNum.value);
        }
      })
    }).catch(() => {
      Message.info({
        type: 'info',
        message: '已取消删除'
      });
    })

  } else if (activeName.value == 'craftFlow') {
    if (craftFlowSelect.value.length <= 0) {
      Message.warning("请选择要修改的数据");
      return false
    }
    MessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      let ids = craftFlowSelect.value.map(item => {
        return item.id
      }).join(",")
      delCraftFlow(ids).then(res => {
        if (res.code == 200) {
          Message.success(res.msg);
          handleChange(pageSize.value, pageNum.value);
        }
      })
    }).catch(() => {
      Message.info({
        type: 'info',
        message: '已取消删除'
      });
    });
  }
}
const handleChange = (size, num) => {
  pageNum.value = num
  pageSize.value = size
  craftResult.value = []
  craftFlowResult.value = []
  getList()
}
const handleSelectionChange = (e) => {
  if (activeName.value == 'craft') {
    craftSelect.value = e
  } else if (activeName.value == 'craftFlow') {
    craftFlowSelect.value = e
  }
}
const checkBoxChange = (e) => {
  craftFlowForm.value.craftFlowId = e.map(item => {
    return item.id
  }).toString()
  craftFlowForm.value.craftFlowTxt = e.map(item => {
    return item.craftName
  }).join(" → ")
}
const handleRowClick = (row, column, event) => {
  craftTableRef.value.toggleRowSelection(row, column)
}
const handleFlowRowClick = (row, column, event) => {
  craftFlowTableRef.value.toggleRowSelection(row, column)
}
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__content, .el-tab-pane {
  height: calc(100% - 39px);
  transition: 1s;
}

::v-deep textarea {
  font-family: Arial;
}

.craftSelectBox {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
}

::v-deep .el-input.is-disabled .el-input__inner {
  color: #333;
}
</style>
