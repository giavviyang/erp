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
        <a @click="addType" v-hasPermi="['system:product_type:add']">新增类型</a>
        <a @click="editType" v-hasPermi="['system:product_type:edit']">编辑类型</a>
        <a @click="delType" v-hasPermi="['system:product_type:del']">删除类型</a>
      </VueContextMenu>
      <div slot="mainRight" class="app-container">
        <el-form size="mini" :model="queryParams" ref="queryForm" :inline="true" class="iptAndBtn">

          <el-form-item label="产品名称">
            <el-input
              v-model="queryParams.productName"
              placeholder="请输入产品名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="产品编号">
            <el-input
              v-model="queryParams.no"
              placeholder="请输入产品编号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
<!--          <el-form-item>-->
            <el-button type="primary" size="mini" icon="el-icon-search" @click="handleQuery">搜索</el-button>
<!--          </el-form-item>-->
        </el-form>
        <div class="btn">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            v-hasPermi="['system:product:add']"
            @click="handleAdd"
          >新增产品
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            v-hasPermi="['system:product:edit']"
            @click="handleUpdate"
          >编辑产品
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-delete"
            size="mini"
            v-hasPermi="['system:product:del']"
            @click="handleDelete"
          >删除产品
          </el-button>
        </div>
        <slot-table class="rightTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum" :total="count">
          <el-table highlight-current-row
            :data="tableDate"
            stripe
            border
            height="100%"
            ref="productTableRef"
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
              :key="'1'+JSON.stringify(item.prop)"
              :prop="item.prop"
              :label="item.label"
              show-overflow-tooltip
              :min-width="item.width"
              :formatter="item.formatter">
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </MainFlexibleTreeRightClick>
    <el-dialog :title="typeDialogType=='add'?'新增产品类型':typeDialogType=='edit'?'编辑产品类型':''" :visible.sync="typeDialog" width="500px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
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
    <el-dialog :title="dialogType=='add'?'新增产品':dialogType=='edit'?'编辑产品':''" :visible.sync="dialog" width="900px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <el-form :model="productForm" :rules="productFormRules" ref="productFormRef" label-width="120px" size="mini" style="display: flex;flex-wrap: wrap;">
        <el-form-item label="产品类型" prop="typeId">
          <el-cascader
            style="width: 170px"
            placeholder="请选择产品类型"
            v-model="productForm.typeId"
            @change="cascaderChange"
            :props="{value:'id',label:'typeName' }"
            :show-all-levels="false"
            filterable @blur="productTypeInput"
            :options="treeData" clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="玻璃层数">
          <el-select :disabled="dialogType=='edit'" v-model="productForm.semiNum" @change="layerChange" style="width: 170px" placeholder="请选择">
            <el-option
              v-for="item in dict.type.sys_glass_layer"
              :key="'2'+JSON.stringify(item)"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="产品编号" prop="no">
          <el-input style="width: 170px" v-model="productForm.no" placeholder="请输入产品编号"></el-input>
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input style="width: 460px" v-model="productForm.productName" placeholder="请输入产品名称"></el-input>
        </el-form-item>
        <div style="display:flex;align-items:center;height:36px;color: red;margin-left: 50px;font-size: 14px;">产品名称为发货单产品名称</div>
<!--        <el-form-item label="产品全称">-->
<!--&lt;!&ndash;          <div style="width: 460px;">{{productForm.fullName}}</div>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-input style="width: 460px;color: #4d4d4d" v-model="productForm.fullName" disabled></el-input>&ndash;&gt;-->
<!--          <el-input style="width: 460px;color: #4d4d4d" v-model="productForm.fullName" placeholder="请设置产品全称" disabled></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="产品价格(元)" prop="price">
          <el-input style="width: 170px" v-model="productForm.price" placeholder="请输入产品价格"></el-input>
        </el-form-item>
      </el-form>
      <el-table highlight-current-row
        v-if="productForm.semiProduct.length > 0"
        :data="productForm.semiProduct"
        border
        style="width: 100%" height="calc(30vh)">
        <el-table-column
          prop="attribute"
          label="属性" show-overflow-tooltip >
        </el-table-column>
        <el-table-column
          label="厚度(mm)" width="160">
          <template slot-scope="scope">
