import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Home from '@/views/Home'
import Teste from '@/components/Teste'
import Login from '@/views/auth/Login'
import SituacoesLeitos from '@/views/cadastros/SituacoesLeitos'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/situacoes_leitos',
      name: 'situacoes_leitos',
      component: SituacoesLeitos
    },
    {
      path: '/hello',
      name: 'hello',
      component: HelloWorld
    },
    {
      path: '/teste',
      name: 'teste',
      component: Teste
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: {
        requiresVisitor: true
      }
    },
  ]
})

router.beforeEach((to, from, next) => {
  console.log('beforeEach in route from ', from)
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