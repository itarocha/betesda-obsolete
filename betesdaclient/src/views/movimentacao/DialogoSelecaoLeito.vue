<template>
  <div>
    <v-dialog v-model="dialogVisible" width="650" :scrollable="true">
        <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          {{titulo}}
        </v-card-title>
        <v-card-text>
          <v-container grid-list-md>
            <v-layout wrap>
              <v-flex xs12 sm12 md12>
                  <h3>{{nome}}</h3>
              </v-flex>
              <v-flex xs12 sm12 md12>
                  <v-select v-model="quarto" :items="itensQuarto" item-text="displayText" item-value="id" label="Quarto" required></v-select>
              </v-flex>
              <v-flex xs12 sm12 md12 v-if="quarto != null">
                  <p style="color:'red'">Clique sobre a representação do leito para selecionar...</p>
              </v-flex>

              <v-flex xs3 v-for="(leito, i) in leitos" :key="i">
                <v-card v-bind:class="{'amber lighten-4':leitoSelecionado(leito), 'grey lighten-4':!leitoSelecionado(leito)}" @click.native="selecionarLeito(leito)">
                  <v-card-text class="text-sm">
                    <h3>{{leito.numero}}</h3>
                    <div class="caption">{{leito.tipoLeito.descricao}}</div>
                    <div class="caption">{{leito.tipoLeito.quantidadeCamas}} cama(s)</div>
                  </v-card-text>
                </v-card>
              </v-flex>

            </v-layout>
          </v-container>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small class="white--text cyan darken-4" @click.native="close(true)" :disabled="quarto == null || leito == null">
            Ok
          </v-btn>
          <v-btn small color="secondary" @click.native="close(false)">
            Cancelar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

export default {
  name: 'DialogoSelecaoLeito',
  
  props: {
    nome : String
  },

  data: () =>({
    titulo : "Selecionar Leito",
    hospede : null,
    quarto : null,
    leito: null,
    leitos : [],
    quartos : [],
    dialogVisible : false,
  }),

  created(){
    this.reset()
  },

  watch: {
    quarto(value) {
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
      this.dialogVisible = true
    },

    leitoSelecionado(leito){
      return this.leito && leito.id == this.leito.id
    },

    selecionarLeito(leito){
      this.leito = leito
    },

    close(selected){
      if (selected){
        var item = null
        
        for (var i = 0; i < this.quartos.length; i++) {
          if (this.quarto == this.quartos[i].id){
            item = {
              quarto : this.quartos[i], 
              leito : this.leito
            }
            break;
          }
        }
        
        this.$emit('close',this.hospede, item)
      } else {
        this.$emit('close',this.hospede, null)
      }
      this.dialogVisible = false
    },

    loadQuartosPorTipoUtilizacao(destinacaoHospedagemId){
      this.quartos = [];
      if (destinacaoHospedagemId != null){
        let uri = petra.base_uri + "/app/quarto/por_destinacao_hospedagem/"+destinacaoHospedagemId
        axios.get(uri).then(response => {
          this.quartos = response.data
        });
      }
    },

    reset(){
      this.tipoHospede = null
      this.quarto = null
      this.leito = null
      this.leitos = []
    },

  }
}
</script>