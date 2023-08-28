<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="设备编号">
        <el-input
          v-model="queryParams.no"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
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
        v-hasPermi="['system:device:add']">新增设备
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['system:device:edit']">编辑设备
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"
        v-hasPermi="['system:device:del']">删除设备
      </el-button>
    </div>
    <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize" :pageNum="queryParams.pageNum" :total="queryParams.count">
      <el-table highlight-current-row
        :data="tableDate"
        stripe
        border
        height="100%"
        ref="deviceTableRef"
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
       :min-width="item.width"
          :formatter="item.formatter">
        </el-table-column>
      </el-table>
    </slot-table>
    <el-dialog :title="dialogType=='add'?'新增设备':dialogType=='edit'?'编辑设备':''" :visible.sync="dialog" width="650px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form size="mini" :model="deviceForm" :rules="deviceFormRules" ref="deviceFormRef" label-width="120px" style="display: flex;flex-wrap: wrap;">
        <el-form-item label="设备编号" prop="no">
          <el-input style="width: 170px" v-model="deviceForm.no" placeholder="请输入设备编号"></el-input>
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input style="width: 170px" v-model="deviceForm.deviceName" placeholder="请输入设备名称"></el-input>
        </el-form-item>
        <el-form-item label="设备简称" prop="simpleName">
          <el-input style="width: 170px" v-model="deviceForm.simpleName" placeholder="请输入设备简称"></el-input>
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input style="width: 170px" v-model="deviceForm.brand" placeholder="请输入品牌"></el-input>
        </el-form-item>
        <el-form-item label="厂家" prop="manufactor">
          <el-input style="width: 170px" v-model="deviceForm.manufactor" placeholder="请输入厂家"></el-input>
        </el-form-item>
        <el-form-item label="工序" prop="actionProcess">
          <el-select style="width: 170px" v-model="deviceForm.actionProcess" clearable placeholder="请选择工序">
            <el-option
              v-for="item in craftList"
              :key="item.id"
              :label="item.craftName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="总功率（KW）" prop="totalPower">
          <el-input style="width: 170px" v-model="deviceForm.totalPower" placeholder="请输入总功率KW"></el-input>
        </el-form-item>
        <el-form-item label="重量（KG）" prop="weight">
          <el-input style="width: 170px" v-model="deviceForm.weight" placeholder="请输入重量KG"></el-input>
        </el-form-item>
        <el-form-item label="保修时间" prop="warrantyPeriod">
          <el-date-picker
            v-model="deviceForm.warrantyPeriod"
            style="width: 170px"
            type="date"
            placeholder="请选择保修时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="出厂时间" prop="deliveryDate">
          <el-date-picker
            v-model="deviceForm.deliveryDate"
            style="width: 170px"
            type="date"
            placeholder="请选择出厂时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注">
          <el-input style="width: 170px" v-model="deviceForm.remarks" type="textarea" size="mini" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="deviceForm.status">
            <el-radio :label="0">启用</el-radio>
            <el-radio :label="1">废置</el-radio>
          </el-radio-group>

<!--          <el-select style="width: 170px" v-model="deviceForm.status" clearable placeholder="请选择">-->
<!--            <el-option-->
<!--              :key="0"-->
<!--              label="已启用"-->
<!--              :value="0">已启用</el-option>-->
<!--            <el-option-->
<!--              :key="1"-->
<!--              label="未启用"-->
<!--              :value="1">未启用</el-option>-->
<!--          </el-select>-->
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
<script setup name="device">
import AppContent from "@/components/AppContent/index"
import SlotTable from "@/components/public/table/SlotTable";
import {getDeviceList,addDevice,editDevice,delDevice} from "@/api/system/device"
import {Message,MessageBox} from "element-ui";
import {ref,reactive,onMounted,getCurrentInstance,watch } from "vue";
import {getAllCraftList} from "@/api/system/craft";
const _this = getCurrentInstance()

onMounted(async ()=>{
  let { data } = await getAllCraftList()
  craftList.value = data
  handleQuery();
})
//**************Data***************
const queryParams = ref({
  no: "",
  deviceName:"",
  pageSize:20,
  pageNum:1,
  count:0
})
const tableDate = ref([])
const selectList = ref([])
const deviceTableRef = ref([])
const tableColumn = ref([
  {label: '设备编号', prop: 'no',width:'100'},
  {label: '设备名称', prop: 'deviceName',width:'150'},
  {label: '设备简称', prop: 'simpleName',width:'100'},
  {label: '设备品牌', prop: 'brand',width:'100'},
  {label: '厂家', prop: 'manufactor',width:'150'},
  {label: '工序', prop: 'actionProcess',width:'100',formatter:(row, column, cellValue)=>{
      let craftName = craftList.value.filter(item=>{
        return item.id == cellValue
      })[0]
      return craftName ? craftName.craftName :''
    }},
  {label: '总功率（KW）', prop: 'totalPower',width:'120'},
  {label: '重量（KG）', prop: 'weight',width:'100'},
  {label: '保修时间', prop: 'warrantyPeriod',width:'180'},
  {label: '出厂时间', prop: 'deliveryDate',width:'180'},
  {label: '创建人', prop: 'createdPerson',width:'100'},
  {label: '备注', prop: 'remarks',width:'150'},
  {label: '状态', prop: 'status',width:'100',formatter:(row, column, cellValue)=>{
      return cellValue == 0 ? '启用' : '废置'
  }}
])
const craftList = ref([])
const deviceForm = ref({
  no:"",
  deviceName:"",
  simpleName:"",
  brand:"",
  manufactor:"",
  actionProcess:"",
  totalPower:"",
  weight:"",
  warrantyPeriod:"",
  deliveryDate:"",
  status: 0,
  remarks:"",
})
watch(
  () => deviceForm.value.warrantyPeriod,
  (newVal, oldVal) => {
    if(deviceForm.value.warrantyPeriod instanceof Date){
      deviceForm.value.warrantyPeriod = newVal.toLocaleDateString().replaceAll("/","-")
    }
  },{
    deep: true
  });
