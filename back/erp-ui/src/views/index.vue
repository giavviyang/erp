<template>
  <div class="app-container-padding home">
    <div class="containerTop">
      <div class="boxCard" v-for="item in cardInfo" :style="{backgroundColor:item.divColor}">
        <div>
          <div class="cardTitle">{{ item.cardTitle }}</div>
          <p v-if="item.hasOwnProperty('money')">金额：<span>{{ item.money || 0 }}</span>元</p>
          <p v-if="item.hasOwnProperty('area')">面积：<span>{{ item.area || 0 }}</span>m²</p>
          <p v-if="item.hasOwnProperty('monthAmount')">本月收款：<span>{{ item.monthAmount || 0 }}</span>元</p>
          <p v-if="item.hasOwnProperty('weekAmount')">近7天收款：<span>{{ item.weekAmount || 0 }}</span>元</p>
        </div>
        <div>
          <svg-icon :icon-class="item.iconClass" style="color: rgba(0,0,0,0.1);font-size: 80px"/>
        </div>
      </div>
    </div>
    <div class="containerBottom" style="min-height: 350px;">
      <div class="orderFigures">
        <div class="figuresTop">
          <div>订单统计</div>
          <el-radio-group v-model="orderLabelDuration" size="mini" @change="handleChangeProgress">
            <el-radio-button label="currentWeek">近7天</el-radio-button>
            <el-radio-button label="currentMonth">近30天</el-radio-button>
            <el-radio-button label="halfYear">近半年</el-radio-button>
            <el-radio-button label="almostYear">近一年</el-radio-button>
          </el-radio-group>
          <div @click="handleOrderFigures">了解更多</div>
        </div>
        <div class="figuresCenter">
          <div style="position: relative;height: 100%;overflow: auto">
            <el-progress type="circle" :width="200" :stroke-width="20"
                         :percentage="parseInt((progress.total / progress.orderTotal)*100) || 0"></el-progress>
            <span style="display: inline-block;position: absolute;top:calc(50% + 20px);color: #0E98EE">总订单统计</span>
          </div>
          <div>
            <div class="orderInfo">
              <p><span>当前状态数量</span> <span>{{ progress.total }}</span></p>
              <p><span>全部订单数量</span> <span>{{ progress.orderTotal }}</span></p>
            </div>
            <div class="statusProgress">
              <div><span>已审核</span>
                <el-progress :text-inside="true" :stroke-width="20" :percentage="parseInt((progress.reviewed / progress.total)*100) || 0"></el-progress>
              </div>
              <div><span>已分架</span>
                <el-progress :text-inside="true" :stroke-width="20" :percentage="parseInt((progress.split / progress.total)*100) || 0"></el-progress>
              </div>
              <div><span>已生产</span>
                <el-progress :text-inside="true" :stroke-width="20"
                             :percentage="parseInt((progress.produced / progress.total)*100) || 0"></el-progress>
              </div>
              <div><span>已发货</span>
                <el-progress :text-inside="true" :stroke-width="20"
                             :percentage="parseInt((progress.shipped / progress.total)*100) || 0"></el-progress>
              </div>
              <div><span>已收款</span>
                <el-progress :text-inside="true" :stroke-width="20"
                             :percentage="parseInt((progress.received / progress.total)*100) || 0"></el-progress>
              </div>

            </div>
          </div>
        </div>
      </div>
      <div class="productionSchedule">
        <div class="figuresTop">
          <div>生产进度</div>
          <div @click="handleFlowFigures">了解更多</div>
        </div>
        <slot-table class="figuresBottom">
          <el-table highlight-current-row
                    :data="scheduleList"
                    height="100%"
                    style="width: 100%;"
                    slot="table">
            <el-table-column
              type="index"
              label="序号"
              width="50">
            </el-table-column>
            <el-table-column
              show-overflow-tooltip
              v-for="(item,index) in scheduleColumns"
              :key="index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width">
            </el-table-column>
          </el-table>
        </slot-table>
      </div>
    </div>
    <div class="containerCenter" style="padding-bottom: 10px;">
      <div class="productionFigures">
        <div class="figuresTop echartWidth">
          <div>生产统计</div>
          <el-radio-group v-model="productLabelDuration" size="mini" @change="handleChange">
            <el-radio-button label="currentWeek">近7天</el-radio-button>
            <el-radio-button label="currentMonth">近30天</el-radio-button>
            <el-radio-button label="halfYear">近半年</el-radio-button>
            <el-radio-button label="almostYear">近一年</el-radio-button>
          </el-radio-group>
          <div @click="handleProductionFigures">了解更多</div>
        </div>
        <div id="figuresContent" style="height:calc(100% - 30px);"></div>
        <!--        <div id="figuresContent" style="width:100%;height:calc(100% - 30px);"></div>-->
        <!--        <div id="figuresContent" style="width:100%;height:100%"></div>-->
      </div>
      <div class="patchFigures">
        <div class="figuresTop">
          <div>补片统计</div>
          <div @click="handlePatchFigures">了解更多</div>
        </div>
        <div class="figuresCenter">
          <p><span>已补片</span> <span>{{ this.patchData.patch }}</span></p>
          <p><span>未补片</span> <span>{{ this.patchData.noPatch }}</span></p>
        </div>
        <el-table highlight-current-row
          :data="patchData.table"
          height="100%"
          class="figuresBottom">
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="产品名称" prop="product_name" >
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="产品数量" prop="patch_num" width="100">
          </el-table-column>
        </el-table>
      </div>
    </div>

  </div>
