<template>
  <div id="header">
    <span style="width: 500px" class="dateStyle">{{dateTimeStr}}</span>
    <span class="title">{{title}}</span>
    <span style="width: 500px"></span>
  </div>
</template>

<script>
import {parseTime} from "@/utils/erp";

export default {
  props:{
    title:{
      type:String,
      default:""
    }
  },
  data(){
    return{
      dateTimeStr:''
    }
  },
  mounted() {
    setInterval(()=>{
      this.dateTimeStr = parseTime(new Date,'{y}年{m}月{d}日 {h}时{i}分{s}秒');
    },1000)
  }
}
</script>

<style lang="scss" scoped>
#header{
  width: 100vw;
  height: 3.5rem;
  background: url("../../../../assets/screen/header.png") no-repeat;
  background-size:100% 3.5rem;
  background-attachment:fixed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .title{
    font-size: 1.8rem;
    display: flex;
    justify-content: center;
    align-items: flex-end;
    letter-spacing: .5rem;
    background-image: linear-gradient(to bottom, #ffffff, #99eeff);
    -webkit-background-clip: text;
    color: transparent;
  }
  .dateStyle{
    background-image: linear-gradient(to top, #ffffff, #99eeff);
    -webkit-background-clip: text;
    color: transparent;
    font-size: 0.9rem;
    font-weight: bold;
    padding-left: 3.5rem;
    padding-top: 1.2rem;
  }
}
</style>
