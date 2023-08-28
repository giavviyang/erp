<template>
  <div class="app-container-padding addOrder">
    <div class="basicInfo">
      <p class="title1">基本信息</p>
      <div class="basicInfoContainer">
        <el-form :model="basicInfoForm" size="mini" :inline="true" ref="ruleForm" class="ipt2">
          <el-form-item label="订单编号">
            <el-input v-model="basicInfoForm.orderNo" disabled/>
          </el-form-item>
          <el-form-item label="自定义编号">
            <el-input v-model="basicInfoForm.customNo" disabled/>
          </el-form-item>
          <el-form-item label="客户名称" prop="customerName">
            <el-input v-model="basicInfoForm.customerName" disabled ref="customerNameInput"/>
          </el-form-item>
          <el-form-item label="项目名称">
            <el-input v-model="basicInfoForm.entryName" disabled/>
          </el-form-item>
          <el-form-item label="交货日期" prop="receiptDate">
            <el-date-picker
              v-model="basicInfoForm.receiptDate"
              type="date"
              placeholder="选择日期时间"
              value-format="yyyy-MM-dd" disabled>
            </el-date-picker>
          </el-form-item>
          <el-form-item label="下单时间" prop="preparationDate">
            <el-date-picker
              v-model="basicInfoForm.preparationDate"
              type="date"
              placeholder="选择日期时间"
              value-format="yyyy-MM-dd" disabled>
            </el-date-picker>
          </el-form-item>

          <el-form-item label="联系人">
            <el-input v-model="basicInfoForm.contacts" disabled/>
          </el-form-item>
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-model.number="basicInfoForm.contactNumber" disabled/>
          </el-form-item>
          <el-form-item label="收货地址" class="receiptAddress">
            <el-input v-model="basicInfoForm.receiptAddress" disabled/>
          </el-form-item>

          <el-form-item label="定金（元）" prop="urgentCost">
            <el-input v-model="basicInfoForm.orderDeposit" disabled/>
          </el-form-item>
          <el-form-item label="订单类型">
            <el-select v-model="basicInfoForm.orderType" placeholder='请选择订单类型' prop="orderType" disabled>
              <el-option
                v-for="orderItem in orderTypeOptions"
                :key="orderItem.value"
                :label="orderItem.label"
                :value="orderItem.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="结算方式">
            <el-select v-model="basicInfoForm.settlementMethod" placeholder="请选择" disabled>
              <el-option
                v-for="item in dict.type.order_settlement"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="加急费（元）" prop="urgentCost">
            <el-input v-model="basicInfoForm.urgentCost" disabled/>
          </el-form-item>
          <el-form-item label="附加费（元）" prop="urgentCost">
            <el-input v-model="basicInfoForm.additionalCost" disabled/>
          </el-form-item>
          <el-form-item label="生产备注" class="remarks">
            <el-input v-model="basicInfoForm.remarks" type="textarea" size="mini" placeholder="请输入内容" disabled/>
          </el-form-item>
          <el-form-item label="订单备注" class="remarks">
            <el-input v-model="basicInfoForm.orderRemarks" type="textarea" size="mini" placeholder="请输入内容" disabled/>
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
            <el-checkbox v-for="item in additionCheckOptions" :key="item.id" :label="item.id" disabled>
              {{ item.additionalName }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
        <div class="special">
          <p>特殊产品</p>
          <el-checkbox-group v-model="additionCheckList">
            <el-checkbox v-for="item in specialCheckOptions" :key="item.label" :label="item.label" disabled>
              {{ item.value }}
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
          icon="el-icon-refresh-left"
          size="mini"
          @click="handleCheckBack"
        >返回
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>

import {HyperFormula} from 'hyperformula';
import SlotTable from "@/components/public/table/SlotTable";
import MainFlexibleTree from "@/components/public/MainBody/MainFlexibleTree";
import {listAddition} from "@/api/system/addition";
import {listOrder, listProduct, orderNumber, auditSize, auditOrder} from "@/api/salse/order/order";
import {queryProcess} from "@/api/salse/order/orderProduct";
import {getInfo} from "@/api/login";
import {keepOneNum} from "@/utils/order/order";

export default {
  dicts: ['order_settlement'],
  components: {MainFlexibleTree, SlotTable},

  name: "selectOrder",
  data() {
    return {
      //查询产品字段
      // queryDetail: {
      //   orderId: ""
      // },
      // orderId: '',
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
        orderType: '',                               //订单类型
        preparationDate: '',                        //下单日期
        preparer: "",                               //下单人
        productList: [],                            //产品集合
        receiptAddress: "",                         //收货地址
        receiptDate: "",                            //收货日期
        remarks: "",                                //生产备注
        orderRemarks: "",                           //订单备注
        totalArea: 0,                               //总面积
        totalWeigh: 0,                              //总重量
        urgentCost: 0,                              //加急费
        settlementMethod: "",                        //结算方式
        isCache: 1,                                 //是否是缓存数据
        pageNum: 1,
        pageSize: 20,
        collectionStatus: '',
        id: '',
        packagingStatus: '',
        preparationDateEnd: '',
        preparationDateStart: '',
        productionStatus: '',
        rackSplittingStatus: '',
        shipmentStatus: '',       // createTime: undefined,
        isDel: 0,
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        collectionStatus: '',
        contactNumber: '',
        contacts: '',
        customNo: '',
        customerName: '',
        entryName: '',
        id: '',
        isCache: 0,
        orderNo: '',
        orderType: '',
        packagingStatus: '',
        preparationDateEnd: '',
        preparationDateStart: '',
        preparer: '',
        productionStatus: '',
        rackSplittingStatus: '',
        shipmentStatus: '',       // createTime: undefined,
        isDel: 0,
      },
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
      //特殊产品选中的值
      // specialCheckList: [],
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
      tableArr: [],
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
        colHeaders: ['产品Id', '产品名称', '打印名称', '宽（mm）', '高（mm）', '数量（片）', '单价（元/㎡）', '位置', '加工要求', '备注一', '备注二', '备注三', '延长米数（m）', '总周长（m）', '单片实际面积（m²）', '单片结算面积（m²）', '总面积（m²）', '总重量（t）', '总价（元）'],
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
      // //总合计
      summation: [
        {label: 'orderNum', title: '总数量', value: '', unit: '片'},
        {label: 'totalLengthen', title: '总周长', value: '', unit: 'm'},
        {label: 'totalArea', title: '总面积', value: '', unit: 'm²'},
        {label: 'totalWeigh', title: '总重量', value: '', unit: 't'},
        {label: 'orderAmount', title: '总金额', value: '', unit: '元'},
        {label: 'receivedAmount', title: '已收款', value: '', unit: '元'},
        {label: 'discountAmount', title: '优惠金额', value: '', unit: '元'},
        {label: 'dsk', title: '待收款', value: '', unit: '元'}
      ],
      sizeCheckDialog: false,   //尺寸审核弹窗
      //尺寸审核表头
      sizeCheckColumn: [
        {label: '宽(宽大头)', prop: 'wideHead', width: '100', count: 0,},
        {label: '宽小头', prop: 'wideLittleHead', width: '80', count: 0,},
        {label: '高(高大头)', prop: 'highHead', width: '100', count: 0,},
        {label: '高小头', prop: 'highLittleHead', width: '80', count: 0,},
        {label: '直径(圆)', prop: 'diameter', width: '100', count: 0,},
        {label: '直径增量(圆)', prop: 'diameterIncrement', width: '100', count: 0,},
        {label: '直径(弯弧)', prop: 'arcDiameter', width: '100', count: 0,},
        {label: '内弧长', prop: 'innerArcLength', width: '80', count: 0,},
        {label: '数量（片）', prop: 'num', width: '80', count: 0,},
      ],
      spanArr: [], // 一个空的数组，用于存放每一行记录的合并数
      pos: 0, // spanArr 的索引
      contentSpanArr: [],
      position: 0,
      sizeCheckColumn2: [],
      craftCheckDialog: false,   //工艺审核弹窗
      tableData: [],
      craftCheckColumn: [
        {label: '产品名称', prop: 'productName', width: '200', count: 0,},
        {label: '宽(宽大头)', prop: 'wideHead', width: '100', count: 0,},
        {label: '宽小头', prop: 'wideLittleHead', width: '80', count: 0,},
        {label: '高(高大头)', prop: 'highHead', width: '100', count: 0,},
        {label: '高小头', prop: 'highLittleHead', width: '80', count: 0,},
        {label: '直径(圆)', prop: 'diameter', width: '100', count: 0,},
        {label: '直径增量(圆)', prop: 'diameterIncrement', width: '100', count: 0,},
        {label: '直径(弯弧)', prop: 'arcDiameter', width: '100', count: 0,},
        {label: '内弧长', prop: 'innerArcLength', width: '80', count: 0,},
        {label: '数量（片）', prop: 'num', width: '80', count: 0,},
        {label: '成品面', prop: 'itemSurface', width: '80', count: 0,},
        {label: '单片名称', prop: 'itemName', width: '100', count: 0,},
        {label: '工艺流程', prop: 'processContent', width: '300', count: 0,},
        {label: '加工要求', prop: 'requirement', width: '100', count: 0,},
      ],
      checkDialogTitle: '审核通过',
      checkDialog: false,   // 审核弹窗
      failureCheckDialog: false,  //审核不通过
      flag: false,  //是否显示审核不通过原因
      userName: '',
      checkForm: {
        orderId: '',  //id
        orderResult: 0,
        reviewedBy: '',     //审核人
      },
      failureCheckForm: {
        orderId: '',  //id
        orderResult: 1,
        reviewedBy: '',  //审核不通过审核人
        failureReason: '',  //不通过原因
      },
      checkRules: {
        reviewedBy: [
          {required: true, message: "审核人不能为空", trigger: "blur"}
        ],
      },
      failureCheckRules: {
        reviewedBy: [
          {required: true, message: "审核人不能为空", trigger: "blur"}
        ],
        failureReason: [
          {required: true, message: "不通过原因不能为空", trigger: "blur"}
        ],
      }
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
  created() {
    this.queryProductList();
    this.getAdditionList();
    this.getUserInfo();
  },
   methods: {

    /*查询登录信息*/
    getUserInfo() {
      getInfo().then(res => {
        console.log('000', res)
        if (res.code === 200) {
          this.userName = res.user.nickName;
        }
      })
    },
    /* 查询订单信息 */
    queryProductList() {
      this.queryParams.id = this.$route.params.id;
      listOrder(this.queryParams).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.basicInfoForm = res.data[0];
          this.additionCheckList = res.data[0].orderAdditional.split(", ")
          this.summation.forEach(sumItem => {
            // console.log(res.data[0])
            if(sumItem.label == 'dsk'){
              sumItem.value = Number(res.data[0].orderAmount) - Number(res.data[0].receivedAmount) - Number(res.data[0].discountAmount) - Number(res.data[0].orderDeposit)
            }else if (res.data[0].hasOwnProperty(sumItem.label)) {
              sumItem.value = res.data[0][sumItem.label]
            }
            if (sumItem.label === "orderAmount" || sumItem.label === "receivedAmount" ||  sumItem.label === "dsk") {
              sumItem.value = keepOneNum(sumItem.value)
            }
          })
        }
      })
    },
    /* 查询表格数据 */
    getTableData() {
      let hot = this.$refs.hotTable.hotInstance;
      listProduct({orderId: this.$route.params.id}).then(response => {
        if (response.code === 200) {
          console.log(response.data)
          let arr = [];
          response.data.forEach(one => {
            let arrOne = [];
            this.hotSettings.columns.forEach(item => {
              let x = item.data;
              console.log(x)
              if (x.indexOf("_") >= 0 && one.additionalMap != null) {
                arrOne.push(one.additionalMap[x])
              } else {
                arrOne.push(one[x])
              }
            })
            arr.push(arrOne)
          })
          hot.populateFromArray(0, 0, arr);
          //如果存在数据，则数据不可更改，无右侧下拉菜单
          if (arr) {
            this.hotSettings.columns.forEach(item => {
              item.readOnly = true;
            })
            console.log(this.hotSettings.columns)
            hot.updateSettings({
              columns: this.hotSettings.columns,
            }, false);
          }
        }
      })
    },
    /* 获取附加项列表 */
    getAdditionList() {
      listAddition().then(response => {
        // console.log(response)
        if (response.code === 200) {
          this.additionCheckOptions = response.data
          this.getTableData();
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
     /* 合并行  查看工艺 */
     processSpanMethod({row, column, rowIndex, columnIndex}) {
       if (columnIndex === 1 || columnIndex === 2 || columnIndex === 3|| columnIndex === 4) {
         const _row = this.spanArr[rowIndex];
         const _col = _row > 0 ? 1 : 0;
         return {
           rowspan: _row,
           colspan: _col
         };
       }
     },
    /*  尺寸审核通过 */
    saveSizeCheck(param) {
      console.log(param)
      auditSize({orderId: this.basicInfoForm.id, fieldName: "dimensions_result", fieldValue: param}).then(res => {
        if (res.code === 200) {
          this.$message({
            message: res.msg,
            type: 'success'
          });
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      })
      this.sizeCheckDialog = false;
    },
    /*  保存工艺审核 */
    saveCraftCheck(param) {
      auditSize({orderId: this.basicInfoForm.id, fieldName: "workmanship_result", fieldValue: param}).then(res => {
        if (res.code === 200) {
          this.$message({
            message: res.msg,
            type: 'success'
          });
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      })
      this.craftCheckDialog = false;
    },
    /* 审核返回 */
    handleCheckBack() {
      this.$router.go(-1);
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
      .el-input__inner{
        color: red;
      }
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

          .el-checkbox__input.is-checked .el-checkbox__inner,
          .el-checkbox__input.is-indeterminate .el-checkbox__inner {
            background-color: red !important;
            border: red !important;
          }
          .el-checkbox__input.is-checked + .el-checkbox__label {
            color: red;
          }
          .el-checkbox__inner::after{
            left: 5px;
            top:2px;
          }
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
      .ht_clone_left{
        border-right:2px solid #cccccc ;
      }
      //td {
      //  text-align: center !important;
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
::v-deep .sizeCheckDialog {
  .el-dialog {
    height: 60%;

    .el-dialog__body {
      height: calc(100% - 90px);

      .rightTable {
        height: 100%;

        .table {
          height: 100%;
          .el-table__row:hover {
            background-color: transparent;

            .sizeChecked {
              background-color: rgb(144, 147, 153) !important;
            }
          }

          .wideHead, .highHead, .num, .sizeChecked {
            background-color: rgb(144, 147, 153);

          }

          td {
            padding: 5px 0;
          }
        }

        .page {
          display: none;
        }
      }
    }
  }
}
::v-deep .checkDialog {
  width: 80%;
  margin: auto;
  .el-dialog{
    min-height: 5%;
    .el-dialog__body {
      display: flex;
      justify-content: center;
      align-items: center;

      .el-form {
        width: 80%;
        height: 100%;
        display: flex;
        flex-direction: column;

        .el-form-item__label {
          width: 100px;
        }

        .el-input, .el-textarea {
          width: calc(100% - 100px);
        }

        .el-form-item__error {
          left: 110px;
        }
      }
    }
  }
}
</style>
