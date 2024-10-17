<template>
  <div style="text-align: center;height: auto">
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
        <!--all gene-->
<!--        <el-form-item label="Gene" v-show="allGene">-->
<!--          <el-input v-model="gene" placeholder="please enter gene symbol"></el-input>-->
<!--        </el-form-item>-->
        <!--longivity gene-->
        <el-form-item label="Gene:" >
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
        <el-button class="buttonStyle2" @click="getDiffPic">GO</el-button>
      </el-form>
    </div>

    <el-divider></el-divider>
    <div v-loading="loading" v-show="show">
      <!--mRNA表达图片-->
      <el-image
        style=" height: 600px; width: 1250px"
        :src="'data:image/png;base64,' + img64"
        fit="contain">
      </el-image>
      <br>
      <el-button type="primary" round @click="downloadDiffImg">Download</el-button>

      <el-divider></el-divider>

      <!--mRNA差异表达数据-->
      <el-table
        :stripe="true"
        :data="diffList"
        height="500"
        border
        style="width: 100%;">
        <el-table-column
          prop="gene"
          label="Gene">
        </el-table-column>
        <el-table-column
          prop="cancerType"
          label="Cancer Type">
        </el-table-column>
        <el-table-column
          prop="logFC"
          label="logFC">
        </el-table-column>
        <el-table-column
          prop="aveExpr"
          label="AveExpr">
        </el-table-column>
        <el-table-column
          prop="adjP"
          label="Adj.P">
        </el-table-column>
      </el-table>
      <br>
      <el-button type="primary" round @click="downloadDiffData">Download</el-button>
      <br>
    </div>
      <h3  v-if="!show" style="color: rgba(51,51,51,0.24);height: 200px;display: flex;align-items: center;justify-content: center;flex-direction: column" >
        <svg class="empty-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 66">
          <path d="M2 22v28l30 16V38L2 22z" fill="#e0e0e0"/>
          <path d="M32 2l30 16-30 16L2 18 32 2z" fill="#f5f5f5"/>
          <path d="M62 22v28l-30 16V38l30-16z" fill="#e0e0e0"/>
          <path d="M32 38v28l-30-16V22L32 38z" fill="#d0d0d0"/>
          <path d="M62 22L32 38V10l30-8v20z" fill="#d0d0d0"/>
          <path d="M2 22L32 38V10L2 18v4z" fill="#e0e0e0"/>
        </svg>
        <p class="empty-description">No Data Available</p>
        </h3>
  </div>
</template>

<script>

  export default {
    name: "DiffR",
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
        img64: "",
        loading: false,
        diffList: [],
        allGene: false,
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
      // 获取基因表达水平图
      getDiffPic() {
        console.log('Clicked GO, current show:', this.show);  // 调试输出
        this.show = true;
        this.loading = true;
        this.img64 = "";
        this.$http.get(this.api.rURL + "/diffpic/" + this.gene).then(res => {
          this.img64 = res.data;
          this.$http.get(this.api.dzxURL + "/diff/" + this.gene).then(res => {
            this.diffList = res.data;
          }).catch(err =>{
            this.$message.error('error'); // 显示加载失败的消息
            this.loading = false;
          });
          this.loading = false;
        }).catch(err =>{
          this.$message.error('error'); // 显示加载失败的消息
          this.loading = false;
        });
      },
      downloadDiffImg() {
        let imgHref = this.api.dzxURL + "/diff/down/img/" + this.gene;
        let a = document.createElement('a');
        a.href = imgHref;
        a.click();
      },
      downloadDiffData() {
        let dataHref = this.api.dzxURL + "/diff/down/data/" + this.gene;
        let a = document.createElement('a');
        a.href = dataHref;
        a.click();
      },
    },
    mounted() {
      if (this.gene) {
        setTimeout(() => {
          this.getDiffPic();
        }, 100); // 延迟0.1秒以确保DOM加载完成
      }
    },
    // mounted() {
    //   this.$http.get('http://'+serverIP+':9009/wh-allpathway-genelist/selectGeneListObj').then(res => {
    //     this.allGenes = res.data;
    //     // console.log("this.dropdData:",this.allGenes);
    //   })
    // }
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