</template>

<script>
import SlotTable from "@/components/public/table/SlotTable";
import {getData,getOrder,getProduct} from "@/api/index";
export default {
  name: "Index",
  components: {SlotTable},
  data() {
    return {
      cardInfo: [],
      productLabelDuration: 'currentWeek',
      orderLabelDuration: 'currentWeek',
      progress: {},
      patchData: {},
      scheduleList: [],
      scheduleColumns: [
        {prop: "flow_card_no", label: "流程卡号",width:"100"},
        {prop: "entry_name", label: "项目名称",width:"100"},
        {prop: "custom_no", label: "自编号",width:"100"},
        {prop: "product_name", label: "产品名称",width:"100"},
        {prop: "monolithic_name", label: "单片名称",width:"100"},
        {prop: "total_area", label: "总面积(m²)",width:"100"},
        {prop: "complete_process", label: "完成工序",width:"200"},
      ],
    };
  },
  // mounted() {
  //   this.$nextTick(() => {
  //   this.getAppraisalResults(this.xDataA,this.areaDataA);
  //   });
  //   // console.log(this.$echarts)
  // },
  mounted() {
    this.getList();
    this.getOrderCount(this.orderLabelDuration);
    this.getProductCount(this.productLabelDuration);
  },
  methods: {
    getOrderCount(type){
      getOrder(type).then(res=>{
        this.progress = {
          ...res.data,
          orderTotal:res.count
        }
      })
    },
    getProductCount(type){
      getProduct(type).then(res=>{
        console.log(res)
        this.getAppraisalResults(res.data.map(item=>{
          return item.timeDate
        }), res.data.map(item=>{
          return item.area
        }))
      })
    },
    getList(){
      getData().then(res=>{
        this.cardInfo = [
          {
            divColor: '#FA708B',
            cardTitle: '本月发货量',
            money: res.data.deliveringAmountData.todayPrice.toFixed(2),
            area: res.data.deliveringAmountData.today.toFixed(2),
            iconClass: 'additional'
          },
          {
            divColor: '#38ACF5',
            cardTitle: '本月下单量',
            money: res.data.placeOrderData.todayPrice.toFixed(2),
            area: res.data.placeOrderData.today.toFixed(2),
            iconClass: 'bl'
          },
          {
            divColor: '#3FC8A9',
            cardTitle: '本月成品量',
            area: res.data.finishedProductData.today.toFixed(2),
            iconClass: 'additional'
          },
          {
            divColor:'#8E6BEB',
            cardTitle: '收款统计',
            monthAmount: res.data.collectionData.monthAmount.toFixed(2),
            weekAmount: res.data.collectionData.weekAmount.toFixed(2),
            iconClass: 'sk'
          }
        ]
        this.patchData = {
          noPatch:res.data.patchCountData.damage_num||0 - res.data.patchCountData.patch_num ||0,
          patch: res.data.patchCountData.patch_num ||0,
          table:res.data.patchListCountData
        }
        this.scheduleList = res.data.currentFlowCardData
      })
    },
    getAppraisalResults(xData, areaData) {
      // getAppraisalResults({month:6}).then(response => {
      //   if (response.code == 200) {
      //     let datas = response.data;
      //     this.currentDates = datas.currentDates;
      //     this.notOpens = datas.notOpens;
      //     this.opens = datas.opens;
      //     this.partOpens = datas.partOpens;
      //   }
      this.initChart(xData, areaData);
      // });
      this.handleChangeProgress('currentWeek');
    },
    //生产统计
    initChart(xData, areaData) {
      let echartWidth = document.querySelector('.echartWidth').clientWidth ;
      // document.getElementById('figuresContent').style.width = echartWidth + 'px';
      console.log(echartWidth)
      let chart = this.$echarts.init(document.getElementById('figuresContent'));
      let option = {
        color: '#FEB445',
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          left: 'center'
        },
        grid: {
          // x: 20, // 左边留白
          y: 20, // 上边留白
          // x2: 35, // 右边留白
          y2: 10, // 下边留白
          // right:'10px',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisLabel: {
              color: "#999093"  //y轴文本颜色
            },//坐标轴值
            axisLine: {
              lineStyle: {
                color: "#bbb"  //y轴文本颜色
              },
            },
            // data: ['2021-08', '2021-09', '2021-10', '2021-11', '2021-12', '2022-1', '2022-2', '2022-3', '2022-4', '2022-5', '2022-6', '2022-7']
            data: xData
          },
        ],
        yAxis: [
          {
            type: 'value',
            axisLabel: {
              color: "#999093"  //y轴文本颜色
            },
            axisLine: {
              lineStyle: {
                color: "#bbb"  //y轴文本颜色
              },
            },
            splitLine: {show: false, width: 2},   //去除网格线
          },
        ],
        series: [
          {
            // name: '档案接收数量',
            type: 'line',
            smooth: true,
            label: {
              show: true,
              formatter: '{c}',
            },
            symbolSize: 8, //拐点大小
            areaStyle: {
              normal: {
                color: {
                  type: 'linear',
                  x: 1,
                  y: 0,
                  x2: 1,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: 'rgba(254,180,69,0.8)' // 0% 处的颜色
                  }, {
                    offset: 1, color: 'rgba(254,180,69,0.1)' // 100% 处的颜色
                  }],
                }
              }
            },
            // data: [1000, 1500, 1400, 450, 390, 240, 1000, 400, 780, 1580, 800, 0]
            data: areaData,
          }
        ]
      };

      chart.setOption(option);

      window.addEventListener("resize", function () {
        if (chart) {
          chart.resize();
        }
      });
    },
    /* 切换生产统计按钮 */
    handleChange(val) {
      this.getProductCount(val)
    },
    /* 跳转到生产统计 */
    handleProductionFigures() {
      this.$router.push('/production/productionFigures');
    },
    /* 跳转到补片统计 */
    handlePatchFigures() {
      this.$router.push('/production/patchFigures');
    },
    /* 跳转到订单统计 */
    handleOrderFigures(){
      this.$router.push('/sales/order');
    },
    /* 跳转到生产进度 */
    handleFlowFigures(){
      this.$router.push('/sales/flowCard');
    },
    /* 切换订单统计 */
    handleChangeProgress(val) {
      this.getOrderCount(val)
    },
  },
};
</script>

