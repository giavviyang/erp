<template>
  <div class="chartsBox">
    <div class="item" v-for="item in list" :key="item?.craft_name" >
      <span class="label">{{ item?.craft_name }}</span>
      <span class="bar">
        <el-progress :show-text="false" :percentage="Number(item.proportion)"></el-progress>
      </span>
      <span class="txt">
        <el-tooltip :content="`${item.area}m²`" placement="top">
        <div>{{item.area}}m²</div>
      </el-tooltip>
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProgressBar",
  props:{
    list: {
      type:Array,
      default:[]
    },
  }
}
</script>

<style lang="scss" scoped>
.chartsBox{
  width: calc(100% - 20px);
  margin-left: 10px;
  height: 100%;
  overflow-y: auto;
  .item{
    width: calc(100% - 10px);
    height: 1.6rem;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    .label{
      width: 50px;
      font-size: .8rem;
      color: #fff;
    }
    .bar{
      width: calc(100% - 130px);

      ::v-deep .el-progress-bar__outer{
        height: .6rem!important;
        background: #1a277b!important;
      }
      ::v-deep .el-progress-bar__inner{
        background: linear-gradient(to right,#67d8fe,#0392ec);
      }
      ::v-deep .el-progress__text{
        color: #08eef2;
        margin-left: 1rem;
        font-size: .6rem!important;
      }
    }
    .txt{
      width: 60px;
      font-size: .8rem;
      color: #08eef2;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>
