<template>
  <div>
    <v-dialog v-model="dialogVisible" width="800" :scrollable="true">
        <v-card>
          <v-card-title dark class="white--text cyan darken-4">
            Informações de Hospedagem #{{hospedagemId}}
          </v-card-title>

          <v-card-text style="height:450px;">
            <!-- Mensagens de erro -->  
            <v-flex xs12 sm12 md12>
              <v-alert :value="true" type="error" v-for="(error,i) in errors" :key="i">
                {{error.errorMessage}}
              </v-alert>
            </v-flex>

            <v-flex xs10 sm12 md12>
              <frame-hospedagem ref="frameHospedagem" @encerrada="onEncerrada" @renovada="onRenovada" @excluida="onExcluida" @refresh="onRefresh"></frame-hospedagem>
            </v-flex>    

          </v-card-text>

          <v-divider></v-divider>

          <v-card-actions class="grey lighten-4"> 
            <v-spacer></v-spacer>
            <v-btn small dark color="cyan darken-4" @click.native="deleteConfirm">
              Excluir
            </v-btn>
            <v-btn small dark color="cyan darken-4" @click.native="showSelecionarDataRenovacao" v-if="permitirRenovar">
              Renovar
            </v-btn>
            <v-btn small dark color="cyan darken-4" @click.native="showSelecionarDataEncerramento" v-if="permitirEncerrar">
              Encerrar
            </v-btn>
            <v-btn small color="secondary" @click.native="close(false)">
              Fechar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
  </div>
</template>

<script>


import FrameHospedagem from './FrameHospedagem.vue'

export default {
  name: 'HospedagemInfo',
  
  components: {
    FrameHospedagem
  },

  data: () =>({
    titulo : "Incluir/Alterar Destinação de Hospedagem",
    descricaoItemExclusao : "???",

    hospedagemId: 0,
    tipoHospede:{},
    destinacaoHospedagem:{},
    hospede: {},
    hospedes: [],
    servicos: [],
    hospedagem:{},
    entidade:{},
    encaminhador:{},
    dados: [],
    errors:[],
    dialogVisible : false,

    permitirRenovar : false,
    permitirEncerrar : true,
  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(hospedagemId){
      this.reset()
      this.hospedagemId = hospedagemId

      this.dialogVisible = true
      this.$refs.frameHospedagem.open(hospedagemId, true)
    },

    onRefresh(hospedagem){
      this.permitirRenovar = hospedagem.dataEfetivaSaida == null
      this.permitirEncerrar = hospedagem.dataEfetivaSaida == null
    },

    showSelecionarDataEncerramento(hospedagemId, dataPrevistaSaida){
      this.$refs.frameHospedagem.showSelecionarDataEncerramento()
    },

    onEncerrada(){
      petra.showMessageSuccess('Hospedagem encerrada com sucesso')
      this.$emit('close',true)
      this.dialogVisible = false
    },

    // Renovação
    showSelecionarDataRenovacao(){
      this.$refs.frameHospedagem.showSelecionarDataRenovacao();
    },

    onRenovada(){
      this.$emit('close',true)
      this.dialogVisible = false
    },

    close(value){
      this.dialogVisible = false
      this.$emit('close',value)
    },

    deleteConfirm() {
      this.$refs.frameHospedagem.deleteConfirm()
    },

    onExcluida(evt) {
      petra.showMessageSuccess('Hospedagem excluída com sucesso')
      this.$emit('close',true)
      this.dialogVisible = false
    },

    save(evt) {
      this.$emit('close',true)          
    },

    reset(evt){
      this.form = {
        id : null,
        descricao : null
      },
      this.errors = []
    },

    getErrors(fieldName){
      return petra.getErrors(fieldName, this.errors)
    }

  }
}
</script>