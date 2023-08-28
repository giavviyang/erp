<template>
  <div class="app-container">
    <MainFlexibleTreeRightClick :data="treeData"
                                :defaultProps="TreeDefaultProps"
                                @handleNodeClick="handleNodeClick"
                                @rightClick="rightClick">
      <VueContextMenu
        :target="contextMenuTarget"
        :show="contextMenuVisible"
        class="right-menus"
        @update:show="(show) => contextMenuVisible = show" slot="RightClick">
        <a @click="addType" v-hasPermi="['system:dict:addType']">新增类型</a>
        <a @click="editType" v-hasPermi="['system:dict:editType']">编辑类型</a>
        <a @click="delType" v-hasPermi="['system:dict:delType']">删除类型</a>
      </VueContextMenu>
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn" @submit.native.prevent>
          <el-form-item label="字典标签" prop="dictLabel">
            <el-input
              v-model="queryParams.dictLabel"
              placeholder="请输入字典标签"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            v-hasPermi="['system:dict:add']"
            @click="handleAdd"
          >新增字典
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            v-hasPermi="['system:dict:edit']"
            @click="handleUpdate"
          >编辑字典
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            v-hasPermi="['system:dict:del']"
            @click="handleDelete"
          >删除字典
          </el-button>
        </div>
        <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize"
                    :pageNum="queryParams.pageNum" :total="queryParams.count">
          <el-table highlight-current-row
            :data="tableDate"
            stripe
            border
            height="100%"
            ref="templateTableRef"
            @row-click="handleRowClick"
            @selection-change="handleSelectionChange" slot="table">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column
              type="index"
              label="序号"
              width="50">
              <template slot-scope="scope">
                <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-for="item in tableColumn"
              :key="item.prop"
              :prop="item.prop"
              :label="item.label"
              :formatter="item.formatter" show-overflow-tooltip>
              <template slot-scope="scope">
                <span v-if="item.prop != 'status'">{{ scope.row[`${item.prop}`] }}</span>
                <dict-tag v-else :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
              </template>
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </MainFlexibleTreeRightClick>
    <el-dialog :title="typeDialogType=='add'?'新增字典类型':typeDialogType=='edit'?'编辑字典类型':''"
               :visible.sync="typeDialog" width="500px" class="dialog-style">
      <el-form ref="dictTypeFormRef" :model="dictTypeForm" :rules="dictTypeRules" label-width="80px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="dictTypeForm.dictName" placeholder="请输入字典名称"/>
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="dictTypeForm.dictType" placeholder="请输入字典类型"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dictTypeForm.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dictTypeForm.remark" type="textarea" size="mini" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="mini">确 定</el-button>
        <el-button @click="typeDialog = false" size="mini">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="dialogType=='add'?'新增字典数据':dialogType=='edit'?'编辑字典数据':''" :visible.sync="dialog"
               width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form ref="dictFormRef" :model="dictForm" :rules="dictRules" label-width="80px">
        <el-form-item label="字典类型">
          <el-input v-model="dictForm.dictType" :disabled="true"/>
        </el-form-item>
        <el-form-item label="数据标签" prop="dictLabel">
          <el-input v-model="dictForm.dictLabel" placeholder="请输入数据标签"/>
        </el-form-item>
        <el-form-item label="数据键值" prop="dictValue">
          <el-input v-model="dictForm.dictValue" placeholder="请输入数据键值"/>
        </el-form-item>
        <el-form-item v-if="false" label="样式属性" prop="cssClass">
          <el-input v-model="dictForm.cssClass" placeholder="请输入样式属性"/>
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="dictForm.dictSort" :min="0" style="width: 100%;"/>
        </el-form-item>
        <el-form-item v-if="false" label="回显样式" prop="listClass">
          <el-select v-model="dictForm.listClass">
            <el-option
              v-for="item in listClassOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dictForm.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dictForm.remark" type="textarea" size="mini" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dictSubmitForm" size="mini">确 定</el-button>
        <el-button @click="dialog = false" size="mini">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  dicts: ['sys_normal_disable']
}
</script>
<script setup>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {component as VueContextMenu} from '@xunlei/vue-context-menu';
import SlotTable from "@/components/public/table/SlotTable";
import MainFlexibleTreeRightClick from "@/components/public/MainBody/MainFlexibleTreeRightClick";
import {
  listType,
  addDictType,
  updateDictType,
  delDictType
} from "@/api/system/dict/type";
import {Message, MessageBox} from "element-ui";
import {ref, onMounted} from "vue";
import {addData, listData, updateData, delData} from "@/api/system/dict/data";

