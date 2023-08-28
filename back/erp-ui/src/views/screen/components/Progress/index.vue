<template>
  <div id="progress">
    <div class="item">
      本月发货量
      <span>{{deliveringAmountData?.value || 0}}</span>
      m²
      &nbsp;较上月
      <span>{{deliveringAmountData?.proportion || 0}}</span>
      %
      <img :src="require(`../../../../assets/screen/${deliveringAmountData.type == 0 ?'top':'bottom'}.png`)" mode="widthFix"/>
    </div>
    <div class="item">
      本月破片量
      <span>{{fragmentQuantityData?.value || 0}}</span>
      m²
      &nbsp;较上月
      <span>{{fragmentQuantityData?.proportion || 0}}</span>
      %
      <img :src="require(`../../../../assets/screen/${fragmentQuantityData.type == 0 ?'top':'bottom'}.png`)" mode="widthFix"/>
    </div>
    <div class="item">
      本月补片量
      <span>{{patchQuantityData?.value || 0}}</span>
      m²
      &nbsp;较上月
      <span>{{patchQuantityData?.proportion || 0}}</span>
      %
      <img :src="require(`../../../../assets/screen/${patchQuantityData.type == 0 ?'top':'bottom'}.png`)" mode="widthFix"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "index",
  props:{
    deliveringAmountData:Object,
    fragmentQuantityData:Object,
    patchQuantityData:Object
  }
}
</script>

<style lang="scss" scoped>
#progress{
  width: 100%;
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;

  .item{
    width: 20%;
    height: 2rem;
    background: url("../../../../assets/screen/frame.png") no-repeat;
    background-size:100% 2rem;
    color: #FFFFFF;
    font-size: .8rem;
    display: flex;
    align-items: center;
    justify-content: center;
    span{
      font-size: 1.2rem;
      text-align: end;
      margin: 0 .2rem;
    }
    span:first-child{
      color: #ffb33a;
    }
    img{
      width: 1rem;
      margin-left: .2rem;
    }
  }
  .item:nth-child(2){
    margin: 0 1.5rem;
  }
}
</style>
