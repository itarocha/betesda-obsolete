<template>
  <div>

    <!--
    <dialogo-selecao-data-encerramento ref="dlgSelecaoDataEncerramento" @close="onSelecionarDataEncerramento"></dialogo-selecao-data-encerramento>
    <dialogo-selecao-data-renovacao ref="dlgSelecaoDataRenovacao" @close="onSelecionarDataRenovacao"></dialogo-selecao-data-renovacao>
    <dialogo-confirmacao ref="dlgExclusao" titulo="Confirmação" @ok="onDeleteConfirmed"></dialogo-confirmacao>

    <dialogo-selecao-leito-transferencia ref="dlgSelecaoLeitoTransferencia" @close="onCloseTransferencia"></dialogo-selecao-leito-transferencia>
    <dialogo-selecao-data-baixa ref="dlgSelecaoDataBaixa" @close="onSelecionarDataBaixa"></dialogo-selecao-data-baixa>
    -->

    <!--
    <v-layout justify-space-between row wrap fill-height>
      <v-flex xs12 sm12 md12 v-if="errors">
        <v-alert :value="true" type="error" v-for="(item, i)  in errors" :key="i">
          {{item.errorMessage}}
        </v-alert>
      </v-flex>
    </v-layout>
    -->

    <!-- begin frame-hospedagem -->
          <el-row>
            <el-col :span="24">
              <el-tabs type="border-card" v-if="hospedagem != null">
                <el-tab-pane label="Hospedagem">
                  <el-row :gutter="10">
                    <el-col :span="8">Código</el-col>
                    <el-col :span="6" class="font-weight-bold">{{config.hospedagemId}}</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">Data de Entrada</el-col>
                    <el-col :span="6" class="font-weight-bold">{{formatDate(hospedagem.dataEntrada)}}</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">Data Prevista de Saída</el-col>
                    <el-col :span="6" class="font-weight-bold">{{formatDate(hospedagem.dataPrevistaSaida)}}</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">Data Efetiva de Saída</el-col>
                    <el-col :span="6" class="font-weight-bold">{{formatDate(hospedagem.dataEfetivaSaida)}}</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">Destinação de Hospedagem</el-col>
                    <el-col :span="6" class="font-weight-bold">{{destinacaoHospedagem.descricao}}</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">Tipo de Utilização</el-col>
                    <el-col :span="6" class="font-weight-bold">{{tipoUtilizacao(hospedagem.tipoUtilizacao)}}</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">Situação</el-col>
                    <el-col :span="6" class="font-weight-bold">{{hospedagem.status}}</el-col>
                  </el-row>
                  <el-row :gutter="10" v-if="servicos.length > 0">
                    <el-col :span="8">Serviços</el-col>
                    <el-col :span="16" class="font-weight-bold">
                      <el-tag v-for="(servico, idx) in servicos" :key="idx" type="success" size="medium" class="tags">{{servico.descricao}}</el-tag>
                    </el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="8">Observações</el-col>
                  </el-row>
                  <el-row :gutter="10">
                    <el-col :span="24">
                      <el-input type="textarea" class="font-size-10" rows="5" v-model="hospedagem.observacoes" readonly></el-input>                      
                    </el-col>
                  </el-row>
                </el-tab-pane>
                <el-tab-pane label="Hóspede(s)">
                  <el-row :gutter="10">
                    <el-col :span="24">
                      <el-collapse v-for="(hpd, i) in hospedagem.hospedes" :key="i" v-model="activeName" accordion>
                        <el-collapse-item  :title="hpd.pessoa.nome + ' - ' + hpd.tipoHospede.descricao" :name="i" 
                          :class="{'grey-lighten-4' : !isBaixado(hpd), 'amber-lighten-4' : isBaixado(hpd)}">   
                          <el-row v-if="hpd.baixado == 'S'" class="font-weight-bold">
                            BAIXADO
                          </el-row>
                          <el-row v-if="permitirEditar" :gutter="10">
                            <el-col>
                              <el-button type="primary" size="mini" v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S'" 
                                @click.native="showSelecionarDataBaixa(hpd.id, hospedagem.dataPrevistaSaida)">Baixar
                              </el-button>
                              <el-button type="primary" size="mini" v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S' && hospedagem.tipoUtilizacao == 'T'" 
                                @click.native="showTransferencia(hpd, hospedagem.destinacaoHospedagem.id)">Transferir
                              </el-button>
                            </el-col>
                          </el-row>

                          <el-row :gutter="10">
                            <el-col :span="4">Nascimento</el-col>
                            <el-col :span="20" class="font-weight-bold">{{formatDate(hpd.pessoa.dataNascimento)}}</el-col>
                          </el-row>
                          <el-row :gutter="10">
                            <el-col :span="4">Endereço</el-col>
                            <el-col :span="20" class="font-weight-bold">{{hpd.pessoa.endereco != null ? hpd.pessoa.endereco.descricao : ''}}</el-col>
                          </el-row>

                          <div xs12 sm12 md12 v-if="hospedagem.tipoUtilizacao == 'T'">
                            <el-row>
                              <el-col>Histórico de Leitos</el-col>
                            </el-row>

                            <el-row v-for="(leito, leitoIndex) in hpd.leitos" :key="leitoIndex" class="body-1 pa-1 pl-3">
                                <el-col>
                                #{{leito.id}} - {{formatDate(leito.dataEntrada)}} - Quarto {{leito.quartoNumero}} Leito {{leito.leitoNumero}}
                                <span v-if="(hpd.baixado == 'S') && (leitoIndex == hpd.leitos.length-1)" class="font-weight-bold"> - Baixado em {{formatDate(leito.dataSaida)}}</span>
                                </el-col>
                            </el-row>
                          </div> 
                        </el-collapse-item>
                      </el-collapse> 
                    </el-col>
                  </el-row>
                  </el-tab-pane>

                <el-tab-pane label="Encaminhador">
                  <div if="entidade != null">
                    <el-row :gutter="5" class="font-size-10">
                      <el-col :span="16">
                        Encaminhador
                      </el-col>
                      <el-col :span="8">
                        Cargo
                      </el-col>
                    </el-row>

                    <el-row :gutter="5">
                      <el-col :span="16">
                        <el-input size="mini" readonly v-model="encaminhador.nome"></el-input>
                      </el-col>
                      <el-col :span="8">
                        <el-input size="mini" readonly v-model="encaminhador.cargo"></el-input>
                      </el-col>
                    </el-row>

                    <el-row :gutter="5" class="font-size-10">
                      <el-col :span="8">
                        Telefone
                      </el-col>
                      <el-col :span="16">
                        Email
                      </el-col>
                    </el-row>

                    <el-row :gutter="5">
                      <el-col :span="8">
                        <el-input size="mini" readonly v-model="encaminhador.telefone"></el-input>
                      </el-col>
                      <el-col :span="16">
                        <el-input size="mini" readonly v-model="encaminhador.email"></el-input>
                      </el-col>
                    </el-row>

                    <el-row :gutter="5" class="font-size-10">
                      <el-col :span="16">
                        Entidade
                      </el-col>
                      <el-col :span="8">
                        CNPJ
                      </el-col>
                    </el-row>

                    <el-row :gutter="5">
                      <el-col :span="16">
                        <el-input size="mini" readonly v-model="entidade.nome"></el-input>
                      </el-col>
                      <el-col :span="8">
                        <el-input size="mini" readonly v-model="entidade.cnpj"></el-input>
                      </el-col>
                    </el-row>

                    <el-row :gutter="5" class="font-size-10">
                      <el-col :span="8">
                        Telefone
                      </el-col>
                      <el-col :span="16">
                        Email
                      </el-col>
                    </el-row>

                    <el-row :gutter="5">
                      <el-col :span="8">
                        <el-input size="mini" readonly v-model="entidade.telefone"></el-input>
                      </el-col>
                      <el-col :span="16">
                        <el-input size="mini" readonly v-model="entidade.email"></el-input>
                      </el-col>
                    </el-row>

                    <el-row :gutter="5" class="font-size-10">
                      <el-col :span="12">
                        Endereço
                      </el-col>
                      <el-col :span="4">
                        Número
                      </el-col>
                      <el-col :span="8">
                        Complemento
                      </el-col>
                    </el-row>

                    <el-row :gutter="5">
                      <el-col :span="12">
                        <el-input size="mini" readonly v-model="entidade.endereco.logradouro"></el-input>
                      </el-col>
                      <el-col :span="4">
                        <el-input size="mini" readonly v-model="entidade.endereco.numero"></el-input>
                      </el-col>
                      <el-col :span="8">
                        <el-input size="mini" readonly v-model="entidade.endereco.complemento"></el-input>
                      </el-col>
                    </el-row>

                    <el-row :gutter="5" class="font-size-10">
                      <el-col :span="8">
                        Bairro
                      </el-col>
                      <el-col :span="4">
                        CEP
                      </el-col>
                      <el-col :span="8">
                        Cidade
                      </el-col>
                      <el-col :span="4">
                        UF
                      </el-col>
                    </el-row>

                    <el-row :gutter="5">
                      <el-col :span="8">
                        <el-input size="mini" readonly v-model="entidade.endereco.bairro"></el-input>
                      </el-col>
                      <el-col :span="4">
                        <el-input size="mini" readonly v-model="entidade.endereco.cep"></el-input>
                      </el-col>
                      <el-col :span="8">
                        <el-input size="mini" readonly v-model="entidade.endereco.cidade"></el-input>
                      </el-col>
                      <el-col :span="4">
                        <el-input size="mini" readonly v-model="entidade.endereco.uf"></el-input>
                      </el-col>
                    </el-row>
                  </div>    

                </el-tab-pane>
              </el-tabs>
            </el-col>
          </el-row>

  </div>
