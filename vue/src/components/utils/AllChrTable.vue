<template>
  <div style="border: rgba(128,128,128,0.56) solid;border-radius: 5px">
    <!--表格-->
    <div style="margin: 10px;display: flex;justify-content: center">
      <button class="button1" @click="xhInterface" style="margin-right: 20px;font-size: 24px">GO<i class="fas fa-chart-line"  style="margin-left: 5px;color: #FFFFFF;"></i></button>
      <button class="button1" @click="downloadCSV" style=" right: 8px;width: 200px;background-color: #ddd;font-size: 20px;border-radius: 3px; color: #333"> <i class="fas fa-download"></i> Output File3</button>
    </div>
    <div v-show="showtable3" style="margin-top: 0px">
<!--echarts-->
      <div style="display: flex;justify-content: center;align-items: center">
        <div id="xrcluster" style="width: 700px;height: 700px;"> </div>

      </div>

      <div style="overflow-x: auto; overflow-y: hidden; white-space: nowrap;">
        <table style="border-collapse: collapse; width: 100%;">
          <thead>
          <tr>
            <th @click="sortTable('gene')" style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;">
              <strong>Hugo_Symbol</strong>
              <i :class="sortIconClass('gene')" style="margin-left: 5px;"></i>
            </th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Chr</strong></th>
            <th @click="sortTable('size')" style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;">
              <strong>MutNumber</strong>
              <i :class="sortIconClass('size')" style="margin-left: 5px;"></i>
            </th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Range (bp)</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Strand</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Cluster</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>CancerType</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>RG</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Context</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Annotation</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>GeneCards</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>To Analysis</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Sample</strong></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(symbol, index) in sortedHugoSymbols" :key="index">
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><strong><span style="color: darkred">{{ symbol.gene }}</span></strong></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.chr }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.size }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><span>{{ symbol.range }}</span></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><span>{{ symbol.strand }}</span></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.cluster }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{cancerType}}</td>
            <td style="padding: 10px; border: 1px solid #ddd; ">{{referenceGenomes }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; ">{{symbol.context}}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{symbol.mutDescription}}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">      <el-link type="primary" :underline="false" :href="'https://www.genecards.org/cgi-bin/carddisp.pl?gene=' + symbol.gene " target="_blank">{{ symbol.gene }}</el-link>
            <td style="padding: 10px; border: 1px solid #ddd;display: flex;justify-content: center">
              <button type="text" style="font-size: 22px;cursor: pointer;"  @click="clickToPage( symbol.gene)">»</button>
            </td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{sample}}</td>

          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { serverIP } from "../../../public/config";
import { uniqueBy } from "friendly-errors-webpack-plugin/src/utils";
import * as echarts from "echarts";

