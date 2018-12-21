import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'

import Home from '@/views/config/Home.vue'
import About from '@/views/modelo/About.vue'
import Login from '@/views/config/Login.vue'

import DestinacaoHospedagem from '@/views/config/DestinacaoHospedagem.vue'
import SituacaoLeito from '@/views/config/SituacaoLeito.vue'

import TipoHospede from '@/views/config/TipoHospede.vue'
import TipoLeito from '@/views/config/TipoLeito.vue'
import TipoServico from '@/views/config/TipoServico.vue'
import Quarto from '@/views/config/Quarto.vue'
import Entidade from '@/views/config/Entidade.vue'

import Checkin from '@/views/movimentacao/Checkin.vue'
import Servicos from '@/views/movimentacao/Servicos.vue'
import HospedagensOld from '@/views/movimentacao/HospedagensOld.vue'
import Hospedagens from '@/views/movimentacao/Hospedagens.vue'
import HistoricoHospedagens from '@/views/movimentacao/HistoricoHospedagens.vue'


Vue.use(Router)

//export default new Router({

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/about',
      name: 'about',
      component: About
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
      path: '/destinacao_hospedagem',
      name: 'destinacao_hospedagem',
      component: DestinacaoHospedagem,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/situacao_leito',
      name: 'situacao_leito',
      component: SituacaoLeito,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/tipo_hospede',
      name: 'tipo_hospede',
      component: TipoHospede,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/tipo_leito',
      name: 'tipo_leito',
      component: TipoLeito,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/tipo_servico',
      name: 'tipo_servico',
      component: TipoServico,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/quarto',
      name: 'quarto',
      component: Quarto,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/entidade',
      name: 'entidade',
      component: Entidade,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/checkin',
      name: 'checkin',
      component: Checkin,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/hospedagens',
      name: 'hospedagens',
      component: Hospedagens,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/hospedagensold',
      name: 'hospedagensold',
      component: HospedagensOld,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/servicos',
      name: 'servicos',
      component: Servicos,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/historico_hospedagens',
      name: 'historico_hospedagens',
      component: HistoricoHospedagens,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/helloworld',
      name: 'HelloWorld',
      component: HelloWorld
    }
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
