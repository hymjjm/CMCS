// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './router/App.vue'
import router from './router'

import ELEMENT  from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en'
// fade/zoom 等
import 'element-ui/lib/theme-chalk/base.css'
import axios from "axios"
import api_ from './api'
import * as echarts from 'echarts';

Vue.use(ELEMENT , { locale })
Vue.config.productionTip = false
Vue.prototype.$http = axios
Vue.prototype.$echarts = echarts
Vue.prototype.api = api_
axios.defaults.baseURL = api_.baseURL

Vue.use(RelationGraph);

//Vue.prototype.request=request

import $ from 'jquery'
import RelationGraph from "relation-graph";

// Vue.use(RelationGraph)
Vue.prototype.$ = $


// 设置 Axios 请求拦截器
axios.interceptors.request.use(function (config) {
  // 从 sessionStorage 获取 token
  const token = sessionStorage.getItem('token');
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
}, function (error) {
  // 处理请求错误
  return Promise.reject(error);
});

// Vue.prototype.$qs = qs;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
