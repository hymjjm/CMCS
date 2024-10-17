<template>
  <div class="home" style="margin-top: 5px">
    <Cluster2></Cluster2>

    <!--    词云-->
    <WordCloud :WordClouddata="WordClouddata" style="margin-top:30px "></WordCloud>

  </div>

</template>

<script>

import Cluster2 from "../components/utils/Cluster2";
import WordCloud from "../components/WordCloud";
 import {serverIP} from "../../public/config";

export default {
  name: 'App',
  components: {Cluster2,WordCloud},
  data() {
    return {
       serverIP:serverIP,
      //  轮播图
    //  词云
      WordClouddata:[],

    };
  },
  methods:{
    // 轮播图
    //词云用
    getAll(){
      this.$http.get('http://' + serverIP + ':9012/jjm-cluster-gene/findAll').then(res => {
        // console.log("All:",res);
        this.WordClouddata = res.data;
        // console.log(this.WordClouddata)
      })
    },

  },
  mounted() {
    this.getAll();
  }


}
</script>

<style scoped>
/*轮播图*/
.custom-carousel .el-carousel__item {
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-carousel .carousel-item {
  position: relative;
  width: 100%;
  height: 100%;
}

.custom-carousel .carousel-image {
  /*width: 100%;*/
  height: 100%;
  cursor: pointer;
  /*object-fit: contain; !* 修改 object-fit 的值，以确保图片完整显示 *!*/
}

.custom-carousel .carousel-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(146, 164, 146, 0.35); /* 修改颜色和透明度 */
  pointer-events: none; /* 保证点击事件透传到图片 */
}

.custom-carousel .no-mask .carousel-mask {
  display: none; /* 隐藏中间显示的图片的遮罩 */
}


.el-carousel__item:nth-child(2n) {
  background-color: rgb(251, 253, 253);
}

.el-carousel__item:nth-child(2n+1) {
  background-color: rgba(247, 248, 250);
}
.el-carousel__item h3 {
  color: #f1f3f5;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

</style>
