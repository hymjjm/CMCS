<template>
<div class="del">
<div class="nickName bor" style="position: absolute; background-color:  rgba(4, 112, 14, 0.71); width: 190px; height: 50px; margin-top: 20px; margin-left: 10px">
  <span style="color:#FFFFFF"> &nbsp;User_Nickname </span> <el-input type="text" v-model="nickName"></el-input>
</div>
  <div class="sample bor" style="position: absolute; background-color: rgba(4,112,14,0.71); width: 190px; height: 50px; margin-top: 20px; margin-left: 250px">
    <span style="color:#FFFFFF"> &nbsp;Sample </span> <el-input type="text" v-model="sample"></el-input>
  </div>
  <div class="cancerType bor" style="position: absolute; background-color: rgba(4, 112, 14, 0.71); width: 190px; height: 50px; margin-top: 90px; margin-left: 10px">
    <span style="color:#FFFFFF"> &nbsp;CancerType </span> <el-input type="text" v-model="cancerType"></el-input>
  </div>
  <div class="GR bor" style="position: absolute; background-color:  rgba(4, 112, 14, 0.71); width: 190px; height: 50px; margin-top: 90px; margin-left: 250px">
    <span style="color:#FFFFFF"> &nbsp;Reference Genomes </span> <el-input type="text" v-model="GR"></el-input>
  </div>
  <div class="button bor" style="position: absolute;text-align: center ; width: 400px; height: 30px; margin-top: 160px; margin-left: 40px">
    <el-button  @click="Delete" style="height: 35px;width: 50%;background-color: rgba(178,50,50,0.82);color: #FFFFFF;border-radius: inherit; font-weight: 400; font-size: 22px; line-height: 13px">DELETE</el-button>
  </div>
</div>
</template>

<script>
import {serverIP} from "../../../public/config";
export default {
  name: "deleteByNickName",
  props:{UserID:String},
  data() {
    return {
      serverIP:serverIP,
      nickName:'',
      sample:'',
      cancerType:'',
      GR:'',

    };
  },
  methods:{
    Delete() {
      this.$http.post('http://'+serverIP+':9012/jjm-cluster-upload/delete',null,{
        params: {
          nickName: this.nickName,
          sample: this.sample,
          cancerType: this.cancerType,
          GR: this.GR,
        }
      }).then(res => {
        this.$message.success("Successfully deleted")
      }).catch(err => {
        console.error("Error details:", err);
        console.log("this.UserID:",this.UserID)
        if(!this.UserID){
          this.$message.error("Please log in first");
        }else {
          this.$message.error("You can only delete data uploaded by yourself");
        }
      })
    },
  },
}
</script>

<style scoped>
.del {
  position: absolute;
  box-shadow: 0 2px 8px 0 rgba(51, 51, 51, 0.47), 0 6px 10px 0 rgba(51, 51, 51, 0.32);
  padding: 5px;
  left: 25%;
  height: 200px;
  width: 450px;
  margin-left: 27%;
  border-radius: 20px;
  background-color: rgba(4, 112, 14, 0.44);
}
.bor {
  border-radius: 10px;
}
</style>
