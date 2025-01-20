import Vue from 'vue'
import Router from 'vue-router'

// Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect:'/home',
    },
    {
      path: '/home',
      name: 'Home',
      component:()=>import('../views/Home'),
    },
    {
      path: '/omics',
      name: 'Omics',
      component:()=>import('../views/Omics'),
    },
    {
      path: '/download',
      name: 'download',
      component:()=>import('../views/Download'),
    },
    {
      path: '/help',
      name: 'Help',
      component:()=>import('../views/Help'),
    },
    {
      path: '/contact',
      name: 'Contact',
      component:()=>import('../views/ContactUs'),
    },

  ]
})
