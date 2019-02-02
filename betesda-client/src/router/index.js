import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Teste from '@/views/cadastros/Teste'
import Login from '@/views/util/Login'
import Home from '@/views/Home'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: {
        requiresVisitor: true
      }
    },
    {
      path: '/hello',
      name: 'hello',
      component: Hello,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/teste',
      name: 'teste',
      component: Teste,
      meta: {
        requiresAuth: true
      }
    },
    
  ]
})

router.beforeEach((to, from, next) => {
  if(to.matched.some(record => record.meta.requiresAuth)){
    var loggedIn = store.getters.loggedIn
    //console.log("Path: ",to.path," loggedIn = ",loggedIn," meta = ", to.meta)
    if (!loggedIn){
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router

