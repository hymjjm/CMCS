<template>
  <div>
<!--    <input type="file" ref="fileInput" @change="handleFileChange" />-->

<!--    <button @click="uploadFile">Upload</button>-->
<!--    action="http://47.97.192.100:9012/jjm-cluster-upload/import/"-->
<!--    action="http://localhost:9012/jjm-cluster-upload/import/"-->
    <el-upload
      class="upload-demo"
      action="http://localhost:9012/jjm-cluster-upload/import/"
      multiple
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
      :limit="1"
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

      <div style="display: flex;justify-content: center;align-items: center">
        <div slot="tip" style="text-align: center;margin-left: 10px" class="el-upload__tip">Can Only Be TSV Or TXT</div>
      </div>

    </el-upload>
  </div>
</template>

<script>
import {serverIP} from "../../../public/config";
import axios from "axios";

export default {
  data() {
    return {
      serverIP:serverIP,
      uploading: false ,// 用于控制 loading 效果的变量
      uploadHeaders: {} // 用于存储请求头信息
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
      const isCsvOrTxt =
        file.type === 'text/tsv' || file.type === 'text/plain' || file.name.endsWith('.tsv') || file.name.endsWith('.txt');
      if (!isCsvOrTxt) {
        this.$message.error('Only TSV or TXT files are allowed. Please upload a valid file with .tsv or .txt extension.');
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
         return true;
    },

    handleSuccess(response, file, fileList) {
      // 处理上传成功的逻辑
     this.$message.success({
       showClose: true,
       message: "File uploaded : "+response,
     });
      // 刷新数据
      // 发出事件请求更新数据
      this.$emit('update-sample-and-cancer-types');
      // 上传完成后关闭 loading
      this.uploading = false;
    },
    handleError(err, file, fileList) {
      this.$message.error('Upload error'); // 显示上传失败的消息
       console.error('Upload error:', err); // 在控制台打印详细错误信息
      // 上传失败关闭 loading
      this.uploading = false;
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
