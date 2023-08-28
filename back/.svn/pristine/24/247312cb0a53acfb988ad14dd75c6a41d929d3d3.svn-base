<template>
  <div class="chartsBox">
    <div id="line2"></div>
  </div>
</template>

<script>
import echarts from "echarts"
import {formatDateMonthDay} from "@/utils";

export default {
  name: "LineChart2",
  props:{
    data:Array
  },
  watch:{
    data(val){
      this.getLoadEcharts(val);
    }
  },
  methods: {
    getLoadEcharts(val) {
      let myChart = echarts.init(document.getElementById("line2"));
      let option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            label: {
              show: true,
              backgroundColor: '#fff',
              color: '#556677',
              borderColor: 'rgba(0,0,0,0)',
              shadowColor: 'rgba(0,0,0,0)',
              shadowOffsetY: 0,
            },
            lineStyle: {
              width: 0,
            },
          },
          backgroundColor: '#fff',
          textStyle: {
            color: '#5c6c7c',
          },
          formatter: function (params) {
            return `${params[0].value}m²`
          }
        },
        grid: {
          top: '10%',
          right: '5%',
          bottom: '15%'
        },
        xAxis: [
          {
            type: 'category',
            data: val.map(item=>{
              return formatDateMonthDay(item.click_date)
            }),
            axisLine: {
              lineStyle: {
                color: '#0173DA',
              },
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              interval: 0,
              textStyle: {
                color: '#FFFFFF',
              },
              // 默认x轴字体大小
              fontSize: 12,
              // margin:文字到x轴的距离
            },
            boundaryGap: false,
            splitLine: {
              show: true,
              lineStyle: {
                type: 'dashed',
                color: '#0173DA',
              },
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            axisTick: {
              show: false,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: '#0173DA',
              },
            },
            axisLabel: {
              textStyle: {
                color: '#FFFFFF',
              },
            },
            splitLine: {
              show: true,
              lineStyle: {
                type: 'dashed',
                color: '#0173DA',
              },
            },
          },
          {
            type: 'value',
            position: 'right',
            axisTick: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: '#0173DA',
              },
              formatter: '{value}m²',
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: '#0173DA',
              },
            },
            splitLine: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: '报损面积',
            type: 'line',
            data: val.map(item=>{
              return item.area
            }),
            symbolSize: 1,
            symbol: 'circle',
            smooth: true,
            yAxisIndex: 0,
            showSymbol: false,
            lineStyle: {
              width: 2,
              color: '#00DAFF',
            },
            itemStyle: {
              normal: {
                color: '#00DAFF',
                borderColor: '#00DAFF',
              },
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: '#00DAFF',
                    },
                    {
                      offset: 1,
                      color: 'rgba(0,0,0,0)',
                    },
                  ],
                  false
                ),
              },
            },
          },
        ],
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

  #line2{
    width: 100%;
    height: 100%;
  }
}
</style>
