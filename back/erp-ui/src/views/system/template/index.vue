<template>
  <div class="app-container">
    <MainFlexibleTreeRightClick :data="treeData"
                                :defaultProps="TreeDefaultProps"
                                @handleNodeClick="handleNodeClick"
                                @rightClick="rightClick" >
<!--      <VueContextMenu
        :target="contextMenuTarget"
        :show="contextMenuVisible"
        class="right-menus"
        @update:show="(show) => contextMenuVisible = show" slot="RightClick">
        <a @click="addType" v-hasPermi="['system:template_type:add']">新增类型</a>
        <a @click="editType" v-hasPermi="['system:template_type:edit']">编辑类型</a>
        <a @click="delType" v-hasPermi="['system:template_type:del']">删除类型</a>
      </VueContextMenu>-->
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn" @submit.native.prevent>
          <el-form-item label="模板名称" prop="deptName">
            <el-input
              v-model="queryParams.templateName"
              placeholder="请输入模板名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            v-hasPermi="['system:template:add']"
            @click="handleAdd"
          >新增模板
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            v-hasPermi="['system:template:edit']"
            @click="handleUpdate"
          >编辑模板
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            v-hasPermi="['system:template:del']"
            @click="handleDelete"
          >删除模板
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-download"
            size="mini"
            @click="handleDownFile"
          >下载
          </el-button>
        </div>
        <slot-table class="rightTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum" :total="count">
          <el-table highlight-current-row
            :data="tableDate"
            stripe
            border
            height="100%"
            ref="templateTableRef"
            @selection-change="handleSelectionChange"
            @row-click="handleRowClick" slot="table">
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
              v-for="item in tableColumn"
              :key="item.prop"
              :prop="item.prop"
              :label="item.label"
              :formatter="item.formatter" show-overflow-tooltip>
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </MainFlexibleTreeRightClick>
    <el-dialog :title="typeDialogType=='add'?'新增模板类型':typeDialogType=='edit'?'编辑模板类型':''" :visible.sync="typeDialog" width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form size="mini" :model="typeForm" :rules="typeFormRules" ref="typeFormRef" label-width="100px">
        <el-form-item label="上级类型" prop="tid">
          <span v-if="typeDialogType == 'add'">{{currentNodeInfo?currentNodeInfo.typeName:'无'}}</span>
          <span v-else>{{typeForm.tidName}}</span>
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input style="width: 350px" v-model="typeForm.typeName" placeholder="请输入类型名称"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="typeSubmit('save')">保存</el-button>
        <el-button size="mini" @click="typeSubmit('cancel')">取消</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="dialogType=='add'?'新增模板':dialogType=='edit'?'编辑模板':''" :visible.sync="dialog" width="600px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form size="mini" :model="templateForm" :rules="templateFormRules" ref="templateFormRef" label-width="100px" style="display: flex;flex-wrap: wrap;">
        <el-form-item label="模板名称" prop="templateName">
          <el-input style="width: 170px" v-model="templateForm.templateName" placeholder="请输入模板名称"></el-input>
        </el-form-item>
        <el-form-item label="模板类型" prop="typeId">
          <el-cascader
            style="width: 170px"
            placeholder="请选择模板类型"
            v-model="templateForm.typeId"
            @change="cascaderChange"
            :props="{value:'id',label:'typeName'}"
            :show-all-levels="false"
            :options="treeData" clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="模板文件" prop="fileList">
          <el-upload
            style="display: flex;align-items: center;"
            :action="uploadUrl"
            :headers = "header"
            :on-change="handleUploadChange"
            :on-remove="handleUploadRemove"
            :on-success="handleUploadSuccess"
            ref="uploadFileRef"
            :file-list="templateForm.fileList">
            <el-button size="mini" type="primary" style="margin-right: 20px;">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input

            style="width: 440px;"

            v-model="templateForm.remarks" type="textarea" size="mini" placeholder="请输入内容">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="templateSave">保存</el-button>
        <el-button size="mini" @click="dialog = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {component as VueContextMenu} from '@xunlei/vue-context-menu';
import SlotTable from "@/components/public/table/SlotTable";
import MainFlexibleTreeRightClick from "@/components/public/MainBody/MainFlexibleTreeRightClick";
import {getTemplateTypeList,addTemplateType,editTemplateType,delTemplateType,getTemplateList,addTemplate,getTemplate,editTemplate,delTemplate,downloadFile} from "@/api/system/template";
import {Message,MessageBox} from "element-ui";
import { groupBy } from "@/utils/groupBy";
import {ref,onMounted} from "vue";
import {getToken} from "@/utils/auth";
import {saveFile} from "@/utils/saveFile"
import {getDefaultTree} from "@/utils/getDefaultTree";

