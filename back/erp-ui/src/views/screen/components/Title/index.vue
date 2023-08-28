<template>
  <div id="title">{{title}}</div>
</template>
<script>
export default {
  props:{
    title:{
      type:String,
      default:""
    }
  }
}
</script>
<style lang="scss" scoped>
#title{
  width: 100%;
  height: 1.5rem;
  display: flex;
  align-items: center;
  background: url("../../../../assets/screen/title.png") no-repeat;
  background-size: 40px .5rem;
  background-position: left center;
  padding-left: 3rem;
  font-size: .8rem;
  color: #FFFFFF;
  margin-left: .1rem;
}
</style>