</template>

<script>

/*
import DialogoSelecaoDataEncerramento from "./DialogoSelecaoDataEncerramento.vue"
import DialogoSelecaoDataRenovacao from "./DialogoSelecaoDataRenovacao.vue"
import DialogoSelecaoDataBaixa from "./DialogoSelecaoDataBaixa.vue"
import DialogoSelecaoLeitoTransferencia from "./DialogoSelecaoLeitoTransferencia.vue"
import DialogoConfirmacao from '../config/DialogoConfirmacao.vue'
*/

export default {
  name: 'FrameHospedagem',
  
  components: {
    /*
    DialogoSelecaoDataEncerramento,
    DialogoSelecaoDataBaixa,
    DialogoSelecaoDataRenovacao,
    DialogoSelecaoLeitoTransferencia,
    DialogoConfirmacao
    */
  },

  computed: {
    // config é composto de hospedagemId, permitirEditar
  },

  watch: {
    config:{
      deep: true,
      handler() {
        this.open(this.config.hospedagemId, this.config.permitirEditar)
      }
    }
  },

  props:['codigo', 'config'],

  data: () =>({

    hospedagemId: 0,
    hospedagem:{},

    tipoHospede:{},
    destinacaoHospedagem:{},
    hospede: {},
    hospedes: [],
    servicos: [],
    entidade:{},
    encaminhador:{},

    activeName:null,

    dados: [],
    errors:[],
    dialogVisible : false,

    permitirEditar : false,
    tabActive : 0,
  }),

  created(){
    this.reset()
  },

  mounted(){
    this.open(this.config.hospedagemId, this.config.permitirEditar)
  },

  methods: {

    // public
    open(hospedagemId, permitirEditar){
      this.reset()
      this.permitirEditar = permitirEditar || false
      this.hospedagemId = hospedagemId
      this.getInfo(hospedagemId)
    },

    // public
    showSelecionarDataEncerramento(){
      //this.$refs.dlgSelecaoDataEncerramento.openDialog(this.hospedagem.id, this.hospedagem.dataPrevistaSaida);
    },

    // public
    showSelecionarDataRenovacao(){
      //this.$refs.dlgSelecaoDataRenovacao.openDialog(this.hospedagem.id, this.hospedagem.dataPrevistaSaida);
    },

    // public
    getHospedagem(){
      return this.hospedagem
    },

    // interno
    showSelecionarDataBaixa(hospedeId, dataPrevistaSaida){
      //this.$refs.dlgSelecaoDataBaixa.openDialog(hospedeId, dataPrevistaSaida);
    },

    // public
    deleteConfirm() {
      //this.$refs.dlgExclusao.openDialog("Deseja realmente excluir esta Hospedagem?")
    },

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

      petra.axiosPost("/app/hospedagem/mapa/hospedagem_info", dados, false)
        .then(response => { 
            this.hospedagem = response.data
            this.entidade = (this.hospedagem && this.hospedagem.entidade) ? this.hospedagem.entidade : null
            this.encaminhador = (this.hospedagem && this.hospedagem.encaminhador) ? this.hospedagem.encaminhador : null
            this.hospedes = this.hospedagem.hospedes
            this.servicos = (this.hospedagem && this.hospedagem.servicos) ? this.hospedagem.servicos : []
            this.tipoHospede = this.hospedagem.hospedes[0].tipoHospede
            this.destinacaoHospedagem = this.hospedagem.destinacaoHospedagem
            this.$emit('refresh',this.hospedagem)
          }).catch(error => {
            this.errors = petra.tratarErros(error);
          })
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

      petra.axiosPost("/app/hospedagem/mapa/encerramento", dados, false)
        .then(response => {
          this.$emit('encerrada',hospedagemId)
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    onSelecionarDataBaixa(hospedeId, dataBaixa){
      if (dataBaixa != null){
        this.baixarHospedagem(hospedeId, dataBaixa)
      }
    },

    baixarHospedagem(hospedeId, data) {
      var dados = {
        hospedeId : hospedeId,
        data : data
      }
      
      petra.axiosPost("/app/hospedagem/mapa/baixar", dados, false)
        .then(response => {
          this.getInfo(this.hospedagemId)
          petra.showMessageSuccess('Hospedagem encerrada com sucesso')
          this.$emit('close',true)
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

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
      
      petra.axiosPost("/app/hospedagem/mapa/renovacao", dados, false)
        .then(response => {
          this.$emit('renovada',hospedagemId)
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    // interno
    showTransferencia(hospede, destinacaoHospedagemId){
      //this.$refs.dlgSelecaoLeitoTransferencia.openDialog(hospede, destinacaoHospedagemId);
    },

    onCloseTransferencia(sucesso){
      if (sucesso) {
        this.getInfo(this.hospedagemId)
      }
    },  

    onDeleteConfirmed(evt) {
      petra.axiosDelete("/app/hospedagem/"+this.hospedagemId, false)
        .then(response => {
          this.$emit('excluida',this.hospedagemId)
        })
        .catch(error => {
          this.errors = petra.tratarErros(error);
        })
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
<style scoped>

  .font-weight-bold {
    font-weight: bold;    
  }
  .font-size-10 {
    font-size: 10pt;
  }
  .tags {
    margin-right: 5px;
  }

</style>