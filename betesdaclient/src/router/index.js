import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'

import Home from '@/views/config/Home.vue'
import About from '@/views/modelo/About.vue'

import DestinacaoHospedagem from '@/views/config/DestinacaoHospedagem.vue'
import SituacaoLeito from '@/views/config/SituacaoLeito.vue'

import TipoHospede from '@/views/config/TipoHospede.vue'
import TipoLeito from '@/views/config/TipoLeito.vue'
import TipoServico from '@/views/config/TipoServico.vue'
import Quarto from '@/views/config/Quarto.vue'
import Entidade from '@/views/config/Entidade.vue'

import Checkin from '@/views/movimentacao/Checkin.vue'
import Servicos from '@/views/movimentacao/Servicos.vue'
import Hospedagens from '@/views/movimentacao/Hospedagens.vue'
import HistoricoHospedagens from '@/views/movimentacao/HistoricoHospedagens.vue'


Vue.use(Router)

export default new Router({
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
      path: '/destinacao_hospedagem',
      name: 'destinacao_hospedagem',
      component: DestinacaoHospedagem
    },
    {
      path: '/situacao_leito',
      name: 'situacao_leito',
      component: SituacaoLeito
    },
    {
      path: '/tipo_hospede',
      name: 'tipo_hospede',
      component: TipoHospede
    },
    {
      path: '/tipo_leito',
      name: 'tipo_leito',
      component: TipoLeito
    },
    {
      path: '/tipo_servico',
      name: 'tipo_servico',
      component: TipoServico
    },
    {
      path: '/quarto',
      name: 'quarto',
      component: Quarto
    },
    {
      path: '/entidade',
      name: 'entidade',
      component: Entidade
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
      path: '/servicos',
      name: 'servicos',
      component: Servicos
    },
    {
      path: '/historico_hospedagens',
      name: 'historico_hospedagens',
      component: HistoricoHospedagens
    },
    {
      path: '/helloworld',
      name: 'HelloWorld',
      component: HelloWorld
    }
  ]
})
