<template>
  <div class="app-container-padding" style="display: flex;justify-content: space-between;height: calc(100% - 10px)">
    <slot-table class="slotTable" style="width:calc(30% - 5px) ;height: 100%;">
      <el-table highlight-current-row
        v-loading="loading"
        ref="materialTable"
        size="mini"
        :data="materialTableData"
        height="100%"
        row-key="id"
        border
        stripe
        empty-text=" "
        highlight-current-row
        @row-click="StepsListRowClick" slot="table">
        <el-table-column label="序号" width="50px" type="index" align="center"/>
        <el-table-column label="主键" prop="id" v-if=false align="center"/>
        <el-table-column label="文件名称" prop="fileName" min-width="150"  align="center"/>
<!--        <el-table-column label="文件大小" prop="fileSize" show-overflow-tooltip min-width="80"  align="center"/>-->
<!--        <el-table-column label="上传人" prop="createdPerson" show-overflow-tooltip  min-width="100px" align="center"/>-->
<!--        <el-table-column label="上传时间" prop="createdAt" show-overflow-tooltip min-width="120" align="center"/>-->
      </el-table>
    </slot-table>
    <div style="width:calc(70% - 5px);height: 100%;">
      <iframe class="prism-player" ref="pdfIframe" :src="pdfUrl" width="100%" height="100%"></iframe>
    </div>
  </div>
</template>

<script>

import {
  queryAllFile
} from "@/api/salse/order/orderContract";
import SlotTable from "@/components/public/table/SlotTable";

const baseURL = process.env.VUE_APP_BASE_API;
export default {
  name: "",
  components: {
    SlotTable
  },
  mounted() {
    // console.log(this.$route,this.$router)
    this.getThenOnlinePreviewFile(this.$route.query.id);
    this.queryAllFile();
  },
  data() {
    return{
      id: '',
      pdfUrl: '/pdfjs/web/viewer.html',
      loading: false,
      materialTableData: [],   //表格内文件数据
      // tableHeight:'88%',  //表格高度
      formSearch: {
        pageSize: 20,
        pageNum: 1,
      },
      total: 0,
    }
  },
  methods: {
    //查询该合同下的所有文件
    queryAllFile() {
      this.id = this.$route.query.id;
      console.log(this.id)
      queryAllFile({id : this.id, pageNum : this.formSearch.pageNum, pageSize: this.formSearch.pageSize}).then(res => {
        this.total = res.count;
        this.materialTableData = res.data;
      })
    },
    //根据传入的fileId查询文件数据
    getThenOnlinePreviewFile(val) {

      let serverUrl = '/pdfjs/web/viewer.html';
      let pdfUrl = baseURL + "/sales/contract/pdfView?id=" + val;// 调取接口返回文件流
      this.pdfUrl = serverUrl + "?file=" + encodeURIComponent(pdfUrl);
    },
    // headClass() {
    //   return "background:rgba(204, 204, 204, 0.18);color:#909399;height:31px;font-size:12px";
    // },
    //点击行事件
    StepsListRowClick(row) {
      this.getThenOnlinePreviewFile(row.id);
    },
    handleSizeChange(val) {
      this.formSearch.pageSize = val
        this.queryAllFile()
    },
    handleCurrentChange(val) {
      this.formSearch.pageNum = val
        this.queryAllFile()
    },
    fallback() {
      this.$router.go(-1);
    },
  },
}
</script>
<style scoped lang="scss">
::v-deep .slotTable{
  .table{
    height: 100%;
    .el-table--group::after, .el-table--border::after {
      top: 0;
      right: 0;
     //width: 0;
      height: 100%;
    }
  }
  .page{
    display: none;
  }
}

</style>

