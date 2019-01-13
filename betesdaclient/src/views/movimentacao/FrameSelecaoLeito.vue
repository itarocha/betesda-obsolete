<template>
  <div>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-select v-model="quarto" :items="itensQuarto" item-text="displayText" item-value="id" label="Quarto" required></v-select>
      </v-flex>
      <v-flex xs12 sm12 md12 v-if="quarto != null">
        <p style="color:'red'">Clique sobre a representação do leito para selecionar...</p>
      </v-flex>

      <v-flex xs3 v-for="(leito, i) in leitos" :key="i">
        <v-card v-bind:class="{'amber lighten-4':leitoSelecionado(leito), 'grey lighten-4':!leitoSelecionado(leito), 'ma-1':true}" @click.native="selecionarLeito(leito)">
          <v-card-text class="text-sm">
            <h3>{{leito.numero}}</h3>
            <div class="caption">{{leito.tipoLeito.descricao}}</div>
            <div class="caption">{{leito.tipoLeito.quantidadeCamas}} cama(s)</div>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>

export default {
  name: "FrameSelecaoLeito",

  data: () =>({
    nomeHospede : "",
    hospede : null,
    quarto : null,
    leito: null,
    leitos : [],
    quartos : [],
  }),

  created(){
    this.reset()
  },

  watch: {
    quarto(value) {
      this.$emit('selecionarQuarto',value)
      var leitos = []
      this.leito = null
      for(var i=0; i < this.quartos.length; i++){
        if (this.quartos[i].id == value){
          leitos = this.quartos[i].leitos
          break
        }
      }
      this.leitos = leitos
    }
  },

  computed : {
    itensQuarto() {
      if (this.quartos){
        return this.quartos
      } else {
        return []
      }
    }
  },

  methods: {
    openDialog(hospede, destinacaoHospedagemId){
      this.reset()
      this.loadQuartosPorTipoUtilizacao(destinacaoHospedagemId)
      this.hospede = hospede
      this.nomeHospede = hospede.pessoa.nome
    },

    leitoSelecionado(leito){
      return this.leito && leito.id == this.leito.id
    },

    selecionarLeito(leito){
      this.leito = leito
      this.$emit('selecionarLeito',leito)
    },

    // publico
    getSelecao(){
      var item = null
      
      for (var i = 0; i < this.quartos.length; i++) {
        if (this.quarto == this.quartos[i].id){
          item = {
            hospede : this.hospede,
            quarto : this.quartos[i], 
            leito : this.leito
          }
          return item
        }
      }
      return null
    },

    loadQuartosPorTipoUtilizacao(destinacaoHospedagemId){
      this.quartos = [];
      if (destinacaoHospedagemId != null){
        petra.axiosGet("/app/quarto/por_destinacao_hospedagem/"+destinacaoHospedagemId)
          .then(response => {
            this.quartos = response.data
          })
      }
    },

    reset(){
      this.tipoHospede = null
      this.quarto = null
      this.leito = null
      this.leitos = []
    },

  }  
};
</script>
