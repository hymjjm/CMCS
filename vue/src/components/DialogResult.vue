<template>
  <div >

    <el-form  label-width="180px"  id="selectForm">
      <el-form-item class="backcolor item" label="Gene:">
        {{dialogData[0].gene}}
      </el-form-item>
      <el-form-item class="item" label="Chromosome:">
        {{dialogData[0].chr}}
      </el-form-item>
      <el-form-item class="backcolor item" label="Mutation Number:">
        {{dialogData[0].mutNumber}}
      </el-form-item>
      <el-form-item class="item" label="Range:">
        {{dialogData[0].mutRange}}
      </el-form-item>
      <el-form-item  class="backcolor item" label="Cluster Type:">
        {{dialogData[0].clusterType}}
      </el-form-item>
      <el-form-item  class="backcolor item" label="Annotation:">
        {{dialogData[0].mutDescription}}
      </el-form-item>
      <el-form-item class="item" label="Cancer Type:">
        {{dialogData[0].cancer}}
      </el-form-item>
      <el-form-item class="backcolor item" label="Sample:">
        {{dialogData[0].sample}}
      </el-form-item>
      <el-form-item  class="item" label="ReferenceGenomes:">
        {{dialogData[0].rg}}
      </el-form-item>
      <el-form-item  class="item" label="GeneCard:">
        <el-link type="primary" :underline="false" :href="'https://www.genecards.org/cgi-bin/carddisp.pl?gene=' + dialogData[0].gene " target="_blank">{{ dialogData[0].gene }}</el-link>

      </el-form-item>


    </el-form>
  </div>
</template>

<script>

    export default {
      name: "Result",
      props: {
        dialogData: Array,
      },
      data(){
        return{
          zjgeneSymbol:false,

        }
      },
      watch: {
        'dialogData.gene'(newGeneSymbol) {
          this.zjgeneSymbol = newGeneSymbol !== null && newGeneSymbol !== '' && newGeneSymbol !== "None";
        },
      },
      mounted() {
        console.log("dialogData",this.dialogData[0].gene)
        // 如果 zj 在此时已经有值，也可以进行一次初始检查
        if (this.dialogData[0] && this.dialogData[0].gene !== null && this.dialogData[0].gene !== ''&& this.dialogData[0].gene !== "None") {
          this.zjgeneSymbol = true;
        }
      },
    }
</script>

<style scoped>
.backcolor{
  background-color: rgba(100, 33, 33, 0.07);
  border-radius: 8px;
}

#selectForm >>> .el-form-item__label {
  font-size: 16px !important;
  font-weight: bold !important;
  color: rgba(68, 65, 4, 0.72) !important;
}

</style>

