<template>
  <div class="app-container-padding">
    <!--  选择厂家弹窗  -->
    <el-dialog
      title="选择厂家"
      :visible.sync="selectMillDialog"
      width="80%"
      class="dialog-style selectMillDialog"
      :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true"
      :before-close="cancelSelectMill">
      <main-flexible-tree :data="millTypeData"
                          :defaultProps="millTypeProps" @handleNodeClick="handleMillNodeClick">
        <div slot="mainRight" class="app-container">
          <el-form :model="millParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
            <el-form-item label="厂家编号" prop="number">
              <el-input
                v-model="millParams.no"
                placeholder="请输入厂家编号"
                clearable
                @keyup.enter.native="getMillList"
              />
            </el-form-item>
            <el-form-item label="厂家名称" prop="customerName">
              <el-input
                v-model="millParams.millName"
                placeholder="请输入厂家名称"
                clearable
                @keyup.enter.native="getMillList"
              />
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="millParams.phone"
                placeholder="请输入联系电话"
                clearable
                @keyup.enter.native="getMillList"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="getMillList">搜索</el-button>
            </el-form-item>
          </el-form>
          <div class="btn">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAddMill"
            >新增厂家
            </el-button>
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="handleUpdateMill"
            >编辑厂家
            </el-button>
            <el-button
              type="primary"
              icon="el-icon-delete"
              size="mini"
              @click="handleDeleteMill"
            >删除厂家
            </el-button>
          </div>
          <slot-table class="rightTable" @handleChange="handleMillChange" :pageSize="millPageSize"
                      :pageNum="millPageNum" :total="millTotal">
            <el-table highlight-current-row
              :data="millList"
              stripe
              border
              height="100%"
              style="width: 100%"
              ref="multipleMillTable"
              @row-click="handleRowClickMill"
              @selection-change="handleSelectionMill"
              slot="table">
              <el-table-column
                type="selection"
                width="55">
              </el-table-column>
              <el-table-column
                :index="getMillIndex"
                type="index"
                label="序号"
                width="50">
              </el-table-column>
              <el-table-column
                v-for="item in millListColumn"
                :key="item.prop"
                :prop="item.prop"
                :label="item.label"
                :min-width="item.width" show-overflow-tooltip>
              </el-table-column>
            </el-table>
          </slot-table>
        </div>
      </main-flexible-tree>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="saveSelectMill">确定</el-button>
         <el-button size="mini" @click="cancelSelectMill">取消</el-button>
      </span>
    </el-dialog>
    <!-- 新增、编辑厂家 -->
    <el-dialog :title="millDialogType=='add'?'新增厂家':millDialogType=='edit'?'编辑厂家':''"
               :visible.sync="millDialog"
               width="800px" class="dialog-style addMill" :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <el-form :model="millForm" :rules="millFormRules" ref="millFormRef" class="ipt2 addMill">
        <el-form-item label="厂家名称" prop="millName">
          <el-input v-model="millForm.millName" placeholder="请输入厂家名称"></el-input>
        </el-form-item>
        <el-form-item label="厂家类型" prop="typeId">
          <el-cascader

            placeholder="请选择厂家类型"
            v-model="millForm.typeId"
            :props="{value:'id',label:'typeName'}"
            :show-all-levels="false"
            :options="millTypeData" clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="厂家编号" prop="no">
          <el-input v-model="millForm.no" placeholder="请输入厂家编号"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="person">
          <el-input v-model="millForm.person" placeholder="请输入联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="millForm.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="固话" prop="tel">
          <el-input v-model="millForm.tel" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱" prop="mail">
          <el-input v-model="millForm.mail" placeholder="请输入电子邮箱"></el-input>
        </el-form-item>
        <el-form-item label="传真" prop="fax">
          <el-input v-model="millForm.fax" placeholder="请输入传真"></el-input>
        </el-form-item>
        <el-form-item label="厂家地址" prop="address" class="address">
          <el-input v-model="millForm.address" placeholder="请输入厂家地址"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks" class="address">
          <el-input
            type="textarea"
            placeholder="请输入内容" size="mini"
            v-model="millForm.remarks">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="millSave('millFormRef')">保存</el-button>
        <el-button size="mini" @click="millDialog= false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import {addMill, delMill, editMill, getMillTypeList, getMillList} from "@/api/system/mill";