onMounted(()=>{
  getTypeList();
  getList();
})

const uploadUrl = process.env.VUE_APP_BASE_API+'/system/template/upload'
const header = ref({'Authorization' : 'Bearer ' + getToken()})
const treeData = ref([{id:0,typeName:"模板类型"}])
const typeList = ref([])
const TreeDefaultProps = ref({
  children: 'children',
  label: 'typeName'
})
const templateTableRef = ref(null)
const contextMenuTarget = ref("")
const contextMenuVisible = ref(false)
const typeDialogType = ref("add")
const typeDialog = ref(false)
const dialogType = ref("add")
const dialog = ref(false)
const topNodeInfo = ref({})
const currentNodeInfo = ref({})
const typeFormRef = ref(null)
const uploadFileRef = ref(null)
const selectList = ref([])
const typeForm = ref({
  typeName:""
})
const typeFormRules = ref({
  typeName: [
    { required: true, message: '请输入类型名称', trigger: 'blur' }
  ]})
const queryParams = ref({
  templateName:""
})
const count = ref(0);
const pageNum = ref(1);
const pageSize = ref(20);
const tableDate = ref([])
const tableColumn = ref([
  {label: '模板名称', prop: 'templateName'},
  {label: '模板类型', prop: 'typeName'},
  {label: '备注', prop: 'remarks'}])
const templateForm = ref({
  templateName:"",
  typeId:"",
  templateFile:"",
  typeName:"",
  remarks:"",
  fileList:[],
})
const validateFile = (r, value, callback) => {
  if (templateForm.value.fileList.length <= 0) {
    callback(new Error('请上传模板文件'));
  } else {
    callback();
  }
};
const templateFormRules = ref({
  templateName: [
    { required: true, message: '请输入模板名称', trigger: 'blur' }
  ],
  typeId: [
    // { required: true, message: '请选择模板类型', trigger: 'change' }
    { required: true, message: '请选择模板类型', trigger: 'blur' }
  ],
  fileList: [
    { validator: validateFile,trigger: 'change' ,required:true }
  ]})
const templateFormRef = ref(null)
const selectNode = ref({})

