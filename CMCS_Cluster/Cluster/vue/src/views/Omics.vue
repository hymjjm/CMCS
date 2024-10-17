<template>
  <div class="container" style="right: 0;margin-top: 10px">
    <OmicsAnalysis></OmicsAnalysis>
    <OmicsShowPic></OmicsShowPic>

  </div>
</template>

<script>
import OmicsAnalysis from "./OmicsAnalysis";
import OmicsShowPic from "./OmicsShowPic";

export default {
  name: "Cancer",
  components: {
    OmicsAnalysis,
    OmicsShowPic,

  },
  data(){
    return{
    }
  },

}
</script>

<style scoped>

</style>

