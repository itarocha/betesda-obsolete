<template>
  <!-- https://www.npmjs.com/package/vue-moment -->
  <div>
    <v-layout row wrap>
      <v-flex xs12 xm12 md12>
        <v-btn dark small color="cyan darken-4" @click="incluirHospedagem" v-if="estado == 'vendo'">
            Incluir
        </v-btn>
        <v-btn dark small color="cyan darken-4" @click="resetHospedagem" v-if="estado == 'incluindo'">
            Limpar
        </v-btn>
        <v-btn dark small color="cyan darken-4" @click="gravarHospedagem" v-if="estado == 'incluindo'">
            Lançar Hospedagem
        </v-btn>
        <!--
        <v-btn small color="yellow" @click.stop="testarFrameHospedagem">
            Testar Frame Hospedagem
        </v-btn>
        -->
      </v-flex>
    </v-layout>

    <v-layout row wrap class="ml-2 mr-2" v-if="estado == 'incluindo'">
      <v-flex xs12 sm12 md12 v-if="errors">
        <v-alert :value="true" type="error" v-for="(item, i)  in errors" :key="i">
          {{item.errorMessage}}
        </v-alert>
      </v-flex>

      <v-flex xs6 sm6 md6>
        <dialogo-selecao-pessoa ref="dlgSelecaoPessoa" @selecionar="onSelecionarPessoa" @close="onCloseSelecionarPessoa"></dialogo-selecao-pessoa>
        <dialogo-selecao-leito ref="dlgSelecaoLeito" @close="onSelecionarLeito"></dialogo-selecao-leito>
        <pessoa-edit ref="dlgPessoaEdit" @save="onUpdatePessoa"></pessoa-edit>
        <dialogo-selecao-tipo-hospede ref="dlgSelTipoHospede" :valores="itensSelecaoTipoHospede" @close="onCloseSelecaoTipoHospede"></dialogo-selecao-tipo-hospede>

        <v-card class="elevation-0 pr-2">
          <v-toolbar card height="45px" class="grey lighten-2">Opções</v-toolbar> 

          <v-card-text >
            <v-layout wrap>
              <v-flex xs12 sm12 md12>
                <v-select label="Entidade" ref="edtEntidadeId" :items="itensEntidades" v-model="formOpcoes.entidadeId" hide-details></v-select>
              </v-flex>

              <v-flex xs12 sm12 md12>
                <v-select label="Encaminhador" :items="itensEncaminhadores" v-model="formOpcoes.encaminhadorId" hide-details></v-select>
              </v-flex>

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
                      hide-details
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
                      @blur="dataPrevistaSaidaBlur"
                      hide-details>
                    </v-text-field>
                    <v-date-picker v-model="dataPrevistaSaida" locale="pt-br" no-title @input="menu2 = false"></v-date-picker>
                  </v-menu>
              </v-flex>

              <v-flex xs12 sm6 md6>
                <v-select label="Destinação de Hospedagem" :items="itensDestinacaoHospedagem" v-model="formOpcoes.destinacaoHospedagem" hide-details></v-select>
              </v-flex>

              <v-flex xs12 sm6 md6>
                <v-select label="Tipo de Utilização" :items="itensUtilizacao" v-model="formOpcoes.tipoUtilizacao" hide-details></v-select>
              </v-flex>

              <v-flex xs12 sm12 md12 v-if="(formOpcoes.tipoUtilizacao == 'P')" >
                <v-select :items="itensTipoServico" label="Serviços" multiple chipsv-model="formOpcoes.servicos"></v-select>
              </v-flex>

            </v-layout>
          </v-card-text>
        </v-card>
      </v-flex>

      <v-flex xs6 sm6 md6>
        <v-card class="elevation-0">
          <v-toolbar card height="45px" class="grey lighten-2 mb-2">
            Hóspedes
            <v-spacer></v-spacer>
            <v-btn dark small color="cyan darken-4" @click="adicionarPessoa">
              Incluir Hóspede
            </v-btn>
          </v-toolbar>

          <v-flex xs12 sm12 md12 v-for="(item, i)  in hospedes" :key="i">
            <v-card color="amber lighten-4 mb-2">
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
                  <span>Remover da Hospedagem</span>
                </v-tooltip>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-card>
      </v-flex>
    </v-layout>

    <v-layout row wrap v-if="estado == 'vendo'" class="ml-2 mr-2">
      <v-flex xs12 sm2 md6 class="title">
        Hospedagem Gravada com sucesso! Id=#{{hospedagemGravadaId}}
      </v-flex>
        
      <v-flex xs12 xm12 md12>
        <frame-hospedagem ref="frameHospedagem"></frame-hospedagem>
      </v-flex>
    </v-layout>


  </div>
