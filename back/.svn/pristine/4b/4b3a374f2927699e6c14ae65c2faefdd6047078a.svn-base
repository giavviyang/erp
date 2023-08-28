<template>
  <div id="box" v-html="html"></div>
</template>
<script>
const _document = document
const _window = window
</script>
<script setup>
import {ref,nextTick,onMounted,getCurrentInstance} from "vue"
import {getPrintTemplate} from "@/api/system/template"
import {Message} from "element-ui";

const _this = getCurrentInstance().proxy


onMounted(()=>{
  getPrintHtml();
})

const html = ref("");

const getPrintHtml = () => {
  let {printId,...data} = _this.$route.query
  if(!printId){
    Message.error("读取模板错误!");
    return false
  }
  getPrintTemplate(printId,data).then(res=>{
    html.value = res.data.html
    nextTick(()=>{
      let style = _document.createElement("style");
      style.type = "text/css"
      style.innerHTML = res.data.css
      let box = _document.body;
      box.append(style)
      nextTick(()=>{
        setTimeout(()=>{
          _window.print();
        },1000)
      })
    })
  })
}
</script>
