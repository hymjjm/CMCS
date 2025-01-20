<template>
  <div>
    <!--搜索表单-->
    <div style="margin: 20px; text-align: center">
      <span style="font-size: 16px;color: darkgrey">Gene:&nbsp&nbsp</span>
      <el-select
        v-model="gene"
        filterable
        allow-create
        default-first-option
        remote
        placeholder="eg:TP53(All in capital letters)"
        :remote-method="remoteMethod"
        @change="getCnvData"
        @blur="productSelect"
      >
        <!-- remote-method封装好的钩子函数。当用户在输入框中输入内容的时候，会触发这个函数的执行，
        把输入框对应的值作为参数带给回调函数，loading的意思就是远程搜索的时候等待的时间，即：加载中-->
        <el-option
          v-for="option in options"
          :label="option"
          :value="option"
          :key="option">
        </el-option>
      </el-select>
      <button  class="buttonStyle2" @click="getCnvData">GO</button>
    </div>
    <el-divider></el-divider>
    <div style="width: auto;height: 600px" id="cnv" class="chart" v-loading="loading"></div>
  </div>
</template>

<script>
  import {serverIP} from "../../../../public/config";

  export default {
    name: "Methy",
    props:{
      allGenes:Array,
      gene:String,
    },
    data() {
      return {
        // allGenes: [],
        options: [],
        // gene and cancer
        // gene: this.gene,
        cnvList: [],
        loading: false
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
      // 拷贝数变异
      getCnvData() {
        this.loading = true;
        this.$http.get(this.api.dzxURL + '/cnv/' + this.gene)
          .then(res=>{
            this.cnvList = res.data;
            this.cnvInit();
            this.loading = false;
          })
      },
      // cnv初始化
      cnvInit() {
        const container = document.getElementById("cnv");
        if (container.clientWidth === 0 || container.clientHeight === 0) {
          // 如果元素尺寸尚未设置，稍后重试初始化
          setTimeout(this.cnvInit, 100);
          return;
        }
        let data = [];
        let cancers = [];
        for(let i = 0; i < this.cnvList.length; i++) {
          let cnv = this.cnvList[i];
          data.push(cnv.dataList);
          cancers.push(cnv.cancer);
        }
        this.$echarts.init(container).setOption( {
            dataset: [
              {
                source: data
              },
              {
                transform: {
                  type: 'boxplot',
                  config: { itemNameFormatter: function (param) {
                      return cancers[param.value];
                    }
                  }
                }
              },
              {
                fromDatasetIndex: 1,
                fromTransformResult: 1
              }
            ],
            tooltip: {
              trigger: 'item',
              axisPointer: {
                type: 'shadow'
              }
            },
            grid: {
              left: '10%',
              right: '10%',
              bottom: '15%'
            },
            title: [
              {
                text: 'CNV',
                left: 'center'
              },
              {
                text: 'Cancer Type',
                left: 'center',
                top: '95%'
              }
            ],
            xAxis: {
              type: 'category',
              position: 'bottom',
              show: true,
              axisLabel: {
                interval: 0,
                rotate: 40
              },
              nameTextStyle: {
                color: '#000',
                fontSize: 16,
              },
            },
            yAxis: {
              type: 'value',
              nameRotate: 90,
              nameGap: 30,
              nameLocation: "middle",
              name: 'Copy Number Variance',
              nameTextStyle: {
                color: '#000',
                fontSize: 16,
              },
            },
            series: [
              {
                name: 'boxplot',
                type: 'boxplot',
                datasetIndex: 1,
                with: '50%',
              },
              {
                name: 'outlier',
                type: 'scatter',
                datasetIndex: 2
              }
            ]
          }
        )
      },
      handleResize() {
        this.cnvInit();
      }
    },
    mounted() {
      if (this.gene) {
        setTimeout(() => {
          this.getCnvData();
        }, 100); // 延迟0.1秒以确保DOM加载完成
      }
      window.addEventListener('resize', this.handleResize);
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.handleResize);
    },
    // mounted() {
    //   this.$http.get('http://'+serverIP+':9009/wh-allpathway-genelist/selectGeneListObj').then(res => {
    //     this.allGenes = res.data;
    //     // console.log("this.dropdData:",this.allGenes);
    //   })
    //
    // }
  }
</script>

<style scoped>

.buttonStyle2{
  min-width: 16px; /* 设置按钮的最小宽度为100px */
  height:41px;

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