watch(
  () => deviceForm.value.deliveryDate,
  (newVal, oldVal) => {
    if(deviceForm.value.deliveryDate instanceof Date){
      deviceForm.value.deliveryDate = newVal.toLocaleDateString().replaceAll("/","-")
    }
  },{
    deep: true
  });
const deviceFormRef = ref(null)
const deviceFormRules = ref({
  // no:[{ required: true, message: '请输入设备编号', trigger: 'blur' }],
  deviceName:[{ required: true, message: '请输入设备名称', trigger: 'blur' }],
//   simpleName:[{ required: true, message: '请输入设备简称', trigger: 'blur' }],
//   brand:[{ required: true, message: '请输入设备品牌', trigger: 'blur' }],
//   manufactor:[{ required: true, message: '请输入厂家', trigger: 'blur' }],
//   actionProcess:[{ required: true, message: '请选择工序', trigger: 'change' }],
//   totalPower:[{ required: true, message: '请输入总功率KW', trigger: 'blur' }],
//   weight:[{ required: true, message: '请输入重量KG', trigger: 'blur' }],
//   warrantyPeriod:[{ required: true, message: '请输入保修时间', trigger: 'blur' }],
//   deliveryDate:[{ required: true, message: '请输入出厂时间', trigger: 'blur' }],
//   status:[{ required: true, message: '请选择设备状态', trigger: 'change' }]
})
const dialogType = ref('add')
const dialog = ref(false)
const selectType = ref("")
//**************Methods****************
const handleQuery = () => {
  tableDate.value = []
  getDeviceList(queryParams.value).then(res=>{
    tableDate.value = res.data
    queryParams.value.count = res.count
  })
}
const handleChange = (size,num) => {
  queryParams.value.pageSize = size
  queryParams.value.pageNum = num
  tableDate.value=[]
  handleQuery();
}
const handleAdd = () => {
  deviceForm.value = {
    no:"",
    deviceName:"",
    simpleName:"",
    brand:"",
    manufactor:"",
    actionProcess:"",
    totalPower:"",
    weight:"",
    warrantyPeriod:"",
    deliveryDate:"",
    status:0,
    remarks:"",
  }
  dialogType.value = 'add'
  dialog.value = true
}
const handleUpdate = () => {
  if(selectList.value.length <= 0){
    Message.warning("请选择要修改的数据");
    return false
  }
  if(selectList.value.length > 1){
    Message.warning("请选择一条数据进行修改");
    return false
  }
  deviceForm.value = JSON.parse(JSON.stringify(selectList.value[0]))
  let currentCraft = craftList.value.filter(item=>{
    return item.id == deviceForm.value.actionProcess
  })
  if(currentCraft <= 0){
    deviceForm.value.actionProcess = ""
  }
  dialogType.value = 'edit'
  dialog.value = true
}
const handleDelete = () => {
  if(selectList.value.length <= 0){
    Message.warning("请选择要删除的数据");
    return false
  }
  MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let ids = selectList.value.map(item=>{
      return item.id
    }).join(",")
    delDevice(ids).then(res=>{
      if(res.code == 200){
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
const handleRowClick = (row, column, event)=>{
  deviceTableRef.value.toggleRowSelection(row, column)
}
const saveSubmit = ()=>{
  console.log(deviceForm.value)
  deviceFormRef.value.validate((valid) => {
    if (valid) {
      if (dialogType.value == 'add') {
        addDevice(deviceForm.value).then(res=>{
          Message.success(res.msg)
          handleQuery();
          dialog.value = false
        })
      }else if(dialogType.value == 'edit'){
        let {id , ...data} = deviceForm.value
        editDevice(id,data).then(res=>{
          Message.success(res.msg)
          handleQuery();
          dialog.value = false
        })
      }
    }else{
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
.onlyOneChild{
  display: flex;
  justify-content: center;
  align-items: center;
  .el-form-item{
    width: 80%;
    justify-content: center;
    margin-right: 0;
  }
}
</style>
