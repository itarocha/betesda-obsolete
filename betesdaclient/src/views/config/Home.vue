<template>
  <v-container fluid>
    <v-slide-y-transition mode="out-in">
      <v-layout column align-center>
        <img src="@/assets/cartaz.jpg" alt="Casa Betesda" class="mb-5">
        <blockquote>
          A Casa Betesda é uma organização sem fins lucrativos que oferece hospedagem diária e gratuita a mulheres ou crianças em tratamento no HC de Uberlândia.
        </blockquote>
      </v-layout>
    </v-slide-y-transition>
  </v-container>
</template>

<!-- Add "scoped" attribute to limit CSS to this component only -->

<script>

export default {
  name: 'Home',

  components: {
  },

  data: () =>({
    dados: [],
  }),

  computed: {
  },

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','')
  },

  methods: {
  }
}
</script>


<style scoped>
body{
  background-color: white;
}
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
