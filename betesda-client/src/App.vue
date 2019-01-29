<template>
  <div id="app">
    <div class="imagem">
      <img class="imagem" src="./assets/logo.png">
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style>
#app {
  font-family: 'Roboto, Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 20px;
}
.imagem {
  text-align: center;
}
</style>
