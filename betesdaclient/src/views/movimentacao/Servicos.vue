<template>
  <div>
    <titulo texto="Lançamento de Serviços">
    </titulo>

  </div>
</template>
<script>
import Titulo from "../../components/Titulo.vue"

export default {
  name: 'Servicos',

  components: {
    Titulo
  },

  data: () =>({
    dados: [],
  }),

  computed: {
  },

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','Lançamento de Serviços')
  },

  methods: {
  }
}
</script>