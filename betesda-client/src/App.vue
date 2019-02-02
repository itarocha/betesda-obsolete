<template>
  <div id="app">

      <b-navbar toggleable="md" type="dark" variant="primary" class="shadow mb-3">

        <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

        <b-navbar-brand to="/" class="font-weight-bold">{{titulo}}</b-navbar-brand>

        <b-collapse is-nav id="nav_collapse">

          <!-- Right aligned nav items -->
          <b-navbar-nav class="ml-auto">


          <b-navbar-nav>
            <b-nav-item-dropdown text="Cadastros" right>
              <b-dropdown-item to="/"><i class="fas fa-home"></i> Home</b-dropdown-item>
              <b-dropdown-item to="hello"><i class="fas fa-eye"></i> Hello</b-dropdown-item>
              <b-dropdown-item to="teste"><i class="fas fa-book"></i> Teste</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item href="#"><i class="fas fa-check-circle"></i> Situações de Leitos</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-tag"></i> Tipos de Hóspedes</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-bed"></i> Tipos de Leitos</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-coffee"></i> Tipos de Serviços</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-heartbeat"></i> Destinações de Hospedagens</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-hospital"></i> Quartos</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item href="#"><i class="fas fa-university"></i> Entidades</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-users"></i> Pessoas</b-dropdown-item>
            </b-nav-item-dropdown>
            <b-nav-item-dropdown text="Movimentação" right>
              <b-dropdown-item href="#"><i class="fas fa-sign-in-alt"></i> Checkin</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-suitcase"></i> Hospedagens</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-utensils"></i> Lançamento de Serviços</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-id-card"></i> Históricos de Hospedagens</b-dropdown-item>
            </b-nav-item-dropdown>
          </b-navbar-nav>

            <!--  
            <b-nav-form>
              <b-form-input size="sm" class="mr-sm-2" type="text" placeholder="Search"/>
              <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
            </b-nav-form>
            -->

            <b-nav-item-dropdown right>
              <!-- Using button-content slot -->
              <template slot="button-content">
                <em>{{loggedIn ? user.name : 'Entrar'}}</em>
              </template>
              <b-dropdown-item v-if="!loggedIn" @click.stop="login">Login</b-dropdown-item>
              <b-dropdown-item v-if="loggedIn" @click.stop="logout">Logout</b-dropdown-item>
            </b-nav-item-dropdown>
          </b-navbar-nav>

        </b-collapse>
      </b-navbar>    

      <!--  -->
      <b-alert v-if="snackbar" class="mx-4" :variant="flashMessageType" dismissible
          :show="dismissCountDown"
          @dismissed="closeSnackbar"
          @dismiss-count-down="countDownChanged"
      >
        {{ flashMessageText }}

        <p class="small">Esta mensagem desaparecerá em {{dismissCountDown}} segundos...</p>
        <b-progress :variant="flashMessageType"
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px">
        </b-progress>

      </b-alert> <!-- :color="flashMessageType"-->


      <div id="router">
        <router-view></router-view>
      </div>
  </div>
</template>

<script>
export default {
  name: 'App',

  data () {
    return {
      dismissSecs: 20,
      dismissCountDown: 0,
    }
  },

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

    user(){
      return this.$store.getters.user
    },

    flashMessageType(){
      var fm = this.$store.getters.flashMessage
      return fm.type
    },

    flashMessageText(){
      var fm =  this.$store.getters.flashMessage
      return fm.text
    },

    snackbar: {
      // getter
      get: function () {
        this.dismissCountDown = this.dismissSecs
        return this.$store.getters.snackbar
      },
      // setter
      set: function (newValue) {
        this.$store.dispatch('setSnackbar',newValue)
      }
    }
  },

  methods:{

    countDownChanged (dismissCountDown) {
      this.dismissCountDown = dismissCountDown
      if (dismissCountDown == 0){
        this.$store.dispatch('setSnackbar',false)
      }
    },

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

}
</script>

<style scoped lang="scss">
  #router{
    margin-top:10px;
  }

  @import './assets/scss/style';
</style>
