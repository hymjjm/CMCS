<template>
  <div class="home" style="margin-top: 50px;background-color: rgb(255,254,254);">

    <div style="display: flex;justify-content: center;align-items: center;padding: 10px;height: 600px;
    border-radius: 9px;padding-top: 8px;border: 1px solid #ccc;margin-bottom: 70px;"
    >
      <div style="width: 50%;display: flex;justify-content: center;align-items: center" >
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/lbt/7-kegg-new2.png"
                    @click.native="openModal('http://tmliang.cn/pic/jjm/mutCluater/lbt/7-kegg-new2.png')"
                    style="color: #FFFFFF ;height: 600px;width: 600px;" lazy></el-image>
      </div>
      <div style="width: 2px; height: 500px;background-color: rgba(0,0,0,0.49);"/>

      <div style="width: 50%;display: flex;justify-content: center;align-items: center" >
        <el-image src="http://tmliang.cn/pic/jjm/mutCluater/3-chr.png"
                  @click.native="openModal('http://tmliang.cn/pic/jjm/mutCluater/3-chr.png')"
                  style="color: #FFFFFF ;height: 500px;width: 500px;" lazy></el-image>
      </div>


    </div>

    <!--    走马灯图片展示-->
    <div style="display: flex;justify-content: center;margin-bottom: 20px;margin-top: 90px">
      <el-carousel :autoplay=false :arrow="'always'" type="card" height="588px" style="width: 94%;margin-top: 20px" class="custom-carousel" @change="handleCarouselChange">
        <el-carousel-item v-for="(item, index) in lbts" :key="index">
          <div :class="['carousel-item', {'no-mask': index === currentCarouselIndex}]" style="display: flex;justify-content: center">
            <el-image
              :src="item"
              alt="Image"
              class="carousel-image"
              @click.native="openModal(item)"
            ></el-image>
            <!--加遮罩效果-->
            <div class="carousel-mask"></div>
          </div>
        </el-carousel-item>

        <el-dialog :visible.sync="modalVisible" width="90%">
          <el-image :src="selectedImage" alt="Large Image" style="width: 100%; height: auto;" fit="contain" />
        </el-dialog>
      </el-carousel>
    </div>

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
      lbts:[
        "http://tmliang.cn/pic/jjm/mutCluater/lbt/5-deg.png",
        "http://tmliang.cn/pic/jjm/mutCluater/lbt/1-gsvapdf.png",
        "http://tmliang.cn/pic/jjm/mutCluater/lbt/2-correlation.png",
        "http://tmliang.cn/pic/jjm/mutCluater/lbt/4-expression.png",
        "http://tmliang.cn/pic/jjm/mutCluater/lbt/6-drug.png",
      ],
      modalVisible: false,
      selectedImage: '',
      currentCarouselIndex: 0,

    };
  },
  methods:{
    // 轮播图
    handleCarouselChange(index) {
      this.currentCarouselIndex = index;
    },
    openModal(image) {
      this.selectedImage = image;
      this.modalVisible = true;
      // console.log('Image clicked:', image);
      // console.log('Modal visible:', this.modalVisible);
    },


  },


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
