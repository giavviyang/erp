<template>
  <div class="app-container">
    <MainFlexibleTreeRightClick :data="treeData"
                                :defaultProps="TreeDefaultProps"
                                @handleNodeClick="handleNodeClick"
                                @rightClick="rightClick" >
      <VueContextMenu
        :target="contextMenuTarget"
        :show="contextMenuVisible"
        class="right-menus"
        @update:show="(show) => contextMenuVisible = show" slot="RightClick">
        <a @click="addType" v-hasPermi="['system:mill_type:add']">新增类型</a>
        <a @click="editType" v-hasPermi="['system:mill_type:edit']">编辑类型</a>
        <a @click="delType" v-hasPermi="['system:mill_type:del']">删除类型</a>
      </VueContextMenu>
      <div slot="mainRight" class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">

          <el-form-item label="厂家名称" prop="customerName">
            <el-input
              v-model="queryParams.millName"
              placeholder="请输入厂家名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="厂家编号" prop="number">
            <el-input
              v-model="queryParams.no"
              placeholder="请输入厂家编号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input
              v-model="queryParams.phone"
              placeholder="请输入联系电话"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
<!--          <el-form-item>-->
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
<!--          </el-form-item>-->
        </el-form>
        <div class="btn">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            v-hasPermi="['system:mill:add']"
            @click="handleAdd"
          >新增厂家
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            v-hasPermi="['system:mill:edit']"
            @click="handleUpdate"
          >编辑厂家
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            v-hasPermi="['system:mill:del']"
            @click="handleDelete"
          >删除厂家
          </el-button>
        </div>
        <slot-table class="rightTable" @handleChange="handleChange" :pageSize="queryParams.pageSize" :pageNum="queryParams.pageNum" :total="queryParams.count">
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
                <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              v-for="item in tableColumn"
              :key="item.prop"
              :prop="item.prop"
              :label="item.label"
            :min-width="item.width">
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </MainFlexibleTreeRightClick>
    <el-dialog :title="typeDialogType=='add'?'新增厂家类型':typeDialogType=='edit'?'编辑厂家类型':''" :visible.sync="typeDialog" width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
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
    <el-dialog :title="dialogType=='add'?'新增厂家':dialogType=='edit'?'编辑厂家':''" :visible.sync="dialog" width="600px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form  size="mini" :model="millForm" :rules="millFormRules" ref="millFormRef" label-width="100px" style="display: flex;flex-wrap: wrap;">
        <el-form-item label="厂家名称" prop="millName">
          <el-input style="width: 170px" v-model="millForm.millName" placeholder="请输入厂家名称"></el-input>
        </el-form-item>
        <el-form-item label="厂家类型" prop="typeId">
          <el-cascader
            style="width: 170px"
            placeholder="请选择厂家类型"
            v-model="millForm.typeId"
            @change="cascaderChange"
            :props="{value:'id',label:'typeName',checkStrictly: true}"
            :show-all-levels="false"
            :options="treeData" clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="厂家编号" prop="no">
          <el-input style="width: 170px" v-model="millForm.no" placeholder="请输入厂家编号"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="person">
          <el-input style="width: 170px" v-model="millForm.person" placeholder="请输入联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input style="width: 170px" v-model="millForm.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="固话" prop="tel">
          <el-input style="width: 170px" v-model="millForm.tel" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱" prop="mail">
          <el-input style="width: 170px" v-model="millForm.mail" placeholder="请输入电子邮箱"></el-input>
        </el-form-item>
        <el-form-item label="传真" prop="fax">
          <el-input style="width: 170px" v-model="millForm.fax" placeholder="请输入传真"></el-input>
        </el-form-item>
        <el-form-item label="厂家地址" prop="address">
          <el-input style="width: 440px" v-model="millForm.address" placeholder="请输入厂家地址"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input
            type="textarea"
            style="width: 440px;"
            size="mini" placeholder="请输入内容"
            v-model="millForm.remarks">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="millSave">保存</el-button>
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
import {getMillTypeList,addMillType,editMillType,delMillType,getMillList,addMill,editMill,delMill} from "@/api/system/mill"
import {Message,MessageBox} from "element-ui";
import { groupBy } from "@/utils/groupBy";
import {ref,onMounted} from "vue";
import {getDefaultTree} from "@/utils/getDefaultTree";

onMounted(()=>{
  getTypeList();
  getList();
})
const treeData = ref([{id:0,typeName:"厂家类型"}])
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
const selectList = ref([])
const typeForm = ref({
  typeName:""
})
const typeFormRules = ref({
  typeName: [
    { required: true, message: '请输入类型名称', trigger: 'blur' }
  ]})
