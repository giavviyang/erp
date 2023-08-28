<template>
  <div class="app-container">
    <div class="containerTop">
      <div class="completionInfo">
        <p class="title1" style="padding-left: 0;">第一步&nbsp;&nbsp;输入基本信息：</p>
        <el-form size="mini" style="margin-top: 10px" :model="completionForm" class="oneForm" :inline="true" ref="ruleForm"
                 label-width="50px" :rules="rules">
          <el-row>
            <el-col :span="12">
              <el-form-item label="工序" prop="completionCraftId">
                <el-select style="width:100%" v-model="completionForm.completionCraftId" placeholder='请选择工序'
                           prop="orderType">
                  <el-option
                    v-for="craftItem in craftOptions"
                    :key="craftItem.id"
                    :label="craftItem.craftName"
                    :value="craftItem.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="班组" prop="completionTeam">
                <el-select style="width:100%" v-model="completionForm.completionTeam" placeholder='请选择班组'
                           prop="team">
                  <el-option
                    v-for="teamItem in teamOptions"
                    :key="teamItem.id"
                    :label="teamItem.teamName"
                    :value="teamItem.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="班次" prop="completionShift">
                <el-select style="width:100%" v-model="completionForm.completionShift" placeholder='请选择班次'
                           prop="workShift">
                  <el-option
                    v-for="workShiftItem in workShiftOptions"
                    :key="workShiftItem.value"
                    :label="workShiftItem.label"
                    :value="workShiftItem.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="时间" prop="completionDate">
                <el-date-picker
                  style="width:100%" class="date"
                  v-model="completionForm.completionDate"
                  :picker-options="preparationDateOptions"
                  type="date"
                  placeholder="选择日期时间"
                  value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" class="remarks">
                <el-input
                  v-model="completionForm.remarks" type="textarea" size="mini" placeholder="请输入内容">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="completionPerson">
        <p class="title1" style="padding-left: 0;margin-bottom: 5px;">第二步&nbsp;&nbsp;选择完工人员：
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            style="margin-left: 10px;"
            @click="selectPerson">选择职员
          </el-button>
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="average">比例分摊
          </el-button>
        </p>
        <slot-table class="completionPersonList">
          <el-table highlight-current-row
            :data="completionPersonList"
            stripe
            border
            style="width: 100%"
            height="100%"
            slot="table">
            <el-table-column
              type="index"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              v-for="(item,index) in completionPersonListColumns"
              :key="index"
              :label="item.label"
              :prop="item.prop"
              show-overflow-tooltip>
              <template slot-scope="scope">
                <span v-if="item.prop == 'completionCoefficient'">
                  <el-input
                    placeholder="请输入系数"
                    type="number"
                    v-model="scope.row.completionCoefficient"
                    @blur="coefficientFun($event,scope.$index)"
                    clearable>
                  </el-input>
                </span>
                <span v-else>
                  {{ scope.row[item.prop] }}
                </span>
              </template>

            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </div>
    <div class="containerCenter">
      <p class="title1" style="padding-left: 0;">第三步&nbsp;&nbsp;选择完工流程卡：
        <el-input v-model="flowCraftIpt" placeholder="请输入流程卡号" size="mini"
                  style="width: 260px;margin-right: 15px;"></el-input>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFlowCraft">搜索</el-button>
      </p>
      <slot-table class="flowCraftList">
        <el-table highlight-current-row
          :data="flowCraftList"
          stripe
          border
          style="width: 100%"
          height="100%"
          slot="table">
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in flowCraftColumns"
            :key="index"
            :label="item.label"
            :prop="item.prop"
            :min-width="item.width"
            show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="item.prop == 'num'">
<!--                <el-input-->
<!--                  placeholder="请输入完工数"-->
<!--                  v-model="scope.row.num"-->
<!--                  @blur="flowCardFun($event,scope.$index)"-->
<!--                  clearable>-->
<!--                  </el-input>-->

              <!--              item.num - item.shelfNum-->
               <el-input-number size="mini" v-model="scope.row.num"
                                @change="flowCardFun($event,scope.$index)" style="width: 120px;" :min="0" @blur="flowCardFun($event,scope.$index)" :precision="0" :controls="false"></el-input-number>
              </span>
              <span v-else>{{ scope.row[item.prop] }}</span>
            </template>
          </el-table-column>
        </el-table>
      </slot-table>
    </div>
    <div class="containerBottom">
      <div class="btn">
        <el-button
          type="primary"
          icon="el-icon-check"
          size="mini"
          @click="handleSave">保存
        </el-button>
        <el-button
          icon="el-icon-refresh-left"
          size="mini"
          @click="handleBack">返回
        </el-button>
      </div>
    </div>
    <el-dialog title="完工人员" :visible.sync="personDialog" width="1000px" class="dialog-style" :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true">
      <slot-table class="completionPersonList" :isPage="false">
        <el-table highlight-current-row
          :data="personList"
          stripe
          border
          style="width: 100%"
          height="400px"
          ref="templateTableRef"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
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
            v-for="(item,index) in personColumns"
            :key="item.userName"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"
            :formatter="item.formatter"
            show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </slot-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="personSelect">保存</el-button>
        <el-button size="mini" @click="personDialog = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {getCraftList} from "@/api/system/craft";
