// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueLodash from 'vue-lodash'
import App from './App'
import router from './router'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'

import store from './vuex/store'
global.store = store

import axios from 'axios'
global.axios = axios

import petra from './petra'
global.petra = petra

import apiBetesda from './apiBetesda'
global.apiBetesda = apiBetesda

import petraDateTime from './petra-date-time'
import VueRouter from 'vue-router';
global.petraDateTime = petraDateTime

// moment pode ser usado globalmente
var moment = require('moment')
moment.locale('pt-br')
global.moment = moment


Vue.use(Vuetify)
Vue.use(VueLodash)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})