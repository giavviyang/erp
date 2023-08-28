<template>
  <div class="app-container-padding">
    <!--  选择客户弹窗  -->
    <el-dialog
      title="选择客户"
      :visible.sync="selectCustomerDialog"
      width="90%"
      class="dialog-style selectCustomerDialog"
      :close-on-click-modal="false"
       :close-on-press-escape="false" :destroy-on-close="true"
      :before-close="cancelSelectCustomer">
      <main-flexible-tree :data="customerTypeData"
                          :defaultProps="customerTypeProps" @handleNodeClick="handleCustomerNodeClick">
        <div slot="mainRight" class="app-container">
          <el-form :model="customerParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
            <el-form-item label="客户编号" prop="customerName">
              <el-input
                v-model="customerParams.number"
                placeholder="请输入客户编号"
                @keyup.enter.native="getCustomerList"
                clearable/>
            </el-form-item>
            <el-form-item label="客户名称" prop="customerName">
              <el-input
                v-model="customerParams.customerName"
                placeholder="请输入客户名称"
                @keyup.enter.native="getCustomerList"
                clearable/>
            </el-form-item>
            <el-form-item label="客户地址" prop="address">
              <el-input
                v-model="customerParams.address"
                placeholder="请输入客户地址"
                @keyup.enter.native="getCustomerList"
                clearable/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="getCustomerList">搜索</el-button>
            </el-form-item>
          </el-form>
          <div class="btn">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAddCustomer"
            >新增客户
            </el-button>
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="handleUpdateCustomer"
            >编辑客户
            </el-button>
            <el-button
              type="primary"
              icon="el-icon-delete"
              size="mini"
              @click="handleDeleteCustomer"
            >删除客户
            </el-button>
          </div>
          <slot-table class="rightTable" @handleChange="handleCustomerChange" :pageSize="customerPageSize"
                      :pageNum="customerPageNum" :total="customerTotal">
            <!--            <el-table-->
            <!--              :data="customerList"-->
            <!--              stripe-->
            <!--              border-->
            <!--              height="100%"-->
            <!--              style="width: 100%"-->
            <!--              ref="multipleCustomerTable"-->
            <!--              @select="handleSelectionCustomer"-->
            <!--              @row-click="handleRowClickCustomer"-->
            <!--              :index="getCustomerIndex"-->
            <!--              slot="table">-->
            <el-table highlight-current-row
              :data="customerList"
              stripe
              border
              height="100%"
              style="width: 100%"
              ref="multipleCustomerTable"
              @row-click="handleRowClickCustomer"
              @selection-change="handleSelectionCustomer"
              slot="table">
              <el-table-column
                type="selection"
                width="55">
              </el-table-column>
              <el-table-column
                :index="getCustomerIndex"
                type="index"
                label="序号"
                width="50">
              </el-table-column>
              <el-table-column
                v-for="item in customerListColumn"
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
        <el-button type="primary" size="mini" @click="saveSelectCustomer">确定</el-button>
         <el-button size="mini" @click="cancelSelectCustomer">取消</el-button>
      </span>
    </el-dialog>
    <!-- 新增、编辑客户 -->
    <el-dialog :title="customerDialogType=='add'?'新增客户':customerDialogType=='edit'?'编辑客户':''"
               :visible.sync="customerDialog"
               width="800px" class="dialog-style addCustomer" :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true">
      <el-form :model="customerForm" :rules="customerFormRules" size="mini" ref="customerFormRef" class="ipt2 addCustomer">
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="customerForm.customerName" placeholder="请输入客户名称" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="客户类型" prop="typeId">
          <el-cascader
            placeholder="请选择客户类型"
            v-model="customerForm.typeId"
            :props="{value:'id',label:'typeName'}"
            :show-all-levels="false"
            :options="customerTypeData" clearable size="mini">
          </el-cascader>
        </el-form-item>
        <el-form-item label="客户编号" prop="number">
          <el-input v-model="customerForm.number" placeholder="请输入客户编号" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="业务员" prop="salesman">
          <el-input v-model="customerForm.salesman" placeholder="请输入业务员" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="contacts">
          <el-input v-model="customerForm.contacts" placeholder="请输入联系人" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="customerForm.phone" placeholder="请输入联系电话" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="信用额度" prop="credit">
          <el-input v-model="customerForm.credit" placeholder="请输入信用额度" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱" prop="mail">
          <el-input v-model="customerForm.mail" placeholder="请输入电子邮箱" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="传真" prop="fax">
          <el-input v-model="customerForm.fax" placeholder="请输入传真" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="客户地址" prop="address">
          <el-input v-model="customerForm.address" placeholder="请输入客户地址" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input
            type="textarea"
            v-model="customerForm.remarks" size="mini" class="remarks" placeholder="请输入内容">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="customerSave('customerFormRef')">保存</el-button>
        <el-button size="mini" @click="customerDialog= false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import {addCustomer, customerList, customerTypeList, delCustomer, editCustomer} from "@/api/system/customer";
