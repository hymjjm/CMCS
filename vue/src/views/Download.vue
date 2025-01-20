<template>
  <div class="download-page">
    <h1>Download Page</h1>
    <div class="links">
      <button @click="openLink('https://github.com/hymjjm/CMCS-Python-fdr/tree/master')">Python tools</button>
      <button @click="openLink('https://github.com/hymjjm/CMCS/tree/master')">Local download of the website</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "DownloadPage",
  methods: {
    openLink(url) {
      window.open(url, "_blank"); // 打开新窗口并跳转到指定链接
    }
  }
};
</script>

<style scoped>
.download-page {
  text-align: center;
  padding: 20px;
}

.links {
  margin-top: 20px;
}

button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 10px 20px;
  margin: 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #45a049;
}
</style>
