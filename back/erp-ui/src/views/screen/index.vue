<template>
  <div id="box" style="visibility:hidden;">
    <ScreenHeader title="车间数字看板"/>
    <div class="content">
      <Progress :deliveringAmountData="deliveringAmountData" :fragmentQuantityData="fragmentQuantityData" :patchQuantityData="patchQuantityData"/>
      <div class="layout">
        <div class="layout-left">
          <div class="l-1">
            <Title title="近7天生产量（m²）"/>
            <div style="height: calc(100% - 1.5rem);">
              <LineCharts :data="weekOutputData"/>
            </div>
          </div>
          <div class="l-2">
            <Title title="仅7天排产统计（m²）"/>
            <div style="height: calc(100% - 1.5rem);">
              <BarChart :data="weekSchedulingData" />
            </div>
          </div>
        </div>
        <div class="layout-center">
          <div class="c-1">
            <div class="content">
              <div class="bg1"></div>
              <div class="vertical">
                <div class="item">
                  <div class="item_bg"></div>
                  <div class="item_info">
                    <div>
                      {{parseInt((circleData.completion_num/circleData.shelf_num)*100) || 0}}%
                    </div>
                    <div>总生产进度</div>
                  </div>
                </div>
                <div class="item">
                  <div class="item_bg"></div>
                  <div class="item_info">
                    <div>
                      {{circleData.completion_area||0}}m²
                    </div>
                    <div>本月总产量</div>
                  </div>
                </div>
              </div>
              <div class="cross">
                <div class="item">
                  <div class="item_bg"></div>
                  <div class="item_info">
                    <div>
                      {{parseInt((circleData.completion_num/circleData.all_num)*100)||0}}%
                    </div>
                    <div>总成品率</div>
                  </div>
                </div>
                <div class="item">
                  <div class="item_bg"></div>
                  <div class="item_info">
                    <div>
                      {{ circleData.square||0 }}m²
                    </div>
                    <div>本月原片报损面积</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="layout-right">
          <div class="r-1">
            <Title title="本月各工序产量对比（m²）"/>
            <div style="height: calc(100% - 1.5rem);">
              <ProgressBar :list="monthProcessComparisonData"/>
            </div>
          </div>
          <div class="r-2">
            <Title title="近7天报损统计（m²）"/>
            <div style="height: calc(100% - 1.5rem);">
              <LineChart2 :data="weekDamageData"/>
            </div>
          </div>
        </div>
      </div>
      <div class="buttom">
        <div class="buttom-content">
          <Title title="今日流程卡生产情况"/>
          <div style="height: calc(100% - 1.5rem);">
            <Table :list="currentFlowCardData"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import ScreenHeader from "./components/ScreenHeader/index"
import Progress from "./components/Progress/index"
import Title from "./components/Title/index"
import LineCharts from "./components/Echarts/LineCharts"
import ProgressBar from "./components/Echarts/ProgressBar"
import BarChart from "./components/Echarts/BarChart"
import LineChart2 from "./components/Echarts/LineChart2"
import Table from "./components/Echarts/Table"
import "@/utils/rem.js"
import {getData} from "@/api/screen/screen"