onMounted(() => {
  getTypeList();
  getList()
})
const templateTableRef = ref(null)
const treeData = ref([{id: 0, dictName: "字典类型"}])
const TreeDefaultProps = ref({
  children: 'children',
  label: 'dictName'
})
const contextMenuTarget = ref("")
const contextMenuVisible = ref(false)
const typeDialogType = ref("add")
const typeDialog = ref(false)
const topNodeInfo = ref({})
const currentNodeInfo = ref({})
const dictTypeFormRef = ref(null)
const dictTypeForm = ref({
  dictName: ""
})
const dictTypeRules = ref({
  dictName: [
    {required: true, message: "字典名称不能为空", trigger: "blur"}
  ],
  dictType: [
    {required: true, message: "字典类型不能为空", trigger: "blur"}
  ]
})
const queryParams = ref({
  pageNum: 1,
  pageSize: 20,
  count: 0,
  dictName: undefined,
  dictType: undefined,
  status: undefined
})
const tableDate = ref([])
const tableColumn = ref([
  {label: '字典编码', prop: 'dictCode'},
  {label: '字典标签', prop: 'dictLabel'},
  {label: '字典键值', prop: 'dictValue'},
  {label: '字典排序', prop: 'dictSort'},
  {label: '状态', prop: 'status'},
  {label: '备注', prop: 'remark'},
  {label: '创建时间', prop: 'createTime'}
])
const dialogType = ref("add")
const dialog = ref(false)
const dictFormRef = ref(null)
const dictForm = ref({
  dictCode: undefined,
  dictLabel: undefined,
  dictValue: undefined,
  cssClass: undefined,
  listClass: 'default',
  dictSort: 0,
  status: "0",
  remark: undefined,
  dictType: ""
})
const dictRules = ref({
  dictLabel: [
    {required: true, message: "数据标签不能为空", trigger: "blur"}
  ],
  dictValue: [
    {required: true, message: "数据键值不能为空", trigger: "blur"}
  ],
  dictSort: [
    {required: true, message: "数据顺序不能为空", trigger: "blur"}
  ]
})
const listClassOptions = ref([
  {
    value: "default",
    label: "默认"
  },
  {
    value: "primary",
    label: "主要"
  },
  {
    value: "success",
    label: "成功"
  },
  {
    value: "info",
    label: "信息"
  },
  {
    value: "warning",
    label: "警告"
  },
  {
    value: "danger",
    label: "危险"
  }
])
const selectNode = ref(undefined)
const selectList = ref([])