export default {
  name: "AllChrTable",
  props: {
    eps: Number,
    showtable3: Boolean,
    cancerType: String,
    sample: String,
    referenceGenomes: String,
    nickname:String,
  },
  data() {
    return {
      serverIP: serverIP,
      allHugoSymbols: [], // 用于存储所有的hugo_Symbol
      clusterData: [],
      tablename: this.cancerType + '_' + this.sample + '_' + this.referenceGenomes + ".csv",
      sortKey: 'size',
      sortAsc: true,
      chrs: [
        { value: 'chr1' },
        { value: 'chr2' },
        { value: 'chr3' },
        { value: 'chr4' },
        { value: 'chr5' },
        { value: 'chr6' },
        { value: 'chr7' },
        { value: 'chr8' },
        { value: 'chr9' },
        { value: 'chr10' },
        { value: 'chr11' },
        { value: 'chr12' },
        { value: 'chr13' },
        { value: 'chr14' },
        { value: 'chr15' },
        { value: 'chr16' },
        { value: 'chr17' },
        { value: 'chr18' },
        { value: 'chr19' },
        { value: 'chr20' },
        { value: 'chr21' },
        { value: 'chr22' },
        { value: 'chrX' },
        { value: 'chrY' },
      ],
      InterNumber:0,
    }
  },
  computed: {
    sortedHugoSymbols() {
      return this.allHugoSymbols.slice().sort((a, b) => {
        const result = (a[this.sortKey] > b[this.sortKey]) ? 1 : (a[this.sortKey] < b[this.sortKey]) ? -1 : 0;
        return this.sortAsc ? result : -result;
      });
    },
  },
  // mounted() {
  //   this.xhInterface();
  // },
  methods: {
    clickToPage(tag) {
      this.$router.push({ name: 'Omics', params: { key: tag} });
    },
    async xhInterface() {
      this.showtable3=true;
      this.allHugoSymbols = [];
      for (const chr of this.chrs) {
        await this.Interface(chr.value);
      }
      this.allHugoSymbols = uniqueBy(this.allHugoSymbols, JSON.stringify);
      this.updateChart();
    },
    async Interface(chr) {
      // console.log("InterNumber:",this.InterNumber++);
      this.showimg = false;
      const response = await this.$http.get('http://' + serverIP + ':9012/jjm-cluster-upload/MutCluster', {
        params: {
          eps: this.eps,
          Chromosome: chr,
          CancerType: this.cancerType,
          Sample: this.sample,
          ReferenceGenomes: this.referenceGenomes,
          nickname:this.nickname,
        }
      });
      this.clusterData = response.data;
      // console.log("clusterData:", this.clusterData);

      let allSymbols = [];
      this.clusterData.slice(0, -1).forEach(cluster => {
        let xRanges = {};
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
            allSymbols.push({
              gene: point.hugo_Symbol,
              cluster: point.cluster,
              size: point.size,
              range: range,
              strand: point.strand,
              mutDescription: point.mutDescription,
              context: point.context,
              chr: chr });
          }
        });
      });
      this.allHugoSymbols.push(...allSymbols);
    },


    sortTable(key) {
      if (this.sortKey === key) {
        this.sortAsc = !this.sortAsc;
      } else {
        this.sortKey = key;
        this.sortAsc = true;
      }
    },
    //动态改变图标样式
    sortIconClass(key) {
      if (this.sortKey !== key) return 'fas fa-sort';
      return this.sortAsc ? 'fas fa-sort-up' : 'fas fa-sort-down';
    },


    updateChart() {
      var myChart = echarts.init(document.getElementById('xrcluster'));
      const data = this.transformData(this.allHugoSymbols);
      const option = {
        title: {
          text: 'Chromosome Data Summary',
          // subtext: '动态数据',
          textStyle: {
            fontSize: 14,
            align: 'center'
          },
          subtextStyle: {
            align: 'center'
          }
        },
        series: {
          type: 'sunburst',
          data: data,
          radius: [0, '95%'],
          sort: undefined,
          emphasis: {
            focus: 'ancestor'
          },
          levels: [
            {},
            {
              r0: '15%',
              r: '35%',
              itemStyle: {
                borderWidth: 2
              },
              label: {
                rotate: 'tangential'
              }
            },
            {
              r0: '35%',
              r: '70%',
              label: {
                align: 'right'
              }
            },
            {
              r0: '70%',
              r: '72%',
              label: {
                position: 'outside',
                padding: 3,
                silent: false
              },
              itemStyle: {
                borderWidth: 3
              }
            }
          ]
        }
      };
      option && myChart.setOption(option);
    },
    transformData(data) {
      const chrMap = {};
      const clusterMap = {};

      // 聚合数据到chr和cluster层级
      data.forEach(item => {
        if (!chrMap[item.chr]) {
          chrMap[item.chr] = { value: 0, children: {} };
        }
        chrMap[item.chr].value += item.size;

        const clusterKey = item.cluster.charAt(0); // 按首字符分类cluster
        if (!chrMap[item.chr].children[clusterKey]) {
          chrMap[item.chr].children[clusterKey] = { value: 0, children: [] };
        }
        chrMap[item.chr].children[clusterKey].value += item.size;
        chrMap[item.chr].children[clusterKey].children.push({
          name: item.gene,
          value: item.size
        });
      });

      // 转换数据格式以适应ECharts的sunburst图表
      return Object.keys(chrMap).map(chr => ({
        name: chr,
        value: chrMap[chr].value,
        children: Object.keys(chrMap[chr].children).map(clusterKey => ({
          name: clusterKey,
          value: chrMap[chr].children[clusterKey].value,
          children: chrMap[chr].children[clusterKey].children
        }))
      }));
    },


    downloadCSV() {
      const columnNames = ["Hugo_Symbol", "Chromosome", "MutNumber", "Range","Strand", "Cluster","CancerType","Sample","referenceGenomes","context","mutDescription"];
      let csvContent = columnNames.join(",") + "\n";

      this.allHugoSymbols.forEach(symbol => {
        csvContent += `${symbol.gene},${symbol.chr},${symbol.size},${symbol.range},${symbol.strand},${symbol.cluster},${this.cancerType},${this.sample},${this.referenceGenomes},${symbol.context},${this.mutDescription}\n`;
      });

      const encodedUri = encodeURI("data:text/csv;charset=utf-8," + csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", encodedUri);
      link.setAttribute("download", "table3_" + this.tablename);
      document.body.appendChild(link);
      link.click();
    },
  },
}
</script>

<style scoped>
.button1{
  min-width: 16px; /* 设置按钮的最小宽度为100px */
  height:35px;
  overflow: hidden;
  background-color: rgba(4, 86, 15, 0.98);
  color: #ffffff;
  border: none;
  padding: 3px 8px; /* 调整内边距 */
  border-radius: 3px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  font-size: 18px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  outline: none;
}
button{
  background-color: rgba(4, 86, 15, 0.84);
  color: #ffffff;
  border: none;
  border-radius: 3px;
}
.fas {
  font-size: 14px;
  color: #333;
}
</style>