<style lang="scss" scoped>
.home {
  display: flex;
  flex-direction: column;
  padding-bottom: 10px;
  overflow: auto;

  .containerTop {
    height: 150px;
    display: flex;
    justify-content: space-between;

    .boxCard {
      width: 24%;
      height: 100%;
      display: flex;
      box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
      border-radius: 10px;
      overflow: auto;

      & > div:first-of-type {
        //width: 60%;
        flex-grow: 1;
        height: 100%;
        color: #fff;
        display: flex;
        justify-content: space-around;
        flex-direction: column;
        padding: 20px 0 15px 20px;
        font-size: 13px;


        .cardTitle {
          font-size: 14px;
        }

        p:first-of-type, p:nth-of-type(2) {
          span {
            font-size: 20px;
            display: inline-block;
            margin-right: 5px;
          }
        }

        p:nth-of-type(3) {
          i {
            font-size: 16px;
            font-weight: 600;
          }
        }


      }

      & > div:nth-of-type(2) {
        width: 40%;
        min-width: 100px;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center
      }
    }

    .boxCard:last-of-type {
      margin-right: 0;
    }
  }

  .containerCenter {
    max-height: calc(50% - 125px);
    min-height: 280px;
    min-width: 100px;
    margin-top: 10px;
    display: flex;
    justify-content: space-between;

    .productionFigures {
      width: 74.7%;
    }

    .patchFigures {
      width: 24%;
      overflow: hidden;

      .figuresCenter {
        background-color: #f6f6f6;
        border-radius: 8px;
        margin-top: 5px;
        height: 80px;
        display: flex;
        width: 100%;
        justify-content: space-around;

        p {
          width: 45%;
          display: flex;
          flex-direction: column;
          align-items: center;
          padding: 10px;

          span {
            display: inline-block;
            height: 50%;
            font-size: 13px;
            color: #666;
            line-height: 35px;
          }

          span:nth-of-type(2) {
            color: #38ACF5;
            font-size: 20px;
          }
        }

        p:nth-of-type(2) {
          span:nth-of-type(2) {
            color: #3FC8A9;
            font-size: 20px;
          }
        }
      }

      .figuresBottom {
        width: 100%;
        height: calc(100% - 110px) !important;

        ::v-deep .el-table__header-wrapper {
          display: none !important;
        }

        ::v-deep .el-table__body-wrapper {
          td {
            padding: 5px 0 !important;

            .cell {
              color: #909399;
            }
          }

          td:nth-of-type(2) > .cell {
            text-align: left;
          }

          td:first-of-type > .cell {
            width: 38px;
            color: #fff;
            margin-left: 10px;
            border-radius: 3px;
            background-color: #738394;
            padding-top: 2px;
          }

          tr:first-of-type, tr:nth-of-type(2), tr:nth-of-type(3) {
            td:first-of-type > .cell {
              background-color: #FEB445;
            }
          }
        }
      }
    }

  }

  .containerBottom {
    height: calc(50% - 125px);
    min-width: 100px;
    min-height: 280px;
    margin-top: 10px;
    display: flex;
    justify-content: space-between;;

    .orderFigures {
      box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
      width: 49.4%;
      height: 100%;
      border-radius: 10px;

      .figuresCenter {
        width: 100%;
        height: calc(100% - 30px);
        min-height: 150px;
        display: flex;
        justify-content: space-around;

        & > div:first-of-type {
          width: 50%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
          //overflow: auto;

          //.el-progress {
          //  width: 50%;
          //  height: 70%;
          //  //overflow:auto;
          //  ::v-deep .el-progress-circle {
          //    width: 100% !important;
          //    height: 100% !important;
          //  }
          //
          //  ::v-deep .el-progress__text {
          //    color: #38ACF5;
          //    font-size: 30px !important;
          //  }
          //}

        }

        & > div:nth-of-type(2) {
          height: 100%;
          width: 50%;

          .orderInfo {
            margin-top: 5px;
            height: 80px;
            display: flex;
            width: 100%;
            justify-content: space-around;


            p {
              width: 45%;
              display: flex;
              flex-direction: column;
              align-items: center;
              padding: 10px;

              span {
                display: inline-block;
                height: 50%;
                font-size: 13px;
                color: #666;
                line-height: 35px;
              }

              span:nth-of-type(2) {
                color: #38ACF5;
                font-size: 20px;
              }
            }

            p:nth-of-type(2) {
              span:nth-of-type(2) {
                color: #3FC8A9;
                font-size: 20px;
              }
            }
          }

          .statusProgress {
            width: 100%;
            font-size: 13px;
            height: calc(100% - 130px);
            color: #666;
            display: flex;
            flex-direction: column;
            justify-content: space-around;

            & > div {
              display: flex;
              width: 100%;
              height: 20px;

              .el-progress {
                width: calc(100% - 70px);
                margin-left: 5px;
              }
            }
          }
        }
      }
    }

    .productionSchedule {
      box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
      width: 49.2%;
      border-radius: 10px;

      ::v-deep .figuresBottom {

        height: calc(100% - 30px) !important;

        .table {
          height: 100%;

          .el-table {
            ::v-deep .el-table__body-wrapper {
              ::v-deep td:first-of-type > .cell {
                width: 38px;
                color: #fff !important;
                margin-left: 10px;
                border-radius: 3px;
                background-color: #738394;
                padding-top: 2px;
              }

              tr:first-of-type, tr:nth-of-type(2), tr:nth-of-type(3) {
                td:first-of-type > .cell {
                  background-color: #FEB445;
                }
              }
            }
          }

        }


        .page {
          display: none;
        }

      }


    }
  }

  .productionFigures, .patchFigures, .orderFigures, .productionSchedule {
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
    border-radius: 10px;
    padding: 10px 20px;

    .figuresTop {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 30px;

      & > div:first-of-type {
        font-weight: 600;
        font-size: 16px;
      }

      & > div:last-of-type {
        font-size: 12px;
        color: #999093;
      }
    }
  }
}

</style>

