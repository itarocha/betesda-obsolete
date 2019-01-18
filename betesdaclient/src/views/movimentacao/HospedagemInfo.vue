<template>
  <div>
    <dialogo-selecao-data-encerramento ref="dlgSelecaoDataEncerramento" @close="onSelecionarDataEncerramento"></dialogo-selecao-data-encerramento>
    <dialogo-selecao-data-baixa ref="dlgSelecaoDataBaixa" @close="onSelecionarDataBaixa"></dialogo-selecao-data-baixa>
    <dialogo-selecao-leito-transferencia ref="dlgSelecaoLeitoTransferencia" @close="onCloseTransferencia"></dialogo-selecao-leito-transferencia>
    <dialogo-selecao-data-renovacao ref="dlgSelecaoDataRenovacao" @close="onSelecionarDataRenovacao"></dialogo-selecao-data-renovacao>
    <dialogo-confirmacao ref="dlgExclusao" titulo="Confirmação" @ok="onDeleteConfirmed"></dialogo-confirmacao>
    
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
              <v-tabs v-model="tabActive" slider-color="cyan darken-2" fixed-tabs>
                <v-tab>Hospedagem</v-tab>
                <v-tab>Hóspede(s)</v-tab>
                <v-tab>Encaminhador</v-tab>

                <!-- hospedagem -->
                <v-tab-item>
                  <v-layout justify-space-between row wrap fill-height>
                    <v-flex  xs12 sm12 md12><div style="height:50px;"></div></v-flex>

                    <v-flex  xs12 sm2 md4 offset-md2>
                      Data de Entrada:
                    </v-flex>
                    <v-flex xs12 sm2 md6 class="title">
                      {{formatDate(hospedagem.dataEntrada)}}
                    </v-flex>

                    <v-flex xs12 sm2 md4 offset-md2>
                      Data Prevista de Saída:
                    </v-flex>
                    <v-flex md6 class="title">
                      {{formatDate(hospedagem.dataPrevistaSaida)}}
                    </v-flex>

                    <v-flex xs12 sm2 md4 offset-md2>
                      Data de Saída:
                    </v-flex>
                    <v-flex xs12 sm2 md6 class="title">
                      {{formatDate(hospedagem.dataEfetivaSaida)}}
                    </v-flex>

                    <v-flex xs12 sm2 md4 offset-md2>
                      Destinação de Hospedagem:
                    </v-flex>
                    <v-flex xs12 sm2 md6 class="title">
                      {{destinacaoHospedagem.descricao}}
                    </v-flex>

                    <v-flex xs12 sm2 md4 offset-md2>
                      Tipo de Utilização:
                    </v-flex>
                    <v-flex xs12 sm2 md6 class="title">
                      {{tipoUtilizacao(hospedagem.tipoUtilizacao)}}
                    </v-flex>

                    <v-flex xs12 sm2 md4 offset-md2>
                      Situação:
                    </v-flex>
                    <v-flex xs12 sm2 md6 class="text-uppercase title">
                      {{hospedagem.status}}
                    </v-flex>

                    <v-flex xs12 sm2 md4 offset-md2 v-if="servicos.length > 0">
                      Serviços:
                    </v-flex>
                    <v-flex xs12 sm2 md12 v-if="servicos.length > 0" class="subheading ml-2">
                        <v-chip color="amber lighten-2" v-for="(servico, idx) in servicos" :key="idx">{{servico.descricao}}</v-chip>
                    </v-flex>

                  </v-layout>
                </v-tab-item>

                <!-- hóspedes -->
                <v-tab-item>
                  <v-expansion-panel>
                    <v-expansion-panel-content v-for="(hpd, i) in hospedagem.hospedes" :key="i" expand 
                    :class="{'grey lighten-4' : !isBaixado(hpd), 'amber lighten-4' : isBaixado(hpd)}">
                      <div slot="header">
                        <span class="text-uppercase title">{{hpd.pessoa.nome}}</span> - {{hpd.tipoHospede.descricao}} 
                        <span v-if="hpd.baixado == 'S'" class="text-uppercase subheading font-weight-bold"> BAIXADO</span>
                        <div>
                          <v-btn small dark color="cyan darken-4" 
                            v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S'" 
                            @click.native="showSelecionarDataBaixa(hpd.id, hospedagem.dataPrevistaSaida)">Baixar
                          </v-btn>
                          <v-btn small dark color="cyan darken-4" 
                            v-if="hospedagem.dataEfetivaSaida == null && hpd.baixado != 'S' && hospedagem.tipoUtilizacao == 'T'" 
                            @click.native="showTransferencia(hpd, hospedagem.destinacaoHospedagem.id)">Transferir
                          </v-btn>
                        </div>
                      </div>  
                      <v-card>
                        <v-card-text :class="{'grey lighten-5' : !isBaixado(hpd), 'amber lighten-5' : isBaixado(hpd)}">
                        <v-layout row wrap>
                          <v-flex xs2 sm2 md2>
                            <span class="caption">Nascimento:</span>
                          </v-flex>
                          <v-flex xs10 sm10 md10>
                            {{formatDate(hpd.pessoa.dataNascimento)}}
                          </v-flex>

                          <v-flex xs12 sm2 md2 class="caption">
                            Endereço:
                          </v-flex>
                          <v-flex xs12 sm10 md10>
                            {{hpd.pessoa.endereco != null ? hpd.pessoa.endereco.descricao : ''}}
                          </v-flex>

                          <v-flex xs12 sm12 md12 v-if="hospedagem.tipoUtilizacao == 'T'">
                              <div v-for="(leito, leitoIndex) in hpd.leitos" :key="leitoIndex" class="body-1 pa-1 pl-3">
                                  #{{leito.id}} - {{formatDate(leito.dataEntrada)}} - Quarto {{leito.quartoNumero}} Leito {{leito.leitoNumero}}
                                  <span v-if="(hpd.baixado == 'S') && (leitoIndex == hpd.leitos.length-1)" class="subheading"> - Baixado em {{formatDate(leito.dataSaida)}}</span>
                              </div>


                            <!--  
                            <v-list>
                              <v-list-tile v-for="(leito, leitoIndex) in hpd.leitos" :key="leitoIndex">
                                <v-list-tile-content class="body-1 pa-3">
                                  #{{leito.id}} - {{formatDate(leito.dataEntrada)}} - Quarto {{leito.quartoNumero}} Leito {{leito.leitoNumero}}
                                  <span v-if="(hpd.baixado == 'S') && (leitoIndex == hpd.leitos.length-1)" class="subheading"> - Baixado em {{formatDate(leito.dataSaida)}}</span>
                                </v-list-tile-content>
                              </v-list-tile>
                            </v-list>
                            -->
                          </v-flex>  
                        </v-layout>
                        </v-card-text>
                      </v-card>
                    </v-expansion-panel-content>
                  </v-expansion-panel>
                </v-tab-item>

                <!-- encaminhador -->
                <v-tab-item>
                  <v-layout row wrap v-if="entidade != null">

                    <v-flex xs8 sm8 md8>
                      <v-text-field label="Encaminhador" readonly ref="edtNome" v-model="encaminhador.nome" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs4 sm4 md4>
                      <v-text-field label="Cargo" readonly v-model="encaminhador.cargo" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                      <v-text-field label="Telefone" readonly v-model="encaminhador.telefone" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                      <v-text-field label="Email" readonly v-model="encaminhador.email" hide-details></v-text-field>
                    </v-flex>


                    <v-flex xs12 sm8 md8>
                      <v-text-field label="Entidade" readonly v-model="entidade.nome" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm4 md4>
                      <v-text-field label="CNPJ" readonly v-model="entidade.cnpj" :mask="'##.###.###/####-##'" :masked="true" hide-details></v-text-field>
                    </v-flex>

                    <v-flex xs12 sm6 md6>
                      <v-text-field label="Telefone" readonly v-model="entidade.telefone" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                      <v-text-field label="Email" readonly v-model="entidade.email" hide-details></v-text-field>
                    </v-flex>

                    <v-flex xs12 sm6 md6 v-if="entidade.endereco">
                      <v-text-field label="Endereço" readonly v-model="entidade.endereco.logradouro" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm2 md2 v-if="entidade.endereco">
                      <v-text-field label="Número" readonly v-model="entidade.endereco.numero" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm4 md4 v-if="entidade.endereco">
                      <v-text-field label="Complemento" readonly v-model="entidade.endereco.complemento" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm4 md4 v-if="entidade.endereco">
                      <v-text-field label="Bairro" readonly v-model="entidade.endereco.bairro" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm2 md2 v-if="entidade.endereco">
                      <v-text-field label="CEP" readonly v-model="entidade.endereco.cep" :mask="'#####-###'" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm4 md4 v-if="entidade.endereco">
                      <v-text-field label="Cidade" readonly v-model="entidade.endereco.cidade" hide-details></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm2 md2 v-if="entidade.endereco">
                      <v-text-field label="UF" readonly v-model="entidade.endereco.uf" hide-details></v-text-field>
                    </v-flex>
                  </v-layout>    
                </v-tab-item>
              </v-tabs>
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
            <v-btn small dark color="cyan darken-4" @click.native="showSelecionarDataEncerramento(hospedagem.id, hospedagem.dataPrevistaSaida)" v-if="hospedagem.dataEfetivaSaida == null">
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

