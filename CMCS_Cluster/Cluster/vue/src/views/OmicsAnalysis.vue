<template>
  <div class="container" style="right: 0;">
    <el-tabs type="border-card" class="tab-container">
      <el-tab-pane label="mRNA Expression" class="tab-pane">
        <DiffR :allGenes="allGenes" :gene="gene"></DiffR>
      </el-tab-pane>
      <el-tab-pane label="Methylation" class="tab-pane">
        <methy ref="methy" :allGenes="allGenes" :gene="gene"></methy>
      </el-tab-pane>
      <el-tab-pane label="Mutation" class="tab-pane">
        <Mut :allGenes="allGenes" :gene="gene"></Mut>
      </el-tab-pane>
            <el-tab-pane label="Rainfall Plots(Mutation)" class="tab-pane">
              <ClusterUpLoadFile ></ClusterUpLoadFile>
            </el-tab-pane>
      <el-tab-pane label="Copy Number Variation" class="tab-pane">
        <Cnv :allGenes="allGenes" :gene="gene"></Cnv>
      </el-tab-pane>
      <el-tab-pane label="Survival" class="tab-pane">
        <Surv :allGenes="allGenes" :gene="gene"></Surv>
      </el-tab-pane>
      <el-tab-pane label="ssGSEA" class="tab-pane">
        <GSEA :allGenes="allGenes" :gene="gene"></GSEA>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import DiffR from "../components/MultipleOmics/dzx/DiffR";
import Surv from "../components/MultipleOmics/dzx/Surv";
import Methy from "../components/MultipleOmics/dzx/Methy";
import Mut from "../components/MultipleOmics/dzx/Mut";
import Cnv from "../components/MultipleOmics/dzx/Cnv";
import GSEA from "../components/MultipleOmics/dzx/GSEA";
import ClusterUpLoadFile from "../components/utils/ClusterUpLoadFile";

export default {
  name: "",
  components: {
    ClusterUpLoadFile,
    DiffR,
    Surv,
    Methy,
    Mut,
    Cnv,
    GSEA,

  },
  created() {
    // 检查路由参数是否存在，如果不存在，则设置默认值
    if (this.$route.params.key) {
      this.gene = this.$route.params.key;
    } else {
      this.gene = ''; // 设置默认值
    }
  },
  data(){
    return{
      allGenes:[],
      gene:"",
    }
  },

}
</script>

<style scoped>
/* 在全局样式表中添加样式 */
/deep/ .el-tabs__header {
  border-radius: 2px!important;
  border-width: 2px;
  padding-top: 8px;
  padding-bottom: 3px;

}

/* 使用深度选择器来修改标签文字样式 */
/deep/ .el-tabs__item {
  border-radius: 2px!important;
  font-size: 20px;
  font-weight: lighter;
}
/deep/ .el-tabs__item:hover {
  color: rgba(73, 183, 9, 0.95) !important; /* 修改标签背景颜色 */
  font-size: 20px;
}
/* 使用深度选择器来修改标签被选择时的颜色 */
/deep/ .el-tabs__item.is-active {
  font-size: 24px;
  color: rgba(34, 96, 3, 0.87) !important; /* 修改标签背景颜色 */
  background-color: rgba(17, 17, 17, 0.07) !important; /* 修改标签背景颜色 */
  border-radius: 5px!important;
}


</style>

