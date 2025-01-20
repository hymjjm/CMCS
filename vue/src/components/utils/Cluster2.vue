<template>
<div >
  <div style="display: flex; justify-content: center; align-items: center; border-radius: 3px;">

    <div style="margin-right: 10px;padding-bottom: 5px;padding-left:10px;background-color: rgba(10,10,10,0.02);border-radius: 5px;  ">

      <div style="display: flex; justify-content: center; margin-top: 0px;">
        <div style="margin-right: 0px;">
          <label>XMin：</label>
          <input type="number" v-model="xMin" style="width: 90px"  class="input-field"/>
        </div>
        <div>
          <label>XMax：</label>
          <input type="number" v-model="xMax" style="width: 90px"  class="input-field"/>
        </div>
      </div>
      <div style="display: flex; justify-content: center; margin-top: 2px;">
        <div style="margin-right: 0px;">
          <label>YMin：</label>
          <input type="number" v-model="yMin" style="width: 90px"  class="input-field"/>
        </div>
        <div>
          <label>YMax：</label>
          <input type="number" v-model="yMax" style="width: 90px "  class="input-field"/>
        </div>
      </div>

      <div style="display: flex;justify-content: center;align-items: center">
        <button @click="XYOperate" style=" margin-top: 11px;margin-right:5px;margin-left:-20px;width: 80%;height: 50%;font-size: 18px;border-radius: 3px"> <i class="fas fa-tools"></i></button>
        <el-popover
          placement="bottom"
          width="50"
          trigger="hover"
          content="Not a required option">
          <div slot="reference"><i class="fas fa-info-circle" style="color: #750404"></i></div>
        </el-popover>
      </div>

    </div>

      <div style="display: flex; margin:1px;padding: 2px; align-items: center; justify-content: center;border-radius: 10px">
<!--      中间参数输入区-->
       <div >
         <div style="display: flex; padding-top: 15px">
           <span style="color: black;">
             <b>CHROMOSOME：</b>
           </span>
           <el-select v-model="value" placeholder="请选择" style="width:120px; margin-right: 10px;">
             <el-option v-for="item in chrs" :key="item.value" :value="item.value"></el-option>
           </el-select>
           <span style="color: black;">
             <b>CANCER：</b>
           </span>
           <el-autocomplete class="inline-input" v-model="cancerType" :fetch-suggestions="querySearchCancer" placeholder="请输入内容" :trigger-on-focus="false" style="margin-right: 10px; width: 150px"></el-autocomplete>
           <span style="color: black;">
             <b>SAMPLE：</b>
           </span>
           <el-autocomplete class="inline-input" v-model="sample" :fetch-suggestions="querySearch" placeholder="请输入内容" :trigger-on-focus="false" style="width: 150px;margin-right: 30px"></el-autocomplete>

         </div>
         <div style="margin-top: 10px">
         <span style="color: black;">
           <b>ReferenceGenomes：</b>
         </span>
         <el-autocomplete class="inline-input" v-model="referenceGenomes" :fetch-suggestions="querySearchRG" placeholder="请输入内容" :trigger-on-focus="false" style="width: 90px;margin-right: 30px"></el-autocomplete>
           <span style="color: black;">
            <el-tooltip class="item" effect="dark" content="IMD Threshold" placement="top">
              <b style="cursor: pointer;">eps：</b>
            </el-tooltip>
         </span>
           <el-autocomplete class="inline-input" v-model="eps" :fetch-suggestions="querySearchRG" placeholder="请输入内容" :trigger-on-focus="false" style="width: 70px;margin-right: 20px"></el-autocomplete>
           <span style="color: black;">
           <b>User nickname：</b>
         </span>
           <el-input class="inline-input" v-model="nickname"  placeholder="Not required"  style="width: 150px;margin-right:20px"></el-input>
         </div>