</template>
<script>

import Titulo from "../../components/Titulo.vue"
import DialogoSelecaoPessoa from "./DialogoSelecaoPessoa.vue"
import DialogoSelecaoLeito from "./DialogoSelecaoLeito.vue"
import DialogoSelecaoTipoHospede from "./DialogoSelecaoTipoHospede.vue"
import PessoaEdit from "../config/PessoaEdit.vue"
import FrameHospedagem from "./FrameHospedagem.vue"
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
    PessoaEdit,
    FrameHospedagem
  },

  data: () =>({
    estado : 'incluindo', 
    show : true,
    hospedeSelecionado : null,
    dados: [],
    errors: [],
    dataEntrada : null,
    dataPrevistaSaida : null,
    formOpcoes : {
      entidadeId: null,
      encaminhadorId: null,
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
    itensEntidades : [],
    itensEncaminhadores : [],
    itensUtilizacao: [{ text: "Total", value: "T" }, { text: "Parcial", value: "P" }],

    tabActive : null,
    hospedagemGravadaId : 0,
    xhospedagem : null,

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

    entidadeId(){
      return this.formOpcoes.entidadeId
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

    entidadeId(newValue, oldValue){
      //console.logconsole.log(`mudou de ${oldValue} para ${newValue}`)
      if (newValue == null){
        this.itensEncaminhadores = []
      } else {
        this.loadEncaminhadores(newValue)
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
    /*
    testarFrameHospedagem(){
      this.showHospedagemGravada(36)  
    },
    */

    showHospedagemGravada(hospedagemId){
      this.hospedagemGravadaId = hospedagemId
      this.estado = 'vendo'

      this.$nextTick(() =>{
        this.$refs.frameHospedagem.open(hospedagemId)
      })
    },

    reset(){
      this.formOpcoes = {
        entidadeId: null,
        encaminhadorId: null,
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
      this.hospedagemGravadaId = 0
      this.itensEncaminhadores = []
      this.itensEntidades = []
    },

    focus(){
      if (this.estado = 'incluindo') {
        setTimeout(() => {
          this.$refs.edtEntidadeId.focus()
        }, 500)
      }
    },

    loadListas() {
      this.itensDestinacaoHospedagem = [];
      petra.axiosGet("/app/quarto/listas",false).then(
        response => {
          this.itensDestinacaoHospedagem = response.data.listaDestinacaoHospedagem
          this.itensTipoLeito = response.data.listaTipoLeito
          this.itensSituacaoLeito = response.data.listaSituacaoLeito
          this.itensTipoServico = response.data.listaTipoServico
          this.itensTipoHospede = response.data.listaTipoHospede
          this.itensEntidades = response.data.listaEntidade
        })
    },

    loadEncaminhadores(id) {
      this.itensEncaminhadores = [];
      var endpoint = "/app/encaminhadores/lista/"+id
      //console.log(endpoint)
      petra.axiosGet("/app/encaminhadores/lista/"+id).then(
        response => {
          this.itensEncaminhadores = response.data
        })
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
      this.estado = 'incluindo'
    },

    resetHospedagem() {
      this.estado = 'incluindo'
      this.reset()
    },

    gravarHospedagem() {
      
      this.postarHospedagem()
       setTimeout(() => {
          this.errors = []
        }, 20000)
    },

    postarHospedagem() {
      this.errors = []

      var toSave = {
        entidadeId : this.formOpcoes.entidadeId,
        encaminhadorId : this.formOpcoes.encaminhadorId,
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

      petra.axiosPost("/app/hospedagem", toSave)
        .then(response => {
            this.errors = []
            this.resetHospedagem()
            this.showHospedagemGravada(response.data.id)
        })
        .catch(error => {
          this.errors = petra.tratarErros(error)
        })
    },

    tratarErros(error){
      if (error.response) {
        return error.response.data.errors
      }
    },

    rerender(){
        this.show = false
        this.$nextTick(() => {
          this.show = true;
        })
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
      this.$refs.dlgSelecaoLeito.openDialog(hospede, this.formOpcoes.destinacaoHospedagem, this.dataEntrada, this.dataPrevistaSaida)
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
      //console.log("editarHospede ", hospede.pessoa)
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
        //console.log("Selecionando pessoa: ", _pessoa)
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

    onSelecionarLeito(selecao){
      this.focus()
      //hospede, acomodacao

      if (selecao != null){
        for (var i = 0; i < this.hospedes.length; ++i) {
          if (selecao.hospede.pessoa.id == this.hospedes[i].pessoa.id){
            this.hospedes[i].acomodacao = {quarto: selecao.quarto, leito: selecao.leito}
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

  .v-text-field__details {
    display: none;
  }

</style>