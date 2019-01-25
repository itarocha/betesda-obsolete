<template>
  <div>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-select v-model="quarto" :items="itensQuarto" item-text="displayText" item-value="id" label="Quarto" required></v-select>
      </v-flex>
      <v-flex xs12 sm12 md12 v-if="quarto != null">
        <p style="color:'red'">Clique sobre a representação do leito para selecionar...{{dataIni}} - {{dataFim}}</p>
      </v-flex>

      <v-flex xs3 v-for="(leito, i) in leitos" :key="i">
        <v-card :class="classeSituacaoLeito(leito)" @click.native="selecionarLeito(leito)">
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
  
  dataIni : null,
  dataFim : null,

  data: () =>({
    nomeHospede : "",
    hospede : null,
    quarto : null,
    leito: null,
    leitos : [],
    quartos : [],
    leitosOcupados : []
  }),

  created(){
    this.reset()
  },

  mounted(){
  },

  watch: {
    quarto(value) {
      this.$emit('selecionarQuarto',value)
      this.leito = null

      this.refreshLeitos(value)
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
    openDialog(hospede, destinacaoHospedagemId, dataIni, dataFim){
      this.reset()

      var hoje = petraDateTime.hoje()
      this.dataIni = dataIni || hoje
      this.dataFim = dataFim || hoje

      this.loadLeitosOcupados(() =>{
        this.loadQuartosPorTipoUtilizacao(destinacaoHospedagemId)
      })

      this.hospede = hospede
      this.nomeHospede = hospede.pessoa.nome
    },

    loadQuartosPorTipoUtilizacao(destinacaoHospedagemId){
      this.quartos = [];
      if (destinacaoHospedagemId != null){
        petra.axiosGet("/app/quarto/por_destinacao_hospedagem/"+destinacaoHospedagemId)
          .then(response => {
            this.quartos = response.data

            if (this.quartos.length > 0){
              this.quarto = this.quartos[0].id
              this.refreshLeitos(this.quarto.id)
            }

          })
      }
    },

    refreshLeitos(quartoId){
      var leitos = []
      for(var i=0; i < this.quartos.length; i++){
        if (this.quartos[i].id == quartoId){
          leitos = this.quartos[i].leitos
          break
        }
      }
      this.leitos = leitos
    },

    leitoSelecionado(id){
      return this.leito && id == this.leito.id
    },

    classeSituacaoLeito(leito){
      var id = leito ? leito.id : -1

      var leitoSelecionado = this.leitoSelecionado(id)
      var leitoOcupado = this.isLeitoOcupado(id)
      return leitoSelecionado ? "amber lighten-4 ma-1" : leitoOcupado ? "red lighten-2 ma-1" : "grey lighten-4 ma-1"
    },

    isLeitoOcupado(id){
      return (this.leitosOcupados.indexOf(id) >= 0);
    },

    selecionarLeito(leito){
      if (this.isLeitoOcupado(leito.id)){
        // mensagem
      } else {
        this.leito = leito
        this.$emit('selecionarLeito',leito)
      }

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

    loadLeitosOcupados(callback, reject) {

      var request = {
        dataIni : this.dataIni,
        dataFim : this.dataFim
      }

      this.leitosOcupados = []
      petra.axiosPost("/app/hospedagem/leitos_ocupados", request).then(
        response => {
          this.leitosOcupados = response.data
          callback(response)
        })
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
