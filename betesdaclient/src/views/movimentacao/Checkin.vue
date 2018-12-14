<template>
  <!-- https://www.npmjs.com/package/vue-moment -->
  <div>
    <v-layout row wrap>
      <v-flex xs12 xm12 md12>
        <v-btn dark small color="cyan darken-4" @click="incluirHospedagem" v-if="estado == 'vendo'">
            Incluir
        </v-btn>
        <v-btn dark small color="cyan darken-4" @click="resetHospedagem" v-if="estado != 'vendo'">
            Cancelar
        </v-btn>
        <v-btn dark small color="cyan darken-4" @click="gravarHospedagem" v-if="estado != 'vendo'">
            Gravar
        </v-btn>
      </v-flex>
    </v-layout>

    <div v-if="dialogIncluirHospedagem">
      <v-container fluid grid-list-lg>

          <v-layout row wrap class="ml-2 mr-2">
            <v-flex xs12 xm12 md12>
              <v-flex xs12 sm12 md12 v-if="errors">
                <v-alert :value="true" type="error" v-for="(item, i)  in errors" :key="i">
                  {{item.errorMessage}}
                </v-alert>
              </v-flex>
            </v-flex>

            <v-flex xs6 sm6 md6>
              <dialogo-selecao-pessoa ref="dlgSelecaoPessoa" @selecionar="onSelecionarPessoa" @close="onCloseSelecionarPessoa"></dialogo-selecao-pessoa>
              <dialogo-selecao-leito ref="dlgSelecaoLeito" :nome="nomePessoaSelecionada" @close="onSelecionarLeito"></dialogo-selecao-leito>
              <pessoa-edit ref="dlgPessoaEdit" @save="onUpdatePessoa"></pessoa-edit>
              <dialogo-selecao-tipo-hospede ref="dlgSelTipoHospede" :valores="itensSelecaoTipoHospede" @close="onCloseSelecaoTipoHospede"></dialogo-selecao-tipo-hospede>

              <v-card class="elevation-0">
                <v-toolbar card height="45px" class="grey lighten-2">
                  Opções
                </v-toolbar> 

                <!-- EXPLODE -->
                <v-card-text >
                    <v-layout wrap>

                      <v-flex xs12 sm6 md6>
                        <v-menu
                            ref="menu1"
                            :close-on-content-click="false"
                            v-model="menu1"
                            :nudge-right="40"
                            lazy
                            transition="scale-transition"
                            offset-y
                            full-width
                            max-width="290px"
                            min-width="290px"
                          >
                            <v-text-field
                              slot="activator"
                              ref="edtDataEntrada"
                              v-model="formOpcoes.dataEntradaFmt"
                              label="Data de Entrada"
                              persistent-hint
                              prepend-icon="event"
                              v-mask="'##/##/####'"
                              @blur="dataEntradaBlur"
                            ></v-text-field>
                            <v-date-picker v-model="dataEntrada" locale="pt-br" no-title @input="menu1 = false"></v-date-picker>
                        </v-menu>
                      </v-flex>

                      <v-flex xs12 sm6 md6>
                        <v-menu
                            ref="menu2"
                            :close-on-content-click="false"
                            v-model="menu2"
                            :nudge-right="40"
                            lazy
                            transition="scale-transition"
                            offset-y
                            full-width
                            max-width="290px"
                            min-width="290px">
                            <v-text-field
                              slot="activator"
                              ref="edtDataPrevistaSaida"
                              v-model="formOpcoes.dataPrevistaSaidaFmt"
                              label="Data Prevista de Saída"
                              persistent-hint
                              prepend-icon="event"
                              v-mask="'##/##/####'"
                              @blur="dataPrevistaSaidaBlur">
                            </v-text-field>
                            <v-date-picker v-model="dataPrevistaSaida" locale="pt-br" no-title @input="menu2 = false"></v-date-picker>
                          </v-menu>
                      </v-flex>

                      <v-flex xs12 sm6 md6>
                        <v-select label="Destinação de Hospedagem" :items="itensDestinacaoHospedagem" v-model="formOpcoes.destinacaoHospedagem"></v-select>
                      </v-flex>

                      <v-flex xs12 sm6 md6>
                        <v-select label="Tipo de Utilização" :items="itensUtilizacao" v-model="formOpcoes.tipoUtilizacao"></v-select>
                      </v-flex>

                      <v-flex xs12 sm12 md12 v-if="(formOpcoes.tipoUtilizacao == 'P')" >
                        <v-select
                          :items="itensTipoServico"
                          label="Serviços"
                          multiple
                          chips
                          v-model="formOpcoes.servicos"
                        ></v-select>
                      </v-flex>

                    </v-layout>
                </v-card-text>
              </v-card>
            </v-flex>

            <v-flex xs6 sm6 md6>
              <v-card class="elevation-0">
                <v-toolbar card height="45px" class="grey lighten-2">
                  Hóspedes
                  <v-spacer></v-spacer>
                  <v-btn dark small color="cyan darken-4" @click="adicionarPessoa">
                    Adicionar
                  </v-btn>
                </v-toolbar>

                <v-flex xs12 sm12 md12 v-for="(item, i)  in hospedes" :key="i" v-if="show">
                  <v-card color="amber lighten-4">
                    <v-card-title primary-title>
                      <div>
                        <h3>{{item.pessoa.nome}}</h3>
                        <div class="body-1">{{item.tipoHospede.descricao}}</div>
                        <h4 v-if="item.acomodacao != null">Quarto: {{item.acomodacao.quarto.numero}} Leito: {{item.acomodacao.leito.numero}}</h4>
                        <div v-if="(formOpcoes.tipoUtilizacao == 'T') && (item.acomodacao == null)" class="red lighten-4">Selecione o Leito</div> 
                      </div>
                    </v-card-title>

                    <v-divider light></v-divider>
                    <v-card-actions class="grey lighten-4">
                      <v-spacer></v-spacer>
      
                      <v-tooltip bottom>
                        <v-btn icon slot="activator" @click="editarHospede(item)" >
                          <v-icon>edit</v-icon>
                        </v-btn>
                        <span>Conferir/Editar dados do Hóspede</span>
                      </v-tooltip>

                      <v-tooltip bottom >
                        <v-btn icon slot="activator" @click="selecionarTipoHospede(item)" >
                          <v-icon>fa-users</v-icon>
                        </v-btn>
                        <span>Alterar Tipo de Hóspede</span>
                      </v-tooltip>

                      <v-tooltip bottom v-if="formOpcoes.tipoUtilizacao == 'T'">
                        <v-btn icon slot="activator" @click="selecionarLeito(item)" >
                          <v-icon>fa-bed</v-icon>
                        </v-btn>
                        <span>Selecionar Leito</span>
                      </v-tooltip>

                      <v-tooltip bottom>
                        <v-btn icon slot="activator" @click="removerHospede(item)">
                          <v-icon>cancel</v-icon>
                        </v-btn>
                        <span>Remover Hóspede</span>
                      </v-tooltip>
                    </v-card-actions>
                  </v-card>
                </v-flex>

              </v-card>
            </v-flex>
          </v-layout>
      </v-container>
    </div>

    <div v-if="(!dialogIncluirHospedagem) && (hospedagemGravada.id > 0)">
      <v-container grid-list-md>
          <v-layout row wrap>
            <v-flex xs12 sm2 md6 class="title">
              Hospedagem Gravada com sucesso! Id=#{{hospedagemGravada.id}}
            </v-flex>
              
            <v-flex xs12 xm12 md12>
                <v-tabs v-model="tabActive" slider-color="cyan darken-2" fixed-tabs>
                  <v-tab>Hospedagem</v-tab>
                  <v-tab>Hóspede(s)</v-tab>
                  <v-tab>Encaminhador</v-tab>

                  <v-tab-item>
                    <v-layout justify-space-between row wrap fill-height>
                      <v-flex  xs12 sm12 md12><div style="height:50px;"></div></v-flex>

                      <v-flex  xs12 sm2 md4 offset-md2>
                        Data de Entrada:
                      </v-flex>
                      <v-flex xs12 sm2 md6 class="title">
                        {{dma(hospedagemGravada.dataEntrada)}}
                      </v-flex>

                      <v-flex xs12 sm2 md4 offset-md2>
                        Data Prevista de Saída:
                      </v-flex>
                      <v-flex md6 class="title">
                        {{dma(hospedagemGravada.dataPrevistaSaida)}}
                      </v-flex>


                      <v-flex xs12 sm2 md4 offset-md2>
                        Data de Saída:
                      </v-flex>
                      <v-flex xs12 sm2 md6 class="title">
                        {{dma(hospedagemGravada.dataEfetivaSaida)}}
                      </v-flex>

                      <v-flex xs12 sm2 md4 offset-md2>
                        Destinação de Hospedagem:
                      </v-flex>
                      <v-flex xs12 sm2 md6 class="title">
                        {{hospedagemGravada.destinacaoHospedagemDescricao}}
                      </v-flex>

                      <v-flex xs12 sm2 md4 offset-md2>
                        Tipo de Utilização:
                      </v-flex>
                      <v-flex xs12 sm2 md6 class="title">
                        {{traduzTipoUtilizacao(hospedagemGravada.tipoUtilizacao)}}
                      </v-flex>
                      <!--                      
                      <v-flex xs12 sm2 md4 offset-md2>
                        Situação:
                      </v-flex>
                      <v-flex xs12 sm2 md6 class="text-uppercase title">
                        {{hospedagemGravada.status}}
                      </v-flex>
                      -->

                    </v-layout>
                  </v-tab-item>

                  <v-tab-item>

                      <v-expansion-panel>
                        <v-expansion-panel-content v-for="(hpd, i) in hospedagemGravada.hospedes" :key="i" expand>
                          <div slot="header"><span class="text-uppercase title">{{hpd.pessoaNome}}</span> - {{hpd.tipoHospedeDescricao}}</div>  
                          <v-card>
                            <v-card-text class="grey lighten-3">
                            <v-layout row wrap>
                              <v-flex xs12 sm2 md2>
                                <span class="caption">Nascimento:</span>
                              </v-flex>
                              <v-flex xs12 sm10 md10>
                                {{dma(hpd.pessoaDataNascimento)}}
                              </v-flex>
                              <!--
                              <v-flex xs12 sm2 md2 class="caption">
                                Endereço:
                              </v-flex>
                              <v-flex xs12 sm2 md10>
                                {{hpd.pessoa.endereco.descricao}}
                              </v-flex>
                              <v-flex xs12 sm12 md12 v-if="hospedagem.tipoUtilizacao == 'T'">
                                <v-list>
                                  <v-list-tile v-for="leito in hpd.leitos" :key="leito.id">
                                    <v-list-tile-content>
                                      {{dma(leito.dataEntrada)}} - Quarto {{leito.quartoNumero}} Leito {{leito.leitoNumero}}
                                    </v-list-tile-content>
                                  </v-list-tile>
                                </v-list>
                              </v-flex>  
                              -->
                            </v-layout>
                            </v-card-text>
                          </v-card>
                        </v-expansion-panel-content>
                      </v-expansion-panel>

                  </v-tab-item>

                  <v-tab-item>
                  </v-tab-item>
                </v-tabs>

            </v-flex>
          </v-layout>
      </v-container>
    </div>        


  </div>
