<template>
<!--  <el-dialog-->
<!--    title="系统信息"-->
<!--    :visible.sync="dialogVisible"-->
<!--    width="60%"-->
<!--    class="dialog-style verification" :close-on-click-modal="false" :close-on-press-escape="false"-->
<!--    :destroy-on-close="true">-->
<!--    <el-form ref="form" :model="form" label-width="200px" class="formIpt" :inline="true">-->
<!--&lt;!&ndash;      <el-form-item label="用户名称：">&ndash;&gt;-->
<!--&lt;!&ndash;        <div>{{form.userName}}</div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="系统名称：">&ndash;&gt;-->
<!--&lt;!&ndash;        <div>{{form.sysName}}</div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="系统版本号：">&ndash;&gt;-->
<!--&lt;!&ndash;        <div>{{form.verName}}</div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="部署人姓名：">&ndash;&gt;-->
<!--&lt;!&ndash;        <div>{{form.name}}</div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item label="校验信息：" >&ndash;&gt;-->
<!--&lt;!&ndash;        <div>{{form.verInfo}}</div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--      <el-form-item label="授权人：" >-->
<!--        <div>{{form.userName}}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="被授权人：" >-->
<!--        <div>{{form.name}}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="授权产品名称：" >-->
<!--        <div>{{form.sysName}}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="系统版本号：" >-->
<!--        <div>{{form.verInfo}}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="授权类型：" >-->
<!--        <div>{{form.verType}}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="授权日期至：" >-->
<!--        <div>{{form.verDate}}</div>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="售后联系电话：" >-->
<!--        <div>{{form.verPhone}}</div>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--&lt;!&ndash;    <span slot="footer" class="dialog-footer">&ndash;&gt;-->
<!--&lt;!&ndash;    <el-button @click="dialogVisible = false" size="mini">返回</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;    <el-button type="primary" @click="dialogVisible = false" size="mini">生成校验</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;  </span>&ndash;&gt;-->
<!--  </el-dialog>-->
  <el-dialog title="系统信息" :visible.sync="dialogVisible" width="55%" class="dialog-style verification">
    <el-form ref="form" :model="form" label-width="200px" size="mini" class="formIpt">
      <el-row>
        <el-col :span="12">
          <el-form-item label="授权人：">{{form.userName}}</el-form-item>
          <el-form-item
            label="被授权人："
          >{{form.name}}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="授权产品名称：">{{form.sysName}}</el-form-item>
          <el-form-item label="系统版本号：">{{form.verInfo}}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="授权类型：">{{form.verType}}</el-form-item>
          <el-form-item label="授权日期至：">{{form.verDate}}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="售后联系电话：">{{ form.verPhone }}</el-form-item>
        </el-col>
<!--        <el-col :span="24">-->
<!--          <el-form-item label="返回参数：">{{ form.jsonResult }}</el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="操作状态：">-->
<!--            <div v-if="form.status === 0">正常</div>-->
<!--            <div v-else-if="form.status === 1">失败</div>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="操作时间：">{{ parseTime(form.operTime) }}</el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="24">-->
<!--          <el-form-item label="异常信息：" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>-->
<!--        </el-col>-->
      </el-row>
    </el-form>
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button @click="open = false" size="mini">关 闭</el-button>-->
    <!--      </div>-->
  </el-dialog>
</template>

<script>
export default {
  name: "verification",
  data() {
    return {
      dialogVisible: true,
      form: {
        userName:'某某公司',
        sysName:'玻璃加工系统',
        verName:'V1.0',
        name: '某某公司',
        verInfo:'V1.0',
        verType:'永久',
        verDate:'3023-11-2',
        verPhone:'010-~~~~~~'
      }
    }
  },
  created() {

  },
  methods: {
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {
        });
    }
  }
}
</script>

<style lang="scss" scoped>
.verification {
  ::v-deep .el-dialog {
    height: calc(40vh);
    //background-color: red;
.el-dialog__header{
  .el-dialog__title{
    font-size: 18px;
  }
}
    .el-dialog__body {
      height: calc(100% - 50px);
      display: flex;
      justify-content: center;
      align-items: center;

      .formIpt {
        //width: 85%;
//.el-form-item{
//  width: 48%;
//}
        .el-form-item__label{
          font-size: 20px;
          //font-weight: 500;
          text-align: right;
        }
        div{
          font-size: 20px;
        }
        .el-select {
          width: 100%;
          //.el-form-item__label{
          //  font-size: 20px;
          //  font-weight: 500;
          //}
          //div{
          //  font-size: 20px;
          //}
        }
      }


    }
  }
}
</style>
