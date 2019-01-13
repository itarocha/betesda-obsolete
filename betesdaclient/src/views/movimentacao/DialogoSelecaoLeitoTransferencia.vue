<template>
  <div>
    <v-dialog v-model="dialogVisible" width="800" :scrollable="true">
      <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Selecionar Data e Leito para Transferência
        </v-card-title>
        <v-card-text style="height:500px;">
          <v-layout row wrap>
            <!-- Mensagens de erro -->  
            <v-flex xs12 sm12 md12>
              <v-alert :value="true" type="error" v-for="(error,i) in errors" :key="i">
                {{error.errorMessage}}
              </v-alert>
            </v-flex>

            <v-flex xs12 sm12 md12>
              <h3>Hóspede: {{nomeHospede}}</h3>
            </v-flex>
            <v-flex xs12 sm12 md12>
              <v-text-field label="Data de Transferência" ref="edtDataTransferencia" v-model="dataTransferencia" :mask="'##/##/####'"></v-text-field>
            </v-flex>
          </v-layout>

          <frame-selecao-leito ref="frameSelecaoLeito" @selecionarQuarto="onSelecionarQuarto" @selecionarLeito="onSelecionarLeito"></frame-selecao-leito>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small class="white--text cyan darken-4" @click.native="transferir" :disabled="quarto == null || leito == null || dataTransferencia == ''">
            Transferir
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
  name: 'DialogoSelecaoLeitoTransferencia',
  
  components:{
    FrameSelecaoLeito,
  },
  
  data: () =>({
    errors : [],
    nomeHospede : null,
    hospede : null,
    quarto : null,
    leito: null,
    dataTransferencia : null,

    // local
    dialogVisible : false,
  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(hospede, destinacaoHospedagemId){
      this.$refs.frameSelecaoLeito.openDialog(hospede, destinacaoHospedagemId)
      this.reset()
      this.nomeHospede = hospede.pessoa.nome
      this.dataTransferencia = petraDateTime.hoje("DDMMYYYY")
      this.dialogVisible = true

      setTimeout(() => {
        this.$refs.edtDataTransferencia.focus()
      }, 500)

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

    transferir(){
      var selecao = this.$refs.frameSelecaoLeito.getSelecao()

      var dados = {
        hospedeId : selecao.hospede.id,
        leitoId : selecao.leito.id,
        data : petraDateTime.formatDateBrNoMaskToDb(this.dataTransferencia) 
      }

      petra.axiosPost("/app/hospedagem/mapa/transferir", dados)
        .then(response => {
          this.$emit('close',true)
          this.dialogVisible = false
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })

    },

    reset(){
      this.errors = []
      this.quarto = null
      this.leito = null
    },
  }
}
</script>