import {Message, MessageBox} from "element-ui";
import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import SlotTable from "@/components/public/table/SlotTable";
import {groupBy} from "@/utils/groupBy";

export default {
  name: "millDialog",
  components: {SlotTable, MainFlexibleTree},
  data() {
    return {
      // selectMillDialog: false,   //选择厂家弹窗
      millTypeList: [],    //厂家类型
      millTypeData: [],    //厂家类型
      millTypeProps: {
        children: 'children',
        label: 'typeName'
      },
      // 厂家查询参数
      millParams: {
        millName: "",
        no: "",
        phone: "",
        count: 0,
        pageSize: 20,
        pageNum: 1
      },
      millDialogType: 'add',
      millDialog: false,
      millForm: {
        millName: "",
        typeId: "",
        no: "",
        person: "",
        phone: "",
        tel: "",
        mail: "",
        address: "",
        fax: "",
        remarks: ""
      },
      millFormRules: {
        millName: [
          {required: true, message: '请输入厂家名称', trigger: 'blur'}
        ],
        typeId: [
          {required: true, message: '请选择厂家类型', trigger: 'blur'}
        ],
        no: [
          {required: true, message: '请输入厂家编号', trigger: 'blur'}
        ],
        person: [
          {required: true, message: '请输入联系人', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
          {pattern: /^(1[345789]\d{9})$/, message: '请输入正确的手机号'}
        ],
        // mail: [
        //   {required: true, message: '请输入邮箱', trigger: 'blur'},
        //   {pattern: /^([A-z0-9]{1,18})(\w|\-)+@[A-z0-9]+\.([A-z]{2,3})$/, message: '请输入正确的邮箱'}
        // ],
      },
      millPageSize: 20,
      millPageNum: 1,
      millTotal: 0,
      millList: [],         // 表格数据
      millListColumn: [
        {label: '厂家名称', prop: 'millName', width: '150'},
        {label: '厂家编码', prop: 'no', width: '140'},
        {label: '所属分类', prop: 'typeName', width: '100'},
        {label: '联系人', prop: 'person', width: '100'},
        {label: '联系电话', prop: 'phone', width: '120'},
        {label: '电子邮箱', prop: 'mail', width: '130'},
        {label: '传真', prop: 'fax', width: '120'},
        {label: '厂家地址', prop: 'address', width: '180'},
        {label: '录入日期', prop: 'createdAt', width: '180'},
        {label: '备注', prop: 'remarks', width: '100'}
      ],  //表头
      // selectedMill: [],  //厂家列表被选中表格数据
    }
  },
  props: {
    selectMillDialog: {
      type: Boolean,
      default: false,
    },
    // selectedMill:{
    //   type:Array,
    //   default:()=>[]
    // }
    // millParams:{
    //   type: Object,
    //   default: ()=>{
    //
    //   },
    // }
  },
  created() {
    this.getMillTypeList();
    this.getMillList();
  },
  methods: {
    /* 厂家选择弹窗获取厂家列表 */
    getMillTypeList() {
      getMillTypeList().then(response => {
        if (response.code === 200) {
          this.millTypeList = response.data;
          this.millTypeData = [{id: 0, typeName: "厂家类型", children: groupBy(response.data, "tid")}]
        }
      });
    },
    /* 点击厂家类型树，选择厂家 */
    handleMillNodeClick(val, node, component) {
      this.millParams.typeId = val.id;
      this.getMillList();
    },
    /*  查询厂家信息 */
    getMillList() {
      if (this.millParams.typeId == 0) {
        this.millParams.typeId = '';
      }
      getMillList(this.millParams).then(response => {
        if (response.code === 200) {
          this.millList = response.data.map(item=>{
            if(item.typeId != 0 && item.typeId != ''){
              item.typeName = this.millTypeList.filter(item2=>{
                return item2.id == item.typeId
              })[0].typeName
            }else{
              item.typeName = ""
            }
            return item
          })
          this.millTotal = response.count;
        }
      })
    },
    /* 翻页后，序号连贯 */
    getMillIndex($index) {
      //  表格序号
      return (this.millPageNum - 1) * this.millPageSize + $index + 1;
    }
    ,
    /* 新增厂家 */
    handleAddMill() {
      this.millForm = {
        typeId: this.millParams.typeId,
        typeName: "",
        millName: "",
        no: "",
        person: "",
        phone: "",
        tel: "",
        mail: "",
        address: "",
        fax: "",
        remarks: ""
      }
      this.millDialogType = 'add';
      this.millDialog = true
    }
    ,
    millSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.millForm.typeId = Array.isArray(this.millForm.typeId) ? this.millForm.typeId[this.millForm.typeId.length - 1] : this.millForm.typeId;
          if (this.millDialogType == 'add') {
            addMill(this.millForm).then(res => {
              if (res.code == 200) {
                Message.success(res.msg)
                this.millDialog = false
                this.getMillList();
              }
            })
            return false
          }
          if (this.millDialogType == 'edit') {
            editMill(this.millForm.id, this.millForm).then(res => {
              if (res.code == 200) {
                Message.success(res.msg)
                this.millDialog = false
                this.getMillList();
              }
            })
            return false
          }
        } else {
          return false;
        }
      })
    }
    ,
    /* 编辑厂家 */
    handleUpdateMill() {
      if (this.selectedMill.length <= 0) {
        Message.warning("请选择要修改的数据");
        return false
      }
      // console.log(this.selectedMill)
      this.millForm = JSON.parse(JSON.stringify(this.selectedMill[0]));
      this.millDialogType = 'edit';
      this.millDialog = true;
    }
    ,
    /* 删除厂家 */
    handleDeleteMill() {
      if (this.selectedMill <= 0) {
        Message.warning("请选择要删除的数据");
        return false
      }
      MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = this.selectedMill.map(item => {
          return item.id
        }).join(",")
        delMill(ids).then(res => {
          if (res.code == 200) {
            Message.success(res.msg);
            this.getMillList();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    }
    ,
    handleSelectionMill(val) {
      this.selectedMill = val;
      if (val.length === 1) {
        this.millForm.typeId = val[0].typeId;
      }
      if (val.length > 1) {
        this.$refs.multipleMillTable.clearSelection();
        this.$refs.multipleMillTable.toggleRowSelection(val.at(-1), true);
        this.millForm.typeId = val[0].typeId;
      }
      if (val.length ===0) {
        this.selectedMill=[];
      }
    }
    ,
    /* 厂家列表点击行 */
    handleRowClickMill(row, column, event) {
      this.$refs.multipleMillTable.toggleRowSelection(row, column)
    }
    ,
    /* 保存选择厂家弹窗 */
    saveSelectMill(val) {
      this.$emit('saveSelectMill', this.selectedMill, val);
    }
    ,
    cancelSelectMill() {
      this.$emit('cancelSelectMill');
    }
    ,
    /* 厂家分页器 */
    handleMillChange(size, num) {
      this.millPageSize = size;
      this.millPageNum = num;
      this.millParams.pageSize = this.millPageSize;
      this.millParams.pageNum = this.millPageNum;
      this.getMillList();
    }
    ,
  }
}
</script>

<style lang="scss" scoped>
::v-deep .selectMillDialog {
  .el-dialog {
    height: 70%;

    .el-dialog__body {
      padding: 0;
      height: calc(100% - 92px);
      overflow: hidden;

      .mainBody {
        margin: 0;

        .containerTree {
          .el-input__suffix {
            .el-icon-circle-close:before {

              top: 20px;
              right: 5px;
              position: absolute;
            }
          }
        }

        .rightTable {
          height: calc(100% - 105px);

          thead .el-table-column--selection .cell {
            display: none;
          }
        }
      }
    }
  }
}

::v-deep .addMill {

  .el-dialog__body {
    overflow: auto;

    .el-form-item {
      width: 365px;

      .remarks {
        width: 630px;
      }
    }

  }
}

</style>