/*tree右键添加数据*/
const rightClick = (event, object, value, element)=>{
  return false;
  currentNodeInfo.value = object
  if(!Array.isArray(value.parent.data)){
    topNodeInfo.value = value.parent.data
  }
  contextMenuVisible.value = true;// 让菜单显示
  const ele = document.querySelector('.right-menus');//绑定样式
  ele.style.position = 'fixed';
  ele.style.top = `${event.clientY}px`;
  ele.style.left = `${event.clientX + 10}px`; //根据鼠标点击位置设置菜单出现
}
const getTypeList = ()=>{
  if(treeData.value.length <= 1){
    getTemplateTypeList().then(res=>{
      if(res.code == 200){
        typeList.value = res.data
        treeData.value = [{id:0,typeName:"模板类型",children:groupBy(res.data,"tid")}]
        console.log(treeData)
      }
    })
  }
}
const addType = () => {
  contextMenuVisible.value = false
  typeDialogType.value = "add"
  typeForm.value = {
    typeName:""
  }
  typeDialog.value = true
}
const editType = ()=>{
  contextMenuVisible.value = false
  let {id,typeName,tid} = currentNodeInfo.value
  let {typeName:tidName = '无'} = topNodeInfo.value
  if(id == 0){
    Message.warning("无法操作根节点");
    return false
  }
  typeDialogType.value = "edit"
  typeForm.value = {
    typeName,
    id,
    tid,
    tidName
  }
  typeDialog.value = true
}
const delType = () =>{
  contextMenuVisible.value = false
  let {id} = currentNodeInfo.value
  if(id == 0){
    Message.warning("无法操作根节点");
    return false
  }
  contextMenuVisible.value = false
  MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    delTemplateType(id).then(res=>{
      if(res.code == 200){
        Message.success(res.msg)
        treeData.value = [{id:0,typeName:"模板类型"}]
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
const typeSubmit = (type)=>{
  if(type == 'save'){
    if(typeDialogType.value == 'add'){
    let { id = 0 } = currentNodeInfo.value??{}
    let { typeName } = typeForm.value
    addTemplateType({tid:id,typeName}).then(res=>{
      if(res.code == 200){
        Message.success(res.msg);
        treeData.value = [{id:0,typeName:"模板类型"}]
        typeDialog.value = false
        getTypeList()
      }
    })
    }else if(typeDialogType.value == 'edit'){
      let { id,typeName,tid } = typeForm.value
      editTemplateType(id,{typeName,tid}).then(res=>{
        if(res.code == 200){
          Message.success(res.msg);
          treeData.value = [{id:0,typeName:"模板类型"}]
          typeDialog.value = false
          getTypeList()
        }
      })
    }
  }else if(type == 'cancel'){
    typeDialog.value = false
  }
}
const getList = ()=>{
  getTemplateList(queryParams.value).then(res=>{
    if(res.code == 200){
      tableDate.value = tableDate.value.concat(res.data)
      count.value = res.count
    }
  })
}
const handleChange = (size,num) => {
  pageNum.value = num
  pageSize.value = size
  tableDate.value = []
  getList()
}
const handleQuery = ()=>{
  tableDate.value = []
  getList();
}
const handleAdd = ()=>{
  templateForm.value = {
    templateName:"",
    typeId:selectNode.value?selectNode.value.id:"",
    file:"",
    typeName:"",
    remarks:""
  }
  if(selectNode.value != undefined && selectNode.value.hasOwnProperty("id")) {
    templateForm.value.typeId = selectNode.value.id
  }else{
    let defaultVal = getDefaultTree(treeData.value)
    templateForm.value.typeId = defaultVal
  }
  console.log(templateForm.value)
  templateForm.value.fileList = []
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

  getTemplate(selectList.value[0].id).then(res=>{
    if(res.code == 200){
      templateForm.value = res.data
      dialogType.value = 'edit'
      templateForm.value.fileList = [{name:res.data.templateFile.fileName}]
      dialog.value = true
    }
  })
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
    delTemplate(ids).then(res=>{
      if(res.code == 200){
        Message.success(res.msg);
        handleChange(pageSize.value,pageNum.value);
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
const handleUploadSuccess = (file) => {
  if(file.code == 200){
    templateForm.value.templateFile = file.data
    Message.success(file.msg)
  }else{
    Message.error(file.msg)
  }
}
const templateSave = () => {
  templateFormRef.value.validate((valid) => {
    if (valid) {
      if(dialogType.value == 'add'){
        let {typeId,templateName,templateFile,remarks} = templateForm.value
        let tid = Array.isArray(typeId)?typeId[typeId.length-1]:typeId;
        let typeName = typeList.value.filter((item)=>{
          return tid == item.id
        })[0].typeName
        addTemplate({typeName,typeId:tid,templateName,templateFile,remarks}).then(res=>{
          if(res.code == 200){
            Message.success(res.msg)
            dialog.value = false
            handleChange(20,1)
          }
        })
        return false
      }
      if (dialogType.value == 'edit'){
        let {id,typeId,templateName,templateFile,remarks} = templateForm.value
        let tid = Array.isArray(typeId)?typeId[typeId.length-1]:typeId;
        let typeName = typeList.value.filter((item)=>{
          return tid == item.id
        })[0].typeName
        editTemplate(id,{typeName,typeId:tid,templateName,templateFile,remarks}).then(res=>{
          if(res.code == 200){
            Message.success(res.msg)
            dialog.value = false
            handleChange(20,1)
          }
        })
        return false
      }
    }else{
      return false;
    }
  })
}
const handleUploadChange = (_, newfileList)=>{
  uploadFileRef.value.uploadFiles = newfileList.slice(-1);
  templateForm.value.fileList = newfileList.slice(-1);
}
const handleUploadRemove=()=>{
  templateForm.value.fileList = []
  uploadFileRef.value.uploadFiles = []
}
const handleDownFile = ()=>{
  if(selectList.value.length <= 0){
    Message.warning("请选择要下载的模板");
    return false
  }
  downloadFile(selectList.value[0].id).then(res=>{
    saveFile(res)
  })
}
const cascaderChange = (e) => {
  console.log(e)
}
const handleRowClick = (row, column, event)=>{
  templateTableRef.value.toggleRowSelection(row, column)
}
const handleNodeClick = (e)=>{
  if(e.id != 0){
    queryParams.value.typeId = e.id
    queryParams.value.pageNum = 1
    selectNode.value = e
    tableDate.value = []
    getList()
  }else{
    queryParams.value.typeId = undefined
    queryParams.value.pageNum = 1
    selectNode.value = undefined
    tableDate.value = []
    getList()
  }
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
::v-deep .el-upload-list{
  width: 350px;
}
::v-deep .el-upload-list__item{
  transition: all 0s cubic-bezier(0.55, 0, 0.1, 1) !important;
}
</style>