<!--         参考格式指引-->
         <div style="width: 770px;margin-top: 3px;margin-bottom:10px;background-color:rgba(28,28,28,0.04);border-radius: 3px;padding: 3px">
           <div slot="reference" style="display: flex;justify-content: center;align-items: center">
             <div slot="tip" style="text-align: center;margin-right: 20px;margin-bottom: 5px" class="el-upload__tip">
               For upload format, please click the "DataBase" button in the top right for reference.</div>
             <i class="fas fa-info-circle" style="color: #750404"></i>
           </div>
         </div>
       </div>


<!--        右侧按钮区-->
        <div style="background-color: rgba(10,10,10,0.09);border-radius: 5px; margin: 3px; padding-top: 5px">
          <div style="display: flex;justify-content: center;align-items: center;height: 49px;border-radius: 5px">
            <button @click="Interface" style="margin-right: 10px; width: 90px;height: 100%;font-size: 24px;border-radius: 3px">
              GO <i class="fas fa-chart-line"></i>
            </button>
            <button @click="downloadCSV" style=" width: 160px;height: 100%;font-size: 24px;border-radius: 3px">
              <i class="fas fa-download"></i> Output File
            </button>
          </div>
          <UpLoadFile style="margin: 5px;" @update-sample-and-cancer-types="updateSampleAndCancerTypes"></UpLoadFile>
        </div>
 </div>


  </div>


<!--单个染色体结果展示-->
  <div style="border: rgba(15,105,9,0.72) solid;border-radius: 12px;padding-left: 2px;padding-right: 2px;padding-bottom: 2px">
    <div style="display: flex; justify-content: center;margin-bottom: 10px; text-align: center;">
      <h2 style="color: #FFFFFF; margin: 0;display: inline-block; padding-left: 10px;padding-right: 10px; border-bottom-left-radius: 15px; border-bottom-right-radius: 15px;background-color: rgba(15,105,9,0.72);">
         Single Chromosome Results Presentation </h2>
    </div>

    <!--  主页背景图-->
    <div style="display:flex;justify-content: center;align-items: center">
      <div v-show="showimg" class="image-container">
        <svg v-show="showimg" class="image-container" id="_图层_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 572.97 580.43">
          <image width="3236" height="3280" transform="scale(.18)" xlink:href="http://tmliang.cn/pic/jjm/mutCluater/终极版组合图V2.png"/>
        </svg>
      <div  class="neon-text">Please select the parameters and click "GO" to analyze</div>
      </div>
      <h1 v-show="showNoCluster" class="neon-text2">No cluster mutation, please select another chromosome or sample</h1>
    </div>

    <!--  信息展示-->
    <div  v-show="showCluster" style="position: relative;background-color: rgba(41,93,21,0.53);color: rgba(0,0,0,0.83);
          display: flex;padding: 2px;height: 170px;padding-left: 2%">
      <div style="display: flex;justify-content: center;align-items: center;margin-top: 130px">
        <h2>Cluster Statistics：</h2>
        <p style="margin: 10px;font-size: 20px">Total clusters: <b>{{ totalClusters }}</b></p>
        <p style="margin: 10px;font-size: 20px">Kataegis clusters: <b>{{ kClusters }}</b></p>
        <p style="margin: 10px;font-size: 20px">Omikli clusters: <b>{{ oClusters }}</b></p>
        <p style="margin: 10px;font-size: 20px">DBS clusters: <b>{{ DBSClusters }}</b></p>
        <p style="margin: 10px;font-size: 20px">MBS clusters: <b>{{ MBSClusters }}</b></p>
      </div>

      <div style="position: absolute;top: 2px;right: 2px;left: 10px">
        <div style="max-height: 180px; max-width: 99%;overflow-x: auto; overflow-y: hidden; white-space: nowrap;">
          <div v-for="(symbol, index) in allHugoSymbols" :key="index" style="display: inline-block; margin-right: 10px;">
            <div style="background-color: rgba(255,255,255,0.8); padding: 10px; border-radius: 5px;">
              <p style="margin: 0;"><strong><span style="color: darkred">{{ symbol.gene }}</span></strong></p>
              <p style="margin: 5px 0 0;"><strong>Number:</strong> {{ symbol.size }}</p>
              <p style="margin: 5px 0 0;"><strong>Range:</strong> <span style="font-size: 10px">{{ symbol.range }}(bp)</span></p>
              <p style="margin: 5px 0 0;"><strong>Context:</strong> {{ symbol.context }}</p>
              <p style="margin: 5px 0 0;"><strong>Cluster:</strong> {{ symbol.cluster }}</p>
              <!-- 其他相关信息 -->
            </div>
          </div>
        </div>
      </div>



      <el-popover
        width="300"
        placement="top-end"
        trigger="hover">
        <div style="width: 300px;font-size: 12px">
          <p >"K i" refers to the i-th group belonging to Kataegis.</p>
          <p >"O i" refers to the i-th group belonging to Omikli.</p>
          <p>The "Number" represents the number of base site mutations contained in this type of mutation cluster</p>
        </div>
        <div slot="reference" style="position: absolute; bottom: 0; right: 0;margin: 10px;color: rgba(255,255,255,0.8)">
          help<i class="fas fa-info-circle" style="color: #fffefe"></i>
        </div>
      </el-popover>



      </div>


      <!--  这个是簇突变的子组件echarts-->
      <div v-show="showCluster" id="cluster" style="width: 100%;margin-left: -1px; height: 900px;border: gray 1px solid;margin-top:10px ;margin-bottom: 25px;border-radius: 5px; background-color: white"></div>

      <!-- 饼图子组件-->
      <pieEcharts v-show="showCluster" style="margin-top: 20px;margin: 4px" :tablename="tablename" :allHugoSymbols="allHugoSymbols" :kClusters="kClusters" :oClusters="oClusters" :DBSClusters="DBSClusters" :MBSClusters="MBSClusters"></pieEcharts>
  </div>

  <!--一次性出现所有染色体数据的表格-->
  <!--一次性出现所有染色体数据的表格-->
  <AllChrTableUnit   style="margin-top: 50px" :showtable3="showtable3" :eps="eps" :cancerType="cancerType" :sample="sample" :referenceGenomes="referenceGenomes" :nickname="nickname"></AllChrTableUnit>

