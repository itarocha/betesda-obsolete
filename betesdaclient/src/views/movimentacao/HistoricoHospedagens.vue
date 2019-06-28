<template>
  <div>
    <titulo texto="Histórico de Hospedagens">
    </titulo>

  </div>
</template>
<script>

import Titulo from "../../components/Titulo.vue"

export default {
  name: 'HistoricoHospedagens',

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
    this.$store.dispatch('setAcao','Histórico de Hospedagens')
  },

  methods: {
  }
}
</script>