<template>
  <div>
    <dialogo-selecao-data-encerramento ref="dlgSelecaoDataEncerramento" @close="onSelecionarDataEncerramento"></dialogo-selecao-data-encerramento>
    
    <v-dialog v-model="dialogVisible" width="800">
        <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          Informações de Hospedagem
        </v-card-title>
        <v-card-text style="height:450px;">
          <v-flex xs10 sm12 md12>
            <div>
              <div><span class="headline text-uppercase">Hospedagem #{{hospedagemId}}</span></div>
            </div>
            <v-divider></v-divider>
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
                  <v-expansion-panel-content v-for="(hpd, i) in hospedagem.hospedes" :key="i" expand>
                    <div slot="header"><span class="text-uppercase title">{{hpd.pessoa.nome}}</span> - {{hpd.tipoHospede.descricao}}</div>  
                    <v-card>
                      <v-card-text class="grey lighten-3">
                      <v-layout row wrap>
                        <v-flex xs12 sm2 md2>
                          <span class="caption">Nascimento:</span>
                        </v-flex>
                        <v-flex xs12 sm10 md10>
                          {{formatDate(hpd.pessoa.dataNascimento)}}
                        </v-flex>

                        <v-flex xs12 sm2 md2 class="caption">
                          Endereço:
                        </v-flex>
                        <v-flex xs12 sm2 md10>
                          {{hpd.pessoa.endereco != null ? hpd.pessoa.endereco.descricao : ''}}
                        </v-flex>

                        <v-flex xs12 sm12 md12 v-if="hospedagem.tipoUtilizacao == 'T'">
                          <v-list>
                            <v-list-tile v-for="leito in hpd.leitos" :key="leito.id">
                              <v-list-tile-content>
                                {{formatDate(leito.dataEntrada)}} - Quarto {{leito.quartoNumero}} Leito {{leito.leitoNumero}}
                              </v-list-tile-content>
                            </v-list-tile>
                          </v-list>
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
                  <v-flex xs12 sm8 md8>
                    <v-text-field label="Nome" readonly ref="edtNome" v-model="entidade.nome"></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm4 md4>
                    <v-text-field label="CNPJ" readonly v-model="entidade.cnpj" :mask="'##.###.###/####-##'" :masked="true" ></v-text-field>
                  </v-flex>

                  <v-flex xs12 sm6 md6>
                    <v-text-field label="Telefone" readonly v-model="entidade.telefone" ></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm6 md6>
                    <v-text-field label="Email" readonly v-model="entidade.email"></v-text-field>
                  </v-flex>

                  <v-flex xs12 sm6 md6 v-if="entidade.endereco">
                    <v-text-field label="Endereço" readonly v-model="entidade.endereco.logradouro"></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm2 md2 v-if="entidade.endereco">
                    <v-text-field label="Número" readonly v-model="entidade.endereco.numero"></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm4 md4 v-if="entidade.endereco">
                    <v-text-field label="Complemento" readonly v-model="entidade.endereco.complemento"></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm4 md4 v-if="entidade.endereco">
                    <v-text-field label="Bairro" readonly v-model="entidade.endereco.bairro"></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm2 md2 v-if="entidade.endereco">
                    <v-text-field label="CEP" readonly v-model="entidade.endereco.cep" :mask="'#####-###'"></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm4 md4 v-if="entidade.endereco">
                    <v-text-field label="Cidade" readonly v-model="entidade.endereco.cidade"></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm2 md2 v-if="entidade.endereco">
                    <v-text-field label="UF" readonly v-model="entidade.endereco.uf"></v-text-field>
                  </v-flex>
                </v-layout>    
              </v-tab-item>
            </v-tabs>
          </v-flex>    
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small dark color="cyan darken-4" @click.native="showSelecionarDataEncerramento(hospedagem.id, hospedagem.dataPrevistaSaida)" v-if="hospedagem.dataEfetivaSaida == null">
            Encerrar
          </v-btn>
          <v-btn small dark color="cyan darken-4" @click.native="encerrar" v-if="hospedagem.dataEfetivaSaida == null">
            Renovar
          </v-btn>
          <v-btn small dark color="cyan darken-4" @click.native="encerrar" v-if="hospedagem.dataEfetivaSaida == null">
            Transferir
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

export default {
  name: 'HospedagemInfo',
  
  components: {
    DialogoSelecaoDataEncerramento
  },

  props: {
    
  },

  data: () =>({
    titulo : "Incluir/Alterar Destinação de Hospedagem",
    tabActive : 0,

    hospedagemId: 0,
    tipoHospede:{},
    destinacaoHospedagem:{},
    hospede: {},
    hospedes: [],
    servicos: [],
    hospedagem:{},
    entidade:{},
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

    getInfo(hospedagemId) {
      var dados = {
        hospedagemId : hospedagemId
      }

      petra.axiosPost("/app/hospedagem/mapa/hospedagem_info", dados)
        .then(response => { 
            console.log("RETORNOU ", response.data)
            this.hospedagem = response.data
            this.entidade = (this.hospedagem && this.hospedagem.entidade) ? this.hospedagem.entidade : null
            this.hospedes = this.hospedagem.hospedes
            this.servicos = (this.hospedagem && this.hospedagem.servicos) ? this.hospedagem.servicos : []
            this.tipoHospede = this.hospedagem.hospedes[0].tipoHospede
            this.destinacaoHospedagem = this.hospedagem.destinacaoHospedagem
          }).catch(error => {
            this.errors = petra.tratarErros(error);
            //console.log("ERROS = ", this.errors)
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
          //erro
        })
    },


    close(value){
      this.dialogVisible = false
      this.$emit('close',false)
    },

    encerrar(evt) {
      this.$emit('close',true)          
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