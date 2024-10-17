<template>
  <div>
    <div id="left-content-tree" style="position: fixed; top: 120px; left: 9%; width: 14%; border: #0b9305 solid 2px; border-radius: 8px; padding: 5px;">
      <el-tree
        ref="tree"
        :data="data"
        :props="defaultProps"
        default-expand-all="true"
        node-key="id"
        @node-click="handleNodeClick"
        style="border-radius: 8px;">
      </el-tree>
    </div>
    <div id="right-content" style="border: #f7f9fc solid 1px; width: 64%; height: 100%; margin-left: 25%; padding: 13px;">
      <div id="section1">
        <h4>1 Introduction</h4>
          <div>Cluster mutations refer to the phenomenon where multiple mutations occur in a localized region of the genome. These mutations can affect different genes and pathways, leading to multiple functional abnormalities within the same tumor cell. Systematically obtaining cluster mutation data and related genes is particularly important, hence we invented a cluster mutation detection method based on two-dimensional spatial distance developed with Java algorithms and developed the accompanying online web analysis tool CMCS (http://tmliang.cn/cluster). The user-friendly online cluster mutation detection platform is a multi-type cluster mutation detection and expandable tool that can be easily used for online detection, with high convenience and low threshold. It helps to formulate more effective treatment strategies, thereby improving patient clinical outcomes.</div>
        <h4 id="section1-1" style="color: #646363">1.1 Citation</h4>
          <span>If you find this tool useful, please cite:</span>
          <el-divider></el-divider>
          <el-divider></el-divider>
      </div>

      <div id="section2">
        <h4>2 User Login</h4>
          <span>For data security reasons, we have restricted the interfaces for data uploading and modifying database data. These functions can only be used by logging in via SMS. Other functions can be used without logging in.</span>
        <h4 id="section2-1" style="color: #646363">2.1 Login Introduction</h4>
          <span>Click the login icon at the top right to pop up the interface below. Enter any email address and click "Send Code" to send the corresponding verification code to your email.</span>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/login.jpg" lazy></el-image>
          <span>After logging in, the system will automatically generate a permanent nickname for you.</span><br>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/user.jpg" lazy></el-image>
      </div>

      <div id="section3">
        <h4>3 Home Page Function Introduction</h4>
          <span>Except for the four coordinate parameters on the left, the rest of the parameters in the figure below need to be entered before running. The input provides a fuzzy search function. Click the "GO" button to run and get the analysis output results as shown in the figure below, providing visual display.</span>
          <span><br>·Click "Output File" to download the cluster mutation classification result data that best matches the uploaded data format, facilitating extended analysis with the original mutation data file; "Output File2" can download the cluster mutation data of the currently selected chromosome, which is secondary result processing data.</span>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/3.jpg" lazy></el-image>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/31.jpg" lazy></el-image>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/32.jpg" lazy></el-image>
          <span>·Click "Chromosome Data Summary" to get all cluster mutation data of the current sample, which can be viewed in the sunburst chart for dynamic interactive data of the analysis results, providing intuitive visual display.</span>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/33.jpg" lazy></el-image>
        <h4 id="section3-1" style="color: #646363">3.1 Data Upload - Custom Analysis</h4>
          <span>Considering data availability and security, file upload and other operations that will modify the database must be performed by logging in.</span>
          <span><br>·Users can click "Upload File" to upload their mutation data. The upload format is as shown below. The first row must have column names, and the order of the columns must strictly follow the format requirements.</span>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/3_11.jpg" lazy></el-image>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/3_12.jpg" lazy></el-image>
        <h4 id="section3-2" style="color: #646363">3.2 Data Upload - Data Deletion</h4>
          <span>·Users can click the "DataBase" button at the top left to view all uploaded data.</span>
          <span><br>·To prevent user data upload errors, we provide deduplication and conditional deletion functions (of course, these functions can only be used after logging in).</span>
          <span><br>·<b style="color: #fa0a0a">Note:</b> To prevent accidental deletion of others' uploaded data, users can only delete data with the same nickname as their own login.</span>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/3_2.jpg" lazy></el-image>
      </div>

      <div id="section4">
        <h4 >4 Omics Tool</h4>
          <span>·The system provides multi-omics analysis and dynamic visualization functions, enabling users to comprehensively understand the changes of genes at different molecular levels and their potential prognostic capabilities, involving mRNA expression, mutation, DNA methylation, copy number variation, and immune infiltration.</span>
          <span><br>·Users can enter the genes of interest and click the "GO" button to get the analysis results.</span>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/4_1.jpg" lazy></el-image>
        <Method></Method>
      </div>

      <div id="section5">
        <h4 >5 Contact us</h4>
          <span>If you have any other questions, please click the page below to contact us.</span>
          <el-image src="http://tmliang.cn/pic/jjm/mutCluater/contact.jpg" lazy></el-image>
      </div>

    </div>
  </div>
</template>

<script>

import Method from "../components/MultipleOmics/dzx/Method";
export default {
  components: {Method},
  data() {
    return {
      data: [{
        id: 'section1',
        label: '1 Introduction',
        children: [{
          id: 'section1-1',
          label: '1.1 Citation'
        }]
      }, {
        id: 'section2',
        label: '2 User Login',
        children: [{
          id: 'section2-1',
          label: '2.1 Login Introduction'
        }]
      }, {
        id: 'section3',
        label: '3 Home Page ',
        children: [{
          id: 'section3-1',
          label: '3.1 Data Upload - Custom Analysis'
        }, {
          id: 'section3-2',
          label: '3.2 Data Upload - Data Deletion'
        }]
      }, {
        id: 'section4',
        label: '4 Omics Tool',
      },{
        id: 'section5',
        label: '5 Contact us',
      },],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    };
  },
  mounted() {
    this.initObserver();
  },
  methods: {
    handleNodeClick(data) {
      const section = document.getElementById(data.id);
      if (section) {
        // 偏移 120 像素
        const offsetTop = section.offsetTop - 120;
        window.scrollTo({ top: offsetTop, behavior: 'smooth' });
      }
    },
    initObserver() {
      const options = {
        root: null, // 使用视口作为根
        rootMargin: '0px',
        threshold: [0.1, 0.5, 1.0], // 不同的阈值触发不同的状态
      };

      const observer = new IntersectionObserver(this.handleIntersect, options);

      this.data.forEach(node => {
        observer.observe(document.getElementById(node.id));
        if (node.children) {
          node.children.forEach(child => observer.observe(document.getElementById(child.id)));
        }
      });
    },
    handleIntersect(entries) {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          this.$refs.tree.setCurrentKey(entry.target.id);
        }
      });
    }
  }
};
</script>

<style scoped>
</style>
