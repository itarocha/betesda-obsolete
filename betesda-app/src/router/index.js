import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Home from '@/views/Home'
import Teste from '@/components/Teste'
import Login from '@/views/auth/Login'

import SituacoesLeitos from '@/views/cadastros/SituacoesLeitos'
import TiposHospedes from '@/views/cadastros/TiposHospedes'
import TiposServicos from '@/views/cadastros/TiposServicos'
import DestinacoesHospedagens from '@/views/cadastros/DestinacoesHospedagens'
import TiposLeitos from '@/views/cadastros/TiposLeitos'
import Pessoas from '@/views/cadastros/Pessoas'
import Entidades from '@/views/cadastros/Entidades'
import Quartos from '@/views/cadastros/Quartos'
import Checkin from '@/views/movimentacao/Checkin'
import Hospedagens from '@/views/movimentacao/Hospedagens'

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
      path: '/tipos_hospedes',
      name: 'tipos_hospedes',
      component: TiposHospedes
    },
    {
      path: '/tipos_servicos',
      name: 'tipos_servicos',
      component: TiposServicos
    },
    {
      path: '/destinacoes_hospedagens',
      name: 'destinacoes_hospedagens',
      component: DestinacoesHospedagens
    },
    {
      path: '/tipos_leitos',
      name: 'tipos_leitos',
      component: TiposLeitos
    },
    {
      path: '/quartos',
      name: 'quartos',
      component: Quartos
    },
    {
      path: '/pessoas',
      name: 'pessoas',
      component: Pessoas
    },
    {
      path: '/entidades',
      name: 'entidades',
      component: Entidades
    },
    {
      path: '/hello',
      name: 'hello',
      component: HelloWorld
    },
    {
      path: '/checkin',
      name: 'checkin',
      component: Checkin
    },
    {
      path: '/hospedagens',
      name: 'hospedagens',
      component: Hospedagens
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
  //console.log('beforeEach in route from ', from)
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