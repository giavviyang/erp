<template>
  <div class="app-container-padding">
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" class="iptAndBtn">
      <el-form-item label="合同标题">
        <el-input v-model="queryParams.contractTitle"  @keyup.enter.native="handleQuery" clearable placeholder="请输入合同标题"/>
      </el-form-item>
      <el-form-item label="负责人">
        <el-input v-model="queryParams.contractPerson"  @keyup.enter.native="handleQuery" clearable placeholder="请输入负责人"/>
      </el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
    </el-form>
    <div class="btn">
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['sales:contract:addContract']">新增合同
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-edit"
        size="mini"
        @click="handleUpdate"
        v-hasPermi="['sales:contract:updateContract']">编辑合同
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-delete"
        size="mini"
        @click="handleDelete"  v-hasPermi="['sales:contract:delContract']">删除合同
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-paperclip"
        size="mini"
        @click="handleUpload"  v-hasPermi="['sales:contract:uploadContractFile']">上传附件
      </el-button>
      <el-button
        type="primary"
        :icon="tableSizeIcon"
        size="mini"
        style="position: absolute;right: 10px"
        @click="changeTableHeight"
      >{{ tableSize }}
      </el-button>
    </div>
    <slot-table class="contractTable" @handleChange="handleChange" :pageSize="pageSize" :pageNum="pageNum"
                :total="total">
      <el-table highlight-current-row


        :data="contractList"
        stripe
        border
        style="width: 100%"
        height="100%"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        slot="table">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          :index="getIndex"
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="item in contractColumns"
          :key="item.id"
          :label="item.label"
          :min-width="item.width"
          :prop="item.prop"
          show-overflow-tooltip>
        </el-table-column>
      </el-table>
    </slot-table>
    <slot-table class="contractFile">
      <el-table highlight-current-row
        :data="contractFile"
        stripe
        border
        style="width: 100%"
        height="100%"
        slot="table">
        <el-table-column
          type="index"
          label="序号"
          width="55" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          v-for="item in contractFileColumns"
          :key="item.id"
          :label="item.label"
          :prop="item.prop"
          :min-width="item.width"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          label="操作"
          width="150" class-name="operation">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handlePdf(scope.row)"  v-hasPermi="['sales:contract:pdfView']">预览</el-button>
            <el-button type="text" size="mini" @click="handeLoadFile(scope.row)"  v-hasPermi="['sales:contract:downloadContractFile']">下载</el-button>
            <el-button type="text" size="mini" @click.native.prevent="deleteRowFile(scope.row)"  v-hasPermi="['sales:contract:delContractFile']">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </slot-table>
    <el-dialog :title="dialogType==='add'?'新增合同':dialogType==='edit'?'编辑合同':''" :visible.sync="dialogVisible"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true"
               width="50%" class="dialog-style contract">
      <el-form ref="form" :model="form" size="mini" :inline="true" class="ipt2">
        <el-form-item label="合同标题" prop="contractTitle">
          <el-input v-model="form.contractTitle" placeholder="请输入合同标题" maxlength="30"/>
        </el-form-item>
        <el-form-item label="合同介绍" prop="contractIntroduce">
          <el-input v-model="form.contractIntroduce" placeholder="请输入合同介绍" maxlength="30"/>
        </el-form-item>
        <el-form-item label="负责人" prop="contractPerson">
          <el-input v-model="form.contractPerson" placeholder="请输入负责人" maxlength="30"/>
        </el-form-item>
        <el-form-item label="合同金额" prop="contractMoney">
          <el-input v-model="form.contractMoney" placeholder="请输入合同金额" maxlength="30"/>
        </el-form-item>
        <el-form-item label="合同日期" prop="preparationDate">
          <el-date-picker
            v-model="form.contractDate"
            type="date"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="contractRemarks" class="remarks">
          <el-input v-model="form.contractRemarks" type="textarea" size="mini" placeholder="请输入内容" maxlength="200"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="handleSave('form')">保存</el-button>
        <el-button size="mini" @click="dialogVisible=false">取消</el-button>
      </span>
    </el-dialog>
    <!--  上传附件  -->
    <el-dialog title="上传附件" :visible.sync="uploadPdfDialog" width="30%" class="dialog-style uploadPdfDialog"
               :close-on-click-modal="false"  :close-on-press-escape="false" :destroy-on-close="true" :before-close="handleClose">
      <el-upload drag
                 :auto-upload="false"
                 :action="uploadUrl()"
                 :on-change="fileChange"
                 :on-remove="handleRemove"
                 :file-list="fileList"
      accept="pdf">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <!--        <div class="el-upload__tip" slot="tip">只能上传xlsx文件，且不超过10M</div>-->
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="submitUpload">上传</el-button>
<!--         <el-button size="mini" @click="uploadPdfDialog=false;fileList=[]">取消</el-button>-->
         <el-button size="mini" @click="cancelUpload">取消</el-button>
       </span>
    </el-dialog>
  </div>