</template>
<script>

import Titulo from "../../components/Titulo.vue"
import DialogoSelecaoPessoa from "./DialogoSelecaoPessoa.vue"
import DialogoSelecaoLeito from "./DialogoSelecaoLeito.vue"
import DialogoSelecaoTipoHospede from "./DialogoSelecaoTipoHospede.vue"
import PessoaEdit from "./PessoaEdit.vue"
import {mask} from "vue-the-mask"

var moment = require('moment')
moment.locale('pt-br');

export default {
  name: 'Checkin',

  directives: {
    mask
  },

  components :{
    Titulo,
    DialogoSelecaoPessoa,
    DialogoSelecaoLeito,
    DialogoSelecaoTipoHospede,
    PessoaEdit
  },

  data: () =>({
    estado : 'vendo', 
    show : true,
    dialogIncluirHospedagem : false,
    hospedeSelecionado : null,
    dados: [],
    errors: [],
    dataEntrada : null,
    dataPrevistaSaida : null,
    formOpcoes : {
      dataEntrada: null,    
      dataEntradaFmt: null,    
      dataPrevistaSaida: null,
      dataPrevistaSaidaFmt: null,
      destinacaoHospedagem: null,
      tipoUtilizacao: null,
      servicos: []
    },

    hospedes: [],

    itensDestinacaoHospedagem : [],
    itensTipoHospede : [],
    itensUtilizacao: [{ text: "Total", value: "T" }, { text: "Parcial", value: "P" }],
    nomePessoaSelecionada : "",

    tabActive : null,
    hospedagemGravada : {},

    menu1: false,
    menu2: false,    
    stepA : false,
    e1: 0,
  }),

  computed: {

    destinacaoHospedagem(){
      return this.formOpcoes.destinacaoHospedagem
    },

    tipoUtilizacao(){
      return this.formOpcoes.tipoUtilizacao
    },

    itensSelecaoTipoHospede(){
      return this.itensTipoHospede
    },

    servicos(){
      return this.formOpcoes.servicos
    },

  },

  watch : {
    destinacaoHospedagem(){
      //this.loadQuartosPorTipoUtilizacao(this.formOpcoes.destinacaoHospedagem)
      this.clearLeitos()
    },

    tipoUtilizacao(value){
      if (value == "P"){
        this.clearLeitos()
      }
    },

    servicos(){
    },

    dataEntrada (val) {
      this.formOpcoes.dataEntradaFmt = this.formatDate(this.dataEntrada)
    },    

    dataPrevistaSaida (val) {
      this.formOpcoes.dataPrevistaSaidaFmt = this.formatDate(this.dataPrevistaSaida)
    },    

    estado(value){
      if (value == 'incluindo'){
        this.focus()
      }
    }
  },

  created(){
    this.reset()
    this.focus()
    this.loadListas()
  },

  mounted(){
    this.$store.dispatch('setAcao','Checkin')
  },

  methods: {
    reset(){
      this.formOpcoes = {
        dataEntrada: null,    
        dataEntradaFmt: null,    
        dataPrevistaSaida: null,
        dataPrevistaSaidaFmt: null,
        destinacaoHospedagem: null,
        tipoUtilizacao: null,
        servicos: []
      },
      this.hospedes = []
      this.errors = []
      this.hospedagemGravada = {}
    },

    focus(){
      if (this.dialogIncluirHospedagem) {
        setTimeout(() => {
          this.$refs.edtDataEntrada.focus()
        }, 500)
      }
    },

    dataEntradaBlur(){
      this.dataEntrada = this.parseDate(this.formOpcoes.dataEntradaFmt)
    },

    dataPrevistaSaidaBlur(){
      this.dataPrevistaSaida = this.parseDate(this.formOpcoes.dataPrevistaSaidaFmt)
    },

    dma(dia) {
      return dia ? moment(dia).format("DD/MM/YYYY") : "---"
    },

    traduzTipoUtilizacao(tipo){
      return tipo == "T" ? "Total" : "Parcial"
    },

    formatDate(date) {
      if (!date) return null

      const [year, month, day] = date.split('-')
      return `${day}/${month}/${year}`
    },
    
    parseDate(date) {
      if (!date) return null

      const [day, month, year] = date.split('/')
      var retorno = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
      return retorno
    },    

    incluirHospedagem() {
      this.dialogIncluirHospedagem = true
      this.estado = 'incluindo'
    },

    resetHospedagem() {
      this.dialogIncluirHospedagem = false
      this.estado = 'vendo'
      this.reset()
    },

    gravarHospedagem() {
      
      this.postarHospedagem()

      //this.dialogIncluirHospedagem = false
      //this.estado = 'vendo'
    },

    postarHospedagem() {
      this.errors = []

      var toSave = {
        dataEntrada : this.dataEntrada,
        dataPrevistaSaida : this.dataPrevistaSaida,
        destinacaoHospedagemId : this.formOpcoes.destinacaoHospedagem,
        tipoUtilizacao : this.formOpcoes.tipoUtilizacao,
        servicos : [],
        hospedes : []

      }
      if (this.formOpcoes.tipoUtilizacao == "P") {
        toSave.servicos = this.formOpcoes.servicos
      }
      for (var i = 0; i < this.hospedes.length; ++i){
        var hospede = {
          pessoaId : this.hospedes[i].pessoa.id,
          pessoaNome : this.hospedes[i].pessoa.nome,
          tipoHospedeId : this.hospedes[i].tipoHospede.id,
        }

        if ((this.formOpcoes.tipoUtilizacao == "T") && (this.hospedes[i].acomodacao != null)){ 
          hospede.acomodacao = {
            quartoId : this.hospedes[i].acomodacao.quarto.id,
            quartoNumero : this.hospedes[i].acomodacao.quarto.numero,
            leitoId : this.hospedes[i].acomodacao.leito.id,
            leitoNumero : this.hospedes[i].acomodacao.leito.numero,
          }
        }
        toSave.hospedes.push(hospede)
      }

      let uri = petra.base_uri+"/app/hospedagem";
      axios.post(uri, toSave)
          .then(response => { 
            console.log("RETORNO DA HOSPEDAGEM: ", response.data)
            this.errors = []
            this.resetHospedagem()
            this.showHospedagemGravada(response.data)
          }).catch(error => {
            this.errors = this.tratarErros(error);
          });
    },

    tratarErros(error){
        if (error.response) {
          return error.response.data.errors
        }
    },

    showHospedagemGravada(dados) {
      this.hospedagemGravada = dados
    },

    rerender(){
        this.show = false
        this.$nextTick(() => {
          this.show = true;
        })
    },

    loadListas() {
      this.itensDestinacaoHospedagem = [];
      let uri = petra.base_uri + "/app/quarto/listas";
      axios.get(uri).then(response => {
        this.itensDestinacaoHospedagem = response.data.listaDestinacaoHospedagem;
        this.itensTipoLeito = response.data.listaTipoLeito;
        this.itensSituacaoLeito = response.data.listaSituacaoLeito;
        this.itensTipoServico = response.data.listaTipoServico;
        this.itensTipoHospede = response.data.listaTipoHospede;
      });
    },

    clearLeitos(){
      for (var i = 0; i < this.hospedes.length; i++) {
        this.hospedes[i].acomodacao = null
      }      
      this.rerender()
    },

    adicionarPessoa() {
      this.$refs.dlgSelecaoPessoa.openDialog()
    },

    selecionarLeito(hospede) {
      this.selecionarAcomodacao(hospede)
      this.nomePessoaSelecionada = hospede.nome
      this.$refs.dlgSelecaoLeito.openDialog(hospede, this.formOpcoes.destinacaoHospedagem)
    },

    selecionarTipoHospede(hospede) {
      this.hospedeSelecionado = hospede
      this.$refs.dlgSelTipoHospede.openDialog(hospede.pessoa.nome)
    },

    onCloseSelecaoTipoHospede(tipoHospede){
      if (tipoHospede) {
        if (this.hospedeSelecionado != null){
          this.hospedeSelecionado.tipoHospede = tipoHospede
          this.rerender()
        }
      }
    },

    editarHospede(hospede) {
      console.log("editarHospede ", hospede.pessoa)
      this.$refs.dlgPessoaEdit.openDialog(hospede.pessoa)
    },

    onSelecionarPessoa(_pessoa, _tipoHospede){
      if (_pessoa != null) {
        for (var i = 0; i < this.hospedes.length; ++i){
          if (_pessoa.id == this.hospedes[i].pessoa.id){
            return
          }
        }
        var hospede = {
          pessoa : _pessoa,
          tipoHospede : _tipoHospede,
          acomodacao : null // {quarto, leito}
        }
        console.log("Selecionando pessoa: ", _pessoa)
        this.hospedes.push(hospede)
      }
    },

    onUpdatePessoa(_pessoa){
      if (_pessoa != null) {
        for (var i = 0; i < this.hospedes.length; ++i){
          if (_pessoa.id == this.hospedes[i].pessoa.id){
            this.this.hospedes[i].pessoa = _pessoa
            return
          }
        }
      }
    },

    onCloseSelecionarPessoa(){
      this.focus()
    },

    onSelecionarLeito(hospede, acomodacao){
      this.focus()
      if (hospede != null && acomodacao != null){
        for (var i = 0; i < this.hospedes.length; ++i) {
          if (hospede.pessoa.id == this.hospedes[i].pessoa.id){
            this.hospedes[i].acomodacao = acomodacao
            return
          }
        }      
      }
      this.rerender()
    },

    removerHospede(item){
      var indice = this.hospedes.indexOf(item);
      if (indice > -1){
        this.hospedes.splice(indice,1)
      }
    },

    selecionarAcomodacao(hospede){
      this.rerender()
    },

  }
}
</script>
<style scoped>
  .v-stepper__header{
    height: 50px;
  }
  .container.grid-list-md.text-xs-center{
     padding: 10px;
  }
</style>