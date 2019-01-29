// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import BootstrapVue from "bootstrap-vue"
import App from './App'
import router from './router'
import store from './vuex/store'
global.store = store

import axios from 'axios'
global.axios = axios


import VueTheMask from 'vue-the-mask'

import petra from './petra'
global.petra = petra

import apiBetesda from './apiBetesda'
global.apiBetesda = apiBetesda

import petraDateTime from './petra-date-time'
global.petraDateTime = petraDateTime

import VueLodash from 'vue-lodash'

var moment = require('moment')
moment.locale('pt-br')
global.moment = moment

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)
Vue.use(VueLodash)
Vue.use(VueTheMask)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  template: '<App/>',
  components: { App }
})
