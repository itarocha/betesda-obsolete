<template>
  <div>
    <el-container>
      <el-main style="padding:5px; line-height:1.5em; height:350px;" v-if="hospedagem != null">
        <!-- TODO: Componentizar esse resumo de hospedagem. Isso é utilizado em FrameHospedagem -->

        <el-row :gutter="10">
          <el-col :span="6">Código</el-col>
          <el-col :span="6" class="font-weight-bold">{{hospedagem.id}}</el-col>

          <el-col :span="6">Data de Entrada</el-col>
          <el-col :span="6" class="font-weight-bold">{{formatDate(hospedagem.dataEntrada)}}</el-col>
        </el-row>

        <el-row :gutter="10">
          <el-col :span="6">Data Prevista de Saída</el-col>
          <el-col :span="6" class="font-weight-bold">{{formatDate(hospedagem.dataPrevistaSaida)}}</el-col>
          <el-col :span="6">Data Efetiva de Saída</el-col>
          <el-col :span="6" class="font-weight-bold">{{formatDate(hospedagem.dataEfetivaSaida)}}</el-col>
        </el-row>

        <el-row :gutter="10">
          <el-col :span="6">Destinação de Hospedagem</el-col>
          <el-col :span="6" class="font-weight-bold">{{hospedagem.destinacaoHospedagem.descricao}}</el-col>
          <el-col :span="6">Tipo de Utilização</el-col>
          <el-col :span="6" class="font-weight-bold">{{tipoUtilizacao(hospedagem.tipoUtilizacao)}}</el-col>
        </el-row>

        <el-row :gutter="10" style="margin-top:40px;" v-if="state == 'browse'">
          <el-col :span="6">Hóspede</el-col>
          <el-col :span="18" class="font-weight-bold">
            <el-button type="primary" size="mini" @click.native="state = 'hospede'">Selecionar</el-button>
          </el-col>
        </el-row>

        <el-row :gutter="10" style="margin-top:40px;" v-if="state == 'browse'">
          <el-col :span="6">Leito</el-col>
          <el-col :span="18" class="font-weight-bold">
            <el-button type="primary" size="mini" @click.native="showSelecionarLeito">Selecionar</el-button>
          </el-col>
        </el-row>

        <el-row :gutter="10" style="margin-top:10px;" v-if="state == 'hospede'">
          <el-container>
            <h4>SELECIONAR HÓSPEDE</h4>
            <el-main style="padding:5px; line-height:1.5em;">
            <!--  
            <el-row style="margin-bottom:10px; height:225px;">
              <selecao-leito ref="frameSelecaoLeito" :config="configTransferencia" :height="200" @onSelecionar="onSelecionarLeito"></selecao-leito>
            </el-row>
            -->
            </el-main>
            
            <el-footer style="padding-left:0px; padding-bottom:0px;" height="20">
              <el-button size="mini" @click="state = 'browse'">Cancelar</el-button>
              <el-button size="mini" type="primary" @click="state = 'browse'">Confirmar</el-button>
            </el-footer>

          </el-container>
        </el-row>

        <el-row :gutter="10" style="margin-top:10px;" v-if="state == 'leito'" >
          <el-container>
            <el-main style="padding:5px; line-height:1.5em;">
              <el-row style="margin-bottom:10px; height:220px;">
                <selecao-leito ref="frameSelecaoLeito" :config="configTransferencia" :height="130" @onSelecionar="onSelecionarLeito"></selecao-leito>
              </el-row>
            </el-main>
            <el-footer style="padding-left:0px; height:18px;">
              <el-button size="mini" @click="state = 'browse'">Cancelar</el-button>
              <el-button size="mini" type="primary" @click="state = 'browse'">Confirmar</el-button>
            </el-footer>
          </el-container>
        </el-row>

        <!--
        <el-row :gutter="10">
          <el-col :span="8">Situação</el-col>
          <el-col :span="6" class="font-weight-bold">{{hospedagem.status}}</el-col>
        </el-row>
        -->

        <!--
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
        -->
      </el-main>
    </el-container>


  </div>
</template>

<script>

import ListagemErros from "../../components/ListagemErros"
import SelecaoLeito from "./SelecaoLeito.vue"

export default {
  name: 'FrameAcrescentarHospede',
  
  props:['config'],
  
  components: {
    ListagemErros,
    SelecaoLeito,
  },

  computed: {
  },

  watch: {
    config:{
      deep: true,
      handler() {
        this.show()
      }
    }
  },


  data: () =>({
    // pode ser browse, select-pessoa, select-leito
    state : 'browse', 
    
    hospedagemId : 0,
    hospedagem : null,

    configTransferencia : {
      hospede : null,
      destinacaoHospedagemId : null,
      dataEntrada : null,
      dataPrevistaSaida : null
    },

    errors: [],
  }),

  created(){
  },

  mounted(){
    console.log("mounted FrameAcrescentarHospede.config",this.config)
    this.show()
  },

  methods: {

    show(){
      if (this.config){
          this.hospedagemId = this.config.hospedagemId
        } else {
          this.hospedagemId = 0
        }
        this.getInfo(this.hospedagemId)
    },

    showSelecionarLeito(){
      this.configTransferencia = {
        hospede : null,
        destinacaoHospedagemId : this.hospedagem.destinacaoHospedagem.id,
        dataEntrada : this.hospedagem.dataEntrada,
        dataPrevistaSaida : this.hospedagem.dataPrevistaSaida
      },

      this.state = 'leito'
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
            /*
            this.encaminhador = (this.hospedagem && this.hospedagem.encaminhador) ? this.hospedagem.encaminhador : null
            this.hospedes = this.hospedagem.hospedes
            this.servicos = (this.hospedagem && this.hospedagem.servicos) ? this.hospedagem.servicos : []
            this.tipoHospede = this.hospedagem.hospedes[0].tipoHospede
            this.destinacaoHospedagem = this.hospedagem.destinacaoHospedagem
            this.$emit('refresh',this.hospedagem)
            */
          }).catch(error => {
            this.errors = petra.tratarErros(error);
          })
    },

    onSelecionarLeito(acomodacao){
      console.log(acomodacao)
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