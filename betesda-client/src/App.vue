<template>
  <div id="app">

      <b-navbar toggleable="md" type="dark" variant="primary" class="shadow mb-3">

        <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

        <b-navbar-brand href="#" class="font-weight-bold">Casa Betesda - Teste</b-navbar-brand>

        <b-collapse is-nav id="nav_collapse">

          <!-- Right aligned nav items -->
          <b-navbar-nav class="ml-auto">


          <b-navbar-nav>
            <b-nav-item-dropdown text="Cadastros" right>
              <b-dropdown-item href="home"><i class="fas fa-home"></i> Home</b-dropdown-item>
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
                <em>User</em>
              </template>
              <b-dropdown-item href="#">Profile</b-dropdown-item>
              <b-dropdown-item href="#">Signout</b-dropdown-item>
            </b-nav-item-dropdown>
          </b-navbar-nav>

        </b-collapse>
      </b-navbar>    
      <div id="router">
        <router-view></router-view>
      </div>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style scoped lang="scss">
  #router{
    margin-top:10px;
  }

  @import './assets/scss/style';
</style>
