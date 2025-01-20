<template>
  <div id="app">
    <header class="navbar" style="height: 60px;background-color: rgba(37,112,17,0.75);color: rgba(252,249,249,0.99)">
    <div style="position: fixed;left: 10px;top:5px;">
      <img src="http://tmliang.cn/pic/jjm/mutCluater/logo.png" style="width: 50px;height: 50px;margin-right: 30px;">
      <b >CMCS</b> <span style="font-size: 16px;margin-top: 10px;margin-right: 80px">-Cluster Mutation Classification System</span>
    </div>

      <nav class="nav" style="z-index: 999;">
        <ul>
          <li><a href="#/home"><i class="fas fa-home"></i></a></li>
          <li><a href="#/omics" >Omics Tool</a></li>
          <li><a href="#/download">Download</a></li>
          <li><a href="#/help" >Help</a></li>
          <li><a href="#/contact" >Contact Us</a></li>

        </ul>
      </nav>

    </header>
    <div class="navbar2" style="margin-top:60px;background-color: rgba(241,245,240,0.88);">
      <p class="neon-text3">-In about 10% of human cancers, clustered somatic mutations drive cancer evolution and play a crucial role in cancer etiology.
        <br/>To address this, our platform offers fast and efficient data upload, cluster mutation detection and classification, along with visual results for display and download.
      </p>
      <p style="width: 90px"></p>
    </div>