import {getAllTeam} from "@/api/system/team";
import {listAllUser} from "@/api/system/user";
import {getFlowCardInfo, editCompletion, getCompletionInfo} from "@/api/product/completion"
import SlotTable from "@/components/public/table/SlotTable";

export default {
  name: "editCompletion",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: {SlotTable},
  data() {
    //获取当前日期
    let nowDate = new Date()
    let date = {
      year: nowDate.getFullYear(),
      month: nowDate.getMonth() + 1,
      date: nowDate.getDate(),
      hours: nowDate.getHours(),
      minutes: nowDate.getMinutes() < 10 ? '0' + nowDate.getMinutes() : nowDate.getMinutes(),
      seconds: nowDate.getSeconds() < 10 ? '0' + nowDate.getSeconds() : nowDate.getSeconds()
    }
    let currentDay = `${date.year}-${date.month}-${date.date} ${date.hours}:${date.minutes}:${date.seconds}`
    return {
      // 完工不能晚于当前时间
      preparationDateOptions: {
        disabledDate: time => {
          return time.getTime() > Date.now()
        },
      },
      completionForm: {
        completionCraftName: '',
        completionCraftId: '',
        completionShift: '',
        completionTeam: '',
        completionDate: currentDay,                        //下单日期
        remarks: '',
      },
      rules: {
        completionCraftId: [
          {required: true, message: '请选择工艺', trigger: 'change'}
        ],
        completionShift: [
          {required: true, message: '请选择班次', trigger: 'change'}
        ],
        completionTeam: [
          {required: true, message: '请选择班组', trigger: 'change'}
        ],
        completionDate: [
          {required: true, message: '请选择完工时间', trigger: 'change'}
        ]
      },
      craftOptions: [],   //工序
      teamOptions: [],   //班组
      workShiftOptions: [{
        value: '早班',
        label: '早班'
      }, {
        value: '中班',
        label: '中班'
      }, {
        value: '晚班',
        label: '晚班'
      }
      ],
      completionPersonList: [], //完工人员
      completionPersonListColumns: [
        {label: '职员', prop: 'completionPerson'},
        {label: '系数', prop: 'completionCoefficient'},
        {label: '比例', prop: 'completionProportion'},
      ],
      personColumns: [
        {label: '用户账号', prop: 'userName',},
        {label: '用户昵称', prop: 'nickName'},
        {label: '部门', prop: 'dept.deptName'},
        {label: '性别', prop: 'sex', formatter: this.sexFormatter},
        {label: '联系电话', prop: 'phonenumber'},
        {label: '邮箱地址', prop: 'email'},
        {label: '联系地址', prop: 'address'},
        {label: '创建时间', prop: 'createTime', width: '180'},
      ],
      flowCraftIpt: '',
      flowCraftList: [],
      flowCraftColumns: [
        {label: '流程卡号', prop: 'flowCardNo'},
        {label: '订单编号', prop: 'orderNo',width:'120'},
        {label: '自定义编号', prop: 'customNo'},
        {label: '项目名称', prop: 'entryName'},
        {label: '客户名称', prop: 'customerName'},
        {label: '规格', prop: 'specs'},
        {label: '加工要求', prop: 'requirement'},
        {label: '总数量（片）', prop: 'itemNum'},
        {label: '已完工数（片）', prop: 'completeNum'},
        {label: '损坏数量（片）', prop: 'damageNum'},
        {label: '完工数', prop: 'num',width: '150'},
        {label: '备注', prop: 'remarks'},
      ],
      personList: [],
      personDialog: false,
      personSelectList: []
    }
  },
  watch: {
    'completionForm.completionCraftId'(val, oldVal) {
      this.completionForm.completionCraftName = this.craftOptions.filter(item => {
        return item.id == val
      })[0]['craftName']
      this.flowCraftList = []
    },
    'completionForm.completionTeam'(val, oldVal) {
      this.completionPersonList = []
    }
  },
  async mounted() {
    await this.getCraftList();
    await this.getTeamList();
    await this.getInfo(this.$route.query.id);
  },
  methods: {
    getInfo(id) {
      getCompletionInfo(id).then(res => {
        console.log(res)
        let {data} = res
        this.completionForm = {
          id,
          completionNo: data.completionNo,
          completionCraftName: data.completionCraftName,
          completionCraftId: data.completionCraftId,
          completionShift: data.completionShift,
          completionTeam: data.completionTeam,
          completionDate: data.completionDate,
          remarks: data.remarks
        }
        let currentCraft = this.craftOptions.filter(item=>{
          return item.id == data.completionCraftId
        })
        if(currentCraft.length <= 0){
          this.completionForm.completionCraftId = ""
        }
        setTimeout(() => {
          this.completionPersonList = data.completionPersonList
          this.flowCraftList = data.completionBusinessList.map(item => {

            return {
              id: item.id,
              flowCardNo: item.flowCardNo,
              orderNo: item.flowCardInfo.orderNo,
              customNo: item.flowCardInfo.customNo,
              entryName: item.flowCardInfo.entryName,
              customerName: item.flowCardInfo.customerName,
              specs: `${item.flowCardInfo.itemW}*${item.flowCardInfo.itemH}`,
              requirement: item.flowCardInfo.requirement,
              itemNum: item.flowCardInfo.itemNum,
              completeNum: item.num,
              recordId: item.recordId,
              num: item.num,
              remarks: item.flowCardInfo.remarks,
              productId: item.productId,
              itemSurface: item.itemSurface,
              semiProductId: item.semiProductId
            }
          })
        }, 0)
      })
    },
    flowCardFun(e, index) {
      let {value} = e.target
      if (value > this.flowCraftList[index].completeNum) {
        this.$message.warning("完工数量不能大于已完工数")
        this.$set(this.flowCraftList[index], 'num', this.flowCraftList[index].completeNum)
        return false
      }
      if (value < 0) {
        this.$set(this.flowCraftList[index], 'num', 0)
        this.$message.warning("完工数量不能小于0")
        return false
      }
      this.$set(this.flowCraftList[index], 'num', value)
    },
    average() {
      let completionProportion = `${parseInt((100 / Number(this.completionPersonList.length)) * 100) / 100}%`
      this.completionPersonList.forEach((item, index) => {
        item.completionProportion = completionProportion
        item.completionCoefficient = 1
        this.$set(this.personList, index, item)
      })
    },
    coefficientFun(e, index) {
      let {value} = e.target
      this.completionPersonList[index].completionCoefficient = value
      let count = 0
      this.completionPersonList.forEach(item => {
        count += Number(item.completionCoefficient)
      })
      this.completionPersonList.forEach((item, index) => {
        item.completionProportion = `${parseInt((Number(item.completionCoefficient) / count) * 10000) / 100}%`
        this.$set(this.personList, index, item)
      })
    },
    handleSelectionChange(e) {
      this.personSelectList = e
    },
    /* 单击表格行 */
    handleRowClick(row, column, event) {
      this.$refs.templateTableRef.toggleRowSelection(row, column)
    },
    sexFormatter(row, column, cellValue, index) {
      if (this.dict.type.sys_user_sex) {
        let sexArr = this.dict.type.sys_user_sex;
        let sexLabel = '';
        sexArr.forEach(item => {
          if (cellValue == item.value) {
            sexLabel = item.label
          }
        })
        return sexLabel
      }
    },
    personSelect() {
      let completionProportion = `${parseInt((100 / this.personSelectList.length) * 100) / 100}%`
      this.completionPersonList = JSON.parse(JSON.stringify(this.personSelectList.map(item => {
        item.completionPerson = item.nickName
        item.completionProportion = completionProportion
        item.completionCoefficient = 1
        return item
      })))
      this.personDialog = false
    },
    selectPerson() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          listAllUser({teamId: this.completionForm.completionTeam}).then(res => {
            this.personList = res.rows
            this.personDialog = true
          })
        }
      })
    },
    getTeamList() {
      getAllTeam().then(res => {
        this.teamOptions = res.data
      })
    },
    /* 获取工序 */
    getCraftList() {
      getCraftList().then(res => {
        console.log(res)
        if (res.code === 200) {
          this.craftOptions = res.data
        }
      })
      return false;
    },
    /* 获取完工流程卡 */
    handleFlowCraft() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          // if (this.completionPersonList.length <= 0) {
          //   this.$message.error("请完成第二步完工人员后进行操作")
          //   return false
          // }
          let parms = {
            craft: this.completionForm.completionCraftName,
            flowCardNo: this.flowCraftIpt
          }
          if (parms.flowCardNo == '') {
            this.$message.error("请输入流程卡号后进行查询")
            return false
          }
          getFlowCardInfo(parms).then(res => {
            let data = res.data.filter(item => {
              let arr = this.flowCraftList.filter(item2 => {
                return item2.flowCardNo == item.flowCardNo && item2.productId == item.productId && item2.semiProductId == item.semiProductId
              })
              return arr == 0;
            })
            this.flowCraftList = this.flowCraftList.concat(data.map(item => {
              item.completeNum = item.completeNum ?? 0
              item.num = Number(item.itemNum) - Number(item.completeNum)
              item.specs = `${item.itemW}*${item.itemH}`
              delete item.id
              return item
            }))
          })
        }
      })

    },
    /* 保存 */
    handleSave() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          // if (this.completionPersonList.length <= 0) {
          //   this.$message.error("请完成第二步完工人员后进行操作")
          //   return false
          // }
          if (this.flowCraftList.length <= 0) {
            this.$message.error("请填写需完工流程卡信息")
            return false
          }
          let submitForm = {
            ...this.completionForm
          }
          submitForm.completionPersonList = this.completionPersonList.map(item => {
            return {
              id: item.id,
              completionPerson: item.nickName ?? item.completionPerson,
              completionPersonId: item.userId ?? item.completionPersonId,
              completionRecordId: item.completionRecordId,
              completionProportion: item.completionProportion,
              completionCoefficient: item.completionCoefficient
            }
          });
          submitForm.completionNum = 0
          submitForm.completionBusinessList = this.flowCraftList.filter(item => {
            return item.num > 0
          }).map(item => {
            submitForm.completionNum += Number(item.num)
            return {
              id: item.id,
              productId: item.productId,
              flowCardNo: item.flowCardNo,
              num: item.num,
              status: item.num == item.itemNum ? 1 : 0,
              craft: submitForm.completionCraftName,
              itemSurface: item.itemSurface,
              semiProductId: item.semiProductId
            }
          });
          submitForm.flowCardNo = Array.from(new Set(this.flowCraftList.map(item => {
            return item.flowCardNo
          }))).toString()
          editCompletion(submitForm.id, submitForm).then(res => {
            this.$message.success(res.msg)
            this.$router.go(-1)
          })
        }
      })
    },
    /* 返回 */
    handleBack() {
      this.$router.go(-1);
    }
  },
}
</script>

