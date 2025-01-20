<template>
  <div>
    <p style="font-size: 18px">Website Visit Count:<b> {{ visitCount }}</b></p>
  </div>
</template>

<script>
import {serverIP} from "../../../public/config";

export default {
  data() {
    return {
      serverIP:serverIP,
      visitCount: 0
    };
  },
  mounted() {
    this.incrementVisitCount();
  },
  methods: {
    fetchVisitCount() {
      // 发送GET请求获取网站访问总数
      this.$http.get('http://'+serverIP+':9012/api/visits/count')
        .then(response => {
          this.visitCount = response.data;
        })
        .catch(error => {
          console.error('Error fetching visit count:', error);
        });
    },
    incrementVisitCount() {
      // 发送POST请求增加网站访问总数
      this.$http.post('http://'+serverIP+':9012/api/visits/increment')
        .then(() => {
          this.fetchVisitCount(); // 更新网站访问总数
        })
        .catch(error => {
          console.error('Error incrementing visit count:', error);
        });
    }
  }
};
</script>
