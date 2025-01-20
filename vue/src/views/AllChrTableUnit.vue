<template>
  <div class="container" >
    <el-tabs type="border-card"  >
      <el-tab-pane label="Chromosome Data Summary" class="tab-pane">
        <AllChrTable   style="margin-top: 10px" :showtable3="showtable3" :eps="eps"  :cancerType="cancerType" :sample="sample" :referenceGenomes="referenceGenomes" :nickname="nickname"></AllChrTable>
      </el-tab-pane>
      <el-tab-pane label="Chromosome Data Summary(Detail)" class="tab-pane">
        <AllChrTableDetail   style="margin-top: 10px" :showtable3="showtable3" :eps="eps"  :cancerType="cancerType" :sample="sample" :referenceGenomes="referenceGenomes" :nickname="nickname"></AllChrTableDetail>
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
import AllChrTableDetail from "../components/utils/AllChrTableDetail";
import AllChrTable from "../components/utils/AllChrTable";

export default {
  name: "Cancer",
  props: {
    eps: Number,
    showtable3: Boolean,
    cancerType: String,
    sample: String,
    referenceGenomes: String,
    nickname:String
  },
  components: {
    AllChrTable,
    AllChrTableDetail,
  },
}
</script>

<style scoped>
/*!* 在全局样式表中添加样式 *!*/
/*/deep/ .el-tabs__header {*/
/*  border-radius: 2px!important;*/
/*  border-width: 2px;*/
/*  padding-top: 8px;*/
/*  padding-bottom: 3px;*/

/*}*/

/*!* 使用深度选择器来修改标签文字样式 *!*/
/deep/ .el-tabs__item {
  /*border-radius: 2px!important;*/
  font-weight: inherit;
  font-size: 18px;
}
/deep/ .el-tabs__item:hover {
  color: rgba(73, 183, 9, 0.95) !important; /* 修改标签背景颜色 */
  font-size: 18px;
}
/*!* 使用深度选择器来修改标签被选择时的颜色 *!*/
/deep/ .el-tabs__item.is-active {
  font-size: 20px;
  font-weight: inherit;
  color: rgba(34, 96, 3, 0.87) !important; /* 修改标签背景颜色 */
  background-color: rgba(17, 17, 17, 0.06) !important; /* 修改标签背景颜色 */
  /*border-radius: 5px!important;*/
}


</style>

