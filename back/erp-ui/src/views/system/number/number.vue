<template>
  <div style="width: 100%;height: 100%;">
    <slot-table style="height: 100%;" class="rightTable orderTable" @handleChange="handleChange" :pageSize="queryParams.pageSize" :pageNum="queryParams.pageNum"
                :total="queryParams.total">
      <el-table highlight-current-row
        v-loading="tableLoading"
        :data="numberList"
        border
        style="width: 100%"
        height="100%"
        ref="numberTableRef"
        @row-click="handleRowClick"
        slot="table">
        <el-table-column
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-for="item in numberColumn"
          :key="item.label"
          :label="item.label"
          :prop="item.prop"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="操作" width="100" v-hasPermi="['system:number:edit']" show-overflow-tooltip class-name="operation">
          <template slot-scope="scope">
            <el-button
              size="mini" type="text"
              @click="handleEdit(scope.row)">
              编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </slot-table>
    <el-dialog title="编辑编号规则" :visible.sync="numberDialog" width="800px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
    <el-form size="mini" :model="numberForm" :rules="numberFormRules" ref="numberFormRef" label-width="100px" style="display: flex;flex-wrap: wrap;">
      <el-form-item label="编号名称" prop="ruleName">
        <el-input style="width: 280px" v-model="numberForm.ruleName" placeholder="请输入编号名称" disabled></el-input>
      </el-form-item>
      <el-form-item label="编号描述" prop="ruleDescribe">
        <el-input style="width: 280px;color:#333;" v-model="numberForm.ruleDescribe" placeholder="请输入编号描述" ></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" style="width: 660px;color:#333;" v-model="numberForm.remarks" size="mini" placeholder="请输入内容" ></el-input>
      </el-form-item>
      <el-card style="width: 100%">
        <el-form-item label="编号前缀" prop="rulePrefix">
          <el-input @input="numberChange" style="width: 140px" v-model="numberForm.rulePrefix" placeholder="请输入编号名称"></el-input>
        </el-form-item>
        <el-form-item label="编号中部" prop="ruleContent">
          <el-select @change="numberChange" style="width: 140px" v-model="numberForm.ruleContent" clearable placeholder="请选择编号中部">
            <el-option
              v-for="item in dict.type.sys_number_center"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编号后缀" prop="ruleSuffix">
          <el-select @change="numberChange" style="width: 140px" v-model="numberForm.ruleSuffix" clearable placeholder="请选择编号后缀">
            <el-option
              v-for="item in dict.type.sys_number_suffix"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="前中连接符" prop="frontConnection">
          <el-select @change="numberChange" style="width: 140px" v-model="numberForm.frontConnection" clearable placeholder="请选择前中连接符">
            <el-option
              key="+"
              label="+"
              value="+">
            </el-option>
            <el-option
              key="-"
              label="-"
              value="-">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="中后连接符" prop="rearConnection">
          <el-select @change="numberChange" style="width: 140px" v-model="numberForm.rearConnection" clearable placeholder="请选择中后连接符">
            <el-option
              key="+"
              label="+"
              value="+">
            </el-option>
            <el-option
              key="-"
              label="-"
              value="-">
            </el-option>
          </el-select>
        </el-form-item>
        <div style="width: 100%;">
          编号预览：{{numberForm.ruleDisplay}}
        </div>
      </el-card>
    </el-form>
    <span slot="footer" class="dialog-footer">
            <el-button type="primary" size="mini" @click="numberSubmit()">保存</el-button>
            <el-button size="mini" @click="numberDialog = false">取消</el-button>
          </span>
  </el-dialog>
  </div>
</template>

<script>
export default {
  dicts: ['sys_number_center','sys_number_suffix']
}
</script>
<script setup>
import SlotTable from "@/components/public/table/SlotTable";
import {getNumber,editNumber} from "@/api/system/number";
import {ref,onMounted,getCurrentInstance } from "vue";
import {Message,MessageBox} from "element-ui";

onMounted(()=>{
  getNumberList()
})

const _this= getCurrentInstance().proxy

/** data */
const numberColumn = ref([
  {label: '编号名称', prop: 'ruleName'},
  {label: '编号生产规则', prop: 'ruleDisplay'},
  {label: '描述', prop: 'ruleDescribe'},
  {label: '备注', prop: 'remarks'}
]);
const queryParams = ref({
  total:0,
  pageNum:1,
  pageSize:20
})
const tableLoading = ref(false)
const numberList = ref([])
const codeSelect = ref([])
const numberTableRef = ref(null)
const numberDialog = ref(false)
const numberForm = ref({
  ruleName:"",
  ruleDescribe: "",
  remarks:"",
  rulePrefix:"",
  ruleContent:"",
  ruleSuffix:"",
  frontConnection:"",
  rearConnection:"",
  ruleDisplay:""
})
const numberFormRules = ref({
  ruleName:[{ required: true, message: '请输入编号名称', trigger: 'blur' }],
  ruleDescribe: [{ required: true, message: '请输入编号描述', trigger: 'blur' }],
  rulePrefix:[{ required: true, message: '请输入编号前缀', trigger: 'blur' }],
  ruleContent:[{ required: true, message: '请选择编号中部', trigger: 'change' }],
  ruleSuffix:[{ required: true, message: '请选择编号后缀', trigger: 'change' }],
})
const numberFormRef = ref(null)

/** methods */
const handleChange = (size,num) => {
  queryParams.value.pageNum = num
  queryParams.value.pageSize = size
  numberList.value = []
  getNumberList()
}
const handleSelectionChange = (e) => {
  codeSelect.value = e
}
const handleRowClick = (row, column, event)=>{
  numberTableRef.value.toggleRowSelection(row, column)
}
const getNumberList = () => {
  getNumber(queryParams.value).then(res=>{
    numberList.value = res.data
    queryParams.value.total = res.count
  })
}
const numberChange = ()=>{
  numberForm.value.ruleDisplay =
    `${numberForm.value.rulePrefix}${
      numberForm.value.frontConnection
    }${getYmd(numberForm.value.ruleContent)}${
      numberForm.value.rearConnection
    }${
      new Array(numberForm.value.ruleSuffix!="" ? Number(numberForm.value.ruleSuffix) : 0).fill('0').join("")
    }`
}
const getYmd = (type)=>{
  type = type.toLowerCase();
  let date = _this.parseTime(new Date()).split(" ")[0].split("-").join("")
  if(type == 'yymmdd'){
    return date.substr(2)
  }else if(type == 'yyyymmdd'){
    return date
  }else {
    return ""
  }
}
const handleEdit = (row)=>{
  Object.assign(numberForm.value,row)
  numberForm.value.ruleSuffix =  numberForm.value.ruleSuffix+""
  numberChange();
  numberDialog.value = true
}
const numberSubmit = ()=>{
  numberFormRef.value.validate((valid) => {
    if (valid){
      editNumber(numberForm.value.id,numberForm.value).then(res=>{
        Message.success(res.msg)
        numberDialog.value = false
        handleChange(20,1);
      })
    }
    return false
  })
}
</script>
