<template>
  <div class="chartsBox">
    <div id="bar"></div>
  </div>
</template>

<script>
import echarts from "echarts";
import {formatDateMonthDay} from "@/utils";

export default {
  name: "BarChart",
  props:{
    data:Array
  },
  watch:{
    data(val){
      this.getLoadEcharts(val)
    }
  },
  methods: {
    getLoadEcharts(val) {
      let myChart = echarts.init(document.getElementById("bar"));
      let option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function (params) {
            return `${params[0].value}m²`
          }
        },
        grid: {
          top: '5%',
          right: '8%',
          left: '15%',
          bottom: '22%'
        },
        xAxis: [{
          type: 'category',
          data: val.map(item=>{
            return formatDateMonthDay(item.click_date)
          }),
          axisLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0.12)'
            }
          },
          axisLabel: {
            margin: 5,
            color: '#63cff5',
            rotate:38,
            textStyle: {
              fontSize: 10
            },
          },
        }],
        yAxis: [{
          splitNumber: 5,
          axisLabel: {
            formatter: '{value}',
            color: '#e2e9ff',
          },
          axisLine: {
            show: false
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#233e64'
            }
          },
        }],
        series: [{
          type: 'bar',
          data: val.map(item=>{
            return item.area
          }),
          barWidth: '10px',
          itemStyle: {
            normal: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(0,244,255,1)' // 0% 处的颜色
              }, {
                offset: 1,
                color: 'rgba(0,77,167,1)' // 100% 处的颜色
              }], false),
              barBorderRadius: [30, 30, 30, 30],
              shadowColor: 'rgba(0,160,221,1)',
              shadowBlur: 4,
            }
          }
        }]
      };
      myChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.chartsBox {
  width: 100%;
  height: 100%;
  background: url("../../../../assets/screen/echartsBg.png") no-repeat;
  background-size: 100% 100%;
  background-position: bottom;

  #bar {
    width: 100%;
    height: 100%;
  }
}
</style>