export default {
  components: {ScreenHeader, Progress, Title, LineCharts, ProgressBar, BarChart, LineChart2, Table},
  data(){
    return{
      deliveringAmountData:{},
      fragmentQuantityData:{},
      patchQuantityData:{},
      weekOutputData:[],
      monthProcessComparisonData:[],
      weekSchedulingData:[],
      weekDamageData:[],
      currentFlowCardData:[],
      circleData:[]
    }
  },
  mounted() {
    window.onresize = () => {
      setTimeout(()=>{
        document.getElementById('box').style.visibility = 'hidden';
      },0)
      window.location.reload()
    };
    setTimeout(()=>{
        document.getElementById('box').style.visibility = 'visible';
    },0)
    this.getList()
    setInterval(()=>{
      this.getList()
    },86400*1000)
  },
  methods:{
    getList(){
      getData().then(res=>{
        let contrast = (cur,top)=>{
          if(cur > top){
              return {
                proportion:parseInt(((cur-top)/cur)*100),
                type:"0"
              }
          }
          if (cur < top){
            return {
              proportion:parseInt(((top - cur)/cur)*100),
              type:"1"
            }
          }
          return {
            proportion:0,
            type:"0"
          }
        }
        let {deliveringAmountData,fragmentQuantityData,patchQuantityData,weekOutputData,monthProcessComparisonData,weekSchedulingData,weekDamageData,currentFlowCardData,monthOrderData,monthOriginalFilmLossData} = res.data
        this.deliveringAmountData = {
          value:deliveringAmountData.currentMonth,
          ...contrast(deliveringAmountData.currentMonth,deliveringAmountData.topMonth)
        }
        this.fragmentQuantityData = {
          value:fragmentQuantityData.currentMonth,
          ...contrast(fragmentQuantityData.currentMonth,fragmentQuantityData.topMonth)
        }
        this.patchQuantityData = {
          value:patchQuantityData.currentMonth,
          ...contrast(patchQuantityData.currentMonth,patchQuantityData.topMonth)
        }
        this.weekOutputData = weekOutputData
        let total = monthProcessComparisonData.map(item=>{ return item.area }).reduce((per,cur)=>{return per + cur})
        this.monthProcessComparisonData = monthProcessComparisonData.map(item=>{
          return {
            ...item,
            proportion:parseInt((item.area / total)*100)
          }
        })
        this.weekSchedulingData = weekSchedulingData
        this.weekDamageData = weekDamageData
        this.currentFlowCardData = currentFlowCardData
        this.circleData = {
          ...monthOrderData,
          ...monthOriginalFilmLossData
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
/** ----- 滚动条样式 ----- */
::v-deep ::-webkit-scrollbar {
  width: 3px !important;
  height: 4px !important;
  background-color: #03243d !important;
  border-radius: 6px !important;
}

::v-deep ::-webkit-scrollbar-thumb {
  background-color: #05cff6 !important;
  border-radius: 6px !important;
  height: 1px !important;
}

::v-deep ::-webkit-scrollbar-corner {
  background-color: #03243d!important;
}
/** ------------------ */
#box {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  -moz-user-select: none;
  -khtml-user-select: none;
  user-select: none;
  background: url("../../assets/screen/bg.png") no-repeat;
  background-size: 100% 100%;
  background-attachment: fixed;

  .content {
    width: 100%;
    margin-top: 1.5rem;
    height: calc(100vh - 5rem);
    position: relative;

    .layout {
      margin: 0 5rem;
      margin-top: 2rem;
      height: calc(100% - 13rem);
      display: flex;
      align-items: center;
      justify-content: space-between;

      .layout-left {
        padding-top: 2rem;
        width: 35%;
        height: 100%;
        display: flex;
        flex-flow: column;
        justify-content: space-between;
        perspective: 300px;

        .l-1 {
          width: 100%;
          height: 50%;
          background: url("../../assets/screen/framebox.png") no-repeat;
          background-size: 100% 100%;
          padding: .5rem;
          transform: rotateY(8deg);
        }

        .l-2 {
          width: 78%;
          height: calc(50% - 1rem);
          background: url("../../assets/screen/framebox.png") no-repeat;
          background-size: 100% 100%;
          padding: .5rem;
          transform: rotateY(8deg);
        }
      }

      .layout-center {
        width: 30%;
        height: 100%;
        position: relative;
        .c-1 {
          margin-top: 2rem;
          width: 100%;
          height: calc(100% - 6.5rem);
          display: flex;
          align-items: center;
          justify-content: center;
          position: relative;

          @-webkit-keyframes rotation {
            from {
              -webkit-transform: rotate(0deg);
            }
            to {
              -webkit-transform: rotate(360deg);
            }
          }
          @-webkit-keyframes rotation2 {
            from {
              -webkit-transform: rotate(360deg);
            }
            to {
              -webkit-transform: rotate(0deg);
            }
          }

          .bg1 {
            -webkit-transform: rotate(360deg);
            animation: rotation 3s linear infinite;
            -moz-animation: rotation 3s linear infinite;
            -webkit-animation: rotation 3s linear infinite;
            -o-animation: rotation 3s linear infinite;
            width: 100%;
            height: 100%;
            background: url("../../assets/screen/center.png") no-repeat;
            background-size: 50%;
            background-position: center;
            position: absolute;
            left: 0;
            top: 0;
          }
        }

        .content {
          position: absolute;
          width: 100%;
          height: 26rem;
          top: 33%;
          transform: translateY(-50%);

          .vertical {
            width: 50%;
            height: 100%;
            margin: 0 auto;
            display: flex;
            align-items: center;
            flex-flow: column;
            justify-content: space-between;
          }

          .cross {
            height: 50%;
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
          }

          .item {
            width: 10rem;
            height: 10rem;
            position: relative;

            .item_bg {
              -webkit-transform: rotate(360deg);
              animation: rotation2 3s linear infinite;
              -moz-animation: rotation2 3s linear infinite;
              -webkit-animation: rotation2 3s linear infinite;
              -o-animation: rotation2 3s linear infinite;
              width: 100%;
              height: 100%;
              background: url("../../assets/screen/little.png") no-repeat;
              background-size: 100%;
            }

            .item_info {
              position: absolute;
              left: 10%;
              top: 10%;
              width: 80%;
              height: 80%;
              display: flex;
              flex-flow: column;
              align-items: center;
              justify-content: center;
              margin-top: .5rem;

              div {
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 2.5rem;
                color: #FFFFFF;
                font-weight: bold;
              }

              div:last-child {
                font-size: .8rem;
                margin-top: .1rem;
              }
            }
          }
        }
      }

      .layout-right {
        padding-top: 2rem;
        width: 35%;
        height: 100%;
        display: flex;
        flex-flow: column;
        align-items: flex-end;
        justify-content: space-between;
        perspective: 300px;

        .r-1 {
          width: 100%;
          height: 50%;
          background: url("../../assets/screen/framebox.png") no-repeat;
          background-size: 100% 100%;
          padding: .5rem;
          transform: rotateY(-8deg);
        }

        .r-2 {
          width: 78%;
          height: calc(50% - 1rem);
          background: url("../../assets/screen/framebox.png") no-repeat;
          background-size: 100% 100%;
          padding: .5rem;
          transform: rotateY(-8deg);
        }
      }
    }

    .buttom {
      width: 74%;
      height: 15rem;
      background: red;
      position: absolute;
      left: 13%;
      bottom: 0;
      background: url("../../assets/screen/buttom_bg.png") no-repeat;
      background-size: 100% 100%;

      .buttom-content {
        width: 50%;
        margin-left: 25%;
        height: calc(75% - .2rem);
        margin-top: 1rem;
      }
    }
  }
}
</style>
