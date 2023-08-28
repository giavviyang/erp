<template>
  <div class="dashboard-editor-container">
    <el-container class="div_container">
      <el-header>
        <div class="div_header_text">
          硬件加密-信息采集
        </div>
      </el-header>
      <el-main>
        <el-form :model="form" :rules="rules"  ref="form" label-width="100px">
          <el-form-item label="授权系统" prop="systemName">
            <el-input v-model="form.systemName" placeholder="请输入授权系统" clearable></el-input>
          </el-form-item>
          <el-form-item label="授权公司" prop="systemCorp">
            <el-input v-model="form.systemCorp" placeholder="请输入授权公司" clearable></el-input>
          </el-form-item>
          <el-form-item label="授权负责人" prop="person">
            <el-input v-model="form.person" placeholder="请输入授权负责人" clearable></el-input>
          </el-form-item>
          <el-form-item label="被授权公司" prop="buyCorp">
            <el-input v-model="form.buyCorp" placeholder="请输入被授权公司" clearable></el-input>
          </el-form-item>
          <el-form-item label="系统版本号" prop="systemVersion">
            <el-select v-model="form.systemVersion" placeholder="请选择系统版本号" style="width: 100%;"  clearable>
              <el-option label="V1.0.0" value="V1.0.0"></el-option>
              <el-option label="V1.1.0" value="V1.1.0"></el-option>
              <el-option label="V1.2.0" value="V1.2.0"></el-option>
              <el-option label="V2.0.0" value="V2.0.0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="授权类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择授权类型" style="width: 100%;"  clearable>
              <el-option label="永久" value="0"></el-option>
              <el-option label="长期" value="1"></el-option>
              <el-option label="短期" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="授权日期" v-if="form.type != '0'" prop="date">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.date" style="width: 100%;" clearable></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('form')">信息录入</el-button>
            <el-button type="primary" @click="delData">删除录入信息</el-button>
<!--            <el-button type="primary" @click="checkData">获取录入信息</el-button>-->
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>

</template>

<script>
import { add, del, check } from "@/api/dongle";


export default {

  data() {
    return {
      form: {
        systemName: '',
        systemCorp: '',
        person: '',
        buyCorp: '',
        systemVersion: '',
        type: '0',
        date: ''
      },
      rules: {
        systemName: [
          { required: true, message: '请输入授权系统', trigger: 'blur' },
        ],
        systemCorp: [
          { required: true, message: '请输入授权公司', trigger: 'blur' },
        ],
        person: [
          { required: true, message: '请输入授权负责人', trigger: 'blur' },
        ],
        buyCorp: [
          { required: true, message: '请输入被授权公司', trigger: 'blur' },
        ],
        systemVersion: [
          { required: true, message: '请选择系统版本号', trigger: 'change' }
        ],
        type: [
          { required: true, message: '请选择授权类型', trigger: 'change' }
        ],
        date: [
          { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
        ]
      },
    }
  },
  watch: {},
  created() {

  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          add({json: JSON.stringify(this.form)}).then(res => {
            if (res.code == 200) {
              this.$message.success(res.msg)
            }else {
              this.$message.error(res.msg)
            }
          })
        } else {
          this.resetForm()
          return false;
        }
      });
    },
    resetForm() {
      this.form = {
        systemName: '',
        systemCorp: '',
        person: '',
        buyCorp: '',
        systemVersion: '',
        type: '0',
        date: ''
      }
    },
    //删除录入信息
    delData(){
      del().then(res => {
        if (res.code === 200) {
          this.$message.success(res.msg)
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    checkData(){
      check().then(res => {
        alert(res.data)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.div_container {

  /*margin-top: 20px;*/
  width: 40%;
  border-radius: 10px;
  /*background-color: #f0f2f5;*/
  margin-left: 30%;
  margin-top: 50px;
  border: #eaecec 1px solid;


  .div_header_text {
    height: 60px;
    line-height: 60px;
    width: 100%;
    border-bottom: #C0C4CC 1px solid;
  }

  .div_footer_text {
    float: right;
    width: 100%;
    padding-top: 10px;
    border-top: #C0C4CC 1px solid;
  }

  .div_header {
    height: 90%;
    width: 100%;
  }

  .div_footer {
    height: 10%;
    width: 100%;
  }
}

.dashboard-editor-container {
  padding-top: 20px;
  height: 100%;
  /*padding: 32px;*/
  /*background-color: rgb(240, 242, 245);*/
  /*position: relative;*/

  /*.chart-wrapper {*/
  /*  background: #fff;*/
  /*  padding: 16px 16px 0;*/
  /*  margin-bottom: 32px;*/
  /*}*/
}
</style>
