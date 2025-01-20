<template>
  <div>
    <div style="display: flex;justify-content: center;align-items: center; flex-direction: column;">
    <!--    <input type="file" ref="fileInput" @change="handleFileChange" />-->

    <!--    <button @click="uploadFile">Upload</button>-->
    <!--    action="http://47.97.192.100:9012/R/RunYL/"-->
    <!--    action="http://localhost:9012/R/RunYL/"-->
      <el-upload
        class="upload-demo"
        action="http://47.97.192.100:9012/R/RunYL/"
        multiple
        :on-success="handleSuccess"
        :on-error="handleError"
        :before-upload="beforeUpload"
        :limit="1"
        style="text-align: center; "
        :headers="uploadHeaders">
          <el-button size="small" type="primary" :loading="uploading">
            <el-popover
              placement="top-start"
              width="100"
              trigger="hover"
              content="Log in to upload files">
              <template slot="reference">
                <i class="fas fa-upload"></i> Upload File
              </template>
            </el-popover>
          </el-button>
        <div slot="tip" style="text-align: center; background-color: rgba(28, 28, 28, 0.04); border-radius: 3px; padding: 3px; margin-top: 10px;" class="el-upload__tip">
          For upload format, please click the "DataBase" button in the top right for reference.
        </div>
<!--        <div slot="tip" style="text-align: center; margin-top: 10px;" class="el-upload__tip">-->
<!--          Can Only Be xlsx-->
<!--        </div>-->


      </el-upload>
      <!--    显示图片-->
      <div style="width: auto;height: 600px">
        <h3 v-if="!showimg" style="color: rgba(51,51,51,0.24);display: flex;align-items: center;justify-content: center;flex-direction: column">
          <img :src="clusterImg" style="width: 700px; height:600px;">
        </h3>
      </div>
      <el-image
        v-show="showimg" :src="img" style=" height: 100%; width: 100%; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);border-radius: 3px;margin-top: 8px"
        v-loading="loadingimg"
        element-loading-text="Calling R to load, please wait patiently"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
      </el-image>
    </div>


  </div>
</template>

<script>
import {serverIP} from "../../../public/config";
import clusterImg from "../../assets/cluster.png";

export default {
  data() {
    return {
      serverIP:serverIP,
      uploading: false ,// 用于控制 loading 效果的变量
      uploadHeaders: {}, // 用于存储请求头信息
    //  图片显示
      clusterImg:clusterImg,
      img:'',
      loadingimg:false,
      showimg:false,
      showEmpty:true,
    };
  },
  mounted() {
    this.uploadHeaders.Authorization = sessionStorage.getItem('token');
    // console.log("___________________uploadHeaders token:",this.uploadHeaders)
  },
  methods: {
    beforeUpload(file) {
      // 文件上传前打开 loading
      this.uploading = true;
// 检查文件格式
      const isXlsxOrTxt = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'text/plain';
      if (!isXlsxOrTxt) {
        this.$message.error('Only xlsx files are allowed.');
        this.uploading = false;
        return false; // 取消上传
      }

      // 检查token
      const token = sessionStorage.getItem('token');
      if (!token) {
        this.$message.error('Token not found. Please log in first.');
        this.uploading = false;
        return false; // 取消上传
      }
      //到这里了说明文件上传没问题可以打开图片的loading
      this.loadingimg =true;
         return true;
    },

    handleSuccess(response, file, fileList) {
      // 处理上传成功的逻辑
     this.$message.success({
       showClose: true,
       message: "Upload success",
     });
      // 上传完成后关闭文件上传的 uploading
      this.uploading = false;
     //
      this.loadingimg =false;
      // console.log("response:",response);
      this.img='data:image/png;base64,'+response;
      this.showEmpty = false;
      this.showimg =true;

    },
    handleError(err, file, fileList) {
      this.$message.error('Upload error'); // 显示上传失败的消息
       console.error('Upload error:', err); // 在控制台打印详细错误信息
      // 上传失败关闭 loading
      this.uploading = false;
      this.loadingimg =false;
    },
  }
};
</script>
<style scoped>
button{
  width: 260px; /* 设置按钮的最小宽度为100px */
  height:30px;
  overflow: hidden;
  background-color: rgba(4, 110, 148, 0.75);
  color: #ffffff;
  border: none;
  padding: 5px 5px; /* 调整内边距 */
  border-radius: 3px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  font-size: 18px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  outline: none;
}

</style>
