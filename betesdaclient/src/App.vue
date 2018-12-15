<template>
  <v-app>
    <v-navigation-drawer
      persistent
      :mini-variant="miniVariant"
      :clipped="clipped"
      v-model="drawer"
      enable-resize-watcher
      :light=true
      app
    >
    <!-- fixed -->
      <v-list dense class="pa-1">
        <v-list-tile value="true" v-for="(item, i) in items" :key="i" router :to="item.link" @click.stop="drawer = false">
          <v-list-tile-action>
            <!--<v-icon v-html="item.icon"></v-icon>-->
            <v-icon color="cyan darken-4">{{item.icon}}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title class="teal--text" v-text="item.title"></v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar app :clipped-left="clipped" dark color="cyan darken-4">
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <!--
      <v-btn icon @click.stop="miniVariant = !miniVariant">
        <v-icon v-html="miniVariant ? 'chevron_right' : 'chevron_left'"></v-icon>
      </v-btn>
      -->
      <!--
      <v-btn icon @click.stop="clipped = !clipped">
        <v-icon>web</v-icon>
      </v-btn>
      -->
      <!--
      <v-btn icon @click.stop="fixed = !fixed">
        <v-icon>remove</v-icon>
      </v-btn>
      -->
      <v-toolbar-title v-text="titulo"></v-toolbar-title>
      <v-spacer></v-spacer>
            
      <v-btn v-if="!loggedIn" flat @click.stop="login">Login</v-btn>
      <v-btn v-if="loggedIn" flat @click.stop="logout">Logout</v-btn>

      <v-snackbar
        v-model="snackbar"
        :bottom="false"
        :left="false"
        :multi-line="true"
        :right="false"
        :timeout="3000"
        :top="true"
        :vertical="true"
      >
        {{ flashMessageText }}
        <v-btn
          color="blue"
          flat
          @click="closeSnackbar"
        >
          Fechar
        </v-btn>
      </v-snackbar>

    </v-toolbar>
    <v-content style="background-color:white;">
      <router-view/>
    </v-content>
    <!--
    <v-navigation-drawer
      temporary
      :right="right"
      v-model="rightDrawer"
      fixed
      app
    >
      <v-list>
        <v-list-tile @click="right = !right">
          <v-list-tile-action>
            <v-icon>compare_arrows</v-icon>
          </v-list-tile-action>
          <v-list-tile-title>Switch drawer (click me)</v-list-tile-title>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    -->
    <v-footer :fixed="fixed" app>
      <span>Desenvolvido por Itamar Rocha itarocha@gmail.com &copy; 2018</span>
    </v-footer>
  </v-app>
</template>

<script>

export default {
  name: 'App',

  computed: {
    loggedIn(){
      return this.$store.getters.loggedIn
    },
    count () {
	    return this.$store.state.count
    },
    titulo(){
      return this.$store.getters.titulo
    },
    flashMessageText(){
      return this.$store.getters.flashMessageText
    },
    snackbar: {
      // getter
      get: function () {
        return this.$store.getters.snackbar
      },
      // setter
      set: function (newValue) {
        this.$store.dispatch('setSnackbar',newValue)
      }
    }
  },
  methods:{
    mostrarFlashMessage(mensagem){
      this.$store.dispatch('showFlashMessage', mensagem)
    },
    closeSnackbar(){
      this.$store.dispatch('setSnackbar',false)
    },
    login(){
      this.$router.push({name: 'login'})
    },
    logout(){
      this.$store.dispatch('destroyToken')
      // promisse
      // .then(response => {this.$router.push({name:'home'})})
      this.$router.push({name:'home'})
    },
  },
  data () {
    return {
      clipped: true,
      drawer: true,
      fixed: false,
      flashMessage: "Hello World",
      //https://fontawesome.com/icons?d=gallery&m=free
      items: [
        {
          icon: 'fa-home',
          title: 'Home',
          link: '/'
        },
        {
          icon: 'fa-check-circle',
          title: 'Situações de Leitos',
          link: '/situacao_leito'
        },
        {
          icon: 'fa-users',
          title: 'Tipos de Hóspedes',
          link: '/tipo_hospede'
        },
        {
          icon: 'fa-bed',
          title: 'Tipos de Leitos',
          link: '/tipo_leito'
        },
        {
          icon: 'fa-coffee',
          title: 'Tipos de Serviços',
          link: '/tipo_servico'
        },
        {
          icon: 'fa-heartbeat',
          title: 'Destinações de Hospedagens',
          link: '/destinacao_hospedagem'
        },
        {
          icon: 'fa-hospital',
          title: 'Quartos',
          link: '/quarto'
        },
        {
          icon: 'fa-university',
          title: 'Entidades',
          link: '/entidade'
        },
        {
          icon: 'fa-sign-in-alt',
          title: 'Check-In',
          link: '/checkin'
        },
        {
          icon: 'fa-suitcase',
          title: 'Hospedagens',
          link: '/hospedagens'
        },
        {
          icon: 'fa-utensils',
          title: 'Lançamento de Serviços',
          link: '/servicos'
        },
        {
          icon: 'fa-id-card',
          title: 'Histórico de Hospedagens',
          link: '/historico_hospedagens'
        },
      ],
      miniVariant: false,
      right: true,
      rightDrawer: false,
      title: 'Casa de Hospedagem Betesda'
    }
  }
}
</script>
<style scoped>
  .v-content__wrap {
    background-color: brown;
  }
</style>