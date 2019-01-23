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
              <frame-hospedagem ref="frameHospedagem"></frame-hospedagem>
            </v-flex>    

          </v-card-text>

          <v-divider></v-divider>

          <v-card-actions class="grey lighten-4"> 
            <v-spacer></v-spacer>
            <v-btn small dark color="cyan darken-4" @click.native="deleteConfirm">
              Excluir
            </v-btn>
            <v-btn small dark color="cyan darken-4" @click.native="showSelecionarDataRenovacao" v-if="hospedagem.dataEfetivaSaida == null">
              Renovar
            </v-btn>
            <v-btn small dark color="cyan darken-4" @click.native="showSelecionarDataEncerramento" v-if="hospedagem.dataEfetivaSaida == null">
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

    //tabActive : 0,

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
    dialogVisible : false
  }),

  created(){
    this.reset()
  },

  methods: {
    openDialog(hospedagemId){
      this.reset()
      this.hospedagemId = hospedagemId
      //this.getInfo(hospedagemId)
      

      this.dialogVisible = true
      this.$refs.frameHospedagem.open(hospedagemId, true)
    },

    /*
    formatDate(data, formato){
      return petraDateTime.formatDate(data) || '---'
    },
   
    tipoUtilizacao(tipo){
      return tipo == "T" ? "Total" : "Parcial"
    },

    isBaixado(hospede){
      return hospede.baixado == 'S'
    },

    getInfo(hospedagemId) {
      var dados = {
        hospedagemId : hospedagemId
      }

      petra.axiosPost("/app/hospedagem/mapa/hospedagem_info", dados)
        .then(response => { 
            this.hospedagem = response.data
            this.entidade = (this.hospedagem && this.hospedagem.entidade) ? this.hospedagem.entidade : null
            this.encaminhador = (this.hospedagem && this.hospedagem.encaminhador) ? this.hospedagem.encaminhador : null
            this.hospedes = this.hospedagem.hospedes
            this.servicos = (this.hospedagem && this.hospedagem.servicos) ? this.hospedagem.servicos : []
            this.tipoHospede = this.hospedagem.hospedes[0].tipoHospede
            this.destinacaoHospedagem = this.hospedagem.destinacaoHospedagem
          }).catch(error => {
            this.errors = petra.tratarErros(error);
          })
    },
    */

    showSelecionarDataEncerramento(hospedagemId, dataPrevistaSaida){
      this.$refs.frameHospedagem.showSelecionarDataEncerramento()
    },

    onSelecionarDataEncerramento(hospedagemId, dataEncerramento){
      if (dataEncerramento != null){
        this.encerrarHospedagem(hospedagemId, dataEncerramento)
      }
    },

    encerrarHospedagem(hospedagemId, data) {
      var dados = {
        hospedagemId : hospedagemId,
        data : data
      }

      petra.axiosPost("/app/hospedagem/mapa/encerramento", dados)
        .then(response => {
          this.$emit('encerrada',hospedagemId)
          this.dialogVisible = false
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    // Baixa
    showSelecionarDataBaixa(hospedeId, dataPrevistaSaida){
      this.$refs.dlgSelecaoDataBaixa.openDialog(hospedeId, dataPrevistaSaida);
    },

    // TODO trigger
    onSelecionarDataBaixa(hospedeId, dataBaixa){
      /*
      if (dataBaixa != null){
        this.baixarHospedagem(hospedeId, dataBaixa)
      }
      */
    },

    /*
    baixarHospedagem(hospedeId, data) {
      var dados = {
        hospedeId : hospedeId,
        data : data
      }
      
      petra.axiosPost("/app/hospedagem/mapa/baixar", dados)
        .then(response => {
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },
    */

    // Renovação
    showSelecionarDataRenovacao(){
      this.$refs.frameHospedagem.showSelecionarDataRenovacao();
    },

    // TODO trigger
    onSelecionarDataRenovacao(hospedagemId, dataRenovacao){
      if (dataRenovacao != null){
        this.renovarHospedagem(hospedagemId, dataRenovacao)
      }
    },

    renovarHospedagem(hospedagemId, data) {
      var dados = {
        hospedagemId : hospedagemId,
        data : data
      }
      
      petra.axiosPost("/app/hospedagem/mapa/renovacao", dados)
        .then(response => {
          this.$emit('close',true)
          this.dialogVisible = false
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    // TODO trigger
    onCloseTransferencia(sucesso){
      if (sucesso){
        //this.getInfo(this.hospedagemId)
      }
    },  

    close(value){
      this.dialogVisible = false
      this.$emit('close',value)
    },

    encerrar(evt) {
      this.$emit('close',true)
    },

    deleteConfirm() {
      this.$refs.frameHospedagem.deleteConfirm()
    },

    // TODO trigger
    onDeleteConfirmed(evt) {
      this.$emit('close',true)
      /*
      petra.axiosDelete("/app/hospedagem/"+this.hospedagemId)
        .then(response => {
          petra.showMessageSuccess('Hospedagem excluída com sucesso')
          this.$emit('close',true)
          this.dialogVisible = false
        })
        .catch(error => {
        })
      */  
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