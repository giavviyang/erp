<template>
  <div class="pdf">
    <pdf class="prism-player" ref="pdfIframe" :src="pdfSrc" width="100%" height="100%"></pdf>
  </div>
</template>

<script>

import pdf from 'vue-pdf';
export default {
  name: "index",
  components: {pdf},
  data(){
    return{
      pdfSrc:''
    }
  },
}
</script>

<style scoped>

</style>
