<template>
  <div style="border: rgba(128,128,128,0.56) solid;border-radius: 5px">
    <!--表格-->
    <div style="margin: 10px;display: flex;justify-content: center">
      <button @click="xhInterface" style="margin-right: 20px;font-size: 24px">GO<i class="fas fa-chart-line"  style="margin-left: 5px;color: #FFFFFF;"></i></button>
      <button @click="downloadCSV" style=" right: 8px;width: 200px;background-color: #ddd;font-size: 20px;border-radius: 3px; color: #333"> <i class="fas fa-download"></i> Output File4</button>
    </div>
    <div v-show="showtable3" style="margin-top: 0px">
      <!--echarts-->
      <div style="display: flex;justify-content: center;align-items: center">
        <div id="mutationFrequencyChart" style="width: 600px; height: 400px;"></div>
        <div id="TCWA3Chart" style="width: 600px; height: 400px;"></div>
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
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Start Position</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>WT</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>MUT</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Strand</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Cluster</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>CancerType</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>RG</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Context</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Annotation</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>GeneCards</strong></th>
            <th style="padding: 10px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Sample</strong></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(symbol, index) in sortedHugoSymbols" :key="index">
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><strong><span style="color: darkred">{{ symbol.gene }}</span></strong></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.chr }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.position }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><span>{{ symbol.wtAlle }}</span></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><span>{{ symbol.mutAlle }}</span></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);"><span>{{ symbol.strand }}</span></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.cluster }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{cancerType}}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{referenceGenomes }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{symbol.context }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{ symbol.mutDescription }}</td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">      <el-link type="primary" :underline="false" :href="'https://www.genecards.org/cgi-bin/carddisp.pl?gene=' + symbol.gene " target="_blank">{{ symbol.gene }}</el-link></td>
            <td style="padding: 10px; border: 1px solid #ddd; background-color: rgba(255,255,255,0.8);">{{sample }}</td>

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
    nickname: String,
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
      // console.log("InterNumber:", this.InterNumber++);
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
        cluster.forEach(point => {
          allSymbols.push({
            gene: point.hugo_Symbol,
            cluster: point.cluster,
            position: point.x,
            wtAlle: point.wtAlle,
            mutAlle: point.mutAlle,
            strand: point.strand,
            mutDescription: point.mutDescription,
            context: point.context,
            chr: chr
          });
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
    transformData(data) {
      return data.map((item) => {
        const context = item && item.context; // 确保 item 和 context 存在
        if (!context || typeof context !== "string") return null; // 如果为空或非字符串，返回 null

        const match = context.match(/([A-Z])\[([A-Z]>[A-Z])\]([A-Z])/); // 正则匹配提取
        if (!match) return null; // 如果匹配失败，返回 null

        const [, leftBase, mutationType, rightBase] = match;
        return {
          context,
          mutationType,
          leftBase,
          rightBase,
        };
      }).filter(Boolean); // 过滤掉不符合格式的项（即 `null`）
    },


    updateChart() {
      const mutationData = this.transformData(this.allHugoSymbols);

      // 统计突变类型频率
      const mutationCounts = {};
      mutationData.forEach(({ mutationType }) => {
        if (mutationType) {
          mutationCounts[mutationType] = (mutationCounts[mutationType] || 0) + 1;
        }
      });

      // 统计上下文分布
      let a3Count = 0; // A3 谱系 (TCW 且 C>T 或 C>G)
      let tcwCount = 0; // 其他 TCW 上下文
      let nonTCWCount = 0; // 非 TCW 突变

      mutationData.forEach(({ leftBase, rightBase, mutationType }) => {
        if (mutationType === 'C>T' || mutationType === 'C>G') {
          // 判断是否属于 A3 谱系 (TCW 上下文，且满足 A3 条件)
          if ((leftBase === 'T') && (rightBase === 'A' || rightBase === 'T')) {
            a3Count++;
          } else if (leftBase === 'T' || rightBase === 'T') {
            tcwCount++; // 属于其他 TCW 上下文
          } else {
            nonTCWCount++; // 属于非 TCW 突变
          }
        } else {
          nonTCWCount++; // 属于非 TCW 突变
        }
      });

      // 设置突变频率柱状图
      const barChart = echarts.init(document.getElementById('mutationFrequencyChart'));
      const barOptions = {
        title: {
          text: 'Mutation Frequency Distribution',
          left: 'center',
        },
        xAxis: {
          type: 'category',
          data: Object.keys(mutationCounts), // 突变类型，例如 ["C>T", "C>G"]
          axisLabel: {
            interval: 0,
            rotate: 45,
          },
        },
        yAxis: {
          type: 'value',
          name: 'Frequency',
        },
        series: [
          {
            data: Object.values(mutationCounts), // 对应的频率
            type: 'bar',
            barWidth: '60%',
            itemStyle: {
              color: '#5470C6',
            },
          },
        ],
      };
      barChart.setOption(barOptions);

      // 设置上下文分布饼图
      const pieChart = echarts.init(document.getElementById('TCWA3Chart'));
      const pieOptions = {
        title: {
          text: 'A3 Signature and Context Distribution',
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['A3 Signature', 'Other TCW Context', 'Non-TCW Mutations'],
        },
        series: [
          {
            name: 'Mutation Context',
            type: 'pie',
            radius: '50%',
            data: [
              { value: a3Count, name: 'A3 Signature' },
              { value: tcwCount, name: 'Other TCW Context' },
              { value: nonTCWCount, name: 'Non-TCW Mutations' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      };
      pieChart.setOption(pieOptions);
    },


    downloadCSV() {
      const columnNames = ["Hugo_Symbol", "Chromosome", "Start_Position", "WT Alle","MUT Alle","Strand", "Cluster","CancerType","Sample","referenceGenomes","context","mutDescription"];
      let csvContent = columnNames.join(",") + "\n";

      this.allHugoSymbols.forEach(symbol => {
        csvContent += `${symbol.gene},${symbol.chr},,${symbol.position},${symbol.wtAlle},${symbol.mutAlle},${symbol.strand},${symbol.cluster},${this.cancerType},${this.sample},${this.referenceGenomes},${symbol.context},${this.mutDescription}\n`;
      });

      const encodedUri = encodeURI("data:text/csv;charset=utf-8," + csvContent);
      const link = document.createElement("a");
      link.setAttribute("href", encodedUri);
      link.setAttribute("download", "table4_" + this.tablename);
      document.body.appendChild(link);
      link.click();
    },
  },
}
</script>

<style scoped>
button{
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
.fas {
  font-size: 14px;
  color: #333;
}
</style>