<!--            <el-input class="table-input" v-model="scope.row.thick" placeholder="请输入厚度"></el-input>-->
            <el-input-number size="mini"  :min="0" :precision="2" :step="1" v-model="scope.row.thick" style="width: 120px"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column
          label="单片名称" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-input @input="getFullName" class="table-input" v-model="scope.row.semiProductName" placeholder="请输入单片名称(流程卡单片名称)"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="品类" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-select v-model="scope.row.type" class="table-input" placeholder="请选择品类">
              <el-option
                v-for="item in dict.type.semi_product_type"
                :key="'3'+JSON.stringify(item)"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="工艺流程" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-select filterable  v-model="scope.row.craftFlow" class="table-input" placeholder="请选择工艺流程">
              <el-option
                v-for="item in craftFlowOptions"
                :key="'4'+JSON.stringify(item)"
                :label="item.craftFlowTxt"
                :value="JSON.stringify(item)">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="productSave">保存</el-button>
        <el-button size="mini" @click="dialog = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  dicts: ['sys_glass_layer','semi_product_type']
}
</script>
<script setup>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {component as VueContextMenu} from '@xunlei/vue-context-menu';
import SlotTable from "@/components/public/table/SlotTable";
import MainFlexibleTreeRightClick from "@/components/public/MainBody/MainFlexibleTreeRightClick";
import {getProductType,addProductType,editProductType,delProductType,getProduct,addProduct,editProduct,getSemiProduct,delProduct} from "@/api/system/product"
import {Message,MessageBox} from "element-ui";
import { groupBy } from "@/utils/groupBy";
import {ref,onMounted,computed } from "vue";
import {getToken} from "@/utils/auth";
import {getAllCraftFlowList} from "@/api/system/craft";
import {getDefaultTree} from '@/utils/getDefaultTree'

onMounted(()=>{
  getTypeList();
  getList();
  getCraftFlow();
})
const getFullName = ()=>{
  let name = ""
  productForm.value.semiProduct.forEach(item=>{
    if(item.semiProductName != ""){
      if(name == ""){
        name = item.semiProductName
      }else{
        name += `+${item.semiProductName}`
      }
    }
  })
  productForm.value.fullName = name
}
const selectNode = ref({})
const craftFlowOptions = ref([])
const glassAttribute = ref(['A','B','C','D','E','F','G','H'])
const header = ref({'Authorization' : 'Bearer ' + getToken()})
const treeData = ref([{id:0,typeName:"产品类型"}])
const typeList = ref([])
const TreeDefaultProps = ref({
  children: 'children',
  label: 'typeName'
})
const productTableRef = ref(null)
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
  productName:"",
  no:""
})
const count = ref(0);
const pageNum = ref(1);
const pageSize = ref(20);
const tableDate = ref([])
const tableColumn = ref([
  {label: '产品名称', prop: 'productName',width:'180'},
  // {label: '产品全称', prop: 'fullName',width:'300'},
  {label: '产品类别', prop: 'type',width:'100'},
  {label: '产品编号', prop: 'no',width:'100'},
  {label: '产品价格', prop: 'price',width:'100'},
  {label: '创建时间', prop: 'createdAt',width:'180'}])