<style lang="scss" scoped>
::v-deep .containerTop {
  height: 240px;
  display: flex;
  border-bottom: 1px solid #dfe6ec;

  .completionInfo {
    height: 100%;
    width: 40%;
    padding: 0 10px;
    box-sizing: border-box;

    .ipt2 {
      .el-form-item {
        width: 80%;
      }

      .remarks {
        .el-textarea__inner {
          min-height: 50px !important;
        }
      }

    }
  }

  .completionPerson {
    border-left: 1px solid #dfe6ec;
    height: 100%;
    width: 60%;
    padding: 0 10px 10px 10px;
    flex-direction: column;
    border-left: 1px solid #dfe6ec;

    .title {
      height: 40px;
      line-height: 40px;
    }

    .completionPersonList {
      height: calc(100% - 40px);
      width: 100%;

      .table {
        height: 100%;
      }

      .page {
        display: none;
      }
    }
  }
}

::v-deep .containerCenter {
  margin-top: 10px;
  height: calc(100% - 295px);
  padding: 0 10px;
  box-sizing: border-box;

  .flowCraftList {
    height: calc(100% - 35px);

    .table {
      height: 100%;
      margin-top: 5px;
    }

    .page {
      display: none;
    }
  }
}

.containerBottom {
  height: 30px;

  .btn {
    justify-content: center;
  }

}


::v-deep  .oneForm {
 .el-form-item{
    width: 90%;
    .el-form-item__content {
      width: calc(100% - 50px);
      .date{
        .el-input__inner {
          padding-right: 15px;
        }
      }
    }
  }
  .remarks{
    width: 95%;
  }


}
</style>
