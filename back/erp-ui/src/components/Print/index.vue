<template>
  <el-dropdown @command="toPrint">
    <el-button type="primary" size="mini" icon="el-icon-printer" v-hasPermi="['sales:order:print']">
      打印<i class="el-icon-arrow-down el-icon--right"></i>
    </el-button>
    <el-dropdown-menu slot="dropdown">
      <!-- 跳转打印页面时 传入模板id-->
      <el-dropdown-item v-for="item in printList" :command="item.id" icon="el-icon-printer">{{ item.templateName }}</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import {getTemplateList} from "@/api/system/template";

export default {
  props:{
    typeId:String
  },
  data(){
    return{
      printList:[]
    }
  },
  mounted() {
    getTemplateList({typeId:this.typeId,pageNum:1,pageSize:100}).then(res=>{
      this.printList = res.data
    })
  },
  methods:{
    toPrint(printId) {
      this.$emit("toPrint",printId)
    },
  }
}
</script>

<style scoped>

</style>
