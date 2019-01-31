<template>
  <div id="app">

      <b-navbar toggleable="md" type="dark" variant="primary">

        <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

        <b-navbar-brand href="#" class="font-weight-bold">Casa Betesda - Teste</b-navbar-brand>

        <b-collapse is-nav id="nav_collapse">


          <!-- Right aligned nav items -->
          <b-navbar-nav class="ml-auto">


          <b-navbar-nav>
            <b-nav-item-dropdown text="Cadastros" right>
              <b-dropdown-item href="#"><i class="fas fa-home"></i> Home</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-check-circle"></i> Situações de Leitos</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-tag"></i> Tipos de Hóspedes</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-bed"></i> Tipos de Leitos</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-coffee"></i> Tipos de Serviços</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-heartbeat"></i> Destinações de Hospedagens</b-dropdown-item>
              <b-dropdown-item href="#"><i class="fas fa-hospital"></i> Quartos</b-dropdown-item>
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
                <em>User</em>
              </template>
              <b-dropdown-item href="#">Profile</b-dropdown-item>
              <b-dropdown-item href="#">Signout</b-dropdown-item>
            </b-nav-item-dropdown>
          </b-navbar-nav>

        </b-collapse>
      </b-navbar>    




       <div class="imagem">

    <p>Have a cup of coffee: <i class="fas fa-coffee"></i> </p>
    <p>Have a module of JavaScript: <i class="fab fa-js"></i> </p>
    <p>Have a module of Vue.js: <i class="fab fa-vuejs"></i> </p>


      <i class="fas fa-igloo"></i> <!-- this icon's 1) style prefix == fas and 2) icon name == igloo -->
      <i class="fas fa-igloo"></i> <!-- using an <i> element to reference the icon -->
      <span class="fas fa-igloo"></span> <!-- using a <span> element to reference the icon -->

      <img class="imagem" src="./assets/logo.png">
       <p>Have a cup of coffee: <font-awesome-icon icon="coffee" /></p>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style lang="scss">
#app {
  font-family: 'Roboto, Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*color: #2c3e50;*/
  /*color: var(--indigo);*/

}
.imagem {
  text-align: center;
}
  @import './assets/scss/style';
  //@import './assets/scss/bs';
</style>