const queryParams = ref({
  millName:"",
  no:"",
  phone:"",
  count:0,
  pageSize:20,
  pageNum:1
})
const tableDate = ref([])
const tableColumn = ref([
  {label: '厂家名称', prop: 'millName',width:'150'},
  {label: '厂家编号', prop: 'no',width:'150'},
  {label: '所属分类', prop: 'typeName',width:'120'},
  {label: '联系人', prop: 'person',width:'100'},
  {label: '联系电话', prop: 'phone',width:'120'},
  {label: '电子邮箱', prop: 'mail',width:'200'},
  {label: '传真', prop: 'fax',width:'120'},
  {label: '厂家地址', prop: 'address',width:'250'},
  {label: '录入日期', prop: 'createdAt',width:'180'},
  {label: '备注', prop: 'remarks',width:'150'}])
const millForm = ref({
  millName:"",
  typeId:"",
  no:"",
  person:"",
  phone:"",
  tel:"",
  mail:"",
  address:"",
  fax:"",
  remarks:""
})
const millFormRules = ref({
  millName:[
    { required: true, message: '请输入厂家名称', trigger: 'blur' }
  ],
  typeId:[
    { required: true, message: '请选择厂家类型', trigger: 'blur' }
  ],
  // no:[
  //   { required: true, message: '请输入厂家编号', trigger: 'blur' }
  // ],
  // person:[
  //   { required: true, message: '请输入联系人', trigger: 'blur' }
  // ],
  phone:[
    // { required: true, message: '请输入手机号', trigger: 'blur' },
    {pattern: /^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/, message: '请输入正确的手机号'}
  ],
  // mail:[
  //   { required: true, message: '请输入邮箱', trigger: 'blur' },
  //   {pattern: /^([A-z0-9]{1,18})(\w|\-)+@[A-z0-9]+\.([A-z]{2,3})$/, message: '请输入正确的邮箱'}
  // ],
})
const millFormRef = ref(null)
const selectNode = ref({})

/*tree右键添加数据*/
const rightClick = (event, object, value, element)=>{
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
    getMillTypeList().then(res=>{
      if(res.code == 200){
        typeList.value = res.data
        treeData.value = [{id:0,typeName:"厂家类型",children:groupBy(res.data,"tid")}]
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
    delMillType(id).then(res=>{
      if(res.code == 200){
        Message.success(res.msg)
        treeData.value = [{id:0,typeName:"厂家类型"}]
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
      addMillType({tid:id,typeName}).then(res=>{
        if(res.code == 200){
          Message.success(res.msg);
          treeData.value = [{id:0,typeName:"厂家类型"}]
          typeDialog.value = false
          getTypeList()
        }
      })
    }else if(typeDialogType.value == 'edit'){
      let { id,typeName,tid } = typeForm.value
      editMillType(id,{typeName,tid}).then(res=>{
        if(res.code == 200){
          Message.success(res.msg);
          treeData.value = [{id:0,typeName:"厂家类型"}]
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
  getMillList(queryParams.value).then(res=>{
    if(res.code == 200){
      tableDate.value = res.data.map(item=>{
        if(item.typeId != 0 && item.typeId != ''){
          item.typeName = typeList.value.filter(item2=>{
            return item2.id == item.typeId
          })[0].typeName
        }else{
          item.typeName = ""
        }
        return item
      })
      queryParams.value.count = res.count
    }
  })
}
const handleChange = (size,num) => {
  queryParams.value.pageNum = num
  queryParams.value.pageSize = size
  tableDate.value = []
  getList()
}
const handleQuery = ()=>{
  tableDate.value = []
  getList();
}
const handleAdd = ()=>{
  if(treeData.value[0].children.length <= 0){
    Message.warning("请新增类型后新增客户")
    return false
  }
  millForm.value = {
    typeId:selectNode.value?selectNode.value.id:"",
    millName:"",
    no:"",
    person:"",
    phone:"",
    tel:"",
    mail:"",
    address:"",
    fax:"",
    remarks:""
  }
  if(selectNode.value != undefined && selectNode.value.hasOwnProperty("id")) {
    millForm.value.typeId = selectNode.value.id
  }else{
    let defaultVal = getDefaultTree(treeData.value)
    millForm.value.typeId = defaultVal
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
  millForm.value = JSON.parse(JSON.stringify(selectList.value[0]))
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
    delMill(ids).then(res=>{
      if(res.code == 200){
        Message.success(res.msg);
        handleChange(20,1);
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
const millSave = () => {
  millFormRef.value.validate((valid) => {
    if (valid) {
      millForm.value.typeId =  Array.isArray(millForm.value.typeId)?millForm.value.typeId[millForm.value.typeId.length-1]:millForm.value.typeId;
      if(dialogType.value == 'add'){
        addMill(millForm.value).then(res=>{
          if(res.code == 200){
            Message.success(res.msg)
            dialog.value = false
            handleChange(20,1)
          }
        })
        return false
      }
      if (dialogType.value == 'edit'){
        editMill(millForm.value.id,millForm.value).then(res=>{
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