import {Message, MessageBox} from "element-ui";
import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import SlotTable from "@/components/public/table/SlotTable";
import {groupBy} from "@/utils/groupBy";

export default {
  name: "customerDialog",
  components: {SlotTable, MainFlexibleTree},
  data() {
    return {
      // selectCustomerDialog: false,   //选择客户弹窗
      customerTypeData: [],    //客户类型
      customerTypeProps: {
        children: 'children',
        label: 'typeName'
      },
      // 客户查询参数
      customerParams: {
        number: '',
        customerName: '',
        address: '',
        typeId: '',
        pageNum: 1,
        pageSize: 20,
      },
      customerDialogType: 'add',
      customerDialog: false,
      customerForm: {
        customerName: "",
        typeId: "",
        number: "",
        salesman: "",
        contacts: "",
        phone: "",
        credit: "",
        mail: "",
        address: "",
        fax: "",
        remarks: ""
      },
      customerFormRules: {
        customerName: [
          {required: true, message: '请输入客户名称', trigger: 'blur'}
        ],
        typeId: [
          {required: true, message: '请选择客户类型', trigger: 'blur'}
        ],
        number: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        salesman: [
          {required: true, message: '请输入业务员', trigger: 'blur'}
        ],
        contacts: [
          {required: true, message: '请输入联系人', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
          {pattern: /^(1[345789]\d{9})$/, message: '请输入正确的手机号'}
        ],
        // mail: [
        //   {required: true, message: '请输入邮箱', trigger: 'blur'},
        //   {pattern: /^([A-z0-9]{6,18})(\w|\-)+@[A-z0-9]+\.([A-z]{2,3})$/, message: '请输入正确的邮箱'}
        // ],
      },
      customerPageSize: 20,
      customerPageNum: 1,
      customerTotal: 0,
      customerList: [],         // 表格数据
      customerListColumn: [
        {label: '客户名称', prop: 'customerName', width: '200'},
        {label: '客户编号', prop: 'number', width: '200'},
        {label: '联系人', prop: 'contacts', width: '120'},
        {label: '联系电话', prop: 'phone', width: '130px'},
        {label: '邮箱', prop: 'mail', width: '200'},
        {label: '传真', prop: 'fax', width: '180px'},
        {label: '客户地址', prop: 'address', width: '300'},
        {label: '创建时间', prop: 'createdAt', width: '180'},
        {label: '备注', prop: 'remarks'},
      ],  //表头
      // selectedCustomer: [],  //客户列表被选中表格数据
    }
  },
  props: {
    selectCustomerDialog: {
      type: Boolean,
      default: false,
    },
    // selectedCustomer:{
    //   type:Array,
    //   default:()=>[]
    // }
    // customerParams:{
    //   type: Object,
    //   default: ()=>{
    //
    //   },
    // }
  },
  created() {
    this.getCustomerTypeList();
    this.getCustomerList();
  },
   methods: {

    /* 客户选择弹窗获取客户列表 */
    getCustomerTypeList() {
      customerTypeList().then(response => {
        if (response.code === 200) {
          // this.customerTypeData = response.data;
          this.customerTypeData = [{id: 0, typeName: "客户类型", children: groupBy(response.data, "tid")}]
        }
      });
    },
    /* 点击客户类型树，选择客户 */
    handleCustomerNodeClick(val, node, component) {
      this.customerParams.typeId = val.id;
      this.getCustomerList();
    },
    /*  查询客户信息 */
    getCustomerList() {
      if (this.customerParams.typeId == 0) {
        this.customerParams.typeId = '';
      }
      customerList(this.customerParams).then(response => {
        console.log(response);
        if (response.code === 200) {
          this.customerList = response.data;
          this.customerTotal = response.count;
        }
      })
    },
    /* 翻页后，序号连贯 */
    getCustomerIndex($index) {
      //  表格序号
      return (this.customerPageNum - 1) * this.customerPageSize + $index + 1;
    },
    /* 新增客户 */
    handleAddCustomer() {
      this.customerForm = {
        typeId: this.customerParams.typeId,
        customerName: "",
        number: "",
        salesman: "",
        contacts: "",
        phone: "",
        credit: "",
        mail: "",
        address: "",
        fax: "",
        remarks: ""
      }
      this.customerDialogType = 'add';
      this.customerDialog = true
    },
    customerSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.customerForm.typeId = Array.isArray(this.customerForm.typeId) ? this.customerForm.typeId[this.customerForm.typeId.length - 1] : this.customerForm.typeId;
          if (this.customerDialogType == 'add') {
            addCustomer(this.customerForm).then(res => {
              if (res.code == 200) {
                Message.success(res.msg)
                this.customerDialog = false
                this.getCustomerList();
              }
            })
            return false
          }
          if (this.customerDialogType == 'edit') {
            editCustomer(this.customerForm.id, this.customerForm).then(res => {
              if (res.code == 200) {
                Message.success(res.msg)
                this.customerDialog = false
                this.getCustomerList();
              }
            })
            return false
          }
        } else {
          return false;
        }
      })
    },
    /* 编辑客户 */
    handleUpdateCustomer() {
      if (this.selectedCustomer.length <= 0) {
        Message.warning("请选择要修改的数据");
        return false
      }
      console.log(this.selectedCustomer)
      this.customerForm = this.selectedCustomer[0];
      this.customerDialogType = 'edit';
      this.customerDialog = true;
    },
    /* 删除客户 */
    handleDeleteCustomer() {
      if (this.selectedCustomer <= 0) {
        Message.warning("请选择要删除的数据");
        return false
      }
      MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = this.selectedCustomer.map(item => {
          return item.id
        }).join(",")
        delCustomer(ids).then(res => {
          if (res.code == 200) {
            Message.success(res.msg);
            this.getCustomerList();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleSelectionCustomer(val) {
      this.selectedCustomer = val;
      if (val.length === 1) {
        this.customerForm.typeId = val[0].typeId;
      }
      if (val.length > 1) {
        this.$refs.multipleCustomerTable.clearSelection();
        this.$refs.multipleCustomerTable.toggleRowSelection(val.at(-1), true);
        this.customerForm.typeId = val[0].typeId;
      }
      if (val.length ===0) {
        this.selectedCustomer=[];
      }
    },
    /* 客户列表点击行 */
    handleRowClickCustomer(row, column, event) {
      this.$refs.multipleCustomerTable.toggleRowSelection(row, column)
    },
    /* 保存选择客户弹窗 */
    saveSelectCustomer(val) {
      // this.selectCustomerDialog = false;
      // if (this.selectedCustomer !== undefined && this.selectedCustomer.length > 0) {
      //   this.basicInfoForm.customerName = this.selectedCustomer[0].customerName;
      //   this.basicInfoForm.receiptAddress = this.selectedCustomer[0].address;
      //   this.basicInfoForm.contactNumber = this.selectedCustomer[0].phone;
      //   this.basicInfoForm.contacts = this.selectedCustomer[0].contacts;
      //   // this.$refs.customerNameInput.blur();
      //   this.$refs.multipleCustomerTable.clearSelection();
      // }
      this.$emit('saveSelectCustomer', this.selectedCustomer, val);
    },
    cancelSelectCustomer() {
      this.$emit('cancelSelectCustomer');
      // this.selectCustomerDialog = false;
      // this.$refs.multipleCustomerTable.clearSelection();
    },
    /* 客户分页器 */
    handleCustomerChange(size, num) {
      this.customerPageSize = size;
      this.customerPageNum = num;
      this.customerParams.pageSize = this.customerPageSize;
      this.customerParams.pageNum = this.customerPageNum;
      this.getCustomerList();
    },
  }
}
</script>

<style lang="scss" scoped>
::v-deep .selectCustomerDialog {
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

::v-deep .addCustomer {

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