<!--    DataBase-->
    <div  style="display: flex;align-items: center;justify-content: center">
      <button class="database-btn" @click="showTable"><i class="fas fa-eye" style="margin-right: 3px"></i>DataBase</button>

      <el-dialog
        :visible.sync="showModal"
        title="A presentation of data that has been uploaded to a database"
        width="1080px"
      >

        <div id="totalFox" style="height: 220px; width: 1040px;">
          <div id="leftFox" style="width: 450px;border: rgba(51,51,51,0.16) solid;border-radius: 15px;padding: 15px ">
            <!--      全部数据去重代码-->
            <button v-loading="dedupLoading" class="deduplicateData-btn" @click="deduplicateData"><i class="fas fa-filter" style="margin-right: 3px"></i>Deduplication of all data</button>
            <!-- 用户说明文本 -->
            <p >The column order is mandatory.</p>
            <p>It must be a single nucleotide polymorphism (SNP).</p>
            <p >Please ensure that the data uploaded is de-duplicated.</p>
            <p >You can only delete data whose nickname is the same as your own.</p>
          </div>
          <!--       删除数据-只能删和昵称相同的数据   -->
          <deleteByNickName id="rightFox"  :UserID="UserID"></deleteByNickName>
        </div>

        <!--  表格-->
        <div style="background-color: white;border-radius: 5px;text-align: center">
          <OriTable :tableData="tableData" v-loading="dedupLoading" ></OriTable>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[2, 5, 10, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            i18n="en"
          >
          </el-pagination>
        </div>
      </el-dialog>
    </div>

<!--    用户登陆-->
    <div>
      <button class="database-USER" @click="showUserLog" >
        <el-popover
          placement="top-start"
          width="100"
          trigger="hover"
        >
        <template slot="reference">
        <i class="el-icon-user-solid" style="font-size: 28px;"></i>
        </template>
          <div>Nicaname: {{UserID}}</div>
        </el-popover>
      </button>

      <el-dialog
        :visible.sync="showUserLogin"
        width="550px"
      >
              <UserLogin @login-success="handleLoginSuccess"></UserLogin>
      </el-dialog>
    </div>



    <div id="appmain" style="margin-bottom:100px ">
      <router-view />
    </div>

    <!--    注脚-->
    <footer class="footer" style="margin-top: 10px">
      <div class="copyright">
        <p>Copyright &copy; Guo Lab, State Key Laboratory of Organic Electronics and Information Displays & Institute of Advanced Materials (IAM), Nanjing University of Posts & Telecommunications, Nanjing, China</p>
        <p>Liang Lab, College of Life Science, Nanjing Normal University, Nanjing, China</p>
      </div>
      <div class="icp">
<!--        网站访问次数-->
        <VisitCounter></VisitCounter>
        <p>&copy; All rights reserved. ICP9036104</p>
      </div>
    </footer>
  </div>
</template>

<script>

import OriTable from "../components/utils/OriTable";
import {serverIP} from "../../public/config";
import VisitCounter from "../components/utils/VisitCounter";
import UserLogin from "../components/UserLogin";
import deleteByNickName from "../components/utils/deleteByNickName";

export default {
  name: 'App',
  components: {OriTable,VisitCounter,UserLogin,deleteByNickName},
  data() {
    return {
      serverIP:serverIP,
      showModal: false,
      showUserLogin: false,
      UserID:"",
      tableData:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      dedupLoading: false,
    };
  },
  methods:{
    deduplicateData(){
      this.dedupLoading=true;

      this.$http.post('http://' + serverIP + ':9012/jjm-cluster-upload/deduplicateData', {}, {
      }).then(response => {
        this.$message({
          message:"Successful deduplication",
          type: 'success'
        });
        this.dedupLoading=false;
        // console.log(response.data);
        // 去重后刷新数据
        this.SelectOriData();
      }).catch(err =>{
        const token = sessionStorage.getItem('token');
        console.log('Sending request with token:', token);
        this.$message.error({message:"This part of the function is available only after login"})
      })
      this.dedupLoading=false;
    },
    showTable(){
      this.showModal=true;
    },
    showUserLog(){
      this.showUserLogin=true
    },
    handleLoginSuccess() {
      this.showUserLogin = false;
      this.$http.get('http://'+serverIP+':9012/user/me').then(res =>{
        // console.log(res)
        this.UserID = res.data.nickName;
        // console.log(this.UserID);
      })
    },
    SelectOriData() {
      this.$http.get('http://'+serverIP+':9012/jjm-cluster-upload/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        // console.log("SelectOriData:",res.data.records)
        this.tableData = []
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleSizeChange(pageSize) {
      console.log(`每页 ${pageSize} 条`)
      this.pageSize = pageSize
      this.SelectOriData();
    },
    handleCurrentChange(pageNum) {
      console.log(`当前页: ${pageNum}`)
      this.pageNum = pageNum
      this.SelectOriData();
    },
  },
  mounted() {
    this.SelectOriData();
    this.handleLoginSuccess();
  }

}
</script>

<style >
/* 修改 el-select 的宽度 */
.el-select {
  width: 300px !important; /* 根据需要设置宽度 */
}
/* 导航栏样式 */
.navbar {
  position: fixed; /* 固定定位 */
  top: 0; /* 距离顶部距离 */
  left: 0; /* 距离左侧距离 */
  width: 100%; /* 宽度占满整个页面 */
  height: 60px; /* 导航栏高度 */
  z-index: 1000; /* 设置 z-index 值确保导航栏位于其他内容上方 */
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  font: 30px/60px "heiti";
  /*box-shadow: 9px 9px 9px rgba(245, 242, 242, 0.01); !* 添加阴影效果 *!*/
}
.navbar2 {
  position: fixed; /* 固定定位 */
  top: 0; /* 距离顶部距离 */
  left: 0; /* 距离左侧距离 */
  width: 100%; /* 宽度占满整个页面 */
  height: 50px; /* 导航栏高度 */
  z-index: 1000; /* 设置 z-index 值确保导航栏位于其他内容上方 */
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  /*font: 30px/60px "heiti";*/
  box-shadow: 0px 2px 3.5px rgba(0, 0, 0, 0.3); /* 添加阴影效果 */
}

/* 页面内容样式 */
#appmain {
  flex: 1; /* 使主内容区占满剩余空间 */
  margin-top: 105px; /* 将页面内容下移，避免导航栏遮挡内容 */
}
/*!* 修改选中字体的颜色和加粗 *!*/
/*.el-menu-item.is-active {*/
/*  color: #5457697F; !* 替换为你想要的选中字体颜色，可以使用颜色代码、颜色名称等 *!*/
/*  font-weight: bold;*/
/*  background-color: #54576918; !* 替换为你想要的选中背景颜色，可以使用颜色代码、颜色名称等 *!*/
/*}*/

/* Footer styles---------------------------------------------------------------------- */
.footer {
  /*position: fixed;*/
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(17, 17, 17, 0.92); /* 设置底部的背景色，根据需要调整颜色值 */
  padding: 10px;
  text-align: center;
  font-size: 14px;
  color: white;
}
.footer p {
  margin: 5px 0;
}
/*body {*/
/*  background-color: rgba(16, 54, 2, 0.07);*/
/*}*/
body {
  background-color: rgba(129, 134, 128, 0.07);
}
a {
  text-decoration: none;
}

.database-btn {
  position: fixed; /* 固定定位 */
  top: 65px;
  right: 3px;
  z-index: 1000;
  background-color: #032e5b;
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  padding: 8px 8px;
  font-size: 13px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.database-USER {
  position: fixed; /* 固定定位 */
  top: 10px;
  right: 10px;
  z-index: 1000;
  background-color: #f9fafc;
  color: #0f6909;
  border: none;
  border-radius: 54px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  box-shadow: 2px 2px 1px 1px rgba(26, 25, 25, 0.44);
}
.deduplicateData-btn {

  background-color: rgba(15, 91, 3, 0.88);
  color: #FFFFFF;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}


.nav {
  display: block;
  position: fixed;
  top: -30px;
  right: 100px;
  width: 100%;
  padding: 0;

}
.nav ul {
  display: flex;
  justify-content: flex-end; /* 右对齐导航项 */
  color: rgba(161, 161, 11, 0.64);

}
.nav li {
  margin-right: 45px;
}
.nav li:hover {
  color: rgba(14, 42, 180, 0.75);
}
.nav a {
  text-decoration: none;
  color: #ffffff;
  /*font-weight: bold;*/
  font-size: 22px; /* 修改字体大小 */
  margin-right: 10px;
}
.nav a:hover {
  /*color: rgba(14, 42, 180, 0.75);*/
}

.neon-text3 {
  font-size: 17px; /* 调整字体大小 */
  margin-left: 0px;
  color: #000000; /* 设置文字颜色为白色 */
  line-height: 1.2; /* 调整行间距，可以根据需要修改值 */
  animation: flicker 2.5s infinite alternate;
  /*font-family: Arial;*/
  font-weight: 300; /* 让字体变细 */
  text-shadow:
    rgba(72, 71, 69, 0.3); /* 调整发光效果的颜色和模糊半径 */  animation: flicker 2.5s infinite alternate;
}


#totalFox {
  display: flex;
  height: 220px;
  width: 1040px;
  justify-content: space-between;
}

#leftFox {
  width: 450px;
  border: rgba(51,51,51,0.16) solid;
  border-radius: 15px;
  padding: 15px;
  margin: 10px;
}

#rightFox {
  width: 450px;
}
</style>