const productForm = ref({
  productName:"",
  fullName:"",
  thick:"",
  type:"",
  typeId:"",
  no:"",
  price:"",
  semiNum:"",
  semiProduct:[]
})
const productFormRules = ref({
  productName:[
    { required: true, message: '请输入产品名称', trigger: 'blur' }
  ],
  typeId:[
    { required: true, message: '请选择产品类型', trigger: 'blur' }
  ],
  price:[
    {pattern: /^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/g, message: '请输入大于0的整数或小数'}
  ]
})
const productFormRef = ref(null)

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
    getProductType().then(res=>{
      if(res.code == 200){
        typeList.value = res.data
        treeData.value = [{id:0,typeName:"产品类型",children:groupBy(res.data,"tid")}]
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
    delProductType(id).then(res=>{
      if(res.code == 200){
        Message.success(res.msg)
        treeData.value = [{id:0,typeName:"产品类型"}]
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
      addProductType({tid:id,typeName}).then(res=>{
        if(res.code == 200){
          Message.success(res.msg);
          treeData.value = [{id:0,typeName:"产品类型"}]
          typeDialog.value = false
          getTypeList()
        }
      })
    }else if(typeDialogType.value == 'edit'){
      let { id,typeName,tid } = typeForm.value
      editProductType(id,{typeName,tid}).then(res=>{
        if(res.code == 200){
          Message.success(res.msg);
          treeData.value = [{id:0,typeName:"产品类型"}]
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
  queryParams.value.pageNum = pageNum.value
  queryParams.value.pageSize = pageSize.value
  getProduct(queryParams.value).then(res=>{
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
const productTypeInput = (e) => {
  let value = e.target.value;
  if(value) {
    let isAdd = treeData.value[0]["children"].filter(item=>{
      return item.typeName == value
    })
    if(isAdd.length <= 0){
      treeData.value[0]["children"] = treeData.value[0]["children"].filter(item=>{
        return item.id != '000'
      }).concat({
        id:"000",
        typeName:value
      })
      productForm.value.typeId = "000"
    }else{
      productForm.value.typeId = isAdd[0].id
    }
  }
}
const handleAdd = ()=>{
  if(treeData.value[0].children.length <= 0){
    Message.warning("请新增类型后新增产品")
    return false
  }
  productForm.value = {
    productName:"",
    fullName:"",
    thick:"",
    type:"",
    typeId:undefined,
    no:"",
    price:"",
    semiNum:"1",
    semiProduct:[]
  }
  console.log(selectNode)
  if(selectNode.value != undefined && selectNode.value.hasOwnProperty("id")) {
    productForm.value.typeId = selectNode.value.id
  }else{
    let defaultVal = getDefaultTree(treeData.value)
    productForm.value.typeId = defaultVal
  }
  layerChange(1);
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

  getSemiProduct(selectList.value[0].id).then(res=>{
    if(res.code == 200){
      productForm.value = selectList.value[0]
      productForm.value.semiProduct = res.data
      dialogType.value = 'edit'
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
    delProduct(ids).then(res=>{
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
const productSave = () => {
  productFormRef.value.validate((valid) => {
    if (valid) {
      if(productForm.value.semiProduct.length <= 0){
        Message.error("请选择层数后，输入半产品信息")
        return false
      }
      if(dialogType.value == 'add'){
        // let isOk = true
        productForm.value.semiProduct = productForm.value.semiProduct.map(item=>{
          try {
            let craftFlow = JSON.parse(item.craftFlow)
            item.craftFlow = craftFlow.craftFlowTxt
            item.craftFlowId = craftFlow.id
            return item
          }catch (e){
            return item
          }
        })
        // try {
        //   productForm.value.semiProduct.forEach(item=>{
        //     let keys = Object.keys(item)
        //     if(item['attribute'] != '中间层'){
        //       keys.forEach(item2=>{
        //         if(item[item2]+"" == "" || item[item2]+""  == null){
        //           console.log(item[item2])
        //           console.log(item2)
        //           throw new Error("请将半产品列表中所有项填写后进行保存")
        //           return false
        //         }
        //       })
        //     }
        //   })
        // }catch (e){
        //   isOk = false
        //   Message.error(e.message)
        // }
        // if(isOk){
          productForm.value.typeId = Array.isArray(productForm.value.typeId)?productForm.value.typeId[productForm.value.typeId.length-1]:productForm.value.typeId
          productForm.value.type = treeData.value[0]["children"].filter((item)=>{
            return productForm.value.typeId == item.id
          })[0].typeName
          console.log(productForm.value)
          addProduct(productForm.value).then(res=>{
            if(res.code == 200){
              Message.success(res.msg)
              dialog.value = false
              handleChange(20,1)
              getTypeList()
            }
          })
        // }
        return false
      }
      if (dialogType.value == 'edit'){
        // let isOk = true
        productForm.value.semiProduct = productForm.value.semiProduct.map(item=>{
          try {
            let craftFlow = JSON.parse(item.craftFlow)
            item.craftFlow = craftFlow.craftFlowTxt
            item.craftFlowId = craftFlow.id
            return item
          }catch (e){
            return item
          }
        })
        // try {
        //   productForm.value.semiProduct.forEach(item=>{
        //     let keys = Object.keys(item)
        //     if(item['attribute'] != '中间层') {
        //       keys.forEach(item2 => {
        //         if (item[item2] + "" == "" || item[item2] + "" == null) {
        //           console.log(item[item2])
        //           console.log(item2)
        //           throw new Error("请将半产品列表中所有项填写后进行保存")
        //           return false
        //         }
        //       })
        //     }
        //   })
        // }catch (e){
        //   isOk = false
        //   Message.error(e.message)
        // }
        // if(isOk){
          productForm.value.typeId = Array.isArray(productForm.value.typeId)?productForm.value.typeId[productForm.value.typeId.length-1]:productForm.value.typeId
          productForm.value.type = treeData.value[0]["children"].filter((item)=>{
            return productForm.value.typeId == item.id
          })[0].typeName
          console.log(productForm.value)
          editProduct(productForm.value.id,productForm.value).then(res=>{
            if(res.code == 200){
              Message.success(res.msg)
              dialog.value = false
              handleChange(20,1)
              getTypeList()
            }
          })
        // }
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
  productTableRef.value.toggleRowSelection(row, column)
}
const layerChange= (e) => {
  let str = new Array(Number(e)).fill("").map((item,index)=>{
    return JSON.stringify({
      semiProductName: '',
      attribute: glassAttribute.value[index]+'片',
      thick: '',
      craftFlowId: '',
      craftFlow: '',
      sort: '',
      type: ''
    });
  }).join(`,${JSON.stringify({
    semiProductName: '',
    attribute: '中间层',
    thick: '',
    craftFlowId: '',
    craftFlow: '',
    sort: '',
    type: ''
  })},`)
  let arr =  JSON.parse(`[${str}]`).map((item,index)=>{
    item.sort = index
    return item
  })
  productForm.value.semiProduct = arr
}
const getCraftFlow = ()=>{
  getAllCraftFlowList().then(res=>{
    craftFlowOptions.value = res.data
  })
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
.table-input{
  ::v-deep .el-input__inner{
    height: 26px;
    line-height: 26px;
    border: none;
  }
  ::v-deep .el-input__suffix , .el-input__suffix-inner{
    display: flex;
    align-items: center;
  }
}
::v-deep .el-table {
  border-right:1px solid #dfe6ec;
  color: #909399;

  th{
    height: 34px !important;
  }

  & > .el-table__header-wrapper {
    & > table {
      tr:first-of-type {
        th {
          background: rgba(204, 204, 204, 0.18);
          color: #909399;
          font-size: 12px;
          text-align: center;
          height: 34px !important;
          padding: 5px 0 !important;
        }
      }
      tr:nth-of-type(2) {
        th {
          background: #fff;
          color: #909399;
          font-size: 12px;
          text-align: center;
          padding: 0 !important;
          height: 34px;
          .el-input__inner {
            border: none;
            padding: 0;
            height: 20px;
          }

          .el-input__suffix {
            width: 12px;

            .el-input__icon {

              line-height: 23px;
            }
          }
        }
      }

      & > colgroup {
        col {
          &:last-of-type {
            width: 17px !important;
          }
        }
      }
    }
  }

  .el-table__body-wrapper {
    overflow: auto !important;
    .el-table__row {
      td {
        text-align: center;
        padding: 5px 0 !important;
        .operation {
          .cell {
            display: flex;
            justify-content: space-around;
            width: 100%;
            white-space:nowrap;
            overflow:hidden;
            text-overflow: ellipsis;

            .el-button {
              width: 100%;
              height: 100%;
              position: relative;
              top: 0;
              right: 0;
              margin: 0 2px;
            }
          }
        }
      }
    }
  }
  .el-table__cell {
    padding: 5px 0 !important;
  }
}
</style>
