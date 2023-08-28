<template>
  <div class="app-container-padding addOrder">
    <div class="basicInfo">
      <p class="title1">基本信息</p>
      <div class="basicInfoContainer">
        <el-form :model="basicInfoForm" size="mini" :inline="true" ref="ruleForm" class="ipt2">
          <el-form-item label="订单编号">
            <el-input v-model="basicInfoForm.orderNo" disabled clearable/>
          </el-form-item>
          <el-form-item label="自定义编号" prop="customNo">
            <el-input v-model="basicInfoForm.customNo" placeholder="请输入自定义编号" clearable/>
          </el-form-item>
          <el-form-item label="客户名称" prop="customerName">
<!--            <el-input v-model="basicInfoForm.customerName" clearable @focus="handleBlur" ref="customerNameInput"-->
<!--                      placeholder="请选择客户名称"/>-->
            <el-select ref="customerName"
                       v-model="basicInfoForm.customerName" filterable allow-create placeholder="请选择客户" @keyup.enter.native="selectBlur"  @blur="selectBlur" clearable>
              <el-option
                v-for="(item,index) in customerNameList"
                :key="index"
                :label="item.customerName"
                :value="item.customerName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="项目名称" prop="customNo">
            <el-input v-model="basicInfoForm.entryName" placeholder="请输入客户名称" clearable/>
          </el-form-item>
          <el-form-item label="交货日期" prop="receiptDate">
            <el-date-picker
              v-model="basicInfoForm.receiptDate"
              type="datetime"
              placeholder="请选择交货日期"
              value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="下单人" prop="preparer">
            <el-input v-model="basicInfoForm.preparer" disabled/>
          </el-form-item>
          <el-form-item label="下单时间" prop="preparationDate">
            <el-date-picker
              v-model="basicInfoForm.preparationDate"
              type="datetime"
              placeholder="请选择下单日期"
              value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="联系人" prop="customNo">
            <el-input v-model="basicInfoForm.contacts" clearable placeholder="请输入联系人"/>
          </el-form-item>
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-model.number="basicInfoForm.contactNumber" clearable placeholder="请输入联系人电话"/>
          </el-form-item>
          <el-form-item label="收货地址" class="receiptAddress" prop="customNo">
            <el-input v-model="basicInfoForm.receiptAddress" clearable placeholder="请输入收货地址"/>
          </el-form-item>
          <el-form-item label="定金（元）" prop="orderDeposit">
            <el-input v-model="basicInfoForm.orderDeposit" clearable/>
          </el-form-item>
          <el-form-item label="订单类型">
            <el-select v-model="basicInfoForm.orderType" prop="orderType" placeholder='请选择订单类型'>
              <el-option
                v-for="item in dict.type.order_type"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="结算方式">
            <el-select v-model="basicInfoForm.settlementMethod" placeholder="请选择结算方式" clearable>
              <el-option
                v-for="item in dict.type.order_settlement"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="加急费（元）" prop="urgentCost">
            <el-input v-model="basicInfoForm.urgentCost" clearable/>
          </el-form-item>
          <el-form-item label="附加费（元）" prop="additionalCost">
            <el-input v-model="basicInfoForm.additionalCost" clearable/>
          </el-form-item>
          <el-form-item label="生产备注" class="remarks">
            <el-input v-model="basicInfoForm.remarks" type="textarea" size="mini" placeholder="请输入内容"/>
          </el-form-item>
          <el-form-item label="订单备注" class="remarks">
            <el-input v-model="basicInfoForm.orderRemarks" type="textarea" size="mini" placeholder="请输入内容"/>
          </el-form-item>
        </el-form>
      </div>
      <el-button
        type="primary"
        :icon="formSizeIcon"
        size="mini"
        class="changeBtn"
        @click="changeBasicInfo"
      >{{ formSize }}
      </el-button>
    </div>
    <div class="basicInfo">
      <p class="title1">配置列</p>
      <div class="basicChecked">
        <div class="addition">
          <p>附加项</p>
          <el-checkbox-group v-model="additionCheckList">
            <el-checkbox v-for="item in additionCheckOptions" :key="item.id" :label="item.id">
              {{ item.additionalName }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
        <div class="special">
          <p>特殊产品</p>
          <el-checkbox-group v-model="additionCheckList">
            <el-checkbox v-for="item in specialCheckOptions" :key="item.label" :label="item.label">{{ item.value }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <el-button
        type="primary"
        :icon="checkedSizeIcon"
        size="mini"
        class="changeBtn"
        @click="changeBasicChecked"
      >{{ checkedSize }}
      </el-button>
    </div>
    <div class="handTable" ref="handTable">
      <hot-table :settings="hotSettings" ref="hotTable" style="height: 100%;"></hot-table>
    </div>
    <div class="countAndBtn">
      <div class="count">
        <p>合计</p>
        <p v-for="item in summation" :key="item.label">{{ item.title }}：<span>{{ item.value }}</span>{{ item.unit }}</p>
      </div>
      <div class="btn">
        <el-button
          type="primary"
          icon="el-icon-download"
          @click="downloadTemplate"
          size="mini">下载Excel模板
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-upload2"
          @click="importTemplate"
          size="mini">导入Excel
        </el-button>
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-mobile-phone"
          @click="handleCount">
          计算
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-s-operation"
          size="mini"
          @click="handleMerge">合并同规格
        </el-button>
        <el-button
          type="primary"
          icon="el-icon-check"
          size="mini"
          @click="saveProductInfo('ruleForm')">保存
        </el-button>
        <el-button
          icon="el-icon-refresh-left"
          size="mini"
          @click="addBack">返回
        </el-button>
      </div>
    </div>
    <!--  选择客户弹窗  -->
    <el-dialog
      title="选择客户"
      :visible.sync="selectCustomerDialog"
      width="90%"
      class="dialog-style selectCustomerDialog"
      :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true"
      :before-close="cancelSelectCustomer">
      <main-flexible-tree :data="customerTypeData"
                          :defaultProps="customerTypeProps" @handleNodeClick="handleCustomerNodeClick">
        <div slot="mainRight" class="app-container">
          <el-form :model="customerParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">

            <el-form-item label="客户名称" prop="customerName">
              <el-input
                v-model="customerParams.customerName"
                placeholder="请输入客户名称"
                @keyup.enter.native="getCustomerList"
                clearable/>
            </el-form-item>
            <el-form-item label="客户编号" prop="customerName">
              <el-input
                v-model="customerParams.number"
                placeholder="请输入客户编号"
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
              v-hasPermi="['system:customer:add']"
              @click="handleAddCustomer"
            >新增客户
            </el-button>
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="handleUpdateCustomer"
              v-hasPermi="['system:customer:edit']"
            >编辑客户
            </el-button>
            <el-button
              type="primary"
              icon="el-icon-delete"
              size="mini"
              @click="handleDeleteCustomer"
              v-hasPermi="['system:customer:del']"
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
                      :index="getCustomerIndex"
                      slot="table">
              <el-table-column
                type="selection"
                width="55">
              </el-table-column>
              <el-table-column
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
               width="800px" class="dialog-style addCustomer" :close-on-click-modal="false"
               :close-on-press-escape="false" :destroy-on-close="true">
      <el-form :model="customerForm" ref="customerFormRef" class="ipt2 addCustomer">
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="customerForm.customerName" placeholder="请输入客户名称" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="客户类型" prop="typeId">
          <el-cascader
            placeholder="请选择客户类型"
            v-model="customerForm.typeId"
            :props="{value:'id',label:'typeName'}"
            :show-all-levels="false"
            :options="customerTypeData" clearable filterable @blur="customerTypeInput" size="mini">
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
        <el-button type="primary" size="mini" @click="customerSave('customerFormRef')">确定</el-button>
        <el-button size="mini" @click="customerDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!-- 选择产品弹窗 -->
    <el-dialog
      title="选择产品"
      :visible.sync="selectProductDialog"
      width="90%"
      class="dialog-style selectProductDialog"
      :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <main-flexible-tree :data="productTypeData"
                          :defaultProps="productTypeProps" @handleNodeClick="handleProductNodeClick">
        <div slot="mainRight" class="app-container">
          <el-form :model="productParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
            <el-form-item label="产品名称" prop="productName">
              <el-input
                v-model="productParams.productName"
                placeholder="请输入产品名称"
                @keyup.enter.native="getProductList"
                clearable/>
            </el-form-item>
            <el-form-item label="产品编号" prop="no">
              <el-input
                v-model="productParams.no"
                placeholder="请输入产品编号"
                @keyup.enter.native="getProductList"
                clearable/>
            </el-form-item>
            <!--            <el-form-item label="产品厚度" prop="thick">
                          <el-input
                            v-model="productParams.thick"
                            placeholder="请输入产品厚度"
                            @keyup.enter.native="getProductList"
                            clearable/>
                        </el-form-item>-->
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="getProductList">搜索</el-button>
            </el-form-item>
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
              @click="handleUpdate"
              v-hasPermi="['system:product:edit']"
            >编辑产品
            </el-button>
            <el-button
              type="primary"
              icon="el-icon-delete"
              size="mini"
              @click="handleDelete"
              v-hasPermi="['system:product:del']"
            >删除产品
            </el-button>
          </div>
          <slot-table class="rightTable" @handleChange="handleProductChange" :pageSize="productPageSize"
                      :pageNum="productPageNum" :total="productTotal">
            <el-table highlight-current-row
                      :data="systemProductList"
                      stripe
                      border
                      height="100%"
                      ref="multipleProductTable"
                      @selection-change="handleSelectionProduct"
                      @row-click="handleRowClickProduct"
                      :index="getProductIndex"
                      slot="table">
              <el-table-column
                type="selection"
                width="55">
              </el-table-column>
              <el-table-column
                type="index"
                label="序号"
                width="50">
              </el-table-column>
              <el-table-column
                v-for="item in systemProductListColumn"
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
        <el-button type="primary" size="mini" @click="saveSelectProduct">添加</el-button>
         <el-button size="mini" @click="cancelSelectProduct">取消</el-button>
      </span>
    </el-dialog>
    <!--  新增、编辑产品 -->
    <el-dialog :title="productDialogType=='add'?'新增产品':productDialogType=='edit'?'编辑产品':''"
               :visible.sync="productDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true"
               width="80%" class="dialog-style">
      <el-form :model="productForm" ref="productFormRef" label-width="120px" size="mini"
               style="display: flex;flex-wrap: wrap;">
        <el-form-item label="产品类型" prop="typeId">
          <el-cascader
            style="width: 170px"
            placeholder="请选择产品类型"
            v-model="productForm.typeId"
            @change="cascaderChange"
            :props="{value:'id',label:'typeName'}"
            :show-all-levels="false"
            :options="productTypeData"
            filterable @blur="productTypeInput"
            clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="玻璃层数">
          <el-select :disabled="productDialogType=='edit'" v-model="productForm.semiNum" @change="layerChange"
                     style="width: 170px" placeholder="请选择">
            <el-option
              v-for="(item,index) in dict.type.sys_glass_layer"
              :key="index"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="产品编号" prop="no">
          <el-input style="width: 170px" v-model="productForm.no" placeholder="请输入产品编号"></el-input>
        </el-form-item>
        <el-form-item label="产品价格(元)" prop="price">
          <el-input style="width: 170px" v-model="productForm.price" placeholder="请输入产品价格"></el-input>
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input style="width: 460px" v-model="productForm.productName" placeholder="请输入产品名称"></el-input>
        </el-form-item>
        <div style="display:flex;align-items:center;height:36px;color: red;margin-left: 50px;font-size: 14px;">
          产品名称为发货单产品名称
        </div>
<!--        <el-form-item label="产品全称">-->
<!--          &lt;!&ndash;          <div style="width: 460px;">{{productForm.fullName}}</div>&ndash;&gt;-->
<!--          &lt;!&ndash;          <el-input style="width: 460px;color: #4d4d4d" v-model="productForm.fullName" disabled></el-input>&ndash;&gt;-->
<!--          <el-input style="width: 460px;color: #4d4d4d" v-model="productForm.fullName" placeholder="请设置产品全称"-->
<!--                    disabled></el-input>-->
<!--        </el-form-item>-->

      </el-form>
      <el-table highlight-current-row
                v-if="productForm.semiProduct.length > 0"
                :data="productForm.semiProduct"
                border
                style="width: 100%" height="calc(30vh)">
        <el-table-column
          prop="attribute"
          label="属性" show-overflow-tooltip width="100">
        </el-table-column>
        <el-table-column
          label="厚度(mm)" width="160" show-overflow-tooltip>
          <template slot-scope="scope">
            <!--            <el-input class="table-input" v-model="scope.row.thick" placeholder="请输入厚度"></el-input>-->
            <el-input-number size="mini" :min="1" :step="1" :precision="0" v-model="scope.row.thick"
                             style="width: 120px"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column
          label="单片名称" show-overflow-tooltip min-width="300">
          <template slot-scope="scope">
            <el-input @input="getFullName" class="table-input" v-model="scope.row.semiProductName"
                      placeholder="请输入单片名称(流程卡单片名称)" style="width: 85%"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="品类" show-overflow-tooltip min-width="150">
          <template slot-scope="scope">
            <el-select v-model="scope.row.type" class="table-input" placeholder="请选择品类" style="width: 85%">
              <el-option
                v-for="item in dict.type.semi_product_type"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          prop="address"
          label="工艺流程" show-overflow-tooltip min-width="300">
          <template slot-scope="scope">
            <el-select filterable v-model="scope.row.craftFlow" class="table-input" placeholder="请选择工艺流程"
                       style="width: 85%">
              <el-option
                v-for="item in craftFlowOptions"
                :key="JSON.stringify(item)"
                :label="item.craftFlowTxt"
                :value="JSON.stringify(item)">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="productSave('productFormRef')">确定</el-button>
      <el-button size="mini" @click="productDialog=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  上传文件  -->
    <el-dialog title="上传附件" :visible.sync="uploadFileDialog" width="30%" class="dialog-style uploadFileDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true"
               :before-close="handleClose">
      <el-upload drag
                 :auto-upload="false"
                 :action="uploadUrl()"
                 :on-change="fileChange"
                 :on-remove="handleRemove"
                 :file-list="fileList">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <!--        <div class="el-upload__tip" slot="tip">只能上传xlsx文件，且不超过10M</div>-->
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="submitUpload">上传</el-button>
        <!--         <el-button size="mini" @click="uploadFileDialog=false;fileList=[]">取消</el-button>-->
         <el-button size="mini" @click="cancelUpload">取消</el-button>
       </span>
    </el-dialog>
  </div>
</template>

<script>

import {HyperFormula} from 'hyperformula';
import SlotTable from "@/components/public/table/SlotTable";
import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import {listAddition} from "@/api/system/addition";
import {addCustomer,addOrderCustomer, customerList, customerTypeList, delCustomer, editCustomer} from "@/api/system/customer";
import {
  listOrder,
  listProduct,
  orderNumber,
  addRecord,
  addRecordCache,
  reallyDelOrder,
  downloadTemplate,
  importTemplate, update
} from "@/api/salse/order/order";
import {
  getProductType,
  getProduct,
  getSemiProduct,
  addProduct,
  editProduct,
  delProduct,
  nameGetSemiProduct
} from "@/api/system/product";
import {keepThreeNum, getCurrentDay, isNumber, sum, keepOneNum} from "@/utils/order/order";
import {Message, MessageBox} from "element-ui";
import {getAllCraftFlowList} from "@/api/system/craft";
import {queryProduct} from "@/api/system/product";
import {groupBy} from "@/utils/groupBy";
import {saveFile} from "@/utils/saveFile";
import {getDefaultTree} from "@/utils/getDefaultTree";

export default {
  dicts: ['order_settlement', 'sys_glass_layer', 'semi_product_type', 'order_type'],
  components: {MainFlexibleTree, SlotTable},
  // name: "AddOrder"
  created() {
    this.queryCache();
    this.getAdditionList();
    customerList({
      pagenum: 1,
      pageSize: 100000
    }).then(res => {
      this.customerNameList = res.data;
    })
  },
  mounted() {
    let hot = this.$refs.hotTable.hotInstance;
    hot.addHook('afterBeginEditing', this.myAfterBeginEditing);
    hot.addHook('afterChange', this.getNum);
    hot.addHook('afterCopy', this.afterCopy);
  },
  data() {
    //数值、小数点验证
    var checkEmergencyFee = (rule, value, callback) => {
      let reg = /^[+]?(0|([1-9]\d*))(\.\d+)?$/g;
      if (!reg.test(value)) {
        callback(new Error('请输入大于等于0的数字'));
      } else {
        callback();
      }
    };
    /*校验电话码格式 */
    const isPhone = (rule, value, callback) => {
      const reg = /^1(3|4|5|6|7|8|9)\d{9}$/;
      if (!reg.test(value)) {
        callback(new Error('请输入正确的手机号格式'));
      } else {
        callback();
      }
    };
    return {
      customerNameList:[],
      copyData: [],
      //查询产品字段
      queryDetail: {
        orderId: ""
      },
      //订单信息
      basicInfoForm: {
        additionalCost: 0,                          //附加费
        additionalIds: [],                          //附加项id集合
        contactNumber: "",                          //联系电话
        contacts: "",                               //联系人
        customNo: "",                               //自定义编号
        customerId: "",                             //客户id
        customerName: "",                           //客户名称
        entryName: "",                              //项目名称
        orderAmount: 0,                             //订单总金额
        receivedAmount: 0,                           //已收款金额
        orderDeposit: 0,                            //定金
        orderNo: "",                                //订单编号
        orderNum: '',                                //订单数量
        orderType: "普通订单",                               //订单类型
        preparationDate: getCurrentDay(),                        //下单日期
        preparer: localStorage.getItem("nickName"),                               //下单人
        productList: [],                            //产品集合
        receiptAddress: "",                         //收货地址
        receiptDate: "",                            //收货日期
        remarks: "",                                //生产备注
        orderRemarks: "",                           //订单备注
        totalArea: 0,                               //总面积
        totalWeigh: 0,                              //总重量
        totalLengthen: 0,                           //总周长
        urgentCost: 0,                              //加急费
        settlementMethod: "",                        //结算方式
        isCache: 1,                                 //是否是缓存数据
      },
      // // 交货时间不能大于当前时间
      // deliveryDateOptions: {
      //   disabledDate: time => {
      //     return time.getTime() < Date.now()
      //   },
      // },
      // // 下单时间不能晚于当前时间
      // preparationDateOptions: {
      //   disabledDate: time => {
      //     return time.getTime() > Date.now()
      //   },
      // },
      selectCustomerDialog: false,   //选择客户弹窗
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
        {label: '客户编号', prop: 'number', width: '180'},
        {label: '联系人', prop: 'contacts', width: '120'},
        {label: '联系电话', prop: 'phone', width: '130px'},
        {label: '邮箱', prop: 'mail', width: '200'},
        {label: '传真', prop: 'fax', width: '180px'},
        {label: '客户地址', prop: 'address', width: '300'},
        {label: '创建时间', prop: 'createdAt', width: '180'},
        {label: '备注', prop: 'remarks'},
      ],  //表头
      selectedCustomer: [],  //客户列表被选中表格数据
      //订单类型
      orderTypeOptions: [{
        value: '普通订单',
        label: '普通订单'
      }, {
        value: '加急订单',
        label: '加急订单'
      }, {
        value: '外协订单',
        label: '外协订单'
      }],
      //附加项选中的值
      additionCheckList: [],
      additionCheckOptions: [],
      specialCheckOptions: [
        {label: '001', value: '大小头（宽）'},
        {label: '002', value: '大小头（高）'},
        {label: '003', value: '异形'},
        {label: '004', value: '弯弧'},
      ],
      formSize: '收起',   //表单最大化
      checkedSize: '更多',   //配置列最大化
      formSizeIcon: 'el-icon-arrow-up',   // 表单最大化图标
      checkedSizeIcon: 'el-icon-plus', // 配置列最大化图标
      formFlag: false,
      checkFlag: false,
      hotSettings: {
        licenseKey: 'non-commercial-and-evaluation',
        data: [],
        width: '100%',
        hiddenColumns: {
          copyPasteEnabled: false,
          columns: [0]
        },
        formulas: {
          engine: HyperFormula,  //计算插件  暂时用不到
        },
        columns: [   //每一列对应的数据和数据类型
          {
            data: 'productId',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中

          },
          {
            data: 'productName',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'printName',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'wideHead',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'highHead',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'num',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'unitPrice',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'position',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'requirement',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'remarks',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'remarksOne',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'remarksTwo',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'curve',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'lengthen',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'singleArea',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'singleClearingArea',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'productArea',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'productWeight',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'productAmount',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
        ],
        dropdownMenu: ['alignment', '---------', 'filter_by_condition', 'filter_by_value', 'filter_action_bar'],  // 对齐，筛选条件，按值过滤
        rowHeaders: true,
        colHeaders: ['产品Id', '产品名称','打印名称' , '宽（mm）', '高（mm）', '数量（片）', '单价（元/㎡）', '位置', '加工要求', '备注一', '备注二', '备注三', '延长米数（m）', '总周长（m）', '单片实际面积（m²）', '单片结算面积（m²）', '总面积（m²）', '总重量（t）', '总价（元）'],
        contextMenu: {
          items: {
            "row_above": {
              name: "向上插入一行"
            },
            "row_below": {
              name: "向下插入一行"
            },
            "remove_row": {
              name: "移除当前行"
            },
            'alignment': {
              name: '对齐'
            },
            'copy': {
              name: `复制<span style="margin-left: 70%">Ctrl+C</span>`,
            },
            'cut': {
              name: `剪切<span style="margin-left: 70%">Ctrl+X</span>`
            },
            'paste': {
              name: '粘贴<span style="margin-left: 70%" class="onPaste">Ctrl+V</span>',
              callback: () => {
                var plugin = this.$refs.hotTable.hotInstance.getPlugin('copyPaste');
                this.$refs.hotTable.hotInstance.listen();
                plugin.paste(this.copyData);
              }
            }

          }
        },
        language: 'zh-CN',
        manualColumnMove: false, //移动列
        manualRowMove: true,  //移动行
        manualColumnResize: true,  //手动更改列距
        minCols: 30, //最小列数
        minRows: 50, //最小行数
        fixedColumnsLeft: 6, //固定左侧6列
        fixedRowsTop: 0,//固定表头
        autoColumnSize: true, // 自适应列大小
        filters: true,  //筛选
        columnSorting: true, //排序
        copyPaste: true,  //允许粘贴
        fillHandle: true, //允许拖拽复制
        stretchH: 'all',  //根据宽度横向拓展
      },
      editRow: 0,   //handsonTable 编辑行
      selectProductDialog: false,   //选择产品弹窗
      productTypeList: [],  //产品集合
      productTypeData: [{id: 0, typeName: "产品类型"}], //产品类型
      typeList: [],
      productTypeProps: {
        children: 'children',
        label: 'typeName'
      },
      // 产品查询参数
      productParams: {
        productName: '',
        no: '',
        typeId: '',
        thick: '',
        pageSize: 20,
        pageNum: 1,
      },
      systemProductList: [],         // 产品表格数据
      // 产品表头
      systemProductListColumn: [
        {label: '产品名称', prop: 'productName', width: '200'},
        {label: '产品编号', prop: 'no', width: '100'},
        {label: '单价（元/m²）', prop: 'price', width: '100'},
        // {label: '厚度（mm）', prop: 'thick', width: '100'},
        // {label: '产品名称(全称)', prop: 'fullName', width: '250'},
        {label: '产品类型', prop: 'type', width: '100'},
      ],
      productPageSize: 20,
      productPageNum: 1,
      productTotal: 0,
      selectedProduct: [],  //产品列表被选中表格数据
      productDialogType: 'add', //弹窗类型
      //产品表单
      productForm: {
        productName: "",
        fullName: "",
        thick: "",
        type: "",
        typeId: "",
        no: "",
        price: "",
        semiNum: "",
        semiProduct: []
      },
      productFormRules: {
        productName: [
          {required: true, message: '请输入产品名称', trigger: 'blur'}
        ],
        typeId: [
          {required: true, message: '请选择产品类型', trigger: 'blur'}
        ],
        no: [
          {required: true, message: '请输入编号', trigger: 'blur'}
        ],
        price: [
          {required: true, message: '请输入价格', trigger: 'blur'},
          {pattern: /^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/g, message: '请输入大于0的整数或小数'}
        ]
      },
      productDialog: false,

      craftFlowOptions: [],
      glassAttribute: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'],
      //总合计
      summation: [
        {label: 'orderNum', title: '总数量', value: 0, unit: '片', label2: 'num'},
        {label: 'totalLengthen', title: '总周长', value: 0, unit: 'm', label2: 'lengthen'},
        {label: 'totalArea', title: '总面积', value: 0, unit: 'm²', label2: 'productArea'},
        {label: 'totalWeigh', title: '总重量', value: 0, unit: 't', label2: 'productWeight'},
        {label: 'orderAmount', title: '总金额', value: 0, unit: '元', label2: 'productAmount'},
      ],
      selectedHand: [],   //选择弹窗产品后的二维数组单元格
      // flag:true,
      // 表单校验
      rules: {
        customNo: [
          {max: 125, message: '字符长度最大不得超过125', trigger: 'blur'}
        ],
        customerName: [
          {required: true, message: "客户名称不能为空", change: "blur"},
          {max: 125, message: '字符长度最大不得超过125', trigger: 'blur'}
        ],
        orderType: [
          {required: true, message: "订单类型不能为空", trigger: "blur"}
        ],
        receiptDate: [
          {required: true, message: '交货日期不能为空', trigger: 'blur'}
        ],
        orderDeposit: [
          {pattern: /^[+]?(0|([1-9]\d*))(\.\d+)?$/g, message: '请输入大于等于0的整数或小数'}
        ],
        urgentCost: [
          {pattern: /^[+]?(0|([1-9]\d*))(\.\d+)?$/g, message: '请输入大于等于0的整数或小数'}
        ],
        additionalCost: [
          {pattern: /^[+]?(0|([1-9]\d*))(\.\d+)?$/g, message: '请输入大于等于0的整数或小数'}
        ],
        contactNumber: [
          {validator: isPhone, change: 'blur'}
        ],
      },
      //上传文件
      uploadFileDialog: false,
      fileList: [],
    }
  },
  watch: {
    /* 附加项筛选 */
    additionCheckList() {
      let hot = this.$refs.hotTable.hotInstance;
      if (this.additionCheckOptions) {
        let insertIndex = this.hotSettings.colHeaders.findIndex((el) => {
          return el === '加工要求'
        })
        this.hotSettings.colHeaders = ['产品Id', '产品名称','打印名称', '宽（mm）', '高（mm）', '数量（片）', '单价（元/㎡）', '位置', '加工要求', '备注一', '备注二', '备注三', '延长米数（m）', '总周长（m）', '单片实际面积（m²）', '单片结算面积（m²）', '总面积（m²）', '总重量（t）', '总价（元）'];
        this.hotSettings.columns = [   //每一列对应的数据和数据类型
          {
            data: 'productId',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'productName',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'printName',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'wideHead',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'highHead',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'num',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'unitPrice',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'position',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'requirement',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'remarks',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'remarksOne',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'remarksTwo',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'curve',
            type: 'text',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'lengthen',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'singleArea',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'singleClearingArea',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'productArea',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'productWeight',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
          {
            data: 'productAmount',
            type: 'numeric',
            className: 'htCenter htMiddle'  //水平垂直居中
          },
        ];
        //附加项
        this.additionCheckOptions.forEach(item => {
          if (this.additionCheckList.includes(item.id)) {
            let strings = item.computeType.split(',');
            //先判断附加项类型
            if (strings[0] == "数量") {//计算方式为    数量*价格
              //添加相应字段
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: item.additionalAlias + "_oneNumber",//每片中附加项数量
                  type: 'text',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
                {
                  data: item.additionalAlias + "_unitPrice",//附加项单价
                  type: 'text',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
                {
                  data: item.additionalAlias + "_allAmount",//附加项总金额
                  type: 'text',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                "每片" + item.additionalName + "数量",
                item.additionalName + "单价",
                item.additionalName + "总额"
              );
            } else if (strings[0] == "面积") {//计算方式为      面积*价格
              //添加相应字段
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: item.additionalAlias + "_unitPrice",//附加项单价
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
                {
                  data: item.additionalAlias + "_allAmount",//附加项总金额
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                item.additionalName + "单价",
                item.additionalName + "总额"
              );
            } else if (strings[0] == "周长") {
              //添加相应字段
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: item.additionalAlias + "_unitPrice",//附加项单价
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
                {
                  data: item.additionalAlias + "_allAmount",//附加项总金额
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                item.additionalName + "单价",
                item.additionalName + "总额"
              );
            } else if (strings[0] == "边长") {
              //添加相应字段
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: item.additionalAlias + "_type",//附加项单价
                  type: 'dropdown',
                  source: ['矩形—4边', '矩形—2宽', '矩形—2高', '矩形—2宽1高', '矩形—2高1宽', '矩形—1宽', '矩形—1高', '矩形—1宽1高',],
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
                {
                  data: item.additionalAlias + "_unitPrice",//附加项单价
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
                {
                  data: item.additionalAlias + "_allAmount",//附加项总金额
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                item.additionalName + "类型",
                item.additionalName + "单价",
                item.additionalName + "总额"
              );
            }
          }
        })
        //特殊产品
        this.specialCheckOptions.forEach(item => {
          if (this.additionCheckList.includes(item.label)) {
            if (item.label == "001") { //宽大小头
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: "wideLittleHead",//
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                "宽小头"
              );
            } else if (item.label == "002") {//高大小头
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: "highLittleHead",//执行数量
                  type: 'numeric',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                "高小头"
              );
            } else if (item.label == "003") {//异形
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: item.label,//执行数量
                  type: 'text',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                item.value
              );
            } else if (item.label == "004") {//弯弧
              this.hotSettings.columns.splice(insertIndex + 1, 0,
                {
                  data: "innerArcLength",//内弧长
                  type: 'text',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
                {
                  data: "arcDiameter",//弯弧直径
                  type: 'text',
                  className: 'htCenter htMiddle'  //水平垂直居中
                },
              )
              this.hotSettings.colHeaders.splice(insertIndex + 1, 0,
                "内弧长", "弯弧直径"
              );
            }
          }
        })

        hot.updateSettings({
          columns: this.hotSettings.columns,
          colHeaders: this.hotSettings.colHeaders
        }, false);
      }
    },
  },

  methods: {
    selectBlur(e) {
      let value = e.target.value;
      if(value) {
        this.basicInfoForm.customerName = value
      }
    },
    productTypeInput(e) {
      let value = e.target.value;
      if(value) {
        let isAdd = this.productTypeData[0]["children"].filter(item=>{
          return item.typeName == value
        })
        if(isAdd.length <= 0){
          this.productTypeData[0]["children"] = this.productTypeData[0]["children"].filter(item=>{
            return item.id != '000'
          }).concat({
            id:"000",
            typeName:value
          })
          this.productForm.typeId = "000"
        }else{
          this.productForm.typeId = isAdd[0].id
        }
      }
    },
    customerTypeInput(e) {
      let value = e.target.value;
      if(value) {
        let isAdd = this.customerTypeData[0]["children"].filter(item=>{
          return item.typeName == value
        })
        if(isAdd.length <= 0){
          this.customerTypeData[0]["children"] = this.customerTypeData[0]["children"].filter(item=>{
            return item.id != '000'
          }).concat({
            id:"000",
            typeName:value
          })
          this.customerForm.typeId = "000"
        }else{
          this.customerForm.typeId = isAdd[0].id
        }

        console.log(this.customerForm)
      }
    },
    /*查询是否有缓存数据*/
    queryCache() {
      let params = {
        pageNum: 1,
        pageSize: 20,
        isCache: 1,
        isDel: 0,
      }
      listOrder(params).then(res => {
        if (res.code === 200 && res.count === 1) {
          this.basicInfoForm = res.data[0]
          this.additionCheckList = res.data[0].orderAdditional.split(", ")
          this.queryDetail.orderId = res.data[0].id
          this.queryProduct();
        } else {
          this.getOrderNumber();
        }
      })
    },
    /* 查询订单中的产品 */
    queryProduct() {
      let hot = this.$refs.hotTable.hotInstance;
      listProduct(this.queryDetail).then(response => {
        if (response.code === 200) {
          let arr = [];
          response.data.forEach(one => {
            let arrOne = [];
            this.hotSettings.columns.forEach(item => {
              let x = item.data;
              if (x.indexOf("_") >= 0 && one.additionalMap != null) {
                arrOne.push(one.additionalMap[x])
              } else {
                arrOne.push(one[x])
              }
            })
            arr.push(arrOne)

          })
          if (arr.length > 0) {
            hot.populateFromArray(0, 0, arr)
          }
        }
        this.orderDetails = response.data;
      })
    },
    /*获取订单编号*/
    getOrderNumber() {
      orderNumber().then(response => {
        if (response.code === 200) {
          // console.log(response)
          this.basicInfoForm.orderNo = response.msg;
        }
      });
    },
    /* 选择客户 */
    handleBlur() {
      this.selectCustomerDialog = true;
      // this.selectedCustomer=[];
      // 客户查询参数
      this.customerParams = {
        number: '',
        customerName: '',
        address: '',
        typeId: '',
        pageNum: 1,
        pageSize: 20,
      };
      this.getCustomerTypeList();
      this.getCustomerList();
      // this.$refs.multipleCustomerTable.clearSelection();
    },
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
      if(this.customerParams.typeId != "") {
        this.customerForm.typeId = this.customerParams.typeId
      }else{
        let defaultVal = getDefaultTree(this.customerTypeData)
        this.customerForm.typeId = defaultVal
      }
      console.log(this.customerForm)
      this.customerDialogType = 'add';
      this.customerDialog = true
    },
    customerSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.customerForm.typeId = Array.isArray(this.customerForm.typeId) ? this.customerForm.typeId[this.customerForm.typeId.length - 1] : this.customerForm.typeId;
          this.customerForm.typeName = this.customerTypeData[0]["children"].filter(item=>{
            return item.id == this.customerForm.typeId
          })[0].typeName
          if (this.customerDialogType == 'add') {
            addCustomer(this.customerForm).then(res => {
              if (res.code == 200) {
                Message.success(res.msg)
                this.customerDialog = false
                this.getCustomerList();
                this.getCustomerTypeList();
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
                this.getCustomerTypeList();
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
      })
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
      if (val.length === 0) {
        this.selectedCustomer = [];
      }
    },
    /* 客户列表点击行 */
    handleRowClickCustomer(row, column, event) {
      this.$refs.multipleCustomerTable.toggleRowSelection(row, column)
    },
    /* 保存选择客户弹窗 */
    saveSelectCustomer() {
      this.selectCustomerDialog = false;
      if (this.selectedCustomer !== undefined && this.selectedCustomer.length > 0) {
        this.basicInfoForm.customerName = this.selectedCustomer[0].customerName;
        this.basicInfoForm.receiptAddress = this.selectedCustomer[0].address;
        this.basicInfoForm.contactNumber = this.selectedCustomer[0].phone;
        this.basicInfoForm.contacts = this.selectedCustomer[0].contacts;
        this.$refs.customerNameInput.blur();
        this.$refs.multipleCustomerTable.clearSelection();
      }
    },
    cancelSelectCustomer() {
      this.selectCustomerDialog = false;
      this.$refs.multipleCustomerTable.clearSelection();
    },
    /* 客户分页器 */
    handleCustomerChange(size, num) {
      this.customerPageSize = size;
      this.customerPageNum = num;
      this.customerParams.pageSize = this.customerPageSize;
      this.customerParams.pageNum = this.customerPageNum;
      this.getCustomerList();
    },
    /* 获取附加项列表 */
    getAdditionList() {
      listAddition().then(response => {
        // console.log(response)
        if (response.code === 200) {
          this.additionCheckOptions = response.data;
        }
      });
    },
    /* 调整基本信息最大化 */
    changeBasicInfo() {
      let basicInfo = document.querySelector('.basicInfo');
      let handTable = document.querySelector('.handTable');
      let hot = this.$refs.hotTable.hotInstance;
      if (!this.formFlag) {
        this.formSizeIcon = 'el-icon-arrow-down';
        this.formSize = '展开';
        basicInfo.style.height = '80px';
        basicInfo.style.overflow = 'hidden';
        handTable.style.height = '100%';
        hot.render();
      }
      if (this.formFlag) {
        this.formSizeIcon = 'el-icon-arrow-up';
        this.formSize = '收起';
        basicInfo.style.height = 'auto';
        basicInfo.style.overflow = 'auto';
        handTable.style.height = '100%';
        hot.render();
      }
      this.formFlag = !this.formFlag;
    },
    /* 调整配置列最大化 */
    changeBasicChecked() {
      let basicChecked = document.querySelector('.basicChecked');
      let handTable = document.querySelector('.handTable');
      let hot = this.$refs.hotTable.hotInstance;
      if (!this.checkFlag) {
        this.checkedSizeIcon = 'el-icon-minus';
        this.checkedSize = '收起';
        // basicChecked.style.flexDirection = 'column';
        basicChecked.style.height = 'calc(100% - 30px)';
        basicChecked.style.lineHeight = '30px';
        basicChecked.style.overflow = 'auto';
        handTable.style.height = '100%';
        hot.render();
      }
      if (this.checkFlag) {
        this.checkedSizeIcon = 'el-icon-plus';
        this.checkedSize = '展开';
        basicChecked.style.flexDirection = 'row';
        basicChecked.style.height = '35px';
        basicChecked.style.lineHeight = '30px';
        basicChecked.style.overflow = 'auto';
        handTable.style.height = '100%';
        hot.render();
      }
      this.checkFlag = !this.checkFlag;
    },
    /* 产品选择弹窗获取产品列表 */
    getProductTypeList() {
      getProductType().then(response => {
        if (response.code === 200) {
          this.productTypeList = response.data;
          this.productTypeData = [{id: 0, typeName: "产品类型", children: groupBy(response.data, "tid")}]
        }
      })
    },
    /* 点击产品类型树，选择产品 */
    handleProductNodeClick(val, node, component) {
      this.productParams.typeId = val.id;
      this.getProductList();

    },
    /*  查询产品信息 */
    getProductList() {
      if (this.productParams.typeId == 0) {
        this.productParams.typeId = '';
      }
      getProduct(this.productParams).then(response => {
        // console.log('000', response);
        if (response.code === 200) {
          this.systemProductList = response.data;
          this.productTotal = response.count;
        }
      })
    },
    /* 翻页后，序号连贯 */
    getProductIndex($index) {
      //  表格序号
      return (this.productPageNum - 1) * this.productPageSize + $index + 1;
    },
    /* 获取工艺流程 */
    getCraftFlow() {
      getAllCraftFlowList().then(res => {
        if (res.code === 200) {
          this.craftFlowOptions = res.data
        }
      })
    },
    /* 新增产品信息 */
    handleAdd() {
      this.productForm = {
        productName: "",
        fullName: "",
        thick: "",
        type: "",
        typeId: this.productParams.typeId,
        no: "",
        price: "",
        semiProduct: []
      };
      if(this.productParams.typeId != "") {
        this.productForm.typeId = this.productParams.typeId
      }else{
        let defaultVal = getDefaultTree(this.productTypeData)
        this.productForm.typeId = defaultVal
      }
      this.productDialogType = 'add';
      this.productDialog = true;
      this.getCraftFlow();
    },
    cascaderChange(e) {
      console.log(e)
    },
    layerChange(e) {
      let str = new Array(Number(e)).fill("").map((item, index) => {
        return JSON.stringify({
          semiProductName: '',
          attribute: this.glassAttribute[index] + '片',
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
      let arr = JSON.parse(`[${str}]`).map((item, index) => {
        item.sort = index
        return item
      })
      this.productForm.semiProduct = arr
    },
    getFullName() {
      let name = ""
      this.productForm.semiProduct.forEach(item => {
        if (item.semiProductName != "") {
          if (name == "") {
            name = item.semiProductName
          } else {
            name += `+${item.semiProductName}`
          }
        }
      })
      this.productForm.fullName = name
    },
    productSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.productForm.semiProduct.length <= 0) {
            this.$message({
              message: '"请选择层数后，输入半产品信息"',
              type: 'warning'
            });
            return false
          }
          if (this.productDialogType == 'add') {
            let isOk = true
            this.productForm.semiProduct = this.productForm.semiProduct.map(item => {
              try {
                let craftFlow = JSON.parse(item.craftFlow)
                item.craftFlow = craftFlow.craftFlowTxt
                item.craftFlowId = craftFlow.id
                return item
              } catch (e) {
                return item
              }
            })
            try {
              this.productForm.semiProduct.forEach(item => {
                let keys = Object.keys(item)
                if (item['attribute'] != '中间层') {
                  keys.forEach(item2 => {
                    if (item[item2] + "" == "" || item[item2] + "" == null) {
                      console.log(item[item2])
                      console.log(item2)
                      throw new Error("请将半产品列表中所有项填写后进行保存")
                      return false
                    }
                  })
                }
              })
            } catch (e) {
              isOk = false
              Message.error(e.message)
            }
            if (isOk) {
              this.productForm.typeId = Array.isArray(this.productForm.typeId) ? this.productForm.typeId[this.productForm.typeId.length - 1] : this.productForm.typeId
              this.productForm.type = this.productTypeData[0]["children"].filter((item) => {
                return this.productForm.typeId == item.id
              })[0].typeName
              addProduct(this.productForm).then(res => {
                if (res.code == 200) {
                  Message.success(res.msg)
                  this.productDialog = false
                  this.getProductList();
                  this.getProductTypeList();
                }
              })
            }
            return false
          }
          if (this.productDialogType == 'edit') {
            let isOk = true
            this.productForm.semiProduct = this.productForm.semiProduct.map(item => {
              try {
                let craftFlow = JSON.parse(item.craftFlow)
                item.craftFlow = craftFlow.craftFlowTxt
                item.craftFlowId = craftFlow.id
                return item
              } catch (e) {
                return item
              }
            })
            try {
              this.productForm.semiProduct.forEach(item => {
                let keys = Object.keys(item)
                if (item['attribute'] != '中间层') {
                  keys.forEach(item2 => {
                    if (item[item2] + "" == "" || item[item2] + "" == null) {
                      console.log(item[item2])
                      console.log(item2)
                      throw new Error("请将半产品列表中所有项填写后进行保存")
                      return false
                    }
                  })
                }
              })
            } catch (e) {
              isOk = false
              Message.error(e.message)
            }
            if (isOk) {
              this.productForm.typeId = Array.isArray(this.productForm.typeId) ? this.productForm.typeId[this.productForm.typeId.length - 1] : this.productForm.typeId
              this.productForm.type = this.productTypeData[0]["children"].filter((item) => {
                return this.productForm.typeId == item.id
              })[0].typeName
              // console.log(productForm.value)
              editProduct(this.productForm.id, this.productForm).then(res => {
                if (res.code == 200) {
                  Message.success(res.msg)
                  this.productDialog = false
                  this.getProductList();
                  this.getProductTypeList();
                }
              })
            }
            return false
          }
        } else {
          return false;
        }
      });
    },
    /* 编辑产品信息 */
    handleUpdate() {
      if (this.selectedProduct.length <= 0) {
        Message.warning("请选择要修改的数据");
        return false
      }
      if (this.selectedProduct.length > 1) {
        Message.warning("请选择一条数据进行修改");
        return false
      }

      getSemiProduct(this.selectedProduct[0].id).then(res => {
        if (res.code == 200) {
          this.productForm = this.selectedProduct[0];
          this.productForm.semiProduct = res.data
          this.productDialogType = 'edit'
          this.productDialog = true
        }
      })
    },
    /* 删除产品信息 */
    handleDelete() {
      if (this.selectedProduct.length <= 0) {
        Message.warning("请选择要删除的数据");
        return false
      }
      MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = this.selectedProduct.map(item => {
          return item.id
        }).join(",")
        delProduct(ids).then(res => {
          if (res.code == 200) {
            Message.success(res.msg);
            this.getProductList();
          }
        })
      })
    },
    /* 产品分页器 */
    handleProductChange(size, num) {
      this.productPageSize = size;
      this.productPageNum = num;
      this.productParams.pageSize = this.productPageSize;
      this.productParams.pageNum = this.productPageNum;
      this.getProductList();
    },
    /* 产品列表复选框 */
    handleSelectionProduct(val) {
      this.selectedProduct = val;
    },
    /* 产品列表点击行 */
    handleRowClickProduct(row, column) {
      this.$refs.multipleProductTable.toggleRowSelection(row, column)
    },
    /* 保存数据 */
    saveSelectProduct() {
      let hot = this.$refs.hotTable.hotInstance;
      // 使用二维数组填充单元格
      if (this.selectedProduct !== undefined && this.selectedProduct.length > 0) {
        //选择的内容转成二位数组，但是数据要一一对应
        let arr = [];
        let productIdIndex = this.hotSettings.columns.findIndex((el) => {
          return el.data === 'productId'
        });
        let productNameIndex = this.hotSettings.columns.findIndex((el) => {
          return el.data === 'productName'
        });
        let priceIndex = this.hotSettings.columns.findIndex((el) => {
          return el.data === 'unitPrice'
        })
        for (let i in this.selectedProduct) {
          arr[i] = [];
          for (let j in this.selectedProduct[i]) {
            //属性为  productName 和 unitPrice 时， 值不为空
            if (j === 'productName') {
              arr[i][productNameIndex] = this.selectedProduct[i][j]
            }
            if (j === 'price') {
              arr[i][priceIndex] = this.selectedProduct[i][j]
            }
            if (j === 'id') {
              arr[i][productIdIndex] = this.selectedProduct[i][j]
            }
          }
          this.selectedHand = arr;
        }
        this.editRow += this.selectedProduct.length
        // 使用二维数组填充单元格
        hot.populateFromArray(this.editRow - this.selectedProduct.length, productIdIndex, this.selectedHand);
        this.$refs.multipleProductTable.clearSelection();
        this.$message({
          message: '添加产品成功',
          type: 'success'
        });
        this.selectProductDialog = false;
      }
    },
    cancelSelectProduct() {
      this.selectProductDialog = false;
      this.$refs.multipleProductTable.clearSelection();
    },
    /* 获取编辑 */
    myAfterBeginEditing(row, col) {
      this.editRow = row;
      console.log(this.editRow)
      let editProductCol = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productName'
      })
      if (editProductCol === col) {
        this.selectProductDialog = true;
        this.productParams = {
          productName: '',
          no: '',
          typeId: '',
          thick: '',
        };
        this.getProductTypeList();
        this.getProductList();
      }
    },

    /* 获取面积和总金额 */
    getNum(change, source) {
      //change[0]是一个数组,第一个值是行索引/行名,第二个值是列索引/列名,第三个值是修改前的值,第四个值是修改后的值
      //source的值目前知道:loadData(加载数据);edit(新增或编辑)
      let hot = this.$refs.hotTable.hotInstance;
      let handData = this.hotSettings.data;
      new Promise((resolve, reject)=>{
        //编辑、复制粘贴、下拉数据时更新
        if (source === 'edit' || source === 'CopyPaste.paste' || source === 'Autofill.fill') {
          if (change) {
            change.forEach(async (changeItem) => {
              let changeOne = changeItem[0];
              let productId = handData[changeOne].productId;
              let nWidth = handData[changeOne].wideHead;
              let nHeight = handData[changeOne].highHead;
              let nNum = handData[changeOne].num;
              let nPrice = handData[changeOne].unitPrice;
              if (nWidth) {
                if (!isNumber(nWidth) || Number(nWidth) < 0) {
                  this.$set(this.hotSettings.data[changeOne], 'wideHead', 0);
                  nWidth = 0
                  if (source !== 'populateFromArray') {
                    this.$message.warning("产品宽度为大于0的数值！")
                  }
                }
              } else {
                if (handData[changeOne].productName) {
                  nWidth = 0
                }
              }
              if (nHeight) {
                if (!isNumber(nHeight) || Number(nHeight) < 0) {
                  this.$set(this.hotSettings.data[changeOne], 'highHead', 0);
                  nHeight = 0
                  if (source !== 'populateFromArray') {
                    this.$message.warning("产品高度为大于0的数值！")
                  }

                }
              } else {
                if (handData[changeOne].productName) {
                  nHeight = 0
                }
              }
              if (nNum) {
                if (!isNumber(nNum) || Number(nNum) < 0) {
                  this.$set(this.hotSettings.data[changeOne], 'num', 0);
                  nNum = 0
                  if (source !== 'populateFromArray') {
                    this.$message.warning("产品数量为大于0的数值！")
                  }
                }
              } else {
                if (handData[changeOne].productName) {
                  nNum = 0
                }
              }
              if (nPrice) {
                if (!isNumber(nPrice) || Number(nPrice) < 0) {
                  this.$set(this.hotSettings.data[changeOne], 'unitPrice', 0);
                  nPrice = 0
                  if (source !== 'populateFromArray') {
                    this.$message.warning("产品单价为大于0的数值！")
                  }
                }
              } else {
                if (handData[changeOne].productName) {
                  nPrice = 0
                }
              }
              //如果有长度、宽度、数量
              if (nWidth && nHeight && nNum) {
                //总周长为 （长度+宽度）*数量*2  注意单位换算
                handData[changeOne].lengthen = keepThreeNum((Number(nWidth) + Number(nHeight)) * Number(nNum) * 2 / 1000);
                //单片实际面积
                handData[changeOne].singleArea = keepThreeNum(Number(nWidth) * Number(nHeight) / 1000000);
                //单片结算面积
                handData[changeOne].singleClearingArea = keepThreeNum(Number(nWidth) * Number(nHeight) / 1000000);
                //总面积
                handData[changeOne].productArea = keepThreeNum(Number(nWidth) * Number(nHeight) * Number(nNum) / 1000000);
                if(nPrice){
                  //总价格
                  handData[changeOne].productAmount = keepThreeNum(Number(nWidth) * Number(nHeight) * Number(nNum) * Number(nPrice) / 1000000);
                }
                // console.log('handData[changeOne].productAmount ', Number(nWidth), Number(nHeight), Number(nNum), Number(nPrice))
                // let weight = 0;
                // getSemiProduct(productId).then(res => {
                //   res.data.forEach(item => {
                //     if (item.attribute != "中间层") {
                //       let x = keepThreeNum((Number(item.thick) * Number(handData[changeOne].productArea) * 0.025));
                //       weight += x;
                //       this.$set(this.hotSettings.data[changeOne], 'productWeight', keepThreeNum(weight / 10));
                //     }
                //   })
                // })
                //总重量
                let weight = 0;
                if (handData[changeOne].productName && nWidth && nHeight && nNum) {
                  await nameGetSemiProduct({productNames: handData[changeOne].productName}).then(res => {
                    console.log(res)
                    if (res.code === 200) {
                      if (res.data) {
                        if (res.data[0].productName === handData[changeOne].productName) {
                          if (res.data[0].semiProduct) {
                            res.data[0].semiProduct.forEach(semiItem => {
                              if (semiItem.attribute != '中间层') {
                                let x =Number(semiItem.thick) * Number(handData[changeOne].productArea) * 0.025;
                                weight += x;
                              }
                            })
                            weight=keepThreeNum(weight)
                            this.$set(this.hotSettings.data[changeOne], 'productWeight', keepThreeNum(weight / 10))
                            resolve()
                          }
                        }
                      }
                    }
                  })
                }
                if (this.additionCheckList) {
                  this.additionCheckList.forEach(item => {
                    this.additionCheckOptions.forEach(one => {
                      if (item == one.id) {
                        if (handData[changeOne][one.additionalAlias + "_unitPrice"]) {
                          let productNum = handData[changeOne]["num"];
                          let oneNumber;
                          let strings = one.computeType.split(',');
                          if (strings[0] == "数量") {
                            oneNumber = handData[changeOne][one.additionalAlias + "_oneNumber"];
                          } else if (strings[0] == "面积") {
                            oneNumber = handData[changeOne]["singleClearingArea"];
                          } else if (strings[0] == "周长") {
                            oneNumber = handData[changeOne]["lengthen"];
                            console.log("lengthen", oneNumber)
                          } else if (strings[0] == "边长") {
                            oneNumber = handData[changeOne][""];
                          }
                          let unitPrice = handData[changeOne][one.additionalAlias + "_unitPrice"];
                          handData[changeOne][one.additionalAlias + "_allAmount"] = keepThreeNum(productNum * oneNumber * unitPrice);
                          handData[changeOne].productAmount += handData[changeOne][one.additionalAlias + "_allAmount"];
                          handData[changeOne].productAmount = keepThreeNum(handData[changeOne].productAmount);
                        }
                      }
                    })
                  })
                }
              }
              setTimeout(()=>{
                resolve()
              },0)
            })
          }
        }
      }).then(res=>{
        console.log(85698)
        hot.updateSettings({
          data: this.hotSettings.data,
        }, false);
        this.$forceUpdate()
      })

    },
    /* 获取系统当前剪切板 */
    afterCopy(data, coords) {
      if (data) {
        this.copyData = data
      }
    },
    //下载excel模板
    downloadTemplate() {
      downloadTemplate().then(res => {
        saveFile(res)
      })
    },
    //导入
    importTemplate() {
      this.uploadFileDialog = true;
      this.fileList = [];
      let hot = this.$refs.hotTable.hotInstance;
      const arr = hot.getData();
      // console.log(this.editRow)
      let productNameIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productName'
      });
      for (let i = 0; i < arr.length; i++) {
        if (arr[i][productNameIndex]) {
          this.editRow = i + 1;
        } else {
          this.editRow += 0;
        }
      }
    },
    uploadUrl: function () {
      // 因为action参数是必填项，我们使用二次确认进行文件上传时，直接填上传文件的url会因为没有参数导致api报404，所以这里将action设置为一个返回为空的方法就行，避免抛错
      return ''
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    fileChange(file) {
      this.fileList = [];
      this.fileList.push(file.raw);
    },
    /* 上传文件 */
    submitUpload() {
      if (this.fileList.length <= 0) {
        this.$message.error('请选择文件');
        return
      } else {
        let form = new FormData()
        form.append('file', this.fileList[0])
        importTemplate(form).then(res => {
          // console.log(res)
          if (res.code === 200) {
            /* 将上传数据添加到表格中 */
            let hot = this.$refs.hotTable.hotInstance;
            // 使用二维数组填充单元格
            if (res.data !== undefined && res.data.length > 0) {
              //选择的内容转成二位数组，但是数据要一一对应
              let arr = [];
              const productName = new Set()
              let productNameIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'productName'
              });
              let productIdIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'productId'
              });
              let wideIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === "wideHead"
              })
              let highIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === "highHead"
              })
              let numIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === "num"
              })
              let priceIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'unitPrice'
              })
              let positionIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'position'
              })
              let requirementIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'requirement'
              })
              let remarksIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'remarks'
              })
              let lengthenIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'lengthen'
              })
              let singleAreaIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'singleArea'
              })
              let singleClearingAreaIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'singleClearingArea'
              })
              let productAreaIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'productArea'
              })
              let productWeightIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'productWeight'
              })
              let productAmountIndex = this.hotSettings.columns.findIndex((el) => {
                return el.data === 'productAmount'
              })
              for (let i in res.data) {
                arr[i] = [];
                for (let j in res.data[i]) {
                  if (j === 'productName') {
                    arr[i][productNameIndex] = res.data[i][j]
                  }
                  if (j === 'wideHead') {
                    arr[i][wideIndex] = res.data[i][j]

                  }
                  if (j === 'highHead') {
                    arr[i][highIndex] = res.data[i][j]
                  }
                  if (j === 'num') {
                    arr[i][numIndex] = res.data[i][j]
                  }
                  if (j === 'unitPrice') {
                    arr[i][priceIndex] = res.data[i][j]
                  }
                  if (j === 'position') {
                    arr[i][positionIndex] = res.data[i][j]
                  }
                  if (j === 'requirement') {
                    arr[i][requirementIndex] = res.data[i][j]
                  }
                  if (j === 'remarks') {
                    arr[i][remarksIndex] = res.data[i][j]
                  }
                  if (arr[i][wideIndex] && arr[i][highIndex] && arr[i][numIndex] && arr[i][priceIndex] &&
                    isNumber(arr[i][priceIndex]) && isNumber(arr[i][numIndex]) && isNumber(arr[i][highIndex]) && isNumber(arr[i][wideIndex])) {
                    arr[i][lengthenIndex] = keepThreeNum((Number(arr[i][wideIndex]) + Number(arr[i][wideIndex])) * Number(arr[i][numIndex]) * 2 / 1000);
                    arr[i][singleAreaIndex] = keepThreeNum(Number(arr[i][wideIndex]) * Number(arr[i][highIndex]) / 1000000);
                    arr[i][singleClearingAreaIndex] = keepThreeNum(Number(arr[i][wideIndex]) * Number(arr[i][highIndex]) / 1000000);
                    arr[i][productAreaIndex] = keepThreeNum(Number(arr[i][wideIndex]) * Number(arr[i][highIndex]) * Number(arr[i][numIndex]) / 1000000);
                    arr[i][productAmountIndex] = keepThreeNum(Number(arr[i][wideIndex]) * Number(arr[i][highIndex]) * Number(arr[i][numIndex]) * Number(arr[i][priceIndex]) / 1000000);
                  }
                }
                if (arr[i][productNameIndex]) {
                  productName.add(arr[i][productNameIndex]);
                }
              }
              if (productName.size) {
                new Promise((resolve, reject) => {
                  nameGetSemiProduct({productNames: [...productName].toString()}).then(res => {
                    // console.log(res)
                    if (res.code === 200) {
                      if (res.data) {
                        res.data.forEach(resItem => {
                          arr.forEach(item => {
                            let weight=0;
                            if (resItem.productName === item[productNameIndex]) {

                              if (resItem.semiProduct) {
                                resItem.semiProduct.forEach(semiItem => {
                                  if (semiItem.attribute != '中间层') {
                                    let x =Number(semiItem.thick) * Number(item[productAreaIndex]) * 0.025;
                                    weight += x;
                                  }
                                })
                                weight=keepThreeNum(weight)
                                item[productWeightIndex] = keepThreeNum(weight / 10);
                              }
                            }
                          })
                        })
                      }
                    }
                    resolve()
                  })
                }).then(p => {
                  this.selectedHand = arr;
                  // this.editRow = 0
                  console.log('this.editRow', this.editRow)
                  this.editRow += res.data.length;
                  // 使用二维数组填充单元格
                  hot.populateFromArray(this.editRow - res.data.length, productIdIndex, this.selectedHand);
                })
              } else {
                this.selectedHand = arr;
                this.editRow += res.data.length;
                // 使用二维数组填充单元格
                hot.populateFromArray(this.editRow - res.data.length, productIdIndex, this.selectedHand);

              }

              // this.$refs.multipleProductTable.clearSelection();
              this.$message({
                message: 'Excel上传成功',
                type: 'success'
              });
            }

            this.uploadFileDialog = false;
            this.fileList = [];
          }
        })
      }
    },
    /* 取消 */
    cancelUpload() {
      this.uploadFileDialog = false;
      this.fileList = []
    },
    handleClose(done) {
      this.uploadFileDialog = false;
      this.fileList = []
    },
    /* 根据产品名称获取重量 */
    getProductWeight() {
      let hot = this.$refs.hotTable.hotInstance;
      const arr = hot.getData();
      // let productNameArr = [];
      const productName = new Set()
      let productNameIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productName'
      });
      let productAreaIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productArea'
      });
      let productWeightIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productWeight'
      });
      arr.forEach(item => {
        if (item[productNameIndex]) {
          productName.add(item[productNameIndex]);
        }
      })
      new Promise((resolve, reject) => {
        nameGetSemiProduct({productNames: [...productName].toString()}).then(res => {
          // console.log(res)
          if (res.code === 200) {
            if (res.data) {
              res.data.forEach(resItem => {
                arr.forEach(item => {
                  if (resItem.productName === item[productNameIndex]) {
                    let weight = 0;
                    if (resItem.semiProduct) {
                      resItem.semiProduct.forEach(semiItem => {
                        if (semiItem.attribute != '中间层') {
                          let x = Number(semiItem.thick) * Number(item[productAreaIndex]) * 0.025;
                          weight += x;
                        }
                      })
                      weight=keepThreeNum(weight)
                      item[productWeightIndex] = keepThreeNum(weight / 10);
                    }
                  }
                })
              })
            }
          }
          resolve()
        })
      }).then(p => {
        // console.log(arr)
        hot.populateFromArray(0, 0, arr);
      })
    },
    /* 计算 */
    handleCount() {
      // console.log('123',hot.getDataAtProp(item.label))
      this.getProductWeight();
      let hot = this.$refs.hotTable.hotInstance;
      const arr = hot.getData();
      //声明一个产品名称数组
      const productName = new Set();
      let productNameArr = [];
      let wideHeadNumber = 0;
      let highHeadNumber = 0;
      let numNumber = 0;
      let unitPriceNumber = 0;
      let wideHeadIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'wideHead'
      });
      let highHeadIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'highHead'
      });
      let numIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'num'
      });
      let unitPriceIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'unitPrice'
      });
      let productNameIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productName'
      });
      arr.forEach(item => {
        if (item[productNameIndex]) {
          productName.add(item[productNameIndex]);
          productNameArr.push(item[productNameIndex])
          if (item[wideHeadIndex]) {
            if (!isNumber(item[wideHeadIndex]) || Number(item[wideHeadIndex]) < 0) {
              wideHeadNumber++;
            }
          }
          if (item[highHeadIndex]) {
            if (!isNumber(item[highHeadIndex]) || Number(item[highHeadIndex]) < 0) {
              highHeadNumber++;
            }
          }
          if (item[numIndex]) {
            if (!isNumber(item[numIndex]) || Number(item[numIndex]) < 0) {
              numNumber++;
            }
          }
          if (item[unitPriceIndex]) {
            if (!isNumber(item[unitPriceIndex]) || Number(item[unitPriceIndex]) < 0) {
              unitPriceNumber++;
            }
          }
        }
      })
      if (!productName.size) {  // }else {
        this.$message.warning("请输入产品信息！")
      } else if (wideHeadNumber) {
        this.$message.warning("产品宽度为大于0的数值！")
      } else if (highHeadNumber) {
        this.$message.warning("产品高度为大于0的数值！")
      } else if (numNumber) {
        this.$message.warning("产品数量为大于0的数值！")
      } else if (unitPriceNumber) {
        this.$message.warning("产品单价为大于0的数值！")
      } else {
        let count = 0;
        this.summation.forEach(item => {
          if (item.label2 == "productAmount") {
            console.log(hot.getDataAtProp(item.label2))
            item.value = keepThreeNum(Number(sum(hot.getDataAtProp(item.label2))) + Number(this.basicInfoForm.additionalCost) + Number(this.basicInfoForm.urgentCost))
            count++
          } else {
            console.log(hot.getDataAtProp(item.label2))
            item.value = keepThreeNum(sum(keepThreeNum(hot.getDataAtProp(item.label2))));
            count++
          }
        })
        if (count === this.summation.length) {
          this.$message.success("计算成功！")
        } else {
          this.$message.success("计算失败！")
        }
      }

    },
    // sum(array) {
    //   return array.reduce((total, num) => total + num, 0)
    // },
    /* 合并同规格 */
    handleMerge() {
      let hot = this.$refs.hotTable.hotInstance;
      let arr = hot.getData();
      const productName = new Set();
      let productNameArr = [];
      let numCount = 0;
      let numIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'num'
      });
      let lengthenIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'lengthen'
      });
      let productAreaIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productArea'
      });
      let productWeightIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productWeight'
      });
      let productAmountIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productAmount'
      });
      let productNameIndex = this.hotSettings.columns.findIndex((el) => {
        return el.data === 'productName'
      });

      arr.forEach(item => {
        if (item[productNameIndex]) {
          productName.add(item[productNameIndex]);
          productNameArr.push(item[productNameIndex])
          if (!item[numIndex]) {
            numCount++;
          }
        }
      })
      if (!productName.size) {
        this.$message.warning("请输入产品信息！")
      } else if (numCount) {
        this.$message.warning("请输入产品数量！")
      } else {
        let obj = {};
        arr.forEach((item, index) => {
          let key = '';
          //用key去重，只算有产品名称的
          if (item[productNameIndex]) {
            this.hotSettings.columns.forEach((columnsItem,columnsIndex) => {
              if(typeof item[columnsIndex]=="object"){
                item[columnsIndex]='';
              }
              if (columnsItem.data !== 'productId' &&
                columnsItem.data !== 'num' &&
                columnsItem.data !== 'lengthen' &&
                columnsItem.data !== 'productArea' &&
                columnsItem.data !== 'productWeight' &&
                columnsItem.data !== 'productAmount' && !columnsItem.data.includes('_allAmount')) {
                // console.log(typeof  item[columnsIndex],item[columnsIndex])


                // key += hot.getDataAtRowProp(index, columnsItem.data);
                key += item[columnsIndex];
                console.log(key)
              }
            })
            if (obj[key]) {
              obj[key][numIndex] += item[numIndex]
              obj[key][lengthenIndex] += item[lengthenIndex]
              obj[key][productAreaIndex] += item[productAreaIndex]
              obj[key][productWeightIndex] += item[productWeightIndex]
              obj[key][productAmountIndex] += item[productAmountIndex]
              obj[key][numIndex] = keepThreeNum(obj[key][numIndex])
              obj[key][lengthenIndex] = keepThreeNum(obj[key][lengthenIndex])
              obj[key][productAreaIndex] = keepThreeNum(obj[key][productAreaIndex])
              obj[key][productWeightIndex] = keepThreeNum(obj[key][productWeightIndex])
              obj[key][productAmountIndex] = keepThreeNum(obj[key][productAmountIndex])
            } else {
              obj[key] = item
            }
          }
        })
        arr = Object.values(obj);
        console.log(arr)
        //数组转对象
        let keys = [];
        this.hotSettings.columns.forEach(item => {
          keys.push(item.data)
        })
        const result = arr.map(r => {
          let obj = {}
          keys.forEach((k, i) => {
            obj[k] = r[i]
          })
          return obj
        })
        this.$message.success("合并同规格成功！")
        this.hotSettings.data = result;
        if (this.additionCheckList) {
          this.additionCheckList.forEach(item => {
            this.additionCheckOptions.forEach(one => {
              if (item == one.id) {
                this.hotSettings.data.forEach(dataItem => {
                  // if (item == one.id) {
                  if (dataItem[one.additionalAlias + "_unitPrice"]) {
                    let productNum = dataItem["num"];
                    let oneNumber;
                    let strings = one.computeType.split(',');
                    if (strings[0] == "数量") {
                      oneNumber = dataItem[one.additionalAlias + "_oneNumber"];
                    } else if (strings[0] == "面积") {
                      oneNumber = dataItem["singleClearingArea"];
                    } else if (strings[0] == "周长") {
                      oneNumber = dataItem["lengthen"];
                      console.log("lengthen", oneNumber)
                    } else if (strings[0] == "边长") {
                      oneNumber = dataItem[""];
                    }
                    let unitPrice = dataItem[one.additionalAlias + "_unitPrice"];
                    dataItem[one.additionalAlias + "_allAmount"] = keepThreeNum(productNum * oneNumber * unitPrice);
                    dataItem.productAmount += dataItem[one.additionalAlias + "_allAmount"];
                    dataItem.productAmount = keepThreeNum(dataItem.productAmount);
                  }
                  // }
                })
              }
            })
          })
        }
        hot.updateSettings({
          data: this.hotSettings.data,
        }, false);
      }
    },
    /* 保存 */
    saveProductInfo(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          //给订单赋值
          let hot = this.$refs.hotTable.hotInstance;
          this.basicInfoForm.additionalIds = this.additionCheckList;
          const arr = hot.getData();
          //声明一个产品名称数组
          const productName = new Set()
          let productNameArr = [];
          let wideHeadCount = 0;
          let highHeadCount = 0;
          let numCount = 0;
          let unitPriceCount = 0;
          let wideHeadIndex = this.hotSettings.columns.findIndex((el) => {
            return el.data === 'wideHead'
          });
          let highHeadIndex = this.hotSettings.columns.findIndex((el) => {
            return el.data === 'highHead'
          });
          let numIndex = this.hotSettings.columns.findIndex((el) => {
            return el.data === 'num'
          });
          let productNameIndex = this.hotSettings.columns.findIndex((el) => {
            return el.data === 'productName'
          });
          let productIdIndex = this.hotSettings.columns.findIndex((el) => {
            return el.data === 'productId'
          });
          let unitPriceIndex = this.hotSettings.columns.findIndex((el) => {
            return el.data === 'unitPrice'
          });
          arr.forEach(item => {
            if (item[productNameIndex]) {
              productName.add(item[productNameIndex]);
              if (!item[wideHeadIndex]) {
                wideHeadCount++;
              }
              if (!item[highHeadIndex]) {
                highHeadCount++;
              }
              if (!item[numIndex]) {
                numCount++;
              }
              if (!item[unitPriceIndex]) {
                unitPriceCount++;
              }
            }
          })
          if (!productName.size) {
            this.$message.warning("请输入产品信息！")
          } else if (wideHeadCount) {
            this.$message.warning("请输入产品宽度！")
          } else if (highHeadCount) {
            this.$message.warning("请输入产品高度！")
          } else if (numCount) {
            this.$message.warning("请输入产品数量！")
          } else if (unitPriceCount) {
            this.$message.warning("请输入产品单价！")
          } else {
            this.summation.forEach(sumItem => {
              if (sumItem.label2 == "productAmount") {
                sumItem.value = keepThreeNum(Number(sum(hot.getDataAtProp(sumItem.label2))) + Number(this.basicInfoForm.additionalCost) + Number(this.basicInfoForm.urgentCost))
                // count++
              } else {
                sumItem.value = keepThreeNum(sum(hot.getDataAtProp(sumItem.label2)));
                // count++
              }
              if (sumItem.label === "orderAmount") {
                sumItem.value = keepOneNum(sumItem.value)
              }
              if (this.basicInfoForm.hasOwnProperty(sumItem.label)) {
                this.basicInfoForm[sumItem.label] = sumItem.value
              }
            })
            //查询系统产品id
            queryProduct({productName: [...productName].toString()}).then(res => {
              // console.log(res)
              let count = 0;
              let str = '';
              let resArr = [];
              res.data.forEach(one => {
                resArr.push(one.productName)
                arr.forEach(item => {
                  if (item[productNameIndex] === one.productName) {
                    item[productIdIndex] = one.id;
                  }
                })
              })
              productNameArr = [...productName];
              productNameArr.forEach(item => {
                if (!resArr.includes(item)) {
                  str = item + ',' + str;
                  count++;
                }
              })
              if (count) {
                this.$message.warning("系统中不存在“" + str.slice(0, -1) + "”产品！")
              } else {
                let keys = [];
                this.hotSettings.columns.forEach(item => {
                  keys.push(item.data)
                })
                const result = arr.map(r => {
                  let obj = {}
                  keys.forEach((k, i) => {
                    obj[k] = r[i]
                  })
                  return obj
                })
                this.basicInfoForm.productList = result;
                addOrderCustomer({customerName:this.basicInfoForm.customerName})
                addRecord(this.basicInfoForm).then(res => {
                  if (res.code === 200) {
                    this.$message({
                      message: res.msg,
                      type: 'success'
                    });
                    loading.close();
                    this.$router.go(-1);
                  } else {
                    this.$message({
                      message: res.msg,
                      type: 'error'
                    });
                  }
                })
              }
            })
          }
        } else {
          return false;
        }
      });
      // this.$refs[formName].validate((valid) => {
      //   if (valid) {
      //     // this.handleCount();
      //     //给订单赋值
      //     let hot = this.$refs.hotTable.hotInstance;
      //     // this.basicInfoForm.additionalIds = this.additionCheckList;
      //     // this.basicInfoForm.orderAmount = this.summation[4].value;
      //     // this.basicInfoForm.orderNum = this.summation[0].value;
      //     // this.basicInfoForm.totalArea = this.summation[2].value;
      //     // this.basicInfoForm.totalWeigh = this.summation[3].value;
      //     // this.basicInfoForm.totalLengthen = this.summation[1].value;
      //     // this.summation.forEach(sumItem => {
      //     //   // console.log(res.data[0])
      //     //   if (this.basicInfoForm.hasOwnProperty(sumItem.label2)) {
      //     //     this.basicInfoForm[sumItem.label2] = sumItem.value
      //     //   }
      //     // })
      //     this.basicInfoForm.receivedAmount = this.basicInfoForm.orderDeposit;
      //     const arr = hot.getData();
      //     //声明一个产品名称数组
      //     const productName = new Set()
      //     let productNameArr = [];
      //     let wideHeadCount = 0;
      //     let highHeadCount = 0;
      //     let numCount = 0;
      //     let wideHeadIndex = this.hotSettings.columns.findIndex((el) => {
      //       return el.data === 'wideHead'
      //     });
      //     let highHeadIndex = this.hotSettings.columns.findIndex((el) => {
      //       return el.data === 'highHead'
      //     });
      //     let numIndex = this.hotSettings.columns.findIndex((el) => {
      //       return el.data === 'num'
      //     });
      //     let productNameIndex = this.hotSettings.columns.findIndex((el) => {
      //       return el.data === 'productName'
      //     });
      //     let productIdIndex = this.hotSettings.columns.findIndex((el) => {
      //       return el.data === 'productId'
      //     });
      //     arr.forEach(item => {
      //       if (item[productNameIndex]) {
      //         productName.add(item[productNameIndex]);
      //         // productNameArr.push(item[productNameIndex])
      //         if (!item[wideHeadIndex]) {
      //           wideHeadCount++;
      //         }
      //         if (!item[highHeadIndex]) {
      //           highHeadCount++;
      //         }
      //         if (!item[numIndex]) {
      //           numCount++;
      //         }
      //       }
      //     })
      //     if (!productName.size) {
      //       this.$message.warning("请输入产品信息！")
      //       return
      //     } else if (wideHeadCount) {
      //       this.$message.warning("请输入产品宽度！")
      //       return
      //     } else if (highHeadCount) {
      //       this.$message.warning("请输入产品高度！")
      //       return
      //     } else if (numCount) {
      //       this.$message.warning("请输入产品数量！")
      //       return
      //     } else {
      //       this.getProductWeight();
      //       this.summation.forEach(item => {
      //         if (item.label == "productAmount") {
      //           item.value = keepThreeNum(Number(sum(hot.getDataAtProp(item.label))) + Number(this.basicInfoForm.additionalCost) + Number(this.basicInfoForm.urgentCost))
      //         } else {
      //           item.value = keepThreeNum(sum(hot.getDataAtProp(item.label)));
      //         }
      //         if(item.value){
      //           if (this.basicInfoForm.hasOwnProperty(item.label)) {
      //             this.basicInfoForm[item.label] = item.value
      //           }
      //         }
      //       })
      //       //查询系统产品id
      //       queryProduct({productName: [...productName].toString()}).then(res => {
      //         // console.log(res)
      //         let count = 0;
      //         let str = '';
      //         let resArr = [];
      //         res.data.forEach(one => {
      //           resArr.push(one.productName)
      //           arr.forEach(item => {
      //             if (item[productNameIndex] === one.productName) {
      //               item[productIdIndex] = one.id;
      //             }
      //           })
      //         })
      //         productNameArr = [...productName];
      //         productNameArr.forEach(item => {
      //           if (!resArr.includes(item)) {
      //             str = item + ',' + str;
      //             count++;
      //           }
      //         })
      //         if (count) {
      //           this.$message.warning("系统中不存在“" + str.slice(0, -1) + "”产品！")
      //         } else {
      //           let keys = [];
      //           this.hotSettings.columns.forEach(item => {
      //             keys.push(item.data)
      //           })
      //           const result = arr.map(r => {
      //             let obj = {}
      //             keys.forEach((k, i) => {
      //               obj[k] = r[i]
      //             })
      //             return obj
      //           })
      //           this.basicInfoForm.productList = result;
      //           addRecord(this.basicInfoForm).then(res => {
      //             if (res.code === 200) {
      //               this.$message({
      //                 message: res.msg,
      //                 type: 'success'
      //               });
      //               this.$router.go(-1);
      //             } else {
      //               this.$message({
      //                 message: res.msg,
      //                 type: 'error'
      //               });
      //             }
      //           })
      //         }
      //       })
      //     }
      //   } else {
      //     return false;
      //   }
      // });
    },
    /* 新增返回 */
    addBack() {
      this.$confirm('是否保存未编辑完的信息，保存后下次将直接展示未编辑完的信息?', '提示', {
        confirmButtonText: '保存',
        cancelButtonText: '不保存',
        type: 'warning',
        size: 'mini'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        this.handleCount();
        //给订单赋值
        let hot = this.$refs.hotTable.hotInstance;
        this.basicInfoForm.additionalIds = this.additionCheckList;
        this.basicInfoForm.orderAmount = this.summation[4].value;
        this.basicInfoForm.orderNum = this.summation[0].value;
        this.basicInfoForm.totalArea = this.summation[2].value;
        this.basicInfoForm.totalWeigh = this.summation[3].value;
        this.basicInfoForm.receivedAmount = this.basicInfoForm.orderDeposit;
        const arr = hot.getData();
        //声明一个产品名称数组
        const productName = new Set()
        let productNameArr = [];
        arr.forEach(item => {
          if (item[1]) {
            productName.add(item[1]);
            productNameArr.push(item[1])
          }
        })
        //查询系统产品id
        queryProduct({productName: [...productName].toString()}).then(res => {
          let count = 0;
          let str = '';
          let resArr = [];
          res.data.forEach(one => {
            resArr.push(one.productName)
            arr.forEach(item => {
              if (item[1] === one.productName) {
                item[0] = one.id;
              }
            })
          })
          productNameArr.forEach(item => {
            if (!resArr.includes(item)) {
              str = item + ',' + str;
              count++;
            }
          })
          if (count) {
            this.$message.warning("系统中不存在“" + str.slice(0, -1) + "”产品！")
          } else {
            let keys = [];
            this.hotSettings.columns.forEach(item => {
              keys.push(item.data)
            })
            const result = arr.map(r => {
              let obj = {}
              keys.forEach((k, i) => {
                obj[k] = r[i]
              })
              return obj
            })
            this.basicInfoForm.productList = result;
            addOrderCustomer({customerName:this.basicInfoForm.customerName})
            addRecordCache(this.basicInfoForm).then(res => {
              console.log("res" + JSON.stringify(res))
              if (res.code === 200) {
                this.$message({
                  message: '保存成功',
                  type: 'success'
                });
              } else {
                this.$message({
                  message: res.msg,
                  type: 'error'
                });
              }
              loading.close();
              this.$router.go(-1);
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消保存'
        });
        if (this.basicInfoForm.id != null) {
          reallyDelOrder({orderIds: this.basicInfoForm.id}).then(res => {
          })
        }
        this.$router.go(-1);
      });
    },
  }
}
</script>

<style lang="scss" scoped>
.addOrder {
  display: flex;
  flex-direction: column;
  color: #606266;

  p {
    font-size: 14px;
  }

  ::v-deep .basicInfo {
    position: relative;
    max-height: 32%;
    border-radius: 15px;
    box-shadow: 0 1px 5px 0 rgba(0, 0, 0, 0.3);
    margin-bottom: 10px;

    .title {
      padding-left: 40px;
      margin-bottom: 0 !important;
    }

    .basicInfoContainer {
      height: calc(100% - 30px);
      overflow: auto;
      background: rgba(100, 200, 188, 0.05);
      border: 1px solid rgba(100, 200, 188, 0.5);
      border-bottom-left-radius: 15px;
      border-bottom-right-radius: 15px;

    }

    .changeBtn {
      position: absolute;
      right: 10px;
      top: 2px;
      border: none;
      background-color: transparent;
      color: #46a6ff;
      font-size: 13px
    }

    .basicChecked {
      height: 35px;
      line-height: 30px;
      overflow: hidden;
      display: flex;
      justify-content: space-around;
      background: rgba(255, 169, 88, 0.05);
      box-sizing: border-box;
      border: 1px solid rgba(255, 169, 88, 0.5);
      //background: rgba(100, 200, 188, 0.05);
      //border: 1px solid rgba(100, 200, 188, 0.5);
      border-bottom-left-radius: 15px;
      border-bottom-right-radius: 15px;

      & > div {
        flex: 1;
        display: flex;
        justify-items: flex-start;

        p {
          width: 100px;
          padding-left: 40px;
          font-weight: 600;
          margin-top: 5px;
        }

        .el-checkbox-group {
          padding: 5px 10px 0 10px;
          width: calc(100% - 100px);
        }
      }

      & > div:last-of-type {
        border-right: none
      }
    }
  }

  .handTable {
    flex: 1;
    width: 100%;
    overflow: hidden;

    ::v-deep .handsontable {
      .ht_clone_left {
        border-right: 5px solid #cccccc;
        //border-right: 6px solid red;
      }

      //td {
      //text-align: center !important;
      //}
    }
  }

  .countAndBtn {
    height: 80px;
    width: 100%;
    margin-top: 10px;
    //background-color: pink;

    .count {
      width: 100%;
      display: flex;
      justify-content: center;

      p {
        border: 1px solid #dfe6ec;
        padding: 5px 10px;
        border-right: none;
        color: rgb(144, 147, 153);
        letter-spacing: 1px;
        font-size: 12px;
        font-weight: 600;

        span {
          color: red;
        }
      }

      p:last-of-type {
        border-right: 1px solid #dfe6ec;
      }

      p:first-of-type {
        background-color: rgba(204, 204, 204, 0.18);
      }
    }

    .btn {
      justify-content: center;
    }
  }
}

::v-deep .selectCustomerDialog, ::v-deep .selectProductDialog {
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


          //thead .el-table-column--selection .cell {
          //  display: none;
          //}
        }
      }
    }
  }
}

::v-deep .selectCustomerDialog {
  thead .el-table-column--selection .cell {
    display: none;
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

::v-deep .uploadFileDialog {
  .el-dialog {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}

::v-deep .el-table {
  border-right: 1px solid #dfe6ec;
  color: #909399;

  th {
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

      }

      .operation {
        .cell {
          display: flex;
          justify-content: space-around;
          width: 100%;
          white-space: nowrap;
          overflow: hidden;
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

  .el-table__cell {
    padding: 5px 0 !important;
  }
}
</style>
