<template>
  <div>
    <div style="display: flex;justify-content: center;align-items: center">
      <div id="pie" style="width: 100%; height: 400px;"></div>
      <div id="piehz" style="width: 100%; height: 400px;"></div>
    </div>
<!--表格-->
    <button @click="downloadCSV" style=" position: absolute; right: 15px;width: 200px;background-color: #ddd;font-size: 20px;border-radius: 3px; color: #333"> <i class="fas fa-download"></i> Output File2</button>
    <div  style="margin-top: 30px">
      <div style=";overflow-x: auto; overflow-y: hidden; white-space: nowrap;">
        <table style="border-collapse: collapse; width: 100%;">
          <thead>
          <tr>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Hugo_Symbol</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>MutNumber</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Range (bp)</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Strand</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Cluster</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Annotation</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>GeneCards</strong></th>
            <!-- 其他相关信息 -->
          </tr>
          </thead>
          <tbody>
          <tr v-for="(symbol, index) in allHugoSymbols" :key="index">
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><strong><span style="color: darkred">{{ symbol.gene }}</span></strong></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.size }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><span >{{ symbol.range }}</span></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><span >{{ symbol.strand }}</span></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.cluster }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.mutDescription }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">      <el-link type="primary" :underline="false" :href="'https://www.genecards.org/cgi-bin/carddisp.pl?gene=' + symbol.gene " target="_blank">{{ symbol.gene }}</el-link>
            </td>

            <!-- 其他相关信息 -->
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "pieEcharts",
  props:{
    kClusters: Number,
    oClusters: Number,
    DBSClusters: Number,
    MBSClusters: Number,
    allHugoSymbols: Array,
    tablename: String,

  },
  watch: {
    allHugoSymbols: {
      handler: function(newVal, oldVal) {
        this.echartsStatic(); // 当 allHugoSymbols 变化时重新生成 echarts 图表
        this.echarts();
      },
      deep: true // 深度监听数组或对象的变化
    }

  },

  mounted() {
    this.echarts();
    this.echartsStatic();
    // console.log("{a}:",{a});
  },
  methods:{
    echartsStatic(){
      var chartDom = document.getElementById('piehz');
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        title: {
          text: 'ClusterType Statistics',
          // subtext: 'Fake Data',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              { value: this.kClusters, name: 'Kataegis clusters' },
              { value: this.oClusters, name: 'Omikli  clusters' },
              { value: this.DBSClusters, name: 'DBS  clusters' },
              { value: this.MBSClusters, name: 'MBS  clusters' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);
    },

    generatePieData() {
      // 根据 allHugoSymbols 生成饼图数据
      // 这里只是一个示例，你需要根据实际数据结构来生成合适的饼图数据
      return this.allHugoSymbols.map(symbol => {
        return {
          value: symbol.size,
          name: symbol.gene,
          tooltip: {
            cluster: symbol.cluster ,// 将 symbol.cluster 的值作为 tooltip 的属性传递
            range: symbol.range ,
            strand: symbol.strand ,
            annotation: symbol.mutDescription ,
          }
        }
      });
    },
    echarts(){
      var chartDom = document.getElementById('pie');
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        title: {
          text: 'Mutation count per cluster',
          // subtext: 'Fake Data',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            return 'Gene name : '+params.name +
              '<br/>mutation count : ' + params.value + ' (' + params.percent + '%)' + '' +
              '<br/> Range : ' + params.data.tooltip.range +'(bp)'+
              '<br/> strand : ' + params.data.tooltip.strand +
              '<br/> ClusterType : ' + params.data.tooltip.cluster+
              '<br/> Annotation : ' + params.data.tooltip.annotation;
          }
        },
        legend: {
          bottom: 10,
          left: 'center',
        },
        series: [
          {
            type: 'pie',
            radius: '65%',
            center: ['50%', '50%'],
            selectedMode: 'single',
            data: this.generatePieData(),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);
    },

  //  **************************************表格下载
    downloadCSV() {
      const columnNames = ["Hugo_Symbol", "MutNumber", "Range","Strand","Cluster"]; // 指定列名
      let csvContent = columnNames.join(",") + "\n";

      // 生成表格内容
      this.allHugoSymbols.forEach(symbol => {
        csvContent += `${symbol.gene},${symbol.size},${symbol.range},${symbol.strand},${symbol.cluster}\n`;
      });

      // 创建并下载 CSV 文件
      const encodedUri = encodeURI("data:text/csv;charset=utf-8," + csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", encodedUri);
      link.setAttribute("download", "table2_"+this.tablename);
      document.body.appendChild(link);
      link.click();
    },

  },

}
</script>

<style scoped>

</style>
