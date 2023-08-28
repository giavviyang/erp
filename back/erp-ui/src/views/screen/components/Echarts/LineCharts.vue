<template>
  <div class="chartsBox">
    <div id="line"></div>
  </div>
</template>

<script>
import echarts from "echarts"
import {formatDateMonthDay} from "@/utils";

export default {
  name: "LineCharts",
  props:{
    data:Array
  },
  watch:{
    data(val){
      this.data = val
      this.getLoadEcharts(val);
    }
  },
  methods: {
    getLoadEcharts(val) {
      let myChart = echarts.init(document.getElementById("line"));
      let option = {
        grid: {
          top: '8%',
          left: '1%',
          right: '1%',
          bottom: '2%',
          containLabel: true,
        },
        tooltip: {
          formatter: `{c}m²`,
          padding: [9, 16],
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: val.map(item=>{
            return formatDateMonthDay(item.click_date)
          }),
          axisTick: {
            alignWithLabel: true
          },
          // 刻度是否显示
          axisTick: {
            show: false
          },
          // 轴线是否显示
          axisLine: {
            show: false
          },
          axisLabel: {
            color: '#63cff5',
            fontSize: 10,
            interval:0,
            rotate:38
          }

        },
        yAxis: {
          type: 'value',
          splitLine: {
            show: true,
            lineStyle: {
              color: '#0e3054'
            }
          },
          axisTick: {
            show: true
          },
          axisLine: {
            show: false
          },
          axisLabel: {
            color: '#63cff5',
            fontSize: 10
          }
        },
        series: [{
          type: 'line',
          smooth: true,
          showSymbol: false,
          data: val.map(item=>{
            return item.area
          }),
          itemStyle: {
            normal: {
              borderWidth: 3,
              //4个参数用于配置渐变色的起止位置, 这4个参数依次对应右/下/左/上四个方位
              color: new echarts.graphic.LinearGradient(
                1,
                0,
                0,
                0,
                [{
                  offset: 0,
                  color: '#9f55ff'
                },
                  {
                    offset: 0.5,
                    color: '#588fff'
                  },
                  {
                    offset: 1,
                    color: '#16c6ff'
                  }
                ]
              )
            }
          },
          lineStyle: {
            width: 5,
            shadowOffsetY: 0,
            shadowBlur: 0
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

  #line{
    width: 100%;
    height: 100%;
  }
}
</style>