/*tree右键添加数据*/
const rightClick = (event, object, value, element) => {
  currentNodeInfo.value = object
  if (!Array.isArray(value.parent.data)) {
    topNodeInfo.value = value.parent.data
  }
  contextMenuVisible.value = true;// 让菜单显示
  const ele = document.querySelector('.right-menus');//绑定样式
  ele.style.position = 'fixed';
  ele.style.top = `${event.clientY}px`;
  ele.style.left = `${event.clientX + 10}px`; //根据鼠标点击位置设置菜单出现
}
const getTypeList = () => {
  if (treeData.value.length <= 1) {
    listType().then(res => {
      if (res.code == 200) {
        treeData.value = [{id: 0, dictName: "字典类型", children: res.data}]
      }
    })
  }
}
const reset = () => {
  dictTypeForm.value = {
    dictId: undefined,
    dictName: undefined,
    dictType: undefined,
    status: "0",
    remark: undefined
  }
}
const addType = () => {
  contextMenuVisible.value = false
  let {id} = currentNodeInfo.value
  if (id != 0) {
    Message.warning("字典类型限制一级，只能在根类型新增")
    return;
  }
  typeDialogType.value = "add"
  reset();
  typeDialog.value = true
}
const editType = () => {
  contextMenuVisible.value = false
  let {id} = currentNodeInfo.value
  if (id == 0) {
    Message.warning("根节点无法修改")
    return;
  }
  typeDialogType.value = "edit"
  dictTypeForm.value = JSON.parse(JSON.stringify(currentNodeInfo.value))
  typeDialog.value = true
}
const delType = () => {
  contextMenuVisible.value = false
  let {id} = currentNodeInfo.value
  if (id == 0) {
    Message.warning("根节点无法删除")
    return;
  }
  MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let {dictId} = currentNodeInfo.value
    delDictType(dictId).then(res => {
      if (res.code == 200) {
        Message.success(res.msg)
        treeData.value = [{id: 0, dictName: "字典类型"}]
        getTypeList();
      }
    })
  }).catch(() => {
    Message.info({
      type: 'info',
      message: '已取消删除'
    });
  });
}
const submitForm = () => {
  dictTypeFormRef.value.validate(valid => {
    if (valid) {
      if (dictTypeForm.value.dictId != undefined) {
        updateDictType(dictTypeForm.value).then(response => {
          Message.success("修改成功");
          typeDialog.value = false;
          getTypeList();
        });
      } else {
        addDictType(dictTypeForm.value).then(response => {
          Message.success("新增成功");
          typeDialog.value = false;
          getTypeList()
        });
      }
    }
  });
}
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}
const handleChange = (pageSize, pageNum) => {
  queryParams.value.pageSize = pageSize
  queryParams.value.pageNum = pageNum
  tableDate.value=[]
  getList()
}
const handleSelectionChange = (e) => {
  selectList.value = e
}
const getList = () => {
  listData(queryParams.value).then(response => {
    tableDate.value = response.rows
    queryParams.value.count = response.total
  });
}
const handleAdd = () => {
  if (!selectNode.value) {
    Message.warning("选择字典类型后，新增字典数据")
    return false
  }
  dictForm.value = {
    dictCode: undefined,
    dictLabel: undefined,
    dictValue: undefined,
    cssClass: undefined,
    listClass: 'default',
    dictSort: 0,
    status: "0",
    remark: undefined,
    dictType: dictForm.value.dictType
  }

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
  dictForm.value = JSON.parse(JSON.stringify(selectList.value[0]))
  dialogType.value = "edit"
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
      return item.dictCode
    }).join(",")
    delData(ids).then(res => {
      if (res.code == 200) {
        Message.success(res.msg);
        handleChange(queryParams.value.pageSize, 1);
      }
    })
  }).catch(() => {
    Message.info({
      type: 'info',
      message: '已取消删除'
    });
  });
}
const handleNodeClick = (e) => {
  if (e.dictType) {
    queryParams.value.dictType = e.dictType
    queryParams.value.pageNum = 1
    selectNode.value = e
    dictForm.value.dictType = e.dictType
    getList()
  } else {
    queryParams.value.dictType = undefined
    queryParams.value.pageNum = 1
    selectNode.value = undefined
    dictForm.value.dictType = ""
    getList()
  }
}
const dictSubmitForm = () => {
  dictFormRef.value.validate(valid => {
    if (valid) {
      if (dictForm.value.dictCode != undefined) {
        updateData(dictForm.value).then(response => {
          Message.success("修改成功");
          dialog.value = false;
          getList()
        });
      } else {
        addData(dictForm.value).then(response => {
          Message.success("新增成功");
          dialog.value = false;
          getList()
        });
      }
    }
  });
}
const handleRowClick = (row, column, event) => {
  templateTableRef.value.toggleRowSelection(row, column)
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
