<template>
  <div>
    <v-dialog v-model="dialogVisible" width="800" :scrollable="true">
      <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Selecionar Leito
        </v-card-title>
        <v-card-text style="height:500px;">


          <v-layout row wrap>
            <v-flex xs12 sm12 md12>
              <h3>Hóspede: {{nomeHospede}}</h3>
            </v-flex>
          </v-layout>


          <frame-selecao-leito ref="frameSelecaoLeito" @selecionarQuarto="onSelecionarQuarto" @selecionarLeito="onSelecionarLeito"></frame-selecao-leito>
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

import FrameSelecaoLeito from "./FrameSelecaoLeito.vue"

export default {
  name: 'DialogoSelecaoLeito',
  
  components:{
    FrameSelecaoLeito,
  },
  
  data: () =>({
    nomeHospede : null,
    hospede : null,
    quarto : null,
    leito: null,

    // local
    dialogVisible : false,
  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(hospede, destinacaoHospedagemId, dataIni, dataFim){
      this.$refs.frameSelecaoLeito.openDialog(hospede, destinacaoHospedagemId, dataIni, dataFim)
      this.reset()
      this.nomeHospede = hospede.pessoa.nome
      this.dialogVisible = true
    },

    onSelecionarLeito(leito){
      this.leito = leito
    },

    onSelecionarQuarto(quarto){
      this.quarto = quarto
    },

    close(selected){
      if (selected){
        var selecao = this.$refs.frameSelecaoLeito.getSelecao()
        // selecao : {hospede, quarto, leito}
        this.$emit('close',selecao)
      } else {
        this.$emit('close',null)
      }
      // local
      this.dialogVisible = false
    },

    reset(){
      this.quarto = null
      this.leito = null
    },
  }
}
</script> 