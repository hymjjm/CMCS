<template>
  <div style="margin: 0 auto;max-width: 1040px;" >
    <el-table
      stripe
      id="left-table"
      :data="tableData"
      element-loading-text="Loading desperately, please wait patiently"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      style="border-radius: 10px"
      :header-cell-style="{ backgroundColor: '#F0F0F0' }"
    >
      <el-table-column
        prop="geneSymbol"
        label="GENE_SYMBOL"
        width="140">
      </el-table-column>
      <el-table-column
        prop="chromosome"
        label="Chromosome"
        width="140">
      </el-table-column>
      <el-table-column
        prop="genomeStart"
        label="Position"
        width="160">
      </el-table-column>
      <el-table-column
        prop="wtAllele"
        label="Reference"
        width="120">
      </el-table-column>
      <el-table-column
        prop="mutAllele"
        label="Alternate"
        width="120">
      </el-table-column>
      <el-table-column
        prop="strand"
        label="Strand"
        width="80">
      </el-table-column>
      <el-table-column
        prop="Mutation Annotation"
        label=" mutDescription"
        width="140">
      </el-table-column>
      <el-table-column
        prop="tumorSample"
        label="Sample"
        width="130">
      </el-table-column>
      <el-table-column
        prop="cancerType"
        label="Cancer_Type"
        width="110">
      </el-table-column>
      <el-table-column
        prop="referenceGenomes"
        label="Reference_Genomes"
        width="120">
      </el-table-column>
      <el-table-column
        prop="context"
        label="Context"
        width="90">
      </el-table-column>

      <el-table-column
        prop="nickName"
        label="User_Nickname"
        width="170">
      </el-table-column>
<!--      <el-table-column-->
<!--        label="Browser"-->
<!--        width="120">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tooltip placement="top" effect="light" >-->
<!--            <div slot="content" v-show="showTooltip"><a :href="GeneCard"  target="_blank">GeneCard</a><br/><a :href="UniProtSearch" target="_blank">UniProt search</a><br/><a :href="GeneMANIA" target="_blank">GeneMANIA</a></div>-->
<!--            <button  class="buttonStyle3" icon="el-icon-search" @click="Browser(scope.row)"><i class="fas fa-search"></i></button>-->
<!--          </el-tooltip>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

  </div>
</template>

<script>
export default {
  name: "Table",
  props:{
    tableData: Array,

  },
  data(){
    return{
      //Browser
      // showTooltip:false,
      // GeneCard:"",
      // UniProtSearch:"",
      // GeneMANIA:"",
    }
  },
  methods:{
    //Browser
    // Browser(row){
    //
    //   this.GeneCard="https://www.genecards.org/cgi-bin/carddisp.pl?gene="+row.gene;
    //   this.UniProtSearch="https://www.uniprot.org/uniprotkb/?query="+row.gene;
    //   this.GeneMANIA="http://genemania.org/search/homo-sapiens/"+row.gene;
    //   this.showTooltip=true;
    //   setTimeout(() => {
    //     this.showTooltip = false
    //   }, 5000) // 鼠标点击后5秒后隐藏提示文本
    // },
  }
}
</script>

<style scoped>

</style>
