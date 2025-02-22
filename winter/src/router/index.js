import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/logIn',
      name: 'logIn',
      component: () => import('../views/logIn.vue'),
    },
    {
      path: '/', // 根路径
      redirect: '/logIn', // 重定向到 /logIn
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/MyHome',
      name: 'MyHome',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/MyHome.vue'),
    },
    {
      path: '/Setting',
      name: 'Setting',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Setting.vue'),
    },
    {
      path: '/publishArt',
      name: 'publishArt',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/publishArt.vue'),
    },
    {
      path: '/articles/:id',
      name: 'articles',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/articles.vue'),
    },
    {
      path: '/headbar',
      name: 'headbar',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/headbar.vue'),
    },
    {
      path: '/comment',
      name: 'comment',
      component: () => import('../views/comment.vue'),
    }
  ],
});
router.beforeEach((to,from,next) => {
  //验证token，只有存在token的时候才能跳转到内容页
  let token = localStorage.getItem("token");
  console.log(token);
  if(token!='null' || to.path === "/logIn"){
    next();
  }else{
    next("/logIn");
  }
})

export default router
