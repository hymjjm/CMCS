<template>
  <!-- 登录容器 -->
  <div class="login_container">
    <!-- 登录盒子 -->
    <div class="login_box">
      <!-- 头像盒子 -->
<!--      <div class="avatar_box">-->
<!--        <img src="" alt="" />-->
<!--      </div>-->
      <!-- 表单 -->
      <el-form label-width="0px" class="login_form" :model="loginForm" :rules="loginFormRules">
        <!-- 邮箱 -->
        <el-form-item prop="username">
          <b style="color:#f8f6f6;">Email:</b><el-input v-model="loginForm.email" placeholder="Please enter email"></el-input>
        </el-form-item>
        <!-- 验证码 -->
        <el-form-item prop="password">
          <b style="color:#f8f6f6;">Verification code:</b> <el-input v-model="loginForm.code" type="password" placeholder="Please enter the verification code"></el-input>
        </el-form-item>
        <!-- 按钮 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="sendCode" :disabled="disabled">{{codeBtnMsg}}</el-button>
          <el-button style="background-color: green; color: #FFFFFF" @click="login">Login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {serverIP} from "../../public/config";

export default {
  name: 'Login',
  data() {
    return {
      serverIP:serverIP,
      disabled: false, // 发送短信按钮
      codeBtnMsg: "SendCode", // 发送短信按钮提示
      // 通过v-model双向绑定登录表单数据
      loginForm: {
        username: '',
        email: '',
        code:'',
      },


    }
  },
  methods:{
    sendCode(){
      if (!this.loginForm.email) {
        this.$message.error("The mailbox cannot be empty");
        return;
      }
      // 发送验证码
      this.$http.post('http://'+serverIP+':9012/user/code?email='+this.loginForm.email)
        .then(() => {})
        .catch(err => {
          console.log(err);
          this.$message.error(err)
        });
      // 禁用按钮
      this.disabled = true;
      // 按钮倒计时
      let i = 60;
      this.codeBtnMsg =  'You can retry in '+ (i--) +' seconds'
      let taskId = setInterval(() => this.codeBtnMsg = 'You can retry in '+ (i--) +' seconds', 1000);
      setTimeout(() => {
        this.disabled = false;
        clearInterval(taskId);
        this.codeBtnMsg = "Send verification code";
      }, 59000)
    },

    login(){
      if(!this.loginForm.email || !this.loginForm.code){
        this.$message.error("The email address and verification code cannot be empty！");
        return
      }
      this.$http.post('http://'+serverIP+':9012/user/login', this.loginForm)
        .then(({data}) => {
          if(data){
            // 保存用户信息到session
            sessionStorage.setItem("token", data.data);
          }
          // // 跳转到首页
          // this.$router.push({ path: '/home' });
          // 触发自定义事件通知父组件关闭dialog
          this.$emit('login-success');
          // 刷新网页
          window.location.reload();
        })
        .catch(err => this.$message.error(err))
    },

  }
}
</script>

<style  scoped>
.login_container {
  /*// 登录组件背景色*/
  background-color: #4e7093;
  height: 100%;
}
  /*// 登录盒子设置*/
  .login_box {
    width: 450px;
    height: 300px;
    background-color: #547ca4;
    border-radius: 3px;
    /*// 盒子水平垂直居中设置*/
    /*position: absolute;*/
    /*left: 50%;*/
    /*top: 50%;*/
    /*transform: translate(-50%, -50%);*/


  }
.login_form {
  position: absolute;
  bottom: 0;
  width: 96%;
  padding: 0 20px;
  box-sizing: border-box;

}
.btns {
  display: flex;
  justify-content: flex-end;
}
/*// 头像盒子*/
.avatar_box {
     width: 130px;
     height: 130px;
     border: 1px solid #eee;
     border-radius: 50%;
     padding: 10px;
     box-shadow: 0 0 10px #ddd;
     position: absolute;
     left: 50%;
     transform: translate(-50%, -50%);
     background-color: #fff;

   }
img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #eee;
}

</style>
