<template>
  <div class="app-container">
    <MainFlexibleTreeRightClick :data="treeData"
                                :defaultProps="TreeDefaultProps"
                                @rightClick="rightClick" >
      <VueContextMenu
        :target="contextMenuTarget"
        :show="contextMenuVisible"
        class="right-menus"
        @update:show="(show) => contextMenuVisible = show" slot="RightClick">
        <a @click="addType">新建</a>
        <a @click="editType">编辑</a>
        <a @click="delType">删除</a>
      </VueContextMenu>
      <div slot="mainRight" class="app-container">
      </div>
    </MainFlexibleTreeRightClick>
    <el-dialog :title="typeDialogType=='add'?'新增模板类型':typeDialogType=='edit'?'编辑模板类型':''" :visible.sync="typeDialog" width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form size="mini" :model="typeForm" :rules="typeFormRules" ref="typeFormRef" label-width="100px">
        <el-form-item label="上级类型" prop="tid">
          <span v-if="typeDialogType == 'add'">{{currentNodeInfo?currentNodeInfo.type_name:'无'}}</span>
          <span v-else>{{typeForm.tid_name}}</span>
        </el-form-item>
        <el-form-item label="类型名称" prop="type_name">
          <el-input style="width: 350px" v-model="typeForm.type_name" placeholder="请输入类型名称"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="typeSubmit('save')">保存</el-button>
        <el-button size="mini" @click="typeSubmit('cancel')">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {component as VueContextMenu} from '@xunlei/vue-context-menu';
import MainFlexibleTreeRightClick from "@/components/public/MainBody/MainFlexibleTreeRightClick";
import {getTemplateTypeList,addTemplateType,editTemplateType,delTemplateType} from "@/api/system/template";
import {Message,MessageBox} from "element-ui";
import {groupBy} from "@/utils/groupBy";
import {ref,onMounted} from "vue";

onMounted(()=>{
  getTypeList();
})

const treeData = ref([])
const TreeDefaultProps = ref({
  children: 'children',
  label: 'type_name'
})
const contextMenuTarget = ref("")
const contextMenuVisible = ref(false)
const typeDialogType = ref("add")
const typeDialog = ref(false)
const topNodeInfo = ref({})
const currentNodeInfo = ref({})
const typeFormRef = ref(null)
const typeForm = ref({
  type_name:""
})
const typeFormRules = ref({
  type_name: [
    { required: true, message: '请输入类型名称', trigger: 'blur' }
  ]})
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
  if(treeData.value.length <= 0){
    getTemplateTypeList().then(res=>{
      if(res.code == 200){
        treeData.value = groupBy(res.data,"tid")
        console.log(treeData)
      }
    })
  }
}
const addType = () => {
  contextMenuVisible.value = false
  typeDialogType.value = "add"
  typeForm.value = {
    type_name:""
  }
  typeDialog.value = true
}
const editType = ()=>{
  contextMenuVisible.value = false
  typeDialogType.value = "edit"
  let {id,type_name,tid} = currentNodeInfo.value
  let {type_name:tid_name = '无'} = topNodeInfo.value
  typeForm.value = {
    type_name,
    id,
    tid,
    tid_name
  }
  typeDialog.value = true
}
const delType = () =>{
  contextMenuVisible.value = false
  MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let {id} = currentNodeInfo.value
    delTemplateType(id).then(res=>{
      if(res.code == 200){
        Message.success(res.msg)
        treeData.value = []
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
    let { type_name } = typeForm.value
    addTemplateType({tid:id,type_name}).then(res=>{
      if(res.code == 200){
        Message.success(res.msg);
        treeData.value = []
        typeDialog.value = false
        getTypeList()
      }
    })
    }else if(typeDialogType.value == 'edit'){
      let { id,type_name,tid } = typeForm.value
      editTemplateType(id,{type_name,tid}).then(res=>{
        if(res.code == 200){
          Message.success(res.msg);
          treeData.value = []
          typeDialog.value = false
          getTypeList()
        }
      })
    }
  }else if(type == 'cancel'){
    typeDialog.value = false
  }
}
</script>
<style lang="scss" scoped>
.rightTable {
  height: calc(100% - 105px);

  ::v-deep .table {
    height: 100%;
  }

  ::v-deep .page {
    display: none;
  }
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