</div>
</template>

<script>
import * as echarts from 'echarts';
import {serverIP} from "../../../public/config";
import UpLoadFile from "./UpLoadFile";
import {uniqueBy} from "friendly-errors-webpack-plugin/src/utils";
import pieEcharts from "./pieEcharts";
import AllChrTableUnit from "../../views/AllChrTableUnit";


export default {
  name: "Cluster",
  components:{AllChrTableUnit, UpLoadFile,pieEcharts,},
  data(){
    return{
      serverIP:serverIP,
      eps: 1000,
      myChart: null, // 添加 myChart 变量并初始化为 null
      showimg:true,
      showCluster:true,
      showNoCluster:false,
      allHugoSymbols: [], // 用于存储所有的hugo_Symbol
      // 用于存储用户输入的 x 轴最小值和最大值
      xMin: 0,
      xMax: 250000000,
      // 用于存储用户输入的 y 轴最小值和最大值
      yMin: 0,
      yMax:1450,
      outForm:"",
      referenceGenomes:"GRCh37",
      referenceGenomess:[],
      value: 'chr4',
      sample:'Cosmic_GenomeScreensMutant_Tsv_v99_GRCh37_PD4103a',
      samples:[],
      cancerType:'BRCA',
      nickname:'',
      cancerTypes:[],
      chrs:[
        {value: 'chr1'},
        {value: 'chr2'},
        {value: 'chr3'},
        {value: 'chr4'},
        {value: 'chr5'},
        {value: 'chr6'},
        {value: 'chr7'},
        {value: 'chr8'},
        {value: 'chr9'},
        {value: 'chr10'},
        {value: 'chr11'},
        {value: 'chr12'},
        {value: 'chr13'},
        {value: 'chr14'},
        {value: 'chr15'},
        {value: 'chr16'},
        {value: 'chr17'},
        {value: 'chr18'},
        {value: 'chr19'},
        {value: 'chr20'},
        {value: 'chr21'},
        {value: 'chr22'},
        {value: 'chrX'},
        {value: 'chrY'},

      ],

      clusterData:[],
      totalClusters: 0,
      kClusters: 0,
      oClusters: 0,
      DBSClusters: 0,
      MBSClusters: 0,
      MutDescription: 0,

      tablename:this.cancerType+'_'+this.sample+'_'+this.value+'_'+this.referenceGenomes+".csv",
      showtable3:false,

    }
  },

  methods:{


    Interface(){
      this.showtable3 = false;
      this.showimg= false;
      this.$http.get('http://' + serverIP + ':9012/jjm-cluster-upload/MutCluster', {
        params: {
          eps: this.eps,
          Chromosome: this.value,
          CancerType: this.cancerType,
          Sample: this.sample,
          ReferenceGenomes: this.referenceGenomes,
          nickname:this.nickname,
        }
      }).then(res => {
        // 清空 clusterData 数组
        this.clusterData.splice(0, this.clusterData.length);
        // 将新数据赋值给 clusterData
        this.clusterData = res.data;
        console.log("clusterData：",this.clusterData); // 打印出clusterData数组，检查其结构和内容

        // 统计Cluster数量
        this.totalClusters = this.clusterData.length-1;
        this.kClusters = this.clusterData.filter(
          (cluster) => cluster[0] && cluster[0].cluster && cluster[0].cluster.startsWith("K")
        ).length;
        this.oClusters = this.clusterData.filter(
          (cluster) => cluster[0] && cluster[0].cluster && cluster[0].cluster.startsWith("O")
        ).length;
        this.DBSClusters = this.clusterData.filter(
          (cluster) => cluster[0] && cluster[0].cluster && cluster[0].cluster.startsWith("DBS")
        ).length;
        this.MBSClusters = this.clusterData.filter(
          (cluster) => cluster[0] && cluster[0].cluster && cluster[0].cluster.startsWith("MBS")
        ).length;

        // 统计所有出现的hugo_Symbol并计算x范围
        let allSymbols = [];
        this.clusterData.slice(0, -1).forEach(cluster => {
          let xRanges = {}; // 用于记录每个基因在每个cluster中的x范围
          cluster.forEach(point => {
            const key = `${point.hugo_Symbol}_${point.cluster}`;
            if (!xRanges[key]) {
              xRanges[key] = { min: point.x, max: point.x };
            } else {
              if (point.x < xRanges[key].min) xRanges[key].min = point.x;
              if (point.x > xRanges[key].max) xRanges[key].max = point.x;
            }
          });
          cluster.forEach(point => {
            const key = `${point.hugo_Symbol}_${point.cluster}`;
            if (!allSymbols.some(item => item.gene === point.hugo_Symbol && item.cluster === point.cluster)) {
              const range = `${xRanges[key].min}-${xRanges[key].max}`;
              allSymbols.push({ gene: point.hugo_Symbol, cluster: point.cluster, size: point.size, range: range,strand: point.strand, mutDescription: point.mutDescription });
            }
          });
        });
// 使用辅助函数去重
        this.allHugoSymbols = uniqueBy(allSymbols, JSON.stringify);
        // console.log(this.allHugoSymbols)


        // 获取 x 轴的最大值和最小值
          let minX = Infinity;
          let maxX = -Infinity;
          for (const cluster of this.clusterData) {
            for (const point of cluster) {
              if (point.x < minX) minX = point.x;
              if (point.x > maxX) maxX = point.x;
            }
          }
          // 更新 x 轴的最大值和最小值
          this.xMin = minX-1000000;
          this.xMax = maxX+10000;

          this.myChart.clear();
          this.cluster(this.xMin, this.xMax);
          this.showNoCluster = false;
          this.showCluster = true;
      }).catch(err=>{
        this.showNoCluster = true
        this.showCluster = false;
      })
    },
    //手动设置xy轴
    XYOperate(){
      this.cluster(this.xMin, this.xMax);
    },
    cluster(xMin, xMax){
      // this.Interface();

      // var chartDom = document.getElementById('cluster');
      // var myChart = echarts.init(chartDom);
      var option;

      option = {
        title: {
          text: 'Kataegis and Omikli cluster analysis',
          subtext: 'Data from: '+this.cancerType+'_'+this.sample+'_'+this.value+'_'+this.referenceGenomes,
        },
        grid: {
          left: '3%',
          right: '7%',
          bottom: '7%',
          containLabel: true
        },
        tooltip: {
          // trigger: 'axis',
          showDelay: 0,
          formatter: function (params) {
            // 检查 params 的结构
            // console.log(params);
            const data = params.data; // 获取鼠标悬停的数据点的数据
            // 对 y 坐标的数值进行反向转换
            //  const originalY = Math.pow(10, data.value[1]);
            return `Gene: ${data.name}<br>X: ${data.value[0]}<br>Y-front: ${data.value[1]}<br>Y-behind: ${data.value[2]}<br>
                     wtAlle:${data.value[3]}<br>mutAlle:${data.value[4]}<br>strand:${data.value[5]}  `;
          },

          axisPointer: {
            show: true,
            type: 'cross',
            lineStyle: {
              type: 'dashed',
              width: 1
            }
          }
        },
        toolbox: {
          feature: {
            dataZoom: {},
            brush: {
              type: ['rect', 'polygon', 'clear']
            }
          }
        },
        brush: {},
        // legend: {
        //   data: ['Female', 'Male'],
        //   left: 'center',
        //   bottom: 10
        // },
        xAxis: [
          {
            name:'Mutation position(bp)',
            nameLocation: 'middle',
            nameGap: 50,
            type: 'value',
            scale: true,
            axisLabel: {
              formatter: '{value}bp'
            },
            splitLine: {
              show: false
            },
            min: xMin,  // 添加最小值
            max: xMax,   // 添加最大值
          }
        ],
        yAxis: [
          {
            name:'Intermutation distance(bp)',
            nameLocation: 'middle',
            nameRotate: 90,
            nameGap: 70,
            type: 'value',
            scale: true,
            axisLabel: {
              formatter: '{value} bp'
            },
            splitLine: {
              show: false
            },
            min: this.yMin,  // 添加最小值
            max: this.yMax   // 添加最大值

          }
        ],
        series: this.generateSeriesElements()
      };

      option && this.myChart.setOption(option);
    },
    generateSeriesElements() {
      const series = [];
      let totalClusters =0;
       totalClusters = this.clusterData.length;
      // 定义缩放比例因子，用于缩小 x 坐标
      const scaleFactor = 0.1; // 比例因子为 0.1，表示 x 坐标缩小为原来的十分之一

      for (let i = 0; i < totalClusters; i++) {
        let cluster = '';
        if(this.clusterData[i].length === 0) continue;
        cluster = this.clusterData[i];
        const data = [];

        let borderWidth = 0.5;
        if(i === totalClusters -1 ){
          borderWidth = 0;
          for (let j = 0; j < cluster.length; j++) {
            const point = cluster[j];
            // 对 x 坐标进行缩放
            // const scaledX = point.x * scaleFactor;
            data.push({
              value: [point.x,point.y],
              name:point.hugo_Symbol,
              Cluster: '',// 添加 Cluster 列并设置为空字符串
              // 为系列设置颜色
              itemStyle: {
                color: "rgba(108,105,105,0.35)"
              }
            });
          }
        }else{
          for (let j = 0; j < cluster.length; j++) {
            const point = cluster[j];
            // 对 x 坐标进行缩放
            // const scaledX = point.x * scaleFactor;
            data.push({
              value: [point.x,point.y,point.y2,point.wtAlle,point.mutAlle,point.strand,point.mutDescription],
              name:point.hugo_Symbol,
              Cluster: this.clusterData[i][0].cluster, // 添加 Cluster 列并设置为空字符串
              // 为系列设置颜色
              itemStyle: {
                // color : this.randomColor() // 如果是最后一个簇，则使用固定颜色，否则生成随机颜色
              }
            });
          }
        }

        const seriesElement = {
          type: 'scatter',
          data: data,
          markArea: {
            silent: true,
            itemStyle: {
              borderWidth: borderWidth,
              borderType: 'dashed',
              color: 'transparent',
            },
            data: [
              [
                {
                  name: data[0].Cluster,
                  xAxis: 'min',
                  yAxis: 'min'
                },
                {
                  xAxis: 'max',
                  yAxis: 'max'
                }
              ]
            ]
          },
        };

        series.push(seriesElement);
      }

      return series;
    },
    // 生成随机颜色字符串的函数
  randomColor() {
  // 生成随机的 R、G、B 分量
  const r = Math.floor(Math.random() * 256);
  const g = Math.floor(Math.random() * 256);
  const b = Math.floor(Math.random() * 256);

  // 将 RGB 分量格式化为十六进制字符串，并返回颜色字符串
  return '#' + r.toString(16).padStart(2, '0') + g.toString(16).padStart(2, '0') + b.toString(16).padStart(2, '0');
  },
  //  **************************表格下载*************************************************************************************
    downloadCSV() {
      const columnNames = ["Hugo_Symbol", "Chromosome", "Start_Position","GENOMIC_WT_ALLELE","GENOMIC_MUT_ALLELE","STRAND", "Cluster","Context","ReferenceGenomes","mutDescription"]; // 指定列名
      const csvContent = "data:text/csv;charset=utf-8," +
        columnNames.join(",") + "\n" +
        this.clusterData.flatMap(cluster => {
          return cluster.map(point => {
            // 提取每个数据点的所需属性
            const name = point.hugo_Symbol;
            const chromosome = this.value; // Chromosome 列固定为 this.value
            const start_position = point.x;
            const GENOMIC_WT_ALLELE = point.wtAlle;
            const GENOMIC_MUT_ALLELE = point.mutAlle;
            const STRAND = point.strand;
            const clusterName = point.cluster; // 从数据点中获取 Cluster 属性
            const Context = point.context;
            const ReferenceGenomes = this.referenceGenomes; // Chromosome 列固定为 this.value
            const mutDescription = this.mutDescription;

            // 将数据点属性按顺序组成一行
            return `${name},${chromosome},${start_position},${GENOMIC_WT_ALLELE},${GENOMIC_MUT_ALLELE},${STRAND},${clusterName},${Context},${ReferenceGenomes},${mutDescription}`;
          });
        }).join("\n");
      const encodedUri = encodeURI(csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", encodedUri);
      link.setAttribute("download", "table1_"+this.tablename);
      document.body.appendChild(link);
      link.click();
    },

    querySearch(queryString, cb) {
      var samples = this.samples;
      var results = queryString ? samples.filter(this.createFilter(queryString)) : samples;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (samples) => {
        return (samples.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    querySearchCancer(queryString, cb) {
      var cancerTypes = this.cancerTypes;
      var results = queryString ? cancerTypes.filter(this.createFilterCancer(queryString)) : cancerTypes;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilterCancer(queryString) {
      return (cancerTypes) => {
        return (cancerTypes.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    querySearchRG(queryString, cb) {
      var referenceGenomes = this.referenceGenomess;
      var results = queryString ? referenceGenomes.filter(this.createFilterRG(queryString)) : referenceGenomes;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilterRG(queryString) {
      return (referenceGenomes) => {
        return (referenceGenomes.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },

    updateSampleAndCancerTypes() {
      // 在这里更新父组件的数据
      this.$http.get('http://' + serverIP + ':9012/jjm-cluster-upload/Sample').then(res => {
        // 清空原始数据
        this.samples = [];
        // 更新数据
        for (let item of res.data) {
          this.samples.push({'value': item});
        }
      });
      this.$http.get('http://' + serverIP + ':9012/jjm-cluster-upload/cancerType').then(res => {
        // 清空原始数据
        this.cancerTypes = [];
        // 更新数据
        for (let item of res.data) {
          this.cancerTypes.push({'value': item});
        }
      });
      this.$http.get('http://' + serverIP + ':9012/jjm-cluster-upload/ReferenceGenomes').then(res => {
        // 清空原始数据
        this.referenceGenomess = [];
        // 更新数据
        for (let item of res.data) {
          this.referenceGenomess.push({'value': item});
        }
      });
    }

  },

  mounted() {
    this.myChart = echarts.init(document.getElementById('cluster'));
    this.showCluster=false;

    // this.myChart = echarts.init(document.getElementById('cluster')); // 初始化 myChart
    // this.cluster();
    this.updateSampleAndCancerTypes();


  },

}
</script>

<style scoped>
/* 修改 el-select 的宽度 */
.el-select {
  width: 85px !important; /* 根据需要设置宽度 */
}
button{
  min-width: 16px; /* 设置按钮的最小宽度为100px */
  height:18px;
  overflow: hidden;
  background-color: rgba(4, 53, 86, 0.98);
  color: #ffffff;
  border: none;
  padding: 3px 3px; /* 调整内边距 */
  border-radius: 1px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  font-size: 18px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  outline: none;
}

.input-field, .select-field, .autocomplete-field {
  width: 90px;
  height: 15px;
  border: 1px solid #ccc;
  border-radius: 3px;
  padding: 4px;
}

.input-field:focus {
  background-color: #d3e1c6;
}
.image-container {
  width: 100%;
  height: 800px;
  /*border: rgba(169, 169, 169, 0.58) solid;*/
  border-radius: 9px;
  /*background-color: white;*/
  background-image:  linear-gradient(to bottom, rgba(236, 241, 238, 0.8), rgba(128, 129, 128, 0.39));
  /*background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0.2)); !* 背景渐变 *!*/
  background-repeat: no-repeat;
  background-position: center;
  text-align: center; /* 文本居中对齐 */
  position: relative; /* 让文本相对于容器定位 */
}

.neon-text {
  z-index: 1; /* 确保文本位于背景之上 */
  position: absolute; /* 绝对定位 */
  top: 25%; /* 距离顶部15% */
  left: 50%; /* 居中对齐 */
  transform: translateX(-50%); /* 水平居中 */
  font-size: 35px; /* 调整字体大小 */
  color: #fff; /* 设置文字颜色为白色 */
  text-shadow:
    0 0 5px rgba(72, 71, 69, 0.3),
    0 0 10px rgba(72, 71, 69, 0.3),
    0 0 20px rgba(72, 71, 69, 0.3),
    0 0 40px rgba(72, 71, 69, 0.3),
    0 0 80px rgba(72, 71, 69, 0.3),
    0 0 140px rgba(72, 71, 69, 0.3); /* 调整发光效果的颜色和模糊半径 */  animation: flicker 2.5s infinite alternate;
}

.neon-text2 {
  margin-top: 10px;
  z-index: 1; /* 确保文本位于背景之上 */
  font-size: 36px; /* 调整字体大小 */
  color: #fa0a0a; /* 设置文字颜色为白色 */
  text-shadow:
     rgba(72, 71, 69, 0.3); /* 调整发光效果的颜色和模糊半径 */  animation: flicker 2.5s infinite alternate;
}
@keyframes flicker {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0.7;
  }
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px; /* 设置滚动条宽度 */
  height: 8px; /* 设置滚动条高度 */
}

/* 轨道 */
::-webkit-scrollbar-track {
  background: rgba(241, 241, 241, 0.16); /* 设置轨道背景色 */
}

/* 滑块 */
::-webkit-scrollbar-thumb {
  background: #eae3e3; /* 设置滑块背景色 */
  border-radius: 4px; /* 设置滑块圆角 */
}
</style>