</template>

<script>

import SlotTable from "@/components/public/table/SlotTable";
import {
  addContract,
  queryContractList,
  updateContract,
  delContract,
  uploadContractFile, queryContractFile, delContractFile
} from "@/api/salse/order/orderContract";
import {Message} from "element-ui";
import {getCurrentDay} from "@/utils/order/order";
export default {
  name: "index",
  components: {SlotTable},

  data() {
    //数值、小数点验证
    var checkContractMoney = (rule, value, callback) => {
      let reg = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g;
      if (!reg.test(value)) {
        callback(new Error('请输入数值'));
      } else {
        callback();
      }
    };
    return {
      //查询参数
      queryParams: {
        contractTitle: '',  //合同标题
        contractPerson: '', //负责人
        pageSize: 20,
        pageNum: 1,
        // startDate:'',
        // endDate:'',
      },
      tableSize: '最大化',
      tableSizeIcon: 'el-icon-zoom-in',
      tableFlag: false,
      // preparationDateRange: [], //合同日期范围
      pageSize: 20,
      pageNum: 1,
      total: 0,
      contractList: [],  //合同列表
      selected: [],
      contractColumns: [
        {label: '合同标题', prop: 'contractTitle', width: '150'},
        {label: '合同介绍', prop: 'contractIntroduce', width: '150'},
        {label: '负责人', prop: 'contractPerson', width: '100'},
        {label: '合同金额（元）', prop: 'contractMoney', width: '150'},
        {label: '合同日期', prop: 'contractDate', width: '180'},
        {label: '创建人', prop: 'createdPerson', width: '100'},
        {label: '创建日期', prop: 'createdAt', width: '180'},
        {label: '备注', prop: 'contractRemarks'},
      ],
      dialogType: 'add',  //弹窗类型
      dialogVisible: false,  //弹窗
      currentId: '',
      //合同表单
      form: {
        contractTitle: '',
        contractIntroduce: '',
        contractPerson: '',
        contractMoney: '',
        contractDate: getCurrentDay(),
        contractRemarks: '',
      },
      // 下单时间不能晚于当前时间
      preparationDateOptions: {
        disabledDate: time => {
          return time.getTime() > Date.now()
        },
      },
      uploadPdfDialog: false,  //上传附件弹窗
      fileList: [],
      contractFile: [],  //合同文件
      contractFileColumns: [
        {label: '文件名称', prop: 'fileName',width: '180'},
        {label: '文件大小', prop: 'fileSize', width: '100'},
        {label: '上传日期', prop: 'createdAt', width: '180'},
        {label: '上传人', prop: 'createdPerson', width: '100'},
        {label: '文件地址', prop: 'fileAddress', width: '180'},
      ],
      // 表单校验
      rules: {
        contractTitle: [
          {required: true, message: "合同标题不能为空", trigger: "blur"},
          {max: 125, message: '字符长度最大不得超过125', trigger: 'blur'}
        ],
        contractIntroduce: [
          {max: 125, message: '字符长度最大不得超过125', trigger: 'blur'}
        ],
        contractPerson: [
          {required: true, message: "负责人不能为空", trigger: "blur"},
          {max: 125, message: '字符长度最大不得超过125', trigger: 'blur'}
        ],
        contractMoney: [
          // {max: 125, message: '字符长度最大不得超过125', trigger: 'blur'}
          // {validator: checkContractMoney, trigger: 'blur'}
        ],
      },
    }
  },
  created() {
    this.handleQuery();
  },
  mounted() {
    // this.keyupSubmit();
  },
   methods: {

    //键盘按下enter搜索事件
    keyupSubmit() {
      document.onkeydown = e => {
        const _key = window.event.keyCode
        if (_key === 13) {
          this.handleQuery();
        }
      }
    },
    /* 条件查询 */
    handleQuery() {
      // if (this.preparationDateRange) {
      //   this.queryParams.startDate = this.preparationDateRange[0];
      //   this.queryParams.endDate = this.preparationDateRange[1];
      // }
      queryContractList(this.queryParams).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.contractList = res.data;
          this.total = res.count;
          if (res.data) {
            this.currentId = res.data[0].id;
            this.getContractFile();
          }
        }
      })
    },
    /* 调整订单表格最大化 */
    changeTableHeight() {
      let contractTable = document.querySelector('.contractTable');
      if (this.tableFlag) {
        this.tableSizeIcon = 'el-icon-zoom-in';
        this.tableSize = '最大化';
        contractTable.style.height = '50%';
        contractTable.style.marginBottom = 'none';
      }
      if (!this.tableFlag) {
        this.tableSizeIcon = 'el-icon-zoom-out';
        this.tableSize = '还原';
        contractTable.style.height = 'calc(100% - 100px)';
        contractTable.style.marginBottom = '5px';
      }
      this.tableFlag = !this.tableFlag;
    },
    /* 新增合同 */
    handleAdd() {
      this.dialogType = 'add';
      this.dialogVisible = true;
      this.form = {
        contractTitle: '',
        contractIntroduce: '',
        contractPerson: '',
        contractMoney: '',
        contractDate: getCurrentDay(),
        contractRemarks: '',
      };
    },
    /* 编辑合同 */
    handleUpdate() {
      // console.log(this.selected)
      if (this.selected.length <= 0) {
        Message.warning("请选择要修改的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行修改");
        return false
      }
      if (this.selected.length == 1) {
        // this.currentId=this.selected[0].id;
        this.form = JSON.parse(JSON.stringify(this.selected[0]));
        // this.form=this.selected[0];
        // console.log(this.form)
        this.dialogVisible = true;
        this.dialogType = 'edit';
      }
    },
    /* 保存 */
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogType === 'add') {
            addContract(this.form).then(res => {
              // console.log(res)
              if (res.code === 200) {
                Message.success(res.msg);
                this.dialogVisible = false;
                this.dialogType = '';
                this.handleQuery();
              }
            })
          }
          if (this.dialogType === 'edit') {
            updateContract(this.form).then(res => {
              if (res.code === 200) {
                Message.success(res.msg);
                this.dialogVisible = false;
                this.dialogType = '';
                this.handleQuery();
              }
            })
          }
        } else {
          return false;
        }
      })
    },
    /* 删除合同 */
    handleDelete() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要删除的数据");
      } else {
        this.$confirm('此操作将删除选中数据,是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          let ids = this.selected.map(item => {
            return item.id
          }).join(",");
          delContract({ids: ids.toString()}).then(res => {
            Message.success(res.msg);
            this.handleQuery();
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });

      }
    },
    /* 上传附件 */
    handleUpload() {
      if (this.selected.length <= 0) {
        Message.warning("请选择要上传附件的数据");
        return false
      }
      if (this.selected.length > 1) {
        Message.warning("请选择一条数据进行修改");
        return false
      }
      if (this.selected.length == 1) {
        this.fileList = [];
        this.currentId = this.selected[0].id;
        this.uploadPdfDialog = true;

      }
    },
    uploadUrl: function () {
      // 因为action参数是必填项，我们使用二次确认进行文件上传时，直接填上传文件的url会因为没有参数导致api报404，所以这里将action设置为一个返回为空的方法就行，避免抛错
      return ''
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
      console.log('0000')
    },
    fileChange(file) {
      console.log(file)
      let extension = file.name.substring(file.name.lastIndexOf('.') + 1)
      this.fileList = [];
      if (extension !== 'pdf') {
        this.$message.warning('只能上传后缀是.pdf的文件');
        return false
      }
      if (extension === 'pdf') {
        this.fileList.push(file.raw);
      }
    },
    /* 上传文件 */
    submitUpload() {
      if (this.fileList.length <= 0) {
        this.$message.error('请选择文件');
        return
      } else {
        // console.log("123" + JSON.stringify(this.fileList[0]))
        let form = new FormData()
        form.append('file', this.fileList[0])
        form.append('id', this.currentId);
        console.log(form)
        uploadContractFile(form).then(res => {
          if (res.code === 200) {
            this.$message({
              message: '附件上传成功',
              type: 'success'
            });
            this.uploadPdfDialog = false;
            this.fileList=[];
            // this.getQueryEnclosure();
            this.getContractFile();
          }
        })
      }

    },
     /* 取消 */
     cancelUpload(){
       this.uploadPdfDialog=false;this.fileList=[]
     },
     handleClose(done) {
       this.uploadPdfDialog = false;
       this.fileList = []
     },
    /* 分页器 */
    handleChange(size, num) {
      this.pageSize = size;
      this.pageNum = num;
      this.queryParams.pageNum = this.pageNum;
      this.queryParams.pageSize = this.pageSize;
      this.handleQuery();
    },
    /* 表格复选框 */
    handleSelectionChange(val) {
      this.selected = val;
      console.log(val)
      if (val !== undefined && val.length > 0) {
        this.currentId = val.slice(-1)[0].id;
        this.getContractFile();
      }
    },
    /* 产品列表点击行 */
    handleRowClick(row, column) {
      this.$refs.multipleTable.toggleRowSelection(row, column);
      this.currentId = row.id;
      this.getContractFile();
      // console.log(this.currentId)
    },
    /* 翻页后，序号连贯 */
    getIndex($index) {
      //  表格序号
      return (this.pageNum - 1) * this.pageSize + $index + 1;
    },
    /* 查看合同附件 */
    getContractFile() {
      queryContractFile({id: this.currentId}).then(res => {
        // console.log(res)
        // this.contractFile
        if (res.code === 200) {
          this.contractFile = res.data;
        }
      })
    },
    /* 预览pdf */
    handlePdf(row){
      // this.$router.push({path: '/pdf/' + row.id});
      let pathInfo = this.$router.resolve({
        path:'/pdf',
        query:{
          id:row.id
        }
      })
      // console.log(pathInfo.href)
      window.open(pathInfo.href, '_blank');
    },
    /* 移除附属文件 */
    deleteRowFile(row) {
      this.$confirm('此操作将删除选中数据,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        let ids = this.selected.map(item => {
          return item.id
        }).join(",");
        delContractFile({
          id: row.id,
        }).then(res => {
          if (res.code === 200) {
            this.$message({
              message: res.msg,
              type: 'success'
            });
            this.getContractFile()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

    },
    /* 下载附属文件 */
    handeLoadFile(row) {
      this.download('/sales/contract/downloadContractFile', {
        id: row.id
      }, row.fileName)
    },

  }
}
</script>

<style lang="scss" scoped>
.contractTable {
  //height: calc(100% - 105px);
  height: 50%;
}

::v-deep .contract {
  .el-form-item {
    width: 50%;
  }
}

::v-deep .uploadPdfDialog {
  .el-dialog {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}

::v-deep .contractFile {
  height: calc(50% - 105px);

  .table {
    height: 100%;
  }

  .page {
    display: none;
  }
}

</style>