import DialogoSelecaoDataEncerramento from "./DialogoSelecaoDataEncerramento.vue"
import DialogoSelecaoDataRenovacao from "./DialogoSelecaoDataRenovacao.vue"
import DialogoSelecaoDataBaixa from "./DialogoSelecaoDataBaixa.vue"
import DialogoSelecaoLeitoTransferencia from "./DialogoSelecaoLeitoTransferencia.vue"
import DialogoConfirmacao from '../config/DialogoConfirmacao.vue'

export default {
  name: 'HospedagemInfo',
  
  components: {
    DialogoSelecaoDataEncerramento,
    DialogoSelecaoDataBaixa,
    DialogoSelecaoDataRenovacao,
    DialogoSelecaoLeitoTransferencia,
    DialogoConfirmacao
  },

  props: {
    
  },

  data: () =>({
    titulo : "Incluir/Alterar Destinação de Hospedagem",
    descricaoItemExclusao : "???",

    tabActive : 0,

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

  computed: {
    xis(){
      return "teste"
    }
  },

  methods: {
    openDialog(hospedagemId){
      this.reset()
      this.hospedagemId = hospedagemId
      this.getInfo(hospedagemId)
      this.dialogVisible = true
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

    showSelecionarDataEncerramento(hospedagemId, dataPrevistaSaida){
      this.$refs.dlgSelecaoDataEncerramento.openDialog(hospedagemId, dataPrevistaSaida);
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
          //this.dialogVisible = false
        })
    },

    // Baixa
    showSelecionarDataBaixa(hospedeId, dataPrevistaSaida){
      this.$refs.dlgSelecaoDataBaixa.openDialog(hospedeId, dataPrevistaSaida);
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
      
      petra.axiosPost("/app/hospedagem/mapa/baixar", dados)
        .then(response => {
          //this.$emit('baixada',hospedeId)
          //this.dialogVisible = false
        }).catch(error => {
          this.errors = petra.tratarErros(error);
          //this.dialogVisible = false
        })
    },


    // Renovação
    showSelecionarDataRenovacao(){
      this.$refs.dlgSelecaoDataRenovacao.openDialog(this.hospedagem.id, this.hospedagem.dataPrevistaSaida);
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
      
      petra.axiosPost("/app/hospedagem/mapa/renovacao", dados)
        .then(response => {
          this.$emit('close',true)
          this.dialogVisible = false
        }).catch(error => {
          this.errors = petra.tratarErros(error);
        })
    },

    // Transferencia
    showTransferencia(hospede, destinacaoHospedagemId){
      this.$refs.dlgSelecaoLeitoTransferencia.openDialog(hospede, destinacaoHospedagemId);
    },

    onCloseTransferencia(sucesso){
      if (sucesso){
        this.getInfo(this.hospedagemId)
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
      this.$refs.dlgExclusao.openDialog("Deseja realmente excluir esta Hospedagem?")
    },

    onDeleteConfirmed(evt) {
      petra.axiosDelete("/app/hospedagem/"+this.hospedagemId)
        .then(response => {
          petra.showMessageSuccess('Hospedagem excluída com sucesso')
          this.$emit('close',true)
          this.dialogVisible = false
        })
        .catch(error => {
        })
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