<template>
  <div style="margin: auto; text-align: center">
    <!--搜索表单-->
    <div style="margin: 20px;">
      <el-form :inline="true" class="demo-form-inline">
<!--        <el-form-item label="All Gene">-->
<!--          <el-switch-->
<!--            v-model="allGene"-->
<!--            active-color="#13ce66"-->
<!--            inactive-color="#ff4949">-->
<!--          </el-switch>-->
<!--        </el-form-item>-->
        <el-form-item label="Cancer">
          <el-select v-model="cancer" filterable placeholder="Cancer">
            <el-option
              v-for="cancer in cancers"
              :key="cancer"
              :label="cancer"
              :value="cancer">
            </el-option>
          </el-select>
        </el-form-item>
<!--        &lt;!&ndash;all gene&ndash;&gt;-->
<!--        <el-form-item label="Gene" v-show="allGene">-->
<!--          <el-input v-model="gene" placeholder="please enter gene symbol"></el-input>-->
<!--        </el-form-item>-->
        <!--longivity gene-->
        <el-form-item label="Gene" v-show="!allGene">
          <el-select
            v-model="gene"
            filterable
            allow-create
            default-first-option
            remote
            placeholder="eg:TP53(All in capital letters)"
            :remote-method="remoteMethod"
            @blur="productSelect"
            :loading="loading">
            <!-- remote-method封装好的钩子函数。当用户在输入框中输入内容的时候，会触发这个函数的执行，
            把输入框对应的值作为参数带给回调函数，loading的意思就是远程搜索的时候等待的时间，即：加载中-->
            <el-option
              v-for="option in options"
              :label="option"
              :value="option"
              :key="option">
            </el-option>
          </el-select>
        </el-form-item>
        <button class="buttonStyle2" @click="getPic">GO</button>
      </el-form>
    </div>
    <!--结果展示模块-->
    <el-divider></el-divider>
    <el-empty description="Wait For Your Click" v-show="!show" style="height: 600px"></el-empty>
    <div v-loading="loading" v-show="show">
      <!--Surv-->
      <el-image
        style=" height: 800px;"
        :src="'data:image/png;base64,' + img64"
        fit="contain">
      </el-image>
      <br>
      <el-button class="buttonStyle2" round @click="downloadImg">Download</el-button>
    </div>
  </div>
</template>

<script>
  import {serverIP} from "../../../../public/config";

  export default {
    name: "GSEA",
    props:{
      allGenes:Array,
      gene:String,
    },
    data() {
      return {
        show: false,
        // allGenes: [],
        options: [],
        // gene and cancer
        // gene: this.gene,
        cancer: 'BRCA',
        img64: "",
        loading: false,
        cancers: [],
        allGene: false
      }
    },
    methods: {
      productSelect(e) {
        let value = e.target.value; // 输入框值
        if(value) { // 你输入才有这个值 不为空，如果你下拉框选择的话 这个值为空
          this.gene = value
        }
      },
      // 模糊搜索
      remoteMethod(query) {
        // 如果用户输入内容了，就发请求拿数据，远程搜索模糊查询
        if (query !== "") {
          this.options = [...new Set(this.allGenes.filter((item) => {
            // 大于-1说明只要有就行，不论是不是开头也好，中间也好，或者结尾也好
            return item.toLowerCase().indexOf(query.toLowerCase()) > -1
          }))]
        } else {
          this.options = [];
        }
      },
      getPic() {
        this.show = true;
        this.loading = true;
        this.img64 = "";
        this.$http.get(this.api.rURL + "/gsea/" + this.gene + "/" + this.cancer).then(res => {
          this.img64 = res.data;
          this.loading = false;
        }).catch(err =>{
          this.$message.error('error'); // 显示加载失败的消息
          this.loading = false;
        });
      },
      downloadImg() {
        let imgHref = this.api.dzxURL + "/down/gsea/" + this.gene + "/" + this.cancer;
        let a = document.createElement('a');
        a.href = imgHref;
        a.click();
      },
    },
    mounted() {
      // // 获取长寿基因列表
      // this.$http.get(`http://`+`47.97.192.100:9090/rdk` + '/anagene/all').then(res => {
      //   this.allGenes = res.data;
      // });
      //   this.$http.get('http://'+serverIP+':9009/wh-allpathway-genelist/selectGeneListObj').then(res => {
      //     this.allGenes = res.data;
      //     // console.log("this.dropdData:",this.allGenes);
      //   })
      this.cancers = ["BLCA", "BRCA", "CHOL", "COAD", "ESCA",
        "HNSC", "KICH", "KIRC", "KIRP", "LIHC",
        "LUAD", "LUSC", "PRAD", "READ", "STAD",
        "THCA", "UCEC"];
      if (this.gene) {
        setTimeout(() => {
          this.getPic();
        }, 100); // 延迟0.1秒以确保DOM加载完成
      }
    }
  }
</script>

<style scoped>
.buttonStyle2{
  min-width: 16px; /* 设置按钮的最小宽度为100px */
  height:40px;

  overflow: hidden;
  background-color: rgba(82, 176, 4, 0.94);
  color: #ffffff;
  border: none;
  padding: 10px 20px; /* 调整内边距 */
  font-weight: bold;
  border-radius: 2px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  font-size: 18px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  outline: none;
}
.buttonStyle2:hover{
  background-color: rgba(8, 22, 115, 0.51);
}
</style>
